package lu.kbra.plant_game.engine.entity.go.impl;

import org.joml.Vector3i;

import lu.kbra.plant_game.engine.entity.impl.MeshOwner;
import lu.kbra.plant_game.engine.entity.impl.Transform3DOwner;
import lu.kbra.plant_game.engine.mesh.MaterialMesh;
import lu.kbra.plant_game.engine.mesh.TexturedMesh;
import lu.kbra.plant_game.engine.mesh.data.AttributeLocation;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.objs.entity.Entity;
import lu.kbra.standalone.gameengine.objs.entity.components.MeshComponent;
import lu.kbra.standalone.gameengine.objs.entity.components.Transform3DComponent;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class GameObject extends Entity implements Transform3DOwner, MeshOwner {

	public static final int MESH_ATTRIB_MATERIAL_ID_ID = 3;
	public static final int MESH_ATTRIB_OBJECT_ID_ID = 4;
	public static final String MESH_ATTRIB_MATERIAL_ID_NAME = "materialId";
	public static final String MESH_ATTRIB_OBJECT_ID_NAME = "objectId";

	protected short materialId = -1;
	protected boolean entityMaterialId = false;
	protected Vector3i objectId;
	protected AttributeLocation objectIdLocation = AttributeLocation.ENTITY; // object id in mesh data
	protected MeshComponent meshComponent;
	protected Transform3DComponent transformComponent;

	public GameObject(final String str, final Mesh mesh) {
		this(str, mesh, null);
	}

	public GameObject(final String str, final Mesh mesh, final Transform3D transform) {
		this(str, mesh, transform, new Vector3i((int) System.nanoTime(), (int) System.nanoTime() % 20056, (int) System.nanoTime() % 255));
	}

	public GameObject(final String str, final Mesh mesh, final Transform3D transform, final Vector3i objectId) {
		this(str, mesh, transform, objectId, (short) -1);
	}

	public GameObject(final String str, final Mesh mesh, final Transform3D transform, final Vector3i objectId, final short materialId) {
		super(str, mesh == null ? null : new MeshComponent(mesh), transform != null ? new Transform3DComponent(transform) : null);
		if (mesh instanceof TexturedMesh || mesh instanceof MaterialMesh) {
			this.entityMaterialId = false;
		} else if (materialId != -1) {
			this.entityMaterialId = true;
		}
		this.meshComponent = super.getComponent(MeshComponent.class);
		this.transformComponent = super.getComponent(Transform3DComponent.class);
		this.objectId = objectId;
		this.materialId = materialId;
	}

	public short getMaterialId() {
		return this.materialId;
	}

	public void setMaterialId(final short materialId) {
		this.materialId = materialId;
	}

	public AttributeLocation getObjectIdLocation() {
		return this.objectIdLocation;
	}

	public void setObjectIdLocation(final AttributeLocation objectIdLocation) {
		this.objectIdLocation = objectIdLocation;
	}

	public boolean isEntityMaterialId() {
		return this.entityMaterialId;
	}

	public void setEntityMaterialId(final boolean entityMaterialIdLocation) {
		this.entityMaterialId = entityMaterialIdLocation;
	}

	public Vector3i getObjectId() {
		return this.objectId;
	}

	public void setObjectId(final Vector3i objectId) {
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
	public String toString() {
		return this.getClass().getSimpleName() + " [materialId=" + this.materialId + ", entityMaterialId=" + this.entityMaterialId
				+ ", objectId=" + this.objectId + ", objectIdLocation=" + this.objectIdLocation + ", getMesh()=" + this.getMesh()
				+ ", getTransform()=" + this.getTransform() + ", getComponents()=" + this.getComponents().size() + ", isActive()="
				+ this.isActive() + "]";
	}

}
