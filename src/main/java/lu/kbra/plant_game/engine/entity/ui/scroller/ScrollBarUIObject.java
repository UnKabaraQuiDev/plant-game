package lu.kbra.plant_game.engine.entity.ui.scroller;

import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector4f;

import lu.pcy113.pclib.PCUtils;

import lu.kbra.plant_game.engine.mesh.TexturedQuadMesh;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.plant_game.generated.ColorMaterial;
import lu.kbra.standalone.gameengine.utils.consts.Direction;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

@DataPath("")
public class ScrollBarUIObject extends FlatQuadUIObject {

	protected final Direction dir;
	protected final Vector2f scrollBounds;
	protected final Vector2f size;
	protected float speed = 1f;

	public ScrollBarUIObject(
			final String str,
			final TexturedQuadMesh mesh,
			final Transform3D transform,
			final Vector4f color,
			final Direction dir,
			final Vector2f bounds,
			final Vector2f size,
			final float speed) {
		super(str, mesh, transform, color);
		if (this.hasTransform()) {
			if (dir.isVertical()) {
				this.getTransform().scaleMul(size.x, 1, size.y);
			} else if (dir.isHorizontal()) {
				this.getTransform().scaleMul(size.y, 1, size.x);
			}
			this.getTransform().updateMatrix();
		}
		this.dir = dir;
		this.scrollBounds = bounds;
		this.size = size;
		this.speed = speed;
	}

	public ScrollBarUIObject(
			final String str,
			final TexturedQuadMesh mesh,
			final Transform3D transform,
			final ColorMaterial mt,
			final Direction dir,
			final Vector2f bounds,
			final Vector2f size) {
		this(str, mesh, transform, new Vector4f(mt.getColor()), dir, bounds, size, 1f);
	}

	public ScrollBarUIObject(
			final String str,
			final TexturedQuadMesh mesh,
			final Transform3D transform,
			final ColorMaterial mt,
			final Direction dir,
			final Vector2f bounds,
			final Vector2f size,
			final float speed) {
		this(str, mesh, transform, new Vector4f(mt.getColor()), dir, bounds, size, speed);
	}

	public ScrollBarUIObject(
			final String str,
			final TexturedQuadMesh mesh,
			final Transform3D transform,
			final Vector4f color,
			final Direction dir,
			final Vector2f bounds,
			final Vector2f size) {
		this(str, mesh, transform, color, dir, bounds, size, 1f);
	}

	public void setColorMaterial(final ColorMaterial mt) {
		super.setTint(mt.getColor());
	}

	public void setScrollBounds(final Vector2fc b) {
		this.scrollBounds.set(b);
	}

	public Vector2f getScrollBounds() {
		return this.scrollBounds;
	}

	public float getSpeed() {
		return this.speed;
	}

	public void setSpeed(final float speed) {
		this.speed = speed;
	}

	public void addScrollPosition(final float f) {
		if (!this.hasTransform() || !this.isActive()) {
			return;
		}

		if (this.dir.isVertical()) {
			this.getTransform().getTranslation().z = PCUtils
					.clampRange(this.scrollBounds.x(), this.scrollBounds.y(), this.getTransform().getTranslation().z() - f * this.speed);
			this.getTransform().getTranslation().x = 1f - this.getBounds().width * this.getTransform().getScale().x;
		} else if (this.dir.isHorizontal()) {
			this.getTransform().getTranslation().x = PCUtils
					.clampRange(this.scrollBounds.x(), this.scrollBounds.y(), this.getTransform().getTranslation().x() + f * this.speed);
			this.getTransform().getTranslation().z = 1f - this.getBounds().height * this.getTransform().getScale().z;
		}

		this.getTransform().updateMatrix();
	}

	public float getScrollRatio() {
		if (!this.hasTransform()) {
			return 0;
		}

		if (this.dir.isVertical()) {
			return PCUtils.map(this.getTransform().getTranslation().z, this.scrollBounds.x(), this.scrollBounds.y(), 0f, 1f);
		}
		if (this.dir.isHorizontal()) {
			return PCUtils.map(this.getTransform().getTranslation().x, this.scrollBounds.x(), this.scrollBounds.y(), 0f, 1f);
		}

		return 0;
	}

	public float setScrollRatio(final float t) {
		if (!this.hasTransform()) {
			return 0;
		}

		if (this.dir.isVertical()) {
			this.getTransform().getTranslation().z = PCUtils.map(t, 0f, 1f, this.scrollBounds.x(), this.scrollBounds.y());
		} else if (this.dir.isHorizontal()) {
			this.getTransform().getTranslation().x = PCUtils.map(t, 0f, 1f, this.scrollBounds.x(), this.scrollBounds.y());
		}

		this.getTransform().updateMatrix();

		return 0;
	}

	public void setScrollPosition(final float t) {
		if (!this.hasTransform() || !this.isActive()) {
			return;
		}

		if (this.dir.isVertical()) {
			this.getTransform().getTranslation().z = PCUtils.clampRange(this.scrollBounds.x(), this.scrollBounds.y(), t);
			this.getTransform().getTranslation().x = 1f - this.getBounds().width * this.getTransform().getScale().x;
			// TODO: this should be enforced by the layout
		} else if (this.dir.isHorizontal()) {
			this.getTransform().getTranslation().x = PCUtils.clampRange(this.scrollBounds.x(), this.scrollBounds.y(), t);
			this.getTransform().getTranslation().z = 1f - this.getBounds().height * this.getTransform().getScale().z;
		}

		this.getTransform().updateMatrix();
	}

	@Override
	public String toString() {
		return "ScrollBarUIObject [dir=" + this.dir + ", bounds=" + this.scrollBounds + ", size=" + this.size + ", color=" + this.color
				+ ", active=" + this.active + ", name=" + this.name + "]";
	}

}
