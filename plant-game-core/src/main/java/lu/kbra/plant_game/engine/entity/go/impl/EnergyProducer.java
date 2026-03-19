package lu.kbra.plant_game.engine.entity.go.impl;

import lu.kbra.plant_game.base.data.DefaultResourceType;
import lu.kbra.plant_game.base.entity.go.obj.energy.GenericResourceProducer;
import lu.kbra.plant_game.engine.scene.world.data.resource.ResourceType;

public interface EnergyProducer extends GenericResourceProducer {

	@Override
	default ResourceType getProducedType() {
		return DefaultResourceType.ENERGY;
	}

}
