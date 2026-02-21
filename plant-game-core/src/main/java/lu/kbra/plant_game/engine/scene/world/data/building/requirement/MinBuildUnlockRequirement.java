package lu.kbra.plant_game.engine.scene.world.data.building.requirement;

import java.text.MessageFormat;

import com.fasterxml.jackson.annotation.JsonTypeName;

import lu.kbra.plant_game.engine.data.locale.LocalizationService;
import lu.kbra.plant_game.engine.entity.go.GameObject;
import lu.kbra.plant_game.engine.entity.go.impl.PlaceableObject;
import lu.kbra.plant_game.engine.scene.world.SunLightOwner;
import lu.kbra.plant_game.plugin.registry.BuildingRegistry;

@JsonTypeName("MIN_BUILD")
public class MinBuildUnlockRequirement implements BuildingRequirement {

	private int count;
	private String building;

	public MinBuildUnlockRequirement() {
	}

	public MinBuildUnlockRequirement(final int count, final String building) {
		this.count = count;
		this.building = building;
	}

	public MinBuildUnlockRequirement(final int count, final Class<? extends GameObject> building) {
		this.count = count;
		this.building = BuildingRegistry.getInternalObjectName(building);
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
	public boolean isFulfilled(final SunLightOwner scene) {
		return false;
	}

	@Override
	public String getLocalizationKey() {
		return "req.unlock.min.build";
	}

	@Override
	public String getLocalizationValue() {
		return MessageFormat.format(BuildingRequirement.super.getLocalizationValue(),
				this.count,
				LocalizationService.get(PlaceableObject.LOCALIZATION_KEY + this.building.replace(":", ".")));
	}

}
