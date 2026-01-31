import org.joml.Matrix4f;

import lu.kbra.plant_game.engine.entity.go.data.Footprint;
import lu.kbra.plant_game.engine.entity.go.obj.AnimatedMeshFootprintOwner;
import lu.kbra.plant_game.engine.mesh.AnimatedMesh;
import lu.kbra.standalone.gameengine.utils.consts.Direction;

public class MockAnimatedMeshFootprintOwner implements AnimatedMeshFootprintOwner {

	protected AnimatedMesh animatedMesh;
	protected Direction dir = Direction.DEFAULT();

	public MockAnimatedMeshFootprintOwner(final AnimatedMesh animatedMesh) {
		this.animatedMesh = animatedMesh;
	}

	public MockAnimatedMeshFootprintOwner(final AnimatedMesh animatedMesh, final Direction dir) {
		this.animatedMesh = animatedMesh;
		this.dir = dir;
	}

	@Override
	public AnimatedMesh getAnimatedMesh() {
		return this.animatedMesh;
	}

	@Override
	public Footprint getFootprint() {
		return null;
	}

	@Override
	public Direction getRotation() {
		return this.dir;
	}

	@Override
	public Footprint getAnimatedMeshFootprint() {
		return null;
	}

	@Override
	public void setAnimatedMesh(final AnimatedMesh m) {
		this.animatedMesh = m;
	}

	@Override
	public Matrix4f computeAnimatedTransform(final float t) {
		return null;
	}

	@Override
	public void setAnimatedTransform(final Matrix4f animatedTransform) {

	}

	@Override
	public Matrix4f getAnimatedTransform() {
		return null;
	}

}
