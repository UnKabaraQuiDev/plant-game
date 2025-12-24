package lu.kbra.plant_game.engine.mesh.loader;

import static lu.kbra.plant_game.engine.mesh.loader.MeshLoaderLocks.releaseLock;
import static lu.kbra.plant_game.engine.mesh.loader.MeshLoaderLocks.waitOrCreateLock;

import java.net.URI;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.json.JSONObject;

import lu.pcy113.pclib.PCUtils;
import lu.pcy113.pclib.datastructure.pair.Pair;
import lu.pcy113.pclib.datastructure.pair.Pairs;
import lu.pcy113.pclib.impl.ThrowingFunction;

import lu.kbra.plant_game.engine.mesh.AnimatedMesh;
import lu.kbra.plant_game.engine.mesh.loader.StaticMeshLoader.GenericMeshData;
import lu.kbra.plant_game.engine.util.DelegatingObjLoader;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.graph.texture.SingleTexture;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.SkipThen;
import lu.kbra.standalone.gameengine.impl.future.TaskFuture;
import lu.kbra.standalone.gameengine.utils.GameEngineUtils;

public class AnimatedMeshLoader {

	public record AnimatedMeshes(Mesh staticMesh, AnimatedMesh animatedMesh) {

	}

	public record AnimatedMeshesData(GenericMeshData staticMeshData, AnimatedMeshData animatedMeshData) {

	}

	public record AnimatedMeshData(GenericMeshData animatedMeshData, AnimationData animData) {

	}

	public record AnimationData(Vector3f startPosition, Vector3f endPosition, Quaternionf startRotation, Quaternionf endRotation,
			Vector3f startScale, Vector3f endScale) {
	}

	public static AnimatedMeshData getAnimatedMeshData(final String path) {
		try {
			final URI baseURI = URI.create(path);
			final JSONObject obj = new JSONObject(PCUtils.readStringSource(path));

			return readAnimatedMeshData(obj, baseURI);
		} catch (final Exception e) {
			throw new RuntimeException("Error while getting animated mesh file from: " + path, e);
		}
	}

	public static AnimatedMeshData readAnimatedMeshData(final JSONObject obj, final URI baseURI) {
		if (!obj.getBoolean("animated")) {
			return null;
		}

		final JSONObject meshes = obj.getJSONObject("meshes");

		// -- animated mesh
		final JSONObject jsonObj = meshes.getJSONObject("animated");

		final String filePath = jsonObj.getString("file");

		final String meshFilePath = baseURI.resolve(filePath).toString();
		final Vector3f origin = GameEngineUtils.jsonArrayToVec3f(jsonObj.optJSONArray("offset_from_origin"));
		final boolean textureMaterial = jsonObj.getBoolean("texture_material");
		final String texturePath = textureMaterial ? baseURI.resolve(jsonObj.optString("texture_path")).toString()
				: jsonObj.optString("texture_path");
		final float deformRatio = jsonObj.optFloat("deform_ratio", 1);
		final float speedRatio = jsonObj.optFloat("speed_ratio", 1);

		// -- anim
		final JSONObject animationObj = jsonObj.getJSONObject("animation");
		final Vector3f startPosition = GameEngineUtils.jsonArrayToVec3f(animationObj.getJSONArray("start_position"));
		final Vector3f endPosition = GameEngineUtils.jsonArrayToVec3f(animationObj.getJSONArray("end_position"));
		final Quaternionf startRotation = GameEngineUtils.jsonArrayToQuatf(animationObj.getJSONArray("start_rotation"));
		final Quaternionf endRotation = GameEngineUtils.jsonArrayToQuatf(animationObj.getJSONArray("end_rotation"));
		final Vector3f startScale = GameEngineUtils.jsonArrayToVec3f(animationObj.getJSONArray("start_scale"));
		final Vector3f endScale = GameEngineUtils.jsonArrayToVec3f(animationObj.getJSONArray("end_scale"));

		final AnimationData animData = new AnimationData(startPosition, endPosition, startRotation, endRotation, startScale, endScale);

		return new AnimatedMeshData(
				new GenericMeshData(meshFilePath, origin, textureMaterial, texturePath, deformRatio, speedRatio),
				animData);
	}

	public static TaskFuture<?, AnimatedMeshes> getAnimatedFuture(
			final CacheManager cache,
			final String meshName,
			final String path,
			final Dispatcher loader,
			final Dispatcher render) {
		// if the mesh is available at create time: return it
		if (cache.hasMesh(meshName + "-animated") && cache.hasMesh(meshName)) {
			return new TaskFuture<>(
					loader,
					() -> new AnimatedMeshes(cache.getMesh(meshName), (AnimatedMesh) cache.getMesh(meshName + "-animated")));
		}

		// load both
		return new TaskFuture<>(loader, () -> {
			waitOrCreateLock(meshName);
			waitOrCreateLock(meshName + "-animated");

			if (cache.hasMesh(meshName + "-animated") && cache.hasMesh(meshName)) {
				releaseLock(meshName);
				releaseLock(meshName + "-animated");
				throw new SkipThen(2, new AnimatedMeshes(cache.getMesh(meshName), (AnimatedMesh) cache.getMesh(meshName + "-animated")));
			}

			// need to create animated mesh
			if (cache.hasMesh(meshName)) {
				releaseLock(meshName);
				throw new SkipThen(2, new TaskFuture<>(loader, () -> {
					// waitOrCreateLock(meshName + "-animated");

					final URI baseURI = URI.create(path);
					final JSONObject obj = new JSONObject(PCUtils.readStringSource(path));

					return readAnimatedMeshData(obj, baseURI);
				})
						.then(render,
								(ThrowingFunction<AnimatedMeshData, AnimatedMeshes, Throwable>) obj -> new AnimatedMeshes(
										cache.getMesh(meshName),
										createAnimated(cache, meshName + "-animated", obj))));
			}

			// need to create static mesh
			if (cache.hasMesh(meshName + "-animated")) {
				releaseLock(meshName + "-animated");
				throw new SkipThen(2, new TaskFuture<>(loader, () -> {
					// waitOrCreateLock(meshName);

					final URI baseURI = URI.create(path);
					final JSONObject obj = new JSONObject(PCUtils.readStringSource(path));

					return StaticMeshLoader.readStaticMeshData(obj, baseURI);
				})
						.then(render,
								(ThrowingFunction<GenericMeshData, AnimatedMeshes, Throwable>) obj -> new AnimatedMeshes(
										StaticMeshLoader.createStatic(cache, meshName, obj),
										(AnimatedMesh) cache.getMesh(meshName + "-animated"))));
			}

			final URI baseURI = URI.create(path);
			final JSONObject obj = new JSONObject(PCUtils.readStringSource(path));

			final AnimatedMeshData animatedMeshData = readAnimatedMeshData(obj, baseURI);
			final GenericMeshData staticMeshData = StaticMeshLoader.readStaticMeshData(obj, baseURI);

			return new AnimatedMeshesData(staticMeshData, animatedMeshData);
		})
				.then(render,
						(ThrowingFunction<AnimatedMeshesData, Pair<AnimatedMeshesData, Mesh>, Throwable>) obj -> Pairs
								.readOnly(obj, StaticMeshLoader.createStatic(cache, meshName, obj.staticMeshData)))
				.then(render,
						(ThrowingFunction<Pair<AnimatedMeshesData, Mesh>, AnimatedMeshes, Throwable>) pair -> new AnimatedMeshes(
								pair.getValue(),
								createAnimated(cache, meshName + "-animated", pair.getKey().animatedMeshData())));
	}

	public static AnimatedMesh createAnimated(final CacheManager cache, final String meshName, final AnimatedMeshData animatedMeshData) {
		final GenericMeshData meshData = animatedMeshData.animatedMeshData();
		final AnimatedMesh animatedMesh;
		if (meshData.textureMaterial()) {
			final SingleTexture txt0 = cache.hasTexture(meshData.texturePath()) ? (SingleTexture) cache.getTexture(meshData.texturePath())
					: SingleTexture.loadSingleTexture(cache, meshData.texturePath(), meshData.texturePath());

			animatedMesh = DelegatingObjLoader
					.loadAnimatedTexturedMesh(meshName, null, meshData.filePath(), meshData.origin(), txt0, animatedMeshData.animData());
		} else {
			animatedMesh = DelegatingObjLoader
					.loadAnimatedMesh(meshName, null, meshData.filePath(), meshData.origin(), animatedMeshData.animData());
		}

		cache.addMesh(animatedMesh);
		releaseLock(meshName);
		return animatedMesh;
	}

}
