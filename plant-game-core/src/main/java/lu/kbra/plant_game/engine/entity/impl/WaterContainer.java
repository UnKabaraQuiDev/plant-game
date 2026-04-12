package lu.kbra.plant_game.engine.entity.impl;

import java.util.Map;

import lu.kbra.plant_game.base.data.DefaultResourceType;
import lu.kbra.plant_game.engine.scene.world.data.resource.ResourceType;

public interface WaterContainer extends ResourceContainer {

	@Override
	default ResourceType[] getAllowedResources() {
		return new ResourceType[] { DefaultResourceType.WATER };
	}

	@Override
	default Map<ResourceType, Integer> getMaxResources() {
		return Map.of(DefaultResourceType.WATER, this.getMaxWater());
	}

	int getMaxWater();

}
