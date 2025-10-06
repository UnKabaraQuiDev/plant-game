package lu.kbra.plant_game.engine.entity.impl;

import org.joml.Vector3i;

import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.objs.entity.Entity;
import lu.kbra.standalone.gameengine.objs.entity.components.MeshComponent;
import lu.kbra.standalone.gameengine.objs.entity.components.Transform3DComponent;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class GameObject extends Entity implements Transform3Deable {

	public static final int MESH_ATTRIB_MATERIAL_ID_ID = 3;
	public static final int MESH_ATTRIB_OBJECT_ID_ID = 4;
	public static final String MESH_ATTRIB_MATERIAL_ID_NAME = "materialId";
	public static final String MESH_ATTRIB_OBJECT_ID_NAME = "objectId";

	private short materialId = -1;
	private AttributeLocation materialIdLocation = AttributeLocation.STATIC;
	private Vector3i objectId;
	private AttributeLocation objectIdLocation = AttributeLocation.STATIC; // object id in mesh data
	private MeshComponent meshComponent;
	private Transform3DComponent transformComponent;
	private boolean transparent = false;

	public GameObject(String str, Mesh mesh) {
		this(str, mesh, null);
	}

	public GameObject(String str, Mesh mesh, Transform3D transform) {
		this(str, mesh, transform, false);
	}

	public GameObject(String str, Mesh mesh, Transform3D transform, boolean transparent) {
		this(str, mesh, transform, transparent,
				new Vector3i((int) System.nanoTime(), (int) System.nanoTime() % 20056, (int) System.nanoTime() % 255));
	}

	public GameObject(String str, Mesh mesh, Transform3D transform, boolean transparent, Vector3i objectId) {
		this(str, mesh, transform, transparent, objectId, (short) -1);
	}

	public GameObject(String str, Mesh mesh, Transform3D transform, boolean transparent, Vector3i objectId,
			short materialId) {
		super(str, new MeshComponent(mesh), transform != null ? new Transform3DComponent(transform) : null);
		if (mesh instanceof TexturedMesh) {
			materialIdLocation = AttributeLocation.TEXTURE;
		}
		this.meshComponent = super.getComponent(MeshComponent.class);
		this.transformComponent = super.getComponent(Transform3DComponent.class);
		this.transparent = transparent;
		this.objectId = objectId;
		this.materialId = materialId;
	}

	public short getMaterialId() {
		return materialId;
	}

	public void setMaterialId(short materialId) {
		this.materialId = materialId;
	}

	public AttributeLocation getObjectIdLocation() {
		return objectIdLocation;
	}

	public void setObjectIdLocation(AttributeLocation objectIdLocation) {
		this.objectIdLocation = objectIdLocation;
	}

	public AttributeLocation getMaterialIdLocation() {
		return materialIdLocation;
	}

	public void setMaterialIdLocation(AttributeLocation materialIdLocation) {
		this.materialIdLocation = materialIdLocation;
	}

	public Vector3i getObjectId() {
		return objectId;
	}

	public void setObjectId(Vector3i objectId) {
		this.objectId = objectId;
	}

	public boolean isTransparent() {
		return transparent;
	}

	public void setTransparent(boolean transparent) {
		this.transparent = transparent;
	}

	public MeshComponent getMeshComponent() {
		return meshComponent;
	}

	public Transform3DComponent getTransformComponent() {
		return transformComponent;
	}

	public Mesh getMesh() {
		return meshComponent == null ? null : meshComponent.getMesh();
	}

	@Override
	public Transform3D getTransform() {
		return transformComponent == null ? null : transformComponent.getTransform();
	}

}
