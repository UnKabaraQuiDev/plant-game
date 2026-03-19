package lu.kbra.plant_game.base.entity.go.obj.energy;

import org.joml.Matrix4f;
import org.joml.Vector2i;

import lu.kbra.plant_game.engine.entity.go.impl.EnergyProducer;
import lu.kbra.plant_game.engine.entity.go.obj.FootprintComputeMethod;
import lu.kbra.plant_game.engine.entity.go.obj.PlaceableAnimatedGameObject;
import lu.kbra.plant_game.engine.mesh.AnimatedMesh;
import lu.kbra.plant_game.engine.scene.world.WorldLevelScene;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.utils.consts.Direction;

@DataPath("classpath:/models/water-wheel-small.json")
public class WaterWheelSmallObject extends PlaceableAnimatedGameObject implements EnergyProducer {

	public WaterWheelSmallObject(final String str, final Mesh mesh, final AnimatedMesh animatedMesh) {
		super(str, mesh, animatedMesh);
	}

	@Override
	protected FootprintComputeMethod getStaticMeshFootprintComputeMethod() {
		return FootprintComputeMethod.MINIMAL;
	}

	@Override
	public Matrix4f computeAnimatedTransform(final float t) {
		this.getTransform()
				.getMatrix()
				.mul(this.animatedTransform.identity().rotateZ((float) Math.toRadians(t * 25)), this.animatedTransform);
		return this.animatedTransform;
	}

	@Override
	public boolean isPlaceable(final WorldLevelScene scene, final Vector2i tile, final Direction rotation) {
		final float waterLevel = scene.getWaterHeight();

		return super.getAnimatedMeshFootprint()
				.allCellsMatch(tile, rotation, v -> (scene.getTerrain().getMesh().getCellHeight(v) < waterLevel))
				&& super.getStaticMeshFootprint()
						.allCellsMatch(tile, rotation, v -> (scene.getTerrain().getMesh().getCellHeight(v) == (int) Math.ceil(waterLevel)));
	}

	@Override
	public float getProductionRate() {
		return 1f;
	}

}
