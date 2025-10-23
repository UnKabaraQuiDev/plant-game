package lu.kbra.plant_game;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import lu.pcy113.pclib.PCUtils;
import lu.pcy113.pclib.impl.ExceptionFunction;

import lu.kbra.plant_game.engine.entity.AnimatedMeshLoader;
import lu.kbra.plant_game.engine.entity.AnimatedMeshLoader.AnimatedMeshes;
import lu.kbra.plant_game.engine.entity.StaticMeshLoader;
import lu.kbra.plant_game.engine.entity.impl.AnimatedUIObject;
import lu.kbra.plant_game.engine.entity.impl.UIObject;
import lu.kbra.plant_game.engine.mesh.AnimatedMesh;
import lu.kbra.plant_game.engine.scene.ui.UIScene;
import lu.kbra.plant_game.engine.util.DataPath;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.TaskFuture;

public class UIObjectFactory {

	public static UIObjectFactory INSTANCE;

	private Map<Class<? extends UIObject>, Boolean> animatedMesh = new HashMap<>();
	private Map<Class<? extends UIObject>, String> dataPath = new HashMap<>();

	private CacheManager cache;
	private Dispatcher loader, render;

	public UIObjectFactory(CacheManager cache, Dispatcher loader, Dispatcher render) {
		this.cache = cache;
		this.loader = loader;
		this.render = render;
	}

	public <T extends UIObject> TaskFuture<?, T> create_(Class<T> clazz, Object... args) {
		animatedMesh.computeIfAbsent(clazz, k -> AnimatedUIObject.class.isAssignableFrom(k));
		dataPath.computeIfAbsent(clazz, k -> {
			if (!k.isAnnotationPresent(DataPath.class))
				throw new IllegalArgumentException(clazz.getName() + " doesn't have @DataPath.");
			return k.getAnnotation(DataPath.class).value();
		});

		if (dataPath.get(clazz).endsWith("json")) { // data file

			if (animatedMesh.get(clazz)) {

				return AnimatedMeshLoader
						.getAnimatedFuture(cache, clazz.getName(), dataPath.get(clazz), loader, render)
						.then(loader, (ExceptionFunction<AnimatedMeshes, T>) (meshes) -> {
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
			} else {

				return StaticMeshLoader
						.getStaticFuture(cache, clazz.getName(), dataPath.get(clazz), loader, render)
						.then(loader, (ExceptionFunction<Mesh, T>) (mesh) -> {
							final T instance = PCUtils
									.findCompatibleConstructor(clazz,
											PCUtils
													.combineArrays(new Class[] { String.class, Mesh.class },
															Arrays.stream(args).map(Object::getClass).toArray(Class[]::new)))
									.newInstance(PCUtils
											.combineArrays(new Object[] { clazz.getSimpleName() + "#" + System.nanoTime(), mesh }, args));
							return instance;
						});

			}

		} else {

			if (animatedMesh.get(clazz)) {

				return StaticTextureLoader
						.getStaticFuture(cache, clazz.getName(), dataPath.get(clazz), loader, render)
						.then(loader, (ExceptionFunction<AnimatedMeshes, T>) (meshes) -> {
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
			} else {

				return StaticTextureLoader
						.getStaticFuture(cache, clazz.getName(), dataPath.get(clazz), loader, render)
						.then(loader, (ExceptionFunction<Mesh, T>) (mesh) -> {
							final T instance = PCUtils
									.findCompatibleConstructor(clazz,
											PCUtils
													.combineArrays(new Class[] { String.class, Mesh.class },
															Arrays.stream(args).map(Object::getClass).toArray(Class[]::new)))
									.newInstance(PCUtils
											.combineArrays(new Object[] { clazz.getSimpleName() + "#" + System.nanoTime(), mesh }, args));
							return instance;
						});

			}

		}
	}

	public <T extends UIObject> TaskFuture<?, T> create_(Class<T> clazz, UIScene scene, Object... args) {
		return create_(clazz, args).then(loader, (ExceptionFunction<T, T>) scene::addEntity);
	}

	public static <T extends UIObject> TaskFuture<?, T> create(Class<T> clazz, Object... args) {
		return INSTANCE.create_(clazz, args);
	}

	public static <T extends UIObject> TaskFuture<?, T> create(Class<T> clazz, UIScene scene, Object... args) {
		return INSTANCE.create_(clazz, scene, args);
	}

}
