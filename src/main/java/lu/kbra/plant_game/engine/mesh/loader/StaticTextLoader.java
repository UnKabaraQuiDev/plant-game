package lu.kbra.plant_game.engine.mesh.loader;

import static lu.kbra.plant_game.engine.mesh.loader.MeshLoaderLocks.releaseLock;
import static lu.kbra.plant_game.engine.mesh.loader.MeshLoaderLocks.waitOrCreateLock;

import org.joml.Vector2f;

import lu.pcy113.pclib.impl.ThrowingFunction;
import lu.pcy113.pclib.impl.ThrowingSupplier;

import lu.kbra.plant_game.engine.locale.LocalizationService;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.SkipThen;
import lu.kbra.standalone.gameengine.impl.future.TaskFuture;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextAlignment;

public class StaticTextLoader {

	public static final int MAX_CHAR_BUFFER_LENGTH = 256;

	public static TaskFuture<?, TextEmitter> getFuture(
			CacheManager cache,
			String meshName,
			String key,
			Vector2f size,
			TextAlignment ta,
			Dispatcher loader,
			Dispatcher render) {

		return new TaskFuture<>(loader, (ThrowingSupplier<String, Throwable>) () -> {
			waitOrCreateLock(meshName);

			if (cache.hasTextEmitter(meshName)) {
				throw new SkipThen(cache.getTextEmitter(meshName));
			}

			return LocalizationService.get(key);
		}).then(render, (ThrowingFunction<String, TextEmitter, Throwable>) (String text) -> {
			return create(cache, meshName, text, size, ta);
		});

	}

	static TextEmitter create(CacheManager cache, String meshName, String text, Vector2f size, TextAlignment ta) {
		final TextEmitter te = new TextEmitter(meshName, null, Math.min((int) (text.length() * 1.25), MAX_CHAR_BUFFER_LENGTH), text, size);
		te.setTextAlignment(ta);
		te.setup();
		cache.addTextEmitter(te);
		releaseLock(meshName);
		return te;
	}

}
