package lu.kbra.plant_game.engine.render;

import java.lang.ref.Cleaner.Cleanable;

import org.joml.Vector3f;

import lu.kbra.plant_game.engine.entity.go.MeshGameObject;
import lu.kbra.plant_game.engine.scene.world.WorldLevelScene;
import lu.kbra.plant_game.engine.scene.world.modal.ActiveModalController;
import lu.kbra.standalone.gameengine.GameEngine;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.generated.gl_wrapper.GL_W;
import lu.kbra.standalone.gameengine.geom.BoundingBox;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.objs.entity.ParentAwareNode;
import lu.kbra.standalone.gameengine.utils.mem.img.MemImage;

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

	public MemImage renderIcon(
			final GameEngine engine,
			final MeshGameObject obj,
			final int size,
			final Vector3f lightColor,
			final Vector3f lightDir,
			final float ambientLight) {
		final CacheManager cache = engine.getCache();

		final Mesh mesh = obj.getMesh();
		final BoundingBox bb = mesh.getBoundingBox();

		final float radius = new Vector3f(bb.getMax()).sub(bb.getMin()).length() / 2;
		final float distance = radius * 2;

		final Vector3f forwardVector = new Vector3f(1).normalize().negate();
		final Vector3f cameraPos = new Vector3f(bb.getCenter()).sub(forwardVector.mul(distance, new Vector3f()));
		this.fakeWorld.getCamera().lookAt(cameraPos, bb.getCenter());
		this.fakeWorld.getCamera().updateMatrix();

		if (!this.fakeWorld.getCamera().getProjection().isPerspective()) {
			this.fakeWorld.getCamera().getProjection().setSize(1f / radius);
			this.fakeWorld.getCamera().getProjection().update();
		}

		synchronized (this.fakeWorld.getEntitiesLock()) {
			this.fakeWorld.getEntities().values().forEach(c -> {
				if (c instanceof ParentAwareNode pa) {
					pa.setParent(null);
				}
				if (c instanceof Cleanable cp) {
					cp.clean();
				}
			});
			this.fakeWorld.getEntities().clear();
		}
		this.fakeWorld.add(obj);

		this.renderResolution.set(size);
		this.outputResolution.set(size);
		this.outputTxt.setSize(size);
		this.outputTxt.bind();
		this.outputTxt.resize();
		this.outputTxt.unbind();

		GL_W.glEnable(GL_W.GL_MULTISAMPLE);

		this.resizeFramebuffer(this.worldFramebuffer, this.renderResolution);

		this.deferredPass = true;
		this.renderWorldScene(this.fakeWorld, this.renderResolution, true);
		this.deferredPass = false;

		this.renderMaterials(this.fakeWorld, this.renderResolution, true);

		this.renderOutlines(this.renderResolution, true);

		this.renderBloom(this.renderResolution, true);

		this.blitToScreen(engine.getWindow().getSize(), true);

		return this.outputTxt.getStoredImage();
	}

	public ActiveModalController getFakeWorld() {
		return this.fakeWorld;
	}

}
