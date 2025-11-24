package lu.kbra.plant_game.engine.entity.go.factory;

import static lu.kbra.plant_game.engine.mesh.loader.MeshLoaderLocks.releaseLock;
import static lu.kbra.plant_game.engine.mesh.loader.MeshLoaderLocks.waitOrCreateLock;
import static lu.kbra.plant_game.engine.mesh.loader.StaticMeshLoader.getStaticMeshData;

import java.util.function.Function;

import lu.pcy113.pclib.PCUtils;
import lu.pcy113.pclib.impl.ThrowingFunction;
import lu.pcy113.pclib.impl.ThrowingSupplier;

import lu.kbra.plant_game.engine.entity.go.factory.GameObjectFactory.InstanceData;
import lu.kbra.plant_game.engine.entity.go.impl.SwayInstanceEmitter;
import lu.kbra.plant_game.engine.mesh.loader.StaticMeshLoader.GenericMeshData;
import lu.kbra.plant_game.engine.render.SwayMesh;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.SkipThen;
import lu.kbra.standalone.gameengine.impl.future.TaskFuture;
import lu.kbra.standalone.gameengine.utils.transform.Transform;

public class StaticSwayInstanceLoader {

	public static final int MAX_INSTANCE_BUFFER_LENGTH = 1024;
	public static final int MIN_INSTANCE_BUFFER_LENGTH = 0;

	public static TaskFuture<?, SwayInstanceEmitter> getFuture(
			final CacheManager cache,
			final String name,
			final String path,
			final InstanceData id,
			final Dispatcher loader,
			final Dispatcher render) {

		final int bufferSize = id.getBufferSize();
		final Function<Integer, Transform> transforms = id.getTransforms();
		final String meshName = name + "-mesh";

		return new TaskFuture<>(loader, (ThrowingSupplier<GenericMeshData, Throwable>) () -> {
			waitOrCreateLock(name);
			waitOrCreateLock(meshName);

			if (cache.hasInstanceEmitter(name)) {
				releaseLock(meshName);
				releaseLock(name);
				throw new SkipThen(2, cache.getInstanceEmitter(name));
			}

			if (cache.hasMesh(meshName)) {
				releaseLock(meshName);
				throw new SkipThen(cache.getMesh(meshName));
			}

			return getStaticMeshData(path);
		})
				.then(render,
						(ThrowingFunction<GenericMeshData, SwayMesh, Throwable>) obj -> StaticSwayMeshLoader
								.createStatic(cache, meshName, obj))
				.then(render,
						(ThrowingFunction<SwayMesh, SwayInstanceEmitter, Throwable>) (
								final SwayMesh mesh) -> create(cache, name, mesh, bufferSize, transforms));

	}

	static SwayInstanceEmitter create(
			final CacheManager cache,
			final String name,
			final SwayMesh mesh,
			final int bufferSize,
			final Function<Integer, Transform> transform) {
		System.err.println("got instance mesh: " + mesh);
		final SwayInstanceEmitter te = new SwayInstanceEmitter(
				name,
				mesh,
				PCUtils.clamp(MIN_INSTANCE_BUFFER_LENGTH, MAX_INSTANCE_BUFFER_LENGTH, bufferSize),
				transform);
		cache.addInstanceEmitter(te);
		releaseLock(name);
		return te;
	}

}
