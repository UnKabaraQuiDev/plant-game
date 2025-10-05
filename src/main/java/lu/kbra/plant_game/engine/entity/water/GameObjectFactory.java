package lu.kbra.plant_game.engine.entity.water;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import lu.pcy113.pclib.impl.ExceptionFunction;

import lu.kbra.plant_game.engine.entity.GameObject;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.TaskFuture;
import lu.kbra.standalone.gameengine.scene.Scene3D;

public class GameObjectFactory {

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

	public <T extends GameObject> TaskFuture<?, T> create_(Class<T> clazz) {
		animatedMesh.computeIfAbsent(clazz, k -> AnimatedObject.class.isAssignableFrom(k));
		dataPath.computeIfAbsent(clazz, k -> {
			if (!k.isAnnotationPresent(DataPath.class))
				throw new IllegalArgumentException(clazz.getName() + " doesn't have @DataPath.");
			return k.getAnnotation(DataPath.class).value();
		});

		if (animatedMesh.get(clazz)) {
			throw new UnsupportedOperationException();
		} else {

			return StaticObjectLoader
					.getStaticFuture(cache, clazz.getName(), dataPath.get(clazz), loader, render)
					.then(loader,
							(ExceptionFunction<Mesh, T>) (mesh) -> clazz
									.getConstructor(String.class, Mesh.class)
									.newInstance(clazz.getSimpleName() + "#" + System.nanoTime(), mesh));

		}
	}

	public <T extends GameObject> TaskFuture<?, T> create_(Class<T> clazz, Scene3D scene) {
		return create_(clazz).then(loader, (Function<T, T>) scene::addEntity);
	}

	public static <T extends GameObject> TaskFuture<?, T> create(Class<T> clazz) {
		return INSTANCE.create_(clazz);
	}

	public static <T extends GameObject> TaskFuture<?, T> create(Class<T> clazz, Scene3D scene) {
		return INSTANCE.create_(clazz, scene);
	}

}
