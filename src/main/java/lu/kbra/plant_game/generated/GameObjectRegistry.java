// @formatter:off
package lu.kbra.plant_game.generated;

import java.lang.Class;
import java.lang.Integer;
import java.lang.Object;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lu.kbra.plant_game.engine.entity.go.AnimatedMeshGameObject;
import lu.kbra.plant_game.engine.entity.go.GameObject;
import lu.kbra.plant_game.engine.entity.go.InstanceGameObject;
import lu.kbra.plant_game.engine.entity.go.InstanceSwayGameObject;
import lu.kbra.plant_game.engine.entity.go.MeshGameObject;
import lu.kbra.plant_game.engine.entity.go.SwayGameObject;
import lu.kbra.plant_game.engine.entity.go.mesh.terrain.TerrainMesh;
import lu.kbra.plant_game.engine.entity.go.obj.terrain.TerrainEdgeObject;
import lu.kbra.plant_game.engine.entity.go.obj.terrain.TerrainHighlightObject;
import lu.kbra.plant_game.engine.entity.go.obj.terrain.TerrainObject;
import lu.kbra.plant_game.engine.entity.go.obj_inst.particles.GravityParticleGameObject;
import lu.kbra.plant_game.engine.entity.go.obj_inst.particles.ParticleGameObject;
import lu.kbra.plant_game.engine.mesh.AnimatedMesh;
import lu.kbra.plant_game.engine.util.InternalConstructorFunction;
import lu.kbra.plant_game.engine.util.exceptions.GameObjectConstructorNotFound;
import lu.kbra.plant_game.engine.util.exceptions.GameObjectNotFound;
import lu.kbra.plant_game.vanilla.entity.go.obj.energy.SolarPanelObject;
import lu.kbra.plant_game.vanilla.entity.go.obj.energy.WaterWheelObject;
import lu.kbra.plant_game.vanilla.entity.go.obj.flower.champi.LargeChampiFlowerObject;
import lu.kbra.plant_game.vanilla.entity.go.obj.flower.champi.MediumChampiFlowerObject;
import lu.kbra.plant_game.vanilla.entity.go.obj.flower.champi.SmallChampiFlowerObject;
import lu.kbra.plant_game.vanilla.entity.go.obj.flower.round.LargeRoundFlowerObject;
import lu.kbra.plant_game.vanilla.entity.go.obj.flower.round.MediumRoundFlowerObject;
import lu.kbra.plant_game.vanilla.entity.go.obj.flower.round.SmallRoundFlowerObject;
import lu.kbra.plant_game.vanilla.entity.go.obj.grass.LargeGrassObject;
import lu.kbra.plant_game.vanilla.entity.go.obj.grass.MediumGrassObject;
import lu.kbra.plant_game.vanilla.entity.go.obj.grass.SmallGrassObject;
import lu.kbra.plant_game.vanilla.entity.go.obj.water.WaterSprinklerObject3x3;
import lu.kbra.plant_game.vanilla.entity.go.obj.water.WaterSprinklerObject5x5;
import lu.kbra.plant_game.vanilla.entity.go.obj.water.WaterSprinklerObject7x7;
import lu.kbra.plant_game.vanilla.entity.go.obj.water.WaterTowerObject;
import lu.kbra.plant_game.vanilla.entity.go.obj_inst.grass.InstanceLargeGrassObject;
import lu.kbra.plant_game.vanilla.entity.go.obj_inst.grass.InstanceMediumGrassObject;
import lu.kbra.plant_game.vanilla.entity.go.obj_inst.grass.InstanceSmallGrassObject;
import lu.kbra.plant_game.vanilla.entity.go.obj_inst.round.InstanceLargeRoundFlowerObject;
import lu.kbra.plant_game.vanilla.entity.go.obj_inst.round.InstanceMediumRoundFlowerObject;
import lu.kbra.plant_game.vanilla.entity.go.obj_inst.round.InstanceSmallRoundFlowerObject;
import lu.kbra.plant_game.vanilla.entity.go_inst.champi.InstanceLargeChampiFlowerObject;
import lu.kbra.plant_game.vanilla.entity.go_inst.champi.InstanceMediumChampiFlowerObject;
import lu.kbra.plant_game.vanilla.entity.go_inst.champi.InstanceSmallChampiFlowerObject;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.geom.instance.InstanceEmitter;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextureFilter;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextureWrap;

public class GameObjectRegistry {
	public static final Map<Class<? extends GameObject>, List<InternalConstructorFunction<GameObject>>> GAME_OBJECT_CONSTRUCTORS;

	public static final Map<Class<? extends GameObject>, String> DATA_PATH;

	public static final Map<Class<? extends GameObject>, Integer> BUFFER_SIZE;

	public static final Map<Class<? extends GameObject>, TextureFilter> TEXTURE_FILTER;

	public static final Map<Class<? extends GameObject>, TextureWrap> TEXTURE_WRAP;

	static {
		GAME_OBJECT_CONSTRUCTORS = new HashMap<>();
		DATA_PATH = new HashMap<>();
		BUFFER_SIZE = new HashMap<>();
		TEXTURE_FILTER = new HashMap<>();
		TEXTURE_WRAP = new HashMap<>();
		DEFAULT_PRICE = new HashMap<>();

		/*                 MeshGameObject                 */
		final List<InternalConstructorFunction<GameObject>> listMeshGameObject = new ArrayList<>();
		listMeshGameObject.add(new InternalConstructorFunction<>(new Class[] {String.class, Mesh.class}, (Object[] arr) -> (GameObject) new MeshGameObject((String) arr[0], (Mesh) arr[1])));
		GAME_OBJECT_CONSTRUCTORS.put(MeshGameObject.class, listMeshGameObject);

		/*                 InstanceGameObject                 */
		final List<InternalConstructorFunction<GameObject>> listInstanceGameObject = new ArrayList<>();
		listInstanceGameObject.add(new InternalConstructorFunction<>(new Class[] {String.class, InstanceEmitter.class}, (Object[] arr) -> (GameObject) new InstanceGameObject((String) arr[0], (InstanceEmitter) arr[1])));
		GAME_OBJECT_CONSTRUCTORS.put(InstanceGameObject.class, listInstanceGameObject);

		/*                 TerrainObject                 */
		final List<InternalConstructorFunction<GameObject>> listTerrainObject = new ArrayList<>();
		listTerrainObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TerrainMesh.class}, (Object[] arr) -> (GameObject) new TerrainObject((String) arr[0], (TerrainMesh) arr[1])));
		GAME_OBJECT_CONSTRUCTORS.put(TerrainObject.class, listTerrainObject);

		/*                 TerrainEdgeObject                 */
		final List<InternalConstructorFunction<GameObject>> listTerrainEdgeObject = new ArrayList<>();
		listTerrainEdgeObject.add(new InternalConstructorFunction<>(new Class[] {String.class, Mesh.class}, (Object[] arr) -> (GameObject) new TerrainEdgeObject((String) arr[0], (Mesh) arr[1])));
		GAME_OBJECT_CONSTRUCTORS.put(TerrainEdgeObject.class, listTerrainEdgeObject);

		/*                 SwayGameObject                 */
		final List<InternalConstructorFunction<GameObject>> listSwayGameObject = new ArrayList<>();
		listSwayGameObject.add(new InternalConstructorFunction<>(new Class[] {String.class, Mesh.class}, (Object[] arr) -> (GameObject) new SwayGameObject((String) arr[0], (Mesh) arr[1])));
		GAME_OBJECT_CONSTRUCTORS.put(SwayGameObject.class, listSwayGameObject);

		/*                 AnimatedMeshGameObject                 */
		final List<InternalConstructorFunction<GameObject>> listAnimatedMeshGameObject = new ArrayList<>();
		listAnimatedMeshGameObject.add(new InternalConstructorFunction<>(new Class[] {String.class, Mesh.class, AnimatedMesh.class}, (Object[] arr) -> (GameObject) new AnimatedMeshGameObject((String) arr[0], (Mesh) arr[1], (AnimatedMesh) arr[2])));
		GAME_OBJECT_CONSTRUCTORS.put(AnimatedMeshGameObject.class, listAnimatedMeshGameObject);

		/*                 TerrainHighlightObject                 */
		final List<InternalConstructorFunction<GameObject>> listTerrainHighlightObject = new ArrayList<>();
		listTerrainHighlightObject.add(new InternalConstructorFunction<>(new Class[] {String.class, Mesh.class}, (Object[] arr) -> (GameObject) new TerrainHighlightObject((String) arr[0], (Mesh) arr[1])));
		GAME_OBJECT_CONSTRUCTORS.put(TerrainHighlightObject.class, listTerrainHighlightObject);

		/*                 InstanceSwayGameObject                 */
		final List<InternalConstructorFunction<GameObject>> listInstanceSwayGameObject = new ArrayList<>();
		listInstanceSwayGameObject.add(new InternalConstructorFunction<>(new Class[] {String.class, InstanceEmitter.class}, (Object[] arr) -> (GameObject) new InstanceSwayGameObject((String) arr[0], (InstanceEmitter) arr[1])));
		GAME_OBJECT_CONSTRUCTORS.put(InstanceSwayGameObject.class, listInstanceSwayGameObject);

		/*                 ParticleGameObject                 */
		final List<InternalConstructorFunction<GameObject>> listParticleGameObject = new ArrayList<>();
		listParticleGameObject.add(new InternalConstructorFunction<>(new Class[] {String.class, InstanceEmitter.class}, (Object[] arr) -> (GameObject) new ParticleGameObject((String) arr[0], (InstanceEmitter) arr[1])));
		GAME_OBJECT_CONSTRUCTORS.put(ParticleGameObject.class, listParticleGameObject);
		DATA_PATH.put(ParticleGameObject.class, "classpath:/models/cube.json");

		/*                 SolarPanelObject                 */
		final List<InternalConstructorFunction<GameObject>> listSolarPanelObject = new ArrayList<>();
		listSolarPanelObject.add(new InternalConstructorFunction<>(new Class[] {String.class, Mesh.class}, (Object[] arr) -> (GameObject) new SolarPanelObject((String) arr[0], (Mesh) arr[1])));
		GAME_OBJECT_CONSTRUCTORS.put(SolarPanelObject.class, listSolarPanelObject);
		DATA_PATH.put(SolarPanelObject.class, "classpath:/models/solar-panel-medium.json");

		/*                 WaterTowerObject                 */
		final List<InternalConstructorFunction<GameObject>> listWaterTowerObject = new ArrayList<>();
		listWaterTowerObject.add(new InternalConstructorFunction<>(new Class[] {String.class, Mesh.class}, (Object[] arr) -> (GameObject) new WaterTowerObject((String) arr[0], (Mesh) arr[1])));
		GAME_OBJECT_CONSTRUCTORS.put(WaterTowerObject.class, listWaterTowerObject);
		DATA_PATH.put(WaterTowerObject.class, "classpath:/models/water-tower-medium.json");

		/*                 SmallRoundFlowerObject                 */
		final List<InternalConstructorFunction<GameObject>> listSmallRoundFlowerObject = new ArrayList<>();
		listSmallRoundFlowerObject.add(new InternalConstructorFunction<>(new Class[] {String.class, Mesh.class}, (Object[] arr) -> (GameObject) new SmallRoundFlowerObject((String) arr[0], (Mesh) arr[1])));
		GAME_OBJECT_CONSTRUCTORS.put(SmallRoundFlowerObject.class, listSmallRoundFlowerObject);
		DATA_PATH.put(SmallRoundFlowerObject.class, "classpath:/models/flower-round-small.json");

		/*                 LargeChampiFlowerObject                 */
		final List<InternalConstructorFunction<GameObject>> listLargeChampiFlowerObject = new ArrayList<>();
		listLargeChampiFlowerObject.add(new InternalConstructorFunction<>(new Class[] {String.class, Mesh.class}, (Object[] arr) -> (GameObject) new LargeChampiFlowerObject((String) arr[0], (Mesh) arr[1])));
		GAME_OBJECT_CONSTRUCTORS.put(LargeChampiFlowerObject.class, listLargeChampiFlowerObject);
		DATA_PATH.put(LargeChampiFlowerObject.class, "classpath:/models/champi-large.json");

		/*                 LargeRoundFlowerObject                 */
		final List<InternalConstructorFunction<GameObject>> listLargeRoundFlowerObject = new ArrayList<>();
		listLargeRoundFlowerObject.add(new InternalConstructorFunction<>(new Class[] {String.class, Mesh.class}, (Object[] arr) -> (GameObject) new LargeRoundFlowerObject((String) arr[0], (Mesh) arr[1])));
		GAME_OBJECT_CONSTRUCTORS.put(LargeRoundFlowerObject.class, listLargeRoundFlowerObject);
		DATA_PATH.put(LargeRoundFlowerObject.class, "classpath:/models/flower-round-large.json");

		/*                 MediumGrassObject                 */
		final List<InternalConstructorFunction<GameObject>> listMediumGrassObject = new ArrayList<>();
		listMediumGrassObject.add(new InternalConstructorFunction<>(new Class[] {String.class, Mesh.class}, (Object[] arr) -> (GameObject) new MediumGrassObject((String) arr[0], (Mesh) arr[1])));
		GAME_OBJECT_CONSTRUCTORS.put(MediumGrassObject.class, listMediumGrassObject);
		DATA_PATH.put(MediumGrassObject.class, "classpath:/models/grass-medium.json");

		/*                 MediumChampiFlowerObject                 */
		final List<InternalConstructorFunction<GameObject>> listMediumChampiFlowerObject = new ArrayList<>();
		listMediumChampiFlowerObject.add(new InternalConstructorFunction<>(new Class[] {String.class, Mesh.class}, (Object[] arr) -> (GameObject) new MediumChampiFlowerObject((String) arr[0], (Mesh) arr[1])));
		GAME_OBJECT_CONSTRUCTORS.put(MediumChampiFlowerObject.class, listMediumChampiFlowerObject);
		DATA_PATH.put(MediumChampiFlowerObject.class, "classpath:/models/champi-medium.json");

		/*                 LargeGrassObject                 */
		final List<InternalConstructorFunction<GameObject>> listLargeGrassObject = new ArrayList<>();
		listLargeGrassObject.add(new InternalConstructorFunction<>(new Class[] {String.class, Mesh.class}, (Object[] arr) -> (GameObject) new LargeGrassObject((String) arr[0], (Mesh) arr[1])));
		GAME_OBJECT_CONSTRUCTORS.put(LargeGrassObject.class, listLargeGrassObject);
		DATA_PATH.put(LargeGrassObject.class, "classpath:/models/grass-large.json");

		/*                 SmallGrassObject                 */
		final List<InternalConstructorFunction<GameObject>> listSmallGrassObject = new ArrayList<>();
		listSmallGrassObject.add(new InternalConstructorFunction<>(new Class[] {String.class, Mesh.class}, (Object[] arr) -> (GameObject) new SmallGrassObject((String) arr[0], (Mesh) arr[1])));
		GAME_OBJECT_CONSTRUCTORS.put(SmallGrassObject.class, listSmallGrassObject);
		DATA_PATH.put(SmallGrassObject.class, "classpath:/models/grass-small.json");

		/*                 SmallChampiFlowerObject                 */
		final List<InternalConstructorFunction<GameObject>> listSmallChampiFlowerObject = new ArrayList<>();
		listSmallChampiFlowerObject.add(new InternalConstructorFunction<>(new Class[] {String.class, Mesh.class}, (Object[] arr) -> (GameObject) new SmallChampiFlowerObject((String) arr[0], (Mesh) arr[1])));
		GAME_OBJECT_CONSTRUCTORS.put(SmallChampiFlowerObject.class, listSmallChampiFlowerObject);
		DATA_PATH.put(SmallChampiFlowerObject.class, "classpath:/models/champi-small.json");

		/*                 MediumRoundFlowerObject                 */
		final List<InternalConstructorFunction<GameObject>> listMediumRoundFlowerObject = new ArrayList<>();
		listMediumRoundFlowerObject.add(new InternalConstructorFunction<>(new Class[] {String.class, Mesh.class}, (Object[] arr) -> (GameObject) new MediumRoundFlowerObject((String) arr[0], (Mesh) arr[1])));
		GAME_OBJECT_CONSTRUCTORS.put(MediumRoundFlowerObject.class, listMediumRoundFlowerObject);
		DATA_PATH.put(MediumRoundFlowerObject.class, "classpath:/models/flower-round-medium.json");

		/*                 InstanceSmallRoundFlowerObject                 */
		final List<InternalConstructorFunction<GameObject>> listInstanceSmallRoundFlowerObject = new ArrayList<>();
		listInstanceSmallRoundFlowerObject.add(new InternalConstructorFunction<>(new Class[] {String.class, InstanceEmitter.class}, (Object[] arr) -> (GameObject) new InstanceSmallRoundFlowerObject((String) arr[0], (InstanceEmitter) arr[1])));
		GAME_OBJECT_CONSTRUCTORS.put(InstanceSmallRoundFlowerObject.class, listInstanceSmallRoundFlowerObject);
		DATA_PATH.put(InstanceSmallRoundFlowerObject.class, "classpath:/models/flower-round-small.json");

		/*                 InstanceLargeGrassObject                 */
		final List<InternalConstructorFunction<GameObject>> listInstanceLargeGrassObject = new ArrayList<>();
		listInstanceLargeGrassObject.add(new InternalConstructorFunction<>(new Class[] {String.class, InstanceEmitter.class}, (Object[] arr) -> (GameObject) new InstanceLargeGrassObject((String) arr[0], (InstanceEmitter) arr[1])));
		GAME_OBJECT_CONSTRUCTORS.put(InstanceLargeGrassObject.class, listInstanceLargeGrassObject);
		DATA_PATH.put(InstanceLargeGrassObject.class, "classpath:/models/grass-large.json");

		/*                 InstanceLargeRoundFlowerObject                 */
		final List<InternalConstructorFunction<GameObject>> listInstanceLargeRoundFlowerObject = new ArrayList<>();
		listInstanceLargeRoundFlowerObject.add(new InternalConstructorFunction<>(new Class[] {String.class, InstanceEmitter.class}, (Object[] arr) -> (GameObject) new InstanceLargeRoundFlowerObject((String) arr[0], (InstanceEmitter) arr[1])));
		GAME_OBJECT_CONSTRUCTORS.put(InstanceLargeRoundFlowerObject.class, listInstanceLargeRoundFlowerObject);
		DATA_PATH.put(InstanceLargeRoundFlowerObject.class, "classpath:/models/flower-round-large.json");

		/*                 InstanceMediumChampiFlowerObject                 */
		final List<InternalConstructorFunction<GameObject>> listInstanceMediumChampiFlowerObject = new ArrayList<>();
		listInstanceMediumChampiFlowerObject.add(new InternalConstructorFunction<>(new Class[] {String.class, InstanceEmitter.class}, (Object[] arr) -> (GameObject) new InstanceMediumChampiFlowerObject((String) arr[0], (InstanceEmitter) arr[1])));
		GAME_OBJECT_CONSTRUCTORS.put(InstanceMediumChampiFlowerObject.class, listInstanceMediumChampiFlowerObject);
		DATA_PATH.put(InstanceMediumChampiFlowerObject.class, "classpath:/models/champi-medium.json");

		/*                 InstanceSmallGrassObject                 */
		final List<InternalConstructorFunction<GameObject>> listInstanceSmallGrassObject = new ArrayList<>();
		listInstanceSmallGrassObject.add(new InternalConstructorFunction<>(new Class[] {String.class, InstanceEmitter.class}, (Object[] arr) -> (GameObject) new InstanceSmallGrassObject((String) arr[0], (InstanceEmitter) arr[1])));
		GAME_OBJECT_CONSTRUCTORS.put(InstanceSmallGrassObject.class, listInstanceSmallGrassObject);
		DATA_PATH.put(InstanceSmallGrassObject.class, "classpath:/models/grass-small.json");

		/*                 InstanceSmallChampiFlowerObject                 */
		final List<InternalConstructorFunction<GameObject>> listInstanceSmallChampiFlowerObject = new ArrayList<>();
		listInstanceSmallChampiFlowerObject.add(new InternalConstructorFunction<>(new Class[] {String.class, InstanceEmitter.class}, (Object[] arr) -> (GameObject) new InstanceSmallChampiFlowerObject((String) arr[0], (InstanceEmitter) arr[1])));
		GAME_OBJECT_CONSTRUCTORS.put(InstanceSmallChampiFlowerObject.class, listInstanceSmallChampiFlowerObject);
		DATA_PATH.put(InstanceSmallChampiFlowerObject.class, "classpath:/models/champi-small.json");

		/*                 InstanceMediumRoundFlowerObject                 */
		final List<InternalConstructorFunction<GameObject>> listInstanceMediumRoundFlowerObject = new ArrayList<>();
		listInstanceMediumRoundFlowerObject.add(new InternalConstructorFunction<>(new Class[] {String.class, InstanceEmitter.class}, (Object[] arr) -> (GameObject) new InstanceMediumRoundFlowerObject((String) arr[0], (InstanceEmitter) arr[1])));
		GAME_OBJECT_CONSTRUCTORS.put(InstanceMediumRoundFlowerObject.class, listInstanceMediumRoundFlowerObject);
		DATA_PATH.put(InstanceMediumRoundFlowerObject.class, "classpath:/models/flower-round-medium.json");

		/*                 InstanceLargeChampiFlowerObject                 */
		final List<InternalConstructorFunction<GameObject>> listInstanceLargeChampiFlowerObject = new ArrayList<>();
		listInstanceLargeChampiFlowerObject.add(new InternalConstructorFunction<>(new Class[] {String.class, InstanceEmitter.class}, (Object[] arr) -> (GameObject) new InstanceLargeChampiFlowerObject((String) arr[0], (InstanceEmitter) arr[1])));
		GAME_OBJECT_CONSTRUCTORS.put(InstanceLargeChampiFlowerObject.class, listInstanceLargeChampiFlowerObject);
		DATA_PATH.put(InstanceLargeChampiFlowerObject.class, "classpath:/models/champi-large.json");

		/*                 InstanceMediumGrassObject                 */
		final List<InternalConstructorFunction<GameObject>> listInstanceMediumGrassObject = new ArrayList<>();
		listInstanceMediumGrassObject.add(new InternalConstructorFunction<>(new Class[] {String.class, InstanceEmitter.class}, (Object[] arr) -> (GameObject) new InstanceMediumGrassObject((String) arr[0], (InstanceEmitter) arr[1])));
		GAME_OBJECT_CONSTRUCTORS.put(InstanceMediumGrassObject.class, listInstanceMediumGrassObject);
		DATA_PATH.put(InstanceMediumGrassObject.class, "classpath:/models/grass-medium.json");

		/*                 GravityParticleGameObject                 */
		final List<InternalConstructorFunction<GameObject>> listGravityParticleGameObject = new ArrayList<>();
		listGravityParticleGameObject.add(new InternalConstructorFunction<>(new Class[] {String.class, InstanceEmitter.class}, (Object[] arr) -> (GameObject) new GravityParticleGameObject((String) arr[0], (InstanceEmitter) arr[1])));
		GAME_OBJECT_CONSTRUCTORS.put(GravityParticleGameObject.class, listGravityParticleGameObject);
		DATA_PATH.put(GravityParticleGameObject.class, "classpath:/models/cube.json");

		/*                 WaterSprinklerObject7x7                 */
		final List<InternalConstructorFunction<GameObject>> listWaterSprinklerObject7x7 = new ArrayList<>();
		listWaterSprinklerObject7x7.add(new InternalConstructorFunction<>(new Class[] {String.class, Mesh.class, AnimatedMesh.class}, (Object[] arr) -> (GameObject) new WaterSprinklerObject7x7((String) arr[0], (Mesh) arr[1], (AnimatedMesh) arr[2])));
		GAME_OBJECT_CONSTRUCTORS.put(WaterSprinklerObject7x7.class, listWaterSprinklerObject7x7);
		DATA_PATH.put(WaterSprinklerObject7x7.class, "classpath:/models/water-sprinkler-7x7.json");

		/*                 WaterSprinklerObject5x5                 */
		final List<InternalConstructorFunction<GameObject>> listWaterSprinklerObject5x5 = new ArrayList<>();
		listWaterSprinklerObject5x5.add(new InternalConstructorFunction<>(new Class[] {String.class, Mesh.class, AnimatedMesh.class}, (Object[] arr) -> (GameObject) new WaterSprinklerObject5x5((String) arr[0], (Mesh) arr[1], (AnimatedMesh) arr[2])));
		GAME_OBJECT_CONSTRUCTORS.put(WaterSprinklerObject5x5.class, listWaterSprinklerObject5x5);
		DATA_PATH.put(WaterSprinklerObject5x5.class, "classpath:/models/water-sprinkler-5x5.json");

		/*                 WaterSprinklerObject3x3                 */
		final List<InternalConstructorFunction<GameObject>> listWaterSprinklerObject3x3 = new ArrayList<>();
		listWaterSprinklerObject3x3.add(new InternalConstructorFunction<>(new Class[] {String.class, Mesh.class, AnimatedMesh.class}, (Object[] arr) -> (GameObject) new WaterSprinklerObject3x3((String) arr[0], (Mesh) arr[1], (AnimatedMesh) arr[2])));
		GAME_OBJECT_CONSTRUCTORS.put(WaterSprinklerObject3x3.class, listWaterSprinklerObject3x3);
		DATA_PATH.put(WaterSprinklerObject3x3.class, "classpath:/models/water-sprinkler-3x3.json");

		/*                 WaterWheelObject                 */
		final List<InternalConstructorFunction<GameObject>> listWaterWheelObject = new ArrayList<>();
		listWaterWheelObject.add(new InternalConstructorFunction<>(new Class[] {String.class, Mesh.class, AnimatedMesh.class}, (Object[] arr) -> (GameObject) new WaterWheelObject((String) arr[0], (Mesh) arr[1], (AnimatedMesh) arr[2])));
		GAME_OBJECT_CONSTRUCTORS.put(WaterWheelObject.class, listWaterWheelObject);
		DATA_PATH.put(WaterWheelObject.class, "classpath:/models/water-wheel-small.json");

		/*                 GameObject                 */
		final List<InternalConstructorFunction<GameObject>> listGameObject = new ArrayList<>();
		listGameObject.add(new InternalConstructorFunction<>(new Class[] {String.class}, (Object[] arr) -> (GameObject) new GameObject((String) arr[0])));
		GAME_OBJECT_CONSTRUCTORS.put(GameObject.class, listGameObject);

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
