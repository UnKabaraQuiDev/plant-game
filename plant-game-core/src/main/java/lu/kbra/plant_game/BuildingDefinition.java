package lu.kbra.plant_game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import lu.kbra.pclib.concurrency.CountTriggerLatch;
import lu.kbra.pclib.datastructure.list.WeakList;
import lu.kbra.pclib.pointer.prim.IntPointer;
import lu.kbra.plant_game.base.scene.overlay.group.building.BuildingInfoUIObjectGroup;
import lu.kbra.plant_game.base.scene.overlay.group.building.ResourceLineUIObjectGroup;
import lu.kbra.plant_game.engine.data.locale.Localizable;
import lu.kbra.plant_game.engine.entity.go.GameObject;
import lu.kbra.plant_game.engine.entity.go.impl.PlaceableObject;
import lu.kbra.plant_game.engine.entity.ui.text.ProgrammaticTextUIObject;
import lu.kbra.plant_game.engine.scene.world.GameData;
import lu.kbra.plant_game.engine.scene.world.WorldLevelScene;
import lu.kbra.plant_game.engine.scene.world.data.building.requirement.BuildingRequirement;
import lu.kbra.plant_game.engine.scene.world.data.resource.ResourceType;
import lu.kbra.plant_game.generated.ColorMaterial;

public class BuildingDefinition<T extends GameObject & PlaceableObject>
		implements Consumer<BuildingInfoUIObjectGroup>, Cloneable, Localizable {

	protected final Class<T> clazz;
	protected final String internalName;
	protected final Map<ResourceType, Integer> prices;
	protected final List<BuildingRequirement> unlockRequirements;
	protected final List<BuildingRequirement> buildingRequirements;
	protected final int index;

	public BuildingDefinition(final Class<T> clazz, final String internalName, final Map<ResourceType, Integer> prices,
			final List<BuildingRequirement> unlockRequirements, final List<BuildingRequirement> buildingRequirements, final int index) {
		this.clazz = clazz;
		this.internalName = internalName;
		this.prices = prices;
		this.unlockRequirements = unlockRequirements;
		this.buildingRequirements = buildingRequirements;
		this.index = index;
	}

	public boolean isUnlocked(final GameData gameData, final WorldLevelScene worldLevelScene) {
		return this.unlockRequirements.stream().allMatch(c -> c.isFulfilled(worldLevelScene));
	}

	public boolean canBuild(final GameData gameData, final WorldLevelScene worldLevelScene) {
		return this.getPrices().entrySet().stream().allMatch(e -> gameData.getResources().get(e.getKey()) > e.getValue())
				&& this.buildingRequirements.stream().allMatch(c -> c.isFulfilled(worldLevelScene));
	}

	@Override
	public void accept(final BuildingInfoUIObjectGroup t) {
		final GameData gameData = PGLogic.INSTANCE.getGameData();
		final WorldLevelScene worldScene = PGLogic.INSTANCE.getWorldScene();

		{
			t.getTitle().ifSet(s -> {
				s.setText(this.getLocalizationValue()).flushText();
				s.getTextEmitter().setForegroundColor(this.getTextColorMaterial(gameData, worldScene).getColor());
			});
		}

		{
			final WeakList<ResourceLineUIObjectGroup> resourceLinesList = t.getResourceLines();

			final Set<ResourceType> handled = new HashSet<>();

			resourceLinesList.forEach(b -> {
				final ResourceType type = b.getResourceType();
				if (this.prices.containsKey(type)) {
					b.set(this.prices.get(type)).flushValue();
					b.setActive(true);
					handled.add(type);
				} else {
					b.setActive(false);
				}
			});

			for (final Map.Entry<ResourceType, Integer> entry : this.prices.entrySet()) {
				if (!handled.contains(entry.getKey())) {
					t.addCostIntLine(entry.getKey());
				}
			}
		}

		{
			final WeakList<ProgrammaticTextUIObject> messagesList = t.getMessages();
			if (messagesList.size() < this.unlockRequirements.size() + this.buildingRequirements.size()) {
				final int missing = this.unlockRequirements.size() + this.buildingRequirements.size() - messagesList.size();
				// refresh when done
				final CountTriggerLatch latch = new CountTriggerLatch(missing, () -> this.accept(t));
				IntStream.range(0, missing).forEach(i -> t.addStringLine().latch(latch));
			}

			final List<BuildingRequirement> brs = new ArrayList<>(this.unlockRequirements);
			brs.addAll(this.buildingRequirements);

			final IntPointer ip = new IntPointer(0);
			messagesList.forEach(str -> {
				if (ip.getValue() < brs.size()) {
					final BuildingRequirement br = brs.get(ip.getValue());
					str.setText(br.getLocalizationValue()).flushText();
					str.getTextEmitter()
							.setForegroundColor(
									br.isFulfilled(worldScene) ? ColorMaterial.GREEN.getColor() : ColorMaterial.LIGHT_RED.getColor());
					str.setActive(true);
				} else {
					str.setActive(false);
				}
				ip.increment();
			});
		}

		t.doLayout();
	}

	public ColorMaterial getColorMaterial(final GameData gameData, final WorldLevelScene world) {
		if (this.canBuild(gameData, world)) {
			return ColorMaterial.GREEN;
		}
		if (this.isUnlocked(gameData, world)) {
			return ColorMaterial.ORANGE;
		}
		return ColorMaterial.GRAY;
	}

	public ColorMaterial getTextColorMaterial(final GameData gameData, final WorldLevelScene world) {
		if (this.canBuild(gameData, world)) {
			return ColorMaterial.GREEN;
		}
		if (this.isUnlocked(gameData, world)) {
			return ColorMaterial.ORANGE;
		}
		return ColorMaterial.LIGHT_GRAY;
	}

	@Override
	public String getLocalizationKey() {
		return PlaceableObject.getLocalizableKey(this.clazz);
	}

	public String getInternalName() {
		return this.internalName;
	}

	public String getInternalPath() {
		return this.internalName.replaceAll("[.:]", "/");
	}

	public Class<T> getClazz() {
		return this.clazz;
	}

	public Map<ResourceType, Integer> getPrices() {
		return this.prices;
	}

	public List<BuildingRequirement> getUnlockRequirements() {
		return this.unlockRequirements;
	}

	public List<BuildingRequirement> getBuildingRequirements() {
		return this.buildingRequirements;
	}

	public int getIndex() {
		return this.index;
	}

	@Override
	public BuildingDefinition<T> clone() {
		return new BuildingDefinition<>(this.clazz,
				this.internalName,
				new HashMap<>(this.prices),
				new ArrayList<>(this.unlockRequirements),
				new ArrayList<>(this.buildingRequirements),
				this.index);
	}

	@Override
	public String toString() {
		return "BuildingDefinition@" + System.identityHashCode(this) + " [clazz=" + this.clazz + ", prices=" + this.prices
				+ ", unlockRequirements=" + this.unlockRequirements + ", buildingRequirements=" + this.buildingRequirements + ", index="
				+ this.index + "]";
	}

}
