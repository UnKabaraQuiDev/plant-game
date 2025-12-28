package lu.kbra.plant_game.engine.entity.go.obj;

import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;

import lu.kbra.plant_game.engine.entity.go.AnimatedGameObject;
import lu.kbra.plant_game.engine.entity.go.impl.PlaceableObject;
import lu.kbra.plant_game.engine.entity.go.obj.water.NeedsPostConstruct;
import lu.kbra.plant_game.engine.mesh.AnimatedMesh;
import lu.kbra.standalone.gameengine.geom.BoundingBox;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.utils.consts.Direction;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public abstract class PlaceableAnimatedGameObject extends AnimatedGameObject implements PlaceableObject, NeedsPostConstruct {

	protected Direction rotation = Direction.DEFAULT();
	protected Vector2i originOffset;
	protected Vector2i footprint;

	public PlaceableAnimatedGameObject(
			final String str,
			final Mesh mesh,
			final AnimatedMesh animatedMesh,
			final Transform3D transform,
			final Vector3i objectId,
			final short materialId) {
		super(str, mesh, animatedMesh, transform, objectId, materialId);
	}

	public PlaceableAnimatedGameObject(
			final String str,
			final Mesh mesh,
			final AnimatedMesh animatedMesh,
			final Transform3D transform,
			final Vector3i objectId) {
		super(str, mesh, animatedMesh, transform, objectId);
	}

	public PlaceableAnimatedGameObject(final String str, final Mesh mesh, final AnimatedMesh animatedMesh, final Transform3D transform) {
		super(str, mesh, animatedMesh, transform);
	}

	public PlaceableAnimatedGameObject(final String str, final Mesh mesh, final AnimatedMesh animatedMesh) {
		super(str, mesh, animatedMesh);
	}

	protected boolean isInclusiveMesh() {
		return false;
	}

	@Override
	public void init() {
		final boolean inclusive = this.isInclusiveMesh();
		this.originOffset = this.computeMeshOffset(inclusive);
		this.footprint = this.computeMeshFootprint(inclusive);

		System.err.println(this.getClass().getSimpleName() + ": " + this.getOriginOffset() + " " + this.getFootprint());
	}

	protected Vector2i computeMeshFootprint(final boolean inclusive) {
		final Mesh mesh = this.getMesh();
		final AnimatedMesh animatedMesh = this.getAnimatedMesh();
		final BoundingBox bb = BoundingBox.union(mesh.getBoundingBox(), animatedMesh.getBoundingBox());

		final int minX;
		final int minZ;
		final int maxX;
		final int maxZ;
		final Vector3f min = bb.getMin();
		final Vector3f max = bb.getMax();
		if (inclusive) {
			minX = (int) Math.copySign(Math.ceil(Math.abs(min.x())), min.x());
			minZ = (int) Math.copySign(Math.ceil(Math.abs(min.z())), min.z());

			maxX = (int) Math.copySign(Math.ceil(Math.abs(max.x())), max.x());
			maxZ = (int) Math.copySign(Math.ceil(Math.abs(max.z())), max.z());
		} else {
			minX = (int) min.x();
			minZ = (int) min.z();

			maxX = (int) max.x();
			maxZ = (int) max.z();
		}

		return new Vector2i(maxX - minX, maxZ - minZ);
	}

	protected Vector2i computeMeshOffset(final boolean inclusive) {
		final Mesh mesh = this.getMesh();
		final AnimatedMesh animatedMesh = this.getAnimatedMesh();
		final BoundingBox bb = BoundingBox.union(mesh.getBoundingBox(), animatedMesh.getBoundingBox());

		final int minX;
		final int minZ;
		final Vector3f min = bb.getMin();
		if (inclusive) {
			minX = (int) Math.copySign(Math.ceil(Math.abs(min.x())), min.x());
			minZ = (int) Math.copySign(Math.ceil(Math.abs(min.z())), min.z());
		} else {
			minX = (int) min.x();
			minZ = (int) min.z();
		}

		return new Vector2i(-minX, -minZ);
	}

	@Override
	public Vector2i getOriginOffset() {
		return this.originOffset;
	}

	@Override
	public Vector2i getFootprint() {
		return this.footprint;
	}

	@Override
	public Direction getRotation() {
		return this.rotation;
	}

	@Override
	public void setRotation(final Direction dir) {
		assert dir != null : "null";
		this.rotation = dir;
	}

}
