package lu.kbra.plant_game.engine.entity.go.impl;

import java.util.Map;

import lu.kbra.plant_game.engine.scene.world.data.ResourceBuffer;
import lu.kbra.plant_game.engine.scene.world.data.resource.ResourceType;

public interface ResourceConsumer {

	default void consume(final float dTime, final ResourceBuffer rb) {
		this.setWorking(rb.tryConsume(this.getConsumedRate(), dTime));
	}

	default ResourceType[] getConsumedResources() {
		return this.getConsumedRate().keySet().toArray(ResourceType[]::new);
	}

	Map<ResourceType, Float> getConsumedRate();

	void setWorking(boolean working);

	boolean isWorking();

}
