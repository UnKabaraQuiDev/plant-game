package lu.kbra.plant_game.engine.entity.go.factory;

import static lu.kbra.plant_game.engine.mesh.loader.MeshLoaderLocks.releaseLock;
import static lu.kbra.plant_game.engine.mesh.loader.MeshLoaderLocks.waitOrCreateLock;
import static lu.kbra.plant_game.engine.mesh.loader.StaticMeshLoader.getStaticMeshData;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;
import java.util.function.Supplier;

import lu.pcy113.pclib.PCUtils;
import lu.pcy113.pclib.impl.ThrowingFunction;
import lu.pcy113.pclib.impl.ThrowingSupplier;

import lu.kbra.plant_game.engine.mesh.loader.StaticMeshLoader;
import lu.kbra.plant_game.engine.mesh.loader.StaticMeshLoader.GenericMeshData;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.cache.attrib.impl.AttribArray;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.geom.instance.InstanceEmitter;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.SkipThen;
import lu.kbra.standalone.gameengine.impl.future.TaskFuture;
import lu.kbra.standalone.gameengine.utils.transform.Transform;

public class StaticInstanceLoader {

	public static final int MAX_INSTANCE_BUFFER_LENGTH = 1024;
	public static final int MIN_INSTANCE_BUFFER_LENGTH = 0;

	@SuppressWarnings("unchecked")
	public static TaskFuture<?, InstanceEmitter> getFuture(
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
			System.err.println("Creating: " + name);
			waitOrCreateLock(name);
			System.err.println("Creating: " + meshName);
			waitOrCreateLock(meshName);

			if (cache.hasInstanceEmitter(name)) {
				System.err.println("Got: " + name + " + " + meshName);
				releaseLock(meshName);
				releaseLock(name);
				throw new SkipThen(2, cache.getInstanceEmitter(name));
			}

			if (cache.hasMesh(meshName)) {
				System.err.println("Got: " + meshName);
				releaseLock(meshName);
				throw new SkipThen(cache.getMesh(meshName));
			}

			return getStaticMeshData(path);
		}).then(render, (ThrowingFunction<GenericMeshData, Mesh, Throwable>) obj -> StaticMeshLoader.createStatic(cache, meshName, obj));

		final List<AttribArray> createdAttribs = new ArrayList<>();

		if (attribs != null && attribs.length != 0) {
			for (final Supplier<AttribArray> aa : attribs) {
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
			final List<AttribArray> attribs) {
		final InstanceEmitter te = new InstanceEmitter(name,
				mesh,
				PCUtils.clamp(MIN_INSTANCE_BUFFER_LENGTH, MAX_INSTANCE_BUFFER_LENGTH, bufferSize),
				transform,
				attribs.toArray(AttribArray[]::new));
		cache.addInstanceEmitter(te);
		releaseLock(name);
		return te;
	}

}
