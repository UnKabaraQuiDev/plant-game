package lu.kbra.plant_game.base.entity.go.obj_inst.grass;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

import lu.kbra.plant_game.PGLogic;
import lu.kbra.plant_game.engine.entity.go.InstanceSwayGameObject;
import lu.kbra.plant_game.engine.entity.impl.ParticleCountOwner;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.geom.instance.InstanceEmitter;
import lu.kbra.standalone.gameengine.impl.future.TaskFuture;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

@DataPath("classpath:/models/grass-small.json")
public class InstanceSmallGrassObject extends InstanceSwayGameObject implements ParticleCountOwner {

	protected volatile boolean resizing = false;
	protected final List<Vector3f> positions = new ArrayList<>();
	protected int count = 0;

	public InstanceSmallGrassObject(final String str, final InstanceEmitter ie) {
		super(str, ie);
	}

	public void addInstance(final Vector3f position) {
		synchronized (this) {
			if (this.resizing) {
				position.add(position);
				return;
			}
		}
		if (this.count >= this.instanceEmitter.getParticleCount()) {
			this.reallocate();
		}
		((Transform3D) this.instanceEmitter.getParticles()[this.count].getTransform()).translationSet(position).updateMatrix();
		PGLogic.INSTANCE.RENDER_DISPATCHER.post(() -> this.instanceEmitter.updateParticlesTransforms());
		this.count++;
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
			synchronized (InstanceSmallGrassObject.this) {
				this.resizing = false;
			}
		}).then(PGLogic.INSTANCE.WORKERS, (Runnable) () -> {
			this.positions.removeIf(v -> {
				this.addInstance(v);
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
		return "InstanceSmallGrassObject@" + System.identityHashCode(this) + " [resizing=" + this.resizing + ", positions=" + this.positions
				+ ", count=" + this.count + ", deformRatio=" + this.deformRatio + ", speedRatio=" + this.speedRatio + ", scaleRatio="
				+ this.scaleRatio + ", materialId=" + this.materialId + ", isEntityMaterialId=" + this.isEntityMaterialId
				+ ", instanceEmitter=" + this.instanceEmitter + ", objectId=" + this.objectId + ", objectIdLocation="
				+ this.objectIdLocation + ", transform=" + this.transform + ", active=" + this.active + ", name=" + this.name + "]";
	}

}
