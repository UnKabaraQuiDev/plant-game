package lu.kbra.plant_game.engine.entity.go.obj;

import org.joml.Vector2i;
import org.joml.Vector3f;

import lu.kbra.plant_game.engine.entity.go.impl.Footprint;
import lu.kbra.plant_game.engine.entity.go.impl.FootprintOwner;
import lu.kbra.plant_game.engine.entity.impl.MeshOwner;
import lu.kbra.standalone.gameengine.geom.BoundingBox;
import lu.kbra.standalone.gameengine.geom.Mesh;

public interface StaticMeshFootprintOwner extends MeshOwner, FootprintOwner {

	Footprint getStaticMeshFootprint();

	static Footprint computeMeshFootprint(final FootprintComputeMethod method, final Mesh mesh) {
		return new Footprint(computeMeshOffset(method, mesh), computeMeshSize(method, mesh));
	}

	static Vector2i computeMeshSize(final FootprintComputeMethod method, final Mesh mesh) {
		final BoundingBox bb = mesh.getBoundingBox();

		final int minX;
		final int minZ;
		final int maxX;
		final int maxZ;
		final Vector3f min = bb.getMin();
		final Vector3f max = bb.getMax();

		assert min.min(max, new Vector3f()).equals(min, 0.001f) : "Min isn't min " + min + "; " + max;
		assert max.max(min, new Vector3f()).equals(max, 0.001f) : "Max isn't max" + min + "; " + max;

		switch (method) {
		case MINIMAL -> {
			minX = max(min.x());
			minZ = max(min.z());
			maxX = min(max.x());
			maxZ = min(max.z());
		}
		default -> {
			minX = Math.round(min.x());
			minZ = Math.round(min.z());
			maxX = Math.round(max.x());
			maxZ = Math.round(max.z());
		}
		case MAXIMAL -> {
			minX = min(min.x());
			minZ = min(min.z());
			maxX = max(max.x());
			maxZ = max(max.x());
		}
		}

		System.err.println(maxX + " - " + minX + ", " + maxZ + " - " + minZ);

		final int w = Math.abs(maxX - minX);
		final int h = Math.abs(maxZ - minZ);

		return new Vector2i(w == 0 ? 1 : w, h == 0 ? 1 : h);
	}

	static int min(final float x) {
		return x < 0 ? awayFromZero(x) : towardsZero(x);
	}

	static int max(final float x) {
		return x > 0 ? awayFromZero(x) : towardsZero(x);
	}

	static int towardsZero(final float x) {
		return (int) x;
	}

	static int awayFromZero(final float x) {
		return (int) Math.copySign(Math.ceil(Math.abs(x)), x);
	}

	static Vector2i computeMeshOffset(final FootprintComputeMethod method, final Mesh mesh) {
		final BoundingBox bb = mesh.getBoundingBox();

		final int minX;
		final int minZ;
		final Vector3f min = bb.getMin();

		switch (method) {
		case MINIMAL -> {
			minX = max(min.x());
			minZ = max(min.z());
		}
		default -> {
			minX = Math.round(min.x());
			minZ = Math.round(min.z());
		}
		case MAXIMAL -> {
			minX = min(min.x());
			minZ = min(min.z());
		}
		}

		return new Vector2i(-minX, -minZ);
	}

}
