package lu.kbra.plant_game.engine.entity.go;

import org.joml.Vector3i;
import org.joml.Vector3ic;

import lu.kbra.plant_game.engine.entity.go.impl.MaterialIdOwner;
import lu.kbra.plant_game.engine.entity.go.impl.ObjectIdOwner;
import lu.kbra.plant_game.engine.entity.impl.MeshOwner;
import lu.kbra.plant_game.engine.entity.impl.Transform3DOwner;
import lu.kbra.plant_game.engine.mesh.MaterialMesh;
import lu.kbra.plant_game.engine.mesh.TexturedMesh;
import lu.kbra.plant_game.engine.mesh.data.AttributeLocation;
import lu.kbra.plant_game.generated.ColorMaterial;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.objs.entity.Entity;
import lu.kbra.standalone.gameengine.objs.entity.components.MeshComponent;
import lu.kbra.standalone.gameengine.objs.entity.components.Transform3DComponent;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class GameObject extends Entity implements Transform3DOwner, MeshOwner, MaterialIdOwner, ObjectIdOwner {

	public static final int MESH_ATTRIB_MATERIAL_ID_ID = 3;
	public static final int MESH_ATTRIB_OBJECT_ID_ID = 4;
	public static final String MESH_ATTRIB_MATERIAL_ID_NAME = "materialId";
	public static final String MESH_ATTRIB_OBJECT_ID_NAME = "objectId";

	protected short materialId = -1;
	protected boolean isEntityMaterialId = false;
	protected Vector3ic objectId = getRandomObjectId();
	protected AttributeLocation objectIdLocation = AttributeLocation.ENTITY;

	protected MeshComponent meshComponent;
	protected Transform3DComponent transformComponent;

	public static Vector3ic getRandomObjectId() {
		return new Vector3i((int) System.nanoTime(), (int) System.nanoTime() % 20056, (int) System.nanoTime() % 255);
	}

	public GameObject(final String str, final Mesh mesh) {
		super(str);
		this.setMesh(mesh);
//		this.setTransform(transform);
//		this.setMaterialId(this.materialId);
//		this.setObjectId(this.objectId);
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

	public void setMaterial(final ColorMaterial colorMaterial) {
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

	public MeshComponent getMeshComponent() {
		return this.meshComponent;
	}

	public Transform3DComponent getTransformComponent() {
		return this.transformComponent;
	}

	@Override
	public Mesh getMesh() {
		return this.meshComponent == null ? null : this.meshComponent.getMesh();
	}

	@Override
	public Transform3D getTransform() {
		return this.transformComponent == null ? null : this.transformComponent.getTransform();
	}

	@Override
	public void setTransform(final Transform3D ie) {
		if (this.transformComponent != null) {
			if (ie == null) {
				super.removeComponent(Transform3DComponent.class);
				this.transformComponent = null;
			} else {
				this.transformComponent.setTransform(ie);
			}
		} else if (ie != null) {
			super.addComponent(this.transformComponent = new Transform3DComponent(ie));
		}
	}

	@Override
	public void setMesh(final Mesh ie) {
		if (this.meshComponent != null) {
			if (ie == null) {
				super.removeComponent(MeshComponent.class);
				this.meshComponent = null;
			} else {
				this.meshComponent.setMesh(ie);
			}
		} else if (ie != null) {
			super.addComponent(this.meshComponent = new MeshComponent(ie));
		}
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
				+ ", getTransform()=" + this.getTransform() + ", getComponents()=" + this.getComponents().size() + ", isActive()="
				+ this.isActive() + "]";
	}

}
