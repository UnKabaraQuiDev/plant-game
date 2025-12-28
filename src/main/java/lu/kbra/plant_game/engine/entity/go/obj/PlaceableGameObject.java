package lu.kbra.plant_game.engine.entity.go.obj;

import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3ic;

import lu.kbra.plant_game.engine.entity.go.GameObject;
import lu.kbra.plant_game.engine.entity.go.impl.PlaceableObject;
import lu.kbra.plant_game.engine.entity.go.obj.water.NeedsPostConstruct;
import lu.kbra.standalone.gameengine.geom.BoundingBox;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.utils.consts.Direction;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public abstract class PlaceableGameObject extends GameObject implements PlaceableObject, NeedsPostConstruct {

	protected Direction rotation = Direction.DEFAULT();
	protected Vector2i originOffset;
	protected Vector2i footprint;

	public PlaceableGameObject(
			final String str,
			final Mesh mesh,
			final Transform3D transform,
			final Vector3ic objectId,
			final short materialId) {
		super(str, mesh, transform, objectId, materialId);
	}

	public PlaceableGameObject(final String str, final Mesh mesh, final Transform3D transform, final Vector3ic objectId) {
		super(str, mesh, transform, objectId);
	}

	public PlaceableGameObject(final String str, final Mesh mesh, final Transform3D transform) {
		super(str, mesh, transform);
	}

	public PlaceableGameObject(final String str, final Mesh mesh) {
		super(str, mesh);
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
		final BoundingBox bb = mesh.getBoundingBox();

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
		final BoundingBox bb = mesh.getBoundingBox();

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
