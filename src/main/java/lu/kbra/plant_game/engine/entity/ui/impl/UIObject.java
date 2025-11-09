package lu.kbra.plant_game.engine.entity.ui.impl;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

import lu.kbra.plant_game.engine.entity.impl.MeshOwner;
import lu.kbra.plant_game.engine.entity.impl.Transform3DOwner;
import lu.kbra.plant_game.engine.entity.ui.btn.HoverState;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.standalone.gameengine.GameEngine;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.objs.entity.Entity;
import lu.kbra.standalone.gameengine.objs.entity.components.MeshComponent;
import lu.kbra.standalone.gameengine.objs.entity.components.Transform3DComponent;
import lu.kbra.standalone.gameengine.utils.geo.GeoPlane;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public abstract class UIObject extends Entity implements Transform3DOwner, MeshOwner, TransparentEntity {

	public static final Shape SQUARE_1_UNIT = new Rectangle2D.Float(-0.5f, -0.5f, 1f, 1f);

	protected MeshComponent meshComponent;
	protected Transform3DComponent transformComponent;

	public UIObject(String str, Mesh mesh) {
		this(str, mesh, null);
	}

	public UIObject(String str, Mesh mesh, Transform3D transform) {
		super(str, mesh != null ? new MeshComponent(mesh) : null, transform != null ? new Transform3DComponent(transform) : null);
		this.meshComponent = super.getComponent(MeshComponent.class);
		this.transformComponent = super.getComponent(Transform3DComponent.class);
	}

	public abstract Shape getBounds();

	public void hover(final WindowInputHandler input, final float dTime, final HoverState hoverState) {
	}

	public void click(final WindowInputHandler input, final float dTime) {
	}

	public Shape getTransformedBounds() {
		final Shape bounds = getBounds();
		final Vector3f pos3 = getTransform() == null ? GameEngine.ZERO : getTransform().getTranslation();
		final Vector3f scale = getTransform() == null ? GameEngine.IDENTITY_VECTOR3F : getTransform().getScale();
		final Quaternionf rotation = getTransform() == null ? GameEngine.IDENTITY_QUATERNIONF : getTransform().getRotation();
		final Vector2f pos = GeoPlane.XZ.projectToPlane(pos3);

		float angleY = rotation.getEulerAnglesXYZ(new Vector3f()).y;

		AffineTransform transform = new AffineTransform();
		transform.translate(pos.x, pos.y);
		transform.rotate(-angleY);
		transform.scale(scale.x, scale.z);

		return transform.createTransformedShape(bounds);
	}

	public MeshComponent getMeshComponent() {
		return meshComponent;
	}

	public Transform3DComponent getTransformComponent() {
		return transformComponent;
	}

	@Override
	public Mesh getMesh() {
		return meshComponent == null ? null : meshComponent.getMesh();
	}

	@Override
	public Transform3D getTransform() {
		return transformComponent == null ? null : transformComponent.getTransform();
	}

}
