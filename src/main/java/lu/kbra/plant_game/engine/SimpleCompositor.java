package lu.kbra.plant_game.engine;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector4f;

import lu.pcy113.pclib.logger.GlobalLogger;

import lu.kbra.plant_game.engine.shader.TransferShader;
import lu.kbra.plant_game.engine.shader.TransferShader.TransferMaterial;
import lu.kbra.standalone.gameengine.GameEngine;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.cache.attrib.UIntAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.Vec2fAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.Vec3fAttribArray;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.graph.MaterialFactory;
import lu.kbra.standalone.gameengine.graph.composition.layer.PassRenderLayer;
import lu.kbra.standalone.gameengine.graph.composition.layer.RenderLayer;
import lu.kbra.standalone.gameengine.graph.render.Scene3DRenderer;
import lu.kbra.standalone.gameengine.graph.shader.RenderShader;
import lu.kbra.standalone.gameengine.impl.Cleanupable;
import lu.kbra.standalone.gameengine.objs.entity.Entity;
import lu.kbra.standalone.gameengine.objs.entity.components.MeshComponent;
import lu.kbra.standalone.gameengine.objs.entity.components.RenderConditionComponent;
import lu.kbra.standalone.gameengine.objs.entity.components.TransformComponent;
import lu.kbra.standalone.gameengine.scene.Scene2D;
import lu.kbra.standalone.gameengine.scene.Scene3D;
import lu.kbra.standalone.gameengine.scene.camera.Camera;
import lu.kbra.standalone.gameengine.scene.camera.Camera3D;
import lu.kbra.standalone.gameengine.utils.gl.wrapper.GL_W;

public class SimpleCompositor implements Cleanupable {

	public static final String SCREEN_WIDTH = "screen_width";
	public static final String SCREEN_HEIGHT = "screen_height";

	private static Mesh SCREEN = new Mesh("PASS_SCREEN", null,
			new Vec3fAttribArray("pos", 0, 1, new Vector3f[]
			{ new Vector3f(-1, 1, 0), new Vector3f(1, 1, 0), new Vector3f(1, -1, 0), new Vector3f(-1, -1, 0) }),
			new UIntAttribArray("ind", -1, 1, new int[]
			{ 0, 1, 2, 0, 2, 3 }, GL_W.GL_ELEMENT_ARRAY_BUFFER),
			new Vec2fAttribArray("uv", 1, 1, new Vector2f[]
			{ new Vector2f(0, 1), new Vector2f(1, 1), new Vector2f(1, 0), new Vector2f(0, 0) }));

	protected Vector4f background = new Vector4f(0);

	protected TransferMaterial material;
	protected List<PassRenderLayer> layers = new ArrayList<>();

	protected Vector2i resolution = new Vector2i(0, 0);
	protected int samples = 1;

	public void render(GameEngine engine, Scene3D worldScene, Scene2D uiScene, CacheManager worldCache, CacheManager uiCache) {
		final int width = engine.getWindow().getWidth();
		final int height = engine.getWindow().getHeight();

		final boolean needRegen = !resolution.equals(width, height);
		final CacheManager cache = engine.getCache();

		if (needRegen) {
			resolution = new Vector2i(width, height);
			GL_W.glViewport(0, 0, width, height);
		}

		if (material == null) {
			material = MaterialFactory.INSTANCE.newMaterial(TransferMaterial.class);
		}

		GL_W.glEnable(GL_W.GL_DEPTH_TEST);
		GL_W.checkError("Enable(DEPTH_TEST)");

		GL_W.glClearColor(background.x, background.y, background.z, background.w);
		GL_W.checkError("ClearColor(" + background + ")");
		GL_W.glClear(GL_W.GL_COLOR_BUFFER_BIT | GL_W.GL_DEPTH_BUFFER_BIT);
		GL_W.checkError("Clear(COLOR | DEPTH)");

		final TransferShader shader = (TransferShader) material.getRenderShader();

		final Camera camera = worldScene.getCamera();
		final Matrix4f projectionMatrix = camera.getProjection().getProjectionMatrix();
		final Matrix4f viewMatrix = camera.getViewMatrix();

		shader.setUniform(RenderShader.PROJECTION_MATRIX, projectionMatrix);
		shader.setUniform(RenderShader.VIEW_MATRIX, viewMatrix);
		if (camera instanceof Camera3D) {
			material.setPropertyIfPresent(RenderShader.VIEW_POSITION, ((Camera3D) camera).getPosition());
		}

		material.getRenderShader().bind();
		// material.bindProperties(worldCache, worldScene, material.getRenderShader());

		final LinkedHashMap<String, Entity> sortedMap = worldScene
				.getEntities()
				.entrySet()
				.stream()
				.sorted(Scene3DRenderer.PRIORITY_COMPARATOR)
				.collect(LinkedHashMap::new,
						(linkedHashMap, entry) -> linkedHashMap.put(entry.getKey(), entry.getValue()),
						LinkedHashMap::putAll);
		worldScene.setEntities(sortedMap);

		worldScene.getEntities().forEach((name, entity) -> {
			if (entity.hasComponentMatching(RenderConditionComponent.class) && entity
					.getComponentsMatching(RenderConditionComponent.class)
					.parallelStream()
					.allMatch(condition -> condition.get())) {
				return;
			}

			if (entity.hasComponentMatching(MeshComponent.class)) {
				final Mesh mesh = entity.getComponent(MeshComponent.class).getMesh(worldCache);
				mesh.bind();

				Matrix4f transformationMatrix = null;

				if (material.hasProperty(RenderShader.TRANSFORMATION_MATRIX) && entity.hasComponentMatching(TransformComponent.class)) {
					final TransformComponent transform = (TransformComponent) entity.getComponentMatching(TransformComponent.class);
					if (transform != null) {
						transformationMatrix = transform.getTransform().getMatrix();
					}
					material.setProperty(RenderShader.TRANSFORMATION_MATRIX, transformationMatrix);
				}

				material.bindProperties(cache, uiScene, shader);

				GL_W.glDrawElements(shader.getBeginMode().getGlId(), mesh.getIndicesCount(), GL_W.GL_UNSIGNED_INT, 0);
				GL_W.glDisable(GL_W.GL_BLEND);

				GameEngine.DEBUG.wireframe(cache, worldScene, mesh, projectionMatrix, viewMatrix, transformationMatrix);

				mesh.unbind();

				GameEngine.DEBUG.gizmos(cache, worldScene, projectionMatrix, viewMatrix, transformationMatrix);
			}
		});
		material.getRenderShader().unbind();
	}

	@Override
	public void cleanup() {
		GlobalLogger.log("Cleaning up: " + getClass().getName());

		if (SCREEN != null) {
			SCREEN.cleanup();
			SCREEN = null;
		}
	}

	public void setBackground(Vector4f background) {
		this.background = background;
	}

}
