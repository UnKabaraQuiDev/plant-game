package lu.kbra.plant_game.engine.entity.ui.gradient;

import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector4f;
import org.joml.Vector4fc;

import lu.kbra.plant_game.engine.entity.ui.impl.TransparentEntity;
import lu.kbra.plant_game.engine.entity.ui.prim.QuadMeshUIObject;
import lu.kbra.plant_game.engine.mesh.TintOwner;
import lu.kbra.plant_game.engine.render.GradientDirection;
import lu.kbra.plant_game.engine.render.GradientOwner;
import lu.kbra.standalone.gameengine.geom.QuadMesh;
import lu.kbra.standalone.gameengine.utils.GameEngineUtils;

public class GradientQuadUIObject extends QuadMeshUIObject implements TintOwner, GradientOwner, TransparentEntity {

	protected Vector4f tint;
	protected Vector4f startColor;
	protected Vector4f endColor;
	protected GradientDirection gradientDirection;
	protected Vector2f gradientRange;

	public GradientQuadUIObject(final String str, final QuadMesh mesh) {
		super(str, mesh);
	}

	@Override
	public Vector4fc getTint() {
		return this.tint;
	}

	@Override
	public void setTint(final Vector4fc tint) {
		if (this.tint == null) {
			this.tint = new Vector4f(tint);
		} else {
			this.tint.set(tint);
		}
	}

	@Override
	public GradientDirection getDirection() {
		return this.gradientDirection;
	}

	@Override
	public void setDirection(final GradientDirection dir) {
		this.gradientDirection = dir;
	}

	@Override
	public Vector2f getRange() {
		return this.gradientRange;
	}

	@Override
	public void setRange(final Vector2fc range) {
		this.gradientRange = GameEngineUtils.clone(range);
	}

	@Override
	public Vector4f getStartColor() {
		return this.startColor;
	}

	@Override
	public void setStartColor(final Vector4fc color) {
		this.startColor = new Vector4f(color);
	}

	@Override
	public Vector4f getEndColor() {
		return this.endColor;
	}

	@Override
	public void setEndColor(final Vector4fc color) {
		this.endColor = new Vector4f(color);
	}

	@Override
	public String toString() {
		return "GradientQuadUIObject [tint=" + this.tint + ", startColor=" + this.startColor + ", endColor=" + this.endColor
				+ ", gradientDirection=" + this.gradientDirection + ", gradientRange=" + this.gradientRange + ", bounds=" + this.bounds
				+ ", mesh=" + this.mesh + ", transform=" + this.transform + ", active=" + this.active + ", name=" + this.name + "]";
	}

}
