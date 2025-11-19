package lu.kbra.plant_game.engine.util;

import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector4f;
import org.joml.Vector4fc;

import lu.kbra.plant_game.engine.render.GradientDirection;
import lu.kbra.plant_game.engine.render.GradientQuadMesh;
import lu.kbra.standalone.gameengine.geom.LoadedQuadMesh;
import lu.kbra.standalone.gameengine.graph.material.Material;
import lu.kbra.standalone.gameengine.utils.GameEngineUtils;

public class LoadedGradientQuadMesh extends LoadedQuadMesh implements GradientQuadMesh {

	protected Vector4f startColor;
	protected Vector4f endColor;
	protected GradientDirection gradientDirection;
	protected Vector2f gradientRange;

	public LoadedGradientQuadMesh(final String name, final Material material, final Vector2fc size) {
		super(name, material, size);
	}

	public LoadedGradientQuadMesh(
			final String name,
			final Material material,
			final Vector2fc size,
			final Vector4fc startColor,
			final Vector4fc endColor,
			final GradientDirection gradientDirection,
			final Vector2fc gradientRange) {
		super(name, material, size);
		this.startColor = GameEngineUtils.clone(startColor);
		this.endColor = GameEngineUtils.clone(endColor);
		this.gradientDirection = gradientDirection;
		this.gradientRange = GameEngineUtils.clone(gradientRange);
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
		this.startColor = GameEngineUtils.clone(color);
	}

	@Override
	public Vector4f getEndColor() {
		return this.endColor;
	}

	@Override
	public void setEndColor(final Vector4fc color) {
		this.endColor = GameEngineUtils.clone(color);
	}

}
