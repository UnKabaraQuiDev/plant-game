package lu.kbra.plant_game.base.scene.menu.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import lu.kbra.pclib.logger.GlobalLogger;
import lu.kbra.standalone.gameengine.cache.attrib.Vec3fAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.impl.AttribArray;
import lu.kbra.standalone.gameengine.generated.gl_wrapper.GL_W;
import lu.kbra.standalone.gameengine.geom.BoundingBox;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.graph.material.Material;
import lu.kbra.standalone.gameengine.utils.GameEngineUtils;
import lu.kbra.standalone.gameengine.utils.gl.consts.BeginMode;

public class LoadedTriangleStripMesh implements Mesh {

	protected String name;
	protected int vao = -1;
	protected HashMap<Integer, Integer> vbo = new HashMap<>();
	protected Material material;

	protected Vec3fAttribArray vertices;
	protected List<AttribArray> attribs;

	protected int vertexCount;
	protected final BoundingBox boundingBox;

	public LoadedTriangleStripMesh(final String name, final Material material, final Vec3fAttribArray vertices,
			final AttribArray... attribs) {
		this.name = name;
		this.vertices = vertices;
		this.material = material;
		this.attribs = new ArrayList<>(Arrays.asList(attribs));

		this.vertexCount = vertices.getLength();
		this.boundingBox = GameEngineUtils.getBoundingBox(vertices);

		this.vao = GL_W.glGenVertexArrays();
		this.bind();
		vertices.setName(ATTRIB_VERTICES_NAME);
		vertices.setIndex(ATTRIB_VERTICES_ID);
		this.storeAttribArray(vertices);

		for (AttribArray a : attribs) {
			if (this.vbo.containsKey(a.getIndex())) {
				GlobalLogger.log(Level.WARNING, "Duplicate of index: " + a.getIndex() + " from " + a.getName() + ", in Mesh: " + name);
				continue;
			}
			this.storeAttribArray(a);
		}

		this.unbind();

		GlobalLogger.log(Level.INFO, "Mesh " + name + ": " + this.vao + " & " + this.vbo + "; v:" + this.vertexCount + " ");
	}

	@Override
	public BeginMode getBeginMode() {
		return BeginMode.TRIANGLE_STRIP;
	}

	@Override
	public List<AttribArray> getAttribs() {
		return this.attribs;
	}

	@Override
	public String getId() {
		return this.name;
	}

	@Override
	public void cleanup() {
		GlobalLogger.log("Cleaning up: " + this.name + " (" + this.vao + ")");

		if (this.vao == -1) {
			return;
		}

		GL_W.glDeleteVertexArrays(this.vao);
		this.attribs.forEach(AttribArray::cleanup);
		this.attribs = null;
		this.vbo = null;
		this.vao = -1;
	}

	@Override
	public int getGlId() {
		return this.vao;
	}

	@Override
	public boolean isValid() {
		return this.vao != -1;
	}

	@Override
	public int getVertexCount() {
		return this.vertexCount;
	}

	@Override
	public Map<Integer, Integer> getVbo() {
		return this.vbo;
	}

	@Override
	public int getIndicesCount() {
		return this.vertexCount;
	}

	@Override
	public Material getMaterial() {
		return this.material;
	}

	@Override
	public BoundingBox getBoundingBox() {
		return this.boundingBox;
	}

	@Override
	public boolean usesEBO() {
		return false;
	}

	@Override
	public String toString() {
		return "LoadedTriangleStripMesh@" + System.identityHashCode(this) + " [name=" + this.name + ", vao=" + this.vao + ", vbo="
				+ this.vbo + ", material=" + this.material + ", vertices=" + this.vertices + ", attribs=" + this.attribs + ", vertexCount="
				+ this.vertexCount + ", boundingBox=" + this.boundingBox + "]";
	}

}
