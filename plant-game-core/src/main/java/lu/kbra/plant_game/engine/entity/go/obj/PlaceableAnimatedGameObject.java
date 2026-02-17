package lu.kbra.plant_game.engine.entity.go.obj;

import org.joml.Vector2i;

import lu.kbra.plant_game.engine.entity.go.AnimatedMeshGameObject;
import lu.kbra.plant_game.engine.entity.go.data.Footprint;
import lu.kbra.plant_game.engine.entity.go.impl.PlaceableObject;
import lu.kbra.plant_game.engine.entity.impl.NeedsPostConstruct;
import lu.kbra.plant_game.engine.mesh.AnimatedMesh;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.utils.consts.Direction;

public abstract class PlaceableAnimatedGameObject extends AnimatedMeshGameObject
		implements PlaceableObject, NeedsPostConstruct, StaticMeshFootprintOwner, AnimatedMeshFootprintOwner {

	protected Footprint staticMeshFootprint;
	protected Footprint animatedMeshFootprint;
	protected Footprint footprint;

	protected Direction rotation = Direction.DEFAULT();
	protected Vector2i tile;

	public PlaceableAnimatedGameObject(final String str, final Mesh mesh, final AnimatedMesh animatedMesh) {
		super(str, mesh, animatedMesh);
	}

	protected FootprintComputeMethod getStaticMeshFootprintComputeMethod() {
		return FootprintComputeMethod.CLOSEST;
	}

	protected FootprintComputeMethod getAnimatedMeshFootprintComputeMethod() {
		return FootprintComputeMethod.CLOSEST;
	}

	@Override
	public void init() {
		this.staticMeshFootprint = StaticMeshFootprintOwner.computeMeshFootprint(this.getStaticMeshFootprintComputeMethod(),
				this.getMesh());
		this.animatedMeshFootprint = StaticMeshFootprintOwner.computeMeshFootprint(this.getAnimatedMeshFootprintComputeMethod(),
				this.getAnimatedMesh());
		this.footprint = Footprint.union(this.getStaticMeshFootprint(), this.getAnimatedMeshFootprint());
	}

	@Override
	public Footprint getStaticMeshFootprint() {
		return this.staticMeshFootprint;
	}

	@Override
	public Footprint getAnimatedMeshFootprint() {
		return this.animatedMeshFootprint;
	}

	@Override
	public Footprint getFootprint() {
		return this.footprint;
	}

	@Override
	public Direction getRotation() {
		return this.rotation;
	}

	@Override
	public Vector2i getTile() {
		return this.tile;
	}

	@Override
	public void setTile(final Vector2i tile) {
		this.tile = tile;
	}

	@Override
	public void setRotation(final Direction dir) {
		assert dir != null : "null";
		this.rotation = dir;
	}

	@Override
	public String toString() {
		return "PlaceableAnimatedGameObject@" + System.identityHashCode(this) + " [staticMeshFootprint=" + this.staticMeshFootprint
				+ ", animatedMeshFootprint=" + this.animatedMeshFootprint + ", footprint=" + this.footprint + ", rotation=" + this.rotation
				+ ", tile=" + this.tile + ", animatedTransform=" + this.animatedTransform + ", animatedMesh=" + this.animatedMesh
				+ ", materialId=" + this.materialId + ", isEntityMaterialId=" + this.isEntityMaterialId + ", objectId=" + this.objectId
				+ ", objectIdLocation=" + this.objectIdLocation + ", mesh=" + this.mesh + ", transform=" + this.transform + ", active="
				+ this.active + ", name=" + this.name + "]";
	}

}
