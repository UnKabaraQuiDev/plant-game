package lu.kbra.plant_game.engine.entity.go;

import lu.kbra.plant_game.engine.entity.impl.InstanceEmitterOwner;
import lu.kbra.plant_game.engine.entity.impl.MaterialIdOwner;
import lu.kbra.standalone.gameengine.geom.instance.InstanceEmitter;

public class InstanceGameObject extends GenericGameObject implements InstanceEmitterOwner, MaterialIdOwner {

	protected short materialId;
	protected boolean isEntityMaterialId = true;
	protected InstanceEmitter instanceEmitter;

	public InstanceGameObject(final String str, final InstanceEmitter ie) {
		super(str);
		this.setInstanceEmitter(ie);
	}

	@Override
	public void setIsEntityMaterialId(final boolean ie) {
		this.isEntityMaterialId = ie;
	}

	@Override
	public short getMaterialId() {
		return this.materialId;
	}

	@Override
	public void setMaterialId(final short materialId) {
		this.materialId = materialId;
	}

	@Override
	public boolean isEntityMaterialId() {
		return this.isEntityMaterialId;
	}

	@Override
	public InstanceEmitter getInstanceEmitter() {
		return this.instanceEmitter;
	}

	@Override
	public void setInstanceEmitter(final InstanceEmitter ie) {
		this.instanceEmitter = ie;
	}

	@Override
	public String toString() {
		return "InstanceGameObject [instanceEmitter=" + this.instanceEmitter + ", objectId=" + this.objectId + ", objectIdLocation="
				+ this.objectIdLocation + ", transform=" + this.transform + ", active=" + this.active + ", name=" + this.name + "]";
	}

}
