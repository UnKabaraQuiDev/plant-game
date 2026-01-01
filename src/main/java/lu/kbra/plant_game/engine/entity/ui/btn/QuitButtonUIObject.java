package lu.kbra.plant_game.engine.entity.ui.btn;

import org.joml.Vector4f;
import org.joml.Vector4fc;

import lu.kbra.plant_game.PGLogic;
import lu.kbra.plant_game.engine.entity.ui.impl.IndexOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.NeedsClick;
import lu.kbra.plant_game.engine.entity.ui.impl.Scale2dDir;
import lu.kbra.plant_game.engine.entity.ui.text.GrowOnHoverTextUIObject;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.standalone.gameengine.objs.entity.SceneParentAware;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;
import lu.kbra.standalone.gameengine.scene.Scene;

@DataPath("localization:btn.quit")
public class QuitButtonUIObject extends GrowOnHoverTextUIObject implements NeedsClick, IndexOwner, SceneParentAware {

	public static final Vector4fc TARGET_RED = new Vector4f(1, 0.25f, 0.25f, 1);

	public QuitButtonUIObject(final String str, final TextEmitter text) {
		super(str, text);
		this.setDir(Scale2dDir.HORIZONTAL);
		super.getTextEmitter().setForegroundColor(new Vector4f(TextEmitter.DEFAULT_FG_COLOR));
	}

	@Override
	public void click(final WindowInputHandler input) {
		PGLogic.INSTANCE.stop();
	}

	@Override
	public void update(final float dTime, final Scene scene) {
		final float factor = super.grow(dTime, this.isHovered());
		this.getTransform().updateMatrix();

		TextEmitter.DEFAULT_FG_COLOR.lerp(TARGET_RED, factor, this.getTextEmitter().getForegroundColor());
	}

	@Override
	public int getIndex() {
		return 2;
	}

	@Override
	public String toString() {
		return "QuitButtonUIObject [transform=" + this.transform + ", parent=" + this.parent + ", active=" + this.active + ", name="
				+ this.name + "]";
	}

}
