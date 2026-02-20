package lu.kbra.plant_game.engine.loader;

import static lu.kbra.plant_game.engine.loader.MeshLoaderLocks.releaseLock;
import static lu.kbra.plant_game.engine.loader.MeshLoaderLocks.waitOrCreateLock;

import java.util.function.Function;

import lu.kbra.pclib.datastructure.pair.Pair;
import lu.kbra.pclib.datastructure.pair.Pairs;
import lu.kbra.pclib.impl.ThrowingFunction;
import lu.kbra.pclib.impl.ThrowingSupplier;
import lu.kbra.plant_game.engine.mesh.TexturedQuadLoadedMesh;
import lu.kbra.plant_game.engine.mesh.TexturedQuadMesh;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.graph.texture.SingleTexture;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.SkipThen;
import lu.kbra.standalone.gameengine.impl.future.TaskFuture;
import lu.kbra.standalone.gameengine.impl.future.YieldExecutionThrowable;
import lu.kbra.standalone.gameengine.utils.GameEngineUtils;
import lu.kbra.standalone.gameengine.utils.file.FileUtils;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextureFilter;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextureWrap;
import lu.kbra.standalone.gameengine.utils.mem.img.MemImage;

public class StaticTexturedMeshLoader {

	public static TaskFuture<?, TexturedQuadMesh> getStaticFuture(
			final CacheManager cache,
			final String meshName,
			final SingleTexture txt,
			final Dispatcher loader,
			final Dispatcher render) {
		return new TaskFuture<>(loader, (ThrowingSupplier<SingleTexture, Throwable>) () -> {
			if (!waitOrCreateLock(meshName)) {
				throw new YieldExecutionThrowable(() -> cache.hasMesh(meshName));
			}

			if (cache.hasMesh(meshName)) {
				releaseLock(meshName);
				throw new SkipThen(cache.getMesh(meshName));
			}

			return txt;
		}).then(render, (ThrowingFunction<SingleTexture, TexturedQuadMesh, Throwable>) txt2 -> createStaticQuad(cache, meshName, txt2));
	}

	public static TaskFuture<?, TexturedQuadMesh> getStaticFuture(
			final CacheManager cache,
			final String meshName,
			final String path,
			final TextureFilter textureFilter,
			final TextureWrap textureWrap,
			final Dispatcher loader,
			final Dispatcher render) {

		if (!path.startsWith("image:")) {
			throw new IllegalArgumentException("Not an image: " + path);
		}

		return new TaskFuture<>(loader, (ThrowingSupplier<Pair<MemImage, SingleTexture>, Throwable>) () -> {
			if (!waitOrCreateLock(meshName)) {
				throw new YieldExecutionThrowable(() -> cache.hasMesh(meshName));
			}

			if (cache.hasMesh(meshName)) {
				releaseLock(meshName);
				throw new SkipThen(3, cache.getMesh(meshName));
			}

			if (cache.hasTexture(meshName)) {
				throw new SkipThen(2, cache.getTexture(meshName));
			}

			final String imgPath = path.replaceFirst("image:", "");
			final MemImage image = FileUtils.STBILoad(imgPath);
			final SingleTexture txt = new SingleTexture(meshName, image);
			txt.setFilters(textureFilter);
			txt.setWraps(textureWrap);

			return Pairs.readOnly(image, txt);
		}).then(render, (Function<Pair<MemImage, SingleTexture>, Pair<MemImage, SingleTexture>>) pair -> {
			pair.getValue().setup();
			cache.addTexture(pair.getValue());
			return pair;
		}).then(loader, (Function<Pair<MemImage, SingleTexture>, SingleTexture>) pair -> {
			pair.getKey().cleanup();
			return pair.getValue();
		}).then(render, (Function<SingleTexture, TexturedQuadMesh>) txt -> createStaticQuad(cache, meshName, txt));
	}

	static TexturedQuadMesh createStaticQuad(final CacheManager cache, final String meshName, final SingleTexture txt) {
		final TexturedQuadMesh staticMesh = new TexturedQuadLoadedMesh(meshName,
				txt,
				GameEngineUtils.normalizeSize(txt.getWidth(), txt.getHeight()));
		cache.addMesh(staticMesh);
		releaseLock(meshName);
		return staticMesh;
	}

}
