package lu.kbra.plant_game.engine.scene.ui;

import java.util.function.Consumer;

import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

import lu.kbra.plant_game.engine.entity.ui.btn.OptionsButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.btn.PlayButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.btn.QuitButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.factory.UIObjectFactory;
import lu.kbra.plant_game.engine.entity.ui.factory.UIObjectFactory.TextData;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextAlignment;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class MainMenuUIScene extends UIScene {

	protected PlayButtonUIObject playButton;
	protected OptionsButtonUIObject optionsButton;
	protected QuitButtonUIObject quitButton;

	public MainMenuUIScene(CacheManager parent) {
		super("main-menu", parent);
	}

	@Override
	public void init(Dispatcher workers, Dispatcher renderDispatcher) {
		final TextData uiTextData = new TextData(new Vector2f(1), TextAlignment.TEXT_CENTER);
		UIObjectFactory
				.create(PlayButtonUIObject.class,
						this,
						uiTextData,
						new Transform3D(new Vector3f(0, 0, -0.25f), new Quaternionf(), new Vector3f(0.2f)))
				.then(workers, (Consumer<PlayButtonUIObject>) (btn) -> playButton = btn)
				.push();
		UIObjectFactory
				.create(OptionsButtonUIObject.class,
						this,
						uiTextData,
						new Transform3D(new Vector3f(), new Quaternionf(), new Vector3f(0.2f)))
				.then(workers, (Consumer<OptionsButtonUIObject>) (btn) -> optionsButton = btn)
				.push();
		UIObjectFactory
				.create(QuitButtonUIObject.class,
						this,
						uiTextData,
						new Transform3D(new Vector3f(0, 0, 0.25f), new Quaternionf(), new Vector3f(0.2f)))
				.then(workers, (Consumer<QuitButtonUIObject>) (btn) -> quitButton = btn)
				.push();
	}

}
