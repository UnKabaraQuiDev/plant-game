package lu.kbra.plant_game.engine.entity.go.impl;

import java.util.Map;

import lu.kbra.plant_game.engine.scene.world.data.resource.ResourceType;

public interface ResourceContainer {

	default ResourceType[] getAllowedResources() {
		return this.getMaxResources().keySet().toArray(ResourceType[]::new);
	}

	Map<ResourceType, Integer> getMaxResources();

}
