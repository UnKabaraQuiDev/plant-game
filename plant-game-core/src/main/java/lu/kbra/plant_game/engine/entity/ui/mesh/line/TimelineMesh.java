package lu.kbra.plant_game.engine.entity.ui.mesh.line;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4fc;

import lu.kbra.plant_game.engine.entity.go.mesh.path.LineLoadedMesh;
import lu.kbra.plant_game.engine.entity.impl.TintOwner;
import lu.kbra.plant_game.engine.mesh.TexturedMesh;
import lu.kbra.plant_game.generated.ColorMaterial;
import lu.kbra.standalone.gameengine.cache.attrib.UIntAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.Vec3fAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.impl.JavaAttribArray;
import lu.kbra.standalone.gameengine.graph.texture.SingleTexture;

public class TimelineMesh extends LineLoadedMesh implements TexturedMesh, TintOwner {

	private ColorMaterial color = ColorMaterial.GREEN;
	private SingleTexture txt;
	private float defaultYDepth = -0.1f;

	public TimelineMesh(
			final String name,
			final int objectId,
			final SingleTexture txt,
			final Vec3fAttribArray vertices,
			final UIntAttribArray indices,
			final JavaAttribArray... attribs) {
		super(name, objectId, vertices, indices, attribs);
		this.txt = txt;
	}

	public TimelineMesh(
			final String name,
			final int objectId,
			final SingleTexture txt,
			final Vec3fAttribArray vertices,
			final UIntAttribArray indices,
			final float defaultYDepth,
			final JavaAttribArray... attribs) {
		super(name, objectId, vertices, indices, attribs);
		this.defaultYDepth = defaultYDepth;
		this.txt = txt;
	}

	public void addPoint(final Vector2f v) {
		super.addPoint(new Vector3f(v.x, this.defaultYDepth, v.y));
	}

	public ColorMaterial getColor() {
		return this.color;
	}

	public void setColor(final ColorMaterial color) {
		this.color = color;
	}

	@Override
	public Vector4fc getTint() {
		return this.color.getColor();
	}

	@Override
	public void setTint(final Vector4fc tint) {
		throw new UnsupportedOperationException();
	}

	public float getDefaultYDepth() {
		return this.defaultYDepth;
	}

	public void setDefaultYDepth(final float defaultYDepth) {
		this.defaultYDepth = defaultYDepth;
	}

	@Override
	public SingleTexture getTexture() {
		return this.txt;
	}

	@Override
	public void setTexture(final SingleTexture texture) {
		this.txt = texture;
	}

	@Override
	public String toString() {
		return "TimelineMesh@" + System.identityHashCode(this) + " [color=" + this.color + ", txt=" + this.txt + ", defaultYDepth="
				+ this.defaultYDepth + ", name=" + this.name + ", vao=" + this.vao + ", vbo=" + this.vbo + ", material=" + this.material
				+ ", vertices=" + this.vertices + ", indices=" + this.indices + ", attribs=" + this.attribs + ", vertexCount="
				+ this.vertexCount + ", indicesCount=" + this.indicesCount + ", boundingBox=" + this.boundingBox + "]";
	}

}
