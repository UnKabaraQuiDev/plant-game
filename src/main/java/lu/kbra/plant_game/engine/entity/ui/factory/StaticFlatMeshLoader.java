package lu.kbra.plant_game.engine.entity.ui.factory;

import static lu.kbra.plant_game.engine.mesh.loader.MeshLoaderLocks.releaseLock;
import static lu.kbra.plant_game.engine.mesh.loader.MeshLoaderLocks.waitOrCreateLock;

import lu.pcy113.pclib.impl.ThrowingFunction;
import lu.pcy113.pclib.impl.ThrowingSupplier;

import lu.kbra.plant_game.engine.entity.ui.prim.QuadMeshUIObject;
import lu.kbra.plant_game.engine.mesh.LoadedTexturedQuadMesh;
import lu.kbra.plant_game.engine.mesh.TexturedQuadMesh;
import lu.kbra.standalone.gameengine.GameEngine;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.graph.texture.SingleTexture;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.SkipThen;
import lu.kbra.standalone.gameengine.impl.future.TaskFuture;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextureFilter;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextureWrap;
import lu.kbra.standalone.gameengine.utils.mem.img.MemImage;

public class StaticFlatMeshLoader {

	public static final String MESH_NAME = QuadMeshUIObject.class.getName();
	public static final String TEXTURE_NAME = "_WHITE_TXT";

	public static TaskFuture<?, TexturedQuadMesh> getStaticFuture(
			final CacheManager cache,
			final Dispatcher loader,
			final Dispatcher render) {

		return new TaskFuture<>(loader, (ThrowingSupplier<SingleTexture, Throwable>) () -> {
			waitOrCreateLock(MESH_NAME);

			if (cache.hasTexture(TEXTURE_NAME)) {
				throw new SkipThen(1, cache.getTexture(TEXTURE_NAME));
			}

			if (cache.hasMesh(MESH_NAME)) {
				throw new SkipThen(2, cache.getMesh(MESH_NAME));
			}

			final SingleTexture whiteTexture = new SingleTexture(TEXTURE_NAME, MemImage.fromDirect(1, 1, 3, buffer -> {
				buffer.put((byte) 255).put((byte) 255).put((byte) 255);
				buffer.flip();
			}));
			whiteTexture.setFilters(TextureFilter.NEAREST);
			whiteTexture.setWraps(TextureWrap.CLAMP_TO_EDGE);

			return whiteTexture;
		}).then(render, (ThrowingFunction<SingleTexture, SingleTexture, Throwable>) whiteTexture -> {
			whiteTexture.setup();
			cache.addTexture(whiteTexture);
			return whiteTexture;
		}).then(render, (ThrowingFunction<SingleTexture, TexturedQuadMesh, Throwable>) txt -> {
			if (cache.hasMesh(MESH_NAME)) {
				return (TexturedQuadMesh) cache.getMesh(MESH_NAME);
			}

			return createStaticQuad(cache, txt);
		});

	}

	static TexturedQuadMesh createStaticQuad(final CacheManager cache, final SingleTexture texture) {
		final TexturedQuadMesh staticMesh = new LoadedTexturedQuadMesh(MESH_NAME, null, GameEngine.IDENTITY_VECTOR2F, texture);
		if (cache.hasMesh(MESH_NAME)) {
			throw new IllegalStateException("Mesh: " + MESH_NAME + " already exists.");
		}
		cache.addMesh(staticMesh);
		releaseLock(MESH_NAME);
		return staticMesh;
	}

}
