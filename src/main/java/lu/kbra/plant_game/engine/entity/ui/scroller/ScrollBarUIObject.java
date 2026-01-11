package lu.kbra.plant_game.engine.entity.ui.scroller;

import org.joml.Vector2f;
import org.joml.Vector2fc;

import lu.pcy113.pclib.PCUtils;

import lu.kbra.plant_game.engine.entity.ui.FlatQuadUIObject;
import lu.kbra.plant_game.engine.mesh.TexturedQuadMesh;
import lu.kbra.plant_game.generated.ColorMaterial;
import lu.kbra.standalone.gameengine.utils.consts.Direction;

public class ScrollBarUIObject extends FlatQuadUIObject {

	protected Direction dir;
	protected Vector2f range;
	protected Vector2f size;
	protected float speed = 1f;
	protected float margin = 0.00f;

	public ScrollBarUIObject(final String str, final TexturedQuadMesh mesh) {
		super(str, mesh);
	}

	public void addScrollPosition(final float f) {
		if (!this.hasTransform() || !this.isActive()) {
			return;
		}

		if (this.dir.isVertical()) {
			this.getTransform().getTranslation().z = PCUtils
					.clampRange(this.range.x(), this.range.y(), this.getTransform().getTranslation().z() - f * this.speed);
			// TODO: this should be enforced by the layout
			this.getTransform().getTranslation().x = 1f - this.margin;
		} else if (this.dir.isHorizontal()) {
			this.getTransform().getTranslation().x = PCUtils
					.clampRange(this.range.x(), this.range.y(), this.getTransform().getTranslation().x() + f * this.speed);
			this.getTransform().getTranslation().z = 1f - this.margin;
		}

		this.getTransform().updateMatrix();
	}

	public float getScrollRatio() {
		if (!this.hasTransform()) {
			return 0;
		}

		if (this.dir.isVertical()) {
			return PCUtils.map(this.getTransform().getTranslation().z, this.range.x(), this.range.y(), 0f, 1f);
		}
		if (this.dir.isHorizontal()) {
			return PCUtils.map(this.getTransform().getTranslation().x, this.range.x(), this.range.y(), 0f, 1f);
		}

		return 0;
	}

	public float setScrollRatio(final float t) {
		if (!this.hasTransform()) {
			return 0;
		}

		if (this.dir.isVertical()) {
			this.getTransform().getTranslation().z = PCUtils.map(t, 0f, 1f, this.range.x(), this.range.y());
		} else if (this.dir.isHorizontal()) {
			this.getTransform().getTranslation().x = PCUtils.map(t, 0f, 1f, this.range.x(), this.range.y());
		}

		this.getTransform().updateMatrix();

		return 0;
	}

	public void setScrollPosition(final float t) {
		if (!this.hasTransform() || !this.isActive()) {
			return;
		}

		if (this.dir.isVertical()) {
			this.getTransform().getTranslation().z = PCUtils.clampRange(this.range.x(), this.range.y(), t);
			this.getTransform().getTranslation().x = 1f - this.margin;
			// TODO: this should be enforced by the layout
		} else if (this.dir.isHorizontal()) {
			this.getTransform().getTranslation().x = PCUtils.clampRange(this.range.x(), this.range.y(), t);
			this.getTransform().getTranslation().z = 1f - this.margin;
		}

		this.getTransform().updateMatrix();
	}

	@Override
	public void setColorMaterial(final ColorMaterial mt) {
		super.setTint(mt.getColor());
	}

	public void setScrollBounds(final Vector2fc b) {
		this.range.set(b);
	}

	public float getSpeed() {
		return this.speed;
	}

	public void setSpeed(final float speed) {
		this.speed = speed;
	}

	public Vector2fc getRange() {
		return this.range;
	}

	public void setRange(final Vector2fc v) {
		if (this.range == null) {
			this.range = new Vector2f(v);
		} else {
			final float ratio = this.getScrollRatio();
			this.range.set(v);
			this.setScrollRatio(ratio);
		}
	}

	public float getMargin() {
		return this.margin;
	}

	public void setMargin(final float margin) {
		this.margin = margin;
	}

	public Direction getDir() {
		return this.dir;
	}

	public void setDir(final Direction dir) {
		this.dir = dir;
	}

	public Vector2fc getSize() {
		return this.size;
	}

	public void setSize(final Vector2fc size) {
		this.size = new Vector2f(size);
		if (this.hasTransform()) {
			if (this.dir.isVertical()) {
				this.getTransform().scaleMul(size.x(), 1, size.y());
			} else if (this.dir.isHorizontal()) {
				this.getTransform().scaleMul(size.y(), 1, size.x());
			}
			this.getTransform().updateMatrix();
		}
	}

	@Override
	public String toString() {
		return "ScrollBarUIObject [dir=" + this.dir + ", bounds=" + this.range + ", size=" + this.size + ", color=" + this.color
				+ ", active=" + this.active + ", name=" + this.name + "]";
	}

}
