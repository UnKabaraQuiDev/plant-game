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
		final BoundingBox bb = mesh.getBoundingBox();

		int minX;
		int minZ;
		int maxX;
		int maxZ;
		final Vector3f min = bb.getMin();
		final Vector3f max = bb.getMax();
		final Vector3f center = bb.getCenter();

		assert min.min(max, new Vector3f()).equals(min, 0.001f) : "Min isn't min " + min + "; " + max;
		assert max.max(min, new Vector3f()).equals(max, 0.001f) : "Max isn't max" + min + "; " + max;

		switch (method) {
		case MAXIMAL -> {
			minX = floor(min.x);
			minZ = floor(min.z);
			maxX = ceil(max.x);
			maxZ = ceil(max.z);
		}
		case MINIMAL -> {
			minX = ceil(min.x);
			minZ = ceil(min.z);
			maxX = floor(max.x);
			maxZ = floor(max.z);
		}
		default -> {
			minX = Math.round(min.x);
			minZ = Math.round(min.z);
			maxX = Math.round(max.x);
			maxZ = Math.round(max.z);
		}
		}
		if (minX >= maxX) {
			minX = Math.round(center.x());
			maxX = minX + 1;
		}
		if (minZ >= maxZ) {
			minZ = Math.round(center.z());
			maxZ = minZ + 1;
		}

		return new Footprint(new Vector2i(minX, minZ), new Vector2i(maxX, maxZ));
	}

//	static int min(final float x) {
//		return x < 0 ? awayFromZero(x) : towardsZero(x);
//	}
//
//	static int max(final float x) {
//		return x > 0 ? awayFromZero(x) : towardsZero(x);
//	}

	static int floor(final float x) {
		return (int) Math.floor(x);
	}

	static int ceil(final float x) {
		return (int) Math.ceil(x);
	}

//	static int towardsZero(final float x) {
//		return (int) x;
//	}
//
//	static int awayFromZero(final float x) {
//		return (int) Math.copySign(Math.ceil(Math.abs(x)), x);
//	}

}
