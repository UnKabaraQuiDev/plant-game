package lu.kbra.plant_game.engine.entity.ui;

import org.joml.Vector3f;

import lu.kbra.plant_game.PGLogic;
import lu.kbra.plant_game.engine.entity.ui.impl.TextureUIObject;
import lu.kbra.plant_game.engine.entity.ui.impl.UIObject;
import lu.kbra.plant_game.engine.mesh.TexturedQuadMesh;
import lu.kbra.plant_game.engine.util.DataPath;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

@DataPath("classpath:/icons/cursor-128.png")
public class CursorUIObject extends TextureUIObject implements NeedsUpdate {

	private float toX;
	private float toZ;

	public CursorUIObject(String str, TexturedQuadMesh mesh) {
		super(str, mesh);
	}

	public CursorUIObject(String str, TexturedQuadMesh mesh, Transform3D transform) {
		super(str, mesh, transform);
	}

	public void setTargetedObject(UIObject uiObject) {
		if (uiObject == null || uiObject == this) {
			return;
		}

		final float cursorWidth = (float) (getBounds().getWidth() * getTransform().getScale().x);

		toX = (float) (uiObject.getTransform().getTranslation().x + uiObject.getBounds().getBounds2D().getX() - cursorWidth * 1.5);
		toZ = (float) uiObject.getTransform().getTranslation().z;
	}

	@Override
	public void update(float dTime) {
		final Vector3f cursorPos = getTransform().getTranslation();
		final float cursorWidth = (float) (getBounds().getWidth() * getTransform().getScale().x);

		final float speed = 4;
		final float freq = 3;
		cursorPos.x += (toX - cursorPos.x) * dTime * speed;
		cursorPos.x += Math.cos(PGLogic.TOTAL_TIME() * freq) * freq * (cursorWidth / 2) * dTime;
		cursorPos.z += (toZ - cursorPos.z) * dTime * speed;

		getTransform().getRotation().rotationY((float) ((cursorPos.z / 0.25f) * (2 * Math.PI / 3)));

		getTransform().updateMatrix();
	}

}
