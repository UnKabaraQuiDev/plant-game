package lu.kbra.plant_game.engine.entity;

import static lu.kbra.plant_game.engine.entity.MeshLoaderLocks.releaseLock;
import static lu.kbra.plant_game.engine.entity.MeshLoaderLocks.waitOrCreateLock;

import java.net.URI;

import org.joml.Vector3f;
import org.json.JSONObject;

import lu.pcy113.pclib.PCUtils;
import lu.pcy113.pclib.impl.ExceptionFunction;
import lu.pcy113.pclib.impl.ExceptionSupplier;

import lu.kbra.plant_game.engine.util.AdvObjLoader;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.graph.texture.SingleTexture;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.SkipThen;
import lu.kbra.standalone.gameengine.impl.future.TaskFuture;
import lu.kbra.standalone.gameengine.utils.GameEngineUtils;

public class StaticMeshLoader {

	public record GenericMeshData(String filePath, Vector3f origin, boolean textureMaterial, String texturePath) {

	}

	public static GenericMeshData getStaticMeshData(String path) {
		try {
			final URI baseURI = URI.create(path);

			final JSONObject obj = new JSONObject(PCUtils.readStringSource(path));

			return readStaticMeshData(obj, baseURI);
		} catch (Exception e) {
			throw new RuntimeException("Error while getting static mesh file from: " + path, e);
		}
	}

	public static GenericMeshData readStaticMeshData(JSONObject obj, URI baseURI) {
		final JSONObject meshes = obj.getJSONObject("meshes");

		// -- static mesh
		final JSONObject jsonObj = meshes.getJSONObject("static");

		final String filePath = jsonObj.getString("file");

		final String meshFilePath = baseURI.resolve(filePath).toString();
		final Vector3f origin = GameEngineUtils.jsonArrayToVec3f(jsonObj.optJSONArray("offset_from_origin"));
		final boolean textureMaterial = jsonObj.getBoolean("texture_material");
		final String texturePath = textureMaterial ? baseURI.resolve(jsonObj.optString("texture_path")).toString()
				: jsonObj.optString("texture_path");

		return new GenericMeshData(meshFilePath, origin, textureMaterial, texturePath);
	}

	public static TaskFuture<?, Mesh> getStaticFuture(CacheManager cache, String meshName, String path,
			Dispatcher loader, Dispatcher render) {
		return new TaskFuture<>(loader, (ExceptionSupplier<GenericMeshData>) () -> {
			waitOrCreateLock(meshName);

			if (cache.hasMesh(meshName)) {
				throw new SkipThen(cache.getMesh(meshName));
			}

			return getStaticMeshData(path);
		}).then(render, (ExceptionFunction<GenericMeshData, Mesh>) (meshData) -> {
			return createStatic(cache, meshName, meshData);
		});
	}

	static Mesh createStatic(CacheManager cache, String meshName, GenericMeshData meshData) {
		final Mesh staticMesh;
		if (meshData.textureMaterial()) {
			final SingleTexture txt0 = cache.hasTexture(meshData.texturePath())
					? (SingleTexture) cache.getTexture(meshData.texturePath())
					: SingleTexture.loadSingleTexture(cache, meshData.texturePath(), meshData.texturePath());

			staticMesh = AdvObjLoader.loadMesh(meshName, null, meshData.filePath(), meshData.origin(), txt0);
		} else {
			staticMesh = AdvObjLoader.loadMesh(meshName, null, meshData.filePath(), meshData.origin());
		}

		cache.addMesh(staticMesh);
		releaseLock(meshName);
		return staticMesh;
	}

}
