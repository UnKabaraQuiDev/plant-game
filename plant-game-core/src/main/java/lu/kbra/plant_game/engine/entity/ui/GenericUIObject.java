package lu.kbra.plant_game.engine.entity.ui;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lu.kbra.standalone.gameengine.objs.entity.Entity;
import lu.kbra.standalone.gameengine.objs.entity.ParentAwareComponent;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public abstract class GenericUIObject extends Entity implements UIObject {

	protected Transform3D transform;

	@JsonIgnore
	protected ParentAwareComponent parent;

	public GenericUIObject(final String str) {
		super(str);
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
	public ParentAwareComponent getParent() {
		return this.parent;
	}

	@Override
	public <T extends ParentAwareComponent> void setParent(final T e) {
		this.parent = e;
	}

	@Override
	public String toString() {
		return "GenericUIObject@" + System.identityHashCode(this) + " [transform=" + this.transform + ", parent=" + this.parent + "]";
	}

}
