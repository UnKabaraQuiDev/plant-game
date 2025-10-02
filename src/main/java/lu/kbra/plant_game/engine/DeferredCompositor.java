package lu.kbra.plant_game.engine;

import java.util.LinkedHashMap;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector4f;

import lu.pcy113.pclib.logger.GlobalLogger;

import lu.kbra.plant_game.engine.shader.BlitShader.BlitMaterial;
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
import lu.kbra.standalone.gameengine.graph.render.Scene3DRenderer;
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
			new UIntAttribArray("ind", -1, 1, new int[] { 0, 1, 2, 0, 2, 3 }, GL_W.GL_ELEMENT_ARRAY_BUFFER), new Vec2fAttribArray("uv", 1,
					1, new Vector2f[] { new Vector2f(0, 1), new Vector2f(1, 1), new Vector2f(1, 0), new Vector2f(0, 0) }));

	protected Vector4f background = new Vector4f(0);

	protected Framebuffer worldFramebuffer;
	protected TransferMaterial transferMaterial;

	protected SingleTexture outputTxt;
	protected MaskComputeShader maskComputeShader;
	protected BlitMaterial blitMaterial;

	protected Vector2i resolution = new Vector2i(0, 0);
	protected int samples = 1;

	public void render(GameEngine engine, Scene3D worldScene, Scene2D uiScene, CacheManager worldCache, CacheManager uiCache) {
		final int width = engine.getWindow().getWidth();
		final int height = engine.getWindow().getHeight();

		final boolean needRegen = !resolution.equals(width, height);
		final CacheManager cache = engine.getCache();

		if (needRegen) {
			resolution = new Vector2i(width, height);
		}
		if (maskComputeShader == null) {
			maskComputeShader = new MaskComputeShader();
			cache.addAbstractShader(maskComputeShader);
		}

		renderWorldScene(cache, worldScene, worldCache, width, height, needRegen);

		GL_W.glViewport(0, 0, width, height);
		GL_W.checkError("Viewport(" + resolution + ")");
		GL_W.glClear(GL_W.GL_COLOR_BUFFER_BIT | GL_W.GL_DEPTH_BUFFER_BIT);
		GL_W.checkError("Clear(COLOR | DEPTH)");

		/*
		 * worldFramebuffer.bind(GL_W.GL_READ_FRAMEBUFFER); GL_W.glBindFramebuffer(GL_W.GL_DRAW_FRAMEBUFFER,
		 * 0); GL_W.checkError("BindFramebuffer(0)");
		 * GL_W.glReadBuffer(FrameBufferAttachment.COLOR_FIRST.getGlId() + 1);
		 * GL_W.checkError("ReadBuffer(COLOR1)"); GL_W.glBlitFramebuffer(0, 0, width, height, 0, 0, width,
		 * height, GL_W.GL_COLOR_BUFFER_BIT, GL_W.GL_NEAREST); GL_W.checkError("BlitFramebuffer(" + width +
		 * "x" + height + ", COLOR_BUFFER_BIT, NEAREST)"); GL_W.glBindFramebuffer(GL_W.GL_FRAMEBUFFER, 0);
		 * GL_W.checkError("BindFramebuffer(0)");
		 */

		if (outputTxt == null) {
			outputTxt = new SingleTexture("output", width, height);
			outputTxt.setDataType(DataType.UBYTE);
			outputTxt.setFormat(TexelFormat.RGBA);
			outputTxt.setInternalFormat(TexelInternalFormat.RGBA8);
			outputTxt.setFilters(TextureFilter.NEAREST);
			outputTxt.setGenerateMipmaps(false);
			outputTxt.setup();
			cache.addTexture(outputTxt);
		}

		maskComputeShader.bind();

		final SingleTexture posTxt = (SingleTexture) worldFramebuffer
				.getAttachment(FrameBufferAttachment.COLOR_FIRST, WORLD_FRAMEBUFFER_POS_IDX);
		final SingleTexture depthRb = (SingleTexture) worldFramebuffer.getAttachment(FrameBufferAttachment.DEPTH);

		GL_W.glBindImageTexture(0, outputTxt.getTid(), 0, false, 0, GL_W.GL_WRITE_ONLY, TexelInternalFormat.RGBA8.getGlId());

		posTxt.bind(0);
		maskComputeShader.setUniform("uColorTex", posTxt.getTid());

		depthRb.bind(1);
		maskComputeShader.setUniform("uDepthTex", depthRb.getTid());

		maskComputeShader.setUniform(ComputeShader.SCREEN_SIZE, resolution);

		int groupsX = (width + 15) / 16;
		int groupsY = (height + 15) / 16;
		GL_W.glDispatchCompute(groupsX, groupsY, 1);
		GL_W.glMemoryBarrier(GL_W.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT);

		maskComputeShader.unbind();

		// blit to the screen

		if (blitMaterial == null) {
			blitMaterial = MaterialFactory.newMaterial(BlitMaterial.class, outputTxt);
		}

		blitMaterial.getRenderShader().bind();
		blitMaterial.setScreenSize(resolution);
		blitMaterial.bindProperties(cache, worldScene);

		SCREEN.bind();

		GL_W.glDisable(GL_W.GL_DEPTH_TEST);
		GL_W.glBindFramebuffer(GL_W.GL_FRAMEBUFFER, 0);
		GL_W.glDrawElements(GL_W.GL_TRIANGLES, SCREEN.getIndicesCount(), GL_W.GL_UNSIGNED_INT, 0);
		GL_W.glEnable(GL_W.GL_DEPTH_TEST);

		SCREEN.unbind();

		blitMaterial.getRenderShader().unbind();
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
			this.worldFramebuffer = genFramebuffer(cache, width, height);
		} else if (needRegen) {
			resizeFramebuffer(this.worldFramebuffer, width, height);

			worldFramebuffer.bind();
			GL_W.glViewport(0, 0, width, height);
			GL_W.checkError("Viewport(" + resolution + ")");
		} else {
			worldFramebuffer.bind();
		}

		GL_W.glEnable(GL_W.GL_DEPTH_TEST);
		GL_W.checkError("Enable(DEPTH_TEST)");

		GL_W.glClearColor(background.x, background.y, background.z, background.w);
		GL_W.checkError("ClearColor(" + background + ")");
		GL_W.glClear(GL_W.GL_COLOR_BUFFER_BIT | GL_W.GL_DEPTH_BUFFER_BIT);
		GL_W.checkError("Clear(COLOR | DEPTH)");

		final TransferShader shader = (TransferShader) transferMaterial.getRenderShader();
		shader.bind();
		GL_W.glCullFace(shader.getFaceMode().getGlId());

		final Camera3D camera = worldScene.getCamera();
		final Matrix4f projectionMatrix = camera.getProjection().getProjectionMatrix();
		final Matrix4f viewMatrix = camera.getViewMatrix();

		shader.setUniform(RenderShader.PROJECTION_MATRIX, projectionMatrix);
		shader.setUniform(RenderShader.VIEW_MATRIX, viewMatrix);
		if (camera instanceof Camera3D) {
			transferMaterial.setPropertyIfPresent(RenderShader.VIEW_POSITION, ((Camera3D) camera).getPosition());
		}

		// TODO: this is not threadsafe
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
					.allMatch(RenderConditionComponent::get)) {
				return;
			}

			if (entity.hasComponentMatching(MeshComponent.class)) {
				final Mesh mesh = entity.getComponent(MeshComponent.class).getMesh(worldCache);
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
		});
		shader.unbind();
		worldFramebuffer.unbind();
	}

	private void resizeFramebuffer(Framebuffer framebuffer, int width, int height) {
		framebuffer.bind();
		for (FramebufferAttachment fa : framebuffer.getAttachments().values()) {
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
		framebuffer.unbind();

		GlobalLogger.log("Resized framebuffer: " + framebuffer.getId() + " (" + width + "x" + height + ")");
	}

	private Framebuffer genFramebuffer(final CacheManager cache, final int width, final int height) {
		final Framebuffer framebuffer = new Framebuffer(WORLD_FRAMEBUFFER_NAME);
		cache.addFramebuffer(framebuffer);
		framebuffer.gen();
		framebuffer.bind();

		final SingleTexture txtDepth = new SingleTexture(WORLD_FRAMEBUFFER_NAME + ".depth", width, height);
		txtDepth.setDataType(DataType.FLOAT);
		txtDepth.setFormat(TexelFormat.DEPTH);
		txtDepth.setInternalFormat(TexelInternalFormat.DEPTH_COMPONENT24);
		txtDepth.setFilters(TextureFilter.NEAREST);
		txtDepth.setWraps(TextureWrap.CLAMP_TO_EDGE);
		txtDepth.setGenerateMipmaps(false);
		txtDepth.setup();
		cache.addTexture(txtDepth);
		framebuffer.attachTexture(FrameBufferAttachment.DEPTH, txtDepth);

		final SingleTexture txtPos = new SingleTexture(WORLD_FRAMEBUFFER_NAME + ".pos", width, height);
		txtPos.setDataType(DataType.FLOAT);
		txtPos.setFormat(TexelFormat.RGB);
		txtPos.setInternalFormat(TexelInternalFormat.RGB32F);
		txtPos.setFilters(TextureFilter.NEAREST);
		txtPos.setGenerateMipmaps(false);
		txtPos.setup();
		cache.addTexture(txtPos);
		framebuffer.attachTexture(FrameBufferAttachment.COLOR_FIRST, WORLD_FRAMEBUFFER_POS_IDX, txtPos);

		final SingleTexture txtNormal = new SingleTexture(WORLD_FRAMEBUFFER_NAME + ".normal", width, height);
		txtNormal.setDataType(DataType.FLOAT);
		txtNormal.setFormat(TexelFormat.RGB);
		txtNormal.setInternalFormat(TexelInternalFormat.RGB16F);
		txtNormal.setFilters(TextureFilter.NEAREST);
		txtNormal.setGenerateMipmaps(false);
		txtNormal.setup();
		cache.addTexture(txtNormal);
		framebuffer.attachTexture(FrameBufferAttachment.COLOR_FIRST, WORLD_FRAMEBUFFER_NORMAL_IDX, txtNormal);

		final SingleTexture txtUV = new SingleTexture(WORLD_FRAMEBUFFER_NAME + ".uv", width, height);
		txtUV.setDataType(DataType.FLOAT);
		txtUV.setFormat(TexelFormat.RG);
		txtUV.setInternalFormat(TexelInternalFormat.RG16F);
		txtUV.setFilters(TextureFilter.NEAREST);
		txtUV.setGenerateMipmaps(false);
		txtUV.setup();
		cache.addTexture(txtUV);
		framebuffer.attachTexture(FrameBufferAttachment.COLOR_FIRST, WORLD_FRAMEBUFFER_UV_IDX, txtUV);

		final SingleTexture txtIDS = new SingleTexture(WORLD_FRAMEBUFFER_NAME + ".ids", width, height);
		txtIDS.setDataType(DataType.FLOAT);
		txtIDS.setFormat(TexelFormat.RGBA);
		txtIDS.setInternalFormat(TexelInternalFormat.RGBA16F);
		txtIDS.setFilters(TextureFilter.NEAREST);
		txtIDS.setGenerateMipmaps(false);
		txtIDS.setup();
		cache.addTexture(txtIDS);
		framebuffer.attachTexture(FrameBufferAttachment.COLOR_FIRST, WORLD_FRAMEBUFFER_IDS_IDX, txtIDS);

		framebuffer.setup();
		framebuffer.unbind();

		GlobalLogger.log("Created framebuffer: " + framebuffer.getId() + " (" + width + "x" + height + ")");

		return framebuffer;
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
