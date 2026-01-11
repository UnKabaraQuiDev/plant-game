package lu.kbra.plant_game.engine.entity.go;

import org.joml.Vector3i;
import org.joml.Vector3ic;

import lu.kbra.plant_game.engine.entity.go.data.AttributeLocation;
import lu.kbra.plant_game.engine.entity.impl.ObjectIdOwner;
import lu.kbra.plant_game.engine.entity.impl.Transform3DOwner;
import lu.kbra.standalone.gameengine.objs.entity.Entity;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class GameObject extends Entity implements Transform3DOwner, ObjectIdOwner {

	public static final int MESH_ATTRIB_MATERIAL_ID_ID = 3;
	public static final int MESH_ATTRIB_OBJECT_ID_ID = 4;
	public static final String MESH_ATTRIB_MATERIAL_ID_NAME = "materialId";
	public static final String MESH_ATTRIB_OBJECT_ID_NAME = "objectId";

	protected Vector3ic objectId = getRandomObjectId();
	protected AttributeLocation objectIdLocation = AttributeLocation.ENTITY;

	protected Transform3D transform;

	public static Vector3ic getRandomObjectId() {
		return new Vector3i((int) System.nanoTime(), (int) System.nanoTime() % 20056, (int) System.nanoTime() % 255);
	}

	public GameObject(final String str) {
		super(str);
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
	public Vector3ic getObjectId() {
		return this.objectId;
	}

	@Override
	public void setObjectId(final Vector3ic objectId) {
		this.objectId = objectId;
	}

	@Override
	public Transform3D getTransform() {
		return this.transform;
	}

	@Override
	public void setTransform(final Transform3D ie) {
		this.transform = ie;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " [objectId=" + this.objectId + ", objectIdLocation=" + this.objectIdLocation
				+ ", getTransform()=" + this.getTransform() + ", isActive()=" + this.isActive() + "]";
	}

}
