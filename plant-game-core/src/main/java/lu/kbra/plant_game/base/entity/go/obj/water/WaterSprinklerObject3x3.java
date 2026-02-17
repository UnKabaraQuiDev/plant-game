package lu.kbra.plant_game.base.entity.go.obj.water;

import org.joml.Matrix4f;
import org.joml.Vector2i;

import lu.kbra.pclib.PCUtils;
import lu.kbra.plant_game.engine.entity.go.obj.PlaceableAnimatedGameObject;
import lu.kbra.plant_game.engine.mesh.AnimatedMesh;
import lu.kbra.plant_game.engine.scene.world.WorldLevelScene;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.standalone.gameengine.geom.Mesh;

@DataPath("classpath:/models/water-sprinkler-3x3.json")
public class WaterSprinklerObject3x3 extends PlaceableAnimatedGameObject implements NeedsRandomTick {

	private static final int ROTATION_SPEED = 12;

	public WaterSprinklerObject3x3(final String str, final Mesh mesh, final AnimatedMesh animatedMesh) {
		super(str, mesh, animatedMesh);
	}

	@Override
	public void randomTick(final WindowInputHandler inputHandler, final WorldLevelScene worldLevelScene) {
		if (this.tile == null) {
			return;
		}

		final int x = PCUtils.randomIntRange(-2, 2);
		final int z = PCUtils.randomIntRange(-2, 2);
//		worldLevelScene.getTerrain().getMesh().setGrown(this.getTile().add(x, z, new Vector2i()), true);
		worldLevelScene.getTerrain().addWater(this.getTile().add(x, z, new Vector2i()), PCUtils.randomFloatRange(0.33f, 0.67f));
	}

	@Override
	public Matrix4f computeAnimatedTransform(final float t) {
		this.getTransform()
				.getMatrix()
				.mul(this.animatedTransform.identity().rotateY((float) Math.toRadians(t * ROTATION_SPEED)), this.animatedTransform);
		return this.animatedTransform;
	}

}
