package lu.kbra.plant_game.engine.loader;

import static lu.kbra.plant_game.engine.loader.MeshLoaderLocks.releaseLock;
import static lu.kbra.plant_game.engine.loader.MeshLoaderLocks.waitOrCreateLock;
import static lu.kbra.plant_game.engine.loader.StaticMeshLoader.getStaticMeshData;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;
import java.util.function.Supplier;

import lu.kbra.pclib.PCUtils;
import lu.kbra.pclib.impl.ThrowingFunction;
import lu.kbra.pclib.impl.ThrowingSupplier;
import lu.kbra.plant_game.engine.loader.StaticMeshLoader.GenericMeshData;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.cache.attrib.impl.JavaAttribArray;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.geom.instance.InstanceEmitter;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.SkipThen;
import lu.kbra.standalone.gameengine.impl.future.TaskFuture;
import lu.kbra.standalone.gameengine.impl.future.YieldExecutionThrowable;
import lu.kbra.standalone.gameengine.utils.transform.Transform;

public class StaticInstanceLoader {

	public static int MAX_INSTANCE_BUFFER_LENGTH = 1024;
	public static int MIN_INSTANCE_BUFFER_LENGTH = 0;

	@SuppressWarnings("unchecked")
	public static TaskFuture<?, InstanceEmitter> getFuture(
			final CacheManager cache,
			final String name,
			final String path,
			final int bufferSize,
			final IntFunction<Transform> transforms,
			final Supplier<JavaAttribArray>[] attribs,
			final Dispatcher loader,
			final Dispatcher render) {

		final String meshName = name + "-mesh";

		TaskFuture tf = new TaskFuture<>(loader, (ThrowingSupplier<GenericMeshData, Throwable>) () -> {
			if (!cache.hasInstanceEmitter(name) && !waitOrCreateLock(name)) {
				throw new YieldExecutionThrowable(() -> cache.hasInstanceEmitter(name));
			}
			if (!cache.hasMesh(meshName) && !waitOrCreateLock(meshName)) {
				throw new YieldExecutionThrowable(() -> cache.hasMesh(meshName));
			}

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
		}).then(render, (ThrowingFunction<GenericMeshData, Mesh, Throwable>) obj -> StaticMeshLoader.createStatic(cache, meshName, obj));

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
				(ThrowingFunction<Mesh, InstanceEmitter, Throwable>) (
						final Mesh mesh) -> createInstance(cache, name, mesh, bufferSize, transforms, createdAttribs));
	}

	static InstanceEmitter createInstance(
			final CacheManager cache,
			final String name,
			final Mesh mesh,
			final int bufferSize,
			final IntFunction<Transform> transform,
			final List<JavaAttribArray> attribs) {
		final InstanceEmitter te = new InstanceEmitter(name,
				mesh,
				PCUtils.clamp(MIN_INSTANCE_BUFFER_LENGTH, MAX_INSTANCE_BUFFER_LENGTH, bufferSize),
				transform,
				attribs.toArray(JavaAttribArray[]::new)) {
			@Override
			public void cleanup() {
				new Exception("Cleaned up: " + this.getId()).fillInStackTrace().printStackTrace();
				super.cleanup();
			};
		};
		if (cache.hasInstanceEmitter(name)) {
			throw new IllegalStateException("InstanceEmitter: " + name + " already exists.");
		}
		cache.addInstanceEmitter(te);
		releaseLock(name);
		return te;
	}

}
