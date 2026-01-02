package lu.kbra.plant_game.engine.entity.ui.group;

import lu.kbra.plant_game.engine.entity.impl.Transform3DOwner;
import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.entity.ui.impl.TransformedBoundsOwner;
import lu.kbra.plant_game.engine.scene.ui.UIScene;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class OffsetUIObjectGroup extends UIObjectGroup implements Transform3DOwner, TransformedBoundsOwner {

	protected Transform3D transform;

	public OffsetUIObjectGroup(final String str, final UIObject... values) {
		super(str, values);
		this.setTransform(new Transform3D());
	}

	public OffsetUIObjectGroup(final String str, final UIObjectGroup parent, final UIObject... values) {
		super(str, parent, values);
		this.setTransform(new Transform3D());
	}

	public OffsetUIObjectGroup(final String str, final UIScene parent, final UIObject... values) {
		super(str, parent, values);
		this.setTransform(new Transform3D());
	}

	public OffsetUIObjectGroup(final String str, final UIObjectGroup parent, final Transform3D transform, final UIObject... values) {
		super(str, parent, values);
		this.setTransform(transform);
	}

	public OffsetUIObjectGroup(final String str, final UIScene parent, final Transform3D transform, final UIObject... values) {
		super(str, parent, values);
		this.setTransform(transform);
	}

	public OffsetUIObjectGroup(final String str, final Transform3D transform, final UIObject... values) {
		super(str, values);
		this.setTransform(transform);
	}

	@Override
	public Transform3D getTransform() {
		return this.transform;
	}

	@Override
	public void setTransform(final Transform3D transform) {
		this.transform = transform;
	}

	@Override
	public String toString() {
		return "OffsetUIObjectGroup [transform=" + this.transform + ", bounds=" + this.bounds + ", active=" + this.active + ", name="
				+ this.name + "]";
	}

}
