package lu.kbra.plant_game.engine.entity.ui.texture;

import javax.swing.GroupLayout.Alignment;

import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector4f;
import org.joml.Vector4fc;

import lu.kbra.plant_game.engine.entity.ui.impl.QuadUIObject;
import lu.kbra.plant_game.engine.entity.ui.impl.TransparentEntity;
import lu.kbra.plant_game.engine.mesh.TintOwner;
import lu.kbra.plant_game.engine.render.GradientDirection;
import lu.kbra.plant_game.engine.render.GradientMesh;
import lu.kbra.plant_game.engine.render.GradientMeshComponent;
import lu.kbra.plant_game.engine.render.GradientOwner;
import lu.kbra.plant_game.engine.render.GradientQuadMesh;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
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

	public GradientQuadUIObject(final String str, final GradientQuadMesh gradientMesh) {
		this(str, gradientMesh, null, null, null, null, null, null);
	}

	public GradientQuadUIObject(final String str, final GradientQuadMesh gradientMesh, final GradientDirection dir) {
		this(str, gradientMesh, null, dir, null, null, null, null);
	}

	public GradientQuadUIObject(final String str, final GradientQuadMesh gradientMesh, final GradientDirection dir, final Vector4f tint) {
		this(str, gradientMesh, null, dir, null, tint, null, null);
	}

	public GradientQuadUIObject(
			final String str,
			final GradientQuadMesh gradientMesh,
			final GradientDirection dir,
			final Vector2f gradientRange,
			final Vector4f tint) {
		this(str, gradientMesh, null, dir, null, tint, null, null);
	}

	public GradientQuadUIObject(
			final String str,
			final GradientQuadMesh gradientMesh,
			final GradientDirection dir,
			final Vector4f start,
			final Vector4f end) {
		this(str, gradientMesh, null, dir, null, start, end);
	}

	public GradientQuadUIObject(
			final String str,
			final GradientQuadMesh gradientMesh,
			final GradientDirection dir,
			final Vector2f gradientRange,
			final Vector4f start,
			final Vector4f end) {
		this(str, gradientMesh, null, dir, gradientRange, null, start, end);
	}

	public GradientQuadUIObject(
			final String str,
			final GradientQuadMesh gradientMesh,
			final GradientDirection dir,
			final Vector2f gradientRange,
			final Vector4f tint,
			final Vector4f start,
			final Vector4f end) {
		this(str, gradientMesh, null, dir, gradientRange, tint, start, end);
	}

	public GradientQuadUIObject(final String str, final GradientQuadMesh gradientMesh, final Transform3D transform) {
		this(str, gradientMesh, transform, null, null, null, null, null);
	}

	public GradientQuadUIObject(
			final String str,
			final GradientQuadMesh gradientMesh,
			final Transform3D transform,
			final GradientDirection dir) {
		this(str, gradientMesh, transform, dir, null, null, null, null);
	}

	public GradientQuadUIObject(
			final String str,
			final GradientQuadMesh gradientMesh,
			final Transform3D transform,
			final GradientDirection dir,
			final Vector4f tint) {
		this(str, gradientMesh, transform, dir, null, tint, null, null);
	}

	public GradientQuadUIObject(
			final String str,
			final GradientQuadMesh gradientMesh,
			final Transform3D transform,
			final GradientDirection dir,
			final Vector2f gradientRange,
			final Vector4f tint) {
		this(str, gradientMesh, transform, dir, gradientRange, tint, null, null);
	}

	public GradientQuadUIObject(
			final String str,
			final GradientQuadMesh gradientMesh,
			final Transform3D transform,
			final GradientDirection dir,
			final Vector4f start,
			final Vector4f end) {
		this(str, gradientMesh, transform, dir, null, null, start, end);
	}

	public GradientQuadUIObject(
			final String str,
			final GradientQuadMesh gradientMesh,
			final Transform3D transform,
			final GradientDirection dir,
			final Vector2f gradientRange,
			final Vector4f start,
			final Vector4f end) {
		this(str, gradientMesh, transform, dir, gradientRange, null, start, end);
	}

	public GradientQuadUIObject(
			final String str,
			final GradientQuadMesh gradientMesh,
			final Transform3D transform,
			final GradientDirection dir,
			final Vector2f gradientRange,
			final Vector4f tint,
			final Vector4f start,
			final Vector4f end) {
		super(str, null, transform);
		super.addComponent(this.gradientMeshComponent = new GradientMeshComponent(gradientMesh));
		this.bounds = GameEngineUtils.toRectangleBounds(gradientMesh.getSize(), Alignment.CENTER, Alignment.CENTER);
		this.gradientDirection = dir;
		this.gradientRange = gradientRange;
		this.tint = tint;
		this.startColor = start;
		this.endColor = end;
	}

	@Override
	public Vector4fc getTint() {
		return this.tint;
	}

	@Override
	public void setTint(final Vector4fc tint) {
		this.tint.set(tint);
	}

	public GradientMeshComponent getGradientMeshComponent() {
		return this.gradientMeshComponent;
	}

	public GradientMesh getGradientMesh() {
		return this.gradientMeshComponent == null ? null : this.gradientMeshComponent.getGradientMesh();
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
