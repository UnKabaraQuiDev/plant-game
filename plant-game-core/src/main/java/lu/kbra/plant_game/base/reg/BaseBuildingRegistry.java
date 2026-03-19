package lu.kbra.plant_game.base.reg;

import static lu.kbra.plant_game.DefaultBuildingCategories.ENERGY;
import static lu.kbra.plant_game.DefaultBuildingCategories.WATER;

import java.util.List;

import lu.kbra.plant_game.base.entity.go.obj.energy.SolarPanelSmallObject;
import lu.kbra.plant_game.base.entity.go.obj.energy.WaterWheelSmallObject;
import lu.kbra.plant_game.base.entity.go.obj.energy.WindTurbineMediumObject;
import lu.kbra.plant_game.base.entity.go.obj.water.WaterSprinklerObject3x3;
import lu.kbra.plant_game.base.entity.go.obj.water.WaterSprinklerObject5x5;
import lu.kbra.plant_game.base.entity.go.obj.water.WaterSprinklerObject7x7;
import lu.kbra.plant_game.base.entity.go.obj.water.WaterTowerMediumObject;
import lu.kbra.plant_game.engine.scene.world.data.building.requirement.MinBuildUnlockRequirement;
import lu.kbra.plant_game.plugin.PluginDescriptor;
import lu.kbra.plant_game.plugin.registry.BuildingRegistry;

public class BaseBuildingRegistry extends BuildingRegistry {

	public BaseBuildingRegistry(final PluginDescriptor pluginDescriptor) {
		super(pluginDescriptor);
	}

	@Override
	public void register() {
		this.register(ENERGY, SolarPanelSmallObject.class, 40, List.of(), List.of(), 10);
		this.register(ENERGY, WindTurbineMediumObject.class, 125, List.of(), List.of(), 20);
		this.register(ENERGY, WaterWheelSmallObject.class, 125, List.of(), List.of(), 30);

		this.register(WATER, WaterTowerMediumObject.class, 60, List.of(), List.of(), 20);
		this.register(WATER, WaterSprinklerObject3x3.class, 80, List.of(), List.of(), 30);
		this.register(WATER,
				WaterSprinklerObject5x5.class,
				130,
				List.of(new MinBuildUnlockRequirement(3, WaterSprinklerObject3x3.class)),
				List.of(),
				40);
		this.register(WATER,
				WaterSprinklerObject7x7.class,
				250,
				List.of(new MinBuildUnlockRequirement(2, WaterSprinklerObject5x5.class)),
				List.of(),
				50);
	}

}
