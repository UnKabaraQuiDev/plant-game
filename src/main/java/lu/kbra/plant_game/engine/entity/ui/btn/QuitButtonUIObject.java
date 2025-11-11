package lu.kbra.plant_game.engine.entity.ui.btn;

import org.joml.Vector4f;

import lu.kbra.plant_game.PGLogic;
import lu.kbra.plant_game.engine.entity.ui.impl.GrowOnHoverTextUIObject;
import lu.kbra.plant_game.engine.entity.ui.impl.NeedsClick;
import lu.kbra.plant_game.engine.util.DataPath;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;
import lu.kbra.standalone.gameengine.scene.Scene;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

@DataPath("localization:btn.quit")
public class QuitButtonUIObject extends GrowOnHoverTextUIObject implements NeedsClick {

	public static final Vector4f TARGET_RED = new Vector4f(1, 0.25f, 0.25f, 1);

	public QuitButtonUIObject(String str, TextEmitter text) {
		this(str, text, null);
	}

	public QuitButtonUIObject(String str, TextEmitter text, Transform3D transform) {
		super(str, text, transform);

		super.getTextEmitter().setForegroundColor(new Vector4f(TextEmitter.DEFAULT_FG_COLOR));
	}

	@Override
	public void click(WindowInputHandler input, float dTime, Scene scene) {
		PGLogic.INSTANCE.stop();
	}

	@Override
	public void update(float dTime, Scene scene) {
		final float factor = super.grow(dTime, isHovered());
		getTransform().updateMatrix();

		TextEmitter.DEFAULT_FG_COLOR.lerp(TARGET_RED, factor, getTextEmitter().getForegroundColor());
	}

}
