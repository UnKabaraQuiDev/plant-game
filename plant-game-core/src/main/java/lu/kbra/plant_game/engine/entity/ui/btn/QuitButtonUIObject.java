package lu.kbra.plant_game.engine.entity.ui.btn;

import org.joml.Vector4f;
import org.joml.Vector4fc;

import lu.kbra.plant_game.PGLogic;
import lu.kbra.plant_game.engine.entity.ui.impl.IndexOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.NeedsClick;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.standalone.gameengine.objs.entity.SceneParentAware;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;
import lu.kbra.standalone.gameengine.utils.transform.Transform3DShear;

@DataPath("localization:btn.quit")
public class QuitButtonUIObject extends MainMenuItemTextUIObject implements NeedsClick, IndexOwner, SceneParentAware {

	public static final Vector4fc TARGET_RED = new Vector4f(1, 0.25f, 0.25f, 1);

	public QuitButtonUIObject(final String str, final TextEmitter text) {
		super(str, text);
		super.getTextEmitter().setForegroundColor(new Vector4f(TextEmitter.DEFAULT_FG_COLOR));
		super.setTransform(new Transform3DShear());
	}

	@Override
	public void click(final WindowInputHandler input) {
		PGLogic.INSTANCE.stop();
	}

	@Override
	public void animate(final float t, final boolean isHovered) {
		super.animate(t, isHovered);
		TextEmitter.DEFAULT_FG_COLOR.lerp(TARGET_RED, t, this.getTextEmitter().getForegroundColor());
	}

	@Override
	public int getIndex() {
		return 2;
	}

	@Override
	public String toString() {
		return "QuitButtonUIObject [transform=" + this.transform + ", active=" + this.active + ", name=" + this.name + "]";
	}

}
