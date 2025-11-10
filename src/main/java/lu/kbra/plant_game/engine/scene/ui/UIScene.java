package lu.kbra.plant_game.engine.scene.ui;

import java.awt.geom.Point2D;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import lu.kbra.plant_game.UpdateFrameState;
import lu.kbra.plant_game.engine.entity.impl.Transform3DOwner;
import lu.kbra.plant_game.engine.entity.ui.HoverState;
import lu.kbra.plant_game.engine.entity.ui.NeedsUpdate;
import lu.kbra.plant_game.engine.entity.ui.btn.NeedsClick;
import lu.kbra.plant_game.engine.entity.ui.impl.DelegatingTextUIObject;
import lu.kbra.plant_game.engine.entity.ui.impl.NeedsHover;
import lu.kbra.plant_game.engine.entity.ui.impl.UIObject;
import lu.kbra.plant_game.engine.render.DeferredCompositor;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.WorkerDispatcher;
import lu.kbra.standalone.gameengine.objs.entity.Entity;
import lu.kbra.standalone.gameengine.scene.Scene3D;
import lu.kbra.standalone.gameengine.scene.camera.Camera;

public class UIScene extends Scene3D {

	public static final Comparator<Entity> DEPTH_COMPARATOR = Comparator
			.comparing((Entity e) -> e instanceof Transform3DOwner ? ((Transform3DOwner) e).getTransform().getTranslation().y : 0f);

	private final CacheManager uiCache;
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

	protected Set<UIObject> hovering = new HashSet<>();

	public void input(final WindowInputHandler inputHandler, final float dTime, final UpdateFrameState frameState) {
		final Vector2f normalizedMousePosition = inputHandler.getNormalizedMousePosition();
		final Matrix4f inverseProj = new Matrix4f(super.getCamera().getProjection().getProjectionMatrix()).invert();
		final Vector4f mouseClip = new Vector4f(normalizedMousePosition.x, normalizedMousePosition.y, 0f, 1f);
		final Vector4f mouseWorld = inverseProj.transform(mouseClip);

		final Vector2f mouseWorld2D = new Vector2f(mouseWorld.x / mouseWorld.w, -mouseWorld.y / mouseWorld.w);

		final Set<UIObject> newHovered = new HashSet<>();

		synchronized (super.getEntitiesLock()) {
			for (Entity e : this) {
				if (e instanceof UIObject uiObj && (e instanceof NeedsHover || e instanceof NeedsClick)
						&& uiObj.getTransformedBounds().contains(new Point2D.Float(mouseWorld2D.x, mouseWorld2D.y))) {
					frameState.uiSceneCaughtMouseInput = true;

					if (uiObj instanceof NeedsHover uiObjectHover) {
						uiObjectHover.hover(inputHandler, dTime, hovering.contains(uiObj) ? HoverState.STAY : HoverState.ENTER);
						newHovered.add(uiObj);
					}

					if (uiObj instanceof NeedsClick uiObjectClick && inputHandler.isMouseButtonPressedOnce(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
						uiObjectClick.click(inputHandler, dTime);
					}
				}
			}
		}

		hovering.removeAll(newHovered);
		for (UIObject uiObj : hovering) {
			((NeedsHover) uiObj).hover(inputHandler, dTime, HoverState.LEAVE);
		}

		hovering = newHovered;
	}

	public void update(
			WindowInputHandler inputHandler,
			float dTime,
			DeferredCompositor compositor,
			WorkerDispatcher workers,
			Dispatcher render) {
		synchronized (super.getEntitiesLock()) {
			for (Entity e : this) {
				if (e instanceof NeedsUpdate needsUpdate) {
					needsUpdate.update(dTime);
				}
			}
		}
	}

	public CacheManager getCache() {
		return uiCache;
	}

	@Override
	public Iterator<Entity> iterator() {
		return this.entities.values().stream().sorted(DEPTH_COMPARATOR).iterator();
	}

}
