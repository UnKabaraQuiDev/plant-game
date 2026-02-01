package lu.kbra.plant_game.base.reg;

import java.util.List;

import lu.kbra.plant_game.BuildingCategory;
import lu.kbra.plant_game.BuildingRegistry;
import lu.kbra.plant_game.base.entity.go.obj.energy.SolarPanelSmallObject;
import lu.kbra.plant_game.base.entity.go.obj.energy.WaterWheelObject;
import lu.kbra.plant_game.base.entity.go.obj.energy.WindTurbineMediumObject;
import lu.kbra.plant_game.generated.ColorMaterial;
import lu.kbra.plant_game.plugin.PluginDescriptor;

public class BaseBuildingRegistry extends BuildingRegistry {

	public static final BuildingCategory ENERGY = new BuildingCategory("energy", ColorMaterial.YELLOW, 10);
	public static final BuildingCategory WATER = new BuildingCategory("water", ColorMaterial.CYAN, 20);

	public BaseBuildingRegistry(final PluginDescriptor pluginDescriptor) {
		super(pluginDescriptor);
	}

	@Override
	public void init() {
//		register(ENERGY, new BuildingDefinition<>(SolarPanelMediumObject.class, 45, List.of(), List.of(), 20));
		this.register(ENERGY, SolarPanelSmallObject.class, 40, List.of(), List.of(), 10);
		this.register(ENERGY, WindTurbineMediumObject.class, 125, List.of(), List.of(), 20);
		this.register(ENERGY, WaterWheelObject.class, 125, List.of(), List.of(), 30);

//		this.register(WATER, new BuildingDefinition<>(WaterTowerObject.class, 60, List.of(), List.of(), 20));
//		this.register(WATER, new BuildingDefinition<>(WaterSprinklerObject3x3.class, 80, List.of(), List.of(), 30));
//		this.register(WATER,
//				new BuildingDefinition<>(WaterSprinklerObject5x5.class,
//						130,
//						List.of(new MinBuildUnlockRequirement(3, WaterSprinklerObject3x3.class)),
//						List.of(),
//						40));
//		this.register(WATER,
//				new BuildingDefinition<>(WaterSprinklerObject7x7.class,
//						250,
//						List.of(new MinBuildUnlockRequirement(2, WaterSprinklerObject5x5.class)),
//						List.of(),
//						50));
	}

}
