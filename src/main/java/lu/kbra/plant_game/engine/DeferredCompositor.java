package lu.kbra.plant_game.engine;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector4f;

import lu.pcy113.pclib.logger.GlobalLogger;

import lu.kbra.plant_game.engine.shader.BlitShader;
import lu.kbra.plant_game.engine.shader.MaskComputeShader;
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
import lu.kbra.standalone.gameengine.scene.Scene3D;
import lu.kbra.standalone.gameengine.scene.camera.Camera3D;
import lu.kbra.standalone.gameengine.utils.gl.consts.BufferType;
import lu.kbra.standalone.gameengine.utils.gl.consts.DataType;
import lu.kbra.standalone.gameengine.utils.gl.consts.FaceMode;
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

	protected Vector4f background = new Vector4f(0);

	protected SingleTexture depthTexture, posTexture, normalTexture, uvTexture, idsTexture;
	protected Framebuffer worldFramebuffer;
	protected TransferMaterial transferMaterial;

	protected SingleTexture outputTxt;
	protected MaskComputeShader maskComputeShader;
	protected BlitShader blitShader;

	protected Vector2i resolution = new Vector2i(0), outputResolution = new Vector2i(0);

	public void render(GameEngine engine, Scene3D worldScene, Scene2D uiScene, CacheManager worldCache, CacheManager uiCache) {
		final int width = engine.getWindow().getWidth();
		final int height = engine.getWindow().getHeight();

		final boolean needRegen = !resolution.equals(width, height);
		final CacheManager cache = engine.getCache();

		if (needRegen) {
			resolution = new Vector2i(width, height);
			outputResolution = new Vector2i(width, height).div(4);
		}
		if (maskComputeShader == null) {
			maskComputeShader = new MaskComputeShader();
			cache.addAbstractShader(maskComputeShader);
		}

		renderWorldScene(cache, worldScene, worldCache, width, height, needRegen);

		renderMaterials(cache, width, height, needRegen);
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

		maskComputeShader.bind();

		GL_W.glBindImageTexture(0, outputTxt.getTid(), 0, false, 0, GL_W.GL_WRITE_ONLY, outputTxt.getInternalFormat().getGlId());

		posTexture.bind(0);
		depthTexture.bind(1);

		if (needRegen) {
			posTexture.bindUniform(maskComputeShader.getUniformLocation("uColorText"), 0);
			depthTexture.bindUniform(maskComputeShader.getUniformLocation("uDepthTex"), 1);
			maskComputeShader.setUniform(MaskComputeShader.INPUT_SIZE, resolution);
			maskComputeShader.setUniform(MaskComputeShader.OUTPUT_SIZE, outputResolution);
		}
		final int groupsX = (width + 15) / 16;
		final int groupsY = (height + 15) / 16;
		GL_W.glDispatchCompute(groupsX, groupsY, 1);
		assert GL_W.checkError("DispatchCompute(" + groupsX + "," + groupsY + ",1)");
		GL_W.glMemoryBarrier(GL_W.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT);
		assert GL_W.checkError("MemoryBarrier(SHADER_IMAGE_ACCESS_BARRIER_BIT)");

		maskComputeShader.unbind();

		// blit to the screen

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

		GL_W.glDisable(GL_W.GL_DEPTH_TEST);
		assert GL_W.checkError("Disable(DEPTH_TEST)");
		GL_W.glBindFramebuffer(GL_W.GL_FRAMEBUFFER, 0);
		assert GL_W.checkError("BindFramebuffer(0)");
		GL_W.glViewport(0, 0, width, height);
		assert GL_W.checkError("Viewport(" + resolution + ")");
		GL_W.glClear(GL_W.GL_COLOR_BUFFER_BIT | GL_W.GL_DEPTH_BUFFER_BIT);
		assert GL_W.checkError("Clear(COLOR | DEPTH)");
		GL_W.glDrawElements(GL_W.GL_TRIANGLES, SCREEN.getIndicesCount(), GL_W.GL_UNSIGNED_INT, 0);
		assert GL_W.checkError("DrawElements()");
		GL_W.glEnable(GL_W.GL_DEPTH_TEST);
		assert GL_W.checkError("Enable(DEPTH_TEST)");

		SCREEN.unbind();

		blitShader.unbind();
	}

	private void renderWorldScene(
			CacheManager cache,
			Scene3D worldScene,
			CacheManager worldCache,
			int width,
			int height,
			boolean needRegen) {

		if (transferMaterial == null) {
			transferMaterial = MaterialFactory.newMaterial(TransferMaterial.class);
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

		GL_W.glClearColor(background.x, background.y, background.z, background.w);
		assert GL_W.checkError("ClearColor(" + background + ")");
		GL_W.glClear(GL_W.GL_COLOR_BUFFER_BIT | GL_W.GL_DEPTH_BUFFER_BIT);
		assert GL_W.checkError("Clear(COLOR | DEPTH)");

		final TransferShader shader = (TransferShader) transferMaterial.getRenderShader();
		shader.bind();
		GL_W.glCullFace(FaceMode.FRONT_AND_BACK.getGlId());

		final Camera3D camera = worldScene.getCamera();
		final Matrix4f projectionMatrix = camera.getProjection().getProjectionMatrix();
		final Matrix4f viewMatrix = camera.getViewMatrix();

		shader.setUniform(RenderShader.PROJECTION_MATRIX, projectionMatrix);
		shader.setUniform(RenderShader.VIEW_MATRIX, viewMatrix);

		// TODO: this is not threadsafe
		/*
		 * final LinkedHashMap<String, Entity> sortedMap = worldScene .getEntities() .entrySet() .stream()
		 * .sorted(Scene3DRenderer.PRIORITY_COMPARATOR) .collect(LinkedHashMap::new, (linkedHashMap, entry)
		 * -> linkedHashMap.put(entry.getKey(), entry.getValue()), LinkedHashMap::putAll);
		 */
		// worldScene.setEntities(Collections.synchronizedMap(sortedMap));

		for (Entity entity : worldScene.getEntities().values()) {

			if (entity.hasComponentMatching(RenderConditionComponent.class) && entity
					.getComponentsMatching(RenderConditionComponent.class)
					.parallelStream()
					.allMatch(RenderConditionComponent::get)) {
				return;
			}

			if (entity.hasComponentMatching(MeshComponent.class)) {
				final Mesh mesh = entity.getComponent(MeshComponent.class).getMesh();
				mesh.bind();

				Matrix4f transformationMatrix = null;

				if (transferMaterial.hasProperty(RenderShader.TRANSFORMATION_MATRIX)
						&& entity.hasComponentMatching(TransformComponent.class)) {
					final TransformComponent transform = (TransformComponent) entity.getComponentMatching(TransformComponent.class);
					if (transform != null) {
						transformationMatrix = transform.getTransform().getMatrix();
					}
					transferMaterial.setProperty(RenderShader.TRANSFORMATION_MATRIX, transformationMatrix);
				}

				transferMaterial.bindProperties(cache, worldScene);

				GL_W.glDrawElements(shader.getBeginMode().getGlId(), mesh.getIndicesCount(), GL_W.GL_UNSIGNED_INT, 0);

				// GameEngine.DEBUG.wireframe(cache, worldScene, mesh, projectionMatrix, viewMatrix,
				// transformationMatrix);

				mesh.unbind();

				// GameEngine.DEBUG.gizmos(cache, worldScene, projectionMatrix, viewMatrix, transformationMatrix);
			}
		}

		shader.unbind();
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
		idsTexture.setDataType(DataType.FLOAT);
		idsTexture.setFormat(TexelFormat.RGBA);
		idsTexture.setInternalFormat(TexelInternalFormat.RGBA16F);
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

	public void setBackground(Vector4f background) {
		this.background = background;
	}

}
