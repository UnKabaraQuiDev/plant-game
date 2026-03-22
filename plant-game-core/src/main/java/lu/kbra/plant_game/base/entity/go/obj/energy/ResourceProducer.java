package lu.kbra.plant_game.base.entity.go.obj.energy;

import lu.kbra.plant_game.engine.scene.world.data.ResourceBuffer;

public interface ResourceProducer {

	void produce(final float dTime, final ResourceBuffer rb);

}
