package lu.kbra.plant_game.engine.entity.go;

import org.joml.Vector3ic;

import lu.kbra.plant_game.engine.entity.go.data.AttributeLocation;
import lu.kbra.standalone.gameengine.objs.entity.Entity;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class GenericGameObject extends Entity implements GameObject {

	protected Vector3ic objectId = GameObject.getRandomObjectId();
	protected AttributeLocation objectIdLocation = AttributeLocation.ENTITY;

	protected Transform3D transform;

	public GenericGameObject(final String str) {
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
	public GenericGameObject clone() {
		return (GenericGameObject) super.clone();
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " [objectId=" + this.objectId + ", objectIdLocation=" + this.objectIdLocation
				+ ", getTransform()=" + this.getTransform() + ", isActive()=" + this.isActive() + "]";
	}

}
