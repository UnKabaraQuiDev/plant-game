package lu.kbra.plant_game.base.entity.go.obj_inst.grass;

import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.geom.instance.InstanceEmitter;

@DataPath("classpath:/models/grass-small.json")
public class InstanceSmallGrassObject extends ResizableInstanceSwayGameObject {

	public InstanceSmallGrassObject(final String str, final InstanceEmitter ie) {
		super(str, ie);
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
