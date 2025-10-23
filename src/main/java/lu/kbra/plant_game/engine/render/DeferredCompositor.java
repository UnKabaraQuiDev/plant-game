package lu.kbra.plant_game.engine.render;

import java.nio.IntBuffer;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.joml.Vector4f;
import org.joml.Vector4i;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import lu.pcy113.pclib.PCUtils;
import lu.pcy113.pclib.logger.GlobalLogger;
import lu.pcy113.pclib.pointer.prim.BooleanPointer;

import lu.kbra.plant_game.engine.entity.impl.GameObject;
import lu.kbra.plant_game.engine.entity.water.AnimatedMeshComponent;
import lu.kbra.plant_game.engine.entity.water.AnimatedTransformOwner;
import lu.kbra.plant_game.engine.mesh.AnimatedMesh;
import lu.kbra.plant_game.engine.mesh.AttributeLocation;
import lu.kbra.plant_game.engine.mesh.MaterialMesh;
import lu.kbra.plant_game.engine.mesh.TexturedMesh;
import lu.kbra.plant_game.engine.render.shader.BlitShader;
import lu.kbra.plant_game.engine.render.shader.DirectShader;
import lu.kbra.plant_game.engine.render.shader.InstanceDirectShader;
import lu.kbra.plant_game.engine.render.shader.InstanceTransferShader;
import lu.kbra.plant_game.engine.render.shader.MaterialComputeShader;
import lu.kbra.plant_game.engine.render.shader.OutlineShader;
import lu.kbra.plant_game.engine.render.shader.TextureMaterialComputeShader;
import lu.kbra.plant_game.engine.render.shader.TransferShader;
import lu.kbra.plant_game.engine.scene.ui.UIScene;
import lu.kbra.plant_game.engine.scene.world.WorldLevelScene;
import lu.kbra.standalone.gameengine.GameEngine;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.cache.attrib.UIntAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.Vec2fAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.Vec3fAttribArray;
import lu.kbra.standalone.gameengine.geom.LoadedMesh;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.geom.instance.InstanceEmitter;
import lu.kbra.standalone.gameengine.graph.composition.buffer.Framebuffer;
import lu.kbra.standalone.gameengine.graph.composition.buffer.RenderBuffer;
import lu.kbra.standalone.gameengine.graph.material.text.TextShader;
import lu.kbra.standalone.gameengine.graph.shader.ComputeShader;
import lu.kbra.standalone.gameengine.graph.shader.RenderShader;
import lu.kbra.standalone.gameengine.graph.texture.SingleTexture;
import lu.kbra.standalone.gameengine.graph.window.Window;
import lu.kbra.standalone.gameengine.impl.Cleanupable;
import lu.kbra.standalone.gameengine.impl.FramebufferAttachment;
import lu.kbra.standalone.gameengine.impl.GLObject;
import lu.kbra.standalone.gameengine.impl.Renderable;
import lu.kbra.standalone.gameengine.objs.entity.Component;
import lu.kbra.standalone.gameengine.objs.entity.Entity;
import lu.kbra.standalone.gameengine.objs.entity.SceneEntity;
import lu.kbra.standalone.gameengine.objs.entity.components.InstanceEmitterComponent;
import lu.kbra.standalone.gameengine.objs.entity.components.MeshComponent;
import lu.kbra.standalone.gameengine.objs.entity.components.RenderConditionComponent;
import lu.kbra.standalone.gameengine.objs.entity.components.TextEmitterComponent;
import lu.kbra.standalone.gameengine.objs.entity.components.TransformComponent;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;
import lu.kbra.standalone.gameengine.scene.Scene;
import lu.kbra.standalone.gameengine.scene.camera.Camera;
import lu.kbra.standalone.gameengine.utils.MathUtils;
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
	private static final long POLL_OBJECT_ID_TIMEOUT = 1_000;

	public static final String SCREEN_WIDTH = "screen_width";
	public static final String SCREEN_HEIGHT = "screen_height";

	private static Mesh SCREEN = new LoadedMesh(PASS_SCREEN, null,
			new Vec3fAttribArray("pos", 0, 1,
					new Vector3f[] { new Vector3f(-1, 1, 0), new Vector3f(1, 1, 0), new Vector3f(1, -1, 0), new Vector3f(-1, -1, 0) }),
			new UIntAttribArray("ind", -1, 1, new int[] { 0, 1, 2, 0, 2, 3 }, BufferType.ELEMENT_ARRAY), new Vec2fAttribArray("uv", 1, 1,
					new Vector2f[] { new Vector2f(0, 1), new Vector2f(1, 1), new Vector2f(1, 0), new Vector2f(0, 0) }));

	protected Thread ownerThread;

	// world rendering
	protected SingleTexture depthTexture, posTexture, normalTexture, uvTexture, idsTexture;
	protected Framebuffer worldFramebuffer;
	protected TransferShader transferShader;
	protected InstanceTransferShader instanceTransferShader;
	protected Map<Class<? extends Renderable>, RenderShader> worldSceneShaders;

	// ui rendering
	protected DirectShader directShader;
	protected InstanceDirectShader instanceDirectShader;
	protected TextShader textDirectShader;
	protected Map<Class<? extends Renderable>, RenderShader> uiSceneShaders;

	// material passes
	protected Vector4f backgroundColor = new Vector4f(0, 0, 0, 1);
	protected SingleTexture outputTxt;
	protected MaterialComputeShader materialComputeShader;
	protected TextureMaterialComputeShader textureMaterialComputeShader;
	protected BlitShader blitShader;

	// rendering/states
	protected float oldFov;
	protected Vector2i oldResolution = new Vector2i(0), renderResolution = new Vector2i(), outputResolution = new Vector2i();

	// object ids
	protected BooleanPointer awaitingObjectIdPtr = new BooleanPointer(false);
	protected Vector4i objectId = new Vector4i();

	// highlights
	protected volatile boolean outlineChanged = false;
	protected Map<Vector3i, Vector4f> outlinedObjects = new ConcurrentHashMap<>();
	protected OutlineShader outlineShader;

	public DeferredCompositor(GameEngine engine, Thread ownerThread) {
		final CacheManager cache = engine.getCache();
		this.ownerThread = ownerThread;

		cache.addAbstractShader(transferShader = new TransferShader());
		cache.addAbstractShader(instanceTransferShader = new InstanceTransferShader());

		cache.addAbstractShader(materialComputeShader = new MaterialComputeShader());
		cache.addAbstractShader(textureMaterialComputeShader = new TextureMaterialComputeShader());

		cache.addAbstractShader(outlineShader = new OutlineShader());

		cache.addAbstractShader(blitShader = new BlitShader());

		cache.addAbstractShader(directShader = new DirectShader());
		cache.addAbstractShader(instanceDirectShader = new InstanceDirectShader());

		outputTxt = new SingleTexture(WORLD_FRAMEBUFFER_NAME + ".output", engine.getWindow().getSize());
		outputTxt.setDataType(DataType.HALF_FLOAT);
		outputTxt.setFormat(TexelFormat.RGBA);
		outputTxt.setInternalFormat(TexelInternalFormat.RGBA16F);
		outputTxt.setFilters(TextureFilter.NEAREST);
		outputTxt.setGenerateMipmaps(false);
		outputTxt.setup();
		cache.addTexture(outputTxt);

		cache.addFramebuffer(worldFramebuffer = genFramebuffer(cache, engine.getWindow().getSize()));

		worldSceneShaders = PCUtils
				.hashMap(Mesh.class,
						transferShader,
						AnimatedMesh.class,
						transferShader,
						TextEmitter.class,
						null,
						InstanceEmitter.class,
						instanceTransferShader);

		uiSceneShaders = PCUtils
				.hashMap(Mesh.class,
						directShader,
						AnimatedMesh.class,
						directShader,
						TextEmitter.class,
						textDirectShader,
						InstanceEmitter.class,
						instanceDirectShader);
	}

	public void render(GameEngine engine, WorldLevelScene worldScene, UIScene uiScene) {
		final int width = engine.getWindow().getWidth();
		final int height = engine.getWindow().getHeight();

		final float newFov = worldScene.getCamera().getProjection().getFov();
		final boolean needRegen = !oldResolution.equals(width, height) || oldFov != newFov;
		oldFov = newFov;
		final CacheManager cache = engine.getCache();

		if (needRegen) {
			oldResolution.set(width, height);
			final float divisor = (float) MathUtils.map(Math.toDegrees(worldScene.getCamera().getProjection().getFov()), 5, 65, 6, 1);
			// System.err.println("fov: " + worldScene.getCamera().getProjection().getFov()
			// + "rad "
			// + Math.toDegrees(worldScene.getCamera().getProjection().getFov()) + "deg = "
			// + divisor);
			// idk why this uses the option windowSize but ok
			renderResolution.set(engine.getWindow().getOptions().windowSize).div(PCUtils.clamp(1, 100, divisor));
			outputResolution.set(width, height);

			resizeFramebuffer(worldFramebuffer, renderResolution);
		}

		renderWorldScene(cache, worldScene, renderResolution, needRegen);

		renderMaterials(cache, worldScene, renderResolution, needRegen);

		renderOutlines(cache, renderResolution, needRegen);

		blitToScreen(cache, outputResolution, needRegen);

		renderUi(cache, uiScene, outputResolution, needRegen);

		if (awaitingObjectIdPtr.getValue()) {
			getObjectId(engine.getWindow());
			awaitingObjectIdPtr.setValue(false);
			GlobalLogger.log(Level.FINEST, "Retrieved object id: " + objectId + " at " + engine.getWindow().getMousePosition());
		} else {
			objectId.zero();
		}
	}

	private void renderUi(CacheManager cache, UIScene uiScene, Vector2i outputResolution, boolean needRegen) {
		final CacheManager uiCache = uiScene.getCache();

		GL_W.glBindFramebuffer(GL_W.GL_FRAMEBUFFER, 0);
		assert GL_W.checkError("BindFramebuffer(0)");

		GL_W.glViewport(0, 0, outputResolution.x, outputResolution.y);
		assert GL_W.checkError("Viewport(" + outputResolution + ")");

		GL_W.glDisable(GL_W.GL_CULL_FACE);

		synchronized (uiScene.getEntitiesLock()) {
			renderScene(uiScene, uiSceneShaders);
		}
	}

	private void checkComponent(GLObject mesh, Component source, Entity entity) {
		if (mesh == null) {
			throw new NullPointerException("Mesh is null on: " + source.getClass().getSimpleName() + " in " + entity.getId());
		}
		if (!mesh.isValid()) {
			throw new IllegalStateException("Mesh: " + mesh + " in " + entity.getId() + " isn't valid.");
		}
	}

	private void renderOutlines(CacheManager cache, Vector2i resolution, boolean needRegen) {
		GL_W.glViewport(0, 0, resolution.x, resolution.y);
		assert GL_W.checkError("Viewport(" + resolution + ")");

		final int groupsX = (resolution.x + 15) / 16;
		final int groupsY = (resolution.y + 15) / 16;

		assert groupsX != 0;
		assert groupsY != 0;

		outlineShader.bind();

		setupOutlineInputs(outlineShader, resolution, needRegen);

		GL_W.glDispatchCompute(groupsX, groupsY, 1);
		assert GL_W.checkError("DispatchCompute(" + groupsX + "," + groupsY + ",1)");

		GL_W.glMemoryBarrier(GL_W.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT);
		assert GL_W.checkError("MemoryBarrier(SHADER_IMAGE_ACCESS_BARRIER_BIT)");

		outlineShader.unbind();
	}

	private void setupOutlineInputs(OutlineShader computeShader, Vector2i resolution, boolean needRegen) {
		posTexture.bind(0);
		normalTexture.bind(1);
		uvTexture.bind(2);
		idsTexture.bind(3);
		depthTexture.bind(4);

		if (outlineChanged) {
			synchronized (outlinedObjects) {
				computeShader.setUniform(OutlineShader.CURRENT_TARGET_COUNT, outlinedObjects.size());
				final Vector3i[] ids = new Vector3i[outlinedObjects.size()];
				final Vector4f[] colors = new Vector4f[outlinedObjects.size()];
				int i = 0;
				for (Entry<Vector3i, Vector4f> evv : outlinedObjects.entrySet()) {
					ids[i] = evv.getKey();
					colors[i] = evv.getValue();
					i++;
				}
				computeShader.setUniformUnsigned(OutlineShader.TARGET_IDS, ids);
				computeShader.setUniform(OutlineShader.TARGET_COLORS, colors);
			}
		}

		if (needRegen) {
			GL_W.glBindImageTexture(0, outputTxt.getTid(), 0, false, 0, GL_W.GL_READ_WRITE, outputTxt.getInternalFormat().getGlId());
			assert GL_W.checkError("BindImageTexture(0, " + outputTxt.getTid() + ", READ_WRITE)");

			posTexture.bindUniform(computeShader.getUniformLocation("uPosTex"), 0);
			normalTexture.bindUniform(computeShader.getUniformLocation("uNormalTex"), 1);
			uvTexture.bindUniform(computeShader.getUniformLocation("uUvTex"), 2);
			idsTexture.bindUniform(computeShader.getUniformLocation("uIdsTex"), 3);
			depthTexture.bindUniform(computeShader.getUniformLocation("uDepthTex"), 4);

			computeShader.setUniform(ComputeShader.INPUT_SIZE, resolution);
			computeShader.setUniform(ComputeShader.OUTPUT_SIZE, resolution);
		}
	}

	private void blitToScreen(CacheManager cache, Vector2i resolution, boolean needRegen) {
		blitShader.bind();
		if (needRegen) {
			blitShader.setUniform(ComputeShader.INPUT_SIZE, renderResolution);
			blitShader.setUniform(ComputeShader.OUTPUT_SIZE, resolution);
		}
		outputTxt.bindUniform(blitShader.getUniformLocation(BlitShader.TXT0), 0);

		SCREEN.bind();

		GL_W.glDisable(GL_W.GL_CULL_FACE);
		assert GL_W.checkError("Disable(CULL_FACE)");
		GL_W.glDisable(GL_W.GL_DEPTH_TEST);
		assert GL_W.checkError("Disable(DEPTH_TEST)");

		GL_W.glBindFramebuffer(GL_W.GL_FRAMEBUFFER, 0);
		assert GL_W.checkError("BindFramebuffer(0)");
		GL_W.glViewport(0, 0, resolution.x, resolution.y);
		assert GL_W.checkError("Viewport(" + resolution + ")");
		GL_W.glClearColor(backgroundColor.x, backgroundColor.y, backgroundColor.z, backgroundColor.w);
		assert GL_W.checkError("ClearColor(" + backgroundColor + ")");
		GL_W.glClear(GL_W.GL_COLOR_BUFFER_BIT | GL_W.GL_DEPTH_BUFFER_BIT);
		assert GL_W.checkError("Clear(COLOR | DEPTH)");

		GL_W.glDrawElements(blitShader.getBeginMode().getGlId(), SCREEN.getIndicesCount(), GL_W.GL_UNSIGNED_INT, 0);
		assert GL_W.checkError("DrawElements()");

		GL_W.glEnable(GL_W.GL_DEPTH_TEST);
		assert GL_W.checkError("Enable(DEPTH_TEST)");

		SCREEN.unbind();

		blitShader.unbind();
	}

	private void renderMaterials(CacheManager cache, WorldLevelScene worldScene, Vector2i resolution, boolean needRegen) {
		final CacheManager worldCache = worldScene.getCache();

		if (needRegen) {
			outputTxt.setSize(resolution);
			outputTxt.bind();
			outputTxt.resize();
			outputTxt.unbind();
		} else {
			GL_W
					.glClearTexImage(outputTxt
							.getTid(), 0, outputTxt.getFormat().getGlId(), outputTxt.getDataType().getGlId(), new float[] { 0, 0, 0, 0 });
			assert GL_W.checkError("ClearTexImage(" + outputTxt.getTid() + ")");
		}

		GL_W.glViewport(0, 0, resolution.x, resolution.y);
		assert GL_W.checkError("Viewport(" + resolution + ")");

		final int groupsX = (resolution.x + 15) / 16;
		final int groupsY = (resolution.y + 15) / 16;

		assert groupsX != 0;
		assert groupsY != 0;

		// regular materials

		materialComputeShader.bind();

		setupMaterialInputs(materialComputeShader, worldScene, resolution, needRegen);

		GL_W.glDispatchCompute(groupsX, groupsY, 1);
		assert GL_W.checkError("DispatchCompute(" + groupsX + "," + groupsY + ",1)");

		GL_W.glMemoryBarrier(GL_W.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT);
		assert GL_W.checkError("MemoryBarrier(SHADER_IMAGE_ACCESS_BARRIER_BIT)");

		// txt materials

		textureMaterialComputeShader.bind();

		setupMaterialInputs(textureMaterialComputeShader, worldScene, resolution, needRegen);
		final int txt0UniformLoc = textureMaterialComputeShader.getUniformLocation(TextureMaterialComputeShader.TXT0);
		final int currentMaterialIdLoc = textureMaterialComputeShader.getUniformLocation(TextureMaterialComputeShader.CURRENT_MATERIAL_ID);

		final Set<Integer> alreadyRendered = new HashSet<>();

		synchronized (worldScene.getEntitiesLock()) {

			for (Entity entity : worldScene.getEntities().values()) {
				if (entity.hasComponentMatching(MeshComponent.class)) {
					final List<MeshComponent> meshComponents = entity.getComponentsMatching(MeshComponent.class);

					for (MeshComponent meshComponent : meshComponents) {
						final Mesh mesh = meshComponent.getMesh();
						checkComponent(mesh, meshComponent, entity);

						if (mesh instanceof TexturedMesh txtMesh) {
							final SingleTexture txt = txtMesh.getTexture();
							final int tid = txt.getTid();
							if (alreadyRendered.contains(tid))
								continue;

							txt.bindUniform(txt0UniformLoc, 0);

							textureMaterialComputeShader.storeUniformUnsigned(currentMaterialIdLoc, tid);

							GL_W.glDispatchCompute(groupsX, groupsY, 1);
							assert GL_W.checkError("DispatchCompute(" + groupsX + "," + groupsY + ",1)");

							alreadyRendered.add(tid);

							GL_W.glMemoryBarrier(GL_W.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT);
							assert GL_W.checkError("MemoryBarrier(SHADER_IMAGE_ACCESS_BARRIER_BIT)");
						}
					}
				}

				if (entity.hasComponentMatching(InstanceEmitterComponent.class)) {
					PCUtils.throwUnsupported();

					final List<InstanceEmitterComponent> instanceEmitterComponents = entity
							.getComponentsMatching(InstanceEmitterComponent.class);

					for (InstanceEmitterComponent instanceEmitterComponent : instanceEmitterComponents) {
						final InstanceEmitter instances = instanceEmitterComponent.getInstanceEmitter();
						checkComponent(instances, instanceEmitterComponent, entity);
						final Mesh mesh = instances.getParticleMesh();

						if (mesh instanceof TexturedMesh txtMesh) {
							final SingleTexture txt = txtMesh.getTexture();
							final int tid = txt.getTid();
							if (alreadyRendered.contains(tid))
								continue;

							txt.bindUniform(txt0UniformLoc, 0);

							textureMaterialComputeShader.storeUniformUnsigned(currentMaterialIdLoc, tid);

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

	private void setupMaterialInputs(ComputeShader computeShader, WorldLevelScene worldScene, Vector2i resolution, boolean needRegen) {
		posTexture.bind(0);
		normalTexture.bind(1);
		uvTexture.bind(2);
		idsTexture.bind(3);
		depthTexture.bind(4);

		computeShader.setUniform(MaterialComputeShader.LIGHT_COLOR, worldScene.getLightColor());
		computeShader.setUniform(MaterialComputeShader.LIGHT_DIR, worldScene.getLightDirection());
		computeShader.setUniform(MaterialComputeShader.AMBIENT_LIGHT, worldScene.getAmbientLight());

		if (needRegen) {
			GL_W.glBindImageTexture(0, outputTxt.getTid(), 0, false, 0, GL_W.GL_WRITE_ONLY, outputTxt.getInternalFormat().getGlId());
			assert GL_W.checkError("BindImageTexture(0," + outputTxt.getTid() + ",WRITE_ONLY)");

			posTexture.bindUniform(computeShader.getUniformLocation("uPosTex"), 0);
			normalTexture.bindUniform(computeShader.getUniformLocation("uNormalTex"), 1);
			uvTexture.bindUniform(computeShader.getUniformLocation("uUvTex"), 2);
			idsTexture.bindUniform(computeShader.getUniformLocation("uIdsTex"), 3);
			depthTexture.bindUniform(computeShader.getUniformLocation("uDepthTex"), 4);

			computeShader.setUniform(ComputeShader.INPUT_SIZE, resolution);
			computeShader.setUniform(ComputeShader.OUTPUT_SIZE, resolution);
		}
	}

	private void renderWorldScene(CacheManager cache, WorldLevelScene worldScene, Vector2i resolution, boolean needRegen) {
		worldFramebuffer.bind();

		GL_W.glViewport(0, 0, resolution.x, resolution.y);
		assert GL_W.checkError("Viewport(" + resolution + ")");

		GL_W.glEnable(GL_W.GL_DEPTH_TEST);
		assert GL_W.checkError("Enable(DEPTH_TEST)");
		GL_W.glEnable(GL_W.GL_CULL_FACE);
		assert GL_W.checkError("Enable(CULL_FACE)");
		GL_W.glCullFace(GL_W.GL_BACK);
		assert GL_W.checkError("CullFace(BACK)");

		GL_W.glClearColor(0, 0, 0, 1);
		assert GL_W.checkError("ClearColor(0, 0, 0, 1)");
		GL_W.glClear(GL_W.GL_COLOR_BUFFER_BIT | GL_W.GL_DEPTH_BUFFER_BIT);
		assert GL_W.checkError("Clear(COLOR | DEPTH)");

		transferShader.bind();

		synchronized (worldScene.getEntitiesLock()) {
			renderScene(worldScene, worldSceneShaders);
		}

		transferShader.unbind();
		worldFramebuffer.unbind();
	}

	private void renderScene(Scene scene, Map<Class<? extends Renderable>, RenderShader> shaders) {
		final RenderShader meshShader = shaders.get(Mesh.class);
		final RenderShader animatedMeshShader = shaders.get(AnimatedMesh.class);
		final RenderShader textEmitterShader = shaders.get(TextEmitter.class);
		final RenderShader instanceEmitterShader = shaders.get(InstanceEmitter.class);

		setupUniforms(shaders, scene);

		for (Entity entity : scene) {

			if (!entity.isActive()) {
				continue;
			}

			if (entity.hasComponentMatching(RenderConditionComponent.class) && entity
					.getComponentsMatching(RenderConditionComponent.class)
					.parallelStream()
					.allMatch(RenderConditionComponent::get)) {
				continue;
			}

			final Matrix4f transformationMatrix;
			if (entity instanceof GameObject go) {
				transformationMatrix = go.getTransform().getMatrix();
			} else if (entity.hasComponentMatching(TransformComponent.class)) {
				final TransformComponent transform = (TransformComponent) entity.getComponentMatching(TransformComponent.class);
				if (transform != null && transform.getTransform() != null) {
					transformationMatrix = transform.getTransform().getMatrix();
				} else {
					transformationMatrix = GameEngine.IDENTITY_MATRIX4F;
				}
			} else {
				transformationMatrix = GameEngine.IDENTITY_MATRIX4F;
			}

			if (meshShader != null && entity.hasComponentMatching(MeshComponent.class)) {
				final List<MeshComponent> meshComponents = entity.getComponentsMatching(MeshComponent.class);

				for (MeshComponent meshComponent : meshComponents) {
					final Mesh mesh = meshComponent.getMesh();
					renderMesh(mesh, meshComponent, entity, transformationMatrix, meshShader);
				}
			}

			if (animatedMeshShader != null && entity.hasComponentMatching(AnimatedMeshComponent.class)) {
				final List<AnimatedMeshComponent> animatedMeshComponents = entity.getComponentsMatching(AnimatedMeshComponent.class);

				final Matrix4f animatedTransformationMatrix = entity instanceof AnimatedTransformOwner ago ? ago.getAnimatedTransform()
						: transformationMatrix;

				for (AnimatedMeshComponent animatedMeshComponent : animatedMeshComponents) {
					final AnimatedMesh mesh = animatedMeshComponent.getAnimatedMesh();
					renderMesh(mesh, animatedMeshComponent, entity, animatedTransformationMatrix, animatedMeshShader);
				}
			}

			if (textEmitterShader != null && entity.hasComponentMatching(TextEmitterComponent.class)) {
				final List<TextEmitterComponent> textEmitterComponents = entity.getComponentsMatching(TextEmitterComponent.class);

				for (TextEmitterComponent textEmitterComponent : textEmitterComponents) {
					final TextEmitter textEmitter = textEmitterComponent.getTextEmitter();
					renderTextEmitter(textEmitter, textEmitterComponent, entity, transformationMatrix, textEmitterShader);
				}
			}

			if (instanceEmitterShader != null && entity.hasComponentMatching(InstanceEmitterComponent.class)) {
				final List<InstanceEmitterComponent> instanceEmitterComponents = entity
						.getComponentsMatching(InstanceEmitterComponent.class);

				for (InstanceEmitterComponent instanceEmitterComponent : instanceEmitterComponents) {
					final InstanceEmitter instanceEmitter = instanceEmitterComponent.getInstanceEmitter();
					renderInstanceEmitter(instanceEmitter, instanceEmitterComponent, entity, transformationMatrix, instanceEmitterShader);
				}
			}
		}
	}

	private void setupUniforms(Map<Class<? extends Renderable>, RenderShader> shaders, Scene scene) {
		final Camera cam = scene.getCamera();
		cam.getProjection().update(outputResolution);
		final Matrix4f viewMatrix = cam.getViewMatrix(), projectionMatrix = cam.getProjection().getProjectionMatrix();

		for (RenderShader shader : shaders.values()) {
			if (shader == null) {
				continue;
			}

			shader.bind();

			shader.setUniform(RenderShader.VIEW_MATRIX, viewMatrix);
			shader.setUniform(RenderShader.PROJECTION_MATRIX, projectionMatrix);
		}

		GL_W.glUseProgram(0);
		assert GL_W.checkError("UseProgram(0)");
	}

	private void renderInstanceEmitter(
			InstanceEmitter obj,
			InstanceEmitterComponent component,
			Entity entity,
			Matrix4f transformationMatrix,
			RenderShader shader) {
		checkComponent(obj, component, entity);
		final Mesh mesh = obj.getParticleMesh();
		obj.bind();
		shader.bind();

		if (shader.createUniform(RenderShader.TRANSFORMATION_MATRIX)) {
			shader.setUniform(RenderShader.TRANSFORMATION_MATRIX, transformationMatrix);
		}

		GL_W
				.glDrawElementsInstanced(shader.getBeginMode().getGlId(),
						mesh.getIndicesCount(),
						GL_W.GL_UNSIGNED_INT,
						0,
						obj.getParticleCount());
		assert GL_W.checkError("DrawElementsInstanced(" + mesh.getId() + ")");

		shader.unbind();

		mesh.unbind();
	}

	private void renderTextEmitter(
			TextEmitter obj,
			Component component,
			Entity entity,
			Matrix4f transformationMatrix,
			RenderShader shader) {
		checkComponent(obj, component, entity);
		final InstanceEmitter instances = obj.getInstances();
		final Mesh mesh = instances.getParticleMesh();
		instances.bind();
		shader.bind();

		if (shader.createUniform(RenderShader.TRANSFORMATION_MATRIX)) {
			shader.setUniform(RenderShader.TRANSFORMATION_MATRIX, transformationMatrix);
		}

		GL_W.glEnable(GL_W.GL_BLEND);
		assert GL_W.checkError("Enable(BLEND)");
		GL_W.glBlendFunc(GL_W.GL_SRC_ALPHA, GL_W.GL_ONE_MINUS_SRC_ALPHA);
		assert GL_W.checkError("BlendFunc(SRC_ALPHA, ONE_MINUS_SRC_ALPHA)");

		GL_W
				.glDrawElementsInstanced(shader.getBeginMode().getGlId(),
						mesh.getIndicesCount(),
						GL_W.GL_UNSIGNED_INT,
						0,
						instances.getParticleCount());
		assert GL_W.checkError("DrawElementsInstanced(" + mesh.getId() + ")");

		GL_W.glDisable(GL_W.GL_BLEND);
		assert GL_W.checkError("Disable(BLEND)");

		mesh.unbind();
	}

	private void renderMesh(Mesh mesh, Component component, Entity entity, Matrix4f transformationMatrix, RenderShader shader) {
		checkComponent(mesh, component, entity);
		mesh.bind();
		shader.bind();

		if (shader.createUniform(RenderShader.TRANSFORMATION_MATRIX)) {
			shader.setUniform(RenderShader.TRANSFORMATION_MATRIX, transformationMatrix);
		}

		if (mesh instanceof TexturedMesh txtMesh && shader.createUniform(DirectShader.TXT0)) {
			txtMesh.getTexture().bindUniform(shader.getUniformLocation(DirectShader.TXT0), 0);
		}

		if (entity instanceof GameObject go && go.isEntityMaterialId()) {
			// id is in the entity
			final int matId = go.getMaterialId();

			GL_W.glDisableVertexAttribArray(GameObject.MESH_ATTRIB_MATERIAL_ID_ID);
			assert GL_W.checkError("DisableVertexAttribArray(" + GameObject.MESH_ATTRIB_MATERIAL_ID_ID + ")");
			GL_W.glVertexAttribI1ui(GameObject.MESH_ATTRIB_MATERIAL_ID_ID, matId);
			assert GL_W.checkError("VertexAttrib1f(" + GameObject.MESH_ATTRIB_MATERIAL_ID_ID + "," + matId + ")");
		} else if (mesh instanceof TexturedMesh txtMesh) { // id in the texture id
			final int txtId = txtMesh.getTexture().getTid();

			GL_W.glDisableVertexAttribArray(GameObject.MESH_ATTRIB_MATERIAL_ID_ID);
			assert GL_W.checkError("DisableVertexAttribArray(" + GameObject.MESH_ATTRIB_MATERIAL_ID_ID + ")");
			GL_W.glVertexAttribI1ui(GameObject.MESH_ATTRIB_MATERIAL_ID_ID, txtId);
			assert GL_W.checkError("VertexAttrib1f(" + GameObject.MESH_ATTRIB_MATERIAL_ID_ID + "," + txtId + ")");
		} else if (mesh instanceof MaterialMesh matMesh) { // id is in the material
			final int matId = matMesh.getMaterialId();

			GL_W.glDisableVertexAttribArray(GameObject.MESH_ATTRIB_MATERIAL_ID_ID);
			assert GL_W.checkError("DisableVertexAttribArray(" + GameObject.MESH_ATTRIB_MATERIAL_ID_ID + ")");
			GL_W.glVertexAttribI1ui(GameObject.MESH_ATTRIB_MATERIAL_ID_ID, matId);
			assert GL_W.checkError("VertexAttrib1f(" + GameObject.MESH_ATTRIB_MATERIAL_ID_ID + "," + matId + ")");
		}

		if (entity instanceof GameObject go && go.getObjectIdLocation() != AttributeLocation.MESH) {
			// object id is in the entity
			final Vector3i objId = go.getObjectId();
			GL_W.glDisableVertexAttribArray(GameObject.MESH_ATTRIB_OBJECT_ID_ID);
			assert GL_W.checkError("DisableVertexAttribArray(" + GameObject.MESH_ATTRIB_OBJECT_ID_ID + ")");
			GL_W.glVertexAttribI3ui(GameObject.MESH_ATTRIB_OBJECT_ID_ID, objId.x, objId.y, objId.z);
			assert GL_W.checkError("VertexAttrib3f(" + GameObject.MESH_ATTRIB_OBJECT_ID_ID + "," + objId + ")");
		}

		GL_W.glDrawElements(shader.getBeginMode().getGlId(), mesh.getIndicesCount(), GL_W.GL_UNSIGNED_INT, 0);
		assert GL_W.checkError("DrawElements(" + mesh.getId() + ")");

		mesh.unbind();
	}

	private void resizeFramebuffer(Framebuffer fb, Vector2i resolution) {
		fb.bind();
		for (FramebufferAttachment fa : fb.getAttachments().values()) {
			if (fa instanceof SingleTexture txt) {
				txt.setSize(resolution.x, resolution.y);
				txt.bind();
				txt.resize();
			} else if (fa instanceof RenderBuffer rb) {
				rb.setSize(resolution.x, resolution.y);
				rb.bind();
				rb.resize();
			}
		}
		fb.unbind();

		GlobalLogger.log("Resized framebuffer: " + fb.getId() + " (" + resolution + ")");
	}

	private Framebuffer genFramebuffer(final CacheManager cache, Vector2i resolution) {
		final Framebuffer framebuffer = new Framebuffer(WORLD_FRAMEBUFFER_NAME);
		framebuffer.gen();
		framebuffer.bind();

		final int width = resolution.x, height = resolution.y;

		depthTexture = new SingleTexture(WORLD_FRAMEBUFFER_NAME + ".depth", width, height);
		depthTexture.setDataType(DataType.FLOAT);
		depthTexture.setFormat(TexelFormat.DEPTH);
		depthTexture.setInternalFormat(TexelInternalFormat.DEPTH_COMPONENT24);
		depthTexture.setFilters(TextureFilter.NEAREST);
		depthTexture.setWraps(TextureWrap.CLAMP_TO_EDGE);
		depthTexture.setGenerateMipmaps(false);
		depthTexture.setup();
		cache.addTexture(depthTexture);
		framebuffer.attachTexture(FrameBufferAttachment.DEPTH, depthTexture);

		posTexture = new SingleTexture(WORLD_FRAMEBUFFER_NAME + ".pos", width, height);
		posTexture.setDataType(DataType.FLOAT);
		posTexture.setFormat(TexelFormat.RGB);
		posTexture.setInternalFormat(TexelInternalFormat.RGB32F);
		posTexture.setFilters(TextureFilter.NEAREST);
		posTexture.setGenerateMipmaps(false);
		posTexture.setup();
		cache.addTexture(posTexture);
		framebuffer.attachTexture(FrameBufferAttachment.COLOR_FIRST, WORLD_FRAMEBUFFER_POS_IDX, posTexture);

		normalTexture = new SingleTexture(WORLD_FRAMEBUFFER_NAME + ".normal", width, height);
		normalTexture.setDataType(DataType.FLOAT);
		normalTexture.setFormat(TexelFormat.RGB);
		normalTexture.setInternalFormat(TexelInternalFormat.RGB16F);
		normalTexture.setFilters(TextureFilter.NEAREST);
		normalTexture.setGenerateMipmaps(false);
		normalTexture.setup();
		cache.addTexture(normalTexture);
		framebuffer.attachTexture(FrameBufferAttachment.COLOR_FIRST, WORLD_FRAMEBUFFER_NORMAL_IDX, normalTexture);

		uvTexture = new SingleTexture(WORLD_FRAMEBUFFER_NAME + ".uv", width, height);
		uvTexture.setDataType(DataType.FLOAT);
		uvTexture.setFormat(TexelFormat.RG);
		uvTexture.setInternalFormat(TexelInternalFormat.RG16F);
		uvTexture.setFilters(TextureFilter.NEAREST);
		uvTexture.setGenerateMipmaps(false);
		uvTexture.setup();
		cache.addTexture(uvTexture);
		framebuffer.attachTexture(FrameBufferAttachment.COLOR_FIRST, WORLD_FRAMEBUFFER_UV_IDX, uvTexture);

		idsTexture = new SingleTexture(WORLD_FRAMEBUFFER_NAME + ".ids", width, height);
		idsTexture.setDataType(DataType.UINT);
		idsTexture.setFormat(TexelFormat.RGBA_INTEGER);
		idsTexture.setInternalFormat(TexelInternalFormat.RGBA32UI);
		idsTexture.setFilters(TextureFilter.NEAREST);
		idsTexture.setGenerateMipmaps(false);
		idsTexture.setup();
		cache.addTexture(idsTexture);
		framebuffer.attachTexture(FrameBufferAttachment.COLOR_FIRST, WORLD_FRAMEBUFFER_IDS_IDX, idsTexture);

		framebuffer.setup();
		framebuffer.unbind();

		GlobalLogger.log("Created framebuffer: " + framebuffer.getId() + " (" + resolution + ")");

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

	public boolean pollObjectId(boolean blocking) {
		awaitingObjectIdPtr.setValue(true);
		if (blocking) {
			return awaitObjectId();
		}
		return !awaitingObjectIdPtr.getValue();
	}

	public boolean isAwaitingObjectId() {
		return awaitingObjectIdPtr.getValue();
	}

	public boolean awaitObjectId() {
		awaitingObjectIdPtr.waitForFalse(POLL_OBJECT_ID_TIMEOUT);
		return !awaitingObjectIdPtr.getValue();
	}

	public Vector4i getObjectId() {
		return objectId;
	}

	protected void getObjectId(Window window) {
		if (idsTexture == null)
			throw new IllegalStateException("Ids texture is not ready.");
		assert ownerThread == Thread.currentThread();

		final Vector2f mousePosition = window.getMousePosition();
		final Vector2i windowSize = window.getSize();

		final int x = (int) ((mousePosition.x / (float) windowSize.x) * idsTexture.getWidth());
		final int y = (int) ((1 - (mousePosition.y / (float) windowSize.y)) * idsTexture.getHeight());

		worldFramebuffer.bind(GL_W.GL_READ_FRAMEBUFFER);
		GL_W.glReadBuffer(FrameBufferAttachment.COLOR_FIRST.getGlId() + WORLD_FRAMEBUFFER_IDS_IDX);
		assert GL_W.checkError("ReadBuffer(" + (FrameBufferAttachment.COLOR_FIRST.getGlId() + WORLD_FRAMEBUFFER_IDS_IDX) + ")");
		final IntBuffer pixel = BufferUtils.createIntBuffer(4);
		GL11.glReadPixels(x, y, 1, 1, idsTexture.getFormat().getGlId(), idsTexture.getDataType().getGlId(), pixel);
		assert GL_W.checkError("ReadPixels(" + mousePosition + ", " + idsTexture.getFormat() + ", " + idsTexture.getDataType() + ")");
		worldFramebuffer.unbind(GL_W.GL_READ_FRAMEBUFFER);

		int r = pixel.get(0);
		int g = pixel.get(1);
		int b = pixel.get(2);
		int a = pixel.get(3);

		objectId.set(r, g, b, a);
	}

	public void addOutline(SceneEntity e, Vector4f color) {
		Objects.requireNonNull(e);
		if (e instanceof GameObject go) {
			if (go.getObjectIdLocation() != AttributeLocation.ENTITY) {
				throw new UnsupportedOperationException(
						"GameObject: " + go.getId() + " (" + go.getClass().getName() + ") has no entity object id.");
			}
			outlinedObjects.put(go.getObjectId(), color);
			outlineChanged = true;
		} else {
			throw new UnsupportedOperationException("Unsupported object: " + e + " (" + e.getClass().getName() + ")");
		}
	}

	public void removeOutline(SceneEntity e) {
		Objects.requireNonNull(e);
		if (e instanceof GameObject go) {
			if (go.getObjectIdLocation() != AttributeLocation.ENTITY) {
				throw new UnsupportedOperationException(
						"GameObject: " + go.getId() + " (" + go.getClass().getName() + ") has no entity object id.");
			}
			outlinedObjects.remove(go.getObjectId());
			outlineChanged = true;
		} else {
			throw new UnsupportedOperationException("Unsupported object: " + e + " (" + e.getClass().getName() + ")");
		}
	}

	public Vector4f getOutline(SceneEntity e) {
		if (e == null) {
			return null;
		}
		if (e instanceof GameObject go) {
			if (go.getObjectIdLocation() != AttributeLocation.ENTITY) {
				throw new UnsupportedOperationException(
						"GameObject: " + go.getId() + " (" + go.getClass().getName() + ") has no entity object id.");
			}
			return outlinedObjects.get(go.getObjectId());
		} else {
			throw new UnsupportedOperationException("Unsupported object: " + e + " (" + e.getClass().getName() + ")");
		}
	}

	public boolean hasOutline(SceneEntity e) {
		if (e == null) {
			return false;
		}
		if (e instanceof GameObject go) {
			if (go.getObjectIdLocation() != AttributeLocation.ENTITY) {
				throw new UnsupportedOperationException(
						"GameObject: " + go.getId() + " (" + go.getClass().getName() + ") has no entity object id.");
			}
			return outlinedObjects.containsKey(go.getObjectId());
		} else {
			return false;
		}
	}

	public Vector4f getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Vector4f backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

}
