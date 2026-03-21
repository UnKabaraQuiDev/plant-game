package lu.kbra.plant_game.engine.loader;

import static lu.kbra.plant_game.engine.loader.MeshLoaderLocks.releaseLock;
import static lu.kbra.plant_game.engine.loader.MeshLoaderLocks.waitOrCreateLock;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.function.Supplier;

import org.joml.Vector2fc;

import lu.kbra.pclib.PCUtils;
import lu.kbra.pclib.impl.ThrowingFunction;
import lu.kbra.pclib.impl.ThrowingSupplier;
import lu.kbra.plant_game.engine.data.locale.LocalizationService;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.cache.attrib.impl.JavaAttribArray;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.SkipThen;
import lu.kbra.standalone.gameengine.impl.future.TaskFuture;
import lu.kbra.standalone.gameengine.impl.future.YieldExecutionThrowable;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextAlignment;

public class StaticTextLoader {

	public static final int MAX_CHAR_BUFFER_LENGTH = 256;
	public static final int MIN_CHAR_BUFFER_LENGTH = 12;

	public static TaskFuture<?, TextEmitter> getFuture(
			final CacheManager cache,
			final String meshName,
			final String key,
			final Vector2fc charSize,
			final TextAlignment textAlignment,
			final OptionalInt bufferSize,
			final Supplier<JavaAttribArray>[] attribs,
			final Dispatcher loader,
			final Dispatcher render) {

		if (key == null) {
			throw new NullPointerException("Null key.");
		}

		final String absKey;
		if (key.startsWith("localization:")) {
			absKey = key.replaceFirst("localization:", "");
		} else {
			absKey = key;
		}

		TaskFuture tf = new TaskFuture<>(loader, (ThrowingSupplier<String, Throwable>) () -> {
			if (!cache.hasTextEmitter(meshName) && !waitOrCreateLock(meshName)) {
				throw new YieldExecutionThrowable(() -> cache.hasTextEmitter(meshName));
			}

			if (cache.hasTextEmitter(meshName)) {
				releaseLock(meshName);
				throw new SkipThen(cache.getTextEmitter(meshName));
			}

			return LocalizationService.get(absKey);
		});

		final List<JavaAttribArray> createdAttribs = new ArrayList<>();

		if (attribs != null && attribs.length != 0) {
			for (final Supplier<JavaAttribArray> aa : attribs) {
				tf = tf.then(render, (ThrowingFunction<Mesh, Mesh, Throwable>) mesh -> {
					createdAttribs.add(aa.get());
					return mesh;
				});
			}
		}

		return tf.then(render,
				(ThrowingFunction<String, TextEmitter, Throwable>) (
						final String text) -> create(cache, meshName, text, charSize, textAlignment, bufferSize));

	}

	static TextEmitter create(
			final CacheManager cache,
			final String meshName,
			final String text,
			final Vector2fc size,
			final TextAlignment ta,
			final OptionalInt bufferSize) {
		final TextEmitter te = new TextEmitter(meshName,
				null,
				bufferSize.isEmpty() ? PCUtils.clamp(MIN_CHAR_BUFFER_LENGTH, MAX_CHAR_BUFFER_LENGTH, (int) (text.length() * 1.25))
						: bufferSize.getAsInt(),
				text,
				size);
		te.setTextAlignment(ta);
		te.setup();
		cache.addTextEmitter(te);
		releaseLock(meshName);
		return te;
	}

}
