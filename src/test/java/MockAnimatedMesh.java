import java.util.Map;

import lu.kbra.plant_game.engine.mesh.AnimatedMesh;
import lu.kbra.plant_game.engine.mesh.loader.AnimatedMeshLoader.AnimationData;
import lu.kbra.standalone.gameengine.cache.attrib.AttribArray;
import lu.kbra.standalone.gameengine.geom.BoundingBox;
import lu.kbra.standalone.gameengine.graph.material.Material;

public class MockAnimatedMesh implements AnimatedMesh {

	protected String id;
	protected int vertexCount, indexsCount;
	protected BoundingBox bb;
	protected AnimationData animationData;

	public MockAnimatedMesh(final String id, final int vertexCount, final int indexsCount, final BoundingBox bb) {
		this.id = id;
		this.vertexCount = vertexCount;
		this.indexsCount = indexsCount;
		this.bb = bb;
	}

	public MockAnimatedMesh(
			final String id,
			final int vertexCount,
			final int indexsCount,
			final BoundingBox bb,
			final AnimationData animationData) {
		this.id = id;
		this.vertexCount = vertexCount;
		this.indexsCount = indexsCount;
		this.bb = bb;
		this.animationData = animationData;
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

	@Override
	public AnimationData getAnimation() {
		return this.animationData;
	}

	@Override
	public void setAnimation(final AnimationData a) {
		this.animationData = a;
	}

}
