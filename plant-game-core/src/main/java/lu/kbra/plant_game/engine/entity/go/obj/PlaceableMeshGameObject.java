package lu.kbra.plant_game.engine.entity.go.obj;

import org.joml.Vector2i;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lu.kbra.plant_game.engine.entity.go.MeshGameObject;
import lu.kbra.plant_game.engine.entity.go.data.Footprint;
import lu.kbra.plant_game.engine.entity.go.impl.PlaceableObject;
import lu.kbra.plant_game.engine.entity.impl.NeedsPostConstruct;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.utils.consts.Direction;

public abstract class PlaceableMeshGameObject extends MeshGameObject
		implements PlaceableObject, NeedsPostConstruct, StaticMeshFootprintOwner {

	@JsonIgnore
	protected Footprint footprint;

	protected Direction rotation = Direction.DEFAULT;
	protected Vector2i tile;

	public PlaceableMeshGameObject(final String str, final Mesh mesh) {
		super(str, mesh);
	}

	@Override
	public void postConstruct() {
		this.footprint = StaticMeshFootprintOwner.computeMeshFootprint(this.getFootprintComputeMethod(), this.getMesh());
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
	public Vector2i getTile() {
		return this.tile;
	}

	@Override
	public void setTile(final Vector2i tile) {
		this.tile = tile;
	}

	@Override
	public String toString() {
		return "PlaceableMeshGameObject@" + System.identityHashCode(this) + " [footprint=" + this.footprint + ", rotation=" + this.rotation
				+ ", tile=" + this.tile + ", materialId=" + this.materialId + ", isEntityMaterialId=" + this.isEntityMaterialId
				+ ", objectId=" + this.objectId + ", objectIdLocation=" + this.objectIdLocation + ", mesh=" + this.mesh + ", transform="
				+ this.transform + ", active=" + this.active + ", name=" + this.name + "]";
	}

}
