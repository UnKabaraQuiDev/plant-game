package lu.kbra.plant_game.base.entity.go.obj_inst.grass;

import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.geom.instance.InstanceEmitter;

@DataPath("classpath:/models/grass-small.json")
public class InstanceSmallGrassObject extends GrowingInstanceGameObject {

	public InstanceSmallGrassObject(final String str, final InstanceEmitter ie) {
		super(str, ie);
		System.err.println("small grass got: " + ie);
	}

	@Override
	public String toString() {
		return "InstanceSmallGrassObject@" + System.identityHashCode(this) + " [lock=" + this.lock + ", resizing=" + this.resizing
				+ ", pending=" + this.pending + ", dirty=" + this.dirty + ", count=" + this.count + ", deformRatio=" + this.deformRatio
				+ ", speedRatio=" + this.speedRatio + ", scaleRatio=" + this.scaleRatio + ", materialId=" + this.materialId
				+ ", isEntityMaterialId=" + this.isEntityMaterialId + ", instanceEmitter=" + this.instanceEmitter + ", objectId="
				+ this.objectId + ", objectIdLocation=" + this.objectIdLocation + ", transform=" + this.transform + ", active="
				+ this.active + ", name=" + this.name + "]";
	}

}
