package lu.kbra.plant_game.engine.entity.ui.factory;

import static lu.kbra.plant_game.engine.mesh.loader.MeshLoaderLocks.releaseLock;
import static lu.kbra.plant_game.engine.mesh.loader.MeshLoaderLocks.waitOrCreateLock;
import static lu.kbra.plant_game.engine.mesh.loader.StaticMeshLoader.getStaticMeshData;

import java.util.function.Function;

import lu.pcy113.pclib.PCUtils;
import lu.pcy113.pclib.impl.ThrowingSupplier;

import lu.kbra.plant_game.engine.mesh.loader.StaticMeshLoader.GenericMeshData;
import lu.kbra.plant_game.engine.render.GradientMesh;
import lu.kbra.plant_game.engine.render.GradientQuadMesh;
import lu.kbra.plant_game.engine.util.AdvObjLoader;
import lu.kbra.plant_game.engine.util.LoadedGradientQuadMesh;
import lu.kbra.standalone.gameengine.GameEngine;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.SkipThen;
import lu.kbra.standalone.gameengine.impl.future.TaskFuture;

public class StaticGradientMeshLoader {

	public static TaskFuture<?, GradientMesh> getStaticFuture(
			CacheManager cache,
			String meshName,
			String path,
			Dispatcher loader,
			Dispatcher render) {

		if (path == null || path.isBlank()) {

			return new TaskFuture<>(render, (ThrowingSupplier<GradientMesh, Throwable>) () -> {
				waitOrCreateLock(meshName);

				if (cache.hasMesh(meshName)) {
					return (GradientQuadMesh) cache.getMesh(meshName);
				}

				return createStaticQuad(cache, meshName);
			});

		} else if (path != null && path.endsWith("json")) {

			return new TaskFuture<>(loader, (ThrowingSupplier<GenericMeshData, Throwable>) () -> {
				waitOrCreateLock(meshName);

				if (cache.hasMesh(meshName)) {
					throw new SkipThen((GradientMesh) cache.getMesh(meshName));
				}

				return getStaticMeshData(path);
			}).then(render, (Function<GenericMeshData, GradientMesh>) (meshData) -> {
				return createStatic(cache, meshName, meshData);
			});

		} else {

			PCUtils.throwUnsupported(path);
			return null;

		}
	}

	static GradientQuadMesh createStaticQuad(CacheManager cache, String meshName) {
		final GradientQuadMesh staticMesh = new LoadedGradientQuadMesh(meshName, null, GameEngine.IDENTITY_VECTOR2F);
		cache.addMesh(staticMesh);
		releaseLock(meshName);
		return staticMesh;
	}

	static GradientMesh createStatic(CacheManager cache, String meshName, GenericMeshData meshData) {
		final GradientMesh texturedMesh = AdvObjLoader.loadGradientMesh(meshName, null, meshData.filePath());

		cache.addMesh(texturedMesh);
		releaseLock(meshName);
		return texturedMesh;
	}

}
