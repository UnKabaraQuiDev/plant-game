package lu.kbra.plant_game.engine.entity.go.obj;

import lu.kbra.plant_game.engine.entity.go.AnimatedMeshGameObject;
import lu.kbra.plant_game.engine.entity.go.impl.Footprint;
import lu.kbra.plant_game.engine.entity.go.impl.PlaceableObject;
import lu.kbra.plant_game.engine.entity.impl.NeedsPostConstruct;
import lu.kbra.plant_game.engine.mesh.AnimatedMesh;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.utils.consts.Direction;

public abstract class PlaceableAnimatedGameObject extends AnimatedMeshGameObject
		implements PlaceableObject, NeedsPostConstruct, StaticMeshFootprintOwner, AnimatedMeshFootprintOwner {

	protected Direction rotation = Direction.DEFAULT();
	protected Footprint staticMeshFootprint;
	protected Footprint animatedMeshFootprint;
	protected Footprint footprint;

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
		System.err.println(this.getClass().getSimpleName() + " : " + this.getStaticMeshFootprintComputeMethod() + " = "
				+ this.getMesh().getBoundingBox() + " = " + this.staticMeshFootprint);
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
	public void setRotation(final Direction dir) {
		assert dir != null : "null";
		this.rotation = dir;
	}

}
