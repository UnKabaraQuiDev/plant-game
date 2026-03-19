package lu.kbra.plant_game.base.entity.go.obj.energy;

import lu.kbra.plant_game.engine.scene.world.ResourceBuffer;
import lu.kbra.plant_game.engine.scene.world.data.resource.ResourceType;
import lu.kbra.standalone.gameengine.objs.entity.SceneEntity;

public interface GenericResourceProducer extends ResourceProducer, SceneEntity {

	@Override
	default void produce(final float dTime, final ResourceBuffer rb) {
		rb.add(this.getProducedType(), dTime * this.getProductionRate());
	}

	float getProductionRate();

	default boolean isProductionActive() {
		return this.isActive();
	}

	ResourceType getProducedType();

}
