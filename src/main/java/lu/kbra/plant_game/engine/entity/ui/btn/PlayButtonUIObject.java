package lu.kbra.plant_game.engine.entity.ui.btn;

import lu.kbra.plant_game.engine.entity.ui.impl.IndexOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.NeedsClick;
import lu.kbra.plant_game.engine.entity.ui.impl.Scale2dDir;
import lu.kbra.plant_game.engine.entity.ui.text.GrowOnHoverTextUIObject;
import lu.kbra.plant_game.engine.scene.ui.menu.main.MainMenuUIScene;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.standalone.gameengine.objs.entity.SceneParentAware;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;

@DataPath("localization:btn.play")
public class PlayButtonUIObject extends GrowOnHoverTextUIObject implements NeedsClick, IndexOwner, SceneParentAware {

	public PlayButtonUIObject(final String str, final TextEmitter text) {
		super(str, text);
		this.setDir(Scale2dDir.HORIZONTAL);
	}

	@Override
	public void click(final WindowInputHandler input) {
		((MainMenuUIScene) this.getSceneParent()).startTransition(MainMenuUIScene.PLAY);
	}

	@Override
	public int getIndex() {
		return 0;
	}

	@Override
	public String toString() {
		return "PlayButtonUIObject [transform=" + this.transform + ", parent=" + this.parent + ", active=" + this.active + ", name="
				+ this.name + "]";
	}

}
