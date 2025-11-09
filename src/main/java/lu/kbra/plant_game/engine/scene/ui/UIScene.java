package lu.kbra.plant_game.engine.scene.ui;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import lu.kbra.plant_game.UpdateFrameState;
import lu.kbra.plant_game.engine.entity.ui.impl.DelegatingTextUIObject;
import lu.kbra.plant_game.engine.entity.ui.impl.UIObject;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.standalone.gameengine.GameEngine;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.objs.entity.Entity;
import lu.kbra.standalone.gameengine.scene.Scene3D;
import lu.kbra.standalone.gameengine.scene.camera.Camera;
import lu.kbra.standalone.gameengine.utils.geo.GeoPlane;

public class UIScene extends Scene3D {

	private CacheManager uiCache;
	private DelegatingTextUIObject textEntity;

	public UIScene(String name, CacheManager parent) {
		super(name);
		this.uiCache = new CacheManager(name, parent);

		setCamera(Camera.orthographicCamera3D());
		getCamera().getPosition().set(0, 1, 0);
		getCamera().getRotation().set(new Quaternionf().lookAlong(new Vector3f(0, -1, 0), new Vector3f(0, 0, -1)));
		getCamera().getProjection().setSize(1);
		getCamera().getProjection().setNearPlane(0.001f);
		getCamera().getProjection().setFarPlane(1000f);
		getCamera().getProjection().setPerspective(false);
		getCamera().getProjection().update();
		getCamera().updateMatrix();
	}

	public void init(Dispatcher workers, Dispatcher renderDispatcher) {

	}

	public void input(final WindowInputHandler inputHandler, final float dTime, final UpdateFrameState frameState) {
		final Vector2f normalizedMousePosition = inputHandler.getNormalizedMousePosition();
		final Matrix4f inverseProj = new Matrix4f(super.getCamera().getProjection().getProjectionMatrix()).invert();
		final Vector4f mouseClip = new Vector4f(normalizedMousePosition.x, normalizedMousePosition.y, 0f, 1f);
		final Vector4f mouseWorld = inverseProj.transform(mouseClip);

		final Vector2f mouseWorld2D = new Vector2f(mouseWorld.x / mouseWorld.w, mouseWorld.y / mouseWorld.w);

		synchronized (super.getEntitiesLock()) {
			for (Entity e : this) {
				if (e instanceof UIObject uiObj) {
					final Shape bounds = uiObj.getBounds();
					final Vector3f pos3 = uiObj.getTransform() == null ? GameEngine.ZERO : uiObj.getTransform().getTranslation();
					final Vector3f scale3 = uiObj.getTransform() == null ? GameEngine.IDENTITY : uiObj.getTransform().getScale();
					final Vector2f pos = GeoPlane.XY.projectToPlane(pos3);
					mouseWorld2D.sub(pos, pos);

					final AffineTransform scaledTransform = new AffineTransform();
					scaledTransform.scale(scale3.x, scale3.y);
					final Shape scaledBounds = scaledTransform.createTransformedShape(bounds);

					if (scaledBounds.contains(new Point2D.Float(pos.x, pos.y))) {
						frameState.uiSceneCaughtMouseInput = true;

						uiObj.hover(inputHandler, dTime);

						if (inputHandler.isMouseButtonPressedOnce(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
							uiObj.click(inputHandler, dTime);
						}
					}
				}
			}
		}
	}

	public CacheManager getCache() {
		return uiCache;
	}

}
