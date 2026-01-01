package lu.kbra.plant_game.engine.entity.go.factory;

import static lu.kbra.plant_game.generated.GameObjectRegistry.BUFFER_SIZE;
import static lu.kbra.plant_game.generated.GameObjectRegistry.DATA_PATH;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;

import lu.kbra.plant_game.engine.entity.go.GOCreatingTaskFuture;
import lu.kbra.plant_game.engine.entity.go.GameObject;
import lu.kbra.plant_game.engine.entity.impl.AnimatedMeshOwner;
import lu.kbra.plant_game.engine.entity.impl.InstanceEmitterOwner;
import lu.kbra.plant_game.engine.entity.impl.MeshOwner;
import lu.kbra.plant_game.engine.entity.impl.NoMeshObject;
import lu.kbra.plant_game.engine.mesh.loader.AnimatedMeshLoader;
import lu.kbra.plant_game.engine.mesh.loader.AnimatedMeshLoader.AnimatedMeshes;
import lu.kbra.plant_game.engine.mesh.loader.StaticMeshLoader;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.cache.attrib.impl.AttribArray;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.geom.instance.InstanceEmitter;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.TaskFuture;
import lu.kbra.standalone.gameengine.utils.transform.Transform;

public class GameObjectFactory {

	public static final int DEFAULT_BUFFER_SIZE = 12;

	public static GameObjectFactory INSTANCE;

	private final CacheManager cache;
	private final Dispatcher loader, render;

	public GameObjectFactory(final CacheManager cache, final Dispatcher loader, final Dispatcher render) {
		this.cache = cache;
		this.loader = loader;
		this.render = render;
	}

	public <T extends GameObject & InstanceEmitterOwner> GOCreatingTaskFuture<T> createInstances_(
			final Class<T> clazz,
			final IntFunction<Transform> transforms,
			final OptionalInt bufferSize,
			final Optional<String> name,
			final Supplier<AttribArray>... attribs) {

		return StaticInstanceLoader
				.getFuture(this.cache, name.orElse(clazz.getSimpleName()), DATA_PATH.get(clazz), bufferSize.orElseGet(() -> {
					if (!BUFFER_SIZE.containsKey(clazz)) {
						throw new IllegalArgumentException("Class: " + clazz.getName() + " defines no default buffer size.");
					}
					return BUFFER_SIZE.get(clazz);
				}), transforms, attribs, this.loader, this.render)
				.then(this.loader, (Function<InstanceEmitter, List<Object>>) Arrays::asList)
				.then(new GOCreatingTaskFuture(this.loader, clazz));
	}

	public <T extends GameObject & MeshOwner> GOCreatingTaskFuture<T> createMesh_(final Class<T> clazz) {
		return StaticMeshLoader.getStaticFuture(this.cache, clazz.getName(), DATA_PATH.get(clazz), this.loader, this.render)
				.then(this.loader, (Function<Mesh, List<Object>>) Arrays::asList)
				.then(new GOCreatingTaskFuture(this.loader, clazz));
	}

	public <T extends GameObject & MeshOwner> GOCreatingTaskFuture<T> createManual_(final Class<T> clazz, final Mesh mesh) {
		return new TaskFuture<>(this.loader, (Supplier<List<Object>>) () -> Arrays.asList(mesh))
				.then(new GOCreatingTaskFuture(this.loader, clazz));
	}

	public <T extends GameObject & NoMeshObject> GOCreatingTaskFuture<T> createNoMesh_(final Class<T> clazz) {
		return new GOCreatingTaskFuture(this.loader, clazz);
	}

	public <T extends GameObject & AnimatedMeshOwner & MeshOwner> GOCreatingTaskFuture<T> createAnimatedMesh_(final Class<T> clazz) {
		return AnimatedMeshLoader.getAnimatedFuture(this.cache, clazz.getName(), DATA_PATH.get(clazz), this.loader, this.render)
				.then(this.loader,
						(Function<AnimatedMeshes, List<Object>>) meshes -> Arrays.asList(meshes.staticMesh(), meshes.animatedMesh()))
				.then(new GOCreatingTaskFuture(this.loader, clazz));
	}

//	public <T extends GameObject & MeshOwner> GOCreatingTaskFuture<T> taskManual_(final Class<T> clazz) {
//		return new TaskFuture<>(this.loader, (final Mesh m) -> Arrays.asList(m)).then(new GOCreatingTaskFuture(this.loader, clazz));
//	}

	public static <T extends GameObject> GOCreatingTaskFuture<T> create(final Class<T> clazz) {
		if (NoMeshObject.class.isAssignableFrom(clazz)) {
			return INSTANCE.createNoMesh_((Class) clazz);
		}
		// split this for AnimatedMesh & Mesh and only AnimatedMesh
		if (AnimatedMeshOwner.class.isAssignableFrom(clazz)) {
			return INSTANCE.createAnimatedMesh_((Class) clazz);
		}
		if (MeshOwner.class.isAssignableFrom(clazz)) {
			return INSTANCE.createMesh_((Class) clazz);
		}
		throw new UnsupportedOperationException(clazz.getName());
	}

	@SafeVarargs
	public static <T extends GameObject & InstanceEmitterOwner> GOCreatingTaskFuture<T> createInstances(
			final Class<T> clazz,
			final IntFunction<Transform> transforms,
			final OptionalInt bufferSize,
			final Optional<String> name,
			final Supplier<AttribArray>... attribs) {
		return INSTANCE.createInstances_(clazz, transforms, bufferSize, name, attribs);
	}

	public static <T extends GameObject & MeshOwner> GOCreatingTaskFuture<T> createManual(final Class<T> clazz, final Mesh mesh) {
		return INSTANCE.createManual_(clazz, mesh);
	}

//	public static <T extends GameObject & MeshOwner> GOCreatingTaskFuture<T> taskManual(final Class<T> clazz) {
//		return INSTANCE.taskManual_(clazz);
//	}

}
