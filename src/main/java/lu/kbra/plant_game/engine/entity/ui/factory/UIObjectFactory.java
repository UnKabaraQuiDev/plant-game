package lu.kbra.plant_game.engine.entity.ui.factory;

import java.util.HashMap;
import java.util.Map;

import org.joml.Vector2f;

import lu.pcy113.pclib.PCUtils;
import lu.pcy113.pclib.concurrency.ListTriggerLatch;
import lu.pcy113.pclib.impl.ThrowingFunction;

import lu.kbra.plant_game.engine.entity.ui.AnimatedUIObject;
import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.entity.ui.group.ObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.layout.SpacerUIObject;
import lu.kbra.plant_game.engine.entity.ui.scroller.FlatQuadUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.TextUIObject;
import lu.kbra.plant_game.engine.entity.ui.texture.GradientQuadUIObject;
import lu.kbra.plant_game.engine.entity.ui.texture.TextureUIObject;
import lu.kbra.plant_game.engine.locale.NoMeshObject;
import lu.kbra.plant_game.engine.mesh.TexturedMesh;
import lu.kbra.plant_game.engine.mesh.TexturedQuadMesh;
import lu.kbra.plant_game.engine.mesh.loader.AnimatedMeshLoader;
import lu.kbra.plant_game.engine.mesh.loader.AnimatedMeshLoader.AnimatedMeshes;
import lu.kbra.plant_game.engine.mesh.loader.StaticMeshLoader;
import lu.kbra.plant_game.engine.mesh.loader.StaticTextLoader;
import lu.kbra.plant_game.engine.mesh.loader.StaticTexturedMeshLoader;
import lu.kbra.plant_game.engine.render.GradientMesh;
import lu.kbra.plant_game.engine.scene.ui.UIScene;
import lu.kbra.plant_game.engine.util.annotation.BufferSize;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.plant_game.generated.UIObjectRegistry;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.TaskFuture;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextAlignment;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class UIObjectFactory {

	public static class TextData {

		protected Vector2f charSize;
		protected TextAlignment textAlignment;
		protected int bufferSize;
		protected String name;

		public TextData(final Vector2f charSize, final TextAlignment textAlignment, final int bufferSize, final String name) {
			this.charSize = charSize;
			this.textAlignment = textAlignment;
			this.bufferSize = bufferSize;
			this.name = name;
		}

		public TextData(final Vector2f charSize, final TextAlignment textAlignment, final int bufferSize) {
			this.charSize = charSize;
			this.textAlignment = textAlignment;
			this.bufferSize = bufferSize;
		}

		public TextData(final Vector2f charSize, final TextAlignment textAlignment) {
			this.charSize = charSize;
			this.textAlignment = textAlignment;
		}

		public Vector2f getCharSize() {
			return this.charSize;
		}

		public void setCharSize(final Vector2f charSize) {
			this.charSize = charSize;
		}

		public TextAlignment getTextAlignment() {
			return this.textAlignment;
		}

		public void setTextAlignment(final TextAlignment textAlignment) {
			this.textAlignment = textAlignment;
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

	public static final Vector2f DEFAULT_CHAR_SIZE = new Vector2f(0.5f);
	public static final int DEFAULT_BUFFER_SIZE = 12;
	public static final TextData DEFAULT_TEXT_DATA = new TextData(DEFAULT_CHAR_SIZE, TextAlignment.LEFT, DEFAULT_BUFFER_SIZE, null);

	public static UIObjectFactory INSTANCE;

	private final Map<Class<? extends UIObject>, Boolean> animatedMesh = new HashMap<>();
	private final Map<Class<? extends UIObject>, String> dataPath = new HashMap<>();
	private final Map<Class<? extends UIObject>, Integer> bufferSize = new HashMap<>();

	private final CacheManager cache;
	private final Dispatcher loader, render;

	public UIObjectFactory(final CacheManager cache, final Dispatcher loader, final Dispatcher render) {
		this.cache = cache;
		this.loader = loader;
		this.render = render;
	}

	public <T extends UIObject> TaskFuture<?, T> create_(final Class<T> clazz, final Object... args) {
		this.animatedMesh.computeIfAbsent(clazz, k -> AnimatedUIObject.class.isAssignableFrom(k));
		this.dataPath.computeIfAbsent(clazz, k -> {
			if (!k.isAnnotationPresent(DataPath.class)) {
				throw new IllegalArgumentException(clazz.getName() + " doesn't have @DataPath.");
			}
			return k.getAnnotation(DataPath.class).value();
		});

		final String cDataPath = this.dataPath.get(clazz);

		if (this.animatedMesh.get(clazz)) {
			PCUtils.throwUnsupported();
			return null;
		}

		if (cDataPath.endsWith("json")) { // data file

			PCUtils.throwUnsupported();

			if (this.animatedMesh.get(clazz)) {

				return AnimatedMeshLoader
						.getAnimatedFuture(this.cache, clazz.getName(), cDataPath, this.loader, this.render)
						.then(this.loader, (ThrowingFunction<AnimatedMeshes, T, Throwable>) meshes -> {
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
			}
			return StaticMeshLoader
					.getStaticFuture(this.cache, clazz.getName(), cDataPath, this.loader, this.render)
					.then(this.loader, (ThrowingFunction<Mesh, T, Throwable>) mesh -> {
						final T instance = UIObjectRegistry
								.create(clazz,
										PCUtils
												.combineArrays(new Object[] { clazz.getSimpleName() + "#" + System.nanoTime(), mesh },
														args));
						return instance;
					});

		}
		if (TextUIObject.class.isAssignableFrom(clazz) && (cDataPath.startsWith("localization:") || cDataPath.isEmpty())) {

			final String key = cDataPath.substring(cDataPath.indexOf(":") + 1);

			TextData td;
			final Object[] nargs;
			if (args.length > 0 && args[0] instanceof final TextData vvec) {
				td = vvec;
				nargs = PCUtils.removeArray(args, 0);
			} else {
				td = DEFAULT_TEXT_DATA;
				nargs = args;

				if (this.bufferSize
						.computeIfAbsent(clazz,
								c -> clazz.isAnnotationPresent(BufferSize.class) ? clazz.getAnnotation(BufferSize.class).value()
										: -1) != -1) {
					td = new TextData(td.charSize, td.textAlignment, this.bufferSize.get(clazz));
				}
			}

			return StaticTextLoader
					.getFuture(this.cache, td.name == null ? key : td.name, key, td, this.loader, this.render)
					.then(this.loader, (ThrowingFunction<TextEmitter, T, Throwable>) te -> {
						final T instance = UIObjectRegistry
								.create(clazz,
										PCUtils.combineArrays(new Object[] { clazz.getSimpleName() + "#" + System.nanoTime(), te }, nargs));
						return instance;
					});

		}
		if (TextureUIObject.class.isAssignableFrom(clazz) && cDataPath.startsWith("image:")) {

			final String txtPath = cDataPath.substring(cDataPath.indexOf(":") + 1);

			return StaticTexturedMeshLoader
					.getStaticFuture(this.cache, txtPath, txtPath, this.loader, this.render)
					.then(this.loader, (ThrowingFunction<TexturedMesh, T, Throwable>) mesh -> {
						final T instance = UIObjectRegistry
								.create(clazz,
										PCUtils
												.combineArrays(new Object[] { clazz.getSimpleName() + "#" + System.nanoTime(), mesh },
														args));
						return instance;
					});

		}
		if (NoMeshObject.class.isAssignableFrom(clazz)) {

			return new TaskFuture<>(this.loader, () -> {
				final T instance = UIObjectRegistry
						.create(clazz, PCUtils.combineArrays(new Object[] { clazz.getSimpleName() + "#" + System.nanoTime() }, args));
				return instance;
			});

		}
		if (GradientQuadUIObject.class.isAssignableFrom(clazz)) {

			return StaticGradientMeshLoader
					.getStaticFuture(this.cache,
							cDataPath.isBlank() ? GradientQuadUIObject.class.getName() : cDataPath,
							cDataPath,
							this.loader,
							this.render)
					.then(this.loader, (ThrowingFunction<GradientMesh, T, Throwable>) mesh -> {
						final T instance = UIObjectRegistry
								.create(clazz,
										PCUtils
												.combineArrays(new Object[] { clazz.getSimpleName() + "#" + System.nanoTime(), mesh },
														args));
						return instance;
					});

		}
		if (FlatQuadUIObject.class.isAssignableFrom(clazz)) {

			return StaticFlatMeshLoader
					.getStaticFuture(this.cache, this.loader, this.render)
					.then(this.loader, (ThrowingFunction<TexturedQuadMesh, T, Throwable>) mesh -> {
						final T instance = UIObjectRegistry
								.create(clazz,
										PCUtils
												.combineArrays(new Object[] { clazz.getSimpleName() + "#" + System.nanoTime(), mesh },
														args));
						return instance;
					});

		}
		PCUtils.throwUnsupported();
		return null;
	}

	public <T extends UIObject> TaskFuture<?, T> create_(final Class<T> clazz, final UIScene scene, final Object... args) {
		return this.create_(clazz, args).then(this.loader, (ThrowingFunction<T, T, Throwable>) scene::addEntity);
	}

	@SuppressWarnings("unchecked")
	public <T extends UIObject, V extends T> TaskFuture<?, T> create_(
			final Class<T> clazz,
			final ObjectGroup<V> obj,
			final Object... args) {
		return this.create_(clazz, args).then(this.loader, (ThrowingFunction<T, T, Throwable>) (final T t) -> obj.add((V) t));
	}

	public <T extends UIObject, V extends T> TaskFuture<?, T> create_(
			final Class<T> clazz,
			final ListTriggerLatch<V> obj,
			final Object... args) {
		return this.create_(clazz, args).then(this.loader, (ThrowingFunction<T, T, Throwable>) (final T t) -> {
			obj.add((V) t);
			return t;
		});
	}

	public static <T extends UIObject> TaskFuture<?, T> create(final Class<T> clazz, final Object... args) {
		return INSTANCE.create_(clazz, args);
	}

	public static <T extends UIObject> TaskFuture<?, T> create(final Class<T> clazz, final UIScene scene, final Object... args) {
		return INSTANCE.create_(clazz, scene, args);
	}

	public static <T extends UIObject> TaskFuture<?, T> create(final Class<T> clazz, final ListTriggerLatch latch, final Object... args) {
		return INSTANCE.create_(clazz, latch, args);
	}

	public static <T extends UIObject> TaskFuture<?, T> create(final Class<T> clazz, final ObjectGroup obj, final Object... args) {
		return INSTANCE.create_(clazz, obj, args);
	}

	public static UIObject createVerticalSpacer(final float f) {
		return new SpacerUIObject("spacer-v-" + System.nanoTime() % 200_000, null, new Transform3D(), new Vector2f(1f, f));
	}

	public static UIObject createHorizontalSpacer(final float f) {
		return new SpacerUIObject("spacer-h-" + System.nanoTime() % 200_000, null, new Transform3D(), new Vector2f(f, 1f));
	}

	public static UIObject createSpacer(final float x, final float y) {
		return new SpacerUIObject("spacer-" + System.nanoTime() % 200_000, null, new Transform3D(), new Vector2f(x, y));
	}

}
