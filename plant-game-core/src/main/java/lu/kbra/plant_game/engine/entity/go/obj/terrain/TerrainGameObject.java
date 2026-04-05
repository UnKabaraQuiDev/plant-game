package lu.kbra.plant_game.engine.entity.go.obj.terrain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Consumer;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector2ic;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

import lu.kbra.pclib.PCUtils;
import lu.kbra.pclib.concurrency.ObjectTriggerLatch;
import lu.kbra.pclib.datastructure.list.WeakArrayList;
import lu.kbra.pclib.datastructure.list.WeakList;
import lu.kbra.pclib.logger.GlobalLogger;
import lu.kbra.pclib.pointer.JavaPointer;
import lu.kbra.pclib.pointer.WeakObjectPointer;
import lu.kbra.plant_game.base.entity.go.obj.water.NeedsRandomTick;
import lu.kbra.plant_game.base.entity.go.obj_inst.InstanceChampiObject.InstanceLargeChampiObject;
import lu.kbra.plant_game.base.entity.go.obj_inst.InstanceChampiObject.InstanceMediumChampiObject;
import lu.kbra.plant_game.base.entity.go.obj_inst.InstanceChampiObject.InstanceSmallChampiObject;
import lu.kbra.plant_game.base.entity.go.obj_inst.InstanceGrassObject.InstanceLargeGrassObject;
import lu.kbra.plant_game.base.entity.go.obj_inst.InstanceGrassObject.InstanceMediumGrassObject;
import lu.kbra.plant_game.base.entity.go.obj_inst.InstanceGrassObject.InstanceSmallGrassObject;
import lu.kbra.plant_game.base.entity.go.obj_inst.InstanceRoundFlowerObject.InstanceLargeRoundFlowerObject;
import lu.kbra.plant_game.base.entity.go.obj_inst.InstanceRoundFlowerObject.InstanceMediumRoundFlowerObject;
import lu.kbra.plant_game.base.entity.go.obj_inst.InstanceRoundFlowerObject.InstanceSmallRoundFlowerObject;
import lu.kbra.plant_game.engine.entity.go.GameObject;
import lu.kbra.plant_game.engine.entity.go.MeshGameObject;
import lu.kbra.plant_game.engine.entity.go.VariationMeshGameObject;
import lu.kbra.plant_game.engine.entity.go.factory.GameObjectFactory;
import lu.kbra.plant_game.engine.entity.go.impl.PlaceableObject;
import lu.kbra.plant_game.engine.entity.go.mesh.terrain.TerrainMesh;
import lu.kbra.plant_game.engine.entity.go.obj_inst.GrowingInstanceGameObject;
import lu.kbra.plant_game.engine.entity.impl.GrownObject;
import lu.kbra.plant_game.engine.entity.impl.SizeOwner;
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

	public record TerrainData(byte[] moisture, int[] population) {

	}

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
	// basic flags
	private static final int GREEN_BIT = 1 << 0;
	private static final int BUILT_BIT = 1 << 3;

	// ===== GRASS LEVEL (2 bits) =====
	private static final int GRASS_LEVEL_FIRST_BIT_INDEX = 1;
	private static final int GRASS_LEVEL_BITS = 2;
	private static final int GRASS_LEVEL_MASK = (1 << TerrainGameObject.GRASS_LEVEL_BITS)
			- 1 << TerrainGameObject.GRASS_LEVEL_FIRST_BIT_INDEX;

	private static final int GRASS_LEVEL_NONE = 0;
	private static final int GRASS_LEVEL_SMALL = 1;
	private static final int GRASS_LEVEL_MEDIUM = 2;
	private static final int GRASS_LEVEL_LARGE = 3;

	// ===== FLOWER TYPE (0 = none) =====
	private static final int FLOWER_TYPE_FIRST_BIT_INDEX = 4;
	private static final int FLOWER_TYPE_BITS = 4;
	private static final int FLOWER_TYPE_MASK = (1 << TerrainGameObject.FLOWER_TYPE_BITS)
			- 1 << TerrainGameObject.FLOWER_TYPE_FIRST_BIT_INDEX;

	private static final int FLOWER_TYPE_NONE = 0;
	private static final int FLOWER_TYPE_PROHIBIT = 1;
	private static final int FLOWER_TYPE_OFFSET = -2;

	private static final int FLOWER_TYPE_MAX_COUNT = (1 << TerrainGameObject.FLOWER_TYPE_BITS) - 2;

	// ===== GRASS INSTANCE ID =====
	private static final int GRASS_ID_FIRST_BIT_INDEX = 8;
	private static final int GRASS_ID_BITS = 8;
	private static final int GRASS_ID_MAX = (1 << TerrainGameObject.GRASS_ID_BITS) - 1;
	private static final int GRASS_ID_MASK = TerrainGameObject.GRASS_ID_MAX << TerrainGameObject.GRASS_ID_FIRST_BIT_INDEX;

	// ===== DRY =====
	private static final int DRY_GRASS_BIT = 1 << 16;

	// ===== WET =====
	private static final int WET_BIT_INDEX = 17;
	private static final int WET_BITS = 4;
	private static final int WET_MASK = (1 << TerrainGameObject.WET_BITS) - 1 << TerrainGameObject.WET_BIT_INDEX;

	// ===== FLOWER INSTANCE ID =====
	private static final int FLOWER_ID_FIRST_BIT_INDEX = 21;
	private static final int FLOWER_ID_BITS = 8;
	private static final int FLOWER_ID_MAX = (1 << TerrainGameObject.FLOWER_ID_BITS) - 1;
	private static final int FLOWER_ID_MASK = TerrainGameObject.FLOWER_ID_MAX << TerrainGameObject.FLOWER_ID_FIRST_BIT_INDEX;

	private static final ColorMaterial BUILT_GROUND_COLOR = ColorMaterial.GRAY;

	// ===== DATA =====
	protected int width;
	protected int length;

	protected byte[] moisture;
	/**
	 * Packed per-tile state array.
	 *
	 * <p>
	 * Each entry encodes terrain state in a 32-bit integer.
	 * </p>
	 *
	 * <h3>Bit layout</h3>
	 *
	 * <pre>
	 * Bit(s)     Name                        Description
	 * -------------------------------------------------------------------------
	 * 0          GREEN                      Ground is visually grown
	 *
	 * 1 - 2      GRASS_LEVEL                {@link GRASS_LEVEL_NONE} = none
	 *                                       {@link GRASS_LEVEL_SMALL} = small
	 *                                       {@link GRASS_LEVEL_MEDIUM} = medium
	 *                                       {@link GRASS_LEVEL_LARGE} = large
	 *
	 * 3          BUILT                      Structure present
	 *
	 * 4 - 7      FLOWER_TYPE                {@link FLOWER_TYPE_NONE} = no flower
	 *                                       n = flowerTypes[n+{@link FLOWER_TYPE_OFFSET}]
	 *
	 * 8 - 15     GRASS_ID                   Grass instance ID
	 *
	 * 16         DRY_GRASS                  Grass is dry
	 *
	 * 17 - 20    WET                        Temporary wet level
	 *
	 * 21 - 28    FLOWER_ID                  Flower instance ID
	 * </pre>
	 *
	 * <h3>Notes</h3>
	 * <ul>
	 * <li>Grass level is encoded as a 2-bit value.</li>
	 * <li>Flower presence is encoded via {@code FLOWER_TYPE != 0}.</li>
	 * <li>Grass and flower instances are independent.</li>
	 * <li>{@code BUILT} blocks vegetation.</li>
	 * </ul>
	 */
	protected int[] population;

	protected WeakObjectPointer<InstanceSmallGrassObject> smallGrass = new WeakObjectPointer<>();
	protected WeakObjectPointer<InstanceMediumGrassObject> mediumGrass = new WeakObjectPointer<>();
	protected WeakObjectPointer<InstanceLargeGrassObject> largeGrass = new WeakObjectPointer<>();
	protected WeakObjectPointer<InstanceSmallGrassObject> deadSmallGrass = new WeakObjectPointer<>();
	protected WeakObjectPointer<InstanceMediumGrassObject> deadMediumGrass = new WeakObjectPointer<>();
	protected WeakObjectPointer<InstanceLargeGrassObject> deadLargeGrass = new WeakObjectPointer<>();
	protected WeakList<GrowingInstanceGameObject> flowerTypes = new WeakArrayList<>();

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

	public ObjectTriggerLatch<TerrainGameObject> init(final Optional<TerrainData> td) {
		synchronized (this) {
			if (this.moisture != null) {
				throw new IllegalStateException("This TerrainGameObject was already initialized.");
			}

			final TerrainMesh mesh = this.getMesh();
			this.width = mesh.getWidth();
			this.length = mesh.getLength();
			final int expectedSize = this.width * this.length + 1;

			td.ifPresentOrElse(tt -> {
				this.moisture = tt.moisture();
				this.population = tt.population();
			}, () -> {
				this.moisture = new byte[expectedSize];
				this.population = new int[expectedSize];
			});

			if (this.moisture.length != expectedSize || this.population.length != expectedSize) {
				throw new IllegalStateException("Arrays not of expected size: " + this.moisture.length + " & " + this.population.length);
			}
		}

		final ObjectTriggerLatch<TerrainGameObject> latch = new ObjectTriggerLatch<>(12, this);
		latch.then((Consumer<TerrainGameObject>) tgo -> {
			if (this.flowerTypes.isEmpty()) {
				GlobalLogger.warning("No flower type available.");
			} else if (this.flowerTypes.size() > TerrainGameObject.FLOWER_TYPE_MAX_COUNT) {
				GlobalLogger.warning("Too many flower types available: " + this.flowerTypes.size());
			}
			this.flowerTypes
					.sort(Comparator.comparing(c -> ((GrownObject) c).getGrownName()).thenComparingInt(c -> ((SizeOwner) c).getSize()));
			td.ifPresent(d -> this.updateTiles());
		});

		synchronized (this) {
			this.registerGrassType(InstanceSmallGrassObject.class, ColorMaterial.GREEN, this.smallGrass, latch);
			this.registerGrassType(InstanceMediumGrassObject.class, ColorMaterial.GREEN, this.mediumGrass, latch);
			this.registerGrassType(InstanceLargeGrassObject.class, ColorMaterial.GREEN, this.largeGrass, latch);

			this.registerGrassType(InstanceSmallGrassObject.class, ColorMaterial.BROWN, this.deadSmallGrass, latch);
			this.registerGrassType(InstanceMediumGrassObject.class, ColorMaterial.BROWN, this.deadMediumGrass, latch);
			this.registerGrassType(InstanceLargeGrassObject.class, ColorMaterial.BROWN, this.deadLargeGrass, latch);

			this.registerFlowerType(InstanceSmallChampiObject.class, ColorMaterial.LIGHT_CYAN, latch);
			this.registerFlowerType(InstanceMediumChampiObject.class, ColorMaterial.LIGHT_MAGENTA, latch);
			this.registerFlowerType(InstanceLargeChampiObject.class, ColorMaterial.RED, latch);

			this.registerFlowerType(InstanceSmallRoundFlowerObject.class, ColorMaterial.DARK_PINK, latch);
			this.registerFlowerType(InstanceMediumRoundFlowerObject.class, ColorMaterial.DARK_RED, latch);
			this.registerFlowerType(InstanceLargeRoundFlowerObject.class, ColorMaterial.ORANGE, latch);
		}

		return latch;
	}

	private <T extends GrowingInstanceGameObject> void registerGrassType(
			final Class<T> class1,
			final ColorMaterial green,
			final JavaPointer<T> object,
			final ObjectTriggerLatch<TerrainGameObject> latch) {
		GameObjectFactory.createInstances(class1, i -> new Transform3D(), OptionalInt.of(128), Optional.empty())
				.set(i -> i.setColorMaterial(green))
				.set(i -> i.setTransform(new Transform3D(this.getTransform().getTranslation().negate(new Vector3f()))))
				.add(this)
				.get(object)
				.latch(latch)
				.push();
	}

	private <T extends GrowingInstanceGameObject> void registerFlowerType(
			final Class<T> class1,
			final ColorMaterial darkPink,
			final ObjectTriggerLatch<TerrainGameObject> latch) {
		GameObjectFactory.createInstances(class1, i -> new Transform3D(), OptionalInt.of(32), Optional.empty())
				.set(i -> i.setColorMaterial(darkPink))
				.set(i -> i.setTransform(new Transform3D(this.getTransform().getTranslation().negate(new Vector3f()))))
				.add(this)
				.postInit(this.flowerTypes::add)
				.latch(latch)
				.push();
	}

	private void updateTiles() {
		final TerrainMesh mesh = this.getMesh();
		final int total = this.width * this.length;

		for (int i = 0; i < total; i++) {
			int p = this.population[i];

			final int x = i % this.width;
			final int y = i / this.width;
			final Vector2i tile = new Vector2i(x, y);
			final Vector3f pos = this.getTilePosition(tile);

			if ((p & TerrainGameObject.GREEN_BIT) != 0) {
				mesh.setGrown(tile, true);
			}
			if ((p & TerrainGameObject.BUILT_BIT) != 0) {
				mesh.setColorMaterial(tile, TerrainGameObject.BUILT_GROUND_COLOR);
			}

			final int level = TerrainGameObject.getGrassLevelDirect(p);

			if (level != TerrainGameObject.GRASS_LEVEL_NONE && (p & TerrainGameObject.BUILT_BIT) == 0) {
				final boolean isDry = (p & TerrainGameObject.DRY_GRASS_BIT) != 0;

				final GrowingInstanceGameObject obj = isDry ? this.getDeadGrassObject(level) : this.getLivingGrassObject(level);

				if (obj != null) {
					final int newId = obj.addInstance(pos);

					p &= ~TerrainGameObject.GRASS_ID_MASK;
					p |= PCUtils.clamp(0, TerrainGameObject.GRASS_ID_MAX, newId) << TerrainGameObject.GRASS_ID_FIRST_BIT_INDEX
							& TerrainGameObject.GRASS_ID_MASK;
				}
			} else {
				p &= ~TerrainGameObject.GRASS_ID_MASK;
				p &= ~TerrainGameObject.GRASS_LEVEL_MASK;
			}

			final int flowerTypeEncoded = TerrainGameObject.getFlowerTypeDirect(p);

			if (flowerTypeEncoded != TerrainGameObject.FLOWER_TYPE_NONE && (p & TerrainGameObject.BUILT_BIT) == 0) {
				final int flowerIndex = flowerTypeEncoded + TerrainGameObject.FLOWER_TYPE_OFFSET;

				if (flowerIndex >= 0 && flowerIndex < this.flowerTypes.size()) {
					final GrowingInstanceGameObject obj = this.flowerTypes.get(flowerIndex);

					if (obj != null) {
						final int newId = obj.addInstance(pos);

						p &= ~TerrainGameObject.FLOWER_ID_MASK;
						p |= PCUtils.clamp(0, TerrainGameObject.FLOWER_ID_MAX, newId) << TerrainGameObject.FLOWER_ID_FIRST_BIT_INDEX
								& TerrainGameObject.FLOWER_ID_MASK;
					} else {
						System.err.println("No flower: " + flowerIndex);
					}
				} else {
					p &= ~TerrainGameObject.FLOWER_TYPE_MASK;
					p &= ~TerrainGameObject.FLOWER_ID_MASK;
				}
			} else {
				p &= ~TerrainGameObject.FLOWER_ID_MASK;
			}

			this.population[i] = p;
		}
	}

	@Override
	public void randomTick(final WindowInputHandler inputHandler, final WorldLevelScene worldLevelScene) {
		this.decayTick();
	}

	private int idx(final int x, final int y) {
		assert x >= 0 && x < this.width && y >= 0 && y < this.length : "(" + x + ", " + y + ")";
		return x < 0 || x >= this.width || y < 0 || y >= this.length ? this.width * this.length : x + y * this.width;
	}

	private int idx(final Vector2ic tile) {
		assert tile.x() >= 0 && tile.x() < this.width && tile.y() >= 0 && tile.y() < this.length : tile;
		return tile.x() < 0 || tile.x() >= this.width || tile.y() < 0 || tile.y() >= this.length ? this.width * this.length
				: tile.x() + tile.y() * this.width;
	}

	public void decayTick() {
		final TerrainMesh mesh = this.getMesh();

		int completed = 0;
		final int total = this.length * this.width;

		for (int i = 0; i < total; i++) {
			int p = this.population[i];

			final int wet = TerrainGameObject.getWet(p);
			if (wet > 0) {
				p = TerrainGameObject.setWet(p, wet - 1);
				this.population[i] = p;

				if ((p & TerrainGameObject.BUILT_BIT) != 0 || (this.moisture[i] & 0xFF) >= TerrainGameObject.MOISTURE_MEDIUM) {
					completed++;
				}
				continue;
			}

			int m = this.moisture[i] & 0xFF;
			if (m > 0) {
				m = Math.max(0, m - TerrainGameObject.MOISTURE_DECAY);
				this.moisture[i] = (byte) m;
			}

			final int x = i % this.width;
			final int y = i / this.width;
			final Vector2i tile = new Vector2i(x, y);

			if (m < TerrainGameObject.MOISTURE_FLOWER) {
				this.removeFlower(tile);
			}

			mesh.setGrown(tile, false);
			p &= ~TerrainGameObject.GREEN_BIT;

			final OptionalInt grass = this.getGrassLevel(i);

			if (grass.isPresent()) {
				final boolean isDry = (p & TerrainGameObject.DRY_GRASS_BIT) != 0;

				if (!isDry && m < TerrainGameObject.MOISTURE_SMALL) {
					this.convertGrassToDry(tile);
					p = this.population[i];
				} else if (isDry && m < TerrainGameObject.MOISTURE_SMALL) {
					this.removeGrass(tile);
					p = this.population[i];
				}
			}

			this.population[i] = p;

			if ((p & TerrainGameObject.BUILT_BIT) != 0 || m >= TerrainGameObject.MOISTURE_MEDIUM) {
				completed++;
			}
		}

		this.getParent().getGameData().setProgress((byte) (completed * 100 / total));
	}

	public void place(final Optional<Vector2i> source, final Optional<Direction> sourceDir, final PlaceableObject obj) {
		if (source.isPresent() && sourceDir.isPresent()) {
			obj.getFootprint().forEachCell(source.get(), sourceDir.get(), i -> {
				this.population[this.idx(i)] &= ~TerrainGameObject.BUILT_BIT;
				this.removeGrass(i);
				this.removeFlower(i);
			});
		}

		final TerrainMesh mesh = this.getMesh();
		obj.forEachCell(i -> {
			this.population[this.idx(i)] |= TerrainGameObject.BUILT_BIT;
			mesh.setColorMaterial(i, TerrainGameObject.BUILT_GROUND_COLOR);
		});
	}

	private void removeFlower(final Vector2i tile) {
		final int i = this.idx(tile);
		int p = this.population[i];

		final int type = TerrainGameObject.getFlowerTypeDirect(p);
		if (type == TerrainGameObject.FLOWER_TYPE_NONE || type == TerrainGameObject.FLOWER_TYPE_PROHIBIT) {
			return;
		}

		final int id = (p & TerrainGameObject.FLOWER_ID_MASK) >>> TerrainGameObject.FLOWER_ID_FIRST_BIT_INDEX;
		final int index = type + TerrainGameObject.FLOWER_TYPE_OFFSET;

		if (index < 0 || index >= this.flowerTypes.size()) {
			GlobalLogger.warning("Invalid flower index: " + index + " (type=" + type + ")");
			return;
		}

		final GrowingInstanceGameObject obj = this.flowerTypes.get(index);

		if (obj != null) {
			obj.removeInstance(id);
		}

		p &= ~TerrainGameObject.FLOWER_TYPE_MASK;
		p &= ~TerrainGameObject.FLOWER_ID_MASK;

		this.population[i] = p;
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

		final int instanceIndex = (p & TerrainGameObject.GRASS_ID_MASK) >>> TerrainGameObject.GRASS_ID_FIRST_BIT_INDEX;
		final int level = TerrainGameObject.getGrassLevelDirect(p);

		if (level == TerrainGameObject.GRASS_LEVEL_NONE) {
			return;
		}

		final boolean isDry = (p & TerrainGameObject.DRY_GRASS_BIT) != 0;

		final GrowingInstanceGameObject obj = isDry ? this.getDeadGrassObject(level) : this.getLivingGrassObject(level);

		if (obj != null) {
			obj.removeInstance(instanceIndex);
		}

		int np = p;
		np &= ~TerrainGameObject.GRASS_ID_MASK;
		np &= ~TerrainGameObject.GRASS_LEVEL_MASK;
		np &= ~TerrainGameObject.DRY_GRASS_BIT;

		this.population[i] = np;
	}

	public void addWater(final Vector2i tile, final float amount) {
		if (tile.x() < 0 || tile.x() >= this.width || tile.y() < 0 || tile.y() >= this.length) {
			return;
		}

		final int i = this.idx(tile.x(), tile.y());

		this.population[i] = TerrainGameObject.setWet(this.population[i], TerrainGameObject.WET_LEVEL_ON_WATER);

		if ((this.population[i] & TerrainGameObject.BUILT_BIT) != 0) {
			return;
		}

		int m = this.moisture[i] & 0xFF;
		m = Math.min(TerrainGameObject.MAX_MOISTURE, m + (int) (amount * TerrainGameObject.WATER_GROW_SPEED));
		this.moisture[i] = (byte) m;

		if (m >= TerrainGameObject.MOISTURE_FLOWER) {
			this.addFlower(tile);
		}
		if (m >= TerrainGameObject.MOISTURE_LARGE) {
			this.addGrass(tile, TerrainGameObject.GRASS_LEVEL_SMALL);
		} else if (m >= TerrainGameObject.MOISTURE_MEDIUM) {
			this.addGrass(tile, TerrainGameObject.GRASS_LEVEL_MEDIUM);
		} else if (m >= TerrainGameObject.MOISTURE_SMALL) {
			this.addGrass(tile, TerrainGameObject.GRASS_LEVEL_LARGE);
		}
		if (m >= TerrainGameObject.MOISTURE_GROWN) {
			this.getMesh().setGrown(tile, true);
			this.population[i] |= TerrainGameObject.GREEN_BIT;
		}
	}

	private void addFlower(final Vector2i tile) {
		if (tile.x() < 0 || tile.x() >= this.width || tile.y() < 0 || tile.y() >= this.length) {
			return;
		}

		final int i = this.idx(tile);
		int p = this.population[i];

		if ((p & TerrainGameObject.BUILT_BIT) != 0 || TerrainGameObject.getFlowerTypeDirect(p) != 0 || Math.random() >= 0.5
				|| this.flowerTypes.isEmpty()) {
			return;
		}

		if (Math.random() < 0.4) {
			p &= ~TerrainGameObject.FLOWER_TYPE_MASK;
			p |= TerrainGameObject.FLOWER_TYPE_PROHIBIT << TerrainGameObject.FLOWER_TYPE_FIRST_BIT_INDEX
					& TerrainGameObject.FLOWER_TYPE_MASK;

			this.population[i] = p;
			return;
		}
		if (Math.random() >= 0.5) {
			return;
		}

		final int flowerType = PCUtils.randomIntRange(0, Math.min(TerrainGameObject.FLOWER_TYPE_MAX_COUNT, this.flowerTypes.size()));
		final GrowingInstanceGameObject obj = this.flowerTypes.get(flowerType);
		if (obj == null) {
			return;
		}

		final int instanceId = obj.addInstance(this.getTilePosition(tile));

		// store flower type
		p &= ~TerrainGameObject.FLOWER_TYPE_MASK;
		p |= flowerType - TerrainGameObject.FLOWER_TYPE_OFFSET << TerrainGameObject.FLOWER_TYPE_FIRST_BIT_INDEX;

		// store instance id
		p &= ~TerrainGameObject.FLOWER_ID_MASK;
		p |= PCUtils.clamp(0, TerrainGameObject.FLOWER_ID_MAX, instanceId) << TerrainGameObject.FLOWER_ID_FIRST_BIT_INDEX
				& TerrainGameObject.FLOWER_ID_MASK;

		this.population[i] = p;
	}

	private OptionalInt getGrassLevel(final int i) {
		final int level = TerrainGameObject.getGrassLevelDirect(this.population[i]);
		return level == TerrainGameObject.GRASS_LEVEL_NONE ? OptionalInt.empty() : OptionalInt.of(level);
	}

	private static int getGrassLevelDirect(final int p) {
		return (p & TerrainGameObject.GRASS_LEVEL_MASK) >> TerrainGameObject.GRASS_LEVEL_FIRST_BIT_INDEX;
	}

	private static int setGrassLevelDirect(final int p, final int level) {
		return p & ~TerrainGameObject.GRASS_LEVEL_MASK | level << TerrainGameObject.GRASS_LEVEL_FIRST_BIT_INDEX;
	}

	private static int getFlowerTypeDirect(final int p) {
		return (p & TerrainGameObject.FLOWER_TYPE_MASK) >> TerrainGameObject.FLOWER_TYPE_FIRST_BIT_INDEX;
	}

	private void addGrass(final Vector2i tile, final int level) {
		if (tile.x() < 0 || tile.x() >= this.width || tile.y() < 0 || tile.y() >= this.length) {
			return;
		}

		final int i = this.idx(tile);

		if (level < TerrainGameObject.GRASS_LEVEL_SMALL || level > TerrainGameObject.GRASS_LEVEL_LARGE) {
			throw new IllegalArgumentException("Invalid grass level: " + level);
		}

		if ((this.population[i] & TerrainGameObject.BUILT_BIT) != 0) {
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

		final GrowingInstanceGameObject obj = this.getLivingGrassObject(level);
		instanceId = obj.addInstance(pos);

		int p = this.population[i];

		// clear dry
		p &= ~TerrainGameObject.DRY_GRASS_BIT;

		// set level
		p = TerrainGameObject.setGrassLevelDirect(p, level);

		// set id
		p &= ~TerrainGameObject.GRASS_ID_MASK;
		p |= PCUtils.clamp(0, TerrainGameObject.GRASS_ID_MAX, instanceId) << TerrainGameObject.GRASS_ID_FIRST_BIT_INDEX
				& TerrainGameObject.GRASS_ID_MASK;

		this.population[i] = p;

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

		int p = this.population[i];
		final int instanceIndex = (p & TerrainGameObject.GRASS_ID_MASK) >>> TerrainGameObject.GRASS_ID_FIRST_BIT_INDEX;

		// remove green
		GrowingInstanceGameObject grassObj = this.getLivingGrassObject(grass.getAsInt());
		grassObj.removeInstance(instanceIndex);

		// add dry
		grassObj = this.getDeadGrassObject(grass.getAsInt());
		final int newId = grassObj.addInstance(this.getTilePosition(tile));

		p |= TerrainGameObject.DRY_GRASS_BIT;

		p &= ~TerrainGameObject.GRASS_ID_MASK;
		p |= PCUtils.clamp(0, TerrainGameObject.GRASS_ID_MAX, newId) << TerrainGameObject.GRASS_ID_FIRST_BIT_INDEX
				& TerrainGameObject.GRASS_ID_MASK;

		this.population[i] = p;
	}

	private GrowingInstanceGameObject getDeadGrassObject(final int asInt) {
		return switch (asInt) {
		case GRASS_LEVEL_SMALL -> this.getDeadSmallGrassObject();
		case GRASS_LEVEL_MEDIUM -> this.getDeadMediumGrassObject();
		case GRASS_LEVEL_LARGE -> this.getDeadLargeGrassObject();
		default -> null;
		};
	}

	private GrowingInstanceGameObject getLivingGrassObject(final int asInt) {
		return switch (asInt) {
		case GRASS_LEVEL_SMALL -> this.getSmallGrassObject();
		case GRASS_LEVEL_MEDIUM -> this.getMediumGrassObject();
		case GRASS_LEVEL_LARGE -> this.getLargeGrassObject();
		default -> null;
		};
	}

	private static int getWet(final int p) {
		return (p & TerrainGameObject.WET_MASK) >> TerrainGameObject.WET_BIT_INDEX;
	}

	private static int setWet(final int p, final int v) {
		return p & ~TerrainGameObject.WET_MASK | (v & (1 << TerrainGameObject.WET_BITS) - 1) << TerrainGameObject.WET_BIT_INDEX;
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

	public TerrainData getData() {
		return new TerrainData(this.moisture, this.population);
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
