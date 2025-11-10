package lu.kbra.plant_game.engine.scene.ui;

import lu.kbra.plant_game.engine.entity.impl.Transform3DOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.UIObject;
import lu.kbra.standalone.gameengine.objs.entity.components.Transform3DComponent;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class OffsetUIObjectGroup extends UIObjectGroup implements Transform3DOwner {

	protected Transform3DComponent transform3dComponent;

	public OffsetUIObjectGroup(String str, UIObject... values) {
		super(str, values);
		super.addComponent(this.transform3dComponent = new Transform3DComponent());
	}

	public OffsetUIObjectGroup(String str, Transform3D transform, UIObject... values) {
		super(str, values);
		super.addComponent(this.transform3dComponent = new Transform3DComponent(transform));
	}

	public Transform3DComponent getTransform3dComponent() {
		return transform3dComponent;
	}

	@Override
	public Transform3D getTransform() {
		return transform3dComponent == null ? null : transform3dComponent.getTransform();
	}

}
