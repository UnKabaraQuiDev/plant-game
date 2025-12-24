package lu.kbra.plant_game.engine.entity.ui.texture;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import lu.kbra.plant_game.PGLogic;
import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.entity.ui.impl.AbsoluteTransformOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.NeedsUpdate;
import lu.kbra.plant_game.engine.mesh.TexturedQuadMesh;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.scene.Scene;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

@DataPath("image:classpath:/icons/cursor-128.png")
public class CursorUIObject extends TextureUIObject implements NeedsUpdate {

	protected final Vector2f to = new Vector2f();
	protected boolean circlingMouse;
	protected float snapAngle = 0.25f;
	protected float snapPhase = 0;
	protected float offsetX = 0;

	public CursorUIObject(final String str, final TexturedQuadMesh mesh) {
		super(str, mesh);
	}

	public CursorUIObject(final String str, final TexturedQuadMesh mesh, final Transform3D transform) {
		super(str, mesh, transform);
	}

	public void setTargetedObject(final UIObject uiObject) {
		if (uiObject == null || uiObject == this || !uiObject.hasTransform()) {
			return;
		}

		this.circlingMouse = false;

		final float cursorWidth = (float) (this.getBounds().getWidth() * this.getTransform().getScale().x);

		final Matrix4f c = uiObject instanceof AbsoluteTransformOwner ? ((AbsoluteTransformOwner) uiObject).getAbsoluteTransform()
				: uiObject.getTransform().getMatrix();
		final Vector3f pos = c.getTranslation(new Vector3f());
		final float scaleX = new Vector3f(c.m00(), c.m01(), c.m02()).length();

		this.to.x = this.offsetX + pos.x + (float) uiObject.getBounds().getBounds2D().getX() * scaleX - cursorWidth;
		this.to.y = pos.z;
	}

	@Override
	public void update(final float dTime, final Scene scene) {
		final Vector3f cursorPos = this.getTransform().getTranslation();
		final float cursorWidth = (float) (this.getBounds().getWidth() * this.getTransform().getScale().x);

		if (this.circlingMouse) {
			final Vector2f pos = new Vector2f(cursorPos.x, cursorPos.z);
			float radius = pos.distance(this.to);

			final float angularSpeed = (float) Math.PI;
			final float amplitude = 0.05f;
			final float freq = 2;

			radius = cursorWidth + (float) (Math.sin(PGLogic.TOTAL_TIME() * freq) / 2 + 0.5f) * amplitude;

			// radius += (targetRadius - radius) * shrinkSpeed * dTime;

			float angle = (float) Math.atan2(pos.y - this.to.y, pos.x - this.to.x);
			angle += angularSpeed * dTime;

			cursorPos.x = this.to.x + (float) Math.cos(angle) * radius;
			cursorPos.z = this.to.y + (float) Math.sin(angle) * radius;

			this.getTransform().rotationSet(0, (float) (-angle + Math.PI / 3), 0);
		} else {
			final float speed = 4;
			final float freq = 3;
			cursorPos.x += (this.to.x - cursorPos.x) * dTime * speed;
			cursorPos.x += Math.cos(PGLogic.TOTAL_TIME() * freq) * freq * (cursorWidth / 2) * dTime;
			cursorPos.z += (this.to.y - cursorPos.z) * dTime * speed;

			this
					.getTransform()
					.getRotation()
					.rotationY((float) ((cursorPos.z / this.snapAngle + this.snapPhase % this.snapAngle) * (2.0 * Math.PI / 3.0)));

		}

		this.getTransform().updateMatrix();
	}

	public void setCirclingMouse(final Vector2f mouseTarget) {
		this.to.x = mouseTarget.x;
		this.to.y = mouseTarget.y;
		this.circlingMouse = true;
	}

	public boolean isCirclingMouse() {
		return this.circlingMouse;
	}

	public void forceCirclingMouse() {
		this.circlingMouse = true;
	}

	/**
	 * distance between the aligned things
	 */
	public void setSnapAngle(final float snapAngle) {
		this.snapAngle = snapAngle;
	}

	public void setSnapPhase(final float snapPhase) {
		this.snapPhase = snapPhase;
	}

	public void setOffsetX(final float offsetX) {
		this.offsetX = offsetX;
	}

	public float getOffsetX() {
		return this.offsetX;
	}

}
