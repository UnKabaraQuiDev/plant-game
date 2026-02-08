package lu.kbra.plant_game.engine.entity.ui.btn;

import lu.kbra.plant_game.base.scene.menu.main.MainMenuUIScene;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;
import lu.kbra.standalone.gameengine.utils.transform.Transform3DShear;

@DataPath("localization:btn.options")
public class OptionsButtonUIObject extends MainMenuItemTextUIObject {

	public OptionsButtonUIObject(final String str, final TextEmitter text) {
		super(str, text);
		super.setTransform(new Transform3DShear());
	}

	@Override
	public void click(final WindowInputHandler input) {
		this.getUISceneParent()
				.filter(MainMenuUIScene.class::isInstance)
				.map(MainMenuUIScene.class::cast)
				.ifPresent(c -> c.startTransition(MainMenuUIScene.OPTIONS));
	}

	@Override
	public int getIndex() {
		return 1;
	}

	@Override
	public String toString() {
		return "OptionsButtonUIObject [transform=" + this.transform + ", active=" + this.active + ", name=" + this.name + "]";
	}

}
