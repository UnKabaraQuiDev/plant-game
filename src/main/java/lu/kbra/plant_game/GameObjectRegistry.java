// @formatter:off
package lu.kbra.plant_game;

import java.lang.Class;
import java.lang.Object;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import lu.kbra.plant_game.engine.entity.electric.SolarPanelObject;
import lu.kbra.plant_game.engine.entity.impl.GameObject;
import lu.kbra.plant_game.engine.entity.terrain.TerrainMesh;
import lu.kbra.plant_game.engine.entity.terrain.TerrainObject;
import lu.kbra.plant_game.engine.entity.water.AnimatedGameObject;
import lu.kbra.plant_game.engine.entity.water.WaterTowerObject;
import lu.kbra.plant_game.engine.entity.water.WaterWheelObject;
import lu.kbra.plant_game.engine.mesh.AnimatedMesh;
import lu.kbra.plant_game.exceptions.ClassListKey;
import lu.kbra.plant_game.exceptions.GameObjectConstructorNotFound;
import lu.kbra.plant_game.exceptions.GameObjectNotFound;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;
import org.joml.Vector3i;

public class GameObjectRegistry {
	private static final Map<Class<? extends GameObject>, Map<ClassListKey, Function<Object[], GameObject>>> GAME_OBJECT_CONSTRUCTORS;

	static {
		GAME_OBJECT_CONSTRUCTORS = new HashMap<>();

		/*                 AnimatedGameObject                 */
		final Map<ClassListKey, Function<Object[], GameObject>> listAnimatedGameObject = new HashMap<>();
		listAnimatedGameObject.put(new ClassListKey(String.class, Mesh.class, AnimatedMesh.class), (Object[] arr) -> (GameObject) new AnimatedGameObject((String) arr[0], (Mesh) arr[1], (AnimatedMesh) arr[2]));
		listAnimatedGameObject.put(new ClassListKey(String.class, Mesh.class, AnimatedMesh.class, Transform3D.class), (Object[] arr) -> (GameObject) new AnimatedGameObject((String) arr[0], (Mesh) arr[1], (AnimatedMesh) arr[2], (Transform3D) arr[3]));
		listAnimatedGameObject.put(new ClassListKey(String.class, Mesh.class, AnimatedMesh.class, Transform3D.class, Vector3i.class), (Object[] arr) -> (GameObject) new AnimatedGameObject((String) arr[0], (Mesh) arr[1], (AnimatedMesh) arr[2], (Transform3D) arr[3], (Vector3i) arr[4]));
		listAnimatedGameObject.put(new ClassListKey(String.class, Mesh.class, AnimatedMesh.class, Transform3D.class, Vector3i.class, short.class), (Object[] arr) -> (GameObject) new AnimatedGameObject((String) arr[0], (Mesh) arr[1], (AnimatedMesh) arr[2], (Transform3D) arr[3], (Vector3i) arr[4], (short) arr[5]));
		GAME_OBJECT_CONSTRUCTORS.put(AnimatedGameObject.class, listAnimatedGameObject);

		/*                 SolarPanelObject                 */
		final Map<ClassListKey, Function<Object[], GameObject>> listSolarPanelObject = new HashMap<>();
		listSolarPanelObject.put(new ClassListKey(String.class, Mesh.class), (Object[] arr) -> (GameObject) new SolarPanelObject((String) arr[0], (Mesh) arr[1]));
		listSolarPanelObject.put(new ClassListKey(String.class, Mesh.class, Transform3D.class), (Object[] arr) -> (GameObject) new SolarPanelObject((String) arr[0], (Mesh) arr[1], (Transform3D) arr[2]));
		listSolarPanelObject.put(new ClassListKey(String.class, Mesh.class, Transform3D.class, Vector3i.class), (Object[] arr) -> (GameObject) new SolarPanelObject((String) arr[0], (Mesh) arr[1], (Transform3D) arr[2], (Vector3i) arr[3]));
		GAME_OBJECT_CONSTRUCTORS.put(SolarPanelObject.class, listSolarPanelObject);

		/*                 WaterTowerObject                 */
		final Map<ClassListKey, Function<Object[], GameObject>> listWaterTowerObject = new HashMap<>();
		listWaterTowerObject.put(new ClassListKey(String.class, Mesh.class), (Object[] arr) -> (GameObject) new WaterTowerObject((String) arr[0], (Mesh) arr[1]));
		listWaterTowerObject.put(new ClassListKey(String.class, Mesh.class, Transform3D.class), (Object[] arr) -> (GameObject) new WaterTowerObject((String) arr[0], (Mesh) arr[1], (Transform3D) arr[2]));
		listWaterTowerObject.put(new ClassListKey(String.class, Mesh.class, Transform3D.class, Vector3i.class), (Object[] arr) -> (GameObject) new WaterTowerObject((String) arr[0], (Mesh) arr[1], (Transform3D) arr[2], (Vector3i) arr[3]));
		GAME_OBJECT_CONSTRUCTORS.put(WaterTowerObject.class, listWaterTowerObject);

		/*                 TerrainObject                 */
		final Map<ClassListKey, Function<Object[], GameObject>> listTerrainObject = new HashMap<>();
		listTerrainObject.put(new ClassListKey(String.class, TerrainMesh.class), (Object[] arr) -> (GameObject) new TerrainObject((String) arr[0], (TerrainMesh) arr[1]));
		GAME_OBJECT_CONSTRUCTORS.put(TerrainObject.class, listTerrainObject);

		/*                 WaterWheelObject                 */
		final Map<ClassListKey, Function<Object[], GameObject>> listWaterWheelObject = new HashMap<>();
		listWaterWheelObject.put(new ClassListKey(String.class, Mesh.class, AnimatedMesh.class), (Object[] arr) -> (GameObject) new WaterWheelObject((String) arr[0], (Mesh) arr[1], (AnimatedMesh) arr[2]));
		listWaterWheelObject.put(new ClassListKey(String.class, Mesh.class, AnimatedMesh.class, Transform3D.class), (Object[] arr) -> (GameObject) new WaterWheelObject((String) arr[0], (Mesh) arr[1], (AnimatedMesh) arr[2], (Transform3D) arr[3]));
		listWaterWheelObject.put(new ClassListKey(String.class, Mesh.class, AnimatedMesh.class, Transform3D.class, Vector3i.class), (Object[] arr) -> (GameObject) new WaterWheelObject((String) arr[0], (Mesh) arr[1], (AnimatedMesh) arr[2], (Transform3D) arr[3], (Vector3i) arr[4]));
		listWaterWheelObject.put(new ClassListKey(String.class, Mesh.class, AnimatedMesh.class, Transform3D.class, Vector3i.class, short.class), (Object[] arr) -> (GameObject) new WaterWheelObject((String) arr[0], (Mesh) arr[1], (AnimatedMesh) arr[2], (Transform3D) arr[3], (Vector3i) arr[4], (short) arr[5]));
		GAME_OBJECT_CONSTRUCTORS.put(WaterWheelObject.class, listWaterWheelObject);

	}

	@SuppressWarnings("unchecked")
	public static <T extends GameObject> T create(final Class<T> clazz, final Object... args) {
		return (T) get(clazz, args).apply(args);
	}

	public static <T extends GameObject> Function<Object[], GameObject> get(final Class<T> clazz,
			final Object... args) {
		final ClassListKey key = new ClassListKey(Arrays.stream(args).map(Object::getClass).toArray(Class[]::new));
		if (GAME_OBJECT_CONSTRUCTORS.containsKey(clazz)) {
			if (GAME_OBJECT_CONSTRUCTORS.get(clazz).containsKey(key)) {
				return GAME_OBJECT_CONSTRUCTORS.get(clazz).get(key);
			} else {
				throw new GameObjectConstructorNotFound(clazz, args);
			}
		} else {
			throw new GameObjectNotFound(clazz, args);
		}
	}
}
