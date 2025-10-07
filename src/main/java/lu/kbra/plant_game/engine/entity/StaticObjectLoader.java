package lu.kbra.plant_game.engine.entity;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;

import org.json.JSONArray;
import org.json.JSONObject;

import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.geom.utils.ObjLoader;
import lu.kbra.standalone.gameengine.graph.texture.SingleTexture;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.TaskFuture;
import lu.pcy113.pclib.PCUtils;

public class StaticObjectLoader {

	public record MeshData(String filePath, boolean textureMaterial, String texturePath) {

	}

	public static MeshData getStaticMeshData(String path) {
		try {
			final URI baseURI = URI.create(path);

			final JSONObject obj = new JSONObject(PCUtils.readStringSource(path));
			final JSONArray exports = obj.getJSONArray("exports");

			for (int i = 0; i < exports.length(); i++) {
				final JSONObject jsonObj = exports.getJSONObject(i);

				if (!jsonObj.getString("type").equals("static"))
					continue;

				final String filePath = jsonObj.getString("file");

				final String meshFilePath = baseURI.resolve(filePath).toString();
				final boolean textureMaterial = jsonObj.getBoolean("texture_material");
				final String texturePath = textureMaterial
						? baseURI.resolve(jsonObj.optString("texture_path")).toString()
						: jsonObj.optString("texture_path");

				return new MeshData(meshFilePath, textureMaterial, texturePath);
			}
		} catch (Exception e) {
			throw new RuntimeException("Error while getting static mesh file from: " + path, e);
		}

		throw new RuntimeException("No static mesh in: " + path);
	}

	public static TaskFuture<?, Mesh> getStaticFuture(CacheManager cache, String meshName, String path,
			Dispatcher loader, Dispatcher render) {
		if (cache.hasMesh(meshName)) {
			return new TaskFuture<>(loader, () -> cache.getMesh(meshName));
		}

		return new TaskFuture<>(loader, () -> getStaticMeshData(path)).then(render,
				(Function<MeshData, Mesh>) (meshData) -> {
					final Mesh staticMesh;
					if (meshData.textureMaterial) {
						final SingleTexture txt0 = cache.hasTexture(meshData.texturePath)
								? (SingleTexture) cache.getTexture(meshData.texturePath)
								: SingleTexture.loadSingleTexture(cache, meshData.texturePath, meshData.texturePath);
						staticMesh = AdvObjLoader.loadMesh(meshName, null, meshData.filePath, txt0);
					} else {
						staticMesh = ObjLoader.loadMesh(meshName, null, meshData.filePath);
					}
					cache.addMesh(staticMesh);
					return staticMesh;
				});
	}

}
