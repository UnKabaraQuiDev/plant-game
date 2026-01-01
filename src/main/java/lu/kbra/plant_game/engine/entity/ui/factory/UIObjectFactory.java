package lu.kbra.plant_game.engine.entity.ui.factory;

import static lu.kbra.plant_game.generated.UIObjectRegistry.BUFFER_SIZE;
import static lu.kbra.plant_game.generated.UIObjectRegistry.DATA_PATH;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Function;
import java.util.function.Supplier;

import org.joml.Vector2f;
import org.joml.Vector2fc;

import lu.kbra.plant_game.engine.entity.impl.MeshOwner;
import lu.kbra.plant_game.engine.entity.impl.NoMeshObject;
import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.entity.ui.UOCreatingTaskFuture;
import lu.kbra.plant_game.engine.entity.ui.prim.QuadMeshOwner;
import lu.kbra.plant_game.engine.entity.ui.prim.TexturedQuadMeshOwner;
import lu.kbra.plant_game.engine.entity.ui.text.ProgrammaticUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.TextEmitterOwner;
import lu.kbra.plant_game.engine.mesh.TexturedQuadMesh;
import lu.kbra.plant_game.engine.mesh.loader.StaticMeshLoader;
import lu.kbra.plant_game.engine.mesh.loader.StaticTextLoader;
import lu.kbra.plant_game.engine.mesh.loader.StaticTexturedMeshLoader;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.cache.attrib.impl.AttribArray;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.TaskFuture;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextAlignment;

public class UIObjectFactory {

	public static final Vector2f DEFAULT_CHAR_SIZE = new Vector2f(0.5f);
	public static final int DEFAULT_BUFFER_SIZE = 12;
	public static final TextAlignment DEFAULT_TEXT_ALIGNMENT = TextAlignment.LEFT;

	public static UIObjectFactory INSTANCE;

	private final Map<Class<? extends UIObject>, Boolean> animatedMesh = new HashMap<>();
	private final Map<Class<? extends UIObject>, String> dataPath = new HashMap<>();
	private final Map<Class<? extends UIObject>, Integer> bufferSize = new HashMap<>();

	private final CacheManager cache;
	private final Dispatcher loader;
	private final Dispatcher render;

	public UIObjectFactory(final CacheManager cache, final Dispatcher loader, final Dispatcher render) {
		this.cache = cache;
		this.loader = loader;
		this.render = render;
	}

	public <T extends UIObject & TextEmitterOwner> UOCreatingTaskFuture<T> createText_(
			final Class<T> clazz,
			final OptionalInt bufferSize,
			final Optional<Vector2fc> charSize,
			final Optional<TextAlignment> textAlignment,
			final Optional<String> name,
			final Optional<String> key,
			final Supplier<AttribArray>... attribs) {

		return StaticTextLoader
				.getFuture(this.cache,
						name.orElse(clazz.getSimpleName()),
						key.orElse(DATA_PATH.get(clazz)),
						charSize.orElse(DEFAULT_CHAR_SIZE),
						textAlignment.orElse(DEFAULT_TEXT_ALIGNMENT),
						bufferSize.isEmpty() ? BUFFER_SIZE.containsKey(clazz) ? OptionalInt.of(BUFFER_SIZE.get(clazz)) : OptionalInt.empty()
								: OptionalInt.empty(),
						attribs,
						this.loader,
						this.render)
				.then(this.loader, (Function<TextEmitter, List<Object>>) Arrays::asList)
				.then(new UOCreatingTaskFuture(this.loader, clazz));
	}

	public <T extends UIObject & MeshOwner> UOCreatingTaskFuture<T> createMesh_(final Class<T> clazz) {
		return StaticMeshLoader.getStaticFuture(this.cache, clazz.getName(), DATA_PATH.get(clazz), this.loader, this.render)
				.then(this.loader, (Function<Mesh, List<Object>>) Arrays::asList)
				.then(new UOCreatingTaskFuture(this.loader, clazz));
	}

	public <T extends UIObject & MeshOwner> UOCreatingTaskFuture<T> createQuadMesh_(final Class<T> clazz) {
		if (ProgrammaticUIObject.class.isAssignableFrom(clazz)) {
			return StaticFlatMeshLoader.getStaticFuture(this.cache, this.loader, this.render)
					.then(this.loader, (Function<TexturedQuadMesh, List<Object>>) Arrays::asList)
					.then(new UOCreatingTaskFuture(this.loader, clazz));
		}
		return StaticTexturedMeshLoader.getStaticFuture(this.cache, clazz.getName(), DATA_PATH.get(clazz), this.loader, this.render)
				.then(this.loader, (Function<TexturedQuadMesh, List<Object>>) Arrays::asList)
				.then(new UOCreatingTaskFuture(this.loader, clazz));
	}

	public <T extends UIObject & MeshOwner> UOCreatingTaskFuture<T> createManual_(final Class<T> clazz, final Mesh mesh) {
		return new TaskFuture<>(this.loader, (Supplier<List<Object>>) () -> Arrays.asList(mesh))
				.then(new UOCreatingTaskFuture(this.loader, clazz));
	}

	public <T extends UIObject & NoMeshObject> UOCreatingTaskFuture<T> createNoMesh_(final Class<T> clazz) {
		return new UOCreatingTaskFuture(this.loader, clazz);
	}

	public static <T extends UIObject> UOCreatingTaskFuture<T> create(final Class<T> clazz) {
		if (NoMeshObject.class.isAssignableFrom(clazz)) {
			return INSTANCE.createNoMesh_((Class) clazz);
		}
		if (QuadMeshOwner.class.isAssignableFrom(clazz) || TexturedQuadMeshOwner.class.isAssignableFrom(clazz)) {
			return INSTANCE.createQuadMesh_((Class) clazz);
		}
		if (MeshOwner.class.isAssignableFrom(clazz)) {
			return INSTANCE.createMesh_((Class) clazz);
		}
		if (TextEmitterOwner.class.isAssignableFrom(clazz) && !(ProgrammaticUIObject.class.isAssignableFrom(clazz))) {
			return INSTANCE.createText_((Class) clazz,
					OptionalInt.empty(),
					Optional.empty(),
					Optional.empty(),
					Optional.empty(),
					Optional.empty());
		}
		throw new UnsupportedOperationException(clazz.getName());
	}

	@SafeVarargs
	public static <T extends UIObject & TextEmitterOwner> UOCreatingTaskFuture<T> createText(
			final Class<T> clazz,
			final OptionalInt bufferSize,
			final Optional<Vector2fc> charSize,
			final Optional<TextAlignment> textAlignment,
			final Optional<String> name,
			final Optional<String> key,
			final Supplier<AttribArray>... attribs) {
		return INSTANCE.createText_(clazz, bufferSize, charSize, textAlignment, name, key, attribs);
	}

	public static <T extends UIObject & MeshOwner> UOCreatingTaskFuture<T> createManual(final Class<T> clazz, final Mesh mesh) {
		return INSTANCE.createManual_(clazz, mesh);
	}

}
