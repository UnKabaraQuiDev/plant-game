package lu.kbra.plant_game.engine.entity.go.impl;

import org.joml.Vector3i;

import lu.kbra.standalone.gameengine.geom.instance.InstanceEmitter;
import lu.kbra.standalone.gameengine.objs.entity.components.InstanceEmitterComponent;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class InstanceGameObject extends GameObject {

	protected InstanceEmitterComponent instanceEmitterComponent;

	public InstanceGameObject(
			final String str,
			final InstanceEmitter instanceEmitter,
			final Transform3D transform,
			final Vector3i objectId,
			final short materialId) {
		super(str, null, transform, objectId, materialId);
		if (instanceEmitter != null) {
			super.addComponent(this.instanceEmitterComponent = new InstanceEmitterComponent(instanceEmitter));
		}
	}

	public InstanceGameObject(
			final String str,
			final InstanceEmitter instanceEmitter,
			final Transform3D transform,
			final Vector3i objectId) {
		super(str, null, transform, objectId, (short) -1);
		if (instanceEmitter != null) {
			super.addComponent(this.instanceEmitterComponent = new InstanceEmitterComponent(instanceEmitter));
		}
	}

	public InstanceGameObject(final String str, final InstanceEmitter instanceEmitter, final Transform3D transform) {
		super(str, null, transform, getRandomObjectId(), (short) -1);
		if (instanceEmitter != null) {
			super.addComponent(this.instanceEmitterComponent = new InstanceEmitterComponent(instanceEmitter));
		}
	}

	public InstanceGameObject(final String str, final InstanceEmitter instanceEmitter, final short materialId) {
		super(str, null, null, getRandomObjectId(), materialId);
		if (instanceEmitter != null) {
			super.addComponent(this.instanceEmitterComponent = new InstanceEmitterComponent(instanceEmitter));
		}
	}

	public InstanceGameObject(final String str, final InstanceEmitter instanceEmitter) {
		super(str, null, null, getRandomObjectId(), (short) -1);
		if (instanceEmitter != null) {
			super.addComponent(this.instanceEmitterComponent = new InstanceEmitterComponent(instanceEmitter));
		}
	}

	public InstanceEmitterComponent getInstanceEmitterComponent() {
		return this.instanceEmitterComponent;
	}

	public InstanceEmitter getInstanceEmitter() {
		return this.instanceEmitterComponent == null ? null : this.instanceEmitterComponent.getInstanceEmitter();
	}

}
