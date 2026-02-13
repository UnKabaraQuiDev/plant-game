package lu.kbra.plant_game.engine.entity.ui.btn;

import lu.kbra.plant_game.base.scene.menu.main.MainMenuUIScene;
import lu.kbra.plant_game.engine.entity.ui.impl.AnimatedOnHover;
import lu.kbra.plant_game.engine.entity.ui.impl.IndexOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.NeedsClick;
import lu.kbra.plant_game.engine.entity.ui.impl.UISceneParentAware;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;

@DataPath("localization:btn.start")
public class StartButtonUIObject extends MainMenuItemTextUIObject implements NeedsClick, IndexOwner, UISceneParentAware, AnimatedOnHover {

	public StartButtonUIObject(final String str, final TextEmitter text) {
		super(str, text);
	}

	@Override
	public void click(final WindowInputHandler input) {
		this.getUISceneParent()
				.filter(MainMenuUIScene.class::isInstance)
				.map(MainMenuUIScene.class::cast)
				.ifPresent(c -> c.startTransition(MainMenuUIScene.PLAY));
	}

	@Override
	public int getIndex() {
		return 1;
	}

	@Override
	public String toString() {
		return "PlayButtonUIObject@" + System.identityHashCode(this) + " [growthProgress=" + this.growthProgress + ", transform="
				+ this.transform + ", active=" + this.active + ", name=" + this.name + "]";
	}

}
