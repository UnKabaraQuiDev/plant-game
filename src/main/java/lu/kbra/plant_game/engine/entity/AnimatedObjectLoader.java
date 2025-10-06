package lu.kbra.plant_game.engine.entity;

import java.net.URI;
import java.util.function.Function;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.json.JSONArray;
import org.json.JSONObject;

import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.geom.utils.ObjLoader;
import lu.kbra.standalone.gameengine.graph.texture.SingleTexture;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.TaskFuture;
import lu.pcy113.pclib.PCUtils;

public class AnimatedObjectLoader {

	public record AnimatedMeshData(String filePath, boolean textureMaterial, String texturePath, AnimationData animData) {

	}

	public record AnimationData(Vector3f origin, Vector3f startPosition, Vector3f endPosition,
			Quaternionf startRotation, Quaternionf endRotation, Vector3f startScale, Vector3f endScale) {
	}

	public static AnimatedMeshData getAnimatedMeshData(String path) {
		try {
			final URI baseURI = URI.create(path);

			final JSONObject obj = new JSONObject(PCUtils.readStringSource(path));
			final JSONArray exports = obj.getJSONArray("exports");

			for (int i = 0; i < exports.length(); i++) {
				final JSONObject jsonObj = exports.getJSONObject(i);

				if (!jsonObj.getString("type").equals("animated"))
					continue;

				final String filePath = jsonObj.getString("file");

				final String meshFilePath = baseURI.resolve(filePath).toString();
				final boolean textureMaterial = jsonObj.getBoolean("texture_material");
				final String texturePath = textureMaterial
						? baseURI.resolve(jsonObj.optString("texture_path")).toString()
						: jsonObj.optString("texture_path");
				
				final AnimationData animData = new AnimationData(null, null, null, null, null, null, null);

				return new AnimatedMeshData(meshFilePath, textureMaterial, texturePath, animData);
			}
		} catch (Exception e) {
			throw new RuntimeException("Error while getting static mesh file from: " + path, e);
		}

		throw new RuntimeException("No animated mesh in: " + path);
	}

	public static Mesh getStaticDirect(CacheManager cache, String meshName, String path) {
		if (cache.hasMesh(meshName)) {
			return cache.getMesh(meshName);
		}

		final AnimatedMeshData meshData = getAnimatedMeshData(path);

		final Mesh staticMesh = ObjLoader.loadMesh(meshName, null, meshData.filePath());
		cache.addMesh(staticMesh);

		return staticMesh;
	}

	public static TaskFuture<?, Mesh> getStaticFuture(CacheManager cache, String meshName, String path,
			Dispatcher loader, Dispatcher render) {
		if (cache.hasMesh(meshName)) {
			return new TaskFuture<>(loader, () -> cache.getMesh(meshName));
		}

		return new TaskFuture<>(loader, () -> getAnimatedMeshData(path)).then(render,
				(Function<AnimatedMeshData, Mesh>) (meshData) -> {
					final Mesh staticMesh;
					if (meshData.textureMaterial()) {
						final SingleTexture txt0 = cache.hasTexture(meshData.texturePath())
								? (SingleTexture) cache.getTexture(meshData.texturePath())
								: SingleTexture.loadSingleTexture(cache, meshData.texturePath(),
										meshData.texturePath());
						staticMesh = AnimatedTexturedObjLoader.loadMesh(meshName, null, meshData.filePath(), txt0, meshData.animData());
					} else {
						staticMesh = ObjLoader.loadMesh(meshName, null, meshData.filePath());
					}
					cache.addMesh(staticMesh);
					return staticMesh;
				});
	}

}
