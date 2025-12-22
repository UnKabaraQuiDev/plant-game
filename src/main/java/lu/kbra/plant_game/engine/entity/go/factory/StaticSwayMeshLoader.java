package lu.kbra.plant_game.engine.entity.go.factory;

import static lu.kbra.plant_game.engine.mesh.loader.MeshLoaderLocks.releaseLock;
import static lu.kbra.plant_game.engine.mesh.loader.MeshLoaderLocks.waitOrCreateLock;
import static lu.kbra.plant_game.engine.mesh.loader.StaticMeshLoader.getStaticMeshData;

import java.util.function.Function;

import lu.pcy113.pclib.impl.ThrowingSupplier;

import lu.kbra.plant_game.engine.mesh.loader.StaticMeshLoader.GenericMeshData;
import lu.kbra.plant_game.engine.render.SwayMesh;
import lu.kbra.plant_game.engine.util.AdvObjLoader;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.graph.texture.SingleTexture;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.SkipThen;
import lu.kbra.standalone.gameengine.impl.future.TaskFuture;

public class StaticSwayMeshLoader {

	public static TaskFuture<?, SwayMesh> getStaticFuture(
			final CacheManager cache,
			final String meshName,
			final String path,
			final Dispatcher loader,
			final Dispatcher render) {

		return new TaskFuture<>(loader, (ThrowingSupplier<GenericMeshData, Throwable>) () -> {
			waitOrCreateLock(meshName);

			if (cache.hasMesh(meshName)) {
				releaseLock(meshName);
				throw new SkipThen(cache.getMesh(meshName));
			}

			return getStaticMeshData(path);
		}).then(render, (Function<GenericMeshData, SwayMesh>) meshData -> createStatic(cache, meshName, meshData));
	}

	static SwayMesh createStatic(final CacheManager cache, final String meshName, final GenericMeshData meshData) {
		final SwayMesh staticMesh;
		if (meshData.textureMaterial()) {
			final SingleTexture txt0 = cache.hasTexture(meshData.texturePath()) ? (SingleTexture) cache.getTexture(meshData.texturePath())
					: SingleTexture.loadSingleTexture(cache, meshData.texturePath(), meshData.texturePath());

			// Maybe include as offset mesh idk
			staticMesh = AdvObjLoader
					.loadTexturedSwayMesh(meshName, null, meshData.filePath(), meshData.deformRatio(), meshData.speedRatio(), txt0);
		} else {
			staticMesh = AdvObjLoader.loadSwayMesh(meshName, null, meshData.filePath(), meshData.deformRatio(), meshData.speedRatio());
		}

		cache.addMesh(staticMesh);
		releaseLock(meshName);
		return staticMesh;
	}

}
