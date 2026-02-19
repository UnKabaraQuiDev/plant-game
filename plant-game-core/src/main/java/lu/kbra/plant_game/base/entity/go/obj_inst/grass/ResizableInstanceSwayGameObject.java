package lu.kbra.plant_game.base.entity.go.obj_inst.grass;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

import lu.kbra.pclib.datastructure.pair.Pair;
import lu.kbra.pclib.datastructure.pair.Pairs;
import lu.kbra.plant_game.PGLogic;
import lu.kbra.plant_game.engine.entity.go.InstanceSwayGameObject;
import lu.kbra.plant_game.engine.entity.impl.ParticleCountOwner;
import lu.kbra.standalone.gameengine.geom.instance.InstanceEmitter;
import lu.kbra.standalone.gameengine.impl.future.TaskFuture;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class ResizableInstanceSwayGameObject extends InstanceSwayGameObject implements ParticleCountOwner {

	protected volatile boolean resizing = false;
	protected final List<Pair<Integer, Vector3f>> positions = new ArrayList<>();
	protected int count = 0;

	public ResizableInstanceSwayGameObject(final String str, final InstanceEmitter ie) {
		super(str, ie);
	}

	public void removeInstance(final int instanceIndex) {
		final int lastIndex;

		synchronized (this) {
			if (instanceIndex < 0 || instanceIndex >= this.count) {
				return;
			}

			lastIndex = this.count - 1;

			if (instanceIndex != lastIndex) {
				final Transform3D removed = (Transform3D) this.instanceEmitter.getParticles()[instanceIndex].getTransform();
				final Transform3D last = (Transform3D) this.instanceEmitter.getParticles()[lastIndex].getTransform();

				removed.set(last);
				removed.updateMatrix();
			}

			this.count--;
		}

		System.err.println(this.getId() + " removed: " + instanceIndex + " = " + this.count);

		PGLogic.INSTANCE.RENDER_DISPATCHER.post(() -> this.instanceEmitter.updateParticlesTransforms());
	}

	public int addInstance(final Vector3f position) {
		final int index;
		synchronized (this) {
			index = this.count++;
		}
		this.setInstance(index, position);
		return index;
	}

	private void setInstance(final int instanceIndex, final Vector3f position) {
		synchronized (this) {
			if (instanceIndex >= this.instanceEmitter.getParticleCount()) {
				this.positions.add(Pairs.readOnly(instanceIndex, position));
				this.reallocate();
				return;
			}
			if (this.resizing) {
				this.positions.add(Pairs.readOnly(instanceIndex, position));
				return;
			}
		}
		System.err.println(this.getId() + " set: " + instanceIndex + " = " + this.count);

		((Transform3D) this.instanceEmitter.getParticles()[instanceIndex].getTransform()).translationSet(position).updateMatrix();
		PGLogic.INSTANCE.RENDER_DISPATCHER.post(() -> this.instanceEmitter.updateParticlesTransforms());
	}

	private boolean reallocate() {
		synchronized (this) {
			if (this.resizing) {
				return false;
			}
			this.resizing = true;
		}
		new TaskFuture<>(PGLogic.INSTANCE.RENDER_DISPATCHER, (Runnable) () -> {
			super.instanceEmitter.resize(this.instanceEmitter.getParticleCount() + 32, i -> new Transform3D());
			synchronized (ResizableInstanceSwayGameObject.this) {
				this.resizing = false;
			}
		}).then(PGLogic.INSTANCE.WORKERS, (Runnable) () -> {
			this.positions.removeIf(v -> {
				this.setInstance(v.getKey(), v.getValue());
				return true;
			});
		}).push();
		return true;
	}

	@Override
	public int getParticleCount() {
		return this.count;
	}

	@Override
	public String toString() {
		return "ResizableInstanceSwayGameObject@" + System.identityHashCode(this) + " [resizing=" + this.resizing + ", positions="
				+ this.positions + ", count=" + this.count + ", deformRatio=" + this.deformRatio + ", speedRatio=" + this.speedRatio
				+ ", scaleRatio=" + this.scaleRatio + ", materialId=" + this.materialId + ", isEntityMaterialId=" + this.isEntityMaterialId
				+ ", instanceEmitter=" + this.instanceEmitter + ", objectId=" + this.objectId + ", objectIdLocation="
				+ this.objectIdLocation + ", transform=" + this.transform + ", active=" + this.active + ", name=" + this.name + "]";
	}

}
