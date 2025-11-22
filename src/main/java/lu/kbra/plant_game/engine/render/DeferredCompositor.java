package lu.kbra.plant_game.engine.render;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import org.joml.Matrix4f;
import org.joml.Matrix4fc;
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

import lu.kbra.plant_game.engine.entity.go.impl.GameObject;
import lu.kbra.plant_game.engine.entity.impl.AnimatedMeshComponent;
import lu.kbra.plant_game.engine.entity.impl.AnimatedTransformOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.TransformedBoundsOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.TransparentEntity;
import lu.kbra.plant_game.engine.mesh.AnimatedMesh;
import lu.kbra.plant_game.engine.mesh.MaterialMesh;
import lu.kbra.plant_game.engine.mesh.TexturedMesh;
import lu.kbra.plant_game.engine.mesh.TintOwner;
import lu.kbra.plant_game.engine.mesh.data.AttributeLocation;
import lu.kbra.plant_game.engine.render.shader.BlitShader;
import lu.kbra.plant_game.engine.render.shader.DirectShader;
import lu.kbra.plant_game.engine.render.shader.GradientShader;
import lu.kbra.plant_game.engine.render.shader.InstanceDirectShader;
import lu.kbra.plant_game.engine.render.shader.InstanceTransferShader;
import lu.kbra.plant_game.engine.render.shader.LineDirectShader;
import lu.kbra.plant_game.engine.render.shader.LineInstanceDirectShader;
import lu.kbra.plant_game.engine.render.shader.MaterialComputeShader;
import lu.kbra.plant_game.engine.render.shader.OutlineShader;
import lu.kbra.plant_game.engine.render.shader.TextDirectShader;
import lu.kbra.plant_game.engine.render.shader.TextureMaterialComputeShader;
import lu.kbra.plant_game.engine.render.shader.TransferShader;
import lu.kbra.plant_game.engine.scene.ui.UIScene;
import lu.kbra.plant_game.engine.scene.world.WorldLevelScene;
import lu.kbra.standalone.gameengine.GameEngine;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.cache.attrib.UIntAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.Vec2fAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.Vec3fAttribArray;
import lu.kbra.standalone.gameengine.geom.LineMesh;
import lu.kbra.standalone.gameengine.geom.LoadedMesh;
import lu.kbra.standalone.gameengine.geom.LoadedQuadMesh;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.geom.QuadMesh;
import lu.kbra.standalone.gameengine.geom.instance.InstanceEmitter;
import lu.kbra.standalone.gameengine.graph.composition.buffer.Framebuffer;
import lu.kbra.standalone.gameengine.graph.shader.ComputeShader;
import lu.kbra.standalone.gameengine.graph.shader.RenderShader;
import lu.kbra.standalone.gameengine.graph.texture.SingleTexture;
import lu.kbra.standalone.gameengine.graph.window.Window;
import lu.kbra.standalone.gameengine.impl.Cleanupable;
import lu.kbra.standalone.gameengine.impl.GLObject;
import lu.kbra.standalone.gameengine.impl.Renderable;
import lu.kbra.standalone.gameengine.objs.entity.Component;
import lu.kbra.standalone.gameengine.objs.entity.Entity;
import lu.kbra.standalone.gameengine.objs.entity.SceneEntity;
import lu.kbra.standalone.gameengine.objs.entity.components.InstanceEmitterComponent;
import lu.kbra.standalone.gameengine.objs.entity.components.MeshComponent;
import lu.kbra.standalone.gameengine.objs.entity.components.RenderConditionComponent;
import lu.kbra.standalone.gameengine.objs.entity.components.SubEntitiesComponent;
import lu.kbra.standalone.gameengine.objs.entity.components.TextEmitterComponent;
import lu.kbra.standalone.gameengine.objs.entity.components.TransformComponent;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;
import lu.kbra.standalone.gameengine.scene.Scene;
import lu.kbra.standalone.gameengine.scene.camera.Camera;
import lu.kbra.standalone.gameengine.utils.MathUtils;
import lu.kbra.standalone.gameengine.utils.gl.consts.BufferType;
import lu.kbra.standalone.gameengine.utils.gl.consts.DataType;
import lu.kbra.standalone.gameengine.utils.gl.consts.FrameBufferAttachment;
import lu.kbra.standalone.gameengine.utils.gl.consts.PolygonDrawMode;
import lu.kbra.standalone.gameengine.utils.gl.consts.PolygonMode;
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
	private static final String PASS_BOUNDS = "PASS_BOUNDS";

	private static final long POLL_OBJECT_ID_TIMEOUT = 1_000;

	public static final String SCREEN_WIDTH = "screen_width";
	public static final String SCREEN_HEIGHT = "screen_height";

	public static final String DEBUG_TRIANGLES_PROPERTY = DeferredCompositor.class.getSimpleName() + ".debug_triangles";
	public static boolean DEBUG_TRIANGLES = Boolean.getBoolean(DEBUG_TRIANGLES_PROPERTY);
	public static final Vector4f DEBUG_TRIANGLE_COLOR = new Vector4f(1, 0, 0, 1);
	public static final String DEBUG_TRIANGLES_LINE_WIDTH_PROPERTY = DeferredCompositor.class.getSimpleName()
			+ ".debug_triangles.line_width";
	public static float DEBUG_TRIANGLES_LINE_WIDTH = Float.parseFloat(System.getProperty(DEBUG_TRIANGLES_LINE_WIDTH_PROPERTY, "2"));

	public static final String DEBUG_BOUNDS_PROPERTY = DeferredCompositor.class.getSimpleName() + ".debug_bounds";
	public static boolean DEBUG_BOUNDS = Boolean.getBoolean(DEBUG_BOUNDS_PROPERTY);
	public static final Vector4f DEBUG_BOUNDS_COLOR = new Vector4f(0, 1, 1, 1);
	public static final String DEBUG_BOUNDS_LINE_WIDTH_PROPERTY = DeferredCompositor.class.getSimpleName() + ".debug_bounds.line_width";
	public static float DEBUG_BOUNDS_LINE_WIDTH = Float.parseFloat(System.getProperty(DEBUG_BOUNDS_LINE_WIDTH_PROPERTY, "1"));

	private static Mesh SCREEN = new LoadedMesh(
			PASS_SCREEN,
			null,
			new Vec3fAttribArray(
					"pos",
					0,
					1,
					new Vector3f[] { new Vector3f(-1, 1, 0), new Vector3f(1, 1, 0), new Vector3f(1, -1, 0), new Vector3f(-1, -1, 0) }),
			new UIntAttribArray("ind", -1, 1, new int[] { 0, 1, 2, 0, 2, 3 }, BufferType.ELEMENT_ARRAY),
			new Vec2fAttribArray(
					"uv",
					1,
					1,
					new Vector2f[] { new Vector2f(0, 1), new Vector2f(1, 1), new Vector2f(1, 0), new Vector2f(0, 0) }));
	private static QuadMesh QUAD = new LoadedQuadMesh(PASS_BOUNDS, null, new Vector2f(1));

	protected Thread ownerThread;
	protected Window window;

	// world rendering
	protected SingleTexture depthTexture, posTexture, normalTexture, uvTexture, idsTexture;
	protected Framebuffer worldFramebuffer;
	protected TransferShader transferShader;
	protected InstanceTransferShader instanceTransferShader;
	protected Map<Class<? extends Renderable>, RenderShader> worldSceneShaders;

	// ui rendering
	protected SingleTexture fontTexture;
	protected DirectShader directShader;
	protected GradientShader gradientShader;
	protected InstanceDirectShader instanceDirectShader;
	protected TextDirectShader textDirectShader;
	protected Map<Class<? extends Renderable>, RenderShader> uiSceneShaders;

	// material passes
	protected Vector4f backgroundColor = new Vector4f(0, 0, 0, 1);
	protected SingleTexture outputTxt;
	protected MaterialComputeShader materialComputeShader;
	protected TextureMaterialComputeShader textureMaterialComputeShader;
	protected BlitShader blitShader;

	// debug
	protected boolean deferredPass = false;
	protected LineDirectShader lineDirectShader;
	protected LineInstanceDirectShader lineInstanceDirectShader;

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

	public DeferredCompositor(final GameEngine engine, final Thread ownerThread) {
		final CacheManager cache = engine.getCache();
		this.ownerThread = ownerThread;
		this.window = engine.getWindow();

		cache.addAbstractShader(this.transferShader = new TransferShader());
		cache.addAbstractShader(this.instanceTransferShader = new InstanceTransferShader());

		cache.addAbstractShader(this.materialComputeShader = new MaterialComputeShader());
		cache.addAbstractShader(this.textureMaterialComputeShader = new TextureMaterialComputeShader());

		cache.addAbstractShader(this.outlineShader = new OutlineShader());

		cache.addAbstractShader(this.blitShader = new BlitShader());

		cache.addAbstractShader(this.directShader = new DirectShader());
		cache.addAbstractShader(this.gradientShader = new GradientShader());
		cache.addAbstractShader(this.textDirectShader = new TextDirectShader());
		cache.addAbstractShader(this.instanceDirectShader = new InstanceDirectShader());

		cache.addAbstractShader(this.lineDirectShader = new LineDirectShader());
		cache.addAbstractShader(this.lineInstanceDirectShader = new LineInstanceDirectShader());

		this.fontTexture = SingleTexture
				.loadSingleTexture(cache,
						TextDirectShader.FONT_TEXTURE_NAME,
						"classpath:/bakes/fonts/QuinqueFive.ttf/48.png",
						TextureFilter.NEAREST);
		this.fontTexture.setGenerateMipmaps(true);
		this.fontTexture.genMipMaps();
		cache.addTexture(this.fontTexture);

		this.outputTxt = new SingleTexture(WORLD_FRAMEBUFFER_NAME + ".output", engine.getWindow().getSize());
		this.outputTxt.setDataType(DataType.UBYTE);
		this.outputTxt.setFormat(TexelFormat.RGBA);
		this.outputTxt.setInternalFormat(TexelInternalFormat.RGBA8);
		this.outputTxt.setFilters(TextureFilter.NEAREST);
		this.outputTxt.setGenerateMipmaps(false);
		this.outputTxt.setup();
		cache.addTexture(this.outputTxt);

		cache.addFramebuffer(this.worldFramebuffer = this.genFramebuffer(cache, engine.getWindow().getSize()));

		this.worldSceneShaders = PCUtils
				.hashMap(Mesh.class,
						this.transferShader,
						AnimatedMesh.class,
						this.transferShader,
						GradientMesh.class,
						null,
						TextEmitter.class,
						null,
						InstanceEmitter.class,
						this.instanceTransferShader);

		this.uiSceneShaders = PCUtils
				.hashMap(Mesh.class,
						this.directShader,
						AnimatedMesh.class,
						this.directShader,
						GradientMesh.class,
						this.gradientShader,
						TextEmitter.class,
						this.textDirectShader,
						InstanceEmitter.class,
						this.instanceDirectShader);
	}

	public void render(final GameEngine engine, final WorldLevelScene worldScene, final UIScene uiScene) {
		final int width = engine.getWindow().getWidth();
		final int height = engine.getWindow().getHeight();

		final float newFov = worldScene.getCamera().getProjection().getFov();
		final boolean needRegen = !this.oldResolution.equals(width, height) || this.oldFov != newFov;
		this.oldFov = newFov;
		final CacheManager cache = engine.getCache();

		if (needRegen) {
			this.oldResolution.set(width, height);
			final float divisor = (float) MathUtils.map(Math.toDegrees(worldScene.getCamera().getProjection().getFov()), 5, 65, 6, 1);
			// System.err.println("fov: " + worldScene.getCamera().getProjection().getFov()
			// + "rad "
			// + Math.toDegrees(worldScene.getCamera().getProjection().getFov()) + "deg = "
			// + divisor);
			// idk why this uses the option windowSize but ok
			this.renderResolution.set(engine.getWindow().getOptions().windowSize).div(PCUtils.clamp(1, 100, divisor));
			this.renderResolution.set(width, height);
			this.outputResolution.set(width, height);

			this.resizeFramebuffer(this.worldFramebuffer, this.renderResolution);
		}

		this.deferredPass = true;
		this.renderWorldScene(cache, worldScene, this.renderResolution, needRegen);
		this.deferredPass = false;

		this.renderMaterials(cache, worldScene, this.renderResolution, needRegen);

		this.renderOutlines(cache, this.renderResolution, needRegen);

		this.blitToScreen(cache, this.outputResolution, needRegen);

		if (uiScene != null) {
			this.renderUi(cache, uiScene, this.outputResolution, needRegen);
		}

		if (this.awaitingObjectIdPtr.getValue()) {
			this.getObjectId(engine.getWindow());
			this.awaitingObjectIdPtr.setValue(false);
			GlobalLogger.log(Level.FINEST, "Retrieved object id: " + this.objectId + " at " + engine.getWindow().getMousePosition());
		} else {
			this.objectId.zero();
		}
	}

	protected void renderUi(final CacheManager cache, final UIScene uiScene, final Vector2i outputResolution, final boolean needRegen) {
		GL_W.glBindFramebuffer(GL_W.GL_FRAMEBUFFER, 0);
		assert GL_W.checkError("BindFramebuffer(0)");

		GL_W.glViewport(0, 0, outputResolution.x, outputResolution.y);
		assert GL_W.checkError("Viewport(" + outputResolution + ")");

		GL_W.glDisable(GL_W.GL_CULL_FACE);
		assert GL_W.checkError("Disable(CULL_FACE)");

		synchronized (uiScene.getEntitiesLock()) {
			this.renderScene(uiScene, this.uiSceneShaders);
		}
	}

	private void checkComponent(final GLObject mesh, final Component source, final Entity entity) {
		if (mesh == null) {
			throw new NullPointerException("Mesh is null on: " + source.getClass().getSimpleName() + " in " + entity.getId());
		}
		if (!mesh.isValid()) {
			throw new IllegalStateException("Mesh: " + mesh + " in " + entity.getId() + " isn't valid.");
		}
	}

	protected void renderOutlines(final CacheManager cache, final Vector2i resolution, final boolean needRegen) {
		GL_W.glViewport(0, 0, resolution.x, resolution.y);
		assert GL_W.checkError("Viewport(" + resolution + ")");

		final int groupsX = (resolution.x + 15) / 16;
		final int groupsY = (resolution.y + 15) / 16;

		assert groupsX != 0;
		assert groupsY != 0;

		this.outlineShader.bind();

		this.setupOutlineInputs(this.outlineShader, resolution, needRegen);

		GL_W.glDispatchCompute(groupsX, groupsY, 1);
		assert GL_W.checkError("DispatchCompute(" + groupsX + "," + groupsY + ",1)");

		GL_W.glMemoryBarrier(GL_W.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT);
		assert GL_W.checkError("MemoryBarrier(SHADER_IMAGE_ACCESS_BARRIER_BIT)");

		this.outlineShader.unbind();
	}

	private void setupOutlineInputs(final OutlineShader computeShader, final Vector2i resolution, final boolean needRegen) {
		this.posTexture.bind(0);
		this.normalTexture.bind(1);
		this.uvTexture.bind(2);
		this.idsTexture.bind(3);
		this.depthTexture.bind(4);

		if (this.outlineChanged) {
			synchronized (this.outlinedObjects) {
				computeShader.setUniform(OutlineShader.CURRENT_TARGET_COUNT, this.outlinedObjects.size());
				final Vector3i[] ids = new Vector3i[this.outlinedObjects.size()];
				final Vector4f[] colors = new Vector4f[this.outlinedObjects.size()];
				int i = 0;
				for (final Entry<Vector3i, Vector4f> evv : this.outlinedObjects.entrySet()) {
					ids[i] = evv.getKey();
					colors[i] = evv.getValue();
					i++;
				}
				computeShader.setUniformUnsigned(OutlineShader.TARGET_IDS, ids);
				computeShader.setUniform(OutlineShader.TARGET_COLORS, colors);
			}
		}

		if (needRegen) {
			GL_W
					.glBindImageTexture(0,
							this.outputTxt.getGlId(),
							0,
							false,
							0,
							GL_W.GL_READ_WRITE,
							this.outputTxt.getInternalFormat().getGlId());
			assert GL_W.checkError("BindImageTexture(0, " + this.outputTxt.getGlId() + ", READ_WRITE)");

			this.posTexture.bindUniform(computeShader.getUniformLocation("uPosTex"), 0);
			this.normalTexture.bindUniform(computeShader.getUniformLocation("uNormalTex"), 1);
			this.uvTexture.bindUniform(computeShader.getUniformLocation("uUvTex"), 2);
			this.idsTexture.bindUniform(computeShader.getUniformLocation("uIdsTex"), 3);
			this.depthTexture.bindUniform(computeShader.getUniformLocation("uDepthTex"), 4);

			computeShader.setUniform(ComputeShader.INPUT_SIZE, resolution);
			computeShader.setUniform(ComputeShader.OUTPUT_SIZE, resolution);
		}
	}

	protected void blitToScreen(final CacheManager cache, final Vector2i resolution, final boolean needRegen) {
		this.blitShader.bind();
		if (needRegen) {
			this.blitShader.setUniform(ComputeShader.INPUT_SIZE, this.renderResolution);
			this.blitShader.setUniform(ComputeShader.OUTPUT_SIZE, resolution);
		}
		this.outputTxt.bindUniform(this.blitShader.getUniformLocation(BlitShader.TXT0), 0);

		SCREEN.bind();

		GL_W.glDisable(GL_W.GL_CULL_FACE);
		assert GL_W.checkError("Disable(CULL_FACE)");
		GL_W.glDisable(GL_W.GL_DEPTH_TEST);
		assert GL_W.checkError("Disable(DEPTH_TEST)");

		GL_W.glBindFramebuffer(GL_W.GL_FRAMEBUFFER, 0);
		assert GL_W.checkError("BindFramebuffer(0)");
		GL_W.glViewport(0, 0, resolution.x, resolution.y);
		assert GL_W.checkError("Viewport(" + resolution + ")");
		GL_W.glClearColor(this.backgroundColor.x, this.backgroundColor.y, this.backgroundColor.z, this.backgroundColor.w);
		assert GL_W.checkError("ClearColor(" + this.backgroundColor + ")");
		GL_W.glClear(GL_W.GL_COLOR_BUFFER_BIT | GL_W.GL_DEPTH_BUFFER_BIT);
		assert GL_W.checkError("Clear(COLOR | DEPTH)");

		GL_W.glPolygonMode(PolygonMode.FRONT_AND_BACK.getGlId(), PolygonDrawMode.FILL.getGlId());
		assert GL_W.checkError("PolygonMode(FRONT_AND_BACK, FILL)");

		GL_W.glDrawElements(this.blitShader.getBeginMode().getGlId(), SCREEN.getIndicesCount(), GL_W.GL_UNSIGNED_INT, 0);
		assert GL_W.checkError("DrawElements()");

		GL_W.glEnable(GL_W.GL_DEPTH_TEST);
		assert GL_W.checkError("Enable(DEPTH_TEST)");

		SCREEN.unbind();

		this.blitShader.unbind();
	}

	protected void renderMaterials(
			final CacheManager cache,
			final WorldLevelScene worldScene,
			final Vector2i resolution,
			final boolean needRegen) {
		if (needRegen) {
			this.outputTxt.setSize(resolution);
			this.outputTxt.bind();
			this.outputTxt.resize();
			this.outputTxt.unbind();
		} else {
			GL_W
					.glClearTexImage(this.outputTxt.getGlId(),
							0,
							this.outputTxt.getFormat().getGlId(),
							this.outputTxt.getDataType().getGlId(),
							new float[] { 0, 0, 0, 0 });
			assert GL_W.checkError("ClearTexImage(" + this.outputTxt.getGlId() + ")");
		}

		GL_W.glViewport(0, 0, resolution.x, resolution.y);
		assert GL_W.checkError("Viewport(" + resolution + ")");

		final int groupsX = (resolution.x + 15) / 16;
		final int groupsY = (resolution.y + 15) / 16;

		assert groupsX != 0;
		assert groupsY != 0;

		// regular materials

		this.materialComputeShader.bind();

		this.setupMaterialInputs(this.materialComputeShader, worldScene, resolution, needRegen);

		GL_W.glDispatchCompute(groupsX, groupsY, 1);
		assert GL_W.checkError("DispatchCompute(" + groupsX + "," + groupsY + ",1)");

		GL_W.glMemoryBarrier(GL_W.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT);
		assert GL_W.checkError("MemoryBarrier(SHADER_IMAGE_ACCESS_BARRIER_BIT)");

		// txt materials

		this.textureMaterialComputeShader.bind();

		this.setupMaterialInputs(this.textureMaterialComputeShader, worldScene, resolution, needRegen);
		final int txt0UniformLoc = this.textureMaterialComputeShader.getUniformLocation(TextureMaterialComputeShader.TXT0);
		final int currentMaterialIdLoc = this.textureMaterialComputeShader
				.getUniformLocation(TextureMaterialComputeShader.CURRENT_MATERIAL_ID);

		final Set<Integer> alreadyRendered = new HashSet<>();

		synchronized (worldScene.getEntitiesLock()) {

			for (final Entity entity : worldScene.getEntities().values()) {
				if (entity.hasComponentMatching(MeshComponent.class)) {
					final List<MeshComponent> meshComponents = entity.getComponentsMatching(MeshComponent.class);

					for (final MeshComponent meshComponent : meshComponents) {
						final Mesh mesh = meshComponent.getMesh();
						this.checkComponent(mesh, meshComponent, entity);

						if (mesh instanceof final TexturedMesh txtMesh) {
							final SingleTexture txt = txtMesh.getTexture();
							final int tid = txt.getGlId();
							if (alreadyRendered.contains(tid)) {
								continue;
							}

							txt.bindUniform(txt0UniformLoc, 0);

							this.textureMaterialComputeShader.storeUniformUnsigned(currentMaterialIdLoc, tid);

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

					for (final InstanceEmitterComponent instanceEmitterComponent : instanceEmitterComponents) {
						final InstanceEmitter instances = instanceEmitterComponent.getInstanceEmitter();
						this.checkComponent(instances, instanceEmitterComponent, entity);
						final Mesh mesh = instances.getParticleMesh();

						if (mesh instanceof final TexturedMesh txtMesh) {
							final SingleTexture txt = txtMesh.getTexture();
							final int tid = txt.getGlId();
							if (alreadyRendered.contains(tid)) {
								continue;
							}

							txt.bindUniform(txt0UniformLoc, 0);

							this.textureMaterialComputeShader.storeUniformUnsigned(currentMaterialIdLoc, tid);

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

		this.textureMaterialComputeShader.unbind();
	}

	private void setupMaterialInputs(
			final ComputeShader computeShader,
			final WorldLevelScene worldScene,
			final Vector2i resolution,
			final boolean needRegen) {
		this.posTexture.bind(0);
		this.normalTexture.bind(1);
		this.uvTexture.bind(2);
		this.idsTexture.bind(3);
		this.depthTexture.bind(4);

		computeShader.setUniform(MaterialComputeShader.LIGHT_COLOR, worldScene.getLightColor());
		computeShader.setUniform(MaterialComputeShader.LIGHT_DIR, worldScene.getLightDirection());
		computeShader.setUniform(MaterialComputeShader.AMBIENT_LIGHT, worldScene.getAmbientLight());

		if (needRegen) {
			GL_W
					.glBindImageTexture(0,
							this.outputTxt.getGlId(),
							0,
							false,
							0,
							GL_W.GL_WRITE_ONLY,
							this.outputTxt.getInternalFormat().getGlId());
			assert GL_W.checkError("BindImageTexture(0," + this.outputTxt.getGlId() + ",WRITE_ONLY)");

			this.posTexture.bindUniform(computeShader.getUniformLocation("uPosTex"), 0);
			this.normalTexture.bindUniform(computeShader.getUniformLocation("uNormalTex"), 1);
			this.uvTexture.bindUniform(computeShader.getUniformLocation("uUvTex"), 2);
			this.idsTexture.bindUniform(computeShader.getUniformLocation("uIdsTex"), 3);
			this.depthTexture.bindUniform(computeShader.getUniformLocation("uDepthTex"), 4);

			computeShader.setUniform(ComputeShader.INPUT_SIZE, resolution);
			computeShader.setUniform(ComputeShader.OUTPUT_SIZE, resolution);
		}
	}

	protected void renderWorldScene(
			final CacheManager cache,
			final WorldLevelScene worldScene,
			final Vector2i resolution,
			final boolean needRegen) {
		this.worldFramebuffer.bind();

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

		this.transferShader.bind();

		synchronized (worldScene.getEntitiesLock()) {
			this.renderScene(worldScene, this.worldSceneShaders);
		}

		this.transferShader.unbind();
		this.worldFramebuffer.unbind();
	}

	protected void renderScene(final Scene scene, final Map<Class<? extends Renderable>, RenderShader> shaders) {
		final RenderShader meshShader = shaders.get(Mesh.class);
		final RenderShader animatedMeshShader = shaders.get(AnimatedMesh.class);
		final RenderShader textEmitterShader = shaders.get(TextEmitter.class);
		final RenderShader instanceEmitterShader = shaders.get(InstanceEmitter.class);
		final RenderShader gradientMeshShader = shaders.get(GradientMesh.class);

		this.setupUniforms(shaders.values(), scene);
		this.setupUniforms(Arrays.asList(this.lineDirectShader, this.lineInstanceDirectShader), scene);

		for (final Entity entity : scene) {
			this
					.renderEntityRecursive(entity,
							shaders,
							meshShader,
							animatedMeshShader,
							textEmitterShader,
							instanceEmitterShader,
							gradientMeshShader,
							GameEngine.IDENTITY_MATRIX4F);
		}
	}

	private void renderEntityRecursive(
			final Entity entity,
			final Map<Class<? extends Renderable>, RenderShader> shaders,
			final RenderShader meshShader,
			final RenderShader animatedMeshShader,
			final RenderShader textEmitterShader,
			final RenderShader instanceEmitterShader,
			final RenderShader gradientMeshShader,
			final Matrix4fc parentTransform) {

		if (!entity.isActive()) {
			return;
		}

		if (entity.hasComponentMatching(RenderConditionComponent.class)
				&& entity.getComponentsMatching(RenderConditionComponent.class).parallelStream().allMatch(RenderConditionComponent::get)) {
			return;
		}

		final Matrix4fc localTransform = entity instanceof final GameObject go && go.hasTransform() ? go.getTransform().getMatrix()
				: entity.hasComponentMatching(TransformComponent.class)
						? entity.getComponentMatching(TransformComponent.class).getTransform().getMatrix()
				: GameEngine.IDENTITY_MATRIX4F;

		final Matrix4f worldTransform = new Matrix4f(parentTransform).mul(localTransform);

		if (meshShader != null && entity.hasComponentMatching(MeshComponent.class)) {
			for (final MeshComponent meshComponent : entity.getComponentsMatching(MeshComponent.class)) {
				this.renderMesh(meshComponent.getMesh(), meshComponent, entity, worldTransform, meshShader);
			}
		}

		if (gradientMeshShader != null && entity.hasComponentMatching(GradientMeshComponent.class)) {
			for (final GradientMeshComponent gradientMeshComponent : entity.getComponentsMatching(GradientMeshComponent.class)) {
				this.renderMesh(gradientMeshComponent.getGradientMesh(), gradientMeshComponent, entity, worldTransform, gradientMeshShader);
			}
		}

		if (animatedMeshShader != null && entity.hasComponentMatching(AnimatedMeshComponent.class)) {
			final Matrix4f animatedTransform = entity instanceof final AnimatedTransformOwner ago ? ago.getAnimatedTransform()
					: worldTransform;
			for (final AnimatedMeshComponent animatedMeshComponent : entity.getComponentsMatching(AnimatedMeshComponent.class)) {
				this
						.renderMesh(animatedMeshComponent
								.getAnimatedMesh(), animatedMeshComponent, entity, animatedTransform, animatedMeshShader);
			}
		}

		if (textEmitterShader != null && entity.hasComponentMatching(TextEmitterComponent.class)) {
			for (final TextEmitterComponent textEmitterComponent : entity.getComponentsMatching(TextEmitterComponent.class)) {
				this
						.renderTextEmitter(textEmitterComponent
								.getTextEmitter(), textEmitterComponent, entity, worldTransform, textEmitterShader);
			}
		}

		if (instanceEmitterShader != null && entity.hasComponentMatching(InstanceEmitterComponent.class)) {
			for (final InstanceEmitterComponent instanceEmitterComponent : entity.getComponentsMatching(InstanceEmitterComponent.class)) {
				this
						.renderInstanceEmitter(instanceEmitterComponent
								.getInstanceEmitter(), instanceEmitterComponent, entity, worldTransform, instanceEmitterShader);
			}
		}

		if (entity instanceof final TransformedBoundsOwner tbo) {
			this.drawDebugBounds(tbo.getTransformedBounds(parentTransform));
		}

		if (entity.hasComponentMatching(SubEntitiesComponent.class)) {
			final SubEntitiesComponent<?> subEntitiesComponent = entity.getComponentMatching(SubEntitiesComponent.class);
			synchronized (subEntitiesComponent.getEntitiesLock()) {
				for (final Entity child : (List<Entity>) subEntitiesComponent.getEntities()) {
					this
							.renderEntityRecursive(child,
									shaders,
									meshShader,
									animatedMeshShader,
									textEmitterShader,
									instanceEmitterShader,
									gradientMeshShader,
									worldTransform);
				}
			}
		}
	}

	private void setupUniforms(final Collection<RenderShader> collection, final Scene scene) {
		final Camera cam = scene.getCamera();
		cam.getProjection().update(this.outputResolution);
		final Matrix4f viewMatrix = cam.getViewMatrix(), projectionMatrix = cam.getProjection().getProjectionMatrix();

		for (final RenderShader shader : collection) {
			if (shader == null) {
				continue;
			}

			shader.bind();

			shader.setUniform(RenderShader.VIEW_MATRIX, viewMatrix);
			shader.setUniform(RenderShader.PROJECTION_MATRIX, projectionMatrix);
		}

		assert GL_W.checkError("UseProgram(0)");
	}

	private void renderInstanceEmitter(
			final InstanceEmitter obj,
			final InstanceEmitterComponent component,
			final Entity entity,
			final Matrix4f transformationMatrix,
			final RenderShader shader) {
		this.checkComponent(obj, component, entity);
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

		this.drawDebugTrianglesInstanced(mesh, transformationMatrix, obj);

		shader.unbind();

		mesh.unbind();
	}

	private void renderTextEmitter(
			final TextEmitter obj,
			final Component component,
			final Entity entity,
			final Matrix4f transformationMatrix,
			final RenderShader shader) {
		this.checkComponent(obj, component, entity);
		final InstanceEmitter instances = obj.getInstances();
		final Mesh mesh = instances.getParticleMesh();
		shader.bind();
		instances.bind();

		if (shader.createUniform(RenderShader.TRANSFORMATION_MATRIX)) {
			shader.setUniform(RenderShader.TRANSFORMATION_MATRIX, transformationMatrix);
		}

		final boolean transparent = entity instanceof TransparentEntity
				|| (obj.isTransparent() != null ? (boolean) obj.isTransparent() : TextEmitter.DEFAULT_TRANSPARENT);

		this.fontTexture.bindUniform(shader.getUniformLocation(TextDirectShader.TXT0), 1);

		shader
				.setUniform(TextDirectShader.FG_COLOR,
						obj.getForegroundColor() != null ? obj.getForegroundColor() : TextEmitter.DEFAULT_FG_COLOR);
		shader
				.setUniform(TextDirectShader.BG_COLOR,
						obj.getBackgroundColor() != null ? obj.getBackgroundColor() : TextEmitter.DEFAULT_BG_COLOR);
		shader.setUniform(TextDirectShader.TRANSPARENT, transparent);
		shader.setUniformUnsigned(TextDirectShader.TEXT_LENGTH, obj.getCharCount());

		GL_W.glDisable(GL_W.GL_CULL_FACE);
		assert GL_W.checkError("Disable(CULL_FACE)");
		if (transparent) {
			GL_W.glEnable(GL_W.GL_BLEND);
			assert GL_W.checkError("Enable(BLEND)");
		} else {
			GL_W.glDisable(GL_W.GL_BLEND);
			assert GL_W.checkError("Disable(BLEND)");
		}
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
		GL_W.glEnable(GL_W.GL_CULL_FACE);
		assert GL_W.checkError("Enable(CULL_FACE)");

		this.drawDebugTrianglesText(mesh, transformationMatrix, obj);

		instances.unbind();
	}

	private void renderMesh(
			final Mesh mesh,
			final Component component,
			final Entity entity,
			final Matrix4f transformationMatrix,
			final RenderShader shader) {
		this.checkComponent(mesh, component, entity);
		mesh.bind();
		shader.bind();

		if (shader.createUniform(RenderShader.TRANSFORMATION_MATRIX)) {
			shader.setUniform(RenderShader.TRANSFORMATION_MATRIX, transformationMatrix);
		}

		if (entity instanceof TransparentEntity) {
			GL_W.glEnable(GL_W.GL_BLEND);
			assert GL_W.checkError("Enable(BLEND)");
			GL_W.glBlendFunc(GL_W.GL_SRC_ALPHA, GL_W.GL_ONE_MINUS_SRC_ALPHA);
			assert GL_W.checkError("BlendFunc(SRC_ALPHA, ONE_MINUS_SRC_ALPHA)");
		} else {
			GL_W.glDisable(GL_W.GL_BLEND);
			assert GL_W.checkError("Disable(BLEND)");
		}

		if (mesh instanceof final TexturedMesh txtMesh && shader.createUniform(DirectShader.TXT0)) {
			txtMesh.getTexture().bindUniform(shader.getUniformLocation(DirectShader.TXT0), 0);
		}

		final TintOwner tintOwner;
		if (shader.createUniform(DirectShader.TINT)) {
			if (entity instanceof final TintOwner tintEntity) {
				tintOwner = tintEntity;
			} else if (mesh instanceof final TintOwner tintMesh) {
				tintOwner = tintMesh;
			} else {
				tintOwner = null;
			}

			shader
					.setUniform(DirectShader.TINT,
							(tintOwner == null || tintOwner.getTint() == null) ? DirectShader.DEFAULT_TINT : tintOwner.getTint());
		}

		final GradientOwner gradientOwner;
		if (shader.createUniform(GradientShader.GRADIENT_DIRECTION)) {
			if (entity instanceof final GradientOwner gradientEntity) {
				gradientOwner = gradientEntity;
			} else if (mesh instanceof final GradientOwner gradientMesh) {
				gradientOwner = gradientMesh;
			} else {
				gradientOwner = null;
			}

			shader
					.setUniformUnsigned(GradientShader.GRADIENT_DIRECTION,
							((gradientOwner == null || gradientOwner.getDirection() == null) ? GradientShader.DEFAULT_DIRECTION
									: gradientOwner.getDirection()).getId());
			shader
					.setUniform(GradientShader.GRADIENT_RANGE,
							(gradientOwner == null || gradientOwner.getRange() == null) ? GradientShader.DEFAULT_RANGE
									: gradientOwner.getRange());
			shader
					.setUniform(GradientShader.START_COLOR,
							(gradientOwner == null || gradientOwner.getStartColor() == null) ? GradientShader.DEFAULT_START_COLOR
									: gradientOwner.getStartColor());
			shader
					.setUniform(GradientShader.END_COLOR,
							(gradientOwner == null || gradientOwner.getEndColor() == null) ? GradientShader.DEFAULT_END_COLOR
									: gradientOwner.getEndColor());
		}

		if (entity instanceof final GameObject go && go.isEntityMaterialId()) {
			// id is in the entity
			final int matId = go.getMaterialId();

			GL_W.glDisableVertexAttribArray(GameObject.MESH_ATTRIB_MATERIAL_ID_ID);
			assert GL_W.checkError("DisableVertexAttribArray(" + GameObject.MESH_ATTRIB_MATERIAL_ID_ID + ")");
			GL_W.glVertexAttribI1ui(GameObject.MESH_ATTRIB_MATERIAL_ID_ID, matId);
			assert GL_W.checkError("VertexAttrib1f(" + GameObject.MESH_ATTRIB_MATERIAL_ID_ID + "," + matId + ")");
		} else if (mesh instanceof final TexturedMesh txtMesh) { // id in the texture id
			final int txtId = txtMesh.getTexture().getGlId();

			GL_W.glDisableVertexAttribArray(GameObject.MESH_ATTRIB_MATERIAL_ID_ID);
			assert GL_W.checkError("DisableVertexAttribArray(" + GameObject.MESH_ATTRIB_MATERIAL_ID_ID + ")");
			GL_W.glVertexAttribI1ui(GameObject.MESH_ATTRIB_MATERIAL_ID_ID, txtId);
			assert GL_W.checkError("VertexAttrib1f(" + GameObject.MESH_ATTRIB_MATERIAL_ID_ID + "," + txtId + ")");
		} else if (mesh instanceof final MaterialMesh matMesh) { // id is in the material
			final int matId = matMesh.getMaterialId();

			GL_W.glDisableVertexAttribArray(GameObject.MESH_ATTRIB_MATERIAL_ID_ID);
			assert GL_W.checkError("DisableVertexAttribArray(" + GameObject.MESH_ATTRIB_MATERIAL_ID_ID + ")");
			GL_W.glVertexAttribI1ui(GameObject.MESH_ATTRIB_MATERIAL_ID_ID, matId);
			assert GL_W.checkError("VertexAttrib1f(" + GameObject.MESH_ATTRIB_MATERIAL_ID_ID + "," + matId + ")");
		}

		if (entity instanceof final GameObject go && go.getObjectIdLocation() != AttributeLocation.MESH) {
			// object id is in the entity
			final Vector3i objId = go.getObjectId();
			GL_W.glDisableVertexAttribArray(GameObject.MESH_ATTRIB_OBJECT_ID_ID);
			assert GL_W.checkError("DisableVertexAttribArray(" + GameObject.MESH_ATTRIB_OBJECT_ID_ID + ")");
			GL_W.glVertexAttribI3ui(GameObject.MESH_ATTRIB_OBJECT_ID_ID, objId.x, objId.y, objId.z);
			assert GL_W.checkError("VertexAttrib3f(" + GameObject.MESH_ATTRIB_OBJECT_ID_ID + "," + objId + ")");
		}

		GL_W.glPolygonMode(PolygonMode.FRONT_AND_BACK.getGlId(), PolygonDrawMode.FILL.getGlId());
		assert GL_W.checkError("PolygonMode(" + mesh.getPolygonMode() + ", " + mesh.getPolygonDrawMode() + ")");

		if (mesh instanceof final LineMesh lineMesh) {
			if (lineMesh.isLineSmooth()) {
				GL_W.glEnable(GL_W.GL_LINE_SMOOTH);
				assert GL_W.checkError("Enable(LINE_SMOOTH)");
			} else {
				GL_W.glDisable(GL_W.GL_LINE_SMOOTH);
				assert GL_W.checkError("Disable(LINE_SMOOTH)");
			}
			GL_W.glLineWidth(lineMesh.getLineWidth());
			assert GL_W.checkError("Enable(" + lineMesh.getLineWidth() + ")");
		}

		GL_W.glDrawElements(mesh.getBeginMode().getGlId(), mesh.getIndicesCount(), GL_W.GL_UNSIGNED_INT, 0);
		assert GL_W.checkError("DrawElements(" + mesh.getId() + ")");

		this.drawDebugTriangles(mesh, transformationMatrix);

		mesh.unbind();
	}

	private void drawDebugTriangles(final Mesh mesh, final Matrix4f transformationMatrix) {
		if (!this.deferredPass && DEBUG_TRIANGLES && this.lineDirectShader != null) {
			this.lineDirectShader.bind();

			this.lineDirectShader.setUniform(LineDirectShader.TINT, DEBUG_TRIANGLE_COLOR);
			this.lineDirectShader.setUniform(RenderShader.TRANSFORMATION_MATRIX, transformationMatrix);

			GL_W.glEnable(GL_W.GL_LINE_SMOOTH);
			assert GL_W.checkError("Enable(LINE_SMOOTH)");
			GL_W.glLineWidth(DEBUG_TRIANGLES_LINE_WIDTH);
			assert GL_W.checkError("LineWidth(" + DEBUG_TRIANGLES_LINE_WIDTH + ")");
			GL_W.glDisable(GL_W.GL_DEPTH_TEST);
			assert GL_W.checkError("Disable(DEPTH_TEST)");

			GL_W.glPolygonMode(PolygonMode.FRONT_AND_BACK.getGlId(), PolygonDrawMode.LINE.getGlId());
			assert GL_W.checkError("PolygonMode(FRONT_AND_BACK, LINE)");

			GL_W.glDrawElements(this.lineDirectShader.getBeginMode().getGlId(), mesh.getIndicesCount(), GL_W.GL_UNSIGNED_INT, 0);
			assert GL_W.checkError("DrawElements(" + mesh.getId() + ")");

			GL_W.glEnable(GL_W.GL_DEPTH_TEST);
			assert GL_W.checkError("Enable(DEPTH_TEST)");
			GL_W.glPolygonMode(GL_W.GL_FRONT_AND_BACK, GL_W.GL_FILL);
			assert GL_W.checkError("PolygonMode(FRONT_AND_BACK, FILL)");
		}
	}

	private void drawDebugTrianglesInstanced(final Mesh mesh, final Matrix4f transformationMatrix, final InstanceEmitter instances) {
		if (!this.deferredPass && DEBUG_TRIANGLES && this.lineInstanceDirectShader != null) {
			this.lineInstanceDirectShader.bind();

			this.lineInstanceDirectShader.setUniform(LineDirectShader.TINT, DEBUG_TRIANGLE_COLOR);
			this.lineInstanceDirectShader.setUniform(RenderShader.TRANSFORMATION_MATRIX, transformationMatrix);

			GL_W.glEnable(GL_W.GL_LINE_SMOOTH);
			assert GL_W.checkError("Enable(LINE_SMOOTH)");
			GL_W.glLineWidth(DEBUG_TRIANGLES_LINE_WIDTH);
			assert GL_W.checkError("LineWidth(" + DEBUG_TRIANGLES_LINE_WIDTH + ")");
			GL_W.glDisable(GL_W.GL_DEPTH_TEST);
			assert GL_W.checkError("Disable(DEPTH_TEST)");

			GL_W.glPolygonMode(GL_W.GL_FRONT_AND_BACK, GL_W.GL_LINE);
			assert GL_W.checkError("PolygonMode(FRONT_AND_BACK, LINE)");

			GL_W
					.glDrawElementsInstanced(this.lineInstanceDirectShader.getBeginMode().getGlId(),
							mesh.getIndicesCount(),
							GL_W.GL_UNSIGNED_INT,
							0,
							instances.getParticleCount());
			assert GL_W.checkError("DrawElements(" + mesh.getId() + ")");

			GL_W.glEnable(GL_W.GL_DEPTH_TEST);
			assert GL_W.checkError("Enable(DEPTH_TEST)");
			GL_W.glPolygonMode(GL_W.GL_FRONT_AND_BACK, GL_W.GL_FILL);
			assert GL_W.checkError("PolygonMode(FRONT_AND_BACK, FILL)");
		}
	}

	private void drawDebugTrianglesText(final Mesh mesh, final Matrix4f transformationMatrix, final TextEmitter emitter) {
		if (!this.deferredPass && DEBUG_TRIANGLES && this.lineInstanceDirectShader != null) {
			this.lineInstanceDirectShader.bind();

			this.lineInstanceDirectShader.setUniform(LineDirectShader.TINT, DEBUG_TRIANGLE_COLOR);
			this.lineInstanceDirectShader.setUniform(RenderShader.TRANSFORMATION_MATRIX, transformationMatrix);

			GL_W.glEnable(GL_W.GL_LINE_SMOOTH);
			assert GL_W.checkError("Enable(LINE_SMOOTH)");
			GL_W.glLineWidth(DEBUG_TRIANGLES_LINE_WIDTH);
			assert GL_W.checkError("LineWidth(" + DEBUG_TRIANGLES_LINE_WIDTH + ")");
			GL_W.glDisable(GL_W.GL_DEPTH_TEST);
			assert GL_W.checkError("Disable(DEPTH_TEST)");

			GL_W.glPolygonMode(GL_W.GL_FRONT_AND_BACK, GL_W.GL_LINE);
			assert GL_W.checkError("PolygonMode(FRONT_AND_BACK, LINE)");

			GL_W
					.glDrawElementsInstanced(this.lineInstanceDirectShader.getBeginMode().getGlId(),
							mesh.getIndicesCount(),
							GL_W.GL_UNSIGNED_INT,
							0,
							emitter.getStringLength());
			assert GL_W.checkError("DrawElements(" + mesh.getId() + ")");

			GL_W.glEnable(GL_W.GL_DEPTH_TEST);
			assert GL_W.checkError("Enable(DEPTH_TEST)");
			GL_W.glPolygonMode(GL_W.GL_FRONT_AND_BACK, GL_W.GL_FILL);
			assert GL_W.checkError("PolygonMode(FRONT_AND_BACK, FILL)");
		}
	}

	private void drawDebugBounds(final Shape shape) {
		if (!this.deferredPass && DEBUG_BOUNDS && this.lineDirectShader != null) {
			QUAD.bind();
			this.lineDirectShader.bind();

			final Rectangle2D bounds = shape.getBounds2D();

			final float centerX = (float) bounds.getCenterX();
			final float centerY = (float) bounds.getCenterY();
			final float width = (float) bounds.getWidth();
			final float height = (float) bounds.getHeight();

			final Matrix4f transform = new Matrix4f().identity().translate(centerX, 0, centerY).scale(width, 1, height);

			this.lineDirectShader.setUniform(RenderShader.TRANSFORMATION_MATRIX, transform);
			this.lineDirectShader.setUniform(LineDirectShader.TINT, DEBUG_BOUNDS_COLOR);

			GL_W.glEnable(GL_W.GL_LINE_SMOOTH);
			assert GL_W.checkError("Enable(LINE_SMOOTH)");
			GL_W.glLineWidth(DEBUG_BOUNDS_LINE_WIDTH);
			assert GL_W.checkError("LineWidth(" + DEBUG_BOUNDS_LINE_WIDTH + ")");
			GL_W.glDisable(GL_W.GL_DEPTH_TEST);
			assert GL_W.checkError("Disable(DEPTH_TEST)");

			GL_W.glPolygonMode(PolygonMode.FRONT_AND_BACK.getGlId(), PolygonDrawMode.LINE.getGlId());
			assert GL_W.checkError("PolygonMode(FRONT_AND_BACK, LINE)");

			GL_W.glDrawElements(this.lineDirectShader.getBeginMode().getGlId(), QUAD.getIndicesCount(), GL_W.GL_UNSIGNED_INT, 0);
			assert GL_W.checkError("DrawElements(" + QUAD.getId() + ")");

			GL_W.glEnable(GL_W.GL_DEPTH_TEST);
			assert GL_W.checkError("Enable(DEPTH_TEST)");
			GL_W.glPolygonMode(GL_W.GL_FRONT_AND_BACK, GL_W.GL_FILL);
			assert GL_W.checkError("PolygonMode(FRONT_AND_BACK, FILL)");
		}
	}

	protected void resizeFramebuffer(final Framebuffer fb, final Vector2i resolution) {
		fb.bind();
		fb.resize(resolution);
		fb.unbind();
		GlobalLogger.log("Resized framebuffer: " + fb.getId() + " (" + resolution + ")");
	}

	private Framebuffer genFramebuffer(final CacheManager cache, final Vector2i resolution) {
		final Framebuffer framebuffer = new Framebuffer(WORLD_FRAMEBUFFER_NAME);
		framebuffer.gen();
		framebuffer.bind();

		final int width = resolution.x, height = resolution.y;

		this.depthTexture = new SingleTexture(WORLD_FRAMEBUFFER_NAME + ".depth", width, height);
		this.depthTexture.setDataType(DataType.FLOAT);
		this.depthTexture.setFormat(TexelFormat.DEPTH);
		this.depthTexture.setInternalFormat(TexelInternalFormat.DEPTH_COMPONENT24);
		this.depthTexture.setFilters(TextureFilter.NEAREST);
		this.depthTexture.setWraps(TextureWrap.CLAMP_TO_EDGE);
		this.depthTexture.setGenerateMipmaps(false);
		this.depthTexture.setup();
		cache.addTexture(this.depthTexture);
		framebuffer.attachTexture(FrameBufferAttachment.DEPTH, this.depthTexture);

		this.posTexture = new SingleTexture(WORLD_FRAMEBUFFER_NAME + ".pos", width, height);
		this.posTexture.setDataType(DataType.FLOAT);
		this.posTexture.setFormat(TexelFormat.RGB);
		this.posTexture.setInternalFormat(TexelInternalFormat.RGB32F);
		this.posTexture.setFilters(TextureFilter.NEAREST);
		this.posTexture.setGenerateMipmaps(false);
		this.posTexture.setup();
		cache.addTexture(this.posTexture);
		framebuffer.attachTexture(FrameBufferAttachment.COLOR_FIRST, WORLD_FRAMEBUFFER_POS_IDX, this.posTexture);

		this.normalTexture = new SingleTexture(WORLD_FRAMEBUFFER_NAME + ".normal", width, height);
		this.normalTexture.setDataType(DataType.FLOAT);
		this.normalTexture.setFormat(TexelFormat.RGB);
		this.normalTexture.setInternalFormat(TexelInternalFormat.RGB16F);
		this.normalTexture.setFilters(TextureFilter.NEAREST);
		this.normalTexture.setGenerateMipmaps(false);
		this.normalTexture.setup();
		cache.addTexture(this.normalTexture);
		framebuffer.attachTexture(FrameBufferAttachment.COLOR_FIRST, WORLD_FRAMEBUFFER_NORMAL_IDX, this.normalTexture);

		this.uvTexture = new SingleTexture(WORLD_FRAMEBUFFER_NAME + ".uv", width, height);
		this.uvTexture.setDataType(DataType.FLOAT);
		this.uvTexture.setFormat(TexelFormat.RG);
		this.uvTexture.setInternalFormat(TexelInternalFormat.RG16F);
		this.uvTexture.setFilters(TextureFilter.NEAREST);
		this.uvTexture.setGenerateMipmaps(false);
		this.uvTexture.setup();
		cache.addTexture(this.uvTexture);
		framebuffer.attachTexture(FrameBufferAttachment.COLOR_FIRST, WORLD_FRAMEBUFFER_UV_IDX, this.uvTexture);

		this.idsTexture = new SingleTexture(WORLD_FRAMEBUFFER_NAME + ".ids", width, height);
		this.idsTexture.setDataType(DataType.UINT);
		this.idsTexture.setFormat(TexelFormat.RGBA_INTEGER);
		this.idsTexture.setInternalFormat(TexelInternalFormat.RGBA32UI);
		this.idsTexture.setFilters(TextureFilter.NEAREST);
		this.idsTexture.setGenerateMipmaps(false);
		this.idsTexture.setup();
		cache.addTexture(this.idsTexture);
		framebuffer.attachTexture(FrameBufferAttachment.COLOR_FIRST, WORLD_FRAMEBUFFER_IDS_IDX, this.idsTexture);

		framebuffer.setup();
		framebuffer.unbind();

		GlobalLogger.log("Created framebuffer: " + framebuffer.getId() + " (" + resolution + ")");

		return framebuffer;
	}

	@Override
	public void cleanup() {
		GlobalLogger.log("Cleaning up: " + this.getClass().getName());

		if (SCREEN != null) {
			SCREEN.cleanup();
			SCREEN = null;
		}
	}

	public boolean pollObjectId(final boolean blocking) {
		this.awaitingObjectIdPtr.setValue(true);
		if (blocking) {
			return this.awaitObjectId();
		}
		return !this.awaitingObjectIdPtr.getValue();
	}

	public boolean isAwaitingObjectId() {
		return this.awaitingObjectIdPtr.getValue();
	}

	public boolean awaitObjectId() {
		this.awaitingObjectIdPtr.waitForFalse(POLL_OBJECT_ID_TIMEOUT);
		return !this.awaitingObjectIdPtr.getValue();
	}

	public Vector4i getObjectId() {
		return this.objectId;
	}

	protected void getObjectId(final Window window) {
		if (this.idsTexture == null) {
			throw new IllegalStateException("Ids texture is not ready.");
		}
		assert this.ownerThread == Thread.currentThread();

		final Vector2f mousePosition = window.getMousePosition();
		final Vector2i windowSize = window.getSize();

		final int x = (int) ((mousePosition.x / windowSize.x) * this.idsTexture.getWidth());
		final int y = (int) ((1 - (mousePosition.y / windowSize.y)) * this.idsTexture.getHeight());

		this.worldFramebuffer.bind(GL_W.GL_READ_FRAMEBUFFER);
		GL_W.glReadBuffer(FrameBufferAttachment.COLOR_FIRST.getGlId() + WORLD_FRAMEBUFFER_IDS_IDX);
		assert GL_W.checkError("ReadBuffer(" + (FrameBufferAttachment.COLOR_FIRST.getGlId() + WORLD_FRAMEBUFFER_IDS_IDX) + ")");
		final IntBuffer pixel = BufferUtils.createIntBuffer(4);
		GL11.glReadPixels(x, y, 1, 1, this.idsTexture.getFormat().getGlId(), this.idsTexture.getDataType().getGlId(), pixel);
		assert GL_W
				.checkError(
						"ReadPixels(" + mousePosition + ", " + this.idsTexture.getFormat() + ", " + this.idsTexture.getDataType() + ")");
		this.worldFramebuffer.unbind(GL_W.GL_READ_FRAMEBUFFER);

		final int r = pixel.get(0);
		final int g = pixel.get(1);
		final int b = pixel.get(2);
		final int a = pixel.get(3);

		this.objectId.set(r, g, b, a);
	}

	public void addOutline(final SceneEntity e, final Vector4f color) {
		Objects.requireNonNull(e);
		if (!(e instanceof final GameObject go)) {
			throw new UnsupportedOperationException("Unsupported object: " + e + " (" + e.getClass().getName() + ")");
		}
		if (go.getObjectIdLocation() != AttributeLocation.ENTITY) {
			throw new UnsupportedOperationException(
					"GameObject: " + go.getId() + " (" + go.getClass().getName() + ") has no entity object id.");
		}
		this.outlinedObjects.put(go.getObjectId(), color);
		this.outlineChanged = true;
	}

	public void removeOutline(final SceneEntity e) {
		Objects.requireNonNull(e);
		if (!(e instanceof final GameObject go)) {
			throw new UnsupportedOperationException("Unsupported object: " + e + " (" + e.getClass().getName() + ")");
		}
		if (go.getObjectIdLocation() != AttributeLocation.ENTITY) {
			throw new UnsupportedOperationException(
					"GameObject: " + go.getId() + " (" + go.getClass().getName() + ") has no entity object id.");
		}
		this.outlinedObjects.remove(go.getObjectId());
		this.outlineChanged = true;
	}

	public Vector4f getOutline(final SceneEntity e) {
		if (e == null) {
			return null;
		}
		if (!(e instanceof final GameObject go)) {
			throw new UnsupportedOperationException("Unsupported object: " + e + " (" + e.getClass().getName() + ")");
		}
		if (go.getObjectIdLocation() != AttributeLocation.ENTITY) {
			throw new UnsupportedOperationException(
					"GameObject: " + go.getId() + " (" + go.getClass().getName() + ") has no entity object id.");
		}
		return this.outlinedObjects.get(go.getObjectId());
	}

	public boolean hasOutline(final SceneEntity e) {
		if (e == null) {
			return false;
		}
		if (!(e instanceof final GameObject go)) {
			return false;
		}
		if (go.getObjectIdLocation() != AttributeLocation.ENTITY) {
			throw new UnsupportedOperationException(
					"GameObject: " + go.getId() + " (" + go.getClass().getName() + ") has no entity object id.");
		}
		return this.outlinedObjects.containsKey(go.getObjectId());
	}

	public Vector4f getBackgroundColor() {
		return this.backgroundColor;
	}

	public void setBackgroundColor(final Vector4f backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public Window getWindow() {
		return this.window;
	}

}
