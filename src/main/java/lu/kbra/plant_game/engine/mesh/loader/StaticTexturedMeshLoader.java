package lu.kbra.plant_game.engine.mesh.loader;

import static lu.kbra.plant_game.engine.mesh.loader.MeshLoaderLocks.releaseLock;
import static lu.kbra.plant_game.engine.mesh.loader.MeshLoaderLocks.waitOrCreateLock;

import java.util.function.Function;

import lu.pcy113.pclib.datastructure.pair.Pair;
import lu.pcy113.pclib.datastructure.pair.Pairs;
import lu.pcy113.pclib.impl.ThrowingSupplier;

import lu.kbra.plant_game.engine.mesh.TexturedMesh;
import lu.kbra.plant_game.engine.mesh.TexturedQuadLoadedMesh;
import lu.kbra.plant_game.engine.mesh.loader.StaticMeshLoader.GenericMeshData;
import lu.kbra.plant_game.engine.util.AdvObjLoader;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.graph.texture.SingleTexture;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.SkipThen;
import lu.kbra.standalone.gameengine.impl.future.TaskFuture;
import lu.kbra.standalone.gameengine.utils.GameEngineUtils;
import lu.kbra.standalone.gameengine.utils.file.FileUtils;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextureFilter;
import lu.kbra.standalone.gameengine.utils.mem.img.MemImage;

public class StaticTexturedMeshLoader {

	public static TaskFuture<?, TexturedMesh> getStaticFuture(
			CacheManager cache,
			String meshName,
			String path,
			Dispatcher loader,
			Dispatcher render) {

		return new TaskFuture<>(loader, (ThrowingSupplier<Pair<MemImage, SingleTexture>, Throwable>) () -> {
			waitOrCreateLock(meshName);

			if (cache.hasMesh(meshName)) {
				throw new SkipThen(3, (TexturedMesh) cache.getMesh(meshName));
			}

			if (cache.hasTexture(meshName)) {
				throw new SkipThen(2, cache.getTexture(meshName));
			}

			final MemImage image = FileUtils.STBILoad(path);
			final SingleTexture txt = new SingleTexture(path, image);
			txt.setFilters(TextureFilter.NEAREST);

			return Pairs.readOnly(image, txt);
		}).then(render, (Function<Pair<MemImage, SingleTexture>, Pair<MemImage, SingleTexture>>) (pair) -> {
			pair.getValue().setup();
			cache.addTexture(pair.getValue());
			return pair;
		}).then(loader, (Function<Pair<MemImage, SingleTexture>, SingleTexture>) (pair) -> {
			pair.getKey().cleanup();
			return pair.getValue();
		}).then(render, (Function<SingleTexture, TexturedMesh>) (txt) -> {
			return createStaticQuad(cache, meshName, txt);
		});
	}

	static TexturedMesh createStaticQuad(CacheManager cache, String meshName, SingleTexture txt) {
		final TexturedMesh staticMesh = new TexturedQuadLoadedMesh(
				meshName,
				txt,
				GameEngineUtils.normalizeSize(txt.getWidth(), txt.getHeight()));
		cache.addMesh(staticMesh);
		releaseLock(meshName);
		return staticMesh;
	}

	static TexturedMesh createStatic(CacheManager cache, String meshName, GenericMeshData meshData) {
		final SingleTexture txt0 = cache.hasTexture(meshData.texturePath()) ? (SingleTexture) cache.getTexture(meshData.texturePath())
				: SingleTexture.loadSingleTexture(cache, meshData.texturePath(), meshData.texturePath());

		final TexturedMesh texturedMesh = AdvObjLoader.loadTexturedMesh(meshName, null, meshData.filePath(), meshData.origin(), txt0);

		cache.addMesh(texturedMesh);
		releaseLock(meshName);
		return texturedMesh;
	}

}
