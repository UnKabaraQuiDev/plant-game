package lu.kbra.plant_game.engine.entity.go.obj.terrain;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.IntStream;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector4f;

import lu.kbra.pclib.PCUtils;
import lu.kbra.pclib.concurrency.ObjectTriggerLatch;
import lu.kbra.pclib.logger.GlobalLogger;
import lu.kbra.plant_game.base.entity.go.obj.water.NeedsRandomTick;
import lu.kbra.plant_game.base.entity.go.obj_inst.grass.InstanceLargeGrassObject;
import lu.kbra.plant_game.base.entity.go.obj_inst.grass.InstanceMediumGrassObject;
import lu.kbra.plant_game.base.entity.go.obj_inst.grass.InstanceSmallGrassObject;
import lu.kbra.plant_game.engine.entity.go.GameObject;
import lu.kbra.plant_game.engine.entity.go.MeshGameObject;
import lu.kbra.plant_game.engine.entity.go.VariationMeshGameObject;
import lu.kbra.plant_game.engine.entity.go.factory.GameObjectFactory;
import lu.kbra.plant_game.engine.entity.go.impl.PlaceableObject;
import lu.kbra.plant_game.engine.entity.go.mesh.terrain.TerrainMesh;
import lu.kbra.plant_game.engine.entity.impl.SynchronizedEntityContainer;
import lu.kbra.plant_game.engine.entity.impl.Transform3DPivotOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.AbsoluteTransform3DOwner;
import lu.kbra.plant_game.engine.scene.world.WorldLevelScene;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.plant_game.generated.ColorMaterial;
import lu.kbra.standalone.gameengine.objs.entity.ParentAwareComponent;
import lu.kbra.standalone.gameengine.scene.camera.Camera3D;
import lu.kbra.standalone.gameengine.utils.consts.Direction;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;
import lu.kbra.standalone.gameengine.utils.transform.Transform3DPivot;

public class TerrainGameObject extends VariationMeshGameObject
		implements SynchronizedEntityContainer<GameObject>, AbsoluteTransform3DOwner, Transform3DPivotOwner, NeedsRandomTick {

	/**
	 * Moisture system: Stored as byte (0–127 safe range to avoid overflow issues).
	 */
	private static final int MAX_MOISTURE = 100;

	/**
	 * Moisture thresholds for growth stages.
	 */
	private static final int MOISTURE_GROWN = 10;
	private static final int MOISTURE_SMALL = 20;
	private static final int MOISTURE_MEDIUM = 40;
	private static final int MOISTURE_LARGE = 60;
	private static final int MOISTURE_FLOWER = 80;

	/**
	 * Moisture decay per decayTick().
	 */
	public static final int MOISTURE_DECAY = 2;

	private static final int GREEN_BIT = 1 << 0;
	private static final int SMALL_GRASS_BIT = 1 << 1;
	private static final int MEDIUM_GRASS_BIT = 1 << 2;
	private static final int LARGE_GRASS_BIT = 1 << 3;

	private static final int FLOWER_BIT = 1 << 4;
	private static final int BUILT_BIT = 1 << 5;
	private static final int A_ = 1 << 6;
	private static final int B_ = 1 << 7;

	private static final int FLOWER_TYPE_BIT_1 = 1 << 8;
	private static final int FLOWER_TYPE_BIT_2 = 1 << 9;
	private static final int FLOWER_TYPE_BIT_3 = 1 << 10;
	private static final int FLOWER_TYPE_BIT_4 = 1 << 11;

	private static final int GRASS_ID_BIT_1 = 1 << 12;
	private static final int GRASS_ID_BIT_2 = 1 << 13;
	private static final int GRASS_ID_BIT_3 = 1 << 14;
	private static final int GRASS_ID_BIT_4 = 1 << 15;

	private static final int GRASS_ID_BIT_5 = 1 << 16;
	private static final int GRASS_ID_BIT_6 = 1 << 17;
	private static final int GRASS_ID_BIT_7 = 1 << 18;
	private static final int GRASS_ID_BIT_8 = 1 << 19;

	private static final int GRASS_BIT_MASK = SMALL_GRASS_BIT | MEDIUM_GRASS_BIT | LARGE_GRASS_BIT;
	private static final int FLOWER_TYPE_BIT_MASK = FLOWER_TYPE_BIT_1 | FLOWER_TYPE_BIT_2 | FLOWER_TYPE_BIT_3 | FLOWER_TYPE_BIT_4;
	private static final int FLOWER_TYPE_FIRST_BIT_INDEX = 8;
	private static final int GRASS_ID_FIRST_BIT_INDEX = 12;
	private static final int GRASS_ID_MAX = 0xFF;
	private static final int GRASS_ID_BIT_MASK = IntStream.range(GRASS_ID_FIRST_BIT_INDEX, GRASS_ID_FIRST_BIT_INDEX + 8)
			.map(c -> 1 << c)
			.reduce(0, (a, b) -> a | b);

	private static final int SMALL_GRASS_LEVEL = 0;
	private static final int MEDIUM_GRASS_LEVEL = 1;
	private static final int LARGE_GRASS_LEVEL = 2;

	public static final String WATER_FACTOR_PROPERTY = TerrainGameObject.class.getSimpleName() + ".water_factor";
	public static float WATER_GROW_SPEED = PCUtils.getFloat(WATER_FACTOR_PROPERTY, 1f);

	protected Object subEntitiesLock = new Object();
	protected List<GameObject> subEntities = Collections.synchronizedList(new ArrayList<>());

	protected WorldLevelScene parent;

	protected TerrainEdgeObject terrainEdgeObject;
	protected TerrainHighlightObject terrainHighlightObject;
	protected MeshGameObject terrainWaterObject;

	protected final byte[][] moisture;
	protected final int[][] population;

	protected WeakReference<InstanceSmallGrassObject> deadGrass;
	protected WeakReference<InstanceSmallGrassObject> smallGrass;
	protected WeakReference<InstanceMediumGrassObject> mediumGrass;
	protected WeakReference<InstanceLargeGrassObject> largeGrass;

	public TerrainGameObject(final String str, final TerrainMesh mesh) {
		super(str, mesh);
		this.setIsEntityMaterialId(false);
		this.setTransform(new Transform3DPivot());

		this.moisture = new byte[mesh.getWidth()][mesh.getLength()];
		this.population = new int[mesh.getWidth()][mesh.getLength()];
	}

	public ObjectTriggerLatch<TerrainGameObject> init() {
		synchronized (this) {
			if (this.getSmallGrassObject() != null && this.getMediumGrassObject() != null && this.getLargeGrassObject() != null) {
				return new ObjectTriggerLatch<>(0, this);
			}
		}

		final ObjectTriggerLatch<TerrainGameObject> latch = new ObjectTriggerLatch<>(4, this);

		synchronized (this) {
			System.err.println("created grass instances");
			GameObjectFactory.createInstances(InstanceSmallGrassObject.class, i -> new Transform3D(), OptionalInt.of(128), Optional.empty())
					.set(i -> i.setColorMaterial(ColorMaterial.GREEN))
					.set(i -> i.setTransform(new Transform3D(this.getTransform().getTranslation().negate(new Vector3f()))))
					.add(this)
					.postInit(i -> this.smallGrass = new WeakReference<>(i))
					.postInit(i -> System.err.println("grass instance set."))
					.latch(latch)
					.push();

			GameObjectFactory
					.createInstances(InstanceMediumGrassObject.class, i -> new Transform3D(), OptionalInt.of(128), Optional.empty())
					.set(i -> i.setColorMaterial(ColorMaterial.GREEN))
					.set(i -> i.setTransform(new Transform3D(this.getTransform().getTranslation().negate(new Vector3f()))))
					.add(this)
					.postInit(i -> this.mediumGrass = new WeakReference<>(i))
					.latch(latch)
					.push();

			GameObjectFactory.createInstances(InstanceLargeGrassObject.class, i -> new Transform3D(), OptionalInt.of(128), Optional.empty())
					.set(i -> i.setColorMaterial(ColorMaterial.GREEN))
					.set(i -> i.setTransform(new Transform3D(this.getTransform().getTranslation().negate(new Vector3f()))))
					.add(this)
					.postInit(i -> this.largeGrass = new WeakReference<>(i))
					.latch(latch)
					.push();

			GameObjectFactory.createInstances(InstanceSmallGrassObject.class, i -> new Transform3D(), OptionalInt.of(128), Optional.empty())
					.set(i -> i.setColorMaterial(ColorMaterial.LIGHT_BROWN))
					.set(i -> i.setTransform(new Transform3D(this.getTransform().getTranslation().negate(new Vector3f()))))
					.add(this)
					.postInit(i -> this.deadGrass = new WeakReference<>(i))
					.latch(latch)
					.push();
		}

		return latch;
	}

	@Override
	public void randomTick(final WindowInputHandler inputHandler, final WorldLevelScene worldLevelScene) {
		System.err.println("random tick terrain");
		this.decayTick();
	}

	public void decayTick() {
		final TerrainMesh mesh = this.getMesh();

		for (int x = 0; x < mesh.getWidth(); x++) {
			for (int y = 0; y < mesh.getLength(); y++) {

				int current = this.moisture[x][y] & 0xFF;
				if (current == 0) {
					continue;
				}

				current = Math.max(0, current - MOISTURE_DECAY * 4);
				this.moisture[x][y] = (byte) current;

				final Vector2i tile = new Vector2i(x, y);

				// 1. Flower disappears first
				if (current < MOISTURE_FLOWER) {
					this.removeFlower(tile);
				}

				// 2. Grass stages
				if (current >= MOISTURE_LARGE) {
					this.addGrass(tile, LARGE_GRASS_LEVEL);
					continue;
				}

				if (current >= MOISTURE_MEDIUM) {
					this.addGrass(tile, MEDIUM_GRASS_LEVEL);
					continue;
				}

				if (current >= MOISTURE_SMALL) {
					this.addGrass(tile, SMALL_GRASS_LEVEL);
					continue;
				}

				// 3. No more grass
				this.removeGrass(tile);

				// 4. Terrain color stages
				if (current >= MOISTURE_GROWN) {
//					mesh.setGrown(tile, true);
					continue;
				}

				mesh.setGrown(tile, false);
				this.population[x][y] &= ~GREEN_BIT;
			}
		}
	}

	public void place(final Optional<Vector2i> source, final Optional<Direction> sourceDir, final PlaceableObject obj) {
		if (source.isPresent() && sourceDir.isPresent()) {
			obj.getFootprint().forEachCell(source.get(), sourceDir.get(), i -> {
				this.population[i.x()][i.y()] &= ~BUILT_BIT;
				this.removeGrass(i);
				this.removeFlower(i);
			});
		}

		obj.forEachCell(i -> this.population[i.x()][i.y()] |= BUILT_BIT);
	}

	private void removeFlower(final Vector2i tile) {
		final TerrainMesh mesh = this.getMesh();
		if (!isInBounds(mesh, tile)) {
			return;
		}

		if (this.getFlower(tile).isPresent()) {
			this.population[tile.x()][tile.y()] &= ~FLOWER_BIT;
			this.population[tile.x()][tile.y()] &= ~FLOWER_TYPE_BIT_MASK;
		}
	}

	private void removeGrass(final Vector2i tile) {
		final TerrainMesh mesh = this.getMesh();
		if (!isInBounds(mesh, tile)) {
			return;
		}

		final OptionalInt grass = this.getGrassLevel(tile);
		if (grass.isEmpty()) {
			return;
		}

		final int instanceIndex = (this.population[tile.x()][tile.y()] & GRASS_ID_BIT_MASK) >> GRASS_ID_FIRST_BIT_INDEX;

		switch (grass.getAsInt()) {
		case SMALL_GRASS_LEVEL -> {
			final InstanceSmallGrassObject obj = this.getSmallGrassObject();
			if (obj != null) {
				obj.removeInstance(instanceIndex);
			}
		}
		case MEDIUM_GRASS_LEVEL -> {
			final InstanceMediumGrassObject obj = this.getMediumGrassObject();
			if (obj != null) {
				obj.removeInstance(instanceIndex);
			}
		}
		case LARGE_GRASS_LEVEL -> {
			final InstanceLargeGrassObject obj = this.getLargeGrassObject();
			if (obj != null) {
				obj.removeInstance(instanceIndex);
			}
		}
		default -> {
			return;
		}
		}

		this.population[tile.x()][tile.y()] &= ~GRASS_ID_BIT_MASK;
		this.population[tile.x()][tile.y()] &= ~GRASS_BIT_MASK;
	}

	public void addWater(final Vector2i tile, final float amount) {
		final TerrainMesh mesh = this.getMesh();

		if (tile.x() < 0 || tile.x() >= mesh.getWidth() || tile.y() < 0 || tile.y() >= mesh.getLength()) {
			System.err.println(tile + " oob");
			return;
		}

		int current = this.moisture[tile.x()][tile.y()] & 0xFF;
		current += (int) (amount * WATER_GROW_SPEED);
		current = Math.min(current, MAX_MOISTURE);

		this.moisture[tile.x()][tile.y()] = (byte) current;
		System.err.println(current);

		if (current >= MOISTURE_FLOWER) {
			this.addFlower(tile);
		} else if (current >= MOISTURE_LARGE) {
			this.addGrass(tile, LARGE_GRASS_LEVEL);
		} else if (current >= MOISTURE_MEDIUM) {
			this.addGrass(tile, MEDIUM_GRASS_LEVEL);
		} else if (current >= MOISTURE_SMALL) {
			this.addGrass(tile, SMALL_GRASS_LEVEL);
		} else if (current >= MOISTURE_GROWN) {
			mesh.setGrown(tile, true);
			this.population[tile.x()][tile.y()] |= GREEN_BIT;
		}
	}

	private void addFlower(final Vector2i tile) {
		final TerrainMesh mesh = this.getMesh();
		if (!isInBounds(mesh, tile)) {
			return;
		}

		if ((this.population[tile.x()][tile.y()] & BUILT_BIT) != 0) {
			return;
		}

		if (this.getFlower(tile).isPresent()) {
			GlobalLogger.info("Not adding any flower to: " + tile);
			return;
		}

		final boolean addFlower = Math.random() < 0.5;
		if (!addFlower) {
			return;
		}

		final int flowerIndex = 1;

		this.population[tile.x()][tile.y()] &= ~FLOWER_TYPE_BIT_MASK;
		this.population[tile.x()][tile.y()] |= FLOWER_BIT;
		this.population[tile.x()][tile.y()] |= (PCUtils.clamp(0, 15, flowerIndex) << FLOWER_TYPE_FIRST_BIT_INDEX) & FLOWER_TYPE_BIT_MASK;
	}

	private OptionalInt getFlower(final Vector2i tile) {
		if ((this.population[tile.x()][tile.y()] & FLOWER_BIT) == 0) {
			return OptionalInt.empty();
		}

		return OptionalInt.of((this.population[tile.x()][tile.y()] & FLOWER_TYPE_BIT_MASK) >> FLOWER_TYPE_FIRST_BIT_INDEX);
	}

	private OptionalInt getGrassLevel(final Vector2i tile) {
		return (this.population[tile.x()][tile.y()] & SMALL_GRASS_BIT) != 0 ? OptionalInt.of(SMALL_GRASS_LEVEL)
				: (this.population[tile.x()][tile.y()] & MEDIUM_GRASS_BIT) != 0 ? OptionalInt.of(MEDIUM_GRASS_LEVEL)
				: (this.population[tile.x()][tile.y()] & LARGE_GRASS_BIT) != 0 ? OptionalInt.of(LARGE_GRASS_LEVEL)
				: OptionalInt.empty();
	}

	private void addGrass(final Vector2i tile, final int level) {
		final TerrainMesh mesh = this.getMesh();
		if (!isInBounds(mesh, tile)) {
			return;
		}

		if (level < SMALL_GRASS_LEVEL || level > LARGE_GRASS_LEVEL) {
			throw new IllegalArgumentException(
					"Grass level: " + level + " not in range [" + SMALL_GRASS_LEVEL + ".." + LARGE_GRASS_LEVEL + "].");
		}

		if ((this.population[tile.x()][tile.y()] & BUILT_BIT) != 0) {
			GlobalLogger.info("Not adding grass to built tile: " + tile);
			return;
		}

		final OptionalInt grass = this.getGrassLevel(tile);
		if (grass.isPresent() && grass.getAsInt() >= level) {
			GlobalLogger.info("Not adding any grass to: " + tile + " " + grass.getAsInt());
			return;
		}

		if (grass.isPresent()) {
			this.removeGrass(tile);
		}

		final Vector3f position = this.getTilePosition(tile);
		final int instanceId;

		switch (level) {
		case SMALL_GRASS_LEVEL -> {
			final InstanceSmallGrassObject obj = this.getSmallGrassObject();
			if (obj == null) {
				GlobalLogger.warning("Small grass object is not initialized.");
				return;
			}
			instanceId = obj.addInstance(position);
		}
		case MEDIUM_GRASS_LEVEL -> {
			final InstanceMediumGrassObject obj = this.getMediumGrassObject();
			if (obj == null) {
				GlobalLogger.warning("Medium grass object is not initialized.");
				return;
			}
			instanceId = obj.addInstance(position);
		}
		case LARGE_GRASS_LEVEL -> {
			final InstanceLargeGrassObject obj = this.getLargeGrassObject();
			if (obj == null) {
				GlobalLogger.warning("Large grass object is not initialized.");
				return;
			}
			instanceId = obj.addInstance(position);
		}
		default -> throw new IllegalStateException("Unexpected grass level: " + level);
		}

		this.population[tile.x()][tile.y()] &= ~GRASS_BIT_MASK;
		this.population[tile.x()][tile.y()] |= grassLevelToBit(level);

		this.population[tile.x()][tile.y()] &= ~GRASS_ID_BIT_MASK;
		this.population[tile.x()][tile.y()] |= (PCUtils.clamp(0, GRASS_ID_MAX, instanceId) << GRASS_ID_FIRST_BIT_INDEX) & GRASS_ID_BIT_MASK;
	}

	private static boolean isInBounds(final TerrainMesh mesh, final Vector2i tile) {
		return tile.x() >= 0 && tile.x() < mesh.getWidth() && tile.y() >= 0 && tile.y() < mesh.getLength();
	}

	private InstanceSmallGrassObject getSmallGrassObject() {
		return this.smallGrass != null ? this.smallGrass.get() : null;
	}

	private InstanceMediumGrassObject getMediumGrassObject() {
		return this.mediumGrass != null ? this.mediumGrass.get() : null;
	}

	private InstanceLargeGrassObject getLargeGrassObject() {
		return this.largeGrass != null ? this.largeGrass.get() : null;
	}

	private static int grassLevelToBit(final int level) {
		return switch (level) {
		case SMALL_GRASS_LEVEL -> SMALL_GRASS_BIT;
		case MEDIUM_GRASS_LEVEL -> MEDIUM_GRASS_BIT;
		case LARGE_GRASS_LEVEL -> LARGE_GRASS_BIT;
		default -> throw new IllegalArgumentException("Unsupported grass level: " + level);
		};
	}

	public Vector2i pickTerrainCell(final Camera3D cam, final Vector2f mousePos, final int windowWidth, final int windowHeight) {
		final TerrainMesh tMesh = this.getMesh();
		final float cellSize = 1f;

		// 1. Convert mouse to NDC [-1,1]
		final float ndcX = (mousePos.x / windowWidth) * 2f - 1f;
		final float ndcY = 1f - (mousePos.y / windowHeight) * 2f;

		// 2. Unproject to world space
		final Matrix4f invProj = new Matrix4f(cam.getProjection().getProjectionMatrix()).invert();
		final Matrix4f invView = new Matrix4f(cam.getViewMatrix()).invert();

		final Vector4f clipCoords = new Vector4f(ndcX, ndcY, -1f, 1f);
		final Vector4f eyeCoords = invProj.transform(clipCoords);
		eyeCoords.z = -1f;
		eyeCoords.w = 0f;

		final Vector4f rayWorld4 = invView.transform(eyeCoords);
		final Vector3f rayDir = new Vector3f(rayWorld4.x, rayWorld4.y, rayWorld4.z).normalize();
		final Vector3f rayOrigin = cam.getPosition();

		// 3. Transform ray into mesh-local space
		final Matrix4f invMesh = new Matrix4f(this.getTransform().getMatrix()).invert();
		final Vector4f localOrigin4 = invMesh.transform(new Vector4f(rayOrigin, 1f));
		final Vector3f localOrigin = new Vector3f(localOrigin4.x, localOrigin4.y, localOrigin4.z);

		final Vector4f localDir4 = invMesh
				.transform(new Vector4f(rayOrigin.x + rayDir.x, rayOrigin.y + rayDir.y, rayOrigin.z + rayDir.z, 1f));
		final Vector3f localDir = new Vector3f(localDir4.x - localOrigin.x, localDir4.y - localOrigin.y, localDir4.z - localOrigin.z)
				.normalize();

		// Convert hit point on XZ plane to grid
		// Ray-plane intersection at Y = max height
		final float yMax = tMesh.getMaxHeight() * cellSize;
		final float yMin = tMesh.getMinHeight() * cellSize;

		// Sweep from top to bottom
		for (float yLevel = yMax; yLevel >= yMin; yLevel -= cellSize) {
			final float levelY = yLevel * cellSize;

			if (localDir.y == 0f) {
				continue; // parallel, skip
			}

			final float t = (levelY - localOrigin.y) / localDir.y;
			if (t < 0) {
				continue; // behind camera
			}

			final Vector3f hitLocal = new Vector3f(localOrigin.x + localDir.x * t, levelY, localOrigin.z + localDir.z * t);

			final int gridX = (int) Math.floor(hitLocal.x / cellSize);
			final int gridZ = (int) Math.floor(hitLocal.z / cellSize);

			if (!tMesh.isInBounds(gridX, gridZ)) {
				continue;
			}

			final int cellHeight = tMesh.getCellHeight(gridX, gridZ);
			if (yLevel <= cellHeight) {
				// Found first cube that intersects
				return new Vector2i(gridX, gridZ);
			}
		}

		return null; // nothing hit
	}

	public Vector3f getTilePosition(final Vector2i tile) {
		final Vector3f meshTranslation = super.getTransform().getTranslation();
		final int cellHeight = this.getMesh().getCellHeight(tile.x, tile.y);
		return new Vector3f(meshTranslation.x + tile.x + 0.5f, meshTranslation.y + cellHeight, meshTranslation.z + tile.y + 0.5f);
	}

	public Vector2i getCellPosition(final Vector3f pos) {
		final Vector3f scaledPos = pos.sub(0.5f, 0, 0.5f, new Vector3f()).sub(this.getTransform().getTranslation());
		return new Vector2i((int) scaledPos.x, (int) scaledPos.z);
	}

	@Override
	public TerrainMesh getMesh() {
		return (TerrainMesh) super.getMesh();
	}

	public void setTerrainHighlightEntity(final TerrainHighlightObject terrainHighlightObject) {
		this.replace(this.terrainHighlightObject, terrainHighlightObject);
		this.terrainHighlightObject = terrainHighlightObject;
	}

	public void setTerrainEdgeEntity(final TerrainEdgeObject terrainEdgeObject) {
		this.replace(this.terrainEdgeObject, terrainEdgeObject);
		this.terrainEdgeObject = terrainEdgeObject;
	}

	public TerrainEdgeObject getTerrainEdgeObject() {
		return this.terrainEdgeObject;
	}

	public TerrainHighlightObject getMoveHighlightObject() {
		return this.terrainHighlightObject;
	}

	public <T extends MeshGameObject> void setWaterLevel(final T terrainWaterObject) {
		this.replace(this.terrainWaterObject, terrainWaterObject);
		this.terrainWaterObject = terrainWaterObject;
	}

	@Override
	public <T extends ParentAwareComponent> void setParent(final T e) {
		this.parent = (WorldLevelScene) e;
	}

	@Override
	public WorldLevelScene getParent() {
		return this.parent;
	}

	@Override
	public Object getEntitiesLock() {
		return this.subEntitiesLock;
	}

	@Override
	public List<GameObject> getWEntities() {
		return this.subEntities;
	}

	@Override
	public List<GameObject> getROEntities() {
		return List.copyOf(this.subEntities);
	}

	@Override
	public boolean useObjectTransform() {
		return true;
	}

	public MeshGameObject getTerrainWaterObject() {
		return this.terrainWaterObject;
	}

	public TerrainHighlightObject getTerrainHighlightObject() {
		return this.terrainHighlightObject;
	}

	@Override
	public void setTransform(final Transform3DPivot ie) {
		this.transform = ie;
	}

	@Deprecated
	@Override
	public void setTransform(final Transform3D ie) {
		super.setTransform(ie);
	}

	@Override
	public Transform3DPivot getTransform() {
		return (Transform3DPivot) this.transform;
	}

	@Override
	public int getRandomTickDuration() {
		return 2_000;
	}

	@Override
	public String toString() {
		return "TerrainGameObject@" + System.identityHashCode(this) + " [subEntitiesLock=" + this.subEntitiesLock + ", subEntities="
				+ this.subEntities + ", parent=" + this.parent + ", terrainEdgeObject=" + this.terrainEdgeObject
				+ ", terrainHighlightObject=" + this.terrainHighlightObject + ", terrainWaterObject=" + this.terrainWaterObject
				+ ", minVariation=" + this.minVariation + ", maxVariation=" + this.maxVariation + ", variationCellSize="
				+ this.variationCellSize + ", hasVariation=" + this.hasVariation + ", materialId=" + this.materialId
				+ ", isEntityMaterialId=" + this.isEntityMaterialId + ", objectId=" + this.objectId + ", objectIdLocation="
				+ this.objectIdLocation + ", mesh=" + this.mesh + ", transform=" + this.transform + ", active=" + this.active + ", name="
				+ this.name + "]";
	}

}
