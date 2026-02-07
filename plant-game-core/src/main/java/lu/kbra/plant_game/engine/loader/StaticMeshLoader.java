package lu.kbra.plant_game.engine.loader;

import static lu.kbra.plant_game.engine.loader.MeshLoaderLocks.releaseLock;
import static lu.kbra.plant_game.engine.loader.MeshLoaderLocks.waitOrCreateLock;

import java.net.URI;
import java.util.function.Function;

import org.joml.Vector3f;
import org.json.JSONObject;

import lu.kbra.pclib.PCUtils;
import lu.kbra.pclib.impl.ThrowingSupplier;
import lu.kbra.plant_game.engine.util.DelegatingObjLoader;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.graph.texture.SingleTexture;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.SkipThen;
import lu.kbra.standalone.gameengine.impl.future.TaskFuture;
import lu.kbra.standalone.gameengine.impl.future.YieldExecutionThrowable;
import lu.kbra.standalone.gameengine.utils.GameEngineUtils;

public class StaticMeshLoader {

	public record GenericMeshData(String filePath, Vector3f origin, boolean textureMaterial, String texturePath, float deformRatio,
			float speedRatio) {

	}

	public static GenericMeshData getStaticMeshData(final String path) {
		try {
			final URI baseURI = URI.create(path);

			final JSONObject obj = new JSONObject(PCUtils.readStringSource(path));

			return readStaticMeshData(obj, baseURI);
		} catch (final Exception e) {
			throw new RuntimeException("Error while getting static mesh file from: " + path, e);
		}
	}

	public static GenericMeshData readStaticMeshData(final JSONObject obj, final URI baseURI) {
		final JSONObject meshes = obj.getJSONObject("meshes");

		final JSONObject jsonObj = meshes.getJSONObject("static");

		final String filePath = jsonObj.getString("file");

		final String meshFilePath = baseURI.resolve(filePath).toString();
		final Vector3f origin = GameEngineUtils.jsonArrayToVec3f(jsonObj.optJSONArray("offset_from_origin"));
		final boolean textureMaterial = jsonObj.getBoolean("texture_material");
		final String texturePath = textureMaterial ? baseURI.resolve(jsonObj.optString("texture_path")).toString()
				: jsonObj.optString("texture_path");
		final float deformRatio = jsonObj.optFloat("deform_ratio", 1);
		final float speedRatio = jsonObj.optFloat("speed_ratio", 1);

		return new GenericMeshData(meshFilePath, origin, textureMaterial, texturePath, deformRatio, speedRatio);
	}

	public static TaskFuture<?, Mesh> getStaticFuture(
			final CacheManager cache,
			final String meshName,
			final String path,
			final Dispatcher loader,
			final Dispatcher render) {

		return new TaskFuture<>(loader, (ThrowingSupplier<GenericMeshData, Throwable>) () -> {
			if (!cache.hasMesh(meshName) && !waitOrCreateLock(meshName)) {
				throw new YieldExecutionThrowable(() -> cache.hasMesh(meshName));
			}

			if (cache.hasMesh(meshName)) {
				releaseLock(meshName);
				throw new SkipThen(cache.getMesh(meshName));
			}

			return getStaticMeshData(path);
		}).then(render, (Function<GenericMeshData, Mesh>) meshData -> createStatic(cache, meshName, meshData));
	}

	public static Mesh createStatic(final CacheManager cache, final String meshName, final GenericMeshData meshData) {
		final Mesh staticMesh;
		if (meshData.textureMaterial()) {
			final SingleTexture txt0 = cache.hasTexture(meshData.texturePath()) ? (SingleTexture) cache.getTexture(meshData.texturePath())
					: SingleTexture.loadSingleTexture(cache, meshData.texturePath(), meshData.texturePath());

			staticMesh = DelegatingObjLoader.loadTexturedMesh(meshName, null, meshData.filePath(), meshData.origin(), txt0);
		} else {
			staticMesh = DelegatingObjLoader.loadOffsetMesh(meshName, null, meshData.filePath(), meshData.origin());
		}

		if (cache.hasMesh(meshName)) {
			throw new IllegalStateException("Mesh: " + meshName + " already exists.");
		}
		cache.addMesh(staticMesh);
		releaseLock(meshName);
		return staticMesh;
	}

}
