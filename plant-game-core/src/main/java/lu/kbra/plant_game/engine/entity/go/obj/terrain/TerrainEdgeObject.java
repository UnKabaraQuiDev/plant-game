package lu.kbra.plant_game.engine.entity.go.obj.terrain;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

import lu.kbra.plant_game.engine.entity.go.MeshGameObject;
import lu.kbra.plant_game.generated.ColorMaterial;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

@JsonIgnoreType
public class TerrainEdgeObject extends MeshGameObject {

	public TerrainEdgeObject(final String str, final Mesh mesh) {
		super(str, mesh);
		super.setTransform(new Transform3D());
		super.setColorMaterial(ColorMaterial.BLACK);
	}

	@Override
	public String toString() {
		return "TerrainEdgeObject@" + System.identityHashCode(this) + " [materialId=" + this.materialId + ", isEntityMaterialId="
				+ this.isEntityMaterialId + ", objectId=" + this.objectId + ", objectIdLocation=" + this.objectIdLocation + ", mesh="
				+ this.mesh + ", transform=" + this.transform + ", active=" + this.active + ", name=" + this.name + "]";
	}

}
