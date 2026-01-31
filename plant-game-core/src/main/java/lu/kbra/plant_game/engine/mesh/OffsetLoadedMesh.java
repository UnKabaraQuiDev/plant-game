package lu.kbra.plant_game.engine.mesh;

import java.util.Arrays;

import org.joml.Vector3f;

import lu.kbra.plant_game.engine.entity.impl.OffsetOwner;
import lu.kbra.standalone.gameengine.cache.attrib.UIntAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.Vec3fAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.impl.AttribArray;
import lu.kbra.standalone.gameengine.geom.LoadedMesh;
import lu.kbra.standalone.gameengine.graph.material.Material;

public class OffsetLoadedMesh extends LoadedMesh implements OffsetOwner {

	private Vector3f offset;

	public OffsetLoadedMesh(
			final String name,
			final Material material,
			final Vector3f offset,
			final Vec3fAttribArray vertices,
			final UIntAttribArray indices,
			final AttribArray... attribs) {
		super(name, material, vertices, indices, attribs);
		this.offset = offset;
	}

	@Override
	public Vector3f getOffset() {
		return this.offset;
	}

	@Override
	public void setOffset(final Vector3f origin) {
		this.offset = origin;
	}

	@Override
	public String toString() {
		return "OffsetMesh [offset=" + this.offset + ", name=" + this.name + ", vao=" + this.vao + ", vbo=" + this.vbo + ", material="
				+ this.material + ", vertices=" + this.vertices + ", indices=" + this.indices + ", attribs=" + Arrays.toString(this.attribs)
				+ ", vertexCount=" + this.vertexCount + ", indicesCount=" + this.indicesCount + ", isValid()=" + this.isValid() + "]";
	}

}
