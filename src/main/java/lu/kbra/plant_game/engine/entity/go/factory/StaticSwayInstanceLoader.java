package lu.kbra.plant_game.engine.entity.go.factory;

import static lu.kbra.plant_game.engine.entity.go.factory.StaticInstanceLoader.createInstance;
import static lu.kbra.plant_game.engine.mesh.loader.MeshLoaderLocks.releaseLock;
import static lu.kbra.plant_game.engine.mesh.loader.MeshLoaderLocks.waitOrCreateLock;
import static lu.kbra.plant_game.engine.mesh.loader.StaticMeshLoader.getStaticMeshData;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;
import java.util.function.Supplier;

import lu.pcy113.pclib.impl.ThrowingFunction;
import lu.pcy113.pclib.impl.ThrowingSupplier;

import lu.kbra.plant_game.engine.entity.go.impl.SwayInstanceEmitter;
import lu.kbra.plant_game.engine.mesh.loader.StaticMeshLoader.GenericMeshData;
import lu.kbra.plant_game.engine.render.SwayMesh;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.cache.attrib.impl.AttribArray;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.geom.instance.InstanceEmitter;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.SkipThen;
import lu.kbra.standalone.gameengine.impl.future.TaskFuture;
import lu.kbra.standalone.gameengine.utils.transform.Transform;

/**
 * Basically a duplicate of {@link StaticInstanceLoader}
 */
@Deprecated
public class StaticSwayInstanceLoader {

	@Deprecated
	public static final int MAX_INSTANCE_BUFFER_LENGTH = 1024;
	@Deprecated
	public static final int MIN_INSTANCE_BUFFER_LENGTH = 0;

	@Deprecated
	public static TaskFuture<?, SwayInstanceEmitter> getFuture(
			final CacheManager cache,
			final String name,
			final String path,
			final int bufferSize,
			final IntFunction<Transform> transforms,
			final Supplier<AttribArray>[] attribs,
			final Dispatcher loader,
			final Dispatcher render) {

		final String meshName = name + "-mesh";

		TaskFuture tf = new TaskFuture<>(loader, (ThrowingSupplier<GenericMeshData, Throwable>) () -> {
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
		}).then(render,
				(ThrowingFunction<GenericMeshData, SwayMesh, Throwable>) obj -> StaticSwayMeshLoader.createStatic(cache, meshName, obj));

		final List<AttribArray> createdAttribs = new ArrayList<>();

		if (attribs != null && attribs.length != 0) {
			for (final Supplier<AttribArray> aa : attribs) {
				tf = tf.then(render, (ThrowingFunction<Mesh, Mesh, Throwable>) mesh -> {
					createdAttribs.add(aa.get());
					return mesh;
				});
			}
		}

		return tf

				.then(render,
						(ThrowingFunction<SwayMesh, InstanceEmitter, Throwable>) (
								final SwayMesh mesh) -> createInstance(cache, name, mesh, bufferSize, transforms, createdAttribs));

	}

//	static SwayInstanceEmitter createInstance(
//			final CacheManager cache,
//			final String name,
//			final SwayMesh mesh,
//			final int bufferSize,
//			final IntFunction<Transform> transform,
//			final List<AttribArray> attribs) {
//		final SwayInstanceEmitter te = new SwayInstanceEmitter(name,
//				mesh,
//				PCUtils.clamp(MIN_INSTANCE_BUFFER_LENGTH, MAX_INSTANCE_BUFFER_LENGTH, bufferSize),
//				transform,
//				attribs.toArray(AttribArray[]::new));
//		cache.addInstanceEmitter(te);
//		releaseLock(name);
//		return te;
//	}

}
