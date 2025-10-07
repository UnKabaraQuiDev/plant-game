package lu.kbra.plant_game.engine.render;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;

import lu.pcy113.pclib.logger.GlobalLogger;

import lu.kbra.plant_game.engine.entity.impl.GameObject;
import lu.kbra.plant_game.engine.entity.water.AnimatedGameObject;
import lu.kbra.plant_game.engine.mesh.AnimatedMesh;
import lu.kbra.plant_game.engine.mesh.AttributeLocation;
import lu.kbra.plant_game.engine.mesh.MaterialMesh;
import lu.kbra.plant_game.engine.mesh.TexturedMesh;
import lu.kbra.plant_game.engine.render.shader.BlitShader;
import lu.kbra.plant_game.engine.render.shader.MaterialComputeShader;
import lu.kbra.plant_game.engine.render.shader.TextureMaterialComputeShader;
import lu.kbra.plant_game.engine.render.shader.TransferShader;
import lu.kbra.plant_game.engine.scene.WorldLevelScene;
import lu.kbra.standalone.gameengine.GameEngine;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.cache.attrib.UIntAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.Vec2fAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.Vec3fAttribArray;
import lu.kbra.standalone.gameengine.geom.LoadedMesh;
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

	private static Mesh SCREEN = new LoadedMesh(PASS_SCREEN, null,
			new Vec3fAttribArray("pos", 0, 1,
					new Vector3f[] { new Vector3f(-1, 1, 0), new Vector3f(1, 1, 0), new Vector3f(1, -1, 0),
							new Vector3f(-1, -1, 0) }),
			new UIntAttribArray("ind", -1, 1, new int[] { 0, 1, 2, 0, 2, 3 }, BufferType.ELEMENT_ARRAY),
			new Vec2fAttribArray("uv", 1, 1,
					new Vector2f[] { new Vector2f(0, 1), new Vector2f(1, 1), new Vector2f(1, 0), new Vector2f(0, 0) }));

	protected SingleTexture depthTexture, posTexture, normalTexture, uvTexture, idsTexture;
	protected Framebuffer worldFramebuffer;
	protected TransferShader transferShader;

	protected SingleTexture outputTxt;
	protected MaterialComputeShader materialComputeShader;
	protected TextureMaterialComputeShader textureMaterialComputeShader;
	protected BlitShader blitShader;

	protected Vector2i resolution = new Vector2i(0), outputResolution = new Vector2i(0);

	public void render(GameEngine engine, WorldLevelScene worldScene, Scene2D uiScene, CacheManager worldCache,
			CacheManager uiCache) {
		final int width = engine.getWindow().getWidth();
		final int height = engine.getWindow().getHeight();

		final boolean needRegen = !resolution.equals(width, height);
		final CacheManager cache = engine.getCache();

		if (needRegen) {
			resolution = new Vector2i(width, height);
			outputResolution = new Vector2i(width, height).div(4);
		}

		renderWorldScene(cache, worldScene, worldCache, width, height, needRegen);

		renderMaterials(cache, worldScene, worldCache, width, height, needRegen);

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

	private void renderMaterials(CacheManager cache, WorldLevelScene worldScene, CacheManager worldCache, int width,
			int height, boolean needRegen) {
		if (materialComputeShader == null) {
			materialComputeShader = cache.hasAbstractShader(MaterialComputeShader.class.getName())
					? (MaterialComputeShader) cache.getAbstractShader(MaterialComputeShader.class.getName())
					: new MaterialComputeShader();
			cache.addAbstractShader(materialComputeShader);
		}
		if (textureMaterialComputeShader == null) {
			textureMaterialComputeShader = cache.hasAbstractShader(TextureMaterialComputeShader.class.getName())
					? (TextureMaterialComputeShader) cache
							.getAbstractShader(TextureMaterialComputeShader.class.getName())
					: new TextureMaterialComputeShader();
			cache.addAbstractShader(textureMaterialComputeShader);
		}

		if (outputTxt == null) {
			outputTxt = new SingleTexture("output", outputResolution);
			outputTxt.setDataType(DataType.UBYTE);
			outputTxt.setFormat(TexelFormat.RGBA);
			outputTxt.setInternalFormat(TexelInternalFormat.RGBA8);
			outputTxt.setFilters(TextureFilter.NEAREST);
			outputTxt.setGenerateMipmaps(false);
			outputTxt.setup();
			cache.addTexture(outputTxt);
		} else if (needRegen) {
			outputTxt.setSize(outputResolution);
			outputTxt.bind();
			outputTxt.resize();
			outputTxt.unbind();
		} else {
			GL_W.glClearTexImage(outputTxt.getTid(), 0, outputTxt.getFormat().getGlId(),
					outputTxt.getDataType().getGlId(), new float[] { 0, 0, 0, 0 });
			assert GL_W.checkError("ClearTexImage(" + outputTxt.getTid() + ")");
		}

		final int groupsX = (width + 15) / 16;
		final int groupsY = (height + 15) / 16;

		materialComputeShader.bind();

		setupMaterialInputs(materialComputeShader, worldScene, needRegen);

		GL_W.glDispatchCompute(groupsX, groupsY, 1);
		assert GL_W.checkError("DispatchCompute(" + groupsX + "," + groupsY + ",1)");

		GL_W.glMemoryBarrier(GL_W.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT);
		assert GL_W.checkError("MemoryBarrier(SHADER_IMAGE_ACCESS_BARRIER_BIT)");

		materialComputeShader.unbind();

		// txt materials

		textureMaterialComputeShader.bind();

		setupMaterialInputs(textureMaterialComputeShader, worldScene, needRegen);
		final int txt0UniformLoc = textureMaterialComputeShader.getUniformLocation(TextureMaterialComputeShader.TXT0);
		final int currentMaterialIdLoc = textureMaterialComputeShader
				.getUniformLocation(TextureMaterialComputeShader.CURRENT_MATERIAL_ID);

		final Set<Integer> alreadyRendered = new HashSet<>();

		synchronized (worldScene.getEntitiesLock()) {

			for (Entity entity : worldScene.getEntities().values()) {
				if (entity.hasComponentMatching(MeshComponent.class)) {
					final List<MeshComponent> meshComponents = entity.getComponentsMatching(MeshComponent.class);

					for (MeshComponent meshComponent : meshComponents) {
						final Mesh mesh = meshComponent.getMesh();

						if (mesh instanceof TexturedMesh txtMesh) {

							final SingleTexture txt = txtMesh.getTexture();
							final int tid = txt.getTid();
							if (alreadyRendered.contains(tid))
								continue;

							txt.bind(0);
							txt.bindUniform(txt0UniformLoc, 0);

							GL_W.glUniform1ui(currentMaterialIdLoc, tid);
							assert GL_W.checkError("Uniform1ui(" + currentMaterialIdLoc + "," + tid + ")");

							GL_W.glDispatchCompute(groupsX, groupsY, 1);
							assert GL_W.checkError("DispatchCompute(" + groupsX + "," + groupsY + ",1)");

							alreadyRendered.add(tid);

							GL_W.glMemoryBarrier(GL_W.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT);
							assert GL_W.checkError("MemoryBarrier(SHADER_IMAGE_ACCESS_BARRIER_BIT)");
						}
					}
				}
			}

		}

		textureMaterialComputeShader.unbind();
	}

	private void setupMaterialInputs(ComputeShader computeShader, WorldLevelScene worldScene, boolean needRegen) {
		posTexture.bind(0);
		normalTexture.bind(1);
		uvTexture.bind(2);
		idsTexture.bind(3);
		depthTexture.bind(4);

		computeShader.setUniform(MaterialComputeShader.LIGHT_COLOR, worldScene.getLightColor());
		computeShader.setUniform(MaterialComputeShader.LIGHT_DIR, worldScene.getLightDirection());
		computeShader.setUniform(MaterialComputeShader.AMBIENT_LIGHT, worldScene.getAmbientLight());

		if (needRegen) {
			GL_W.glBindImageTexture(0, outputTxt.getTid(), 0, false, 0, GL_W.GL_WRITE_ONLY,
					outputTxt.getInternalFormat().getGlId());
			assert GL_W.checkError("BindImageTexture(0," + outputTxt.getTid() + ",WRITE_ONLY)");

			posTexture.bindUniform(computeShader.getUniformLocation("uPosTex"), 0);
			normalTexture.bindUniform(computeShader.getUniformLocation("uNormalTex"), 1);
			uvTexture.bindUniform(computeShader.getUniformLocation("uUvTex"), 2);
			idsTexture.bindUniform(computeShader.getUniformLocation("uIdsTex"), 3);
			depthTexture.bindUniform(computeShader.getUniformLocation("uDepthTex"), 4);

			computeShader.setUniform(ComputeShader.INPUT_SIZE, resolution);
			computeShader.setUniform(ComputeShader.OUTPUT_SIZE, outputResolution);
		}
	}

	private void renderWorldScene(CacheManager cache, WorldLevelScene worldScene, CacheManager worldCache, int width,
			int height, boolean needRegen) {

		if (transferShader == null) {
			transferShader = cache.hasAbstractShader(TransferShader.class.getName())
					? (TransferShader) cache.getAbstractShader(TransferShader.class.getName())
					: new TransferShader();
			cache.addAbstractShader(transferShader);
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

				if (entity.hasComponentMatching(RenderConditionComponent.class)
						&& entity.getComponentsMatching(RenderConditionComponent.class).parallelStream()
								.allMatch(RenderConditionComponent::get)) {
					return;
				}

				if (entity.hasComponentMatching(MeshComponent.class)) {
					final List<MeshComponent> meshComponents = entity.getComponentsMatching(MeshComponent.class);

					for (MeshComponent meshComponent : meshComponents) {
						final Mesh mesh = meshComponent.getMesh();
						if (mesh == null) {
							throw new NullPointerException("Mesh is null on: "
									+ meshComponent.getClass().getSimpleName() + " in " + entity.getId());
						}
						if (!mesh.isValid()) {
							throw new IllegalStateException(
									"Mesh: " + mesh + " in " + entity.getId() + " isn't valid.");
						}
						mesh.bind();

						if (transferShader.createUniform(RenderShader.TRANSFORMATION_MATRIX)) {
							final Matrix4f transformationMatrix;

							if (entity instanceof AnimatedGameObject ago && mesh instanceof AnimatedMesh) {
								transformationMatrix = ago.getAnimatedTransform();
							} else if (entity instanceof GameObject go) {
								transformationMatrix = go.getTransform().getMatrix();
							} else if (entity.hasComponentMatching(TransformComponent.class)) {
								final TransformComponent transform = (TransformComponent) entity
										.getComponentMatching(TransformComponent.class);
								if (transform != null && transform.getTransform() != null) {
									transformationMatrix = transform.getTransform().getMatrix();
								} else {
									transformationMatrix = GameEngine.IDENTITY_MATRIX4F;
								}
							} else {
								transformationMatrix = GameEngine.IDENTITY_MATRIX4F;
							}

							transferShader.setUniform(RenderShader.TRANSFORMATION_MATRIX, transformationMatrix);
						}

						if (entity instanceof GameObject go && go.isEntityMaterialId()) {
							// id is in the entity
							final int matId = go.getMaterialId();

							GL_W.glDisableVertexAttribArray(GameObject.MESH_ATTRIB_MATERIAL_ID_ID);
							assert GL_W.checkError(
									"DisableVertexAttribArray(" + GameObject.MESH_ATTRIB_MATERIAL_ID_ID + ")");
							GL_W.glVertexAttribI1ui(GameObject.MESH_ATTRIB_MATERIAL_ID_ID, matId);
							assert GL_W.checkError(
									"VertexAttrib1f(" + GameObject.MESH_ATTRIB_MATERIAL_ID_ID + "," + matId + ")");
						} else if (mesh instanceof TexturedMesh txtMesh) { // id in the texture id
							final int txtId = txtMesh.getTexture().getTid();

							GL_W.glDisableVertexAttribArray(GameObject.MESH_ATTRIB_MATERIAL_ID_ID);
							assert GL_W.checkError(
									"DisableVertexAttribArray(" + GameObject.MESH_ATTRIB_MATERIAL_ID_ID + ")");
							GL_W.glVertexAttribI1ui(GameObject.MESH_ATTRIB_MATERIAL_ID_ID, txtId);
							assert GL_W.checkError(
									"VertexAttrib1f(" + GameObject.MESH_ATTRIB_MATERIAL_ID_ID + "," + txtId + ")");
						} else if (mesh instanceof MaterialMesh matMesh) { // id is in the material
							final int matId = matMesh.getMaterialId();

							GL_W.glDisableVertexAttribArray(GameObject.MESH_ATTRIB_MATERIAL_ID_ID);
							assert GL_W.checkError(
									"DisableVertexAttribArray(" + GameObject.MESH_ATTRIB_MATERIAL_ID_ID + ")");
							GL_W.glVertexAttribI1ui(GameObject.MESH_ATTRIB_MATERIAL_ID_ID, matId);
							assert GL_W.checkError(
									"VertexAttrib1f(" + GameObject.MESH_ATTRIB_MATERIAL_ID_ID + "," + matId + ")");
						}

						if (entity instanceof GameObject go && go.getObjectIdLocation() != AttributeLocation.MESH) {
							// object id is in the entity
							final Vector3i objId = go.getObjectId();
							GL_W.glDisableVertexAttribArray(GameObject.MESH_ATTRIB_OBJECT_ID_ID);
							assert GL_W.checkError(
									"DisableVertexAttribArray(" + GameObject.MESH_ATTRIB_OBJECT_ID_ID + ")");
							GL_W.glVertexAttribI3ui(GameObject.MESH_ATTRIB_OBJECT_ID_ID, objId.x, objId.y, objId.z);
							assert GL_W.checkError(
									"VertexAttrib3f(" + GameObject.MESH_ATTRIB_OBJECT_ID_ID + "," + objId + ")");
						}

						GL_W.glDrawElements(transferShader.getBeginMode().getGlId(), mesh.getIndicesCount(),
								GL_W.GL_UNSIGNED_INT, 0);
						assert GL_W.checkError("DrawElements(" + mesh.getId() + ")");

						mesh.unbind();
					}
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
		idsTexture.setDataType(DataType.UBYTE);
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
