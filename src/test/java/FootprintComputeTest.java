import static lu.kbra.plant_game.engine.entity.go.obj.FootprintComputeMethod.CLOSEST;
import static lu.kbra.plant_game.engine.entity.go.obj.FootprintComputeMethod.MAXIMAL;
import static lu.kbra.plant_game.engine.entity.go.obj.FootprintComputeMethod.MINIMAL;
import static lu.kbra.plant_game.engine.entity.go.obj.StaticMeshFootprintOwner.computeMeshOffset;
import static lu.kbra.plant_game.engine.entity.go.obj.StaticMeshFootprintOwner.computeMeshSize;

import org.joml.Vector3f;
import org.junit.Test;

import lu.kbra.plant_game.engine.mesh.AnimatedMesh;
import lu.kbra.standalone.gameengine.geom.BoundingBox;

public class FootprintComputeTest {

	@Test
	public void animatedSubOne() {
		final AnimatedMesh mesh = new MockAnimatedMesh(
				"id",
				4,
				10,
				new BoundingBox(new Vector3f(0.1f, 0.1f, 0.1f), new Vector3f(0.8f, 0.8f, 0.8f)));

		assert computeMeshSize(MAXIMAL, mesh).equals(1, 1) : computeMeshSize(MAXIMAL, mesh);
		assert computeMeshSize(MINIMAL, mesh).equals(1, 1) : computeMeshSize(MINIMAL, mesh);
		assert computeMeshSize(CLOSEST, mesh).equals(1, 1) : computeMeshSize(CLOSEST, mesh);

		assert computeMeshOffset(MAXIMAL, mesh).equals(0, 0) : computeMeshOffset(MAXIMAL, mesh);
		assert computeMeshOffset(MINIMAL, mesh).equals(-1, -1) : computeMeshOffset(MINIMAL, mesh);
		assert computeMeshOffset(CLOSEST, mesh).equals(0, 0) : computeMeshOffset(CLOSEST, mesh);
	}

	@Test
	public void staticOverOne() {
		final AnimatedMesh mesh = new MockAnimatedMesh(
				"id",
				4,
				10,
				new BoundingBox(new Vector3f(0.1f, 0.1f, 0.1f), new Vector3f(2.1f, 2.1f, 2.1f)));

		assert computeMeshSize(MAXIMAL, mesh).equals(3, 3) : computeMeshSize(MAXIMAL, mesh);
		assert computeMeshSize(MINIMAL, mesh).equals(1, 1) : computeMeshSize(MINIMAL, mesh);
		assert computeMeshSize(CLOSEST, mesh).equals(2, 2) : computeMeshSize(CLOSEST, mesh);

		assert computeMeshOffset(MAXIMAL, mesh).equals(0, 0) : computeMeshOffset(MAXIMAL, mesh);
		assert computeMeshOffset(MINIMAL, mesh).equals(-1, -1) : computeMeshOffset(MINIMAL, mesh);
		assert computeMeshOffset(CLOSEST, mesh).equals(0, 0) : computeMeshOffset(CLOSEST, mesh);
	}

	@Test
	public void staticBiPolar() {
		final AnimatedMesh mesh = new MockAnimatedMesh(
				"id",
				4,
				10,
				new BoundingBox(new Vector3f(0.9f, 0.9f, 0.9f).negate(), new Vector3f(2.1f, 2.1f, 2.1f)));

		assert computeMeshSize(MAXIMAL, mesh).equals(4, 4) : computeMeshSize(MAXIMAL, mesh);
		assert computeMeshSize(MINIMAL, mesh).equals(2, 2) : computeMeshSize(MINIMAL, mesh);
		assert computeMeshSize(CLOSEST, mesh).equals(3, 3) : computeMeshSize(CLOSEST, mesh);

		assert computeMeshOffset(MAXIMAL, mesh).equals(1, 1) : computeMeshOffset(MAXIMAL, mesh);
		assert computeMeshOffset(MINIMAL, mesh).equals(0, 0) : computeMeshOffset(MINIMAL, mesh);
		assert computeMeshOffset(CLOSEST, mesh).equals(1, 1) : computeMeshOffset(CLOSEST, mesh);
	}

}
