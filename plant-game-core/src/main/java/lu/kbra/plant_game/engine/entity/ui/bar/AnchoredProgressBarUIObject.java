package lu.kbra.plant_game.engine.entity.ui.bar;

import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.entity.ui.group.UIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.impl.AnchorOwner;
import lu.kbra.plant_game.engine.scene.ui.UIScene;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class AnchoredProgressBarUIObject extends ProgressBarUIObject implements AnchorOwner {

	protected Anchor objectAnchor;
	protected Anchor targetAnchor;

	public AnchoredProgressBarUIObject(final String str, final Transform3D transform, final UIObject... values) {
		super(str, transform, values);
	}

	public AnchoredProgressBarUIObject(final String str, final UIObject... values) {
		super(str, values);
	}

	public AnchoredProgressBarUIObject(
			final String str,
			final UIObjectGroup parent,
			final Transform3D transform,
			final UIObject... values) {
		super(str, parent, transform, values);
	}

	public AnchoredProgressBarUIObject(final String str, final UIObjectGroup parent, final UIObject... values) {
		super(str, parent, values);
	}

	public AnchoredProgressBarUIObject(
			final String str,
			final UIScene parent,
			final Transform3D transform,
			final float margin,
			final float value) {
		super(str, parent, transform, margin, value);
	}

	public AnchoredProgressBarUIObject(final String str, final UIScene parent, final Transform3D transform, final UIObject... values) {
		super(str, parent, transform, values);
	}

	public AnchoredProgressBarUIObject(final String str, final UIScene parent, final UIObject... values) {
		super(str, parent, values);
	}

	public AnchoredProgressBarUIObject(final String str, final Anchor obj, final Anchor tar, final UIObject... values) {
		super(str, values);
		this.objectAnchor = obj;
		this.targetAnchor = tar;
	}

	public AnchoredProgressBarUIObject(
			final String str,
			final UIScene parent,
			final Transform3D transform,
			final Anchor obj,
			final Anchor tar,
			final float margin,
			final float value) {
		super(str, parent, transform, margin, value);
		this.objectAnchor = obj;
		this.targetAnchor = tar;
	}

	@Override
	public Anchor getObjectAnchor() {
		return this.objectAnchor;
	}

	@Override
	public void setObjectAnchor(final Anchor a) {
		this.objectAnchor = a;
	}

	@Override
	public Anchor getTargetAnchor() {
		return this.targetAnchor;
	}

	@Override
	public void setTargetAnchor(final Anchor a) {
		this.targetAnchor = a;
	}

}
