package lu.kbra.plant_game.engine.entity.ui.factory;

import java.util.HashMap;
import java.util.Map;

import org.joml.Vector2f;

import lu.pcy113.pclib.PCUtils;
import lu.pcy113.pclib.impl.ThrowingFunction;

import lu.kbra.plant_game.engine.entity.ui.btn.TextUIObject;
import lu.kbra.plant_game.engine.entity.ui.impl.AnimatedUIObject;
import lu.kbra.plant_game.engine.entity.ui.impl.UIObject;
import lu.kbra.plant_game.engine.mesh.loader.AnimatedMeshLoader;
import lu.kbra.plant_game.engine.mesh.loader.AnimatedMeshLoader.AnimatedMeshes;
import lu.kbra.plant_game.engine.mesh.loader.StaticMeshLoader;
import lu.kbra.plant_game.engine.mesh.loader.StaticTextLoader;
import lu.kbra.plant_game.engine.scene.ui.UIScene;
import lu.kbra.plant_game.engine.util.DataPath;
import lu.kbra.plant_game.generated.UIObjectRegistry;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.TaskFuture;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextAlignment;

public class UIObjectFactory {

	public static final Vector2f DEFAULT_CHAR_SIZE = new Vector2f(0.1f);
	public static UIObjectFactory INSTANCE;

	private final Map<Class<? extends UIObject>, Boolean> animatedMesh = new HashMap<>();
	private final Map<Class<? extends UIObject>, String> dataPath = new HashMap<>();

	private final CacheManager cache;
	private final Dispatcher loader, render;

	public UIObjectFactory(CacheManager cache, Dispatcher loader, Dispatcher render) {
		this.cache = cache;
		this.loader = loader;
		this.render = render;
	}

	public <T extends UIObject> TaskFuture<?, T> create_(Class<T> clazz, Object... args) {
		animatedMesh.computeIfAbsent(clazz, k -> AnimatedUIObject.class.isAssignableFrom(k));
		dataPath.computeIfAbsent(clazz, k -> {
			if (!k.isAnnotationPresent(DataPath.class)) {
				throw new IllegalArgumentException(clazz.getName() + " doesn't have @DataPath.");
			}
			return k.getAnnotation(DataPath.class).value();
		});

		if (dataPath.get(clazz).endsWith("json")) { // data file

			PCUtils.throwUnsupported();

			if (animatedMesh.get(clazz)) {

				return AnimatedMeshLoader
						.getAnimatedFuture(cache, clazz.getName(), dataPath.get(clazz), loader, render)
						.then(loader, (ThrowingFunction<AnimatedMeshes, T, Throwable>) (meshes) -> {
							final T instance = UIObjectRegistry
									.create(clazz,
											PCUtils
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
						.then(loader, (ThrowingFunction<Mesh, T, Throwable>) (mesh) -> {
							final T instance = UIObjectRegistry
									.create(clazz,
											PCUtils
													.combineArrays(new Object[] { clazz.getSimpleName() + "#" + System.nanoTime(), mesh },
															args));
							return instance;
						});

			}

		} else if (TextUIObject.class.isAssignableFrom(clazz) && dataPath.get(clazz).startsWith("localization:")) {

			final String key = dataPath.get(clazz).substring(dataPath.get(clazz).indexOf(":") + 1);
			System.err.println(key);
			return StaticTextLoader
					.getFuture(cache, key, key, DEFAULT_CHAR_SIZE, TextAlignment.LEFT, loader, render)
					.then(loader, (ThrowingFunction<TextEmitter, T, Throwable>) (te) -> {
						final T instance = UIObjectRegistry
								.create(clazz,
										PCUtils.combineArrays(new Object[] { clazz.getSimpleName() + "#" + System.nanoTime(), te }, args));
						return instance;
					});

		} else {

			if (animatedMesh.get(clazz)) {

				PCUtils.throwUnsupported();
				return null;

			} else {

				final String txtPath = dataPath.get(clazz);

				return StaticMeshLoader
						.getStaticFuture(cache, txtPath, txtPath, loader, render)
						.then(loader, (ThrowingFunction<Mesh, T, Throwable>) (mesh) -> {
							final T instance = UIObjectRegistry
									.create(clazz,
											PCUtils
													.combineArrays(new Object[] { clazz.getSimpleName() + "#" + System.nanoTime(), mesh },
															args));
							return instance;
						});

			}

		}
	}

	public <T extends UIObject> TaskFuture<?, T> create_(Class<T> clazz, UIScene scene, Object... args) {
		return create_(clazz, args).then(loader, (ThrowingFunction<T, T, Throwable>) scene::addEntity);
	}

	public static <T extends UIObject> TaskFuture<?, T> create(Class<T> clazz, Object... args) {
		return INSTANCE.create_(clazz, args);
	}

	public static <T extends UIObject> TaskFuture<?, T> create(Class<T> clazz, UIScene scene, Object... args) {
		return INSTANCE.create_(clazz, scene, args);
	}

}
