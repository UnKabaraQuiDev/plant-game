package lu.kbra.plant_game.engine.entity.go.impl;

import java.util.Map;

import lu.kbra.plant_game.base.data.DefaultResourceType;
import lu.kbra.plant_game.engine.scene.world.data.resource.ResourceType;

public interface EnergyContainer extends ResourceContainer {

	@Override
	default ResourceType[] getAllowedResources() {
		return new ResourceType[] { DefaultResourceType.ENERGY };
	}

	@Override
	default Map<ResourceType, Integer> getMaxResources() {
		return Map.of(DefaultResourceType.ENERGY, this.getMaxEnergy());
	}

	int getMaxEnergy();

}
