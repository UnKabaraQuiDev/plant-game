package lu.kbra.plant_game.engine.mesh;

import java.util.Arrays;

import lu.kbra.plant_game.engine.render.SwayMesh;
import lu.kbra.standalone.gameengine.cache.attrib.UIntAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.Vec3fAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.impl.AttribArray;
import lu.kbra.standalone.gameengine.geom.LoadedMesh;
import lu.kbra.standalone.gameengine.graph.material.Material;

public class SwayLoadedMesh extends LoadedMesh implements SwayMesh {

	private float deformRatio = 1f;
	private float speedRatio = 1f;
	private float scaleRatio = 0.1f;

	public SwayLoadedMesh(
			final String name,
			final Material material,
			final float deformRatio,
			final float speedRatio,
			final Vec3fAttribArray vertices,
			final UIntAttribArray indices,
			final AttribArray... attribs) {
		super(name, material, vertices, indices, attribs);
		this.deformRatio = deformRatio;
		this.speedRatio = speedRatio;
	}

	@Override
	public float getDeformRatio() {
		return this.deformRatio;
	}

	@Override
	public float getSpeedRatio() {
		return this.speedRatio;
	}

	@Override
	public float getScaleRatio() {
		return this.scaleRatio;
	}

	@Override
	public void setDeformRatio(final float deformRatio) {
		this.deformRatio = deformRatio;
	}

	@Override
	public void setSpeedRatio(final float speedRatio) {
		this.speedRatio = speedRatio;
	}

	@Override
	public void setScaleRatio(final float scaleRatio) {
		this.scaleRatio = scaleRatio;
	}

	@Override
	public String toString() {
		return "SwayLoadedMesh [deformRatio=" + this.deformRatio + ", speedRatio=" + this.speedRatio + ", scaleRatio=" + this.scaleRatio
				+ ", name=" + this.name + ", vao=" + this.vao + ", vbo=" + this.vbo + ", material=" + this.material + ", vertices="
				+ this.vertices + ", indices=" + this.indices + ", attribs=" + Arrays.toString(this.attribs) + ", vertexCount="
				+ this.vertexCount + ", indicesCount=" + this.indicesCount + ", boundingBox=" + this.boundingBox + ", isValid()="
				+ this.isValid() + "]";
	}

}
