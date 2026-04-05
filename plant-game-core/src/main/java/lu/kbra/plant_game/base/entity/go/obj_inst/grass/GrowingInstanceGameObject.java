package lu.kbra.plant_game.base.entity.go.obj_inst.grass;

import java.util.Arrays;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.joml.Vector3f;

import lu.kbra.pclib.datastructure.pair.Pair;
import lu.kbra.pclib.datastructure.pair.Pairs;
import lu.kbra.plant_game.PGLogic;
import lu.kbra.plant_game.base.entity.go.obj_inst.round.GrownObject;
import lu.kbra.plant_game.base.entity.go_inst.champi.SizeOwner;
import lu.kbra.plant_game.engine.entity.go.InstanceSwayGameObject;
import lu.kbra.plant_game.engine.entity.go.VariationOwner;
import lu.kbra.plant_game.engine.entity.impl.ParticleCountOwner;
import lu.kbra.standalone.gameengine.geom.instance.InstanceEmitter;
import lu.kbra.standalone.gameengine.impl.future.TaskFuture;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public abstract class GrowingInstanceGameObject extends InstanceSwayGameObject
		implements ParticleCountOwner, VariationOwner, GrownObject, SizeOwner {

	protected final Object lock = new Object();

	protected volatile boolean resizing = false;

	protected final Queue<Pair<Integer, Vector3f>> pending = new ConcurrentLinkedQueue<>();
	protected final Set<Integer> dirty = ConcurrentHashMap.newKeySet();

	protected int count = 0;

	protected int[] logicalToPhysical = new int[32];
	protected int[] physicalToLogical = new int[32];

	public GrowingInstanceGameObject(final String str, final InstanceEmitter ie) {
		super(str, ie);
	}

	@Override
	public int getParticleCount() {
		return this.count;
	}

	public int addInstance(final Vector3f position) {
		final int logicalId;

		synchronized (this.lock) {
			logicalId = this.count;

			this.ensureMappingCapacity(logicalId);

			this.logicalToPhysical[logicalId] = logicalId;
			this.physicalToLogical[logicalId] = logicalId;

			this.count++;
		}

		this.setInstanceInternal(logicalId, position);
		return logicalId;
	}

	public void removeInstance(final int logicalId) {
		final int lastLogical;
		final int physicalIndex;
		final int lastPhysical;

		synchronized (this.lock) {
			if (logicalId < 0 || logicalId >= this.count) {
				return;
			}

			lastLogical = this.count - 1;

			physicalIndex = this.logicalToPhysical[logicalId];
			lastPhysical = this.logicalToPhysical[lastLogical];

			// swap physical instances
			if (physicalIndex != lastPhysical) {
				this.swapPhysical(physicalIndex, lastPhysical);

				// update mappings
				final int movedLogical = this.physicalToLogical[lastPhysical];

				this.logicalToPhysical[movedLogical] = physicalIndex;
				this.physicalToLogical[physicalIndex] = movedLogical;
			}

			this.count--;
		}

		this.dispatchDirty();
	}

	private void swapPhysical(final int aIdx, final int bIdx) {
		final Transform3D a = (Transform3D) this.instanceEmitter.getParticles()[aIdx].getTransform();
		final Transform3D b = (Transform3D) this.instanceEmitter.getParticles()[bIdx].getTransform();

		final Transform3D tmp = new Transform3D();
		tmp.set(a);

		a.set(b).updateMatrix();
		b.set(tmp).updateMatrix();

		this.dirty.add(aIdx);
		this.dirty.add(bIdx);
	}

	private void setInstanceInternal(final int logicalId, final Vector3f position) {
		final int physicalIndex;

		synchronized (this.lock) {
			physicalIndex = this.logicalToPhysical[logicalId];
		}

		if (this.needsResize(physicalIndex)) {
			this.pending.add(Pairs.readOnly(logicalId, position));
			this.requestResize();
			return;
		}

		if (this.resizing) {
			this.pending.add(Pairs.readOnly(logicalId, position));
			return;
		}

		this.applyTransform(physicalIndex, position);
	}

	private boolean needsResize(final int index) {
		return index >= this.instanceEmitter.getParticleCount();
	}

	private void applyTransform(final int physicalIndex, final Vector3f position) {
		final Transform3D t = (Transform3D) this.instanceEmitter.getParticles()[physicalIndex].getTransform();
		t.translationSet(position).updateMatrix();

		this.dirty.add(physicalIndex);
		this.dispatchDirty();
	}

	private void requestResize() {
		synchronized (this.lock) {
			if (this.resizing) {
				return;
			}
			this.resizing = true;
		}

		new TaskFuture<>(PGLogic.INSTANCE.RENDER_DISPATCHER,
				(Runnable) () -> this.instanceEmitter.resize(this.instanceEmitter.getParticleCount() + 32, i -> new Transform3D()))
				.then(PGLogic.INSTANCE.WORKERS, (Runnable) () -> {
					synchronized (this.lock) {
						this.resizing = false;
					}
					this.flushPending();
				})
				.push();
	}

	private void flushPending() {
		Pair<Integer, Vector3f> p;

		while ((p = this.pending.poll()) != null) {
			this.setInstanceInternal(p.getKey(), p.getValue());
		}
	}

	private void dispatchDirty() {
		if (this.dirty.isEmpty()) {
			return;
		}

		this.instanceEmitter.updateParticlesTransforms(this.dirty, PGLogic.INSTANCE.WORKERS, PGLogic.INSTANCE.RENDER_DISPATCHER)
				.then(PGLogic.INSTANCE.WORKERS, (Runnable) this.dirty::clear)
				.push();
	}

	private void ensureMappingCapacity(final int id) {
		if (id < this.logicalToPhysical.length) {
			return;
		}

		final int newSize = this.logicalToPhysical.length * 2;

		this.logicalToPhysical = Arrays.copyOf(this.logicalToPhysical, newSize);
		this.physicalToLogical = Arrays.copyOf(this.physicalToLogical, newSize);
	}

	@Override
	public String toString() {
		return "GrowingInstanceGameObject@" + System.identityHashCode(this) + " [lock=" + this.lock + ", resizing=" + this.resizing
				+ ", pending=" + this.pending + ", dirty=" + this.dirty + ", count=" + this.count + ", logicalToPhysical="
				+ Arrays.toString(this.logicalToPhysical) + ", physicalToLogical=" + Arrays.toString(this.physicalToLogical)
				+ ", deformRatio=" + this.deformRatio + ", speedRatio=" + this.speedRatio + ", scaleRatio=" + this.scaleRatio
				+ ", materialId=" + this.materialId + ", isEntityMaterialId=" + this.isEntityMaterialId + ", instanceEmitter="
				+ this.instanceEmitter + ", objectId=" + this.objectId + ", objectIdLocation=" + this.objectIdLocation + ", transform="
				+ this.transform + ", active=" + this.active + ", name=" + this.name + "]";
	}

}
