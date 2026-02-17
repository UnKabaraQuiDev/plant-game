package lu.kbra.plant_game.base.entity.go.obj_inst.grass;

import org.joml.Vector3f;

import lu.kbra.plant_game.PGLogic;
import lu.kbra.plant_game.engine.entity.go.InstanceSwayGameObject;
import lu.kbra.plant_game.engine.entity.impl.ParticleCountOwner;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.geom.instance.InstanceEmitter;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

@DataPath("classpath:/models/grass-small.json")
public class InstanceSmallGrassObject extends InstanceSwayGameObject implements ParticleCountOwner {

	protected int count = 0;

	public InstanceSmallGrassObject(final String str, final InstanceEmitter ie) {
		super(str, ie);
	}

	public void addInstance(final Vector3f position) {
		if (this.count >= this.instanceEmitter.getParticleCount()) {
			this.reallocate();
		}
		((Transform3D) this.instanceEmitter.getParticles()[this.count].getTransform()).translationSet(position).updateMatrix();
		PGLogic.INSTANCE.RENDER_DISPATCHER.post(() -> this.instanceEmitter.updateParticlesTransforms());
		this.count++;
	}

	private void reallocate() {
		PGLogic.INSTANCE.RENDER_DISPATCHER
				.post(() -> super.instanceEmitter.resize(this.instanceEmitter.getParticleCount() + 32, i -> new Transform3D()));
	}

	@Override
	public int getParticleCount() {
		return this.count;
	}

//	@Override
//	public void setCount(final int count) {
//		this.count = Math.min(count, this.instanceEmitter.getParticleCount());
//	}

	@Override
	public String toString() {
		return "InstanceSmallGrassObject@" + System.identityHashCode(this) + " [count=" + this.count + ", deformRatio=" + this.deformRatio
				+ ", speedRatio=" + this.speedRatio + ", scaleRatio=" + this.scaleRatio + ", materialId=" + this.materialId
				+ ", isEntityMaterialId=" + this.isEntityMaterialId + ", instanceEmitter=" + this.instanceEmitter + ", objectId="
				+ this.objectId + ", objectIdLocation=" + this.objectIdLocation + ", transform=" + this.transform + ", active="
				+ this.active + ", name=" + this.name + "]";
	}

}
