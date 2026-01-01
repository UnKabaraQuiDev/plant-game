package lu.kbra.plant_game.engine.entity.ui;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import lu.kbra.plant_game.engine.entity.impl.Transform3DOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.TransformedBoundsOwner;
import lu.kbra.standalone.gameengine.objs.entity.Entity;
import lu.kbra.standalone.gameengine.objs.entity.ParentAware;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public abstract class UIObject extends Entity implements Transform3DOwner, TransformedBoundsOwner, ParentAware {

	public static final Shape SQUARE_1_UNIT = new Rectangle2D.Float(-0.5f, -0.5f, 1f, 1f);

	protected Transform3D transform;

	protected Object parent;

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
	public Object getParent() {
		return this.parent;
	}

	@Override
	public void setParent(final Object e) {
		this.parent = e;
	}

	@Override
	public String toString() {
		return "UIObject [transform=" + this.transform + ", parent=" + this.parent + ", active=" + this.active + ", name=" + this.name
				+ "]";
	}

}
