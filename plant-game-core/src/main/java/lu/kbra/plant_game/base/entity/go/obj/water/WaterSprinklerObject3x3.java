package lu.kbra.plant_game.base.entity.go.obj.water;

import org.joml.Matrix4f;
import org.joml.Vector2i;

import lu.kbra.plant_game.engine.entity.go.obj.PlaceableAnimatedGameObject;
import lu.kbra.plant_game.engine.entity.go.obj.terrain.TerrainGameObject;
import lu.kbra.plant_game.engine.mesh.AnimatedMesh;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.utils.consts.Direction;

@DataPath("classpath:/models/water-sprinkler-3x3.json")
public class WaterSprinklerObject3x3 extends PlaceableAnimatedGameObject {

	public WaterSprinklerObject3x3(final String str, final Mesh mesh, final AnimatedMesh animatedMesh) {
		super(str, mesh, animatedMesh);
	}

	@Override
	public void confirmPlaceDown(
			final TerrainGameObject terrain,
			final Vector2i source,
			final Direction sourceRotation,
			final Vector2i currentPos,
			final Direction targetRotation) {
		terrain.addRandomTickTile(currentPos);
		terrain.addRandomTickTile(currentPos.x, currentPos.y + 1);
		terrain.addRandomTickTile(currentPos.x, currentPos.y - 1);
		terrain.addRandomTickTile(currentPos.x + 1, currentPos.y);
		terrain.addRandomTickTile(currentPos.x - 1, currentPos.y);
		terrain.addRandomTickTile(currentPos.x + 1, currentPos.y + 1);
		terrain.addRandomTickTile(currentPos.x - 1, currentPos.y - 1);
		terrain.addRandomTickTile(currentPos.x + 1, currentPos.y - 1);
		terrain.addRandomTickTile(currentPos.x - 1, currentPos.y + 1);
	}

	@Override
	public Matrix4f computeAnimatedTransform(final float t) {
		this.getTransform()
				.getMatrix()
				.mul(this.animatedTransform.identity().rotateY((float) Math.toRadians(t * 12)), this.animatedTransform);
		return this.animatedTransform;
	}

}
