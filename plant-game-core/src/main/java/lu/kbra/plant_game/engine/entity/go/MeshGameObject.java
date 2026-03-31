package lu.kbra.plant_game.engine.entity.go;

import org.joml.Vector3ic;

import lu.kbra.plant_game.engine.entity.go.data.AttributeLocation;
import lu.kbra.plant_game.engine.entity.impl.MaterialIdOwner;
import lu.kbra.plant_game.engine.entity.impl.MeshOwner;
import lu.kbra.plant_game.engine.mesh.MaterialMesh;
import lu.kbra.plant_game.engine.mesh.TexturedMesh;
import lu.kbra.plant_game.generated.ColorMaterial;
import lu.kbra.standalone.gameengine.geom.Mesh;

public class MeshGameObject extends GenericGameObject implements MeshOwner, MaterialIdOwner {

	public static final int MESH_ATTRIB_MATERIAL_ID_ID = 3;
	public static final int MESH_ATTRIB_OBJECT_ID_ID = 4;
	public static final String MESH_ATTRIB_MATERIAL_ID_NAME = "materialId";
	public static final String MESH_ATTRIB_OBJECT_ID_NAME = "objectId";

	protected short materialId = -1;
	protected boolean isEntityMaterialId = false;

	protected Mesh mesh;

	public MeshGameObject(final String str, final Mesh mesh) {
		super(str);
		this.setMesh(mesh);
	}

	@Override
	public short getMaterialId() {
		return this.materialId;
	}

	@Override
	public void setMaterialId(final short materialId) {
		this.materialId = materialId;

		final Mesh mesh = this.getMesh();
		if (this.materialId > 0) {
			this.isEntityMaterialId = true;
		} else if (mesh != null && (mesh instanceof TexturedMesh || mesh instanceof MaterialMesh)) {
			this.isEntityMaterialId = false;
		}
	}

	@Override
	public void setColorMaterial(final ColorMaterial colorMaterial) {
		this.setMaterialId(colorMaterial.getId());
	}

	@Override
	public AttributeLocation getObjectIdLocation() {
		return this.objectIdLocation;
	}

	@Override
	public void setObjectIdLocation(final AttributeLocation objectIdLocation) {
		this.objectIdLocation = objectIdLocation;
	}

	@Override
	public boolean isEntityMaterialId() {
		return this.isEntityMaterialId;
	}

	@Override
	public void setIsEntityMaterialId(final boolean entityMaterialIdLocation) {
		this.isEntityMaterialId = entityMaterialIdLocation;
	}

	@Override
	public Vector3ic getObjectId() {
		return this.objectId;
	}

	@Override
	public void setObjectId(final Vector3ic objectId) {
		this.objectId = objectId;
	}

	@Override
	public Mesh getMesh() {
		return this.mesh;
	}

	@Override
	public void setMesh(final Mesh ie) {
		this.mesh = ie;
		if (ie != null) {
			if (ie instanceof TexturedMesh || ie instanceof MaterialMesh) {
				this.isEntityMaterialId = false;
			} else if (this.materialId > 0) {
				this.isEntityMaterialId = true;
			} else {
				this.isEntityMaterialId = false;
			}
		}
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " [materialId=" + this.materialId + ", entityMaterialId=" + this.isEntityMaterialId
				+ ", objectId=" + this.objectId + ", objectIdLocation=" + this.objectIdLocation + ", getMesh()=" + this.getMesh()
				+ ", getTransform()=" + this.getTransform() + ", isActive()=" + this.isActive() + "]";
	}

}
