package lu.kbra.plant_game.engine.entity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import lu.kbra.plant_game.engine.entity.AnimatedMeshLoader.AnimatedMeshes;
import lu.kbra.plant_game.engine.entity.impl.GameObject;
import lu.kbra.plant_game.engine.entity.water.AnimatedGameObject;
import lu.kbra.plant_game.engine.mesh.AnimatedMesh;
import lu.kbra.plant_game.engine.mesh.TexturedMesh;
import lu.kbra.plant_game.engine.util.DataPath;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.TaskFuture;
import lu.kbra.standalone.gameengine.scene.Scene3D;
import lu.pcy113.pclib.PCUtils;
import lu.pcy113.pclib.impl.ExceptionFunction;

public class GameObjectFactory {

	private static final long LOCK_WAIT_TIMEOUT = 1000;

	public static GameObjectFactory INSTANCE;

	private Map<Class<? extends GameObject>, Boolean> animatedMesh = new HashMap<>();
	private Map<Class<? extends GameObject>, String> dataPath = new HashMap<>();

	private CacheManager cache;
	private Dispatcher loader, render;

	public GameObjectFactory(CacheManager cache, Dispatcher loader, Dispatcher render) {
		this.cache = cache;
		this.loader = loader;
		this.render = render;
	}

	public <T extends GameObject> TaskFuture<?, T> create_(Class<T> clazz, Object... args) {
		animatedMesh.computeIfAbsent(clazz, k -> AnimatedGameObject.class.isAssignableFrom(k));
		dataPath.computeIfAbsent(clazz, k -> {
			if (!k.isAnnotationPresent(DataPath.class))
				throw new IllegalArgumentException(clazz.getName() + " doesn't have @DataPath.");
			return k.getAnnotation(DataPath.class).value();
		});

		if (animatedMesh.get(clazz)) {

			return AnimatedMeshLoader.getAnimatedFuture(cache, clazz.getName(), dataPath.get(clazz), loader, render)
					.then(loader, (ExceptionFunction<AnimatedMeshes, T>) (meshes) -> {
						final T instance = PCUtils.findCompatibleConstructor(clazz,
								PCUtils.combineArrays(new Class[] { String.class, Mesh.class, AnimatedMesh.class },
										Arrays.stream(args).map(Object::getClass).toArray(Class[]::new)))
								.newInstance(PCUtils
										.combineArrays(new Object[] { clazz.getSimpleName() + "#" + System.nanoTime(),
												meshes.staticMesh(), meshes.animatedMesh() }, args));
						return instance;
					});
		} else {

			return StaticMeshLoader.getStaticFuture(cache, clazz.getName(), dataPath.get(clazz), loader, render)
					.then(loader, (ExceptionFunction<Mesh, T>) (mesh) -> {
						final T instance = PCUtils
								.findCompatibleConstructor(clazz,
										PCUtils.combineArrays(new Class[] { String.class, Mesh.class },
												Arrays.stream(args).map(Object::getClass).toArray(Class[]::new)))
								.newInstance(PCUtils.combineArrays(
										new Object[] { clazz.getSimpleName() + "#" + System.nanoTime(), mesh }, args));
						instance.setMaterialId(
								(short) (mesh instanceof TexturedMesh ? ((TexturedMesh) mesh).getTexture().getTid()
										: -1));
						return instance;
					});

		}
	}

	public <T extends GameObject> TaskFuture<?, T> create_(Class<T> clazz, Scene3D scene, Object... args) {
		return create_(clazz, args).then(loader, (ExceptionFunction<T, T>) scene::addEntity);
	}

	public static <T extends GameObject> TaskFuture<?, T> create(Class<T> clazz, Object... args) {
		return INSTANCE.create_(clazz, args);
	}

	public static <T extends GameObject> TaskFuture<?, T> create(Class<T> clazz, Scene3D scene, Object... args) {
		return INSTANCE.create_(clazz, scene, args);
	}

}
