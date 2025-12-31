package lu.kbra.plant_game.engine.entity.go.impl;

import lu.kbra.plant_game.engine.entity.go.GameObject;
import lu.kbra.standalone.gameengine.geom.instance.InstanceEmitter;
import lu.kbra.standalone.gameengine.objs.entity.components.InstanceEmitterComponent;

public class InstanceGameObject extends GameObject implements InstanceEmitterOwner {

	protected InstanceEmitterComponent instanceEmitterComponent;

	public InstanceGameObject(final String str, final InstanceEmitter ie) {
		super(str, null);
		this.setInstanceEmitter(ie);
	}

	public InstanceEmitterComponent getInstanceEmitterComponent() {
		return this.instanceEmitterComponent;
	}

	@Override
	public InstanceEmitter getInstanceEmitter() {
		return this.instanceEmitterComponent == null ? null : this.instanceEmitterComponent.getInstanceEmitter();
	}

	@Override
	public void setInstanceEmitter(final InstanceEmitter ie) {
		if (this.instanceEmitterComponent != null) {
			if (ie == null) {
				super.removeComponent(InstanceEmitterComponent.class);
			} else {
				this.instanceEmitterComponent.setInstanceEmitter(ie);
			}
		} else if (ie != null) {
			super.addComponent(this.instanceEmitterComponent = new InstanceEmitterComponent(ie));
		}
	}

	@Override
	public String toString() {
		return "InstanceGameObject [materialId=" + this.materialId + ", entityMaterialId=" + this.isEntityMaterialId + ", objectId="
				+ this.objectId + ", objectIdLocation=" + this.objectIdLocation + ", active=" + this.active + ", name=" + this.name
				+ ", components=" + this.components.size() + ", getInstanceEmitter()=" + this.getInstanceEmitter() + ", getTransform()="
				+ this.getTransform() + "]";
	}

}
