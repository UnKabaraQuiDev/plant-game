package lu.kbra.plant_game.engine.scene.ui;

import java.awt.geom.Point2D;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import lu.kbra.plant_game.engine.UpdateFrameState;
import lu.kbra.plant_game.engine.entity.impl.Transform3DOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.HoverState;
import lu.kbra.plant_game.engine.entity.ui.impl.NeedsClick;
import lu.kbra.plant_game.engine.entity.ui.impl.NeedsHover;
import lu.kbra.plant_game.engine.entity.ui.impl.NeedsInput;
import lu.kbra.plant_game.engine.entity.ui.impl.NeedsUpdate;
import lu.kbra.plant_game.engine.entity.ui.impl.UIObject;
import lu.kbra.plant_game.engine.entity.ui.text.Focusable;
import lu.kbra.plant_game.engine.render.DeferredCompositor;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.standalone.gameengine.GameEngine;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.WorkerDispatcher;
import lu.kbra.standalone.gameengine.objs.entity.Entity;
import lu.kbra.standalone.gameengine.objs.entity.components.SubEntitiesComponent;
import lu.kbra.standalone.gameengine.objs.entity.components.TransformComponent;
import lu.kbra.standalone.gameengine.scene.Scene3D;
import lu.kbra.standalone.gameengine.scene.camera.Camera;

public class UIScene extends Scene3D {

	public static final Comparator<Entity> DEPTH_COMPARATOR = Comparator
			.comparing((Entity e) -> e instanceof Transform3DOwner ? ((Transform3DOwner) e).getTransform().getTranslation().y : 0f);

	private final CacheManager uiCache;

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
	protected Focusable focused;

	public void input(final WindowInputHandler inputHandler, final float dTime, final UpdateFrameState frameState) {
		final Vector2f mouseWorld2D = getMouseCoords(inputHandler);

		final Set<UIObject> newHovered = new HashSet<>();

		if (focused != null && !focused.hasFocus()) {
			focused = null;
		}

		synchronized (super.getEntitiesLock()) {
			for (Entity e : this) {
				checkInput(e,
						inputHandler,
						dTime,
						frameState,
						new Point2D.Float(mouseWorld2D.x, mouseWorld2D.y),
						newHovered,
						GameEngine.IDENTITY_MATRIX4F);
			}
		}

		hovering.removeAll(newHovered);
		for (UIObject uiObj : hovering) {
			((NeedsHover) uiObj).hover(inputHandler, dTime, HoverState.LEAVE, this);
		}

		hovering = newHovered;
	}

	public Vector2f getMouseCoords(WindowInputHandler inputHandler) {
		final Vector2f normalizedMousePosition = inputHandler.getNormalizedMousePosition();
		final Matrix4f inverseProj = new Matrix4f(super.getCamera().getProjection().getProjectionMatrix()).invert();
		final Vector4f mouseClip = new Vector4f(normalizedMousePosition.x, normalizedMousePosition.y, 0f, 1f);
		final Vector4f mouseWorld = inverseProj.transform(mouseClip);
		return new Vector2f(mouseWorld.x / mouseWorld.w, -mouseWorld.y / mouseWorld.w);
	}

	private void checkInput(
			Entity e,
			WindowInputHandler inputHandler,
			float dTime,
			UpdateFrameState frameState,
			Point2D.Float mousePos,
			Set<UIObject> newHovered,
			Matrix4f parentTransform) {

		if (e.hasComponentMatching(SubEntitiesComponent.class)) {
			final Matrix4f newMatrix = e.hasComponentMatching(TransformComponent.class)
					? parentTransform.mul(e.getComponentMatching(TransformComponent.class).getTransform().getMatrix(), new Matrix4f())
					: parentTransform;

			e.getComponentsMatching(SubEntitiesComponent.class).forEach(se -> {
				synchronized (se.getEntitiesLock()) {
					for (Entity e2 : (List<Entity>) se.getEntities()) {
						checkInput(e2, inputHandler, dTime, frameState, mousePos, newHovered, newMatrix);
					}
				}
			});
		}

		if (e instanceof UIObject uiObj) {
			if (uiObj instanceof NeedsInput uiObjectInput) {
				uiObjectInput.input(inputHandler, dTime, this);
				newHovered.add(uiObj);
			}

			if ((e instanceof NeedsHover || e instanceof NeedsClick) && uiObj.getTransformedBounds(parentTransform).contains(mousePos)) {
				frameState.uiSceneCaughtMouseInput = true;

				if (uiObj instanceof NeedsHover uiObjectHover) {
					uiObjectHover.hover(inputHandler, dTime, hovering.contains(uiObj) ? HoverState.STAY : HoverState.ENTER, this);
					newHovered.add(uiObj);
				}

				if (uiObj instanceof NeedsClick uiObjectClick && inputHandler.isMouseButtonPressedOnce(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
					uiObjectClick.click(inputHandler, dTime, this);

					if (uiObj instanceof Focusable) {
						if (focused != null) {
							focused.removeFocus();
						}
						focused = (Focusable) uiObj;
						focused.giveFocus();
					}
				}
			}
		}
	}

	public void update(
			WindowInputHandler inputHandler,
			float dTime,
			DeferredCompositor compositor,
			WorkerDispatcher workers,
			Dispatcher render) {
		synchronized (super.getEntitiesLock()) {
			for (Entity e : this) {
				updateEntity(inputHandler, dTime, e);
			}
		}
	}

	private void updateEntity(WindowInputHandler inputHandler, float dTime, Entity e) {
		if (e.hasComponentMatching(SubEntitiesComponent.class)) {
			e.getComponentsMatching(SubEntitiesComponent.class).forEach(se -> {
				synchronized (se.getEntitiesLock()) {
					for (Entity e2 : (List<Entity>) se.getEntities()) {
						updateEntity(inputHandler, dTime, e2);
					}
				}
			});
		}

		if (e instanceof NeedsUpdate needsUpdate) {
			needsUpdate.update(dTime, this);
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
