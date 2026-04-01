package lu.kbra.plant_game.engine.render;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.logging.Level;

import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector2i;
import org.joml.Vector2ic;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.joml.Vector3ic;
import org.joml.Vector4f;
import org.joml.Vector4fc;
import org.joml.Vector4i;
import org.joml.Vector4ic;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import lu.kbra.pclib.PCUtils;
import lu.kbra.pclib.logger.GlobalLogger;
import lu.kbra.pclib.pointer.prim.BooleanPointer;
import lu.kbra.plant_game.PGLogic;
import lu.kbra.plant_game.engine.entity.go.GameObject;
import lu.kbra.plant_game.engine.entity.go.VariationOwner;
import lu.kbra.plant_game.engine.entity.go.data.AttributeLocation;
import lu.kbra.plant_game.engine.entity.go.data.Footprint;
import lu.kbra.plant_game.engine.entity.go.obj.AnimatedMeshFootprintOwner;
import lu.kbra.plant_game.engine.entity.go.obj.StaticMeshFootprintOwner;
import lu.kbra.plant_game.engine.entity.impl.AnimatedMeshOwner;
import lu.kbra.plant_game.engine.entity.impl.AnimatedTransformOwner;
import lu.kbra.plant_game.engine.entity.impl.FootprintOwner;
import lu.kbra.plant_game.engine.entity.impl.GradientOwner;
import lu.kbra.plant_game.engine.entity.impl.InstanceEmitterOwner;
import lu.kbra.plant_game.engine.entity.impl.MaterialIdOwner;
import lu.kbra.plant_game.engine.entity.impl.MeshOwner;
import lu.kbra.plant_game.engine.entity.impl.ObjectIdOwner;
import lu.kbra.plant_game.engine.entity.impl.ParticleCountOwner;
import lu.kbra.plant_game.engine.entity.impl.RenderConditionOwner;
import lu.kbra.plant_game.engine.entity.impl.SwayOwner;
import lu.kbra.plant_game.engine.entity.impl.TintOwner;
import lu.kbra.plant_game.engine.entity.impl.TransformOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.AbsoluteTransform3DOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.DebugBoundsColor;
import lu.kbra.plant_game.engine.entity.ui.impl.TransformedBoundsOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.TransparentEntity;
import lu.kbra.plant_game.engine.entity.ui.text.TextEmitterOwner;
import lu.kbra.plant_game.engine.mesh.MaterialMesh;
import lu.kbra.plant_game.engine.mesh.TexturedBloomMesh;
import lu.kbra.plant_game.engine.mesh.TexturedMesh;
import lu.kbra.plant_game.engine.render.filter.FilterShaderConfiguration;
import lu.kbra.plant_game.engine.render.shader.compute.MaterialComputeShader;
import lu.kbra.plant_game.engine.render.shader.compute.OutlineShader;
import lu.kbra.plant_game.engine.render.shader.compute.ShadowShader;
import lu.kbra.plant_game.engine.render.shader.compute.TextureMaterialComputeShader;
import lu.kbra.plant_game.engine.render.shader.compute.filter.BlurComputeShader;
import lu.kbra.plant_game.engine.render.shader.compute.filter.FilterShader;
import lu.kbra.plant_game.engine.render.shader.gbuffer.InstanceTransferShader;
import lu.kbra.plant_game.engine.render.shader.gbuffer.TransferShader;
import lu.kbra.plant_game.engine.render.shader.render.BlitShader;
import lu.kbra.plant_game.engine.render.shader.render.DirectShader;
import lu.kbra.plant_game.engine.render.shader.render.GradientShader;
import lu.kbra.plant_game.engine.render.shader.render.InstanceDirectShader;
import lu.kbra.plant_game.engine.render.shader.render.LineDirectShader;
import lu.kbra.plant_game.engine.render.shader.render.LineInstanceDirectShader;
import lu.kbra.plant_game.engine.render.shader.render.TextDirectShader;
import lu.kbra.plant_game.engine.scene.ui.UIScene;
import lu.kbra.plant_game.engine.scene.world.WorldLevelScene;
import lu.kbra.plant_game.engine.scene.world.data.SunLightOwner;
import lu.kbra.plant_game.generated.ColorMaterial;
import lu.kbra.plant_game.plugin.registry.FilterShaderRegistry;
import lu.kbra.standalone.gameengine.GameEngine;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.cache.attrib.UByteAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.UIntAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.Vec2fAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.Vec3fAttribArray;
import lu.kbra.standalone.gameengine.generated.gl_wrapper.GL_W;
import lu.kbra.standalone.gameengine.geom.LineMesh;
import lu.kbra.standalone.gameengine.geom.LoadedMesh;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.geom.QuadLoadedMesh;
import lu.kbra.standalone.gameengine.geom.QuadMesh;
import lu.kbra.standalone.gameengine.geom.instance.InstanceEmitter;
import lu.kbra.standalone.gameengine.graph.composition.buffer.Framebuffer;
import lu.kbra.standalone.gameengine.graph.shader.ComputeShader;
import lu.kbra.standalone.gameengine.graph.shader.RenderShader;
import lu.kbra.standalone.gameengine.graph.texture.SingleTexture;
import lu.kbra.standalone.gameengine.graph.texture.Texture;
import lu.kbra.standalone.gameengine.graph.window.Window;
import lu.kbra.standalone.gameengine.impl.AutoCleanupable;
import lu.kbra.standalone.gameengine.impl.GLObject;
import lu.kbra.standalone.gameengine.objs.entity.SceneEntity;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;
import lu.kbra.standalone.gameengine.scene.EntityContainer;
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
import lu.kbra.standalone.gameengine.utils.gl.consts.TextureType;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextureWrap;

public class DeferredCompositor extends AutoCleanupable {

	private static final int BLUR_RADIUS = 5;
	public static final int BLOOM_TID_OFFSET = 10000;

	protected static enum Method {
		DIRECT,
		DEFERRED,
		SHADOW;
	}

	private static final String SWAY_NOISE_PATH = System.getProperty(DeferredCompositor.class.getSimpleName() + ".path.sway_noise",
			"classpath:/bakes/noise/sway/512.png");
	private static final String VARIATION_NOISE_PATH = System.getProperty(DeferredCompositor.class.getSimpleName() + ".path.sway_noise",
			"classpath:/bakes/noise/white/512.png");
	private static final String FONT_PATH = System.getProperty(DeferredCompositor.class.getSimpleName() + ".path.font",
			"classpath:/bakes/fonts/QuinqueFive.ttf/48.png");

	private static final String WORLD_FRAMEBUFFER_NAME = "_WORLD_FRAMEBUFFER";
	private static final int WORLD_FRAMEBUFFER_POS_IDX = 0;
	private static final int WORLD_FRAMEBUFFER_NORMAL_IDX = 1;
	private static final int WORLD_FRAMEBUFFER_UV_IDX = 2;
	private static final int WORLD_FRAMEBUFFER_IDS_IDX = 3;
	private static final String FILTER_FRAMEBUFFER_NAME = "_FILTER_FRAMEBUFFER";
	private static final String SHADOW_FRAMEBUFFER_NAME = "_SHADOW_FRAMEBUFFER";
	private static final int SHADOW_FRAMEBUFFER_SHADOW_IDX = 0;

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
	public static final String DEBUG_BOUNDS_IGNORE_DEPTH_PROPERTY = DeferredCompositor.class.getSimpleName() + ".debug_bounds.ignore_depth";
	public static boolean DEBUG_BOUNDS_IGNORE_DEPTH = Boolean.getBoolean(DEBUG_BOUNDS_IGNORE_DEPTH_PROPERTY);

	public static final String DEBUG_FOOTPRINTS_PROPERTY = DeferredCompositor.class.getSimpleName() + ".debug_footprints";
	public static boolean DEBUG_FOOTPRINTS = Boolean.getBoolean(DEBUG_FOOTPRINTS_PROPERTY);
	public static final Vector4f DEBUG_FOOTPRINTS_COLOR = new Vector4f(0.8f, 0.2f, 0.8f, 1);
	public static final Vector4f DEBUG_STATIC_FOOTPRINTS_COLOR = new Vector4f(0.0f, 0.2f, 0.8f, 1);
	public static final Vector4f DEBUG_ANIMATED_FOOTPRINTS_COLOR = new Vector4f(0.0f, 0.8f, 0.2f, 1);
	public static final String DEBUG_FOOTPRINTS_LINE_WIDTH_PROPERTY = DeferredCompositor.class.getSimpleName()
			+ ".debug_footprints.line_width";
	public static float DEBUG_FOOTPRINTS_LINE_WIDTH = Float.parseFloat(System.getProperty(DEBUG_FOOTPRINTS_LINE_WIDTH_PROPERTY, "5"));

	public static final String FAIL_ON_INVALID_COMPONENT_PROPERTY = DeferredCompositor.class.getSimpleName() + ".fail_invalid_component";
	public static boolean FAIL_ON_INVALID_COMPONENT = Boolean.getBoolean(FAIL_ON_INVALID_COMPONENT_PROPERTY);
	public static final String SYNC_COMPONENT_CHECK_PROPERTY = DeferredCompositor.class.getSimpleName() + ".sync_component_check";
	public static boolean SYNC_COMPONENT_CHECK = Boolean.getBoolean(SYNC_COMPONENT_CHECK_PROPERTY);

	public static final String GL_LINE_SMOOTHING_PROPERTY = DeferredCompositor.class.getSimpleName() + ".gl_line_smoothing";
	public static final boolean GL_LINE_SMOOTHING = Boolean.getBoolean(GL_LINE_SMOOTHING_PROPERTY);

	public static final String GL_LINE_WIDTH_CHECK_PROPERTY = DeferredCompositor.class.getSimpleName() + ".gl_line_width_check";
	public static final boolean GL_LINE_WIDTH_CHECK = Boolean.getBoolean(GL_LINE_WIDTH_CHECK_PROPERTY);

	public static final String GL_FORCE_SYNC_COMPUTE_SHADERS_PROPERTY = DeferredCompositor.class.getSimpleName()
			+ ".gl_force_sync_compute_shaders";
	public static final boolean GL_FORCE_SYNC_COMPUTE_SHADERS = Boolean.getBoolean(GL_FORCE_SYNC_COMPUTE_SHADERS_PROPERTY);

	private Mesh SCREEN;
	private QuadMesh QUAD;

	protected Thread ownerThread;
	protected CacheManager cache;
	protected Window window;

	// world rendering
	protected SingleTexture shadowMap;
	protected Framebuffer shadowFramebuffer;
	protected SingleTexture depthTexture;
	protected SingleTexture posTexture;
	protected SingleTexture normalTexture;
	protected SingleTexture uvTexture;
	/**
	 * (mat, obj, obj, obj)
	 */
	protected SingleTexture idsTexture;
	protected Framebuffer worldFramebuffer;
	protected TransferShader deferredTransferShader;
	protected InstanceTransferShader deferredInstanceTransferShader;
	protected Texture swayMap;
	protected Texture variationMap;

	// ui rendering
	protected SingleTexture fontTexture;
	protected DirectShader directShader;
	protected GradientShader directGradientShader;
	protected InstanceDirectShader directInstanceShader;
	protected TextDirectShader directTextShader;

	// material passes
	protected Vector4f backgroundColor = new Vector4f(0, 0, 0, 0);
	protected SingleTexture outputTxt;
	protected SingleTexture outputBloomTxt;
	protected SingleTexture blurBloomTxt;
	protected float[] blurWeights;
	protected MaterialComputeShader materialComputeShader;
	protected TextureMaterialComputeShader textureMaterialComputeShader;
	protected OutlineShader outlineShader;
	protected BlurComputeShader blurComputeShader;
	protected BlitShader blitShader;

	// filters
	protected Map<Class<? extends FilterShader<?>>, FilterShader<?>> filterShaders = new HashMap<>();
	protected List<FilterShaderConfiguration<?>> enabledFilters = new ArrayList<>();

	// debug
	protected boolean deferredPass = false;
	protected LineDirectShader lineDirectShader;
	protected LineInstanceDirectShader lineInstanceDirectShader;

	// rendering/states
	protected float oldFov;
	protected Vector2i oldResolution = new Vector2i(0);
	protected Vector2i renderResolution = new Vector2i();
	protected Vector2i outputResolution = new Vector2i();

	// object ids
	protected BooleanPointer isAwaitingObjectId = new BooleanPointer(false);
	protected Vector4i objectId = new Vector4i();

	// highlights
	protected volatile boolean outlineChanged = false;
	protected Map<Vector3ic, Vector4fc> outlinedObjects = new ConcurrentHashMap<>();

	@SuppressWarnings("unchecked")
	public DeferredCompositor(final GameEngine engine, final Thread ownerThread) {
		this.cache = engine.getCache();
		this.ownerThread = ownerThread;
		this.window = engine.getWindow();

		this.cache.addMesh(this.SCREEN = new LoadedMesh(PASS_SCREEN,
				null,
				new Vec3fAttribArray("pos",
						0,
						new Vector3f[] { new Vector3f(-1, 1, 0), new Vector3f(1, 1, 0), new Vector3f(1, -1, 0), new Vector3f(-1, -1, 0) }),
				new UIntAttribArray("ind", -1, new int[] { 0, 1, 2, 0, 2, 3 }, BufferType.ELEMENT_ARRAY),
				new Vec2fAttribArray("uv",
						1,
						new Vector2f[] { new Vector2f(0, 1), new Vector2f(1, 1), new Vector2f(1, 0), new Vector2f(0, 0) })));
		this.cache.addMesh(this.QUAD = new QuadLoadedMesh(PASS_BOUNDS,
				null,
				new Vector2f(1),
				new UByteAttribArray(GameObject.MESH_ATTRIB_MATERIAL_ID_NAME, GameObject.MESH_ATTRIB_MATERIAL_ID_ID, new byte[4])));

		this.cache.addAbstractShader(this.deferredTransferShader = new TransferShader());
		this.cache.addAbstractShader(this.deferredInstanceTransferShader = new InstanceTransferShader());

		this.cache.addAbstractShader(this.materialComputeShader = new MaterialComputeShader());
		this.cache.addAbstractShader(this.textureMaterialComputeShader = new TextureMaterialComputeShader());

		this.cache.addAbstractShader(this.outlineShader = new OutlineShader());
		this.cache.addAbstractShader(this.blurComputeShader = new BlurComputeShader());

		this.cache.addAbstractShader(this.blitShader = new BlitShader());

		this.cache.addAbstractShader(this.directShader = new DirectShader());
		this.cache.addAbstractShader(this.directGradientShader = new GradientShader());
		this.cache.addAbstractShader(this.directTextShader = new TextDirectShader());
		this.cache.addAbstractShader(this.directInstanceShader = new InstanceDirectShader());

		this.cache.addAbstractShader(this.lineDirectShader = new LineDirectShader());
		this.cache.addAbstractShader(this.lineInstanceDirectShader = new LineInstanceDirectShader());

		FilterShaderRegistry.FILTER_SHADERS.stream().map(Supplier::get).forEach(fs -> {
			this.cache.addAbstractShader(fs);
			this.filterShaders.put((Class<? extends FilterShader<?>>) fs.getClass(), fs);
		});

		this.fontTexture = SingleTexture
				.loadSingleTexture(this.cache, TextDirectShader.FONT_TEXTURE_NAME, FONT_PATH, TextureFilter.NEAREST);
		this.fontTexture.setGenerateMipmaps(true);
		this.fontTexture.genMipMaps();
		this.cache.addTexture(this.fontTexture);

		this.swayMap = SingleTexture.loadSingleTexture(this.cache,
				TransferShader.SWAY_MAP_TEXTURE_NAME,
				SWAY_NOISE_PATH,
				TextureFilter.LINEAR,
				TextureType.TXT2D,
				TextureWrap.REPEAT);
		this.swayMap.setGenerateMipmaps(true);
		this.swayMap.genMipMaps();
		this.cache.addTexture(this.swayMap);

		this.variationMap = SingleTexture.loadSingleTexture(this.cache,
				MaterialComputeShader.VARIATION_MAP_TEXTURE_NAME,
				VARIATION_NOISE_PATH,
				TextureFilter.LINEAR,
				TextureType.TXT2D,
				TextureWrap.REPEAT);
		this.variationMap.setGenerateMipmaps(true);
		this.variationMap.genMipMaps();
		this.cache.addTexture(this.variationMap);

		this.outputTxt = new SingleTexture(WORLD_FRAMEBUFFER_NAME + ".output", engine.getWindow().getSize());
		this.outputTxt.setColorFormat(DataType.FLOAT, TexelFormat.RGBA, TexelInternalFormat.RGBA16F);
		this.outputTxt.setFilters(TextureFilter.NEAREST);
		this.outputTxt.setGenerateMipmaps(false);
		this.outputTxt.setup();
		this.cache.addTexture(this.outputTxt);

		this.outputBloomTxt = new SingleTexture(WORLD_FRAMEBUFFER_NAME + ".outputBloom", engine.getWindow().getSize());
		this.outputBloomTxt.setColorFormat(DataType.FLOAT, TexelFormat.RGBA, TexelInternalFormat.RGBA16F);
		this.outputBloomTxt.setFilters(TextureFilter.LINEAR);
		this.outputBloomTxt.setGenerateMipmaps(false);
		this.outputBloomTxt.setup();
		this.cache.addTexture(this.outputBloomTxt);

		this.blurBloomTxt = new SingleTexture(WORLD_FRAMEBUFFER_NAME + ".blurBloom", engine.getWindow().getSize());
		this.blurBloomTxt.setColorFormat(DataType.FLOAT, TexelFormat.RGBA, TexelInternalFormat.RGBA16F);
		this.blurBloomTxt.setFilters(TextureFilter.LINEAR);
		this.blurBloomTxt.setGenerateMipmaps(false);
		this.blurBloomTxt.setup();
		this.cache.addTexture(this.blurBloomTxt);

		this.cache.addFramebuffer(this.shadowFramebuffer = this.genShadowFramebuffer());
		this.cache.addFramebuffer(this.worldFramebuffer = this.genWorldFramebuffer(engine.getWindow().getSize()));
	}

	public void render(final GameEngine engine, final WorldLevelScene worldScene, final UIScene uiScene) {
		final CacheManager cache = engine.getCache();

		final int width = engine.getWindow().getWidth();
		final int height = engine.getWindow().getHeight();

		final boolean needRegen;
		if (worldScene != null) {
			final float newFov = worldScene.getCamera().getProjection().getFov();
			needRegen = !this.oldResolution.equals(width, height) || this.oldFov != newFov;
			this.oldFov = newFov;

			if (needRegen) {
				this.oldResolution.set(width, height);
				final float divisor = (float) MathUtils.map(Math.toDegrees(worldScene.getCamera().getProjection().getFov()), 5, 65, 6, 1);
				// idk why this uses the option windowSize but ok
				this.renderResolution.set(engine.getWindow().getOptions().windowSize).div(PCUtils.clamp(1, 100, divisor));
				this.renderResolution.set(width, height).div(2);
				this.outputResolution.set(width, height);

				this.resizeFramebuffer(this.worldFramebuffer, this.renderResolution);
			}

			GL_W.glEnable(GL_W.GL_MULTISAMPLE);

			this.deferredPass = true;
			this.renderShadows(worldScene);

			this.renderWorldScene(worldScene, this.renderResolution, needRegen);
			this.deferredPass = false;

			this.renderMaterials(worldScene, this.renderResolution, needRegen);

			this.renderOutlines(this.renderResolution, needRegen);

			this.renderBloom(this.renderResolution, needRegen);
		} else {
			needRegen = false;
		}

		this.renderFilters(cache, this.outputResolution, needRegen);

		this.blitToScreen(this.outputResolution, needRegen);

		if (uiScene != null) {
			this.renderUi(uiScene, this.outputResolution, needRegen);
		}

		if (this.isAwaitingObjectId.getValue()) {
			this.getObjectId(engine.getWindow());
			this.isAwaitingObjectId.setValue(false);
			GlobalLogger.log(Level.FINEST, "Retrieved object id: " + this.objectId + " at " + engine.getWindow().getMousePosition());
		} else {
			this.objectId.zero();
		}
	}

	private void renderShadows(final WorldLevelScene worldScene) {
		this.shadowFramebuffer.bind();

		final Vector2i resolution = this.shadowMap.getSize2D();
		GL_W.glViewport(0, 0, resolution.x, resolution.y);

		GL_W.glEnable(GL_W.GL_DEPTH_TEST);
		GL_W.glEnable(GL_W.GL_CULL_FACE);
		GL_W.glCullFace(GL_W.GL_BACK);

		GL_W.glClear(GL_W.GL_DEPTH_BUFFER_BIT);

		synchronized (worldScene.getEntitiesLock()) {
			this.renderScene(worldScene, Method.SHADOW);
		}

		this.shadowFramebuffer.unbind();
	}

	private void renderBloom(final Vector2i resolution, final boolean needRegen) {
		this.blurComputeShader.bind();

		this.blurComputeShader.setUniform(BlurComputeShader.RADIUS, BLUR_RADIUS);
		if (this.blurWeights == null || this.blurWeights.length != BLUR_RADIUS + 1) {
			this.blurWeights = PCUtils.computeGaussianWeights(BLUR_RADIUS);
			this.blurComputeShader.setUniform(BlurComputeShader.WEIGHTS, this.blurWeights);
		}

		if (needRegen) {
			this.blurComputeShader.setUniform(BlurComputeShader.INPUT_SIZE, resolution);
			this.blurComputeShader.setUniform(BlurComputeShader.OUTPUT_SIZE, resolution);
			this.blurBloomTxt.resize(resolution);
		}
		this.blurBloomTxt.clear(this.backgroundColor);

		final Vector3ic groups = this.blurComputeShader.getGlobalGroup(new Vector3i(resolution.x, resolution.y, 1));

		{
			this.blurComputeShader.setUniform(BlurComputeShader.HORIZONTAL, false);
			this.outputBloomTxt.bindUniform(this.blurComputeShader, BlurComputeShader.TXT_BLOOM, 0);
			GL_W.glBindImageTexture(0,
					this.blurBloomTxt.getGlId(),
					0,
					false,
					0,
					GL_W.GL_WRITE_ONLY,
					this.blurBloomTxt.getTexelInternalFormat().getGlId());
			GL_W.glDispatchCompute(groups.x(), groups.y(), 1);
			GL_W.glMemoryBarrier(GL_W.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT | GL_W.GL_TEXTURE_FETCH_BARRIER_BIT);
		}

		{
			this.blurComputeShader.setUniform(BlurComputeShader.HORIZONTAL, true);
			this.blurBloomTxt.bindUniform(this.blurComputeShader, BlurComputeShader.TXT_BLOOM, 0);
			GL_W.glBindImageTexture(0,
					this.outputBloomTxt.getGlId(),
					0,
					false,
					0,
					GL_W.GL_WRITE_ONLY,
					this.outputBloomTxt.getTexelInternalFormat().getGlId());
			GL_W.glDispatchCompute(groups.x(), groups.y(), 1);
			GL_W.glMemoryBarrier(GL_W.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT | GL_W.GL_TEXTURE_FETCH_BARRIER_BIT);
		}

		this.blurComputeShader.unbind();

	}

	protected void renderFilters(final CacheManager cache, final Vector2i outputResolution, final boolean needRegen) {
		if (this.enabledFilters.isEmpty()) {
			return;
		}

		this.SCREEN.bind();

		GL_W.glDisable(GL_W.GL_CULL_FACE);
		GL_W.glDisable(GL_W.GL_DEPTH_TEST);
		GL_W.glEnable(GL_W.GL_BLEND);
		GL_W.glBlendFunc(GL_W.GL_SRC_ALPHA, GL_W.GL_ONE_MINUS_SRC_ALPHA);
		GL_W.glPolygonMode(PolygonMode.FRONT_AND_BACK.getGlId(), PolygonDrawMode.FILL.getGlId());

		GL_W.glBindFramebuffer(GL_W.GL_FRAMEBUFFER, 0);
		GL_W.glViewport(0, 0, outputResolution.x, outputResolution.y);

		for (FilterShaderConfiguration fsc : this.enabledFilters) {
			if (!this.filterShaders.containsKey(fsc.getShaderClass())) {
				GlobalLogger.warning("FilterShader not found for class: " + fsc.getShaderClass());
				continue;
			}
			final FilterShader<?> fs = this.filterShaders.get(fsc.getShaderClass());

			fs.bind();

			fs.setUniform(FilterShader.INPUT_SIZE, this.renderResolution);
			fs.setUniform(FilterShader.OUTPUT_SIZE, outputResolution);
			this.outputTxt.bindUniform(fs, FilterShader.TXT0, 0);

			fsc.apply((FilterShader) fsc.getShaderClass().cast(fs));

			GL_W.glDrawElements(fs.getBeginMode().getGlId(), this.SCREEN.getIndicesCount(), GL_W.GL_UNSIGNED_INT, 0);
		}

		GL_W.glEnable(GL_W.GL_DEPTH_TEST);
		GL_W.glEnable(GL_W.GL_CULL_FACE);
		GL_W.glDisable(GL_W.GL_BLEND);

		this.SCREEN.unbind();

		GL_W.glUseProgram(0);
	}

	protected void renderUi(final UIScene uiScene, final Vector2i outputResolution, final boolean needRegen) {
		GL_W.glBindFramebuffer(GL_W.GL_FRAMEBUFFER, 0);

		GL_W.glViewport(0, 0, outputResolution.x, outputResolution.y);

		GL_W.glDisable(GL_W.GL_CULL_FACE);

		synchronized (uiScene.getEntitiesLock()) {
			this.renderScene(uiScene, Method.DIRECT);
		}
	}

	/**
	 * @return true if the rendering should be skipped.
	 */
	private boolean checkComponent(final GLObject mesh, final SceneEntity entity) {
		if (mesh == null) {
			if (FAIL_ON_INVALID_COMPONENT) {
				throw new NullPointerException("Mesh is null in " + entity.getId());
			}
			GlobalLogger.info("Mesh is null in: " + entity.getId());
			return true;
		}
		if (SYNC_COMPONENT_CHECK) {
			synchronized (mesh) {
				if (mesh == null || !mesh.isValid()) {
					if (FAIL_ON_INVALID_COMPONENT) {
						throw new IllegalStateException("Mesh: " + Objects.toString(mesh) + " in " + entity.getId() + " isn't valid.");
					}
					GlobalLogger.info("Mesh " + Objects.toString(mesh) + " in " + entity.getId() + " isn't valid.");
					return true;
				}
			}
		} else {
			if (mesh == null || !mesh.isValid()) {
				if (FAIL_ON_INVALID_COMPONENT) {
					throw new IllegalStateException("Mesh: " + Objects.toString(mesh) + " in " + entity.getId() + " isn't valid.");
				}
				GlobalLogger.info("Mesh " + Objects.toString(mesh) + " in " + entity.getId() + " isn't valid.");
				return true;
			}
		}
		return false;
	}

	protected void renderOutlines(final Vector2i resolution, final boolean needRegen) {
		GL_W.glViewport(0, 0, resolution.x, resolution.y);

		final Vector3ic groups = this.outlineShader.getGlobalGroup(new Vector3i(resolution.x, resolution.y, 1));

		this.outlineShader.bind();

		this.setupOutlineInputs(this.outlineShader, resolution, needRegen);

		GL_W.glDispatchCompute(groups.x(), groups.y(), 1);

		GL_W.glMemoryBarrier(GL_W.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT);

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
				final Vector3ic[] ids = new Vector3ic[this.outlinedObjects.size()];
				final Vector4fc[] colors = new Vector4fc[this.outlinedObjects.size()];
				int i = 0;
				for (final Entry<Vector3ic, Vector4fc> evv : this.outlinedObjects.entrySet()) {
					ids[i] = evv.getKey();
					colors[i] = evv.getValue();
					i++;
				}
				computeShader.setUniformUnsigned(OutlineShader.TARGET_IDS, ids);
				computeShader.setUniform(OutlineShader.TARGET_COLORS, colors);
			}
		}

		if (needRegen) {
			GL_W.glBindImageTexture(0,
					this.outputTxt.getGlId(),
					0,
					false,
					0,
					GL_W.GL_READ_WRITE,
					this.outputTxt.getTexelInternalFormat().getGlId());

			this.posTexture.bindUniform(computeShader.getUniformLocation("uPosTex"), 0);
			this.normalTexture.bindUniform(computeShader.getUniformLocation("uNormalTex"), 1);
			this.uvTexture.bindUniform(computeShader.getUniformLocation("uUvTex"), 2);
			this.idsTexture.bindUniform(computeShader.getUniformLocation("uIdsTex"), 3);
			this.depthTexture.bindUniform(computeShader.getUniformLocation("uDepthTex"), 4);

			computeShader.setUniform(ComputeShader.INPUT_SIZE, resolution);
			computeShader.setUniform(ComputeShader.OUTPUT_SIZE, resolution);
		}
	}

	protected void blitToScreen(final Vector2i outputResolution, final boolean needRegen) {
		this.blitShader.bind();
		if (needRegen) {
			this.blitShader.setUniform(ComputeShader.INPUT_SIZE, this.renderResolution);
			this.blitShader.setUniform(ComputeShader.OUTPUT_SIZE, outputResolution);
		}

		this.SCREEN.bind();

		GL_W.glDisable(GL_W.GL_CULL_FACE);
		GL_W.glDisable(GL_W.GL_DEPTH_TEST);

		GL_W.glBindFramebuffer(GL_W.GL_FRAMEBUFFER, 0);
		GL_W.glViewport(0, 0, outputResolution.x, outputResolution.y);
		GL_W.glClearColor(this.backgroundColor.x, this.backgroundColor.y, this.backgroundColor.z, this.backgroundColor.w);
		GL_W.glClear(GL_W.GL_COLOR_BUFFER_BIT | GL_W.GL_DEPTH_BUFFER_BIT);

		GL_W.glPolygonMode(PolygonMode.FRONT_AND_BACK.getGlId(), PolygonDrawMode.FILL.getGlId());

		{
			this.outputTxt.bindUniform(this.blitShader, BlitShader.TXT0, 0);
			GL_W.glDrawElements(this.blitShader.getBeginMode().getGlId(), this.SCREEN.getIndicesCount(), GL_W.GL_UNSIGNED_INT, 0);
		}

		{
			GL_W.glEnable(GL_W.GL_BLEND);
			GL_W.glBlendFunc(GL_W.GL_SRC_ALPHA, GL_W.GL_ONE_MINUS_SRC_ALPHA);
			this.outputBloomTxt.bindUniform(this.blitShader, BlitShader.TXT0, 0);
			GL_W.glDrawElements(this.blitShader.getBeginMode().getGlId(), this.SCREEN.getIndicesCount(), GL_W.GL_UNSIGNED_INT, 0);
		}

		GL_W.glDisable(GL_W.GL_BLEND);
		GL_W.glEnable(GL_W.GL_CULL_FACE);
		GL_W.glEnable(GL_W.GL_DEPTH_TEST);

		this.SCREEN.unbind();

		this.blitShader.unbind();
	}

	protected void renderMaterials(final WorldLevelScene worldScene, final Vector2i resolution, final boolean needRegen) {
		if (needRegen) {
			this.outputTxt.resize(resolution);
			this.outputBloomTxt.resize(resolution);
		}
		this.outputTxt.clear(this.backgroundColor);
		this.outputBloomTxt.clear(this.backgroundColor);

		final Vector3ic groups = this.materialComputeShader.getGlobalGroup(new Vector3i(resolution.x, resolution.y, 1));

		// regular materials

		this.materialComputeShader.bind();

		this.setupMaterialInputs(this.materialComputeShader, worldScene, resolution, needRegen);
		this.materialComputeShader.setUniform(MaterialComputeShader.COLOR_VARIATION, false);
		this.materialComputeShader.setUniform(MaterialComputeShader.SINGLE_OBJECT, false);

		GL_W.glBindImageTexture(0,
				this.outputTxt.getGlId(),
				0,
				false,
				0,
				GL_W.GL_WRITE_ONLY,
				this.outputTxt.getTexelInternalFormat().getGlId());
		GL_W.glDispatchCompute(groups.x(), groups.y(), 1);

		final int variationMapUniformLoc = this.materialComputeShader.getUniformLocation(MaterialComputeShader.VARIATION_MAP);

		// we need to wait because the textures might overwrite some of these pixels, or not
		GL_W.glMemoryBarrier(GL_W.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT);

		// txt materials

		this.textureMaterialComputeShader.bind();

		this.setupMaterialInputs(this.textureMaterialComputeShader, worldScene, resolution, needRegen);
		final int txtDiffuseUniformLoc = this.textureMaterialComputeShader.getUniformLocation(TextureMaterialComputeShader.TXT_DIFFUSE);
		final int txtBloomUniformLoc = this.textureMaterialComputeShader.getUniformLocation(TextureMaterialComputeShader.TXT_BLOOM);
		final int currentMaterialIdLoc = this.textureMaterialComputeShader
				.getUniformLocation(TextureMaterialComputeShader.CURRENT_MATERIAL_ID);

		GL_W.glBindImageTexture(0,
				this.outputTxt.getGlId(),
				0,
				false,
				0,
				GL_W.GL_WRITE_ONLY,
				this.outputTxt.getTexelInternalFormat().getGlId());
		GL_W.glBindImageTexture(1,
				this.outputBloomTxt.getGlId(),
				0,
				false,
				0,
				GL_W.GL_WRITE_ONLY,
				this.outputTxt.getTexelInternalFormat().getGlId());

		final Set<Integer> alreadyRendered = new HashSet<>();

		synchronized (worldScene.getEntitiesLock()) {

			for (final SceneEntity entity : worldScene.getEntities().values()) {
				this.renderTexture(alreadyRendered,
						txtDiffuseUniformLoc,
						txtBloomUniformLoc,
						variationMapUniformLoc,
						currentMaterialIdLoc,
						groups.x(),
						groups.y(),
						entity);
			}

		}

		if (!GL_FORCE_SYNC_COMPUTE_SHADERS) {
			GL_W.glMemoryBarrier(GL_W.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT);
		}

		this.textureMaterialComputeShader.unbind();
	}

	private void renderTexture(
			final Set<Integer> alreadyRendered,
			final int txtDiffuseUniformLoc,
			final int txtBloomUniformLoc,
			final int variationMapUniformLoc,
			final int currentMaterialIdLoc,
			final int groupsX,
			final int groupsY,
			final SceneEntity entity) {

		if (entity instanceof final MeshOwner mo) {
			final Mesh mesh = mo.getMesh();
			if (this.checkComponent(mesh, entity)) {
				return;
			}

			this.renderTextureMesh(mesh,
					alreadyRendered,
					txtDiffuseUniformLoc,
					txtBloomUniformLoc,
					variationMapUniformLoc,
					currentMaterialIdLoc,
					groupsX,
					groupsY,
					entity);
		}

		if (entity instanceof final EntityContainer<?> ec) {
			ec.forEach(e -> this.renderTexture(alreadyRendered,
					txtDiffuseUniformLoc,
					txtBloomUniformLoc,
					variationMapUniformLoc,
					currentMaterialIdLoc,
					groupsX,
					groupsY,
					e));
		}

		if (entity instanceof final InstanceEmitterOwner ieo) {
			final InstanceEmitter instances = ieo.getInstanceEmitter();
			if (this.checkComponent(instances, entity)) {
				return;
			}
			final Mesh mesh = instances.getParticleMesh();

			this.renderTextureMesh(mesh,
					alreadyRendered,
					txtDiffuseUniformLoc,
					txtBloomUniformLoc,
					variationMapUniformLoc,
					currentMaterialIdLoc,
					groupsX,
					groupsY,
					entity);
		}
	}

	private void renderTextureMesh(
			final Mesh mesh,
			final Set<Integer> alreadyRendered,
			final int txtDiffuseUniformLoc,
			final int txtBloomUniformLoc,
			final int variationMapUniformLoc,
			final int currentMaterialIdLoc,
			final int groupsX,
			final int groupsY,
			final SceneEntity entity) {

		if (mesh instanceof final TexturedBloomMesh txtBloomMesh) {
			final SingleTexture txtBloom = txtBloomMesh.getBloomTexture();
			final SingleTexture txtDiffuse = txtBloomMesh.getDiffuseTexture();
			final int tid = ColorMaterial.values().length + txtDiffuse.getGlId();

			if (!alreadyRendered.contains(BLOOM_TID_OFFSET + tid)) {
				this.textureMaterialComputeShader.bind();
				txtDiffuse.bindUniform(txtDiffuseUniformLoc, 6);
				txtBloom.bindUniform(txtBloomUniformLoc, 7);
				this.textureMaterialComputeShader.setUniform(TextureMaterialComputeShader.BLOOM_STRENGTH, txtBloomMesh.getBloomStrength());
				this.textureMaterialComputeShader.storeUniformUnsigned(currentMaterialIdLoc, tid);
				this.textureMaterialComputeShader.setUniform(TextureMaterialComputeShader.APPLY_BLOOM, true);

				GL_W.glDispatchCompute(groupsX, groupsY, 1);

				alreadyRendered.add(BLOOM_TID_OFFSET + tid);

				if (GL_FORCE_SYNC_COMPUTE_SHADERS) {
					GL_W.glMemoryBarrier(GL_W.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT);
				}
			}
		} else if (mesh instanceof final TexturedMesh txtMesh) {
			final SingleTexture txt = txtMesh.getTexture();
			final int tid = ColorMaterial.values().length + txt.getGlId();

			if (!alreadyRendered.contains(tid)) {
				this.textureMaterialComputeShader.bind();
				txt.bindUniform(txtDiffuseUniformLoc, 5);
				this.textureMaterialComputeShader.setUniform(TextureMaterialComputeShader.APPLY_BLOOM, false);
				this.textureMaterialComputeShader.storeUniformUnsigned(currentMaterialIdLoc, tid);

				GL_W.glDispatchCompute(groupsX, groupsY, 1);

				alreadyRendered.add(tid);

				if (GL_FORCE_SYNC_COMPUTE_SHADERS) {
					GL_W.glMemoryBarrier(GL_W.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT);
				}
			}
		} else if (entity instanceof GameObject go && go.getObjectIdLocation() == AttributeLocation.ENTITY
				&& entity instanceof VariationOwner variationOwner) {
			this.materialComputeShader.bind();

			this.materialComputeShader.setUniform(MaterialComputeShader.COLOR_VARIATION, true);
			this.materialComputeShader.setUniform(MaterialComputeShader.VARIATION_MIN, variationOwner.getMinVariation());
			this.materialComputeShader.setUniform(MaterialComputeShader.VARIATION_MAX, variationOwner.getMaxVariation());
			this.materialComputeShader.setUniform(MaterialComputeShader.VARIATION_CELLS_SIZE, variationOwner.getVariationCellSize());
			this.materialComputeShader.setUniform(MaterialComputeShader.SINGLE_OBJECT, true);
			this.materialComputeShader.setUniformUnsigned(MaterialComputeShader.SINGLE_OBJECT_ID, go.getObjectId());
			this.materialComputeShader.setUniform(MaterialComputeShader.VARIATION_MAP_SCALE, 0.05f);
			if (variationOwner.useObjectTransform() && go instanceof AbsoluteTransform3DOwner ato) {
				this.materialComputeShader.setUniform(MaterialComputeShader.USE_OBJECT_TRANSFORM, true);
				this.materialComputeShader.setUniform(MaterialComputeShader.INVERSE_OBJECT_TRANSFORM, ato.getAbsoluteTransform().invert());
			}

			this.variationMap.bindUniform(variationMapUniformLoc, 5);

			GL_W.glDispatchCompute(groupsX, groupsY, 1);

			if (GL_FORCE_SYNC_COMPUTE_SHADERS) {
				GL_W.glMemoryBarrier(GL_W.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT);
			}
		}
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
		this.shadowMap.bind(8);

		computeShader.setUniform(MaterialComputeShader.LIGHT_COLOR, worldScene.getLightColor());
		computeShader.setUniform(MaterialComputeShader.LIGHT_DIR, worldScene.getLightDirection());
		computeShader.setUniform(MaterialComputeShader.LIGHT_INTENSITY, worldScene.getLightIntensity());
		computeShader.setUniform(MaterialComputeShader.AMBIENT_LIGHT, worldScene.getAmbientLight());
		computeShader.setUniform(ShadowShader.LIGHT_SPACE_MATRIX, worldScene.getLightSpaceMatrix());

		if (needRegen) {
			this.posTexture.bindUniform(computeShader.getUniformLocation("uPosTex"), 0);
			this.normalTexture.bindUniform(computeShader.getUniformLocation("uNormalTex"), 1);
			this.uvTexture.bindUniform(computeShader.getUniformLocation("uUvTex"), 2);
			this.idsTexture.bindUniform(computeShader.getUniformLocation("uIdsTex"), 3);
			this.depthTexture.bindUniform(computeShader.getUniformLocation("uDepthTex"), 4);
			this.shadowMap.bindUniform(computeShader.getUniformLocation("shadowMap"), 8);

			computeShader.setUniform(ComputeShader.INPUT_SIZE, resolution);
			computeShader.setUniform(ComputeShader.OUTPUT_SIZE, resolution);
		}
	}

	protected void renderWorldScene(final WorldLevelScene worldScene, final Vector2i resolution, final boolean needRegen) {
		this.worldFramebuffer.bind();

		GL_W.glViewport(0, 0, resolution.x, resolution.y);

		GL_W.glEnable(GL_W.GL_DEPTH_TEST);
		GL_W.glEnable(GL_W.GL_CULL_FACE);
		GL_W.glCullFace(GL_W.GL_BACK);

		this.idsTexture.clear(new Vector4i(-1));

		GL_W.glClearColor(this.backgroundColor.x, this.backgroundColor.y, this.backgroundColor.z, this.backgroundColor.w);
		GL_W.glClear(GL_W.GL_COLOR_BUFFER_BIT | GL_W.GL_DEPTH_BUFFER_BIT);

		synchronized (worldScene.getEntitiesLock()) {
			this.renderScene(worldScene, Method.DEFERRED);
		}

		this.worldFramebuffer.unbind();
	}

	protected void renderScene(final Scene scene, final Method method) {
		final RenderShader meshShader;
		final RenderShader textEmitterShader;
		final RenderShader instanceEmitterShader;
		final RenderShader gradientMeshShader;
		switch (method) {
		case SHADOW, DEFERRED -> {
			meshShader = this.deferredTransferShader;
			textEmitterShader = null;
			instanceEmitterShader = this.deferredInstanceTransferShader;
			gradientMeshShader = null;
		}
		case DIRECT -> {
			meshShader = this.directShader;
			textEmitterShader = this.directTextShader;
			instanceEmitterShader = this.directInstanceShader;
			gradientMeshShader = this.directGradientShader;
		}
		default -> {
			throw new IllegalArgumentException(Objects.toString(method));
		}
		}

		this.setupSceneUniforms(Arrays.asList(meshShader,
				textEmitterShader,
				instanceEmitterShader,
				gradientMeshShader,
				this.lineDirectShader,
				this.lineInstanceDirectShader), scene, method);

		scene.forEach(entity -> this.renderEntityRecursive(entity,
				meshShader,
				textEmitterShader,
				instanceEmitterShader,
				gradientMeshShader,
				method,
				GameEngine.IDENTITY_MATRIX4F));

	}

	private void renderEntityRecursive(final SceneEntity entity, final RenderShader meshShader,
//			final RenderShader animatedMeshShader,
			final RenderShader textEmitterShader,
			final RenderShader instanceEmitterShader,
			final RenderShader gradientMeshShader,
//			final RenderShader swayMeshShader,
//			final RenderShader swayInstanceEmitterShader,
			final Method method,
			final Matrix4fc parentTransformMatrix) {

		if (!entity.isActive()) {
			return;
		}

		if (entity instanceof final RenderConditionOwner rco && !rco.fullFillsRenderConditions()) {
			return;
		}

		final Matrix4fc localTransformMatrix;
		if (entity instanceof final TransformOwner to && to.hasTransform()) {
			localTransformMatrix = to.getTransform().getMatrix();
		} else {
			localTransformMatrix = GameEngine.IDENTITY_MATRIX4F;
		}

		final Matrix4f worldTransform = new Matrix4f(parentTransformMatrix).mulAffine(localTransformMatrix);

		if (entity instanceof final GradientOwner go && method != Method.DIRECT) {
			GlobalLogger.warning("Gradient only supported for direct rendered meshes.");
		} else if (entity instanceof final GradientOwner go && method == Method.DIRECT) {
			if (entity instanceof final MeshOwner mo) {
				this.renderMesh(mo.getMesh(), entity, worldTransform, gradientMeshShader);
			}

			if (entity instanceof final AnimatedMeshOwner amo) {
				GlobalLogger.warning("Gradient AnimatedMesh rendering not supported !");
			}

			if (entity instanceof final InstanceEmitterOwner ieo) {
				GlobalLogger.warning("Gradient InstanceEmitter rendering not supported !");
			}

			if (entity instanceof final TextEmitterOwner amo) {
				GlobalLogger.warning("Gradient TextEmitter rendering not supported !");
			}
		} else {
			if (entity instanceof final MeshOwner mo) {
				this.renderMesh(mo.getMesh(), entity, worldTransform, meshShader);
			}

			if (entity instanceof final AnimatedMeshOwner amo) {
				final Matrix4f animatedTransform = entity instanceof final AnimatedTransformOwner ago ? ago.getAnimatedTransform()
						: worldTransform;
				this.renderMesh(amo.getAnimatedMesh(), entity, animatedTransform, meshShader);
			}

			if (entity instanceof final InstanceEmitterOwner teo) {
				this.renderInstanceEmitter(teo.getInstanceEmitter(), entity, worldTransform, instanceEmitterShader);
			}

			if (entity instanceof final TextEmitterOwner teo) {
				this.renderTextEmitter(teo.getTextEmitter(), entity, worldTransform, textEmitterShader);
			}
		}

		if (entity instanceof final TransformedBoundsOwner tbo) {
			this.drawDebugBounds(entity, tbo.getTransformedBounds(parentTransformMatrix));
		}

		if (entity instanceof final StaticMeshFootprintOwner smfo) {
			this.drawDebugFootprint(parentTransformMatrix, localTransformMatrix, smfo.getStaticMeshFootprint(), ColorMaterial.PINK);
		}
		if (entity instanceof final AnimatedMeshFootprintOwner amfo) {
			this.drawDebugFootprint(parentTransformMatrix, localTransformMatrix, amfo.getAnimatedMeshFootprint(), ColorMaterial.RED);
		}
		if (entity instanceof final FootprintOwner fo) {
			this.drawDebugFootprint(parentTransformMatrix, localTransformMatrix, fo.getFootprint(), ColorMaterial.YELLOW);
		}

		if (entity instanceof final EntityContainer<?> ec) {
			ec.forEach(child -> this.renderEntityRecursive(child,
					meshShader,
					textEmitterShader,
					instanceEmitterShader,
					gradientMeshShader,
					method,
					worldTransform));
		}
	}

	private void drawDebugFootprint(
			final Matrix4fc parentTransform,
			final Matrix4fc localTransform,
			final Footprint footprint,
			final ColorMaterial color) {
		if (this.deferredPass && DEBUG_FOOTPRINTS && this.deferredTransferShader != null) {
			this.QUAD.bind();
			this.deferredTransferShader.bind();

			final Vector2fc center = footprint.getCenter();
			final Vector2ic size = footprint.getSize();

			final Matrix4f localOffset = new Matrix4f()
					.translate(center.x() - 0.5f, (float) color.ordinal() / ColorMaterial.values().length, center.y() - 0.5f)
					.scale(size.x(), 1f, size.y());

			final Matrix4f mat = new Matrix4f(parentTransform).mul(new Matrix4f(localTransform)).mul(localOffset);

			this.deferredTransferShader.setUniform(RenderShader.TRANSFORMATION_MATRIX, mat);
			GL_W.glDisableVertexAttribArray(GameObject.MESH_ATTRIB_MATERIAL_ID_ID);
			GL_W.glVertexAttribI1ui(GameObject.MESH_ATTRIB_MATERIAL_ID_ID, color.getId());

			if (GL_LINE_SMOOTHING) {
				GL_W.glEnable(GL_W.GL_LINE_SMOOTH);
			}
			GL_W.glLineWidth(DEBUG_FOOTPRINTS_LINE_WIDTH);

			GL_W.glPolygonMode(PolygonMode.FRONT_AND_BACK.getGlId(), PolygonDrawMode.LINE.getGlId());

			GL_W.glDrawElements(this.deferredTransferShader.getBeginMode().getGlId(), this.QUAD.getIndicesCount(), GL_W.GL_UNSIGNED_INT, 0);

			GL_W.glPolygonMode(GL_W.GL_FRONT_AND_BACK, GL_W.GL_FILL);
			GL_W.glEnableVertexAttribArray(GameObject.MESH_ATTRIB_MATERIAL_ID_ID);
		}
	}

	private void setupSceneUniforms(final Collection<RenderShader> collection, final Scene scene, final Method method) {
		final Camera cam = scene.getCamera();
		cam.getProjection().update(this.outputResolution);
		final Matrix4fc viewMatrix;
		final Matrix4fc projectionMatrix;
		if (method == Method.SHADOW) {
			viewMatrix = ((SunLightOwner) scene).getLightViewMatrix();
			projectionMatrix = ((SunLightOwner) scene).getLightProjectionMatrix();
		} else {
			viewMatrix = cam.getViewMatrix();
			projectionMatrix = cam.getProjection().getProjectionMatrix();
		}

		for (final RenderShader shader : collection) {
			if (shader == null) {
				continue;
			}

			shader.bind();

			shader.setUniform(RenderShader.VIEW_MATRIX, viewMatrix);
			shader.setUniform(RenderShader.PROJECTION_MATRIX, projectionMatrix);

			if (scene instanceof final SunLightOwner slo && shader.createUniform(ShadowShader.LIGHT_SPACE_MATRIX)) {
				shader.setUniform(ShadowShader.LIGHT_SPACE_MATRIX, slo.getLightSpaceMatrix());
			}
			if (scene instanceof final WorldLevelScene wls && shader.createUniform(TransferShader.SCROLL_DIRECTION)) {
				shader.setUniform(TransferShader.SCROLL_DIRECTION, wls.getWindDirection());
			}
		}

	}

	private void renderInstanceEmitter(
			final InstanceEmitter obj,
			final SceneEntity entity,
			final Matrix4f transformationMatrix,
			final RenderShader shader) {
		if (this.checkComponent(obj, entity)) {
			return;
		}
		final Mesh mesh = obj.getParticleMesh();
		obj.bind();
		shader.bind();

		if (shader.createUniform(RenderShader.TRANSFORMATION_MATRIX)) {
			shader.setUniform(RenderShader.TRANSFORMATION_MATRIX, transformationMatrix);
		}

		// duplicate code, maybe don't do this
		if (entity instanceof TransparentEntity) {
			GL_W.glEnable(GL_W.GL_BLEND);
			GL_W.glBlendFunc(GL_W.GL_SRC_ALPHA, GL_W.GL_ONE_MINUS_SRC_ALPHA);
		} else {
			GL_W.glDisable(GL_W.GL_BLEND);
		}

		if (mesh instanceof final TexturedMesh txtMesh && shader.createUniform(DirectShader.TXT0)) {
			txtMesh.getTexture().bindUniform(shader.getUniformLocation(DirectShader.TXT0), 0);
		}

		if (shader.createUniform(DirectShader.TINT)) {
			final TintOwner tintOwner;
			if (entity instanceof final TintOwner tintEntity) {
				tintOwner = tintEntity;
			} else if (mesh instanceof final TintOwner tintMesh) {
				tintOwner = tintMesh;
			} else {
				tintOwner = null;
			}

			shader.setUniform(DirectShader.TINT,
					(tintOwner == null || tintOwner.getTint() == null) ? DirectShader.DEFAULT_TINT : tintOwner.getTint());
		}

		if (shader.createUniform(TransferShader.DEFORM_RATIO)) {
			final SwayOwner swayOwner;
			if (entity instanceof final SwayOwner swayEntity) {
				swayOwner = swayEntity;
			} else if (mesh instanceof final SwayOwner swayMesh) {
				swayOwner = swayMesh;
			} else {
				swayOwner = null;
			}

			shader.setUniform(TransferShader.APPLY_SWAY, swayOwner != null);
			if (swayOwner != null) {
				shader.setUniform(TransferShader.DEFORM_RATIO, swayOwner.getDeformRatio());
				shader.setUniform(TransferShader.SPEED_RATIO, swayOwner.getSpeedRatio());
				shader.setUniform(TransferShader.SCALE_RATIO, swayOwner.getScaleRatio());
				shader.setUniform(TransferShader.TIME, (float) PGLogic.TOTAL_TIME());

				this.swayMap.bindUniform(shader.getUniformLocation(TransferShader.SWAY_MAP), 1);
			}
		}

		if (entity instanceof final MaterialIdOwner go && go.isEntityMaterialId()) {
			// id is in the entity
			final int matId = go.getMaterialId();

			GL_W.glDisableVertexAttribArray(GameObject.MESH_ATTRIB_MATERIAL_ID_ID);
			GL_W.glVertexAttribI1ui(GameObject.MESH_ATTRIB_MATERIAL_ID_ID, matId);
		} else if (mesh instanceof final TexturedMesh txtMesh) { // id in the texture id
			final int txtId = txtMesh.getTexture().getGlId();

			GL_W.glDisableVertexAttribArray(GameObject.MESH_ATTRIB_MATERIAL_ID_ID);
			GL_W.glVertexAttribI1ui(GameObject.MESH_ATTRIB_MATERIAL_ID_ID, txtId);
		} else if (mesh instanceof final MaterialMesh matMesh) { // id is in the mesh
			final int matId = matMesh.getMaterialId();

			GL_W.glDisableVertexAttribArray(GameObject.MESH_ATTRIB_MATERIAL_ID_ID);
			GL_W.glVertexAttribI1ui(GameObject.MESH_ATTRIB_MATERIAL_ID_ID, matId);
		} else if (entity instanceof final MaterialIdOwner mo && !mo.isEntityMaterialId()
				&& mesh.getVbo().containsKey(GameObject.MESH_ATTRIB_MATERIAL_ID_ID)) {
			// id is in the mesh buffer
			GL_W.glEnableVertexAttribArray(GameObject.MESH_ATTRIB_MATERIAL_ID_ID);
		}

		if (entity instanceof final GameObject go && go.getObjectIdLocation() != AttributeLocation.MESH) {
			// object id is in the entity
			final Vector3ic objId = go.getObjectId();
			GL_W.glDisableVertexAttribArray(GameObject.MESH_ATTRIB_OBJECT_ID_ID);
			GL_W.glVertexAttribI3ui(GameObject.MESH_ATTRIB_OBJECT_ID_ID, objId.x(), objId.y(), objId.z());
		} else if (entity instanceof final GameObject go && go.getObjectIdLocation() == AttributeLocation.MESH) {
			// object id is in the mesh
			GL_W.glEnableVertexAttribArray(GameObject.MESH_ATTRIB_OBJECT_ID_ID);
		}

		GL_W.glPolygonMode(PolygonMode.FRONT_AND_BACK.getGlId(), PolygonDrawMode.FILL.getGlId());

		if (mesh instanceof final LineMesh lineMesh) {
			if (GL_LINE_SMOOTHING) {
				if (lineMesh.isLineSmooth()) {
					GL_W.glEnable(GL_W.GL_LINE_SMOOTH);
				} else {
					GL_W.glDisable(GL_W.GL_LINE_SMOOTH);
				}
			}
			GL_W.glLineWidth(lineMesh.getLineWidth());
		}

		final int particleCount = entity instanceof ParticleCountOwner pco ? pco.getParticleCount() : obj.getParticleCount();
		GL_W.glDrawElementsInstanced(shader.getBeginMode().getGlId(), mesh.getIndicesCount(), GL_W.GL_UNSIGNED_INT, 0, particleCount);

		this.drawDebugTrianglesInstanced(mesh, transformationMatrix, obj, particleCount);

		shader.unbind();

		mesh.unbind();
	}

	private void renderTextEmitter(
			final TextEmitter obj,
			final SceneEntity entity,
			final Matrix4f transformationMatrix,
			final RenderShader shader) {
		if (this.checkComponent(obj, entity)) {
			return;
		}
		final InstanceEmitter instances = obj.getInstances();
		final Mesh mesh = instances.getParticleMesh();
		shader.bind();
		instances.bind();

		if (shader.createUniform(RenderShader.TRANSFORMATION_MATRIX)) {
			shader.setUniform(RenderShader.TRANSFORMATION_MATRIX, transformationMatrix);
		}

		final boolean transparent = entity instanceof TransparentEntity
				|| (obj.isTransparent() != null ? (boolean) obj.isTransparent() : TextEmitter.DEFAULT_TRANSPARENT);

		if (entity instanceof SwayOwner) {
			GlobalLogger.warning("Text rendering not compatible with sway.");
		}

		this.fontTexture.bindUniform(shader.getUniformLocation(TextDirectShader.TXT0), 1);

		shader.setUniform(TextDirectShader.FG_COLOR,
				(obj.getForegroundColor() != null ? obj.getForegroundColor() : TextEmitter.DEFAULT_FG_COLOR)
						.mul(1, 1, 1, obj.getOpacity(), new Vector4f()));
		shader.setUniform(TextDirectShader.BG_COLOR,
				(obj.getBackgroundColor() != null ? obj.getBackgroundColor() : TextEmitter.DEFAULT_BG_COLOR)
						.mul(1, 1, 1, obj.getOpacity(), new Vector4f()));
		shader.setUniform(TextDirectShader.TRANSPARENT, transparent);
		shader.setUniformUnsigned(TextDirectShader.TEXT_LENGTH, obj.getCharCount());

		GL_W.glDisable(GL_W.GL_CULL_FACE);
		if (transparent) {
			GL_W.glEnable(GL_W.GL_BLEND);
			GL_W.glBlendFunc(GL_W.GL_SRC_ALPHA, GL_W.GL_ONE_MINUS_SRC_ALPHA);
		} else {
			GL_W.glDisable(GL_W.GL_BLEND);
		}

		GL_W.glDrawElementsInstanced(shader.getBeginMode()
				.getGlId(), mesh.getIndicesCount(), GL_W.GL_UNSIGNED_INT, 0, instances.getParticleCount());

		GL_W.glDisable(GL_W.GL_BLEND);
		GL_W.glEnable(GL_W.GL_CULL_FACE);

		this.drawDebugTrianglesText(mesh, transformationMatrix, obj);

		instances.unbind();
	}

	private void renderMesh(final Mesh mesh, final SceneEntity entity, final Matrix4f transformationMatrix, final RenderShader shader) {
		if (this.checkComponent(mesh, entity)) {
			return;
		}
		mesh.bind();
		shader.bind();

		if (shader.createUniform(RenderShader.TRANSFORMATION_MATRIX)) {
			shader.setUniform(RenderShader.TRANSFORMATION_MATRIX, transformationMatrix);
		}

		if (entity instanceof TransparentEntity te) {
			GL_W.glEnable(GL_W.GL_BLEND);
			GL_W.glBlendFunc(GL_W.GL_SRC_ALPHA, GL_W.GL_ONE_MINUS_SRC_ALPHA);
		} else {
			GL_W.glDisable(GL_W.GL_BLEND);
		}

		if (mesh instanceof final TexturedMesh txtMesh && shader.createUniform(DirectShader.TXT0)) {
			txtMesh.getTexture().bindUniform(shader.getUniformLocation(DirectShader.TXT0), 0);
			shader.setUniform(DirectShader.HAS_TEXTURE, true);
		} else {
			shader.setUniform(DirectShader.HAS_TEXTURE, false);
		}

		if (shader.createUniform(DirectShader.TINT)) {
			final TintOwner tintOwner;
			if (entity instanceof final TintOwner tintEntity) {
				tintOwner = tintEntity;
			} else if (mesh instanceof final TintOwner tintMesh) {
				tintOwner = tintMesh;
			} else {
				tintOwner = null;
			}

			shader.setUniform(DirectShader.TINT,
					(tintOwner == null || tintOwner.getTint() == null) ? DirectShader.DEFAULT_TINT : tintOwner.getTint());
		}

		if (shader.createUniform(TransferShader.APPLY_SWAY)) {
			final SwayOwner swayOwner;
			if (entity instanceof final SwayOwner swayEntity) {
				swayOwner = swayEntity;
			} else if (mesh instanceof final SwayOwner swayMesh) {
				swayOwner = swayMesh;
			} else {
				swayOwner = null;
			}

			shader.setUniform(TransferShader.APPLY_SWAY, swayOwner != null);

			if (swayOwner != null) {
				shader.setUniform(TransferShader.DEFORM_RATIO, swayOwner.getDeformRatio());
				shader.setUniform(TransferShader.SPEED_RATIO, swayOwner.getSpeedRatio());
				shader.setUniform(TransferShader.SCALE_RATIO, swayOwner.getScaleRatio());
				shader.setUniform(TransferShader.TIME, (float) PGLogic.TOTAL_TIME());

				this.swayMap.bindUniform(shader.getUniformLocation(TransferShader.SWAY_MAP), 1);
			}
		}

		if (shader.createUniform(GradientShader.GRADIENT_DIRECTION)) {
			final GradientOwner gradientOwner;
			if (entity instanceof final GradientOwner gradientEntity) {
				gradientOwner = gradientEntity;
			} else if (mesh instanceof final GradientOwner gradientMesh) {
				gradientOwner = gradientMesh;
			} else {
				gradientOwner = null;
			}

			shader.setUniformUnsigned(GradientShader.GRADIENT_DIRECTION,
					((gradientOwner == null || gradientOwner.getDirection() == null) ? GradientShader.DEFAULT_DIRECTION
							: gradientOwner.getDirection()).getId());
			shader.setUniform(GradientShader.GRADIENT_RANGE,
					(gradientOwner == null || gradientOwner.getRange() == null) ? GradientShader.DEFAULT_RANGE : gradientOwner.getRange());
			shader.setUniform(GradientShader.START_COLOR,
					(gradientOwner == null || gradientOwner.getStartColor() == null) ? GradientShader.DEFAULT_START_COLOR
							: gradientOwner.getStartColor());
			shader.setUniform(GradientShader.END_COLOR,
					(gradientOwner == null || gradientOwner.getEndColor() == null) ? GradientShader.DEFAULT_END_COLOR
							: gradientOwner.getEndColor());
		}

		if (entity instanceof final MaterialIdOwner mo && mo.isEntityMaterialId()) {
			// id is in the entity
			final int matId = mo.getMaterialId();

			GL_W.glDisableVertexAttribArray(GameObject.MESH_ATTRIB_MATERIAL_ID_ID);
			GL_W.glVertexAttribI1ui(GameObject.MESH_ATTRIB_MATERIAL_ID_ID, matId);
		} else if (mesh instanceof final TexturedMesh txtMesh) {
			// id in the texture id
			final int txtId = ColorMaterial.values().length + txtMesh.getTexture().getGlId();

			GL_W.glDisableVertexAttribArray(GameObject.MESH_ATTRIB_MATERIAL_ID_ID);
			GL_W.glVertexAttribI1ui(GameObject.MESH_ATTRIB_MATERIAL_ID_ID, txtId);
		} else if (mesh instanceof final MaterialMesh matMesh) {
			// id is in the mesh
			final int matId = matMesh.getMaterialId();

			GL_W.glDisableVertexAttribArray(GameObject.MESH_ATTRIB_MATERIAL_ID_ID);
			GL_W.glVertexAttribI1ui(GameObject.MESH_ATTRIB_MATERIAL_ID_ID, matId);
		} else if (entity instanceof final MaterialIdOwner mo && !mo.isEntityMaterialId()
				&& mesh.getVbo().containsKey(GameObject.MESH_ATTRIB_MATERIAL_ID_ID)) {
			// id is in the mesh buffer
			GL_W.glEnableVertexAttribArray(GameObject.MESH_ATTRIB_MATERIAL_ID_ID);
		}

		if (entity instanceof final ObjectIdOwner oio && oio.getObjectIdLocation() != AttributeLocation.MESH) {
			// object id is in the entity
			final Vector3ic objId = oio.getObjectId();

			GL_W.glDisableVertexAttribArray(GameObject.MESH_ATTRIB_OBJECT_ID_ID);
			GL_W.glVertexAttribI3ui(GameObject.MESH_ATTRIB_OBJECT_ID_ID, objId.x(), objId.y(), objId.z());
		} else if (entity instanceof final ObjectIdOwner oio && oio.getObjectIdLocation() == AttributeLocation.MESH) {
			// object id is in the mesh
			GL_W.glEnableVertexAttribArray(GameObject.MESH_ATTRIB_OBJECT_ID_ID);
		}

		if (mesh instanceof final LineMesh lineMesh) {
			if (GL_LINE_SMOOTHING) {
				if (lineMesh.isLineSmooth()) {
					GL_W.glEnable(GL_W.GL_LINE_SMOOTH);
				} else {
					GL_W.glDisable(GL_W.GL_LINE_SMOOTH);
				}
			}
			if (GL_LINE_WIDTH_CHECK) {
				final FloatBuffer range = BufferUtils.createFloatBuffer(2);
				GL_W.glGetFloatv(GL_W.GL_ALIASED_LINE_WIDTH_RANGE, range);

				final float min = range.get(0);
				final float max = range.get(1);

				if (lineMesh.getLineWidth() < min || lineMesh.getLineWidth() > max) {
					GlobalLogger.severe("Width out of range: " + lineMesh.getLineWidth() + " [" + min + ", " + max + "].");
				}
			}
			GL_W.glLineWidth(lineMesh.getLineWidth());
		} else if (GL_LINE_SMOOTHING) {
			GL_W.glDisable(GL_W.GL_LINE_SMOOTH);
		}

		GL_W.glPolygonMode(mesh.getPolygonMode().getGlId(), mesh.getPolygonDrawMode().getGlId());

		if (mesh.usesEBO()) {
			GL_W.glDrawElements(mesh.getBeginMode().getGlId(), mesh.getIndicesCount(), GL_W.GL_UNSIGNED_INT, 0);
		} else {
			GL_W.glDisable(GL_W.GL_CULL_FACE);
			GL_W.glDrawArrays(GL_W.GL_TRIANGLE_STRIP, 0, mesh.getVertexCount());
			GL_W.glEnable(GL_W.GL_CULL_FACE);
		}

		this.drawDebugTriangles(mesh, transformationMatrix);

		mesh.unbind();
	}

	private void drawDebugTriangles(final Mesh mesh, final Matrix4f transformationMatrix) {
		if (!this.deferredPass && DEBUG_TRIANGLES && this.lineDirectShader != null) {
			this.lineDirectShader.bind();

			this.lineDirectShader.setUniform(LineDirectShader.TINT, DEBUG_TRIANGLE_COLOR);
			this.lineDirectShader.setUniform(RenderShader.TRANSFORMATION_MATRIX, transformationMatrix);

			if (GL_LINE_SMOOTHING) {
				GL_W.glEnable(GL_W.GL_LINE_SMOOTH);
			}
			GL_W.glLineWidth(DEBUG_TRIANGLES_LINE_WIDTH);

			GL_W.glPolygonMode(PolygonMode.FRONT_AND_BACK.getGlId(), PolygonDrawMode.LINE.getGlId());

			if (mesh.usesEBO()) {
				GL_W.glDrawElements(this.lineDirectShader.getBeginMode().getGlId(), mesh.getIndicesCount(), GL_W.GL_UNSIGNED_INT, 0);
			} else {
				GlobalLogger.warning("Unsupported: " + mesh);
			}

			GL_W.glPolygonMode(GL_W.GL_FRONT_AND_BACK, GL_W.GL_FILL);
		} else if (this.deferredPass && DEBUG_TRIANGLES && this.deferredTransferShader != null) {
			this.deferredTransferShader.bind();

			if (mesh.getVbo().containsKey(GameObject.MESH_ATTRIB_MATERIAL_ID_ID)) {
				GL_W.glDisableVertexAttribArray(GameObject.MESH_ATTRIB_MATERIAL_ID_ID);
			}
			GL_W.glVertexAttribI1ui(GameObject.MESH_ATTRIB_MATERIAL_ID_ID, ColorMaterial.LIGHT_GREEN.getId());
			this.deferredTransferShader.setUniform(RenderShader.TRANSFORMATION_MATRIX, transformationMatrix);

			if (GL_LINE_SMOOTHING) {
				GL_W.glEnable(GL_W.GL_LINE_SMOOTH);
			}
			GL_W.glLineWidth(DEBUG_TRIANGLES_LINE_WIDTH);
			GL_W.glDisable(GL_W.GL_DEPTH_TEST);

			GL_W.glPolygonMode(PolygonMode.FRONT_AND_BACK.getGlId(), PolygonDrawMode.LINE.getGlId());

			if (mesh.usesEBO()) {
				GL_W.glDrawElements(mesh.getBeginMode().getGlId(), mesh.getIndicesCount(), GL_W.GL_UNSIGNED_INT, 0);
			} else {
				GlobalLogger.warning("Unsupported: " + mesh);
			}

			GL_W.glEnable(GL_W.GL_DEPTH_TEST);
			GL_W.glPolygonMode(GL_W.GL_FRONT_AND_BACK, GL_W.GL_FILL);
			if (mesh.getVbo().containsKey(GameObject.MESH_ATTRIB_MATERIAL_ID_ID)) {
				GL_W.glEnableVertexAttribArray(GameObject.MESH_ATTRIB_MATERIAL_ID_ID);
			}
		}
	}

	private void drawDebugTrianglesInstanced(
			final Mesh mesh,
			final Matrix4f transformationMatrix,
			final InstanceEmitter instances,
			final int count) {
		if (!this.deferredPass && DEBUG_TRIANGLES && this.lineInstanceDirectShader != null) {
			this.lineInstanceDirectShader.bind();

			this.lineInstanceDirectShader.setUniform(LineDirectShader.TINT, DEBUG_TRIANGLE_COLOR);
			this.lineInstanceDirectShader.setUniform(RenderShader.TRANSFORMATION_MATRIX, transformationMatrix);

			if (GL_LINE_SMOOTHING) {
				GL_W.glEnable(GL_W.GL_LINE_SMOOTH);
			}
			GL_W.glLineWidth(DEBUG_TRIANGLES_LINE_WIDTH);
//			GL_W.glDisable(GL_W.GL_DEPTH_TEST);

			GL_W.glPolygonMode(GL_W.GL_FRONT_AND_BACK, GL_W.GL_LINE);

			GL_W.glDrawElementsInstanced(this.lineInstanceDirectShader.getBeginMode()
					.getGlId(), mesh.getIndicesCount(), GL_W.GL_UNSIGNED_INT, 0, count);

//			GL_W.glEnable(GL_W.GL_DEPTH_TEST);
			GL_W.glPolygonMode(GL_W.GL_FRONT_AND_BACK, GL_W.GL_FILL);
		}
	}

	private void drawDebugTrianglesText(final Mesh mesh, final Matrix4f transformationMatrix, final TextEmitter emitter) {
		if (!this.deferredPass && DEBUG_TRIANGLES && this.lineInstanceDirectShader != null) {
			this.lineInstanceDirectShader.bind();

			this.lineInstanceDirectShader.setUniform(LineDirectShader.TINT, DEBUG_TRIANGLE_COLOR);
			this.lineInstanceDirectShader.setUniform(RenderShader.TRANSFORMATION_MATRIX, transformationMatrix);

			if (GL_LINE_SMOOTHING) {
				GL_W.glEnable(GL_W.GL_LINE_SMOOTH);
			}
			GL_W.glLineWidth(DEBUG_TRIANGLES_LINE_WIDTH);
//			GL_W.glDisable(GL_W.GL_DEPTH_TEST);

			GL_W.glPolygonMode(GL_W.GL_FRONT_AND_BACK, GL_W.GL_LINE);

			GL_W.glDrawElementsInstanced(this.lineInstanceDirectShader.getBeginMode()
					.getGlId(), mesh.getIndicesCount(), GL_W.GL_UNSIGNED_INT, 0, emitter.getStringLength());

//			GL_W.glEnable(GL_W.GL_DEPTH_TEST);
			GL_W.glPolygonMode(GL_W.GL_FRONT_AND_BACK, GL_W.GL_FILL);
		}
	}

	private void drawDebugBounds(final SceneEntity entity, final Shape shape) {
		if (shape == null) {
			GlobalLogger.warning("Entity: " + entity.getId() + " has no/invalid bounds.");
		}
		if (!this.deferredPass && DEBUG_BOUNDS && this.lineDirectShader != null && shape != null) {
			this.QUAD.bind();
			this.lineDirectShader.bind();

			final Rectangle2D bounds = shape.getBounds2D();

			final float centerX = (float) bounds.getCenterX();
			final float centerY = (float) bounds.getCenterY();
			final float width = (float) bounds.getWidth();
			final float height = (float) bounds.getHeight();

			final Matrix4f transform = new Matrix4f().identity().translate(centerX, 0, centerY).scale(width, 1, height);

			this.lineDirectShader.setUniform(RenderShader.TRANSFORMATION_MATRIX, transform);
			this.lineDirectShader.setUniform(LineDirectShader.TINT,
					entity instanceof DebugBoundsColor dbc ? dbc.getBoundsColor().getColor() : DEBUG_BOUNDS_COLOR);

			if (GL_LINE_SMOOTHING) {
				GL_W.glEnable(GL_W.GL_LINE_SMOOTH);
			}
			GL_W.glLineWidth(DEBUG_BOUNDS_LINE_WIDTH);
			if (DEBUG_BOUNDS_IGNORE_DEPTH) {
				GL_W.glDisable(GL_W.GL_DEPTH_TEST);
			}

			GL_W.glPolygonMode(PolygonMode.FRONT_AND_BACK.getGlId(), PolygonDrawMode.LINE.getGlId());

			GL_W.glDrawElements(this.lineDirectShader.getBeginMode().getGlId(), this.QUAD.getIndicesCount(), GL_W.GL_UNSIGNED_INT, 0);

			if (DEBUG_BOUNDS_IGNORE_DEPTH) {
				GL_W.glEnable(GL_W.GL_DEPTH_TEST);
			}
			GL_W.glPolygonMode(GL_W.GL_FRONT_AND_BACK, GL_W.GL_FILL);
		}
	}

	protected void resizeFramebuffer(final Framebuffer fb, final Vector2i resolution) {
		fb.bind();
		fb.resize(resolution);
		fb.unbind();
		GlobalLogger.log("Resized framebuffer: " + fb.getId() + " (" + resolution + ")");
	}

	private Framebuffer genShadowFramebuffer() {
		final Framebuffer framebuffer = new Framebuffer(SHADOW_FRAMEBUFFER_NAME);
		framebuffer.gen();
		framebuffer.bind();

		this.shadowMap = new SingleTexture(SHADOW_FRAMEBUFFER_NAME + ".shadows", new Vector2i(1024));
		this.shadowMap.setColorFormat(DataType.FLOAT, TexelFormat.DEPTH, TexelInternalFormat.DEPTH_COMPONENT24);
		this.shadowMap.setFilters(TextureFilter.NEAREST);
		this.shadowMap.setGenerateMipmaps(false);
		this.shadowMap.setup();
		this.cache.addTexture(this.shadowMap);

		framebuffer.attachTexture(FrameBufferAttachment.DEPTH, SHADOW_FRAMEBUFFER_SHADOW_IDX, this.shadowMap);

		framebuffer.setup();

		framebuffer.unbind();

		return framebuffer;
	}

	private Framebuffer genWorldFramebuffer(final Vector2i resolution) {
		final Framebuffer framebuffer = new Framebuffer(WORLD_FRAMEBUFFER_NAME);
		framebuffer.gen();
		framebuffer.bind();

		final int width = resolution.x;
		final int height = resolution.y;

		this.depthTexture = new SingleTexture(WORLD_FRAMEBUFFER_NAME + ".depth", width, height);
		this.depthTexture.setDataType(DataType.FLOAT);
		this.depthTexture.setTexelFormat(TexelFormat.DEPTH);
		this.depthTexture.setTexelInternalFormat(TexelInternalFormat.DEPTH_COMPONENT24);
		this.depthTexture.setFilters(TextureFilter.NEAREST);
		this.depthTexture.setWraps(TextureWrap.CLAMP_TO_EDGE);
		this.depthTexture.setGenerateMipmaps(false);
		this.depthTexture.setup();
		this.cache.addTexture(this.depthTexture);
		framebuffer.attachTexture(FrameBufferAttachment.DEPTH, this.depthTexture);

		this.posTexture = new SingleTexture(WORLD_FRAMEBUFFER_NAME + ".pos", width, height);
		this.posTexture.setDataType(DataType.FLOAT);
		this.posTexture.setTexelFormat(TexelFormat.RGB);
		this.posTexture.setTexelInternalFormat(TexelInternalFormat.RGB32F);
		this.posTexture.setFilters(TextureFilter.NEAREST);
		this.posTexture.setGenerateMipmaps(false);
		this.posTexture.setup();
		this.cache.addTexture(this.posTexture);
		framebuffer.attachTexture(FrameBufferAttachment.COLOR_FIRST, WORLD_FRAMEBUFFER_POS_IDX, this.posTexture);

		this.normalTexture = new SingleTexture(WORLD_FRAMEBUFFER_NAME + ".normal", width, height);
		this.normalTexture.setDataType(DataType.FLOAT);
		this.normalTexture.setTexelFormat(TexelFormat.RGB);
		this.normalTexture.setTexelInternalFormat(TexelInternalFormat.RGB16F);
		this.normalTexture.setFilters(TextureFilter.NEAREST);
		this.normalTexture.setGenerateMipmaps(false);
		this.normalTexture.setup();
		this.cache.addTexture(this.normalTexture);
		framebuffer.attachTexture(FrameBufferAttachment.COLOR_FIRST, WORLD_FRAMEBUFFER_NORMAL_IDX, this.normalTexture);

		this.uvTexture = new SingleTexture(WORLD_FRAMEBUFFER_NAME + ".uv", width, height);
		this.uvTexture.setDataType(DataType.FLOAT);
		this.uvTexture.setTexelFormat(TexelFormat.RG);
		this.uvTexture.setTexelInternalFormat(TexelInternalFormat.RG16F);
		this.uvTexture.setFilters(TextureFilter.NEAREST);
		this.uvTexture.setGenerateMipmaps(false);
		this.uvTexture.setup();
		this.cache.addTexture(this.uvTexture);
		framebuffer.attachTexture(FrameBufferAttachment.COLOR_FIRST, WORLD_FRAMEBUFFER_UV_IDX, this.uvTexture);

		this.idsTexture = new SingleTexture(WORLD_FRAMEBUFFER_NAME + ".ids", width, height);
		this.idsTexture.setDataType(DataType.UINT);
		this.idsTexture.setTexelFormat(TexelFormat.RGBA_INTEGER);
		this.idsTexture.setTexelInternalFormat(TexelInternalFormat.RGBA32UI);
		this.idsTexture.setFilters(TextureFilter.NEAREST);
		this.idsTexture.setGenerateMipmaps(false);
		this.idsTexture.setup();
		this.cache.addTexture(this.idsTexture);
		framebuffer.attachTexture(FrameBufferAttachment.COLOR_FIRST, WORLD_FRAMEBUFFER_IDS_IDX, this.idsTexture);

		framebuffer.setup();
		framebuffer.unbind();

		GlobalLogger.log("Created framebuffer: " + framebuffer.getId() + " (" + resolution + ")");

		return framebuffer;
	}

	@Override
	public void cleanup() {
		// managed by the parent's cache
//		if (this.SCREEN != null) {
//			this.SCREEN.cleanup();
//			this.SCREEN = null;
//
//			this.QUAD.cleanup();
//			this.QUAD = null;
//
//			GlobalLogger.log("Cleaning up: " + this.getClass().getName());
//		}
	}

	public boolean pollObjectId(final boolean blocking) {
		this.isAwaitingObjectId.setValue(true);
		if (blocking) {
			return this.awaitObjectId();
		}
		return !this.isAwaitingObjectId.getValue();
	}

	public boolean isAwaitingObjectId() {
		return this.isAwaitingObjectId.getValue();
	}

	public boolean awaitObjectId() {
		this.isAwaitingObjectId.waitForFalse(POLL_OBJECT_ID_TIMEOUT);
		return !this.isAwaitingObjectId.getValue();
	}

	public Vector4ic getObjectId() {
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
		final IntBuffer pixel = BufferUtils.createIntBuffer(4);
		GL11.glReadPixels(x, y, 1, 1, this.idsTexture.getTexelFormat().getGlId(), this.idsTexture.getDataType().getGlId(), pixel);
		assert GL_W.checkError(
				"ReadPixels(" + mousePosition + ", " + this.idsTexture.getTexelFormat() + ", " + this.idsTexture.getDataType() + ")");
		this.worldFramebuffer.unbind(GL_W.GL_READ_FRAMEBUFFER);

		final int r = pixel.get(0);
		final int g = pixel.get(1);
		final int b = pixel.get(2);
		final int a = pixel.get(3);

		this.objectId.set(r, g, b, a);
	}

	public void addOutline(final SceneEntity e, final Vector4fc color) {
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

	public Vector4fc getOutline(final SceneEntity e) {
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

	public <T extends FilterShader<T>> void enableFilter(final FilterShaderConfiguration<T> config) {
		Objects.requireNonNull(config);

		this.enabledFilters.add(config);
	}

	public <T extends FilterShader<T>> void disableFilter(final FilterShaderConfiguration<T> config) {
		Objects.requireNonNull(config);

		this.enabledFilters.remove(config);
	}

	public void disableFilters() {
		this.enabledFilters.clear();
	}

	public Window getWindow() {
		return this.window;
	}

	public Map<Class<? extends FilterShader<?>>, FilterShader<?>> getFilterShaders() {
		return this.filterShaders;
	}

	public <T extends FilterShader<T>> T getFilterShader(final Class<T> class1) {
		return (T) this.filterShaders.get(class1);
	}

}
