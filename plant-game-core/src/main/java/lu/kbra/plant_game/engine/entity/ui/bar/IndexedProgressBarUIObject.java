package lu.kbra.plant_game.engine.entity.ui.bar;

import lu.kbra.plant_game.engine.entity.impl.IndexOwner;
import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.entity.ui.group.UIObjectGroup;
import lu.kbra.plant_game.engine.scene.ui.UIScene;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class IndexedProgressBarUIObject extends ProgressBarUIObject implements IndexOwner {

	protected int index;

	public IndexedProgressBarUIObject(final String str, final Transform3D transform, final UIObject... values) {
		super(str, transform, values);
	}

	public IndexedProgressBarUIObject(final String str, final UIObject... values) {
		super(str, values);
	}

	public IndexedProgressBarUIObject(final String str, final UIObjectGroup parent, final Transform3D transform, final UIObject... values) {
		super(str, parent, transform, values);
	}

	public IndexedProgressBarUIObject(final String str, final UIObjectGroup parent, final Transform3D transform, final int index) {
		super(str, parent, transform);
		this.index = index;
	}

	public IndexedProgressBarUIObject(final String str, final UIObjectGroup parent, final UIObject... values) {
		super(str, parent, values);
	}

	public IndexedProgressBarUIObject(
			final String str,
			final UIScene parent,
			final Transform3D transform,
			final float margin,
			final float value) {
		super(str, parent, transform, margin, value);
	}

	public IndexedProgressBarUIObject(final String str, final UIScene parent, final Transform3D transform, final UIObject... values) {
		super(str, parent, transform, values);
	}

	public IndexedProgressBarUIObject(final String str, final UIScene parent, final UIObject... values) {
		super(str, parent, values);
	}

	@Override
	public int getIndex() {
		return this.index;
	}

	public void setIndex(final int index) {
		this.index = index;
	}

	@Override
	public String toString() {
		return "IndexedProgressBarUIObject@" + System.identityHashCode(this) + " [index=" + this.index + ", margin=" + this.margin
				+ ", value=" + this.value + ", background=" + this.background + ", foreground=" + this.foreground + ", subEntitiesLock="
				+ this.subEntitiesLock + ", subEntities=" + this.subEntities + ", computedBounds=" + this.computedBounds + ", transform="
				+ this.transform + ", active=" + this.active + ", name=" + this.name + "]";
	}

}
