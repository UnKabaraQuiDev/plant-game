package lu.kbra.plant_game.engine.entity.ui.texture;

import javax.swing.GroupLayout.Alignment;

import org.joml.Vector2f;
import org.joml.Vector4f;

import lu.kbra.plant_game.engine.entity.ui.impl.QuadUIObject;
import lu.kbra.plant_game.engine.entity.ui.impl.TransparentEntity;
import lu.kbra.plant_game.engine.mesh.TintOwner;
import lu.kbra.plant_game.engine.render.GradientDirection;
import lu.kbra.plant_game.engine.render.GradientMesh;
import lu.kbra.plant_game.engine.render.GradientMeshComponent;
import lu.kbra.plant_game.engine.render.GradientOwner;
import lu.kbra.plant_game.engine.render.GradientQuadMesh;
import lu.kbra.plant_game.engine.util.DataPath;
import lu.kbra.standalone.gameengine.utils.GameEngineUtils;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

@DataPath("")
public class GradientQuadUIObject extends QuadUIObject implements TintOwner, GradientOwner, TransparentEntity {

	protected Vector4f tint;
	protected Vector4f startColor;
	protected Vector4f endColor;
	protected GradientDirection gradientDirection;
	protected Vector2f gradientRange;

	protected GradientMeshComponent gradientMeshComponent;

	public GradientQuadUIObject(String str, GradientQuadMesh gradientMesh) {
		this(str, gradientMesh, null, null, null, null, null, null);
	}

	public GradientQuadUIObject(String str, GradientQuadMesh gradientMesh, GradientDirection dir) {
		this(str, gradientMesh, null, dir, null, null, null, null);
	}

	public GradientQuadUIObject(String str, GradientQuadMesh gradientMesh, GradientDirection dir, Vector4f tint) {
		this(str, gradientMesh, null, dir, null, tint, null, null);
	}

	public GradientQuadUIObject(String str, GradientQuadMesh gradientMesh, GradientDirection dir, Vector2f gradientRange, Vector4f tint) {
		this(str, gradientMesh, null, dir, null, tint, null, null);
	}

	public GradientQuadUIObject(String str, GradientQuadMesh gradientMesh, GradientDirection dir, Vector4f start, Vector4f end) {
		this(str, gradientMesh, null, dir, null, start, end);
	}

	public GradientQuadUIObject(
			String str,
			GradientQuadMesh gradientMesh,
			GradientDirection dir,
			Vector2f gradientRange,
			Vector4f start,
			Vector4f end) {
		this(str, gradientMesh, null, dir, gradientRange, null, start, end);
	}

	public GradientQuadUIObject(
			String str,
			GradientQuadMesh gradientMesh,
			GradientDirection dir,
			Vector2f gradientRange,
			Vector4f tint,
			Vector4f start,
			Vector4f end) {
		this(str, gradientMesh, null, dir, gradientRange, tint, start, end);
	}

	public GradientQuadUIObject(String str, GradientQuadMesh gradientMesh, Transform3D transform) {
		this(str, gradientMesh, transform, null, null, null, null, null);
	}

	public GradientQuadUIObject(String str, GradientQuadMesh gradientMesh, Transform3D transform, GradientDirection dir) {
		this(str, gradientMesh, transform, dir, null, null, null, null);
	}

	public GradientQuadUIObject(String str, GradientQuadMesh gradientMesh, Transform3D transform, GradientDirection dir, Vector4f tint) {
		this(str, gradientMesh, transform, dir, null, tint, null, null);
	}

	public GradientQuadUIObject(
			String str,
			GradientQuadMesh gradientMesh,
			Transform3D transform,
			GradientDirection dir,
			Vector2f gradientRange,
			Vector4f tint) {
		this(str, gradientMesh, transform, dir, gradientRange, tint, null, null);
	}

	public GradientQuadUIObject(
			String str,
			GradientQuadMesh gradientMesh,
			Transform3D transform,
			GradientDirection dir,
			Vector4f start,
			Vector4f end) {
		this(str, gradientMesh, transform, dir, null, null, start, end);
	}

	public GradientQuadUIObject(
			String str,
			GradientQuadMesh gradientMesh,
			Transform3D transform,
			GradientDirection dir,
			Vector2f gradientRange,
			Vector4f start,
			Vector4f end) {
		this(str, gradientMesh, transform, dir, gradientRange, null, start, end);
	}

	public GradientQuadUIObject(
			String str,
			GradientQuadMesh gradientMesh,
			Transform3D transform,
			GradientDirection dir,
			Vector2f gradientRange,
			Vector4f tint,
			Vector4f start,
			Vector4f end) {
		super(str, null, transform);
		super.addComponent(gradientMeshComponent = new GradientMeshComponent(gradientMesh));
		bounds = GameEngineUtils.toRectangleBounds(gradientMesh.getSize(), Alignment.CENTER, Alignment.CENTER);
		this.gradientDirection = dir;
		this.gradientRange = gradientRange;
		this.tint = tint;
		this.startColor = start;
		this.endColor = end;
	}

	@Override
	public Vector4f getTint() {
		return tint;
	}

	@Override
	public void setTint(Vector4f tint) {
		this.tint = tint;
	}

	public GradientMeshComponent getGradientMeshComponent() {
		return gradientMeshComponent;
	}

	public GradientMesh getGradientMesh() {
		return gradientMeshComponent == null ? null : gradientMeshComponent.getGradientMesh();
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
