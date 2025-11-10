package lu.kbra.plant_game.engine.entity.ui.factory;

import java.util.HashMap;
import java.util.Map;

import org.joml.Vector2f;

import lu.pcy113.pclib.PCUtils;
import lu.pcy113.pclib.impl.ThrowingFunction;

import lu.kbra.plant_game.engine.entity.ui.impl.AnimatedUIObject;
import lu.kbra.plant_game.engine.entity.ui.impl.TextUIObject;
import lu.kbra.plant_game.engine.entity.ui.impl.TextureUIObject;
import lu.kbra.plant_game.engine.entity.ui.impl.UIObject;
import lu.kbra.plant_game.engine.entity.ui.texture.GradientQuadUIObject;
import lu.kbra.plant_game.engine.mesh.TexturedMesh;
import lu.kbra.plant_game.engine.mesh.loader.AnimatedMeshLoader;
import lu.kbra.plant_game.engine.mesh.loader.AnimatedMeshLoader.AnimatedMeshes;
import lu.kbra.plant_game.engine.mesh.loader.StaticMeshLoader;
import lu.kbra.plant_game.engine.mesh.loader.StaticTextLoader;
import lu.kbra.plant_game.engine.mesh.loader.StaticTexturedMeshLoader;
import lu.kbra.plant_game.engine.render.GradientMesh;
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

	public static final Vector2f DEFAULT_CHAR_SIZE = new Vector2f(0.5f);
	public static final TextData DEFAULT_TEXT_DATA = new TextData(DEFAULT_CHAR_SIZE, TextAlignment.LEFT);
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

	public <T extends UIObject> TaskFuture<?, T> create_(Class<T> clazz, final Object... args) {
		animatedMesh.computeIfAbsent(clazz, k -> AnimatedUIObject.class.isAssignableFrom(k));
		dataPath.computeIfAbsent(clazz, k -> {
			if (!k.isAnnotationPresent(DataPath.class)) {
				throw new IllegalArgumentException(clazz.getName() + " doesn't have @DataPath.");
			}
			return k.getAnnotation(DataPath.class).value();
		});

		final String cDataPath = dataPath.get(clazz);

		if (cDataPath.endsWith("json")) { // data file

			PCUtils.throwUnsupported();

			if (animatedMesh.get(clazz)) {

				return AnimatedMeshLoader
						.getAnimatedFuture(cache, clazz.getName(), cDataPath, loader, render)
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
						.getStaticFuture(cache, clazz.getName(), cDataPath, loader, render)
						.then(loader, (ThrowingFunction<Mesh, T, Throwable>) (mesh) -> {
							final T instance = UIObjectRegistry
									.create(clazz,
											PCUtils
													.combineArrays(new Object[] { clazz.getSimpleName() + "#" + System.nanoTime(), mesh },
															args));
							return instance;
						});

			}

		} else if (TextUIObject.class.isAssignableFrom(clazz) && cDataPath.startsWith("localization:")) {

			final String key = cDataPath.substring(cDataPath.indexOf(":") + 1);

			final TextData td;
			final Object[] nargs;
			if (args[0] instanceof TextData vvec) {
				td = vvec;
				nargs = PCUtils.removeArray(args, 0);
			} else {
				td = DEFAULT_TEXT_DATA;
				nargs = args;
			}

			return StaticTextLoader
					.getFuture(cache, key, key, td, loader, render)
					.then(loader, (ThrowingFunction<TextEmitter, T, Throwable>) (te) -> {
						final T instance = UIObjectRegistry
								.create(clazz,
										PCUtils.combineArrays(new Object[] { clazz.getSimpleName() + "#" + System.nanoTime(), te }, nargs));
						return instance;
					});

		} else if (TextureUIObject.class.isAssignableFrom(clazz) && cDataPath.startsWith("image:")) {

			if (animatedMesh.get(clazz)) {

				PCUtils.throwUnsupported();
				return null;

			} else {

				final String txtPath = cDataPath.substring(cDataPath.indexOf(":") + 1);

				return StaticTexturedMeshLoader
						.getStaticFuture(cache, txtPath, txtPath, loader, render)
						.then(loader, (ThrowingFunction<TexturedMesh, T, Throwable>) (mesh) -> {
							final T instance = UIObjectRegistry
									.create(clazz,
											PCUtils
													.combineArrays(new Object[] { clazz.getSimpleName() + "#" + System.nanoTime(), mesh },
															args));
							return instance;
						});

			}

		} else if (GradientQuadUIObject.class.isAssignableFrom(clazz)) {

			if (animatedMesh.get(clazz)) {

				PCUtils.throwUnsupported();
				return null;

			} else {

				return StaticGradientMeshLoader
						.getStaticFuture(cache,
								cDataPath.isBlank() ? GradientQuadUIObject.class.getName() : cDataPath,
								cDataPath,
								loader,
								render)
						.then(loader, (ThrowingFunction<GradientMesh, T, Throwable>) (mesh) -> {
							final T instance = UIObjectRegistry
									.create(clazz,
											PCUtils
													.combineArrays(new Object[] { clazz.getSimpleName() + "#" + System.nanoTime(), mesh },
															args));
							return instance;
						});

			}

		} else {

			PCUtils.throwUnsupported(clazz.getName() + " & " + cDataPath);
			return null;

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

	public static record TextData(Vector2f charSize, TextAlignment textAlignment) {
	}

}
