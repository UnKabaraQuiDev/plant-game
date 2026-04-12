package lu.kbra.plant_game.base.entity.go.obj.water;

import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector2ic;

import lu.kbra.pclib.PCUtils;
import lu.kbra.plant_game.engine.entity.go.data.Footprint;
import lu.kbra.plant_game.engine.entity.go.data.QuadFootprint;
import lu.kbra.plant_game.engine.entity.go.obj.PlaceableAnimatedGameObject;
import lu.kbra.plant_game.engine.mesh.AnimatedMesh;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.utils.consts.Direction;

@DataPath("classpath:/models/water-sprinkler-5x5.json")
public class WaterSprinklerObject5x5 extends PlaceableAnimatedGameObject implements SprinklerObject {

	protected static Footprint wateringFootprint = new QuadFootprint(new Vector2i(-2, -2), new Vector2i(2, 2), true);
	protected static Vector2ic[][] cachedOffsets = new Vector2ic[Direction.COUNT][];
	private static final int ROTATION_SPEED = 50;

	protected int currentTileIndex = PCUtils.randomIntRange(0, wateringFootprint.getCellCount());
	protected boolean working = false;

	public WaterSprinklerObject5x5(final String str, final Mesh mesh, final AnimatedMesh animatedMesh) {
		super(str, mesh, animatedMesh);
	}

	@Override
	public Matrix4f computeAnimatedTransform(final float t) {
		this.animatedTransform.rotateY(this.working ? (float) Math.toRadians(t * ROTATION_SPEED) : 0);
		return this.animatedTransform;
	}

	@Override
	public int getCurrentTileIndex() {
		return this.currentTileIndex;
	}

	@Override
	public void setCurrentTileIndex(final int currentTileIndex) {
		this.currentTileIndex = currentTileIndex;
	}

	@Override
	public Footprint getWateringFootprint() {
		return wateringFootprint;
	}

	@Override
	public Vector2ic[][] getCachedOffsets() {
		return cachedOffsets;
	}

	@Override
	public float getMinSprinkledWater() {
		return 1.5f;
	}

	@Override
	public float getMaxSprinkledWater() {
		return 3f;
	}

	@Override
	public int getRandomTickDuration() {
		return DEFAULT_RANDOM_TICK_DURATION / 3 * 2;
	}

	@Override
	public float getConsumedEnergy() {
		return 2.7f;
	}

	@Override
	public boolean isWorking() {
		return this.working;
	}

	@Override
	public void setWorking(final boolean working) {
		this.working = working;
	}

}
