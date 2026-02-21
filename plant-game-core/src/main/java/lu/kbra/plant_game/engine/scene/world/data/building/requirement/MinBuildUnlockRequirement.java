package lu.kbra.plant_game.engine.scene.world.data.building.requirement;

import com.fasterxml.jackson.annotation.JsonTypeName;

import lu.kbra.plant_game.BuildingDefinition;
import lu.kbra.plant_game.engine.data.locale.AbstractLocalizationString;
import lu.kbra.plant_game.engine.data.locale.Localizable;
import lu.kbra.plant_game.engine.entity.go.GameObject;
import lu.kbra.plant_game.engine.entity.go.impl.PlaceableObject;
import lu.kbra.plant_game.engine.scene.world.WorldLevelScene;
import lu.kbra.plant_game.plugin.registry.BuildingRegistry;

@JsonTypeName("MIN_BUILD")
public class MinBuildUnlockRequirement extends AbstractLocalizationString implements BuildingRequirement {

	private int count;
	private String building;

	public MinBuildUnlockRequirement() {
	}

	@Deprecated
	public MinBuildUnlockRequirement(final int count, final String building) {
		this.count = count;
		this.building = building;
		this.addParam(Localizable.raw(count));
		this.addParam(Localizable.of(building));
	}

	public MinBuildUnlockRequirement(final int count, final Class<? extends GameObject> building) {
		this.count = count;
		this.building = BuildingRegistry.getInternalName(building);
		this.addParam(Localizable.raw(count));
		this.addParam(PlaceableObject.getLocalizable(building));
	}

	public MinBuildUnlockRequirement(final int count, final BuildingDefinition<?> building) {
		this.count = count;
		this.building = building.getInternalName();
		this.addParam(Localizable.raw(count));
		this.addParam(PlaceableObject.getLocalizable(building.getClazz()));
	}

	public int getCount() {
		return this.count;
	}

	public String getBuilding() {
		return this.building;
	}

	public void setCount(final int count) {
		this.count = count;
	}

	public void setBuilding(final String building) {
		this.building = building;
	}

	@Override
	public boolean isFulfilled(final WorldLevelScene scene) {
		return false;
	}

	@Override
	public String getLocalizationKey() {
		return "req.unlock.min.build";
	}

	@Override
	public String toString() {
		return "MinBuildUnlockRequirement@" + System.identityHashCode(this) + " [count=" + this.count + ", building=" + this.building
				+ ", params=" + this.params + ", computed=" + this.computed + "]";
	}

}
