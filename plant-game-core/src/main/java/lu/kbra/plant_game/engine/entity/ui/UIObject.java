package lu.kbra.plant_game.engine.entity.ui;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import lu.kbra.plant_game.engine.entity.impl.Transform3DOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.TransformedBoundsOwner;
import lu.kbra.standalone.gameengine.objs.entity.Entity;
import lu.kbra.standalone.gameengine.objs.entity.ParentAwareComponent;
import lu.kbra.standalone.gameengine.objs.entity.ParentAwareNode;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public abstract class UIObject extends Entity implements Transform3DOwner, TransformedBoundsOwner, ParentAwareNode {

	public static final Shape SQUARE_1_UNIT = new Rectangle2D.Float(-0.5f, -0.5f, 1f, 1f);

	protected Transform3D transform;

	protected ParentAwareComponent parent;

	public UIObject(final String str) {
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
		return "UIObject@" + System.identityHashCode(this) + " [transform=" + this.transform + ", active=" + this.active + ", name="
				+ this.name + "]";
	}

}
