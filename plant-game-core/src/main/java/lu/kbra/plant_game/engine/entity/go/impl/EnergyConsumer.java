package lu.kbra.plant_game.engine.entity.go.impl;

import java.util.Map;

import lu.kbra.plant_game.base.data.DefaultResourceType;
import lu.kbra.plant_game.engine.scene.world.data.resource.ResourceType;

public interface EnergyConsumer extends ResourceConsumer {

	@Override
	default Map<ResourceType, Float> getConsumedRate() {
		return Map.of(DefaultResourceType.ENERGY, this.getConsumedEnergy());
	}

	float getConsumedEnergy();

}
