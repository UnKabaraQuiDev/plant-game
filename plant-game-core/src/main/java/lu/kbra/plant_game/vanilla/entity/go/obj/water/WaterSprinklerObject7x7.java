package lu.kbra.plant_game.vanilla.entity.go.obj.water;

import org.joml.Matrix4f;

import lu.kbra.plant_game.engine.entity.go.obj.FootprintComputeMethod;
import lu.kbra.plant_game.engine.entity.go.obj.PlaceableAnimatedGameObject;
import lu.kbra.plant_game.engine.mesh.AnimatedMesh;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.geom.Mesh;

@DataPath("classpath:/models/water-sprinkler-7x7.json")
public class WaterSprinklerObject7x7 extends PlaceableAnimatedGameObject {

	public WaterSprinklerObject7x7(final String str, final Mesh mesh, final AnimatedMesh animatedMesh) {
		super(str, mesh, animatedMesh);
	}

	@Override
	protected FootprintComputeMethod getAnimatedMeshFootprintComputeMethod() {
		return FootprintComputeMethod.MINIMAL;
	}

	@Override
	protected FootprintComputeMethod getStaticMeshFootprintComputeMethod() {
		return FootprintComputeMethod.MINIMAL;
	}

	@Override
	public Matrix4f computeAnimatedTransform(final float t) {
		this.getTransform()
				.getMatrix()
				.mul(this.animatedTransform.identity().rotateY((float) Math.toRadians(t * 12)), this.animatedTransform);
		return this.animatedTransform;
	}

}
