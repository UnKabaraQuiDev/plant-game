package lu.kbra.plant_game.engine.scene.ui;

import java.util.Optional;
import java.util.function.Consumer;

import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

import lu.kbra.plant_game.engine.entity.ui.CursorUIObject;
import lu.kbra.plant_game.engine.entity.ui.btn.OptionsButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.btn.PlayButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.btn.QuitButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.factory.UIObjectFactory;
import lu.kbra.plant_game.engine.entity.ui.factory.UIObjectFactory.TextData;
import lu.kbra.plant_game.engine.entity.ui.impl.UIObject;
import lu.kbra.plant_game.engine.render.DeferredCompositor;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.WorkerDispatcher;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextAlignment;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class MainMenuUIScene extends UIScene {

	protected PlayButtonUIObject playButton;
	protected OptionsButtonUIObject optionsButton;
	protected QuitButtonUIObject quitButton;
	protected CursorUIObject cursor;

	public MainMenuUIScene(CacheManager parent) {
		super("main-menu", parent);
	}

	@Override
	public void init(Dispatcher workers, Dispatcher renderDispatcher) {
		final TextData uiTextData = new TextData(new Vector2f(0.2f), TextAlignment.TEXT_CENTER);

		final float x = -0.8f;

		UIObjectFactory
				.create(PlayButtonUIObject.class, this, uiTextData, new Transform3D(new Vector3f(x, 0, -0.25f)))
				.then(workers, (Consumer<PlayButtonUIObject>) (btn) -> playButton = btn)
				.push();
		UIObjectFactory
				.create(OptionsButtonUIObject.class, this, uiTextData, new Transform3D(new Vector3f(x, 0, 0)))
				.then(workers, (Consumer<OptionsButtonUIObject>) (btn) -> optionsButton = btn)
				.push();
		UIObjectFactory
				.create(QuitButtonUIObject.class, this, uiTextData, new Transform3D(new Vector3f(x, 0, 0.25f)))
				.then(workers, (Consumer<QuitButtonUIObject>) (btn) -> quitButton = btn)
				.push();

		UIObjectFactory
				.create(CursorUIObject.class, this, new Transform3D(new Vector3f(x, 0.01f, 0.25f), new Quaternionf(), new Vector3f(0.15f)))
				.then(workers, (Consumer<CursorUIObject>) (btn) -> {
					btn.setTargetedObject(playButton);
					cursor = btn;
				})
				.push();
	}

	@Override
	public void update(
			WindowInputHandler inputHandler,
			float dTime,
			DeferredCompositor compositor,
			WorkerDispatcher workers,
			Dispatcher render) {
		super.update(inputHandler, dTime, compositor, workers, render);

		if (cursor != null) {
			final Optional<UIObject> hoverObj = hovering.stream().findFirst();
			if (hoverObj.isPresent()) {
				cursor.setTargetedObject(hoverObj.get());
			}
		}
	}

}
