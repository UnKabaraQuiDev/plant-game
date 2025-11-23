package lu.kbra.plant_game.engine.entity.go.factory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import lu.pcy113.pclib.PCUtils;
import lu.pcy113.pclib.impl.ThrowingFunction;

import lu.kbra.plant_game.engine.entity.go.impl.AnimatedGameObject;
import lu.kbra.plant_game.engine.entity.go.impl.GameObject;
import lu.kbra.plant_game.engine.entity.go.impl.SwayGameObject;
import lu.kbra.plant_game.engine.mesh.AnimatedMesh;
import lu.kbra.plant_game.engine.mesh.TexturedMesh;
import lu.kbra.plant_game.engine.mesh.loader.AnimatedMeshLoader;
import lu.kbra.plant_game.engine.mesh.loader.AnimatedMeshLoader.AnimatedMeshes;
import lu.kbra.plant_game.engine.mesh.loader.StaticMeshLoader;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.plant_game.generated.GameObjectRegistry;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.TaskFuture;
import lu.kbra.standalone.gameengine.objs.entity.components.SubEntitiesComponent;
import lu.kbra.standalone.gameengine.scene.Scene3D;

public class GameObjectFactory {

	public static GameObjectFactory INSTANCE;

	private final Map<Class<? extends GameObject>, Boolean> animatedMesh = new HashMap<>();
	private final Map<Class<? extends GameObject>, String> dataPath = new HashMap<>();

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
					.then(this.loader, (ThrowingFunction<Mesh, T, Throwable>) mesh -> {
						final T instance = GameObjectRegistry
								.create(clazz,
										PCUtils
												.combineArrays(new Object[] { clazz.getSimpleName() + "#" + System.nanoTime(), mesh },
														args));
						instance.setMaterialId((short) (mesh instanceof TexturedMesh ? ((TexturedMesh) mesh).getTexture().getGlId() : -1));
						return instance;
					});

		}
		return StaticMeshLoader
				.getStaticFuture(this.cache, clazz.getName(), this.dataPath.get(clazz), this.loader, this.render)
				.then(this.loader, (ThrowingFunction<Mesh, T, Throwable>) mesh -> {
					final T instance = GameObjectRegistry
							.create(clazz,
									PCUtils.combineArrays(new Object[] { clazz.getSimpleName() + "#" + System.nanoTime(), mesh }, args));
					instance.setMaterialId((short) (mesh instanceof TexturedMesh ? ((TexturedMesh) mesh).getTexture().getGlId() : -1));
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
