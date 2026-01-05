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
import lu.kbra.plant_game.engine.entity.go.MeshGameObject;
import lu.kbra.plant_game.engine.entity.go.impl.InstanceGameObject;
import lu.kbra.plant_game.engine.entity.go.impl.InstanceSwayGameObject;
import lu.kbra.plant_game.engine.entity.go.impl.SwayGameObject;
import lu.kbra.plant_game.engine.entity.go.obj.energy.SolarPanelObject;
import lu.kbra.plant_game.engine.entity.go.obj.energy.WaterWheelObject;
import lu.kbra.plant_game.engine.entity.go.obj.flower.champi.LargeChampiFlowerObject;
import lu.kbra.plant_game.engine.entity.go.obj.flower.champi.MediumChampiFlowerObject;
import lu.kbra.plant_game.engine.entity.go.obj.flower.champi.SmallChampiFlowerObject;
import lu.kbra.plant_game.engine.entity.go.obj.flower.round.LargeRoundFlowerObject;
import lu.kbra.plant_game.engine.entity.go.obj.flower.round.MediumRoundFlowerObject;
import lu.kbra.plant_game.engine.entity.go.obj.flower.round.SmallRoundFlowerObject;
import lu.kbra.plant_game.engine.entity.go.obj.grass.LargeGrassObject;
import lu.kbra.plant_game.engine.entity.go.obj.grass.MediumGrassObject;
import lu.kbra.plant_game.engine.entity.go.obj.grass.SmallGrassObject;
import lu.kbra.plant_game.engine.entity.go.obj.terrain.TerrainEdgeObject;
import lu.kbra.plant_game.engine.entity.go.obj.terrain.TerrainHighlightObject;
import lu.kbra.plant_game.engine.entity.go.obj.terrain.TerrainObject;
import lu.kbra.plant_game.engine.entity.go.obj.water.WaterSprinklerObject3x3;
import lu.kbra.plant_game.engine.entity.go.obj.water.WaterSprinklerObject5x5;
import lu.kbra.plant_game.engine.entity.go.obj.water.WaterSprinklerObject7x7;
import lu.kbra.plant_game.engine.entity.go.obj.water.WaterTowerObject;
import lu.kbra.plant_game.engine.entity.go.obj_inst.champi.InstanceLargeChampiFlowerObject;
import lu.kbra.plant_game.engine.entity.go.obj_inst.champi.InstanceMediumChampiFlowerObject;
import lu.kbra.plant_game.engine.entity.go.obj_inst.champi.InstanceSmallChampiFlowerObject;
import lu.kbra.plant_game.engine.entity.go.obj_inst.grass.InstanceLargeGrassObject;
import lu.kbra.plant_game.engine.entity.go.obj_inst.grass.InstanceMediumGrassObject;
import lu.kbra.plant_game.engine.entity.go.obj_inst.grass.InstanceSmallGrassObject;
import lu.kbra.plant_game.engine.entity.go.obj_inst.particles.GravityParticleGameObject;
import lu.kbra.plant_game.engine.entity.go.obj_inst.particles.ParticleGameObject;
import lu.kbra.plant_game.engine.entity.go.obj_inst.round.InstanceLargeRoundFlowerObject;
import lu.kbra.plant_game.engine.entity.go.obj_inst.round.InstanceMediumRoundFlowerObject;
import lu.kbra.plant_game.engine.entity.go.obj_inst.round.InstanceSmallRoundFlowerObject;
import lu.kbra.plant_game.engine.util.BuildingOption;
import lu.kbra.plant_game.engine.util.InternalConstructorFunction;
import lu.kbra.plant_game.engine.util.exceptions.GameObjectConstructorNotFound;
import lu.kbra.plant_game.engine.util.exceptions.GameObjectNotFound;
import lu.kbra.standalone.gameengine.geom.Mesh;
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

		/*                 MeshGameObject                 */
		final List<InternalConstructorFunction<GameObject>> listMeshGameObject = new ArrayList<>();
		GAME_OBJECT_CONSTRUCTORS.put(MeshGameObject.class, listMeshGameObject);

		/*                 InstanceGameObject                 */
		final List<InternalConstructorFunction<GameObject>> listInstanceGameObject = new ArrayList<>();
		GAME_OBJECT_CONSTRUCTORS.put(InstanceGameObject.class, listInstanceGameObject);

		/*                 TerrainObject                 */
		final List<InternalConstructorFunction<GameObject>> listTerrainObject = new ArrayList<>();
		GAME_OBJECT_CONSTRUCTORS.put(TerrainObject.class, listTerrainObject);

		/*                 TerrainEdgeObject                 */
		final List<InternalConstructorFunction<GameObject>> listTerrainEdgeObject = new ArrayList<>();
		GAME_OBJECT_CONSTRUCTORS.put(TerrainEdgeObject.class, listTerrainEdgeObject);

		/*                 SwayGameObject                 */
		final List<InternalConstructorFunction<GameObject>> listSwayGameObject = new ArrayList<>();
		GAME_OBJECT_CONSTRUCTORS.put(SwayGameObject.class, listSwayGameObject);

		/*                 AnimatedMeshGameObject                 */
		final List<InternalConstructorFunction<GameObject>> listAnimatedMeshGameObject = new ArrayList<>();
		GAME_OBJECT_CONSTRUCTORS.put(AnimatedMeshGameObject.class, listAnimatedMeshGameObject);

		/*                 TerrainHighlightObject                 */
		final List<InternalConstructorFunction<GameObject>> listTerrainHighlightObject = new ArrayList<>();
		GAME_OBJECT_CONSTRUCTORS.put(TerrainHighlightObject.class, listTerrainHighlightObject);

		/*                 InstanceSwayGameObject                 */
		final List<InternalConstructorFunction<GameObject>> listInstanceSwayGameObject = new ArrayList<>();
		GAME_OBJECT_CONSTRUCTORS.put(InstanceSwayGameObject.class, listInstanceSwayGameObject);

		/*                 ParticleGameObject                 */
		final List<InternalConstructorFunction<GameObject>> listParticleGameObject = new ArrayList<>();
		GAME_OBJECT_CONSTRUCTORS.put(ParticleGameObject.class, listParticleGameObject);

		/*                 WaterTowerObject                 */
		final List<InternalConstructorFunction<GameObject>> listWaterTowerObject = new ArrayList<>();
		listWaterTowerObject.add(new InternalConstructorFunction(new Class<?>[] {Mesh.class}, new BuildingOption[] {new BuildingOption<>("mesh:classpath:/models/water-tower-medium.obj", TextureFilter.NEAREST, TextureWrap.REPEAT, 0)}, (arr) -> (GameObject) new WaterTowerObject((String) arr[0], (Mesh) arr[1])));
		GAME_OBJECT_CONSTRUCTORS.put(WaterTowerObject.class, listWaterTowerObject);

		/*                 SolarPanelObject                 */
		final List<InternalConstructorFunction<GameObject>> listSolarPanelObject = new ArrayList<>();
		GAME_OBJECT_CONSTRUCTORS.put(SolarPanelObject.class, listSolarPanelObject);

		/*                 SmallGrassObject                 */
		final List<InternalConstructorFunction<GameObject>> listSmallGrassObject = new ArrayList<>();
		GAME_OBJECT_CONSTRUCTORS.put(SmallGrassObject.class, listSmallGrassObject);

		/*                 LargeRoundFlowerObject                 */
		final List<InternalConstructorFunction<GameObject>> listLargeRoundFlowerObject = new ArrayList<>();
		GAME_OBJECT_CONSTRUCTORS.put(LargeRoundFlowerObject.class, listLargeRoundFlowerObject);

		/*                 LargeChampiFlowerObject                 */
		final List<InternalConstructorFunction<GameObject>> listLargeChampiFlowerObject = new ArrayList<>();
		GAME_OBJECT_CONSTRUCTORS.put(LargeChampiFlowerObject.class, listLargeChampiFlowerObject);

		/*                 LargeGrassObject                 */
		final List<InternalConstructorFunction<GameObject>> listLargeGrassObject = new ArrayList<>();
		GAME_OBJECT_CONSTRUCTORS.put(LargeGrassObject.class, listLargeGrassObject);

		/*                 SmallChampiFlowerObject                 */
		final List<InternalConstructorFunction<GameObject>> listSmallChampiFlowerObject = new ArrayList<>();
		GAME_OBJECT_CONSTRUCTORS.put(SmallChampiFlowerObject.class, listSmallChampiFlowerObject);

		/*                 SmallRoundFlowerObject                 */
		final List<InternalConstructorFunction<GameObject>> listSmallRoundFlowerObject = new ArrayList<>();
		GAME_OBJECT_CONSTRUCTORS.put(SmallRoundFlowerObject.class, listSmallRoundFlowerObject);

		/*                 MediumGrassObject                 */
		final List<InternalConstructorFunction<GameObject>> listMediumGrassObject = new ArrayList<>();
		GAME_OBJECT_CONSTRUCTORS.put(MediumGrassObject.class, listMediumGrassObject);

		/*                 MediumChampiFlowerObject                 */
		final List<InternalConstructorFunction<GameObject>> listMediumChampiFlowerObject = new ArrayList<>();
		GAME_OBJECT_CONSTRUCTORS.put(MediumChampiFlowerObject.class, listMediumChampiFlowerObject);

		/*                 MediumRoundFlowerObject                 */
		final List<InternalConstructorFunction<GameObject>> listMediumRoundFlowerObject = new ArrayList<>();
		GAME_OBJECT_CONSTRUCTORS.put(MediumRoundFlowerObject.class, listMediumRoundFlowerObject);

		/*                 InstanceLargeRoundFlowerObject                 */
		final List<InternalConstructorFunction<GameObject>> listInstanceLargeRoundFlowerObject = new ArrayList<>();
		GAME_OBJECT_CONSTRUCTORS.put(InstanceLargeRoundFlowerObject.class, listInstanceLargeRoundFlowerObject);

		/*                 InstanceSmallGrassObject                 */
		final List<InternalConstructorFunction<GameObject>> listInstanceSmallGrassObject = new ArrayList<>();
		GAME_OBJECT_CONSTRUCTORS.put(InstanceSmallGrassObject.class, listInstanceSmallGrassObject);

		/*                 InstanceSmallRoundFlowerObject                 */
		final List<InternalConstructorFunction<GameObject>> listInstanceSmallRoundFlowerObject = new ArrayList<>();
		GAME_OBJECT_CONSTRUCTORS.put(InstanceSmallRoundFlowerObject.class, listInstanceSmallRoundFlowerObject);

		/*                 InstanceLargeGrassObject                 */
		final List<InternalConstructorFunction<GameObject>> listInstanceLargeGrassObject = new ArrayList<>();
		GAME_OBJECT_CONSTRUCTORS.put(InstanceLargeGrassObject.class, listInstanceLargeGrassObject);

		/*                 InstanceLargeChampiFlowerObject                 */
		final List<InternalConstructorFunction<GameObject>> listInstanceLargeChampiFlowerObject = new ArrayList<>();
		GAME_OBJECT_CONSTRUCTORS.put(InstanceLargeChampiFlowerObject.class, listInstanceLargeChampiFlowerObject);

		/*                 InstanceMediumRoundFlowerObject                 */
		final List<InternalConstructorFunction<GameObject>> listInstanceMediumRoundFlowerObject = new ArrayList<>();
		GAME_OBJECT_CONSTRUCTORS.put(InstanceMediumRoundFlowerObject.class, listInstanceMediumRoundFlowerObject);

		/*                 InstanceSmallChampiFlowerObject                 */
		final List<InternalConstructorFunction<GameObject>> listInstanceSmallChampiFlowerObject = new ArrayList<>();
		GAME_OBJECT_CONSTRUCTORS.put(InstanceSmallChampiFlowerObject.class, listInstanceSmallChampiFlowerObject);

		/*                 InstanceMediumGrassObject                 */
		final List<InternalConstructorFunction<GameObject>> listInstanceMediumGrassObject = new ArrayList<>();
		GAME_OBJECT_CONSTRUCTORS.put(InstanceMediumGrassObject.class, listInstanceMediumGrassObject);

		/*                 InstanceMediumChampiFlowerObject                 */
		final List<InternalConstructorFunction<GameObject>> listInstanceMediumChampiFlowerObject = new ArrayList<>();
		GAME_OBJECT_CONSTRUCTORS.put(InstanceMediumChampiFlowerObject.class, listInstanceMediumChampiFlowerObject);

		/*                 GravityParticleGameObject                 */
		final List<InternalConstructorFunction<GameObject>> listGravityParticleGameObject = new ArrayList<>();
		GAME_OBJECT_CONSTRUCTORS.put(GravityParticleGameObject.class, listGravityParticleGameObject);

		/*                 WaterSprinklerObject7x7                 */
		final List<InternalConstructorFunction<GameObject>> listWaterSprinklerObject7x7 = new ArrayList<>();
		GAME_OBJECT_CONSTRUCTORS.put(WaterSprinklerObject7x7.class, listWaterSprinklerObject7x7);

		/*                 WaterSprinklerObject5x5                 */
		final List<InternalConstructorFunction<GameObject>> listWaterSprinklerObject5x5 = new ArrayList<>();
		GAME_OBJECT_CONSTRUCTORS.put(WaterSprinklerObject5x5.class, listWaterSprinklerObject5x5);

		/*                 WaterSprinklerObject3x3                 */
		final List<InternalConstructorFunction<GameObject>> listWaterSprinklerObject3x3 = new ArrayList<>();
		GAME_OBJECT_CONSTRUCTORS.put(WaterSprinklerObject3x3.class, listWaterSprinklerObject3x3);

		/*                 WaterWheelObject                 */
		final List<InternalConstructorFunction<GameObject>> listWaterWheelObject = new ArrayList<>();
		GAME_OBJECT_CONSTRUCTORS.put(WaterWheelObject.class, listWaterWheelObject);

		/*                 GameObject                 */
		final List<InternalConstructorFunction<GameObject>> listGameObject = new ArrayList<>();
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
