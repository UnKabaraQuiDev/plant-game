package lu.kbra.plant_game.engine.entity.ui.texture;

import org.joml.Vector2f;
import org.joml.Vector3f;

import lu.kbra.plant_game.PGLogic;
import lu.kbra.plant_game.engine.entity.ui.impl.NeedsUpdate;
import lu.kbra.plant_game.engine.entity.ui.impl.TextureUIObject;
import lu.kbra.plant_game.engine.entity.ui.impl.UIObject;
import lu.kbra.plant_game.engine.mesh.TexturedQuadLoadedMesh;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.scene.Scene;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

@DataPath("image:classpath:/icons/cursor-128.png")
public class CursorUIObject extends TextureUIObject implements NeedsUpdate {

	protected final Vector2f to = new Vector2f();
	protected boolean circlingMouse;
	protected float snapAngle = 0.25f;
	protected float offsetX = 0;

	public CursorUIObject(String str, TexturedQuadLoadedMesh mesh) {
		super(str, mesh);
	}

	public CursorUIObject(String str, TexturedQuadLoadedMesh mesh, Transform3D transform) {
		super(str, mesh, transform);
	}

	public void setTargetedObject(UIObject uiObject) {
		if (uiObject == null || uiObject == this || uiObject.getTransform() == null) {
			return;
		}

		this.circlingMouse = false;

		final float cursorWidth = (float) (getBounds().getWidth() * getTransform().getScale().x);

		this.to.x = offsetX + uiObject.getTransform().getTranslation().x
				+ (float) uiObject.getBounds().getBounds2D().getX() * uiObject.getTransform().getScale().x - cursorWidth;
		this.to.y = (float) uiObject.getTransform().getTranslation().z;
	}

	@Override
	public void update(final float dTime, final Scene scene) {
		final Vector3f cursorPos = getTransform().getTranslation();
		final float cursorWidth = (float) (getBounds().getWidth() * getTransform().getScale().x);

		if (circlingMouse) {
			final Vector2f pos = new Vector2f(cursorPos.x, cursorPos.z);
			float radius = pos.distance(to);

			final float angularSpeed = (float) Math.PI; // radians per second
			final float amplitude = 0.05f; // final radius
			final float shrinkSpeed = 0.5f;
			final float freq = 2;

			radius = cursorWidth + (float) (Math.sin(PGLogic.TOTAL_TIME() * freq) / 2 + 0.5f) * amplitude;

			// radius += (targetRadius - radius) * shrinkSpeed * dTime;

			float angle = (float) Math.atan2(pos.y - to.y, pos.x - to.x);
			angle += angularSpeed * dTime;

			cursorPos.x = to.x + (float) Math.cos(angle) * radius;
			cursorPos.z = to.y + (float) Math.sin(angle) * radius;

			getTransform().rotationSet(0, (float) (-angle + Math.PI / 3), 0);
		} else {
			final float speed = 4;
			final float freq = 3;
			cursorPos.x += (to.x - cursorPos.x) * dTime * speed;
			cursorPos.x += Math.cos(PGLogic.TOTAL_TIME() * freq) * freq * (cursorWidth / 2) * dTime;
			cursorPos.z += (to.y - cursorPos.z) * dTime * speed;

			getTransform().getRotation().rotationY((float) ((cursorPos.z / snapAngle) * (2 * Math.PI / 3)));
		}

		getTransform().updateMatrix();
	}

	public void setCirclingMouse(Vector2f mouseTarget) {
		this.to.x = mouseTarget.x;
		this.to.y = mouseTarget.y;
		this.circlingMouse = true;
	}

	public boolean isCirclingMouse() {
		return circlingMouse;
	}

	public void forceCirclingMouse() {
		circlingMouse = true;
	}

	public void setSnapAngle(float snapAngle) {
		this.snapAngle = snapAngle;
	}

	public void setOffsetX(float offsetX) {
		this.offsetX = offsetX;
	}

	public float getOffsetX() {
		return offsetX;
	}

}
