package lu.kbra.plant_game.engine.util;

import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector4f;
import org.joml.Vector4fc;

import lu.kbra.plant_game.engine.render.GradientDirection;
import lu.kbra.plant_game.engine.render.GradientMesh;
import lu.kbra.standalone.gameengine.cache.attrib.AttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.UIntAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.Vec3fAttribArray;
import lu.kbra.standalone.gameengine.geom.LoadedMesh;
import lu.kbra.standalone.gameengine.graph.material.Material;
import lu.kbra.standalone.gameengine.utils.GameEngineUtils;

public class GradientLoadedMesh extends LoadedMesh implements GradientMesh {

	protected Vector4f startColor;
	protected Vector4f endColor;
	protected GradientDirection gradientDirection;
	protected Vector2f gradientRange;

	public GradientLoadedMesh(
			final String name,
			final Material material,
			final Vec3fAttribArray vertices,
			final UIntAttribArray indices,
			final AttribArray... attribs) {
		super(name, material, vertices, indices, attribs);
	}

	public GradientLoadedMesh(
			final String name,
			final Material material,
			final Vec3fAttribArray vertices,
			final UIntAttribArray indices,
			final Vector4f startColor,
			final Vector4f endColor,
			final GradientDirection gradientDirection,
			final Vector2f gradientRange,
			final AttribArray... attribs) {
		super(name, material, vertices, indices, attribs);
		this.startColor = startColor;
		this.endColor = endColor;
		this.gradientDirection = gradientDirection;
		this.gradientRange = gradientRange;
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
