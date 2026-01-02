package lu.kbra.plant_game.engine.scene.ui.overlay;

import java.awt.Shape;

import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.entity.ui.group.UIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.impl.BoundsOwnerParentAware;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor;
import lu.kbra.plant_game.engine.scene.ui.layout.Layout;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class AnchoredBoundedUIObjectGroup extends AnchoredLayoutUIObjectGroup implements BoundsOwnerParentAware {

	public AnchoredBoundedUIObjectGroup(final String str, final Layout layout, final Anchor obj, final Anchor tar,
			final UIObject... values) {
		super(str, layout, obj, tar, values);
	}

	public AnchoredBoundedUIObjectGroup(final String str, final Layout layout, final Transform3D transform, final UIObject... values) {
		super(str, layout, transform, values);
	}

	public AnchoredBoundedUIObjectGroup(final String str, final Layout layout, final UIObjectGroup parent, final UIObject... values) {
		super(str, layout, parent, values);
	}

	@Override
	public Shape getBounds() {
		return this.getBoundsOwnerParent().getBounds();
	}

}
