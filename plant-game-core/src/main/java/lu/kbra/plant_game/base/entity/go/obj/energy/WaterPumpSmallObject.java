package lu.kbra.plant_game.base.entity.go.obj.energy;

import lu.kbra.plant_game.engine.entity.go.obj.PlaceableAnimatedGameObject;
import lu.kbra.plant_game.engine.entity.impl.WaterContainer;
import lu.kbra.plant_game.engine.entity.impl.WaterProducer;
import lu.kbra.plant_game.engine.mesh.AnimatedMesh;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.geom.Mesh;

@DataPath("classpath:/models/water-pump-small.json")
public class WaterPumpSmallObject extends PlaceableAnimatedGameObject implements WaterContainer, WaterProducer {

	public WaterPumpSmallObject(final String str, final Mesh mesh, final AnimatedMesh animatedMesh) {
		super(str, mesh, animatedMesh);
	}

	@Override
	public float getProductionRate() {
		return 3;
	}

	@Override
	public int getMaxWater() {
		return 100;
	}

}
