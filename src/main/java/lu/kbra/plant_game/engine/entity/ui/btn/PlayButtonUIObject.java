package lu.kbra.plant_game.engine.entity.ui.btn;

import lu.kbra.plant_game.engine.entity.ui.impl.AbsoluteTransformedBoundsOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.GrowOnHoverTextUIObject;
import lu.kbra.plant_game.engine.entity.ui.impl.NeedsClick;
import lu.kbra.plant_game.engine.entity.ui.impl.Scale2dDir;
import lu.kbra.plant_game.engine.scene.ui.menu.main.MainMenuUIScene;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;
import lu.kbra.standalone.gameengine.scene.Scene;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;
import lu.kbra.standalone.gameengine.utils.transform.Transform3DPivot;

@DataPath("localization:btn.play")
public class PlayButtonUIObject extends GrowOnHoverTextUIObject implements NeedsClick, IndexedMenuElement, AbsoluteTransformedBoundsOwner {

	public PlayButtonUIObject(final String str, final TextEmitter text) {
		super(str, text, Scale2dDir.HORIZONTAL);
	}

	public PlayButtonUIObject(final String str, final TextEmitter text, final Transform3D transform) {
		super(str, text, Scale2dDir.HORIZONTAL, transform);
		super.getTransformComponent().setTransform(new Transform3DPivot(transform));

		((Transform3DPivot) super.getTransform()).scalePivotSet(0, 0, 0);
	}

	@Override
	public void click(final WindowInputHandler input, final float dTime, final Scene scene) {
		((MainMenuUIScene) scene).startTransition(MainMenuUIScene.PLAY);
	}

	@Override
	public int getIndex() {
		return 0;
	}

}
