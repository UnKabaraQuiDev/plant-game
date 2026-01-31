package lu.kbra.plant_game;

import java.util.List;

import lu.kbra.plant_game.engine.scene.world.data.building.requirement.MinBuildUnlockRequirement;
import lu.kbra.plant_game.generated.ColorMaterial;
import lu.kbra.plant_game.vanilla.entity.go.obj.energy.SolarPanelObject;
import lu.kbra.plant_game.vanilla.entity.go.obj.energy.WaterWheelObject;
import lu.kbra.plant_game.vanilla.entity.go.obj.water.WaterSprinklerObject3x3;
import lu.kbra.plant_game.vanilla.entity.go.obj.water.WaterSprinklerObject5x5;
import lu.kbra.plant_game.vanilla.entity.go.obj.water.WaterSprinklerObject7x7;
import lu.kbra.plant_game.vanilla.entity.go.obj.water.WaterTowerObject;

public class VanillaBuildingDefinitionRegistry extends BuildingDefinitionRegistry {

	public static final BuildingCategory ENERGY = new BuildingCategory("energy", ColorMaterial.YELLOW, 10);
	public static final BuildingCategory WATER = new BuildingCategory("water", ColorMaterial.CYAN, 20);

	public void init() {
		register(ENERGY, new BuildingDefinition<>(SolarPanelObject.class, 45, List.of(), List.of(), 20));
		register(ENERGY, new BuildingDefinition<>(WaterWheelObject.class, 85, List.of(), List.of(), 30));

		register(WATER, new BuildingDefinition<>(WaterTowerObject.class, 60, List.of(), List.of(), 20));
		register(WATER, new BuildingDefinition<>(WaterSprinklerObject3x3.class, 80, List.of(), List.of(), 30));
		register(WATER, new BuildingDefinition<>(WaterSprinklerObject5x5.class, 130,
				List.of(new MinBuildUnlockRequirement(3, WaterSprinklerObject3x3.class)), List.of(), 40));
		register(WATER, new BuildingDefinition<>(WaterSprinklerObject7x7.class, 250,
				List.of(new MinBuildUnlockRequirement(2, WaterSprinklerObject5x5.class)), List.of(), 50));
	}

}
