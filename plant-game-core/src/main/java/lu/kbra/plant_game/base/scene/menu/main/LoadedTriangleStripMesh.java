package lu.kbra.plant_game.base.scene.menu.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import lu.kbra.pclib.logger.GlobalLogger;
import lu.kbra.standalone.gameengine.cache.attrib.Vec3fAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.impl.JavaAttribArray;
import lu.kbra.standalone.gameengine.generated.gl_wrapper.GL_W;
import lu.kbra.standalone.gameengine.geom.BoundingBox;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.graph.material.Material;
import lu.kbra.standalone.gameengine.utils.GameEngineUtils;
import lu.kbra.standalone.gameengine.utils.gl.consts.BeginMode;

@Deprecated
public class LoadedTriangleStripMesh implements Mesh {

	@Deprecated
	protected String name;
	@Deprecated
	protected int vao = -1;
	@Deprecated
	protected HashMap<Integer, Integer> vbo = new HashMap<>();
	@Deprecated
	protected Material material;

	@Deprecated
	protected Vec3fAttribArray vertices;
	@Deprecated
	protected List<JavaAttribArray> attribs;

	@Deprecated
	protected int vertexCount;
	@Deprecated
	protected final BoundingBox boundingBox;

	@Deprecated
	public LoadedTriangleStripMesh(
			final String name,
			final Material material,
			final Vec3fAttribArray vertices,
			final JavaAttribArray... attribs) {
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

		for (JavaAttribArray a : attribs) {
			if (this.vbo.containsKey(a.getIndex())) {
				GlobalLogger.log(Level.WARNING, "Duplicate of index: " + a.getIndex() + " from " + a.getName() + ", in Mesh: " + name);
				continue;
			}
			this.storeAttribArray(a);
		}

		this.unbind();

		GlobalLogger.log(Level.INFO, "Mesh " + name + ": " + this.vao + " & " + this.vbo + "; v:" + this.vertexCount + " ");
	}

	@Deprecated
	@Override
	public BeginMode getBeginMode() {
		return BeginMode.TRIANGLE_STRIP;
	}

	@Deprecated
	@Override
	public List<JavaAttribArray> getAttribs() {
		return this.attribs;
	}

	@Deprecated
	@Override
	public String getId() {
		return this.name;
	}

	@Deprecated
	@Override
	public void cleanup() {
		if (this.vao == -1) {
			return;
		}

		GlobalLogger.log("Cleaning up: " + this.name + " (" + this.vao + ")");

		GL_W.glDeleteVertexArrays(this.vao);
		this.attribs.forEach(JavaAttribArray::cleanup);
		this.attribs = null;
		this.vbo = null;
		this.vao = -1;
	}

	@Deprecated
	@Override
	public int getGlId() {
		return this.vao;
	}

	@Deprecated
	@Override
	public boolean isValid() {
		return this.vao != -1;
	}

	@Deprecated
	@Override
	public int getVertexCount() {
		return this.vertexCount;
	}

	@Deprecated
	@Override
	public Map<Integer, Integer> getVbo() {
		return this.vbo;
	}

	@Deprecated
	@Override
	public int getIndicesCount() {
		return this.vertexCount;
	}

	@Deprecated
	@Override
	public Material getMaterial() {
		return this.material;
	}

	@Deprecated
	@Override
	public BoundingBox getBoundingBox() {
		return this.boundingBox;
	}

	@Deprecated
	@Override
	public boolean usesEBO() {
		return false;
	}

	@Deprecated
	@Override
	public String toString() {
		return "LoadedTriangleStripMesh@" + System.identityHashCode(this) + " [name=" + this.name + ", vao=" + this.vao + ", vbo="
				+ this.vbo + ", material=" + this.material + ", vertices=" + this.vertices + ", attribs=" + this.attribs + ", vertexCount="
				+ this.vertexCount + ", boundingBox=" + this.boundingBox + "]";
	}

}
