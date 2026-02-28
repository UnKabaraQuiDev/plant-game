package lu.kbra.plant_game.base.entity.go.obj_inst.grass;

import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

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

	protected final Object lock = new Object();
	protected volatile boolean resizing = false;
	protected final Queue<Pair<Integer, Vector3f>> pending = new ConcurrentLinkedQueue<>();
	protected final Set<Integer> dirty = ConcurrentHashMap.newKeySet();
	protected int count = 0;

	public ResizableInstanceSwayGameObject(final String str, final InstanceEmitter ie) {
		super(str, ie);
	}

	public void removeInstance(final int index) {
		final int last;

		synchronized (this.lock) {
			if (index < 0 || index >= this.count) {
				return;
			}

			last = this.count - 1;

			if (index != last) {
				final Transform3D a = (Transform3D) this.instanceEmitter.getParticles()[index].getTransform();
				final Transform3D b = (Transform3D) this.instanceEmitter.getParticles()[last].getTransform();

				a.set(b);
				a.updateMatrix();

				this.dirty.add(index);
			}

			this.count--;
		}

		this.dispatchDirty();
	}

	public int addInstance(final Vector3f position) {
		final int index;
		synchronized (this.lock) {
			index = this.count++;
		}

		this.setInstanceInternal(index, position);

		return index;
	}

	private void setInstanceInternal(final int index, final Vector3f position) {
		if (this.needsResize(index)) {
			this.pending.add(Pairs.readOnly(index, position));
			this.requestResize();
			return;
		}

		if (this.resizing) {
			this.pending.add(Pairs.readOnly(index, position));
			return;
		}

		this.applyTransform(index, position);
	}

	private boolean needsResize(final int index) {
		return index >= this.instanceEmitter.getParticleCount();
	}

	private void applyTransform(final int index, final Vector3f position) {
		final Transform3D t = (Transform3D) this.instanceEmitter.getParticles()[index].getTransform();
		t.translationSet(position).updateMatrix();
		this.dirty.add(index);

		this.dispatchDirty();
	}

	private void requestResize() {
		synchronized (this.lock) {
			if (this.resizing) {
				return;
			}
			this.resizing = true;
		}

		new TaskFuture<>(PGLogic.INSTANCE.RENDER_DISPATCHER, (Runnable) () -> {
			this.instanceEmitter.resize(this.instanceEmitter.getParticleCount() + 32, i -> new Transform3D());
		}).then(PGLogic.INSTANCE.WORKERS, (Runnable) () -> {
			synchronized (this.lock) {
				this.resizing = false;
			}
			this.flushPending();
		}).push();
	}

	private void flushPending() {
		Pair<Integer, Vector3f> p;

		while ((p = this.pending.poll()) != null) {
			this.applyTransform(p.getKey(), p.getValue());
		}
	}

	private void dispatchDirty() {

		if (this.dirty.isEmpty()) {
			return;
		}

		PGLogic.INSTANCE.RENDER_DISPATCHER.post(() -> {
			this.instanceEmitter.updateParticlesTransforms(this.dirty);
			this.dirty.clear();
		});
	}

	@Override
	public int getParticleCount() {
		return this.count;
	}

	@Override
	public String toString() {
		return "ResizableInstanceSwayGameObject@" + System.identityHashCode(this) + " [lock=" + this.lock + ", resizing=" + this.resizing
				+ ", pending=" + this.pending + ", dirty=" + this.dirty + ", count=" + this.count + ", deformRatio=" + this.deformRatio
				+ ", speedRatio=" + this.speedRatio + ", scaleRatio=" + this.scaleRatio + ", materialId=" + this.materialId
				+ ", isEntityMaterialId=" + this.isEntityMaterialId + ", instanceEmitter=" + this.instanceEmitter + ", objectId="
				+ this.objectId + ", objectIdLocation=" + this.objectIdLocation + ", transform=" + this.transform + ", active="
				+ this.active + ", name=" + this.name + "]";
	}

}
