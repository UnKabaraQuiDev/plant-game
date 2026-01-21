package lu.kbra.plant_game.engine.render;

import org.joml.Vector3f;

import lu.kbra.plant_game.engine.entity.go.MeshGameObject;
import lu.kbra.plant_game.engine.scene.world.WorldLevelScene;
import lu.kbra.standalone.gameengine.GameEngine;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.generated.gl_wrapper.GL_W;
import lu.kbra.standalone.gameengine.geom.BoundingBox;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.graph.texture.SingleTexture;
import lu.kbra.standalone.gameengine.utils.gl.consts.DataType;
import lu.kbra.standalone.gameengine.utils.gl.consts.TexelFormat;
import lu.kbra.standalone.gameengine.utils.gl.consts.TexelInternalFormat;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextureFilter;

public class DeferredIconRenderer extends DeferredCompositor {

	protected WorldLevelScene fakeWorld;

	public DeferredIconRenderer(final GameEngine engine, final Thread ownerThread) {
		super(engine, ownerThread);

		this.fakeWorld = new WorldLevelScene("fakeWorld", engine.getCache());

		this.fakeWorld.getCamera().getProjection().setPerspective(false);
		this.fakeWorld.getCamera().getPosition().set(10, 10, 10);
		this.fakeWorld.getCamera().lookAt(this.fakeWorld.getCamera().getPosition(), GameEngine.ZERO);
		this.fakeWorld.getCamera().updateMatrix();
	}

	public SingleTexture renderIcon(
			final GameEngine engine,
			final MeshGameObject obj,
			final int size,
			final Vector3f lightColor,
			final Vector3f lightDir,
			final float ambientLight) {
		final CacheManager cache = engine.getCache();

		final Mesh mesh = obj.getMesh();
		final BoundingBox bb = mesh.getBoundingBox();

		final float radius = 0.5f * new Vector3f(bb.getMax()).sub(bb.getMin()).length();

		final float fovY = this.fakeWorld.getCamera().getProjection().getFov();
		final float distance = radius / (float) Math.sin(fovY / 2f);

		final Vector3f forwardVector = new Vector3f(1).normalize().negate();
		final Vector3f cameraPos = new Vector3f(bb.getCenter()).sub(forwardVector.mul(distance, new Vector3f()));
		this.fakeWorld.getCamera().lookAt(cameraPos, bb.getCenter());
		this.fakeWorld.getCamera().updateMatrix();

		if (!this.fakeWorld.getCamera().getProjection().isPerspective()) {
			this.fakeWorld.getCamera().getProjection().setSize(0.3f);
			this.fakeWorld.getCamera().getProjection().update();
		}

		this.fakeWorld.add(obj);

		// super.render(engine, fakeWorld, null);

		this.renderResolution.set(size);
		this.outputResolution.set(size);
		this.outputTxt.setSize(size);
		this.outputTxt.bind();
		this.outputTxt.resize();
		this.outputTxt.unbind();

		GL_W.glEnable(GL_W.GL_MULTISAMPLE);

		this.resizeFramebuffer(this.worldFramebuffer, this.renderResolution);

		this.deferredPass = true;
		this.renderWorldScene(cache, this.fakeWorld, this.renderResolution, true);
		this.deferredPass = false;
		
		this.renderMaterials(cache, this.fakeWorld, this.renderResolution, true);

		this.renderOutlines(cache, this.renderResolution, true);

		this.blitToScreen(cache, engine.getWindow().getSize(), true);

		final SingleTexture texture = new SingleTexture(obj.getId(), this.renderResolution);
		cache.addTexture(texture);
		texture.setDataType(DataType.UBYTE);
		texture.setFormat(TexelFormat.RGBA);
		texture.setInternalFormat(TexelInternalFormat.RGBA8);
		texture.setFilters(TextureFilter.NEAREST);
		texture.setGenerateMipmaps(false);
		texture.setup();

		GL_W
				.glCopyImageSubData(this.outputTxt.getGlId(),
						this.outputTxt.getTextureType().getGlId(),
						0,
						0,
						0,
						0,
						texture.getGlId(),
						texture.getTextureType().getGlId(),
						0,
						0,
						0,
						0,
						texture.getWidth(),
						texture.getHeight(),
						1);

//		synchronized (fakeWorld.getEntitiesLock()) {
//			this.fakeWorld.getEntities().values().forEach(c -> {
//				if (c instanceof ParentAwareNode pa) {
//					pa.setParent(null);
//				}
//				if (c instanceof Cleanable cp) {
//					cp.clean();
//				}
//			});
//			this.fakeWorld.getEntities().clear();
//		}
//		cache.cleanup();

		return texture;
	}

	public WorldLevelScene getFakeWorld() {
		return fakeWorld;
	}

}
