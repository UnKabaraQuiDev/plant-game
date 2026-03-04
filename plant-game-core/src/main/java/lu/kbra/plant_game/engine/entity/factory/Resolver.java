package lu.kbra.plant_game.engine.entity.factory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;

import lu.kbra.plant_game.engine.loader.AnimatedMeshLoader;
import lu.kbra.plant_game.engine.loader.AnimatedMeshLoader.AnimatedMeshes;
import lu.kbra.plant_game.engine.loader.GenericMeshData;
import lu.kbra.plant_game.engine.loader.StaticMeshLoader;
import lu.kbra.plant_game.engine.mesh.AnimatedMesh;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.plant_game.engine.util.annotation.Qualifier;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.graph.texture.SingleTexture;
import lu.kbra.standalone.gameengine.impl.future.TaskFuture;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextureFilter;

/**
 * Minimal dependency resolver.
 * <p>
 * It resolves a few base types (Mesh, AnimatedMesh, SingleTexture) using the
 * existing loaders.
 * <p>
 * Results are cached by {@link DIKey}. The cached value is the output object,
 * not the internal engine cache.
 */
public final class Resolver {

	private final ResolveContext ctx;
	private final Map<DIKey, Object> cache = new ConcurrentHashMap<>();

	public Resolver(final ResolveContext ctx) {
		this.ctx = ctx;
	}

	public ResolveContext context() {
		return this.ctx;
	}

	@SuppressWarnings("unchecked")
	public <T> TaskFuture<?, T> resolveFuture(final DIKey key, final ResolutionKey rKey) {
		// fast path: already resolved
		final Object cached = this.cache.get(key);
		if (cached != null) {
			return new TaskFuture<>(this.ctx.loader(), (Supplier<T>) () -> (T) cached);
		}

		// resolve qualifier + datapath from annotations if missing
		final Qualifier q = rKey == null ? null : rKey.getProperty(Qualifier.class);
		final DataPath dp = rKey == null ? null : rKey.getProperty(DataPath.class);

		final String qualifier = (key.qualifier() == null || key.qualifier().isEmpty()) && q != null ? q.value()
				: key.qualifier();
		final String dataPath = (key.dataPath() == null || key.dataPath().isEmpty()) && dp != null ? dp.value()
				: key.dataPath();
		final DIKey resolvedKey = new DIKey(key.type(), qualifier, dataPath);

		final Object cached2 = this.cache.get(resolvedKey);
		if (cached2 != null) {
			return new TaskFuture<>(this.ctx.loader(), (Supplier<T>) () -> (T) cached2);
		}

		// create
		final TaskFuture<?, T> created = this.create(resolvedKey);
		created.then(this.ctx.loader(), (Function<T, T>) (final T out) -> {
			this.cache.put(resolvedKey, out);
			return out;
		});
		return created;
	}

	@SuppressWarnings("unchecked")
	private <T> TaskFuture<?, T> create(final DIKey key) {
		if (key.type() == GenericMeshData.class) {
			final String path = key.dataPath();
			return new TaskFuture<>(this.ctx.loader(),
					(Supplier<T>) () -> (T) StaticMeshLoader.getStaticMeshData(path));
		}
		if (key.type() == Mesh.class) {
			final String path = key.dataPath();
			final String meshName = (key.qualifier() == null || key.qualifier().isEmpty()) ? path : key.qualifier();
			return (TaskFuture<?, T>) StaticMeshLoader.getStaticFuture(this.ctx.cache(), meshName, path,
					this.ctx.loader(), this.ctx.render());
		}
		if (AnimatedMesh.class.isAssignableFrom(key.type())) {
			final String path = key.dataPath();
			final String meshName = (key.qualifier() == null || key.qualifier().isEmpty()) ? path : key.qualifier();
			return AnimatedMeshLoader
					.getAnimatedFuture(this.ctx.cache(), meshName, path, this.ctx.loader(), this.ctx.render())
					.then(this.ctx.loader(), (Function<AnimatedMeshes, T>) meshes -> (T) meshes.animatedMesh());
		}
		if (key.type() == SingleTexture.class) {
			final String path = key.dataPath();
			final String name = (key.qualifier() == null || key.qualifier().isEmpty()) ? path : key.qualifier();
			return new TaskFuture<>(this.ctx.render(), (Supplier<T>) () -> {
				if (this.ctx.cache().hasTexture(path)) {
					return (T) this.ctx.cache().getTexture(path);
				}
				return (T) SingleTexture.loadSingleTexture(this.ctx.cache(), name, path, TextureFilter.LINEAR);
			});
		}
		throw new UnsupportedOperationException("No provider for type: " + key.type().getName());
	}
}
