package lu.kbra.plant_game.engine.entity.go.factory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import lu.pcy113.pclib.PCUtils;
import lu.pcy113.pclib.impl.ThrowingFunction;

import lu.kbra.plant_game.engine.entity.go.AnimatedGameObject;
import lu.kbra.plant_game.engine.entity.go.GameObject;
import lu.kbra.plant_game.engine.entity.go.impl.InstanceGameObject;
import lu.kbra.plant_game.engine.entity.go.impl.InstanceSwayGameObject;
import lu.kbra.plant_game.engine.entity.go.impl.SwayGameObject;
import lu.kbra.plant_game.engine.entity.go.impl.SwayInstanceEmitter;
import lu.kbra.plant_game.engine.locale.NoMeshObject;
import lu.kbra.plant_game.engine.mesh.AnimatedMesh;
import lu.kbra.plant_game.engine.mesh.loader.AnimatedMeshLoader;
import lu.kbra.plant_game.engine.mesh.loader.AnimatedMeshLoader.AnimatedMeshes;
import lu.kbra.plant_game.engine.mesh.loader.StaticMeshLoader;
import lu.kbra.plant_game.engine.render.SwayMesh;
import lu.kbra.plant_game.engine.util.annotation.BufferSize;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.plant_game.generated.GameObjectRegistry;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.geom.instance.InstanceEmitter;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.TaskFuture;
import lu.kbra.standalone.gameengine.objs.entity.components.SubEntitiesComponent;
import lu.kbra.standalone.gameengine.scene.Scene3D;
import lu.kbra.standalone.gameengine.utils.transform.Transform;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class GameObjectFactory {

	public static class InstanceData {

		protected Function<Integer, Transform> transforms;
		protected int bufferSize;
		protected String name;

		public InstanceData(final Function<Integer, Transform> transforms, final int bufferSize) {
			this.transforms = transforms;
			this.bufferSize = bufferSize;
		}

		public InstanceData(final Function<Integer, Transform> transforms, final int bufferSize, final String name) {
			this.transforms = transforms;
			this.bufferSize = bufferSize;
			this.name = name;
		}

		public Function<Integer, Transform> getTransforms() {
			return this.transforms;
		}

		public void setTransforms(final Function<Integer, Transform> transforms) {
			this.transforms = transforms;
		}

		public int getBufferSize() {
			return this.bufferSize;
		}

		public void setBufferSize(final int bufferSize) {
			this.bufferSize = bufferSize;
		}

		public String getName() {
			return this.name;
		}

		public void setName(final String name) {
			this.name = name;
		}

	}

	public static final int DEFAULT_BUFFER_SIZE = 12;
	public static final InstanceData DEFAULT_INSTANCE_DATA = new InstanceData(i -> new Transform3D(), DEFAULT_BUFFER_SIZE, null);

	public static GameObjectFactory INSTANCE;

	private final Map<Class<? extends GameObject>, Boolean> animatedMesh = new HashMap<>();
	private final Map<Class<? extends GameObject>, String> dataPath = new HashMap<>();
	private final Map<Class<? extends GameObject>, Integer> bufferSize = new HashMap<>();

	private final CacheManager cache;
	private final Dispatcher loader, render;

	public GameObjectFactory(final CacheManager cache, final Dispatcher loader, final Dispatcher render) {
		this.cache = cache;
		this.loader = loader;
		this.render = render;
	}

	public <T extends GameObject> TaskFuture<?, T> create_(final Class<T> clazz, final Object... args) {
		this.animatedMesh.computeIfAbsent(clazz, k -> AnimatedGameObject.class.isAssignableFrom(k));
		this.dataPath.computeIfAbsent(clazz, k -> {
			if (!k.isAnnotationPresent(DataPath.class)) {
				throw new IllegalArgumentException(clazz.getName() + " doesn't have @DataPath.");
			}
			return k.getAnnotation(DataPath.class).value();
		});

		if (this.animatedMesh.get(clazz)) {

			return AnimatedMeshLoader
					.getAnimatedFuture(this.cache, clazz.getName(), this.dataPath.get(clazz), this.loader, this.render)
					.then(this.loader, (ThrowingFunction<AnimatedMeshes, T, Throwable>) meshes -> {
						final T instance = PCUtils
								.findCompatibleConstructor(clazz,
										PCUtils
												.combineArrays(new Class[] { String.class, Mesh.class, AnimatedMesh.class },
														Arrays.stream(args).map(Object::getClass).toArray(Class[]::new)))
								.newInstance(PCUtils
										.combineArrays(
												new Object[] {
														clazz.getSimpleName() + "#" + System.nanoTime(),
														meshes.staticMesh(),
														meshes.animatedMesh() },
												args));
						return instance;
					});
		}
		if (SwayGameObject.class.isAssignableFrom(clazz)) {

			return StaticSwayMeshLoader
					.getStaticFuture(this.cache, clazz.getName(), this.dataPath.get(clazz), this.loader, this.render)
					.then(this.loader, (ThrowingFunction<SwayMesh, T, Throwable>) mesh -> {
						final T instance = GameObjectRegistry
								.create(clazz,
										PCUtils
												.combineArrays(new Object[] { clazz.getSimpleName() + "#" + System.nanoTime(), mesh },
														args));
						return instance;
					});

		}
		if (InstanceSwayGameObject.class.isAssignableFrom(clazz)) {

			InstanceData td;
			final Object[] nargs;
			if (args.length > 0 && args[0] instanceof final InstanceData vvec) {
				td = vvec;
				nargs = PCUtils.removeArray(args, 0);
			} else {
				td = DEFAULT_INSTANCE_DATA;
				nargs = args;

				if (this.bufferSize
						.computeIfAbsent(clazz,
								c -> clazz.isAnnotationPresent(BufferSize.class) ? clazz.getAnnotation(BufferSize.class).value()
										: -1) != -1) {
					td = new InstanceData(td.transforms, this.bufferSize.get(clazz), td.name);
				}
			}

			return StaticSwayInstanceLoader
					.getFuture(this.cache,
							td.name == null ? clazz.getName() : td.name,
							this.dataPath.get(clazz),
							td,
							this.loader,
							this.render)
					.then(this.loader, (ThrowingFunction<SwayInstanceEmitter, T, Throwable>) ie -> {
						final T instance = GameObjectRegistry
								.create(clazz,
										PCUtils.combineArrays(new Object[] { clazz.getSimpleName() + "#" + System.nanoTime(), ie }, nargs));
						return instance;
					});

		}
		if (InstanceGameObject.class.isAssignableFrom(clazz)) {

			InstanceData td;
			final Object[] nargs;
			if (args.length > 0 && args[0] instanceof final InstanceData vvec) {
				td = vvec;
				nargs = PCUtils.removeArray(args, 0);
			} else {
				td = DEFAULT_INSTANCE_DATA;
				nargs = args;

				if (this.bufferSize
						.computeIfAbsent(clazz,
								c -> clazz.isAnnotationPresent(BufferSize.class) ? clazz.getAnnotation(BufferSize.class).value()
										: -1) != -1) {
					td = new InstanceData(td.transforms, this.bufferSize.get(clazz), td.name);
				}
			}

			return StaticInstanceLoader
					.getFuture(this.cache,
							td.name == null ? clazz.getName() : td.name,
							this.dataPath.get(clazz),
							td,
							this.loader,
							this.render)
					.then(this.loader, (ThrowingFunction<InstanceEmitter, T, Throwable>) ie -> {
						final T instance = GameObjectRegistry
								.create(clazz,
										PCUtils.combineArrays(new Object[] { clazz.getSimpleName() + "#" + System.nanoTime(), ie }, nargs));
						return instance;
					});

		}
		if (NoMeshObject.class.isAssignableFrom(clazz)) {

			return new TaskFuture<>(this.loader, () -> {
				final T instance = GameObjectRegistry
						.create(clazz, PCUtils.combineArrays(new Object[] { clazz.getSimpleName() + "#" + System.nanoTime() }, args));
				return instance;
			});

		}
		return StaticMeshLoader
				.getStaticFuture(this.cache, clazz.getName(), this.dataPath.get(clazz), this.loader, this.render)
				.then(this.loader, (ThrowingFunction<Mesh, T, Throwable>) mesh -> {
					final T instance = GameObjectRegistry
							.create(clazz,
									PCUtils.combineArrays(new Object[] { clazz.getSimpleName() + "#" + System.nanoTime(), mesh }, args));
					return instance;
				});
	}

	public <T extends GameObject> TaskFuture<?, T> create_(final Class<T> clazz, final Scene3D scene, final Object... args) {
		return this.create_(clazz, args).then(this.loader, (ThrowingFunction<T, T, Throwable>) scene::addEntity);
	}

	public <T extends GameObject> TaskFuture<?, T> create_(final Class<T> clazz, final SubEntitiesComponent parent, final Object... args) {
		return this.create_(clazz, args).then(this.loader, (ThrowingFunction<T, T, Throwable>) e -> {
			parent.addEntity(e);
			return e;
		});
	}

	public static <T extends GameObject> TaskFuture<?, T> create(final Class<T> clazz, final Object... args) {
		return INSTANCE.create_(clazz, args);
	}

	public static <T extends GameObject> TaskFuture<?, T> create(
			final Class<T> clazz,
			final SubEntitiesComponent parent,
			final Object... args) {
		return INSTANCE.create_(clazz, parent, args);
	}

	public static <T extends GameObject> TaskFuture<?, T> create(final Class<T> clazz, final Scene3D scene, final Object... args) {
		return INSTANCE.create_(clazz, scene, args);
	}

}
