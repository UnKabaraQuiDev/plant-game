package lu.kbra.plant_game.engine.entity.go.obj;

import lu.kbra.plant_game.engine.entity.go.MeshGameObject;
import lu.kbra.plant_game.engine.entity.go.impl.Footprint;
import lu.kbra.plant_game.engine.entity.go.impl.PlaceableObject;
import lu.kbra.plant_game.engine.entity.impl.NeedsPostConstruct;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.utils.consts.Direction;

public abstract class PlaceableMeshGameObject extends MeshGameObject
		implements PlaceableObject, NeedsPostConstruct, StaticMeshFootprintOwner {

	protected Direction rotation = Direction.DEFAULT();
	protected Footprint footprint;

	public PlaceableMeshGameObject(final String str, final Mesh mesh) {
		super(str, mesh);
	}

	@Override
	public void init() {
		this.footprint = StaticMeshFootprintOwner.computeMeshFootprint(this.getFootprintComputeMethod(), this.getMesh());
//		System.err
//				.println(this.getClass().getSimpleName() + " : " + this.getFootprintComputeMethod() + " = "
//						+ this.getMesh().getBoundingBox() + " = " + this.footprint);
	}

	protected FootprintComputeMethod getFootprintComputeMethod() {
		return FootprintComputeMethod.CLOSEST;
	}

	@Override
	public Footprint getFootprint() {
		return this.footprint;
	}

	@Override
	public Footprint getStaticMeshFootprint() {
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

	@Override
	public String toString() {
		return "PlaceableMeshGameObject [rotation=" + this.rotation + ", footprint=" + this.footprint + ", materialId=" + this.materialId
				+ ", isEntityMaterialId=" + this.isEntityMaterialId + ", objectId=" + this.objectId + ", objectIdLocation="
				+ this.objectIdLocation + ", mesh=" + this.mesh + ", transform=" + this.transform + ", active=" + this.active + ", name="
				+ this.name + "]";
	}

}
