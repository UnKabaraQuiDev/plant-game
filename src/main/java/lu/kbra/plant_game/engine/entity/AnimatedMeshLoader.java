package lu.kbra.plant_game.engine.entity;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.json.JSONObject;

import lu.pcy113.pclib.PCUtils;
import lu.pcy113.pclib.datastructure.pair.Pair;
import lu.pcy113.pclib.datastructure.pair.Pairs;
import lu.pcy113.pclib.impl.ExceptionFunction;
import lu.pcy113.pclib.logger.GlobalLogger;
import lu.kbra.plant_game.engine.entity.StaticMeshLoader.GenericMeshData;
import lu.kbra.plant_game.engine.mesh.AnimatedMesh;
import lu.kbra.plant_game.engine.util.AdvObjLoader;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.graph.texture.SingleTexture;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.SkipThen;
import lu.kbra.standalone.gameengine.impl.future.TaskFuture;
import lu.kbra.standalone.gameengine.utils.GameEngineUtils;

public class AnimatedMeshLoader {

	private static final Map<String, Object> locks = new ConcurrentHashMap<>();
	private static final long LOCK_WAIT_TIMEOUT = 1000;

	public record AnimatedMeshes(Mesh staticMesh, AnimatedMesh animatedMesh) {

	}

	public record AnimatedMeshesData(GenericMeshData staticMeshData, AnimatedMeshData animatedMeshData) {

	}

	public record AnimatedMeshData(GenericMeshData animatedMeshData, AnimationData animData) {

	}

	public record AnimationData(Vector3f startPosition, Vector3f endPosition, Quaternionf startRotation,
			Quaternionf endRotation, Vector3f startScale, Vector3f endScale) {
	}

	public static AnimatedMeshData getAnimatedMeshData(String path) {
		try {
			final URI baseURI = URI.create(path);
			final JSONObject obj = new JSONObject(PCUtils.readStringSource(path));

			return readAnimatedMeshData(obj, baseURI);
		} catch (Exception e) {
			throw new RuntimeException("Error while getting animated mesh file from: " + path, e);
		}
	}

	public static AnimatedMeshData readAnimatedMeshData(JSONObject obj, URI baseURI) {
		if (!obj.getBoolean("animated"))
			return null;

		final JSONObject meshes = obj.getJSONObject("meshes");

		// -- animated mesh
		final JSONObject jsonObj = meshes.getJSONObject("animated");

		final String filePath = jsonObj.getString("file");

		final String meshFilePath = baseURI.resolve(filePath).toString();
		final Vector3f origin = GameEngineUtils.jsonArrayToVec3f(jsonObj.optJSONArray("offset_from_origin"));
		final boolean textureMaterial = jsonObj.getBoolean("texture_material");
		final String texturePath = textureMaterial ? baseURI.resolve(jsonObj.optString("texture_path")).toString()
				: jsonObj.optString("texture_path");

		// -- anim
		final JSONObject animationObj = jsonObj.getJSONObject("animation");
		final Vector3f startPosition = GameEngineUtils.jsonArrayToVec3f(animationObj.getJSONArray("start_position"));
		final Vector3f endPosition = GameEngineUtils.jsonArrayToVec3f(animationObj.getJSONArray("end_position"));
		final Quaternionf startRotation = GameEngineUtils.jsonArrayToQuatf(animationObj.getJSONArray("start_rotation"));
		final Quaternionf endRotation = GameEngineUtils.jsonArrayToQuatf(animationObj.getJSONArray("end_rotation"));
		final Vector3f startScale = GameEngineUtils.jsonArrayToVec3f(animationObj.getJSONArray("start_scale"));
		final Vector3f endScale = GameEngineUtils.jsonArrayToVec3f(animationObj.getJSONArray("end_scale"));

		final AnimationData animData = new AnimationData(startPosition, endPosition, startRotation, endRotation,
				startScale, endScale);

		return new AnimatedMeshData(new GenericMeshData(meshFilePath, origin, textureMaterial, texturePath), animData);
	}

	public static TaskFuture<?, AnimatedMeshes> getAnimatedFuture(CacheManager cache, String meshName, String path,
			Dispatcher loader, Dispatcher render) {
		// if the mesh is available at create time: return it
		if (cache.hasMesh(meshName + "-animated") && cache.hasMesh(meshName)) {
			return new TaskFuture<>(loader, () -> new AnimatedMeshes(cache.getMesh(meshName),
					(AnimatedMesh) cache.getMesh(meshName + "-animated")));
		}

		// create only static
//		if (cache.hasMesh(meshName)) {
//			return new TaskFuture<>(loader, () -> {
//				waitOrCreateLock(meshName);
//
//				if (cache.hasMesh(meshName)) {
//					throw new SkipThen(new AnimatedMeshes(cache.getMesh(meshName),
//							(AnimatedMesh) cache.getMesh(meshName + "-animated")));
//				}
//
//				final URI baseURI = URI.create(path);
//				final JSONObject obj = new JSONObject(PCUtils.readStringSource(path));
//
//				return StaticMeshLoader.readStaticMeshData(obj, baseURI);
//			}).then(render, (ExceptionFunction<GenericMeshData, AnimatedMeshes>) (obj) -> {
//				return new AnimatedMeshes(StaticMeshLoader.createStatic(cache, meshName, obj),
//						(AnimatedMesh) cache.getMesh(meshName + "-animated"));
//			});
//		}

		// create only animated
//		if (cache.hasMesh(meshName)) {
//			return new TaskFuture<>(loader, () -> {
//				waitOrCreateLock(meshName + "-animated");
//
//				if (cache.hasMesh(meshName + "-animated")) {
//					throw new SkipThen(new AnimatedMeshes(cache.getMesh(meshName),
//							(AnimatedMesh) cache.getMesh(meshName + "-animated")));
//				}
//
//				final URI baseURI = URI.create(path);
//				final JSONObject obj = new JSONObject(PCUtils.readStringSource(path));
//
//				final AnimatedMeshData animatedMeshData = readAnimatedMeshData(obj, baseURI);
//
//				return animatedMeshData;
//			}).then(render, (ExceptionFunction<AnimatedMeshData, AnimatedMeshes>) (obj) -> {
//				return new AnimatedMeshes(cache.getMesh(meshName), createAnimated(cache, meshName, obj));
//			});
//		}

		// load both
		return new TaskFuture<>(loader, () -> {
			waitOrCreateLock(meshName);
			waitOrCreateLock(meshName + "-animated");

			if (cache.hasMesh(meshName + "-animated") && cache.hasMesh(meshName)) {
				throw new SkipThen(2, new AnimatedMeshes(cache.getMesh(meshName),
						(AnimatedMesh) cache.getMesh(meshName + "-animated")));
			}

			// need to create animated mesh
			if (cache.hasMesh(meshName)) {
				throw new SkipThen(2, new TaskFuture<>(loader, () -> {
					final URI baseURI = URI.create(path);
					final JSONObject obj = new JSONObject(PCUtils.readStringSource(path));

					return readAnimatedMeshData(obj, baseURI);
				}).then(render, (ExceptionFunction<AnimatedMeshData, AnimatedMeshes>) (obj) -> {
					return new AnimatedMeshes(cache.getMesh(meshName), createAnimated(cache, meshName, obj));
				}));
			}

			// need to create static mesh
			if (cache.hasMesh(meshName + "-animated")) {
				throw new SkipThen(2, new TaskFuture<>(loader, () -> {
					final URI baseURI = URI.create(path);
					final JSONObject obj = new JSONObject(PCUtils.readStringSource(path));

					return StaticMeshLoader.readStaticMeshData(obj, baseURI);
				}).then(render, (ExceptionFunction<GenericMeshData, AnimatedMeshes>) (obj) -> {
					return new AnimatedMeshes(StaticMeshLoader.createStatic(cache, meshName, obj),
							(AnimatedMesh) cache.getMesh(meshName + "-animated"));
				}));
			}

			final URI baseURI = URI.create(path);
			final JSONObject obj = new JSONObject(PCUtils.readStringSource(path));

			final AnimatedMeshData animatedMeshData = readAnimatedMeshData(obj, baseURI);
			final GenericMeshData staticMeshData = StaticMeshLoader.readStaticMeshData(obj, baseURI);

			return new AnimatedMeshesData(staticMeshData, animatedMeshData);
		}).then(render, (ExceptionFunction<AnimatedMeshesData, Pair<AnimatedMeshesData, Mesh>>) (obj) -> {
			return Pairs.readOnly(obj, StaticMeshLoader.createStatic(cache, meshName, obj.staticMeshData));
		}).then(render, (ExceptionFunction<Pair<AnimatedMeshesData, Mesh>, AnimatedMeshes>) (pair) -> {
			return new AnimatedMeshes(pair.getValue(),
					createAnimated(cache, meshName + "-animated", pair.getKey().animatedMeshData()));
		});
	}

	public static AnimatedMesh createAnimated(CacheManager cache, String meshName, AnimatedMeshData animatedMeshData) {
		final GenericMeshData meshData = animatedMeshData.animatedMeshData();
		final AnimatedMesh animatedMesh;
		if (meshData.textureMaterial()) {
			final SingleTexture txt0 = cache.hasTexture(meshData.texturePath())
					? (SingleTexture) cache.getTexture(meshData.texturePath())
					: SingleTexture.loadSingleTexture(cache, meshData.texturePath(), meshData.texturePath());

			animatedMesh = AdvObjLoader.loadMesh(meshName, null, meshData.filePath(), meshData.origin(), txt0,
					animatedMeshData.animData());
		} else {
			animatedMesh = AdvObjLoader.loadMesh(meshName, null, meshData.filePath(), meshData.origin(),
					animatedMeshData.animData());
		}

		releaseLock(meshName);
		cache.addMesh(animatedMesh);
		return animatedMesh;
	}

	private static void waitOrCreateLock(String meshName) throws InterruptedException {
		if (locks.containsKey(meshName)) {
			final Object lock = locks.get(meshName);
			synchronized (lock) {
				int iter = 0;
				while (locks.containsKey(meshName)) {
					System.err.println("wating for lock: " + meshName + " " + Thread.currentThread().getName());
					lock.wait(LOCK_WAIT_TIMEOUT);
					if (iter++ > 10)
						throw new IllegalStateException(
								"Still waiting for mesh: " + meshName + " (" + Thread.currentThread().getName() + ")");
				}
				System.err.println("finished waiting for lock: " + meshName + " " + Thread.currentThread().getName());
			}
		} else {
			System.err.println("creating lock: " + meshName + " " + Thread.currentThread().getName());
			locks.putIfAbsent(meshName, new Object());
		}
	}

	private static void releaseLock(String meshName) {
		if (locks.containsKey(meshName)) {
			final Object lock = locks.get(meshName);
			synchronized (lock) {
				System.err.println("releasing lock: " + meshName + " " + Thread.currentThread().getName());
				lock.notifyAll();
				locks.remove(meshName);
			}
		} else {
			GlobalLogger.severe("Lock wasn't held for: " + meshName);
		}
	}

}
