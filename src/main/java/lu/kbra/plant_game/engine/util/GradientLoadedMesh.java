package lu.kbra.plant_game.engine.util;

import org.joml.Vector2f;
import org.joml.Vector4f;

import lu.kbra.plant_game.engine.render.GradientDirection;
import lu.kbra.plant_game.engine.render.GradientMesh;
import lu.kbra.standalone.gameengine.cache.attrib.AttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.UIntAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.Vec3fAttribArray;
import lu.kbra.standalone.gameengine.geom.LoadedMesh;
import lu.kbra.standalone.gameengine.graph.material.Material;

public class GradientLoadedMesh extends LoadedMesh implements GradientMesh {

	protected Vector4f startColor;
	protected Vector4f endColor;
	protected GradientDirection gradientDirection;
	protected Vector2f gradientRange;

	public GradientLoadedMesh(String name, Material material, Vec3fAttribArray vertices, UIntAttribArray indices, AttribArray... attribs) {
		super(name, material, vertices, indices, attribs);
	}

	public GradientLoadedMesh(
			String name,
			Material material,
			Vec3fAttribArray vertices,
			UIntAttribArray indices,
			Vector4f startColor,
			Vector4f endColor,
			GradientDirection gradientDirection,
			Vector2f gradientRange,
			AttribArray... attribs) {
		super(name, material, vertices, indices, attribs);
		this.startColor = startColor;
		this.endColor = endColor;
		this.gradientDirection = gradientDirection;
		this.gradientRange = gradientRange;
	}

	@Override
	public GradientDirection getDirection() {
		return gradientDirection;
	}

	@Override
	public void setDirection(GradientDirection dir) {
		this.gradientDirection = dir;
	}

	@Override
	public Vector2f getRange() {
		return gradientRange;
	}

	@Override
	public void setRange(Vector2f range) {
		this.gradientRange = range;
	}

	@Override
	public Vector4f getStartColor() {
		return startColor;
	}

	@Override
	public void setStartColor(Vector4f color) {
		this.startColor = color;
	}

	@Override
	public Vector4f getEndColor() {
		return endColor;
	}

	@Override
	public void setEndColor(Vector4f color) {
		this.endColor = color;
	}

}
