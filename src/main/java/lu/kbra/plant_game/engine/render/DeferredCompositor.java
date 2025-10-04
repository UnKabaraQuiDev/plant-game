package lu.kbra.plant_game.engine.render;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;

import lu.pcy113.pclib.logger.GlobalLogger;

import lu.kbra.plant_game.engine.entity.GameObject;
import lu.kbra.plant_game.engine.entity.WorldLevelScene;
import lu.kbra.plant_game.engine.render.shader.BlitShader;
import lu.kbra.plant_game.engine.render.shader.MaterialComputeShader;
import lu.kbra.plant_game.engine.render.shader.TransferShader;
import lu.kbra.standalone.gameengine.GameEngine;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.cache.attrib.UIntAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.Vec2fAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.Vec3fAttribArray;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.graph.composition.buffer.Framebuffer;
import lu.kbra.standalone.gameengine.graph.composition.buffer.RenderBuffer;
import lu.kbra.standalone.gameengine.graph.shader.ComputeShader;
import lu.kbra.standalone.gameengine.graph.shader.RenderShader;
import lu.kbra.standalone.gameengine.graph.texture.SingleTexture;
import lu.kbra.standalone.gameengine.impl.Cleanupable;
import lu.kbra.standalone.gameengine.impl.FramebufferAttachment;
import lu.kbra.standalone.gameengine.objs.entity.Entity;
import lu.kbra.standalone.gameengine.objs.entity.components.MeshComponent;
import lu.kbra.standalone.gameengine.objs.entity.components.RenderConditionComponent;
import lu.kbra.standalone.gameengine.objs.entity.components.TransformComponent;
import lu.kbra.standalone.gameengine.scene.Scene2D;
import lu.kbra.standalone.gameengine.scene.camera.Camera3D;
import lu.kbra.standalone.gameengine.utils.gl.consts.BufferType;
import lu.kbra.standalone.gameengine.utils.gl.consts.DataType;
import lu.kbra.standalone.gameengine.utils.gl.consts.FrameBufferAttachment;
import lu.kbra.standalone.gameengine.utils.gl.consts.TexelFormat;
import lu.kbra.standalone.gameengine.utils.gl.consts.TexelInternalFormat;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextureFilter;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextureWrap;
import lu.kbra.standalone.gameengine.utils.gl.wrapper.GL_W;

public class DeferredCompositor implements Cleanupable {

	private static final String WORLD_FRAMEBUFFER_NAME = "_WORLD_FRAMEBUFFER";
	private static final int WORLD_FRAMEBUFFER_POS_IDX = 0;
	private static final int WORLD_FRAMEBUFFER_NORMAL_IDX = 1;
	private static final int WORLD_FRAMEBUFFER_UV_IDX = 2;
	private static final int WORLD_FRAMEBUFFER_IDS_IDX = 3;
	private static final String PASS_SCREEN = "PASS_SCREEN";

	public static final String SCREEN_WIDTH = "screen_width";
	public static final String SCREEN_HEIGHT = "screen_height";

	private static Mesh SCREEN = new Mesh(PASS_SCREEN, null,
			new Vec3fAttribArray("pos", 0, 1,
					new Vector3f[] { new Vector3f(-1, 1, 0), new Vector3f(1, 1, 0), new Vector3f(1, -1, 0), new Vector3f(-1, -1, 0) }),
			new UIntAttribArray("ind", -1, 1, new int[] { 0, 1, 2, 0, 2, 3 }, BufferType.ELEMENT_ARRAY), new Vec2fAttribArray("uv", 1, 1,
					new Vector2f[] { new Vector2f(0, 1), new Vector2f(1, 1), new Vector2f(1, 0), new Vector2f(0, 0) }));

	protected SingleTexture depthTexture, posTexture, normalTexture, uvTexture, idsTexture;
	protected Framebuffer worldFramebuffer;
	protected TransferShader transferShader;

	protected SingleTexture outputTxt;
	protected MaterialComputeShader materialComputeShader;
	protected BlitShader blitShader;

	protected Vector2i resolution = new Vector2i(0), outputResolution = new Vector2i(0);

	public void render(GameEngine engine, WorldLevelScene worldScene, Scene2D uiScene, CacheManager worldCache, CacheManager uiCache) {
		final int width = engine.getWindow().getWidth();
		final int height = engine.getWindow().getHeight();

		final boolean needRegen = !resolution.equals(width, height);
		final CacheManager cache = engine.getCache();

		if (needRegen) {
			resolution = new Vector2i(width, height);
			outputResolution = new Vector2i(width, height).div(4);
		}
		if (materialComputeShader == null) {
			materialComputeShader = new MaterialComputeShader();
			cache.addAbstractShader(materialComputeShader);
		}

		renderWorldScene(cache, worldScene, worldCache, width, height, needRegen);

		renderMaterials(cache, width, height, needRegen);

		blitToScreen(cache, width, height, needRegen);
	}

	private void blitToScreen(CacheManager cache, int width, int height, boolean needRegen) {
		if (blitShader == null) {
			blitShader = new BlitShader();
			cache.addAbstractShader(blitShader);
		}

		blitShader.bind();
		outputTxt.bind(0);
		if (needRegen) {
			outputTxt.bindUniform(blitShader.getUniformLocation(BlitShader.TXT0), 0);
			blitShader.setUniform(ComputeShader.SCREEN_SIZE, outputResolution);
		}

		SCREEN.bind();

		GL_W.glDisable(GL_W.GL_CULL_FACE);
		assert GL_W.checkError("Disable(CULL_FACE)");
		GL_W.glDisable(GL_W.GL_DEPTH_TEST);
		assert GL_W.checkError("Disable(DEPTH_TEST)");
		GL_W.glBindFramebuffer(GL_W.GL_FRAMEBUFFER, 0);
		assert GL_W.checkError("BindFramebuffer(0)");
		if (needRegen) {
			GL_W.glViewport(0, 0, width, height);
			assert GL_W.checkError("Viewport(" + resolution + ")");
		}
		GL_W.glClear(GL_W.GL_COLOR_BUFFER_BIT | GL_W.GL_DEPTH_BUFFER_BIT);
		assert GL_W.checkError("Clear(COLOR | DEPTH)");
		GL_W.glDrawElements(GL_W.GL_TRIANGLES, SCREEN.getIndicesCount(), GL_W.GL_UNSIGNED_INT, 0);
		assert GL_W.checkError("DrawElements()");
		GL_W.glEnable(GL_W.GL_DEPTH_TEST);
		assert GL_W.checkError("Enable(DEPTH_TEST)");

		SCREEN.unbind();

		blitShader.unbind();
	}

	private void renderMaterials(CacheManager cache, int width, int height, boolean needRegen) {
		if (outputTxt == null) {
			outputTxt = new SingleTexture("output", outputResolution);
			outputTxt.setDataType(DataType.UBYTE);
			outputTxt.setFormat(TexelFormat.RGBA);
			outputTxt.setInternalFormat(TexelInternalFormat.RGBA8);
			outputTxt.setFilters(TextureFilter.NEAREST);
			outputTxt.setGenerateMipmaps(false);
			outputTxt.setup();
			cache.addTexture(outputTxt);
		} else {
			outputTxt.setSize(outputResolution);
			outputTxt.bind();
			outputTxt.resize();
			outputTxt.unbind();
		}

		materialComputeShader.bind();

		GL_W.glBindImageTexture(0, outputTxt.getTid(), 0, false, 0, GL_W.GL_WRITE_ONLY, outputTxt.getInternalFormat().getGlId());
		assert GL_W.checkError("BindImageTexture(0," + outputTxt.getTid() + ",WRITE_ONLY)");

		posTexture.bind(0);
		normalTexture.bind(1);
		uvTexture.bind(2);
		idsTexture.bind(3);
		depthTexture.bind(4);

		if (needRegen) {
			posTexture.bindUniform(materialComputeShader.getUniformLocation("uPosTex"), 0);
			normalTexture.bindUniform(materialComputeShader.getUniformLocation("uNormalTex"), 1);
			uvTexture.bindUniform(materialComputeShader.getUniformLocation("uUvTex"), 2);
			idsTexture.bindUniform(materialComputeShader.getUniformLocation("uIdsTex"), 3);
			depthTexture.bindUniform(materialComputeShader.getUniformLocation("uDepthTex"), 4);

			materialComputeShader.setUniform(MaterialComputeShader.INPUT_SIZE, resolution);
			materialComputeShader.setUniform(MaterialComputeShader.OUTPUT_SIZE, outputResolution);
		}
		final int groupsX = (width + 15) / 16;
		final int groupsY = (height + 15) / 16;
		GL_W.glDispatchCompute(groupsX, groupsY, 1);
		assert GL_W.checkError("DispatchCompute(" + groupsX + "," + groupsY + ",1)");
		GL_W.glMemoryBarrier(GL_W.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT);
		assert GL_W.checkError("MemoryBarrier(SHADER_IMAGE_ACCESS_BARRIER_BIT)");

		materialComputeShader.unbind();
	}

	private void renderWorldScene(
			CacheManager cache,
			WorldLevelScene worldScene,
			CacheManager worldCache,
			int width,
			int height,
			boolean needRegen) {

		if (transferShader == null) {
			transferShader = new TransferShader();
		}

		if (worldFramebuffer == null) {
			genFramebuffer(cache, width, height);
		} else if (needRegen) {
			resizeFramebuffer(width, height);

			worldFramebuffer.bind();
			GL_W.glViewport(0, 0, width, height);
			assert GL_W.checkError("Viewport(" + resolution + ")");
		} else {
			worldFramebuffer.bind();
		}

		GL_W.glEnable(GL_W.GL_DEPTH_TEST);
		assert GL_W.checkError("Enable(DEPTH_TEST)");
		GL_W.glDisable(GL_W.GL_CULL_FACE);
		assert GL_W.checkError("Disable(CULL_FACE)");
		GL_W.glCullFace(GL_W.GL_BACK);
		assert GL_W.checkError("CullFace(BACK)");

		GL_W.glClearColor(0, 0, 0, 1);
		assert GL_W.checkError("ClearColor(0, 0, 0, 1)");
		GL_W.glClear(GL_W.GL_COLOR_BUFFER_BIT | GL_W.GL_DEPTH_BUFFER_BIT);
		assert GL_W.checkError("Clear(COLOR | DEPTH)");

		transferShader.bind();

		final Camera3D camera = worldScene.getCamera();
		final Matrix4f projectionMatrix = camera.getProjection().getProjectionMatrix();
		final Matrix4f viewMatrix = camera.getViewMatrix();

		transferShader.setUniform(RenderShader.PROJECTION_MATRIX, projectionMatrix);
		transferShader.setUniform(RenderShader.VIEW_MATRIX, viewMatrix);

		synchronized (worldScene.getEntitiesLock()) {
			for (Entity entity : worldScene.getEntities().values()) {

				if (!entity.isActive()) {
					continue;
				}

				if (entity.hasComponentMatching(RenderConditionComponent.class) && entity
						.getComponentsMatching(RenderConditionComponent.class)
						.parallelStream()
						.allMatch(RenderConditionComponent::get)) {
					return;
				}

				if (entity.hasComponentMatching(MeshComponent.class)) {
					final Mesh mesh = entity.getComponent(MeshComponent.class).getMesh();
					mesh.bind();

					Matrix4f transformationMatrix = GameEngine.IDENTITY_MATRIX4F;

					if (transferShader.createUniform(RenderShader.TRANSFORMATION_MATRIX)
							&& entity.hasComponentMatching(TransformComponent.class)) {
						final TransformComponent transform = (TransformComponent) entity.getComponentMatching(TransformComponent.class);
						if (transform != null) {
							transformationMatrix = transform.getTransform().getMatrix();
						}
						transferShader.setUniform(RenderShader.TRANSFORMATION_MATRIX, transformationMatrix);
					}

					if (entity instanceof GameObject go) {
						if (!go.isCompositeMaterialId()) {
							GL_W.glDisableVertexAttribArray(GameObject.MESH_ATTRIB_MATERIAL_ID_ID);
							assert GL_W.checkError("DisableVertexAttribArray(" + GameObject.MESH_ATTRIB_MATERIAL_ID_ID + ")");
							GL_W.glVertexAttribI1ui(GameObject.MESH_ATTRIB_MATERIAL_ID_ID, go.getMaterialId());
							assert GL_W
									.checkError("VertexAttrib1f(" + GameObject.MESH_ATTRIB_MATERIAL_ID_ID + "," + go.getMaterialId() + ")");
						}

						if (!go.isCompositeObjectId()) {
							final Vector3i objId = go.getObjectId();
							GL_W.glDisableVertexAttribArray(GameObject.MESH_ATTRIB_OBJECT_ID_ID);
							assert GL_W.checkError("DisableVertexAttribArray(" + GameObject.MESH_ATTRIB_OBJECT_ID_ID + ")");
							GL_W.glVertexAttribI3ui(GameObject.MESH_ATTRIB_OBJECT_ID_ID, objId.x, objId.y, objId.z);
							assert GL_W.checkError("VertexAttrib3f(" + GameObject.MESH_ATTRIB_OBJECT_ID_ID + "," + objId + ")");
						}
					}

					GL_W.glDrawElements(transferShader.getBeginMode().getGlId(), mesh.getIndicesCount(), GL_W.GL_UNSIGNED_INT, 0);
					assert GL_W.checkError("DrawElements(" + mesh.getId() + ")");

					mesh.unbind();
				}
			}
		}

		transferShader.unbind();
		worldFramebuffer.unbind();
	}

	private void resizeFramebuffer(int width, int height) {
		worldFramebuffer.bind();
		for (FramebufferAttachment fa : worldFramebuffer.getAttachments().values()) {
			if (fa instanceof SingleTexture txt) {
				txt.setSize(width, height);
				txt.bind();
				txt.resize();
			} else if (fa instanceof RenderBuffer rb) {
				rb.setSize(width, height);
				rb.bind();
				rb.resize();
			}
		}
		worldFramebuffer.unbind();

		GlobalLogger.log("Resized framebuffer: " + worldFramebuffer.getId() + " (" + width + "x" + height + ")");
	}

	private Framebuffer genFramebuffer(final CacheManager cache, final int width, final int height) {
		worldFramebuffer = new Framebuffer(WORLD_FRAMEBUFFER_NAME);
		cache.addFramebuffer(worldFramebuffer);
		worldFramebuffer.gen();
		worldFramebuffer.bind();

		depthTexture = new SingleTexture(WORLD_FRAMEBUFFER_NAME + ".depth", width, height);
		depthTexture.setDataType(DataType.FLOAT);
		depthTexture.setFormat(TexelFormat.DEPTH);
		depthTexture.setInternalFormat(TexelInternalFormat.DEPTH_COMPONENT24);
		depthTexture.setFilters(TextureFilter.NEAREST);
		depthTexture.setWraps(TextureWrap.CLAMP_TO_EDGE);
		depthTexture.setGenerateMipmaps(false);
		depthTexture.setup();
		cache.addTexture(depthTexture);
		worldFramebuffer.attachTexture(FrameBufferAttachment.DEPTH, depthTexture);

		posTexture = new SingleTexture(WORLD_FRAMEBUFFER_NAME + ".pos", width, height);
		posTexture.setDataType(DataType.FLOAT);
		posTexture.setFormat(TexelFormat.RGB);
		posTexture.setInternalFormat(TexelInternalFormat.RGB32F);
		posTexture.setFilters(TextureFilter.NEAREST);
		posTexture.setGenerateMipmaps(false);
		posTexture.setup();
		cache.addTexture(posTexture);
		worldFramebuffer.attachTexture(FrameBufferAttachment.COLOR_FIRST, WORLD_FRAMEBUFFER_POS_IDX, posTexture);

		normalTexture = new SingleTexture(WORLD_FRAMEBUFFER_NAME + ".normal", width, height);
		normalTexture.setDataType(DataType.FLOAT);
		normalTexture.setFormat(TexelFormat.RGB);
		normalTexture.setInternalFormat(TexelInternalFormat.RGB16F);
		normalTexture.setFilters(TextureFilter.NEAREST);
		normalTexture.setGenerateMipmaps(false);
		normalTexture.setup();
		cache.addTexture(normalTexture);
		worldFramebuffer.attachTexture(FrameBufferAttachment.COLOR_FIRST, WORLD_FRAMEBUFFER_NORMAL_IDX, normalTexture);

		uvTexture = new SingleTexture(WORLD_FRAMEBUFFER_NAME + ".uv", width, height);
		uvTexture.setDataType(DataType.FLOAT);
		uvTexture.setFormat(TexelFormat.RG);
		uvTexture.setInternalFormat(TexelInternalFormat.RG16F);
		uvTexture.setFilters(TextureFilter.NEAREST);
		uvTexture.setGenerateMipmaps(false);
		uvTexture.setup();
		cache.addTexture(uvTexture);
		worldFramebuffer.attachTexture(FrameBufferAttachment.COLOR_FIRST, WORLD_FRAMEBUFFER_UV_IDX, uvTexture);

		idsTexture = new SingleTexture(WORLD_FRAMEBUFFER_NAME + ".ids", width, height);
		idsTexture.setDataType(DataType.UINT);
		idsTexture.setFormat(TexelFormat.RGBA_INTEGER);
		idsTexture.setInternalFormat(TexelInternalFormat.RGBA32UI);
		idsTexture.setFilters(TextureFilter.NEAREST);
		idsTexture.setGenerateMipmaps(false);
		idsTexture.setup();
		cache.addTexture(idsTexture);
		worldFramebuffer.attachTexture(FrameBufferAttachment.COLOR_FIRST, WORLD_FRAMEBUFFER_IDS_IDX, idsTexture);

		worldFramebuffer.setup();
		worldFramebuffer.unbind();

		GlobalLogger.log("Created framebuffer: " + worldFramebuffer.getId() + " (" + width + "x" + height + ")");

		return worldFramebuffer;
	}

	@Override
	public void cleanup() {
		GlobalLogger.log("Cleaning up: " + getClass().getName());

		if (SCREEN != null) {
			SCREEN.cleanup();
			SCREEN = null;
		}
	}

}
