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
import lu.kbra.standalone.gameengine.graph.composition.buffer.Framebuffer;
import lu.kbra.standalone.gameengine.graph.composition.layer.PassRenderLayer;
import lu.kbra.standalone.gameengine.graph.composition.layer.RenderLayer;
import lu.kbra.standalone.gameengine.graph.render.Scene3DRenderer;
import lu.kbra.standalone.gameengine.graph.shader.RenderShader;
import lu.kbra.standalone.gameengine.graph.texture.SingleTexture;
import lu.kbra.standalone.gameengine.impl.Cleanupable;
import lu.kbra.standalone.gameengine.objs.entity.Entity;
import lu.kbra.standalone.gameengine.objs.entity.components.MeshComponent;
import lu.kbra.standalone.gameengine.objs.entity.components.RenderConditionComponent;
import lu.kbra.standalone.gameengine.objs.entity.components.TransformComponent;
import lu.kbra.standalone.gameengine.scene.Scene2D;
import lu.kbra.standalone.gameengine.scene.Scene3D;
import lu.kbra.standalone.gameengine.scene.camera.Camera;
import lu.kbra.standalone.gameengine.scene.camera.Camera3D;
import lu.kbra.standalone.gameengine.utils.consts.DataType;
import lu.kbra.standalone.gameengine.utils.consts.FrameBufferAttachment;
import lu.kbra.standalone.gameengine.utils.consts.TexelFormat;
import lu.kbra.standalone.gameengine.utils.consts.TexelInternalFormat;
import lu.kbra.standalone.gameengine.utils.gl.wrapper.GL_W;

public class SimpleCompositor implements Cleanupable {

	private static final String WORLD_FRAMEBUFFER_NAME = "_WORLD_FRAMEBUFFER";
	private static final int WORLD_FRAMEBUFFER_POS_IDX = 0;
	private static final int WORLD_FRAMEBUFFER_NORMAL_IDX = 1;
	private static final int WORLD_FRAMEBUFFER_UV_IDX = 2;
	private static final String PASS_SCREEN = "PASS_SCREEN";

	public static final String SCREEN_WIDTH = "screen_width";
	public static final String SCREEN_HEIGHT = "screen_height";

	private static Mesh SCREEN = new Mesh(PASS_SCREEN, null,
			new Vec3fAttribArray("pos", 0, 1,
					new Vector3f[] { new Vector3f(-1, 1, 0), new Vector3f(1, 1, 0), new Vector3f(1, -1, 0),
							new Vector3f(-1, -1, 0) }),
			new UIntAttribArray("ind", -1, 1, new int[] { 0, 1, 2, 0, 2, 3 }, GL_W.GL_ELEMENT_ARRAY_BUFFER),
			new Vec2fAttribArray("uv", 1, 1,
					new Vector2f[] { new Vector2f(0, 1), new Vector2f(1, 1), new Vector2f(1, 0), new Vector2f(0, 0) }));

	protected Vector4f background = new Vector4f(0);

	protected TransferMaterial transferMaterial;
	protected List<PassRenderLayer> layers = new ArrayList<>();

	protected Vector2i resolution = new Vector2i(0, 0);
	protected int samples = 1;

	public void render(GameEngine engine, Scene3D worldScene, Scene2D uiScene, CacheManager worldCache,
			CacheManager uiCache) {
		final int width = engine.getWindow().getWidth();
		final int height = engine.getWindow().getHeight();

		final boolean needRegen = !resolution.equals(width, height);
		final CacheManager cache = engine.getCache();

		if (needRegen) {
			resolution = new Vector2i(width, height);
			GL_W.glViewport(0, 0, width, height);
		}

		if (transferMaterial == null) {
			transferMaterial = MaterialFactory.INSTANCE.newMaterial(TransferMaterial.class);
		}

		GL_W.glEnable(GL_W.GL_DEPTH_TEST);
		GL_W.checkError("Enable(DEPTH_TEST)");

		GL_W.glClearColor(background.x, background.y, background.z, background.w);
		GL_W.checkError("ClearColor(" + background + ")");
		GL_W.glClear(GL_W.GL_COLOR_BUFFER_BIT | GL_W.GL_DEPTH_BUFFER_BIT);
		GL_W.checkError("Clear(COLOR | DEPTH)");

		regenFramebuffer(cache);

		final TransferShader shader = (TransferShader) transferMaterial.getRenderShader();

		final Camera camera = worldScene.getCamera();
		final Matrix4f projectionMatrix = camera.getProjection().getProjectionMatrix();
		final Matrix4f viewMatrix = camera.getViewMatrix();

		shader.setUniform(RenderShader.PROJECTION_MATRIX, projectionMatrix);
		shader.setUniform(RenderShader.VIEW_MATRIX, viewMatrix);
		if (camera instanceof Camera3D) {
			transferMaterial.setPropertyIfPresent(RenderShader.VIEW_POSITION, ((Camera3D) camera).getPosition());
		}

		transferMaterial.getRenderShader().bind();
		// material.bindProperties(worldCache, worldScene, material.getRenderShader());

		// TODO: this is not threadsafe
		final LinkedHashMap<String, Entity> sortedMap = worldScene.getEntities().entrySet().stream()
				.sorted(Scene3DRenderer.PRIORITY_COMPARATOR).collect(LinkedHashMap::new,
						(linkedHashMap, entry) -> linkedHashMap.put(entry.getKey(), entry.getValue()),
						LinkedHashMap::putAll);
		worldScene.setEntities(sortedMap);

		worldScene.getEntities().forEach((name, entity) -> {
			if (entity.hasComponentMatching(RenderConditionComponent.class)
					&& entity.getComponentsMatching(RenderConditionComponent.class).parallelStream()
							.allMatch(RenderConditionComponent::get)) {
				return;
			}

			if (entity.hasComponentMatching(MeshComponent.class)) {
				final Mesh mesh = entity.getComponent(MeshComponent.class).getMesh(worldCache);
				mesh.bind();

				Matrix4f transformationMatrix = null;

				if (transferMaterial.hasProperty(RenderShader.TRANSFORMATION_MATRIX)
						&& entity.hasComponentMatching(TransformComponent.class)) {
					final TransformComponent transform = (TransformComponent) entity
							.getComponentMatching(TransformComponent.class);
					if (transform != null) {
						transformationMatrix = transform.getTransform().getMatrix();
					}
					transferMaterial.setProperty(RenderShader.TRANSFORMATION_MATRIX, transformationMatrix);
				}

				transferMaterial.bindProperties(cache, uiScene, shader);

				GL_W.glDrawElements(shader.getBeginMode().getGlId(), mesh.getIndicesCount(), GL_W.GL_UNSIGNED_INT, 0);
				// GL_W.glDisable(GL_W.GL_BLEND);

				GameEngine.DEBUG.wireframe(cache, worldScene, mesh, projectionMatrix, viewMatrix, transformationMatrix);

				mesh.unbind();

				GameEngine.DEBUG.gizmos(cache, worldScene, projectionMatrix, viewMatrix, transformationMatrix);
			}
		});
		transferMaterial.getRenderShader().unbind();
	}

	private void regenFramebuffer(final CacheManager cache, final int width, final int height) {
		final Framebuffer framebuffer = new Framebuffer(WORLD_FRAMEBUFFER_NAME);
		cache.addFramebuffer(framebuffer);
		framebuffer.bind();

		final SingleTexture txtDepth = new SingleTexture(WORLD_FRAMEBUFFER_NAME + ".depth", width, height);
		txtDepth.setDataType(DataType.FLOAT);
		txtDepth.setFormat(TexelFormat.DEPTH);
		txtDepth.setInternalFormat(TexelInternalFormat.DEPTH_COMPONENT);
		txtDepth.setup();
		cache.addTexture(txtDepth);
		framebuffer.attachRenderBuffer(FrameBufferAttachment.DEPTH, 0, txtDepth);

		final SingleTexture txtPos = new SingleTexture(WORLD_FRAMEBUFFER_NAME + ".pos", width, height);
		txtPos.setDataType(DataType.FLOAT);
		txtPos.setFormat(TexelFormat.RGB);
		txtPos.setInternalFormat(TexelInternalFormat.RGB32F);
		txtPos.setup();
		cache.addTexture(txtPos);
		framebuffer.attachTexture(FrameBufferAttachment.COLOR_FIRST, WORLD_FRAMEBUFFER_POS_IDX, txtPos);
		
		final SingleTexture txtNormal = new SingleTexture(WORLD_FRAMEBUFFER_NAME + ".normal", width, height);
		txtNormal.setDataType(DataType.FLOAT);
		txtNormal.setFormat(TexelFormat.RGB);
		txtNormal.setInternalFormat(TexelInternalFormat.RGB16F);
		txtNormal.setup();
		cache.addTexture(txtNormal);
		framebuffer.attachTexture(FrameBufferAttachment.COLOR_FIRST, WORLD_FRAMEBUFFER_NORMAL_IDX, txtNormal);
		
		final SingleTexture txtUV = new SingleTexture(WORLD_FRAMEBUFFER_NAME + ".uv", width, height);
		txtUV.setDataType(DataType.FLOAT);
		txtUV.setFormat(TexelFormat.RG);
		txtUV.setInternalFormat(TexelInternalFormat.RG16F);
		txtUV.setup();
		cache.addTexture(txtUV);
		framebuffer.attachTexture(FrameBufferAttachment.COLOR_FIRST, WORLD_FRAMEBUFFER_UV_IDX, txtUV);
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
