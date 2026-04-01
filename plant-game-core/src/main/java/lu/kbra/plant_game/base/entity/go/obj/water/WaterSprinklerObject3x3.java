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

@DataPath("classpath:/models/water-sprinkler-3x3.json")
public class WaterSprinklerObject3x3 extends PlaceableAnimatedGameObject implements SprinklerObject {

	protected static Footprint wateringFootprint = new QuadFootprint(new Vector2i(-1, -1), new Vector2i(1, 1), true);
	protected static Vector2ic[][] cachedOffsets = new Vector2ic[Direction.COUNT][];
	private static final int ROTATION_SPEED = 12;

	protected int currentTileIndex = PCUtils.randomIntRange(0, wateringFootprint.getCellCount());

	public WaterSprinklerObject3x3(final String str, final Mesh mesh, final AnimatedMesh animatedMesh) {
		super(str, mesh, animatedMesh);
	}

	@Override
	public Matrix4f computeAnimatedTransform(final float t) {
		this.getTransform()
				.getMatrix()
				.mul(this.animatedTransform.identity().rotateY((float) Math.toRadians(t * ROTATION_SPEED)), this.animatedTransform);
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
	public int getMinSprinkledWater() {
		return 2;
	}

	@Override
	public int getMaxSprinkledWater() {
		return 8;
	}

}
