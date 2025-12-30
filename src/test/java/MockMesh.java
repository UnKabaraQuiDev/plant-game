import java.util.Map;

import lu.kbra.standalone.gameengine.cache.attrib.impl.AttribArray;
import lu.kbra.standalone.gameengine.geom.BoundingBox;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.graph.material.Material;

public class MockMesh implements Mesh {

	protected String id;
	protected int vertexCount, indexsCount;
	protected BoundingBox bb;

	public MockMesh(final String id, final int vertexCount, final int indexsCount, final BoundingBox bb) {
		this.id = id;
		this.vertexCount = vertexCount;
		this.indexsCount = indexsCount;
		this.bb = bb;
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public void cleanup() {

	}

	@Override
	public int getGlId() {
		return 0;
	}

	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public void addAttribArray(final AttribArray data) {

	}

	@Override
	public void bind() {

	}

	@Override
	public void unbind() {

	}

	@Override
	public int getVertexCount() {
		return this.vertexCount;
	}

	@Override
	public Map<Integer, Integer> getVbo() {
		return Map.of();
	}

	@Override
	public int getIndicesCount() {
		return this.indexsCount;
	}

	@Override
	public Material getMaterial() {
		return null;
	}

	@Override
	public BoundingBox getBoundingBox() {
		return this.bb;
	}

}
