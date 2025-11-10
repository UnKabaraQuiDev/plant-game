// @formatter:off
package lu.kbra.plant_game.generated;

import java.lang.Class;
import java.lang.Object;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lu.kbra.plant_game.engine.entity.go.impl.GameObject;
import lu.kbra.plant_game.engine.entity.go.obj.energy.SolarPanelObject;
import lu.kbra.plant_game.engine.entity.go.obj.water.WaterTowerObject;
import lu.kbra.plant_game.engine.entity.go.obj.water.WaterWheelObject;
import lu.kbra.plant_game.engine.mesh.AnimatedMesh;
import lu.kbra.plant_game.engine.util.InternalConstructorFunction;
import lu.kbra.plant_game.engine.util.exceptions.GameObjectConstructorNotFound;
import lu.kbra.plant_game.engine.util.exceptions.GameObjectNotFound;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;
import org.joml.Vector3i;

public class GameObjectRegistry {
	private static final Map<Class<? extends GameObject>, List<InternalConstructorFunction<GameObject>>> GAME_OBJECT_CONSTRUCTORS;

	static {
		GAME_OBJECT_CONSTRUCTORS = new HashMap<>();

		/*                 WaterTowerObject                 */
		final List<InternalConstructorFunction<GameObject>> listWaterTowerObject = new ArrayList<>();
		listWaterTowerObject.add(new InternalConstructorFunction<>(new Class[] {String.class, Mesh.class}, (Object[] arr) -> (GameObject) new WaterTowerObject((String) arr[0], (Mesh) arr[1])));
		listWaterTowerObject.add(new InternalConstructorFunction<>(new Class[] {String.class, Mesh.class, Transform3D.class}, (Object[] arr) -> (GameObject) new WaterTowerObject((String) arr[0], (Mesh) arr[1], (Transform3D) arr[2])));
		listWaterTowerObject.add(new InternalConstructorFunction<>(new Class[] {String.class, Mesh.class, Transform3D.class, Vector3i.class}, (Object[] arr) -> (GameObject) new WaterTowerObject((String) arr[0], (Mesh) arr[1], (Transform3D) arr[2], (Vector3i) arr[3])));
		GAME_OBJECT_CONSTRUCTORS.put(WaterTowerObject.class, listWaterTowerObject);

		/*                 SolarPanelObject                 */
		final List<InternalConstructorFunction<GameObject>> listSolarPanelObject = new ArrayList<>();
		listSolarPanelObject.add(new InternalConstructorFunction<>(new Class[] {String.class, Mesh.class}, (Object[] arr) -> (GameObject) new SolarPanelObject((String) arr[0], (Mesh) arr[1])));
		listSolarPanelObject.add(new InternalConstructorFunction<>(new Class[] {String.class, Mesh.class, Transform3D.class}, (Object[] arr) -> (GameObject) new SolarPanelObject((String) arr[0], (Mesh) arr[1], (Transform3D) arr[2])));
		listSolarPanelObject.add(new InternalConstructorFunction<>(new Class[] {String.class, Mesh.class, Transform3D.class, Vector3i.class}, (Object[] arr) -> (GameObject) new SolarPanelObject((String) arr[0], (Mesh) arr[1], (Transform3D) arr[2], (Vector3i) arr[3])));
		GAME_OBJECT_CONSTRUCTORS.put(SolarPanelObject.class, listSolarPanelObject);

		/*                 WaterWheelObject                 */
		final List<InternalConstructorFunction<GameObject>> listWaterWheelObject = new ArrayList<>();
		listWaterWheelObject.add(new InternalConstructorFunction<>(new Class[] {String.class, Mesh.class, AnimatedMesh.class}, (Object[] arr) -> (GameObject) new WaterWheelObject((String) arr[0], (Mesh) arr[1], (AnimatedMesh) arr[2])));
		listWaterWheelObject.add(new InternalConstructorFunction<>(new Class[] {String.class, Mesh.class, AnimatedMesh.class, Transform3D.class}, (Object[] arr) -> (GameObject) new WaterWheelObject((String) arr[0], (Mesh) arr[1], (AnimatedMesh) arr[2], (Transform3D) arr[3])));
		listWaterWheelObject.add(new InternalConstructorFunction<>(new Class[] {String.class, Mesh.class, AnimatedMesh.class, Transform3D.class, Vector3i.class}, (Object[] arr) -> (GameObject) new WaterWheelObject((String) arr[0], (Mesh) arr[1], (AnimatedMesh) arr[2], (Transform3D) arr[3], (Vector3i) arr[4])));
		listWaterWheelObject.add(new InternalConstructorFunction<>(new Class[] {String.class, Mesh.class, AnimatedMesh.class, Transform3D.class, Vector3i.class, short.class}, (Object[] arr) -> (GameObject) new WaterWheelObject((String) arr[0], (Mesh) arr[1], (AnimatedMesh) arr[2], (Transform3D) arr[3], (Vector3i) arr[4], (short) arr[5])));
		GAME_OBJECT_CONSTRUCTORS.put(WaterWheelObject.class, listWaterWheelObject);

	}

	@SuppressWarnings("unchecked")
	public static <T extends GameObject> T create(final Class<T> clazz, final Object... args) {
		return (T) get(clazz, args).apply(args);
	}

	public static <T extends GameObject> InternalConstructorFunction<GameObject> get(
			final Class<T> clazz, final Object... args) {
		if (GAME_OBJECT_CONSTRUCTORS.containsKey(clazz)) {
			final Optional<InternalConstructorFunction<GameObject>> bestConstructor = GAME_OBJECT_CONSTRUCTORS.get(clazz).parallelStream().filter((v) -> v.matches(args)).findFirst();
			if (bestConstructor.isPresent()) {
				return bestConstructor.get();
			} else {
				throw new GameObjectConstructorNotFound(clazz, args);
			}
		} else {
			throw new GameObjectNotFound(clazz, args);
		}
	}
}
