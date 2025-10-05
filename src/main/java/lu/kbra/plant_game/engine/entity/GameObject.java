package lu.kbra.plant_game.engine.entity;

import org.joml.Vector3i;

import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.objs.entity.Entity;
import lu.kbra.standalone.gameengine.objs.entity.components.MeshComponent;
import lu.kbra.standalone.gameengine.objs.entity.components.Transform3DComponent;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class GameObject extends Entity {

	public static final int MESH_ATTRIB_MATERIAL_ID_ID = 3;
	public static final int MESH_ATTRIB_OBJECT_ID_ID = 4;
	public static final String MESH_ATTRIB_MATERIAL_ID_NAME = "materialId";
	public static final String MESH_ATTRIB_OBJECT_ID_NAME = "objectId";

	private byte materialId = -1;
	private boolean compositeMaterialId = false; // material in mesh data
	private Vector3i objectId = new Vector3i((int) System.nanoTime(), (int) System.nanoTime() % 20056, (int) System.nanoTime() % 255);
	private boolean compositeObjectId = false; // object id in mesh data
	private MeshComponent meshComponent;
	private Transform3DComponent transformComponent;
	private boolean transparent = false;

	public GameObject(String str, Mesh mesh) {
		super(str, new MeshComponent(mesh));
	}

	public GameObject(String str, Mesh mesh, Transform3D transform) {
		super(str, new MeshComponent(mesh), new Transform3DComponent(transform));
	}

	public GameObject(String str, Mesh mesh, Transform3D transform, boolean transparent) {
		super(str, new MeshComponent(mesh), new Transform3DComponent(transform));
		this.transparent = transparent;
	}

	public GameObject(String str, Mesh mesh, Transform3D transform, boolean transparent, Vector3i objectId, byte materialId) {
		super(str, new MeshComponent(mesh), new Transform3DComponent(transform));
		this.transparent = transparent;
		this.objectId = objectId;
		this.materialId = materialId;
	}

	public boolean isCompositeMaterialId() {
		return compositeMaterialId;
	}

	public void setCompositeMaterialId(boolean compositeMaterial) {
		this.compositeMaterialId = compositeMaterial;
	}

	public boolean isCompositeObjectId() {
		return compositeObjectId;
	}

	public void setCompositeObjectId(boolean compositeObjectId) {
		this.compositeObjectId = compositeObjectId;
	}

	public byte getMaterialId() {
		return materialId;
	}

	public void setMaterialId(byte materialId) {
		this.materialId = materialId;
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

	public Transform3D getTransform() {
		return transformComponent == null ? null : transformComponent.getTransform();
	}

}
