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
import lu.kbra.plant_game.engine.entity.go.obj.flower.champi.LargeChampiFlowerObject;
import lu.kbra.plant_game.engine.entity.go.obj.flower.champi.MediumChampiFlowerObject;
import lu.kbra.plant_game.engine.entity.go.obj.flower.champi.SmallChampiFlowerObject;
import lu.kbra.plant_game.engine.entity.go.obj.flower.round.LargeRoundFlowerObject;
import lu.kbra.plant_game.engine.entity.go.obj.flower.round.MediumRoundFlowerObject;
import lu.kbra.plant_game.engine.entity.go.obj.flower.round.SmallRoundFlowerObject;
import lu.kbra.plant_game.engine.entity.go.obj.grass.LargeGrassObject;
import lu.kbra.plant_game.engine.entity.go.obj.grass.MediumGrassObject;
import lu.kbra.plant_game.engine.entity.go.obj.grass.SmallGrassObject;
import lu.kbra.plant_game.engine.entity.go.obj.water.WaterTowerObject;
import lu.kbra.plant_game.engine.entity.go.obj.water.WaterWheelObject;
import lu.kbra.plant_game.engine.mesh.AnimatedMesh;
import lu.kbra.plant_game.engine.render.SwayMesh;
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

		/*                 SmallGrassObject                 */
		final List<InternalConstructorFunction<GameObject>> listSmallGrassObject = new ArrayList<>();
		listSmallGrassObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, Transform3D.class}, (Object[] arr) -> (GameObject) new SmallGrassObject((String) arr[0], (SwayMesh) arr[1], (Transform3D) arr[2])));
		listSmallGrassObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, Transform3D.class, short.class}, (Object[] arr) -> (GameObject) new SmallGrassObject((String) arr[0], (SwayMesh) arr[1], (Transform3D) arr[2], (short) arr[3])));
		listSmallGrassObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, float.class, float.class, float.class}, (Object[] arr) -> (GameObject) new SmallGrassObject((String) arr[0], (SwayMesh) arr[1], (float) arr[2], (float) arr[3], (float) arr[4])));
		listSmallGrassObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, Transform3D.class, float.class, float.class, float.class}, (Object[] arr) -> (GameObject) new SmallGrassObject((String) arr[0], (SwayMesh) arr[1], (Transform3D) arr[2], (float) arr[3], (float) arr[4], (float) arr[5])));
		listSmallGrassObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, Transform3D.class, Vector3i.class, float.class, float.class, float.class}, (Object[] arr) -> (GameObject) new SmallGrassObject((String) arr[0], (SwayMesh) arr[1], (Transform3D) arr[2], (Vector3i) arr[3], (float) arr[4], (float) arr[5], (float) arr[6])));
		listSmallGrassObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, Transform3D.class, Vector3i.class, short.class, float.class, float.class}, (Object[] arr) -> (GameObject) new SmallGrassObject((String) arr[0], (SwayMesh) arr[1], (Transform3D) arr[2], (Vector3i) arr[3], (short) arr[4], (float) arr[5], (float) arr[6])));
		listSmallGrassObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, Transform3D.class, Vector3i.class, short.class, float.class, float.class, float.class}, (Object[] arr) -> (GameObject) new SmallGrassObject((String) arr[0], (SwayMesh) arr[1], (Transform3D) arr[2], (Vector3i) arr[3], (short) arr[4], (float) arr[5], (float) arr[6], (float) arr[7])));
		GAME_OBJECT_CONSTRUCTORS.put(SmallGrassObject.class, listSmallGrassObject);

		/*                 LargeRoundFlowerObject                 */
		final List<InternalConstructorFunction<GameObject>> listLargeRoundFlowerObject = new ArrayList<>();
		listLargeRoundFlowerObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, Transform3D.class}, (Object[] arr) -> (GameObject) new LargeRoundFlowerObject((String) arr[0], (SwayMesh) arr[1], (Transform3D) arr[2])));
		listLargeRoundFlowerObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, Transform3D.class, short.class}, (Object[] arr) -> (GameObject) new LargeRoundFlowerObject((String) arr[0], (SwayMesh) arr[1], (Transform3D) arr[2], (short) arr[3])));
		listLargeRoundFlowerObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, float.class, float.class, float.class}, (Object[] arr) -> (GameObject) new LargeRoundFlowerObject((String) arr[0], (SwayMesh) arr[1], (float) arr[2], (float) arr[3], (float) arr[4])));
		listLargeRoundFlowerObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, Transform3D.class, float.class, float.class, float.class}, (Object[] arr) -> (GameObject) new LargeRoundFlowerObject((String) arr[0], (SwayMesh) arr[1], (Transform3D) arr[2], (float) arr[3], (float) arr[4], (float) arr[5])));
		listLargeRoundFlowerObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, Transform3D.class, Vector3i.class, float.class, float.class, float.class}, (Object[] arr) -> (GameObject) new LargeRoundFlowerObject((String) arr[0], (SwayMesh) arr[1], (Transform3D) arr[2], (Vector3i) arr[3], (float) arr[4], (float) arr[5], (float) arr[6])));
		listLargeRoundFlowerObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, Transform3D.class, Vector3i.class, short.class, float.class, float.class}, (Object[] arr) -> (GameObject) new LargeRoundFlowerObject((String) arr[0], (SwayMesh) arr[1], (Transform3D) arr[2], (Vector3i) arr[3], (short) arr[4], (float) arr[5], (float) arr[6])));
		listLargeRoundFlowerObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, Transform3D.class, Vector3i.class, short.class, float.class, float.class, float.class}, (Object[] arr) -> (GameObject) new LargeRoundFlowerObject((String) arr[0], (SwayMesh) arr[1], (Transform3D) arr[2], (Vector3i) arr[3], (short) arr[4], (float) arr[5], (float) arr[6], (float) arr[7])));
		GAME_OBJECT_CONSTRUCTORS.put(LargeRoundFlowerObject.class, listLargeRoundFlowerObject);

		/*                 LargeChampiFlowerObject                 */
		final List<InternalConstructorFunction<GameObject>> listLargeChampiFlowerObject = new ArrayList<>();
		listLargeChampiFlowerObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, Transform3D.class}, (Object[] arr) -> (GameObject) new LargeChampiFlowerObject((String) arr[0], (SwayMesh) arr[1], (Transform3D) arr[2])));
		listLargeChampiFlowerObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, Transform3D.class, short.class}, (Object[] arr) -> (GameObject) new LargeChampiFlowerObject((String) arr[0], (SwayMesh) arr[1], (Transform3D) arr[2], (short) arr[3])));
		listLargeChampiFlowerObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, float.class, float.class, float.class}, (Object[] arr) -> (GameObject) new LargeChampiFlowerObject((String) arr[0], (SwayMesh) arr[1], (float) arr[2], (float) arr[3], (float) arr[4])));
		listLargeChampiFlowerObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, Transform3D.class, float.class, float.class, float.class}, (Object[] arr) -> (GameObject) new LargeChampiFlowerObject((String) arr[0], (SwayMesh) arr[1], (Transform3D) arr[2], (float) arr[3], (float) arr[4], (float) arr[5])));
		listLargeChampiFlowerObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, Transform3D.class, Vector3i.class, float.class, float.class, float.class}, (Object[] arr) -> (GameObject) new LargeChampiFlowerObject((String) arr[0], (SwayMesh) arr[1], (Transform3D) arr[2], (Vector3i) arr[3], (float) arr[4], (float) arr[5], (float) arr[6])));
		listLargeChampiFlowerObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, Transform3D.class, Vector3i.class, short.class, float.class, float.class}, (Object[] arr) -> (GameObject) new LargeChampiFlowerObject((String) arr[0], (SwayMesh) arr[1], (Transform3D) arr[2], (Vector3i) arr[3], (short) arr[4], (float) arr[5], (float) arr[6])));
		listLargeChampiFlowerObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, Transform3D.class, Vector3i.class, short.class, float.class, float.class, float.class}, (Object[] arr) -> (GameObject) new LargeChampiFlowerObject((String) arr[0], (SwayMesh) arr[1], (Transform3D) arr[2], (Vector3i) arr[3], (short) arr[4], (float) arr[5], (float) arr[6], (float) arr[7])));
		GAME_OBJECT_CONSTRUCTORS.put(LargeChampiFlowerObject.class, listLargeChampiFlowerObject);

		/*                 LargeGrassObject                 */
		final List<InternalConstructorFunction<GameObject>> listLargeGrassObject = new ArrayList<>();
		listLargeGrassObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, Transform3D.class}, (Object[] arr) -> (GameObject) new LargeGrassObject((String) arr[0], (SwayMesh) arr[1], (Transform3D) arr[2])));
		listLargeGrassObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, Transform3D.class, short.class}, (Object[] arr) -> (GameObject) new LargeGrassObject((String) arr[0], (SwayMesh) arr[1], (Transform3D) arr[2], (short) arr[3])));
		listLargeGrassObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, float.class, float.class, float.class}, (Object[] arr) -> (GameObject) new LargeGrassObject((String) arr[0], (SwayMesh) arr[1], (float) arr[2], (float) arr[3], (float) arr[4])));
		listLargeGrassObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, Transform3D.class, float.class, float.class, float.class}, (Object[] arr) -> (GameObject) new LargeGrassObject((String) arr[0], (SwayMesh) arr[1], (Transform3D) arr[2], (float) arr[3], (float) arr[4], (float) arr[5])));
		listLargeGrassObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, Transform3D.class, Vector3i.class, float.class, float.class, float.class}, (Object[] arr) -> (GameObject) new LargeGrassObject((String) arr[0], (SwayMesh) arr[1], (Transform3D) arr[2], (Vector3i) arr[3], (float) arr[4], (float) arr[5], (float) arr[6])));
		listLargeGrassObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, Transform3D.class, Vector3i.class, short.class, float.class, float.class}, (Object[] arr) -> (GameObject) new LargeGrassObject((String) arr[0], (SwayMesh) arr[1], (Transform3D) arr[2], (Vector3i) arr[3], (short) arr[4], (float) arr[5], (float) arr[6])));
		listLargeGrassObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, Transform3D.class, Vector3i.class, short.class, float.class, float.class, float.class}, (Object[] arr) -> (GameObject) new LargeGrassObject((String) arr[0], (SwayMesh) arr[1], (Transform3D) arr[2], (Vector3i) arr[3], (short) arr[4], (float) arr[5], (float) arr[6], (float) arr[7])));
		GAME_OBJECT_CONSTRUCTORS.put(LargeGrassObject.class, listLargeGrassObject);

		/*                 SmallChampiFlowerObject                 */
		final List<InternalConstructorFunction<GameObject>> listSmallChampiFlowerObject = new ArrayList<>();
		listSmallChampiFlowerObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, Transform3D.class}, (Object[] arr) -> (GameObject) new SmallChampiFlowerObject((String) arr[0], (SwayMesh) arr[1], (Transform3D) arr[2])));
		listSmallChampiFlowerObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, Transform3D.class, short.class}, (Object[] arr) -> (GameObject) new SmallChampiFlowerObject((String) arr[0], (SwayMesh) arr[1], (Transform3D) arr[2], (short) arr[3])));
		listSmallChampiFlowerObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, float.class, float.class, float.class}, (Object[] arr) -> (GameObject) new SmallChampiFlowerObject((String) arr[0], (SwayMesh) arr[1], (float) arr[2], (float) arr[3], (float) arr[4])));
		listSmallChampiFlowerObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, Transform3D.class, float.class, float.class, float.class}, (Object[] arr) -> (GameObject) new SmallChampiFlowerObject((String) arr[0], (SwayMesh) arr[1], (Transform3D) arr[2], (float) arr[3], (float) arr[4], (float) arr[5])));
		listSmallChampiFlowerObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, Transform3D.class, Vector3i.class, float.class, float.class, float.class}, (Object[] arr) -> (GameObject) new SmallChampiFlowerObject((String) arr[0], (SwayMesh) arr[1], (Transform3D) arr[2], (Vector3i) arr[3], (float) arr[4], (float) arr[5], (float) arr[6])));
		listSmallChampiFlowerObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, Transform3D.class, Vector3i.class, short.class, float.class, float.class}, (Object[] arr) -> (GameObject) new SmallChampiFlowerObject((String) arr[0], (SwayMesh) arr[1], (Transform3D) arr[2], (Vector3i) arr[3], (short) arr[4], (float) arr[5], (float) arr[6])));
		listSmallChampiFlowerObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, Transform3D.class, Vector3i.class, short.class, float.class, float.class, float.class}, (Object[] arr) -> (GameObject) new SmallChampiFlowerObject((String) arr[0], (SwayMesh) arr[1], (Transform3D) arr[2], (Vector3i) arr[3], (short) arr[4], (float) arr[5], (float) arr[6], (float) arr[7])));
		GAME_OBJECT_CONSTRUCTORS.put(SmallChampiFlowerObject.class, listSmallChampiFlowerObject);

		/*                 SmallRoundFlowerObject                 */
		final List<InternalConstructorFunction<GameObject>> listSmallRoundFlowerObject = new ArrayList<>();
		listSmallRoundFlowerObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, Transform3D.class}, (Object[] arr) -> (GameObject) new SmallRoundFlowerObject((String) arr[0], (SwayMesh) arr[1], (Transform3D) arr[2])));
		listSmallRoundFlowerObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, Transform3D.class, short.class}, (Object[] arr) -> (GameObject) new SmallRoundFlowerObject((String) arr[0], (SwayMesh) arr[1], (Transform3D) arr[2], (short) arr[3])));
		listSmallRoundFlowerObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, float.class, float.class, float.class}, (Object[] arr) -> (GameObject) new SmallRoundFlowerObject((String) arr[0], (SwayMesh) arr[1], (float) arr[2], (float) arr[3], (float) arr[4])));
		listSmallRoundFlowerObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, Transform3D.class, float.class, float.class, float.class}, (Object[] arr) -> (GameObject) new SmallRoundFlowerObject((String) arr[0], (SwayMesh) arr[1], (Transform3D) arr[2], (float) arr[3], (float) arr[4], (float) arr[5])));
		listSmallRoundFlowerObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, Transform3D.class, Vector3i.class, float.class, float.class, float.class}, (Object[] arr) -> (GameObject) new SmallRoundFlowerObject((String) arr[0], (SwayMesh) arr[1], (Transform3D) arr[2], (Vector3i) arr[3], (float) arr[4], (float) arr[5], (float) arr[6])));
		listSmallRoundFlowerObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, Transform3D.class, Vector3i.class, short.class, float.class, float.class}, (Object[] arr) -> (GameObject) new SmallRoundFlowerObject((String) arr[0], (SwayMesh) arr[1], (Transform3D) arr[2], (Vector3i) arr[3], (short) arr[4], (float) arr[5], (float) arr[6])));
		listSmallRoundFlowerObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, Transform3D.class, Vector3i.class, short.class, float.class, float.class, float.class}, (Object[] arr) -> (GameObject) new SmallRoundFlowerObject((String) arr[0], (SwayMesh) arr[1], (Transform3D) arr[2], (Vector3i) arr[3], (short) arr[4], (float) arr[5], (float) arr[6], (float) arr[7])));
		GAME_OBJECT_CONSTRUCTORS.put(SmallRoundFlowerObject.class, listSmallRoundFlowerObject);

		/*                 MediumGrassObject                 */
		final List<InternalConstructorFunction<GameObject>> listMediumGrassObject = new ArrayList<>();
		listMediumGrassObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, Transform3D.class}, (Object[] arr) -> (GameObject) new MediumGrassObject((String) arr[0], (SwayMesh) arr[1], (Transform3D) arr[2])));
		listMediumGrassObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, Transform3D.class, short.class}, (Object[] arr) -> (GameObject) new MediumGrassObject((String) arr[0], (SwayMesh) arr[1], (Transform3D) arr[2], (short) arr[3])));
		listMediumGrassObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, float.class, float.class, float.class}, (Object[] arr) -> (GameObject) new MediumGrassObject((String) arr[0], (SwayMesh) arr[1], (float) arr[2], (float) arr[3], (float) arr[4])));
		listMediumGrassObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, Transform3D.class, float.class, float.class, float.class}, (Object[] arr) -> (GameObject) new MediumGrassObject((String) arr[0], (SwayMesh) arr[1], (Transform3D) arr[2], (float) arr[3], (float) arr[4], (float) arr[5])));
		listMediumGrassObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, Transform3D.class, Vector3i.class, float.class, float.class, float.class}, (Object[] arr) -> (GameObject) new MediumGrassObject((String) arr[0], (SwayMesh) arr[1], (Transform3D) arr[2], (Vector3i) arr[3], (float) arr[4], (float) arr[5], (float) arr[6])));
		listMediumGrassObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, Transform3D.class, Vector3i.class, short.class, float.class, float.class}, (Object[] arr) -> (GameObject) new MediumGrassObject((String) arr[0], (SwayMesh) arr[1], (Transform3D) arr[2], (Vector3i) arr[3], (short) arr[4], (float) arr[5], (float) arr[6])));
		listMediumGrassObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, Transform3D.class, Vector3i.class, short.class, float.class, float.class, float.class}, (Object[] arr) -> (GameObject) new MediumGrassObject((String) arr[0], (SwayMesh) arr[1], (Transform3D) arr[2], (Vector3i) arr[3], (short) arr[4], (float) arr[5], (float) arr[6], (float) arr[7])));
		GAME_OBJECT_CONSTRUCTORS.put(MediumGrassObject.class, listMediumGrassObject);

		/*                 MediumChampiFlowerObject                 */
		final List<InternalConstructorFunction<GameObject>> listMediumChampiFlowerObject = new ArrayList<>();
		listMediumChampiFlowerObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, Transform3D.class}, (Object[] arr) -> (GameObject) new MediumChampiFlowerObject((String) arr[0], (SwayMesh) arr[1], (Transform3D) arr[2])));
		listMediumChampiFlowerObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, Transform3D.class, short.class}, (Object[] arr) -> (GameObject) new MediumChampiFlowerObject((String) arr[0], (SwayMesh) arr[1], (Transform3D) arr[2], (short) arr[3])));
		listMediumChampiFlowerObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, float.class, float.class, float.class}, (Object[] arr) -> (GameObject) new MediumChampiFlowerObject((String) arr[0], (SwayMesh) arr[1], (float) arr[2], (float) arr[3], (float) arr[4])));
		listMediumChampiFlowerObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, Transform3D.class, float.class, float.class, float.class}, (Object[] arr) -> (GameObject) new MediumChampiFlowerObject((String) arr[0], (SwayMesh) arr[1], (Transform3D) arr[2], (float) arr[3], (float) arr[4], (float) arr[5])));
		listMediumChampiFlowerObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, Transform3D.class, Vector3i.class, float.class, float.class, float.class}, (Object[] arr) -> (GameObject) new MediumChampiFlowerObject((String) arr[0], (SwayMesh) arr[1], (Transform3D) arr[2], (Vector3i) arr[3], (float) arr[4], (float) arr[5], (float) arr[6])));
		listMediumChampiFlowerObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, Transform3D.class, Vector3i.class, short.class, float.class, float.class}, (Object[] arr) -> (GameObject) new MediumChampiFlowerObject((String) arr[0], (SwayMesh) arr[1], (Transform3D) arr[2], (Vector3i) arr[3], (short) arr[4], (float) arr[5], (float) arr[6])));
		listMediumChampiFlowerObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, Transform3D.class, Vector3i.class, short.class, float.class, float.class, float.class}, (Object[] arr) -> (GameObject) new MediumChampiFlowerObject((String) arr[0], (SwayMesh) arr[1], (Transform3D) arr[2], (Vector3i) arr[3], (short) arr[4], (float) arr[5], (float) arr[6], (float) arr[7])));
		GAME_OBJECT_CONSTRUCTORS.put(MediumChampiFlowerObject.class, listMediumChampiFlowerObject);

		/*                 MediumRoundFlowerObject                 */
		final List<InternalConstructorFunction<GameObject>> listMediumRoundFlowerObject = new ArrayList<>();
		listMediumRoundFlowerObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, Transform3D.class}, (Object[] arr) -> (GameObject) new MediumRoundFlowerObject((String) arr[0], (SwayMesh) arr[1], (Transform3D) arr[2])));
		listMediumRoundFlowerObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, Transform3D.class, short.class}, (Object[] arr) -> (GameObject) new MediumRoundFlowerObject((String) arr[0], (SwayMesh) arr[1], (Transform3D) arr[2], (short) arr[3])));
		listMediumRoundFlowerObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, float.class, float.class, float.class}, (Object[] arr) -> (GameObject) new MediumRoundFlowerObject((String) arr[0], (SwayMesh) arr[1], (float) arr[2], (float) arr[3], (float) arr[4])));
		listMediumRoundFlowerObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, Transform3D.class, float.class, float.class, float.class}, (Object[] arr) -> (GameObject) new MediumRoundFlowerObject((String) arr[0], (SwayMesh) arr[1], (Transform3D) arr[2], (float) arr[3], (float) arr[4], (float) arr[5])));
		listMediumRoundFlowerObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, Transform3D.class, Vector3i.class, float.class, float.class, float.class}, (Object[] arr) -> (GameObject) new MediumRoundFlowerObject((String) arr[0], (SwayMesh) arr[1], (Transform3D) arr[2], (Vector3i) arr[3], (float) arr[4], (float) arr[5], (float) arr[6])));
		listMediumRoundFlowerObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, Transform3D.class, Vector3i.class, short.class, float.class, float.class}, (Object[] arr) -> (GameObject) new MediumRoundFlowerObject((String) arr[0], (SwayMesh) arr[1], (Transform3D) arr[2], (Vector3i) arr[3], (short) arr[4], (float) arr[5], (float) arr[6])));
		listMediumRoundFlowerObject.add(new InternalConstructorFunction<>(new Class[] {String.class, SwayMesh.class, Transform3D.class, Vector3i.class, short.class, float.class, float.class, float.class}, (Object[] arr) -> (GameObject) new MediumRoundFlowerObject((String) arr[0], (SwayMesh) arr[1], (Transform3D) arr[2], (Vector3i) arr[3], (short) arr[4], (float) arr[5], (float) arr[6], (float) arr[7])));
		GAME_OBJECT_CONSTRUCTORS.put(MediumRoundFlowerObject.class, listMediumRoundFlowerObject);

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
