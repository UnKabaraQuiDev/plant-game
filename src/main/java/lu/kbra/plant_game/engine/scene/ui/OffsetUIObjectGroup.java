package lu.kbra.plant_game.engine.scene.ui;

import lu.kbra.plant_game.engine.entity.impl.Transform3DOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.TransformedBoundsOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.UIObject;
import lu.kbra.standalone.gameengine.objs.entity.components.Transform3DComponent;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class OffsetUIObjectGroup extends UIObjectGroup implements Transform3DOwner, TransformedBoundsOwner {

	protected Transform3DComponent transform3dComponent;

	public OffsetUIObjectGroup(final String str, final UIObject... values) {
		super(str, values);
		super.addComponent(this.transform3dComponent = new Transform3DComponent());
	}

	public OffsetUIObjectGroup(final String str, final UIObjectGroup parent, final UIObject... values) {
		super(str, values);
		super.addComponent(this.transform3dComponent = new Transform3DComponent());
		parent.add(this);
	}

	public OffsetUIObjectGroup(final String str, final Transform3D transform, final UIObject... values) {
		super(str, values);
		super.addComponent(this.transform3dComponent = new Transform3DComponent(transform));
	}

	public Transform3DComponent getTransform3dComponent() {
		return this.transform3dComponent;
	}

	@Override
	public Transform3D getTransform() {
		return this.transform3dComponent == null ? null : this.transform3dComponent.getTransform();
	}

}
