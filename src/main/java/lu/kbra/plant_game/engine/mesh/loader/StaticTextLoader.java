package lu.kbra.plant_game.engine.mesh.loader;

import static lu.kbra.plant_game.engine.mesh.loader.MeshLoaderLocks.releaseLock;
import static lu.kbra.plant_game.engine.mesh.loader.MeshLoaderLocks.waitOrCreateLock;

import org.joml.Vector2f;

import lu.pcy113.pclib.impl.ThrowingFunction;
import lu.pcy113.pclib.impl.ThrowingSupplier;

import lu.kbra.plant_game.engine.entity.ui.factory.UIObjectFactory.TextData;
import lu.kbra.plant_game.engine.locale.LocalizationService;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.SkipThen;
import lu.kbra.standalone.gameengine.impl.future.TaskFuture;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextAlignment;

public class StaticTextLoader {

	public static final int MAX_CHAR_BUFFER_LENGTH = 256;
	public static final int MIN_CHAR_BUFFER_LENGTH = 12;

	public static TaskFuture<?, TextEmitter> getFuture(
			final CacheManager cache,
			final String meshName,
			final String key,
			final TextData td,
			final Dispatcher loader,
			final Dispatcher render) {

		final Vector2f charSize = td.getCharSize();
		final TextAlignment textAlignment = td.getTextAlignment();
		final int bufferSize = td.getBufferSize();

		return new TaskFuture<>(loader, (ThrowingSupplier<String, Throwable>) () -> {
			waitOrCreateLock(meshName);

			if (cache.hasTextEmitter(meshName)) {
				throw new SkipThen(cache.getTextEmitter(meshName));
			}

			return LocalizationService.get(key);
		})
				.then(render,
						(ThrowingFunction<String, TextEmitter, Throwable>) (
								final String text) -> create(cache, meshName, text, charSize, textAlignment, bufferSize));

	}

	static TextEmitter create(
			final CacheManager cache,
			final String meshName,
			final String text,
			final Vector2f size,
			final TextAlignment ta,
			final int bufferSize) {
		System.err.println("buffer size: " + bufferSize);
		final TextEmitter te = new TextEmitter(meshName, null,
				bufferSize == -1 ? Math.min(Math.max((int) (text.length() * 1.25), MIN_CHAR_BUFFER_LENGTH), MAX_CHAR_BUFFER_LENGTH)
						: bufferSize,
				text, size);
		te.setTextAlignment(ta);
		te.setup();
		cache.addTextEmitter(te);
		releaseLock(meshName);
		return te;
	}

}
