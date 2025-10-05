package lu.kbra.plant_game.engine.entity.water;

import java.net.URI;
import java.util.function.Function;

import org.json.JSONArray;
import org.json.JSONObject;

import lu.pcy113.pclib.PCUtils;

import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.geom.utils.ObjLoader;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.TaskFuture;

public class StaticObjectLoader {

	public static String getStaticFile(String path) {
		try {
			final URI baseURI = URI.create(path);

			final JSONObject obj = new JSONObject(PCUtils.readStringSource(path));
			final JSONArray exports = obj.getJSONArray("exports");

			for (int i = 0; i < exports.length(); i++) {
				final JSONObject jsonObj = exports.getJSONObject(i);

				if (!jsonObj.getString("type").equals("static"))
					continue;

				final String filePath = jsonObj.getString("file");

				return baseURI.resolve(filePath).getPath();
			}
		} catch (Exception e) {
			throw new RuntimeException("Error while getting static mesh file from: " + path, e);
		}

		throw new RuntimeException("No static mesh in: " + path);
	}

	public static Mesh getStaticDirect(CacheManager cache, String meshName, String path) {
		if (cache.hasMesh(meshName)) {
			return cache.getMesh(meshName);
		}

		final String filePath = getStaticFile(path);

		final Mesh staticMesh = ObjLoader.loadMesh(meshName, null, filePath);
		cache.addMesh(staticMesh);

		return staticMesh;
	}

	public static TaskFuture<?, Mesh> getStaticFuture(
			CacheManager cache,
			String meshName,
			String path,
			Dispatcher loader,
			Dispatcher render) {
		if (cache.hasMesh(meshName)) {
			return new TaskFuture<>(loader, () -> cache.getMesh(meshName));
		}

		return new TaskFuture<>(loader, () -> getStaticFile(path)).then(render, (Function<String, Mesh>) (filePath) -> {
			final Mesh staticMesh = ObjLoader.loadMesh(meshName, null, filePath);
			cache.addMesh(staticMesh);
			return staticMesh;
		});
	}

}
