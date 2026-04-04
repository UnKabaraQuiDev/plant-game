package lu.kbra.plant_game.engine.entity.go.obj.terrain;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector2ic;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

import lu.kbra.pclib.PCUtils;
import lu.kbra.pclib.concurrency.ObjectTriggerLatch;
import lu.kbra.plant_game.base.entity.go.obj.water.NeedsRandomTick;
import lu.kbra.plant_game.base.entity.go.obj_inst.grass.GrowingInstanceGameObject;
import lu.kbra.plant_game.base.entity.go.obj_inst.grass.InstanceLargeGrassObject;
import lu.kbra.plant_game.base.entity.go.obj_inst.grass.InstanceMediumGrassObject;
import lu.kbra.plant_game.base.entity.go.obj_inst.grass.InstanceSmallGrassObject;
import lu.kbra.plant_game.engine.entity.go.GameObject;
import lu.kbra.plant_game.engine.entity.go.MeshGameObject;
import lu.kbra.plant_game.engine.entity.go.VariationMeshGameObject;
import lu.kbra.plant_game.engine.entity.go.factory.GameObjectFactory;
import lu.kbra.plant_game.engine.entity.go.impl.PlaceableObject;
import lu.kbra.plant_game.engine.entity.go.mesh.terrain.TerrainMesh;
import lu.kbra.plant_game.engine.entity.impl.Transform3DPivotOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.AbsoluteTransform3DOwner;
import lu.kbra.plant_game.engine.scene.world.WorldLevelScene;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.plant_game.generated.ColorMaterial;
import lu.kbra.standalone.gameengine.objs.entity.ParentAwareComponent;
import lu.kbra.standalone.gameengine.scene.SynchronizedEntityContainer;
import lu.kbra.standalone.gameengine.scene.camera.Camera3D;
import lu.kbra.standalone.gameengine.utils.consts.Direction;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;
import lu.kbra.standalone.gameengine.utils.transform.Transform3DPivot;

@JsonIgnoreType
public class TerrainGameObject extends VariationMeshGameObject
		implements SynchronizedEntityContainer<GameObject>, AbsoluteTransform3DOwner, Transform3DPivotOwner, NeedsRandomTick {

// ===== CONFIG =====
	private static final int MAX_MOISTURE = 100;
	private static final int WET_LEVEL_ON_WATER = 8;

	private static final int MOISTURE_GROWN = 10;
	private static final int MOISTURE_SMALL = 20;
	private static final int MOISTURE_MEDIUM = 40;
	private static final int MOISTURE_LARGE = 60;
	private static final int MOISTURE_FLOWER = 80;

	public static final int MOISTURE_DECAY = 2;
	public static final int WATER_GROW_SPEED = 1;

// ===== BIT LAYOUT =====
	private static final int GREEN_BIT = 1 << 0;
	private static final int SMALL_GRASS_BIT = 1 << 1;
	private static final int MEDIUM_GRASS_BIT = 1 << 2;
	private static final int LARGE_GRASS_BIT = 1 << 3;

	private static final int FLOWER_BIT = 1 << 4;
	private static final int BUILT_BIT = 1 << 5;

	private static final int FLOWER_TYPE_FIRST_BIT_INDEX = 8;
	private static final int FLOWER_TYPE_BIT_MASK = 0xF << FLOWER_TYPE_FIRST_BIT_INDEX;

	private static final int GRASS_ID_FIRST_BIT_INDEX = 12;
	private static final int GRASS_ID_BITS = 8;
	private static final int GRASS_ID_MAX = (1 << GRASS_ID_BITS) - 1;
	private static final int GRASS_ID_BIT_MASK = GRASS_ID_MAX << GRASS_ID_FIRST_BIT_INDEX;

	private static final int DRY_GRASS_BIT = 1 << 20;

	private static final int WET_BIT_INDEX = 21;
	private static final int WET_BITS = 4;
	private static final int WET_MASK = (1 << WET_BITS) - 1 << WET_BIT_INDEX;
// ===== GRASS =====
	private static final int SMALL_GRASS_LEVEL = 0;
	private static final int MEDIUM_GRASS_LEVEL = 1;
	private static final int LARGE_GRASS_LEVEL = 2;

// ===== DATA =====
	protected int width;
	protected int length;

	protected byte[] moisture;
	protected int[] population;

	protected WeakReference<InstanceSmallGrassObject> smallGrass;
	protected WeakReference<InstanceMediumGrassObject> mediumGrass;
	protected WeakReference<InstanceLargeGrassObject> largeGrass;
	protected WeakReference<InstanceSmallGrassObject> deadSmallGrass;
	protected WeakReference<InstanceMediumGrassObject> deadMediumGrass;
	protected WeakReference<InstanceLargeGrassObject> deadLargeGrass;

	protected Object subEntitiesLock = new Object();
	protected List<GameObject> subEntities = Collections.synchronizedList(new ArrayList<>());
	protected WorldLevelScene parent;
	protected TerrainEdgeObject terrainEdgeObject;
	protected TerrainHighlightObject terrainHighlightObject;
	protected MeshGameObject terrainWaterObject;

	public TerrainGameObject(final String str, final TerrainMesh mesh) {
		super(str, mesh);
		super.setTransform(new Transform3DPivot());
	}

	public ObjectTriggerLatch<TerrainGameObject> init() {
		synchronized (this) {
			if (this.moisture != null) {
				throw new IllegalStateException("This TerrainGameObject was already initialized.");
			}

			final TerrainMesh mesh = this.getMesh();
			this.width = mesh.getWidth();
			this.length = mesh.getLength();

			this.moisture = new byte[this.width * this.length + 1];
			this.population = new int[this.width * this.length + 1];
		}

		final ObjectTriggerLatch<TerrainGameObject> latch = new ObjectTriggerLatch<>(6, this);

		synchronized (this) {
			GameObjectFactory.createInstances(InstanceSmallGrassObject.class, i -> new Transform3D(), OptionalInt.of(128), Optional.empty())
					.set(i -> i.setColorMaterial(ColorMaterial.GREEN))
					.set(i -> i.setTransform(new Transform3D(this.getTransform().getTranslation().negate(new Vector3f()))))
					.add(this)
					.postInit(i -> this.smallGrass = new WeakReference<>(i))
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
					.postInit(i -> this.deadSmallGrass = new WeakReference<>(i))
					.latch(latch)
					.push();

			GameObjectFactory
					.createInstances(InstanceMediumGrassObject.class, i -> new Transform3D(), OptionalInt.of(128), Optional.empty())
					.set(i -> i.setColorMaterial(ColorMaterial.LIGHT_BROWN))
					.set(i -> i.setTransform(new Transform3D(this.getTransform().getTranslation().negate(new Vector3f()))))
					.add(this)
					.postInit(i -> this.deadMediumGrass = new WeakReference<>(i))
					.latch(latch)
					.push();

			GameObjectFactory.createInstances(InstanceLargeGrassObject.class, i -> new Transform3D(), OptionalInt.of(128), Optional.empty())
					.set(i -> i.setColorMaterial(ColorMaterial.LIGHT_BROWN))
					.set(i -> i.setTransform(new Transform3D(this.getTransform().getTranslation().negate(new Vector3f()))))
					.add(this)
					.postInit(i -> this.deadLargeGrass = new WeakReference<>(i))
					.latch(latch)
					.push();
		}

		return latch;
	}

	@Override
	public void randomTick(final WindowInputHandler inputHandler, final WorldLevelScene worldLevelScene) {
		this.decayTick();
	}

	private int idx(final int x, final int y) {
		assert !(x < 0 || x >= this.width || y < 0 || y >= this.length) : "(" + x + ", " + y + ")";
		return x < 0 || x >= this.width || y < 0 || y >= this.length ? this.width * this.length : x + y * this.width;
	}

	private int idx(final Vector2ic tile) {
		assert !(tile.x() < 0 || tile.x() >= this.width || tile.y() < 0 || tile.y() >= this.length) : tile;
		return tile.x() < 0 || tile.x() >= this.width || tile.y() < 0 || tile.y() >= this.length ? this.width * this.length
				: tile.x() + tile.y() * this.width;
	}

	public void decayTick() {
		final TerrainMesh mesh = this.getMesh();

		int completed = 0;
		final int total = this.length * this.width;

		for (int i = 0; i < total; i++) {
			int p = this.population[i];

			// ===== WET HANDLING =====
			final int wet = getWet(p);
			if (wet > 0) {
				p = setWet(p, wet - 1);
				this.population[i] = p;

				// even if wet, still count progress
				if ((p & BUILT_BIT) != 0 || ((this.moisture[i] & 0xFF) >= MOISTURE_MEDIUM)) {
					completed++;
				}
				continue;
			}

			// ===== MOISTURE DECAY =====
			int m = this.moisture[i] & 0xFF;
			if (m > 0) {
				m = Math.max(0, m - MOISTURE_DECAY);
				this.moisture[i] = (byte) m;
			}

			final int x = i % this.width;
			final int y = i / this.width;
			final Vector2i tile = new Vector2i(x, y);

			// ===== FLOWER =====
			if (m < MOISTURE_FLOWER) {
				this.removeFlower(tile);
			}

			// ===== GROUND COLOR =====
			mesh.setGrown(tile, false);
			p &= ~GREEN_BIT;

			// ===== GRASS HANDLING =====
			final OptionalInt grass = this.getGrassLevel(i);

			if (grass.isPresent()) {
				final boolean isDry = (p & DRY_GRASS_BIT) != 0;

				// Step 1: convert to dry
				if (!isDry && m < MOISTURE_SMALL) {
					this.convertGrassToDry(tile);
					p = this.population[i]; // refresh after mutation
				}
				// Step 2: remove dry grass
				else if (isDry && m < MOISTURE_SMALL) {
					this.removeGrass(tile);
					p = this.population[i]; // refresh
				}
			}

			this.population[i] = p;

			// ===== PROGRESS COUNT =====
			if ((p & BUILT_BIT) != 0 || m >= MOISTURE_MEDIUM) {
				completed++;
			}
		}

		System.err.println(completed + " / " + total + " = " + (byte) (completed * 100 / total));

		this.getParent().getGameData().setProgress((byte) (completed * 100 / total));
	}

	public void place(final Optional<Vector2i> source, final Optional<Direction> sourceDir, final PlaceableObject obj) {
		if (source.isPresent() && sourceDir.isPresent()) {
			obj.getFootprint().forEachCell(source.get(), sourceDir.get(), i -> {
				this.population[this.idx(i)] &= ~BUILT_BIT;
				this.removeGrass(i);
				this.removeFlower(i);
			});
		}

		final TerrainMesh mesh = this.getMesh();
		obj.forEachCell(i -> {
			this.population[this.idx(i)] |= BUILT_BIT;
			mesh.setColorMaterial(i, ColorMaterial.DARK_GRAY);
		});
	}

	private void removeFlower(final Vector2i tile) {
		if (tile.x() < 0 || tile.x() >= this.width || tile.y() < 0 || tile.y() >= this.length) {
			return;
		}

		final int i = this.idx(tile);

		if ((this.population[i] & FLOWER_BIT) == 0) {
			return;
		}

		this.population[i] &= ~FLOWER_BIT;
		this.population[i] &= ~FLOWER_TYPE_BIT_MASK;
	}

	private void removeGrass(final Vector2i tile) {
		if (tile.x() < 0 || tile.x() >= this.width || tile.y() < 0 || tile.y() >= this.length) {
			return;
		}

		final int i = this.idx(tile);

		final OptionalInt grass = this.getGrassLevel(i);
		if (grass.isEmpty()) {
			return;
		}

		final int p = this.population[i];

		final int instanceIndex = (p & GRASS_ID_BIT_MASK) >>> GRASS_ID_FIRST_BIT_INDEX;
		final boolean isDry = (p & DRY_GRASS_BIT) != 0;

		final GrowingInstanceGameObject obj = switch (grass.getAsInt()) {
		case SMALL_GRASS_LEVEL -> isDry ? this.getDeadSmallGrassObject() : this.getSmallGrassObject();
		case MEDIUM_GRASS_LEVEL -> isDry ? this.getDeadMediumGrassObject() : this.getMediumGrassObject();
		case LARGE_GRASS_LEVEL -> isDry ? this.getDeadLargeGrassObject() : this.getLargeGrassObject();
		default -> null;
		};

		if (obj != null) {
			obj.removeInstance(instanceIndex);
		}

		int np = p;
		np &= ~GRASS_ID_BIT_MASK;
		np &= ~(SMALL_GRASS_BIT | MEDIUM_GRASS_BIT | LARGE_GRASS_BIT);
		np &= ~DRY_GRASS_BIT;

		this.population[i] = np;
	}

	public void addWater(final Vector2i tile, final float amount) {
		if (tile.x() < 0 || tile.x() >= this.width || tile.y() < 0 || tile.y() >= this.length) {
			return;
		}

		final int i = this.idx(tile.x(), tile.y());

		this.population[i] = setWet(this.population[i], WET_LEVEL_ON_WATER);

		if ((this.population[i] & BUILT_BIT) != 0) {
			return;
		}

		int m = this.moisture[i] & 0xFF;
		m = Math.min(MAX_MOISTURE, m + (int) (amount * WATER_GROW_SPEED));
		this.moisture[i] = (byte) m;

		if (m >= MOISTURE_FLOWER) {
			this.addFlower(tile);
		}
		if (m >= MOISTURE_LARGE) {
			this.addGrass(tile, LARGE_GRASS_LEVEL);
		} else if (m >= MOISTURE_MEDIUM) {
			this.addGrass(tile, MEDIUM_GRASS_LEVEL);
		} else if (m >= MOISTURE_SMALL) {
			this.addGrass(tile, SMALL_GRASS_LEVEL);
		}
		if (m >= MOISTURE_GROWN) {
			this.getMesh().setGrown(tile, true);
			this.population[i] |= GREEN_BIT;
		}
	}

	private void addFlower(final Vector2i tile) {
		if (tile.x() < 0 || tile.x() >= this.width || tile.y() < 0 || tile.y() >= this.length) {
			return;
		}

		final int i = this.idx(tile);

		if ((this.population[i] & BUILT_BIT) != 0 || (this.population[i] & FLOWER_BIT) != 0 || Math.random() >= 0.5) {
			return;
		}

		int p = this.population[i];

		p &= ~FLOWER_TYPE_BIT_MASK;
		p |= FLOWER_BIT;

		final int flowerType = PCUtils.randomIntRange(0, 16);
		p |= flowerType << FLOWER_TYPE_FIRST_BIT_INDEX & FLOWER_TYPE_BIT_MASK;

		this.population[i] = p;
	}

	private OptionalInt getGrassLevel(final int i) {
		final int p = this.population[i];
		if ((p & SMALL_GRASS_BIT) != 0) {
			return OptionalInt.of(SMALL_GRASS_LEVEL);
		}
		if ((p & MEDIUM_GRASS_BIT) != 0) {
			return OptionalInt.of(MEDIUM_GRASS_LEVEL);
		}
		if ((p & LARGE_GRASS_BIT) != 0) {
			return OptionalInt.of(LARGE_GRASS_LEVEL);
		}
		return OptionalInt.empty();
	}

	private void addGrass(final Vector2i tile, final int level) {
		if (tile.x() < 0 || tile.x() >= this.width || tile.y() < 0 || tile.y() >= this.length) {
			return;
		}

		final int i = this.idx(tile);

		if (level < SMALL_GRASS_LEVEL || level > LARGE_GRASS_LEVEL) {
			throw new IllegalArgumentException("Invalid grass level: " + level);
		}

		if ((this.population[i] & BUILT_BIT) != 0) {
			return;
		}

		final OptionalInt current = this.getGrassLevel(i);
		if (current.isPresent() && current.getAsInt() >= level) {
			return;
		}

		if (current.isPresent()) {
			this.removeGrass(tile);
		}

		final Vector3f pos = this.getTilePosition(tile);
		final int instanceId;

		switch (level) {
		case SMALL_GRASS_LEVEL -> {
			final var obj = this.getSmallGrassObject();
			if (obj == null) {
				return;
			}
			instanceId = obj.addInstance(pos);
		}
		case MEDIUM_GRASS_LEVEL -> {
			final var obj = this.getMediumGrassObject();
			if (obj == null) {
				return;
			}
			instanceId = obj.addInstance(pos);
		}
		case LARGE_GRASS_LEVEL -> {
			final var obj = this.getLargeGrassObject();
			if (obj == null) {
				return;
			}
			instanceId = obj.addInstance(pos);
		}
		default -> throw new IllegalStateException();
		}

		int p = this.population[i];

		// clear dry flag
		p &= ~DRY_GRASS_BIT;

		// clear grass level
		p &= ~(SMALL_GRASS_BIT | MEDIUM_GRASS_BIT | LARGE_GRASS_BIT);
		p |= grassLevelToBit(level);

		// set instance id (FIXED precedence)
		p &= ~GRASS_ID_BIT_MASK;
		p |= PCUtils.clamp(0, GRASS_ID_MAX, instanceId) << GRASS_ID_FIRST_BIT_INDEX & GRASS_ID_BIT_MASK;

		this.population[i] = p;
	}

	private void convertGrassToDry(final Vector2i tile) {
		if (tile.x() < 0 || tile.x() >= this.width || tile.y() < 0 || tile.y() >= this.length) {
			return;
		}

		final int i = this.idx(tile);

		final OptionalInt grass = this.getGrassLevel(i);
		if (grass.isEmpty()) {
			return;
		}

		final int p = this.population[i];
		final int instanceIndex = (p & GRASS_ID_BIT_MASK) >>> GRASS_ID_FIRST_BIT_INDEX;

		final Vector3f pos = this.getTilePosition(tile);

		// remove green
		switch (grass.getAsInt()) {
		case SMALL_GRASS_LEVEL -> {
			final var o = this.getSmallGrassObject();
			if (o != null) {
				o.removeInstance(instanceIndex);
			}
		}
		case MEDIUM_GRASS_LEVEL -> {
			final var o = this.getMediumGrassObject();
			if (o != null) {
				o.removeInstance(instanceIndex);
			}
		}
		case LARGE_GRASS_LEVEL -> {
			final var o = this.getLargeGrassObject();
			if (o != null) {
				o.removeInstance(instanceIndex);
			}
		}
		}

		// add dry
		int newId = -1;

		switch (grass.getAsInt()) {
		case SMALL_GRASS_LEVEL -> {
			final var o = this.getDeadSmallGrassObject();
			if (o != null) {
				newId = o.addInstance(pos);
			}
		}
		case MEDIUM_GRASS_LEVEL -> {
			final var o = this.getDeadMediumGrassObject();
			if (o != null) {
				newId = o.addInstance(pos);
			}
		}
		case LARGE_GRASS_LEVEL -> {
			final var o = this.getDeadLargeGrassObject();
			if (o != null) {
				newId = o.addInstance(pos);
			}
		}
		}

		int np = p;
		np |= DRY_GRASS_BIT;

		np &= ~GRASS_ID_BIT_MASK;
		np |= PCUtils.clamp(0, GRASS_ID_MAX, newId) << GRASS_ID_FIRST_BIT_INDEX & GRASS_ID_BIT_MASK;

		this.population[i] = np;
	}

	private static int getWet(final int p) {
		return (p & WET_MASK) >> WET_BIT_INDEX;
	}

	private static int setWet(final int p, final int v) {
		return p & ~WET_MASK | (v & (1 << WET_BITS) - 1) << WET_BIT_INDEX;
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

	private InstanceSmallGrassObject getDeadSmallGrassObject() {
		return this.deadSmallGrass != null ? this.deadSmallGrass.get() : null;
	}

	private InstanceMediumGrassObject getDeadMediumGrassObject() {
		return this.deadMediumGrass != null ? this.deadMediumGrass.get() : null;
	}

	private InstanceLargeGrassObject getDeadLargeGrassObject() {
		return this.deadLargeGrass != null ? this.deadLargeGrass.get() : null;
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
		final float ndcX = mousePos.x / windowWidth * 2f - 1f;
		final float ndcY = 1f - mousePos.y / windowHeight * 2f;

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
		return 3_500;
	}

	@Override
	public String toString() {
		return "TerrainGameObject@" + System.identityHashCode(this) + " [width=" + this.width + ", length=" + this.length + ", moisture="
				+ Arrays.toString(this.moisture) + ", population=" + Arrays.toString(this.population) + ", smallGrass=" + this.smallGrass
				+ ", mediumGrass=" + this.mediumGrass + ", largeGrass=" + this.largeGrass + ", deadSmallGrass=" + this.deadSmallGrass
				+ ", deadMediumGrass=" + this.deadMediumGrass + ", deadLargeGrass=" + this.deadLargeGrass + ", subEntitiesLock="
				+ this.subEntitiesLock + ", subEntities=" + this.subEntities + ", terrainEdgeObject=" + this.terrainEdgeObject
				+ ", terrainHighlightObject=" + this.terrainHighlightObject + ", terrainWaterObject=" + this.terrainWaterObject
				+ ", minVariation=" + this.minVariation + ", maxVariation=" + this.maxVariation + ", variationCellSize="
				+ this.variationCellSize + ", hasVariation=" + this.hasVariation + ", materialId=" + this.materialId
				+ ", isEntityMaterialId=" + this.isEntityMaterialId + ", mesh=" + this.mesh + ", objectId=" + this.objectId
				+ ", objectIdLocation=" + this.objectIdLocation + ", transform=" + this.transform + ", active=" + this.active + ", name="
				+ this.name + "]";
	}

}
