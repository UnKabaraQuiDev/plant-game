package lu.kbra.plant_game.base.entity.go.obj.energy;

import org.joml.Matrix4f;
import org.joml.Vector2i;

import lu.kbra.plant_game.PGLogic;
import lu.kbra.plant_game.base.data.DefaultResourceType;
import lu.kbra.plant_game.base.entity.go.obj.water.NeedsRandomTick;
import lu.kbra.plant_game.engine.entity.go.impl.WaterContainer;
import lu.kbra.plant_game.engine.entity.go.obj.FootprintComputeMethod;
import lu.kbra.plant_game.engine.entity.go.obj.PlaceableAnimatedGameObject;
import lu.kbra.plant_game.engine.mesh.AnimatedMesh;
import lu.kbra.plant_game.engine.scene.world.WorldLevelScene;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.utils.consts.Direction;

@DataPath("classpath:/models/water-wheel-small.json")
public class WaterWheelSmallObject extends PlaceableAnimatedGameObject implements WaterContainer, NeedsRandomTick {

	protected long waterLevel;

	public WaterWheelSmallObject(final String str, final Mesh mesh, final AnimatedMesh animatedMesh) {
		super(str, mesh, animatedMesh);
	}

	@Override
	public void randomTick(final WindowInputHandler inputHandler, final WorldLevelScene worldLevelScene) {
		PGLogic.INSTANCE.getGameData().getResources().compute(DefaultResourceType.ENERGY, (k, v) -> v + 1);
	}

	@Override
	protected FootprintComputeMethod getStaticMeshFootprintComputeMethod() {
		return FootprintComputeMethod.MINIMAL;
	}

	@Override
	public Matrix4f computeAnimatedTransform(final float t) {
		this.getTransform()
				.getMatrix()
				.mul(this.animatedTransform.identity().rotateZ((float) Math.toRadians(t * 25)), this.animatedTransform);
		return this.animatedTransform;
	}

	@Override
	public boolean isPlaceable(final WorldLevelScene scene, final Vector2i tile, final Direction rotation) {
		final float waterLevel = scene.getWaterHeight();

		return super.getAnimatedMeshFootprint()
				.allCellsMatch(tile, rotation, v -> (scene.getTerrain().getMesh().getCellHeight(v) < waterLevel))
				&& super.getStaticMeshFootprint()
						.allCellsMatch(tile, rotation, v -> (scene.getTerrain().getMesh().getCellHeight(v) == (int) Math.ceil(waterLevel)));
	}

	@Override
	public long getWater() {
		return this.waterLevel;
	}

	@Override
	public boolean hasWater(final long val) {
		return this.waterLevel >= val;
	}

	@Override
	public void addWater(final long val) {
		this.waterLevel += val;
	}

	@Override
	public void removeWater(final long val) {
		this.waterLevel -= val;
	}

}
