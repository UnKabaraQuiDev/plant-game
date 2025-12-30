package lu.kbra.plant_game.engine.entity.go.mesh.path;

import org.joml.Vector3f;

import lu.kbra.standalone.gameengine.cache.attrib.UIntAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.Vec3fAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.impl.AttribArray;
import lu.kbra.standalone.gameengine.geom.LineMesh;
import lu.kbra.standalone.gameengine.geom.LoadedMesh;
import lu.kbra.standalone.gameengine.utils.gl.consts.BeginMode;
import lu.kbra.standalone.gameengine.utils.gl.consts.PolygonDrawMode;
import lu.kbra.standalone.gameengine.utils.gl.consts.PolygonMode;

public class LineLoadedMesh extends LoadedMesh implements LineMesh {

	public static final int BUFFER_GROW_SIZE = 16;

	private int objectId;
	private int effectiveLength = 0;
	private float lineWidth = 12;

	public LineLoadedMesh(
			final String name,
			final int objectId,
			final Vec3fAttribArray vertices,
			final UIntAttribArray indices,
			final AttribArray... attribs) {
		super(name, null, vertices, indices, attribs);
		this.objectId = objectId;
	}

	public void addPoint(final Vector3f v) {
		int[] indices = super.indices.getData();
		Vector3f[] verts = super.vertices.getData();

		if (indices.length <= this.effectiveLength + 1 || verts.length <= this.effectiveLength + 1) {
			final int[] newIndices = new int[indices.length + BUFFER_GROW_SIZE];
			final Vector3f[] newVerts = new Vector3f[verts.length + BUFFER_GROW_SIZE];

			System.arraycopy(indices, 0, newIndices, 0, indices.length);
			System.arraycopy(verts, 0, newVerts, 0, verts.length);

			super.indices.resize(newIndices);
			super.vertices.resize(newVerts);

			indices = newIndices;
			verts = newVerts;

			super.vertexCount = verts.length;
			super.indicesCount = indices.length;
		}

		verts[this.effectiveLength + 0] = verts[this.effectiveLength - 1];
		verts[this.effectiveLength + 1] = v;

		indices[this.effectiveLength + 0] = this.effectiveLength;
		indices[this.effectiveLength + 1] = this.effectiveLength + 1;

		this.effectiveLength += 2;
	}

	public void setEffectiveLength(final int effectiveLength) {
		this.effectiveLength = effectiveLength;
	}

	@Override
	public int getIndicesCount() {
		return this.effectiveLength;
	}

	@Override
	public float getLineWidth() {
		return this.lineWidth;
	}

	@Override
	public PolygonDrawMode getPolygonDrawMode() {
		return PolygonDrawMode.LINE;
	}

	@Override
	public PolygonMode getPolygonMode() {
		return PolygonMode.FRONT_AND_BACK;
	}

	@Override
	public BeginMode getBeginMode() {
		return BeginMode.LINES;
	}

	public void setLineWidth(final float lineWidth) {
		this.lineWidth = lineWidth;
	}

	public int getObjectId() {
		return this.objectId;
	}

	public void setObjectId(final int objectId) {
		this.objectId = objectId;
	}

}
