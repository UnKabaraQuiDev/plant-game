package lu.kbra.plant_game.engine.scene.ui;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import lu.kbra.plant_game.engine.UpdateFrameState;
import lu.kbra.plant_game.engine.entity.impl.Transform3DOwner;
import lu.kbra.plant_game.engine.entity.impl.TransformOwner;
import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.entity.ui.impl.BoundsOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.Focusable;
import lu.kbra.plant_game.engine.entity.ui.impl.HoverState;
import lu.kbra.plant_game.engine.entity.ui.impl.NeedsBoundsInput;
import lu.kbra.plant_game.engine.entity.ui.impl.NeedsClick;
import lu.kbra.plant_game.engine.entity.ui.impl.NeedsHover;
import lu.kbra.plant_game.engine.entity.ui.impl.NeedsInput;
import lu.kbra.plant_game.engine.entity.ui.impl.NeedsUpdate;
import lu.kbra.plant_game.engine.render.DeferredCompositor;
import lu.kbra.plant_game.engine.scene.ui.layout.LayoutParent;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.standalone.gameengine.GameEngine;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.WorkerDispatcher;
import lu.kbra.standalone.gameengine.objs.entity.Entity;
import lu.kbra.standalone.gameengine.objs.entity.SceneEntity;
import lu.kbra.standalone.gameengine.scene.EntityContainer;
import lu.kbra.standalone.gameengine.scene.Scene3D;
import lu.kbra.standalone.gameengine.scene.camera.Camera;

public class UIScene extends Scene3D implements BoundsOwner {

	public static final Comparator<Entity> DEPTH_COMPARATOR = Comparator
			.comparing((final Entity e) -> e instanceof Transform3DOwner && ((Transform3DOwner) e).hasTransform()
					? ((Transform3DOwner) e).getTransform().getTranslation().y
					: 0f);

	protected final CacheManager uiCache;

	public UIScene(final String name, final CacheManager parent) {
		super(name);
		this.uiCache = new CacheManager(name, parent);

		this.setCamera(Camera.orthographicCamera3D());
		this.getCamera().getPosition().set(0, 1, 0);
		this.getCamera().getRotation().set(new Quaternionf().lookAlong(new Vector3f(0, -1, 0), new Vector3f(0, 0, -1)));
		this.getCamera().getProjection().setSize(1);
		this.getCamera().getProjection().setNearPlane(0.001f);
		this.getCamera().getProjection().setFarPlane(1000f);
		this.getCamera().getProjection().setPerspective(false);
		this.getCamera().getProjection().update();
		this.getCamera().updateMatrix();
	}

	public void init(final Dispatcher workers, final Dispatcher renderDispatcher) {

	}

	protected Set<UIObject> hovering = new HashSet<>();
	protected Focusable focused;

	public void input(final WindowInputHandler inputHandler, final UpdateFrameState frameState) {
		final Vector2f mouseWorld2D = this.getMouseCoords(inputHandler);

		final Set<UIObject> newHovered = new HashSet<>();

		if (this.focused != null && !this.focused.hasFocus()) {
			this.focused = null;
		}

		final boolean resized = inputHandler.wasResized();
		if (resized) {
			this.camera.getProjection().update(inputHandler.getWindowSize());
		}

		synchronized (super.getEntitiesLock()) {
			for (final SceneEntity e : this) {
				this.checkInput(e,
						inputHandler,
						frameState,
						new Point2D.Float(mouseWorld2D.x, mouseWorld2D.y),
						newHovered,
						GameEngine.IDENTITY_MATRIX4F);

				if (resized && e instanceof final LayoutParent lp) {
					lp.doLayout();
				}
			}
		}

		this.hovering.removeAll(newHovered);
		for (final UIObject uiObj : this.hovering) {
			((NeedsHover) uiObj).hover(inputHandler, HoverState.LEAVE);
		}

		this.hovering = newHovered;

		if (this.focused != null && this.focused.hasFocus()) {
			frameState.uiSceneCaughtKeyboardInput = true;
		}
	}

	public Vector2f getMouseCoords(final WindowInputHandler inputHandler) {
		final Vector2f normalizedMousePosition = inputHandler.getNormalizedMousePosition();
		final Matrix4f inverseProj = new Matrix4f(super.getCamera().getProjection().getProjectionMatrix()).invert();
		final Vector4f mouseClip = new Vector4f(normalizedMousePosition.x, normalizedMousePosition.y, 0f, 1f);
		final Vector4f mouseWorld = inverseProj.transform(mouseClip);
		return new Vector2f(mouseWorld.x / mouseWorld.w, -mouseWorld.y / mouseWorld.w);
	}

	private void checkInput(
			final SceneEntity e,
			final WindowInputHandler inputHandler,
			final UpdateFrameState frameState,
			final Point2D.Float mousePos,
			final Set<UIObject> newHovered,
			final Matrix4fc parentTransform) {

		// treat children first so they are earlier in the hovered stack
		if (e instanceof final EntityContainer<?> ec) {
			final Matrix4fc newMatrix = e instanceof final TransformOwner to && to.hasTransform()
					? parentTransform.mul(to.getTransform().getMatrix(), new Matrix4f())
					: parentTransform;

			ec.forEach(e2 -> this.checkInput(e2, inputHandler, frameState, mousePos, newHovered, newMatrix));
		}

		if (e instanceof final UIObject uiObj) {
			if (uiObj instanceof final NeedsInput uiObjectInput) {
				uiObjectInput.input(inputHandler);
			}

			if ((e instanceof NeedsHover || e instanceof NeedsClick || e instanceof NeedsBoundsInput)
					&& uiObj.getTransformedBounds(parentTransform).contains(mousePos)) {
				frameState.uiSceneCaughtMouseInput = true;

				if (uiObj instanceof final NeedsHover uiObjectHover) {
					uiObjectHover.hover(inputHandler, this.hovering.contains(uiObj) ? HoverState.STAY : HoverState.ENTER);
					newHovered.add(uiObj);
				}

				if (uiObj instanceof final NeedsBoundsInput uiObjectInput) {
					uiObjectInput.input(inputHandler);
					newHovered.add(uiObj);
				}

				if (uiObj instanceof final NeedsClick uiObjectClick && inputHandler.isMouseButtonPressedOnce(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
					uiObjectClick.click(inputHandler);
				}

				if (uiObj instanceof Focusable && inputHandler.isMouseButtonPressedOnce(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
					if (this.focused != null) {
						this.focused.removeFocus();
					}
					this.focused = (Focusable) uiObj;
					this.focused.giveFocus();
				}
			}
		}
	}

	public void update(
			final WindowInputHandler inputHandler,
			final DeferredCompositor compositor,
			final WorkerDispatcher workers,
			final Dispatcher render) {
		synchronized (super.getEntitiesLock()) {
			for (final SceneEntity e : this) {
				this.updateEntity(inputHandler, e);
			}
		}
	}

	private void updateEntity(final WindowInputHandler inputHandler, final SceneEntity e) {
		if (e instanceof final EntityContainer<?> ec) {
			ec.forEach(e2 -> this.updateEntity(inputHandler, e2));
		}

		final float dTime = inputHandler.dTime();

		if (e instanceof final NeedsUpdate needsUpdate) {
			needsUpdate.update(dTime, this);
		}
	}

	public CacheManager getCache() {
		return this.uiCache;
	}

	@Override
	public Rectangle2D.Float getBounds() {
		final float width = this.getCamera().getProjection().getAspectRatio();
		return new Rectangle2D.Float(-width / 2, -1, width, 2);
	}

}
