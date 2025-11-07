package lu.kbra.plant_game.engine.render;

import org.joml.Vector3f;

import lu.kbra.plant_game.engine.entity.impl.GameObject;
import lu.kbra.plant_game.engine.scene.world.WorldLevelScene;
import lu.kbra.standalone.gameengine.GameEngine;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.graph.texture.SingleTexture;

public class DeferredIconRenderer extends DeferredCompositor {

	protected WorldLevelScene fakeWorld;

	public DeferredIconRenderer(GameEngine engine, Thread ownerThread) {
		super(engine, ownerThread);

		fakeWorld = new WorldLevelScene("fakeWorld", engine.getCache());

		fakeWorld.getCamera().getPosition().set(10, 10, 10);
		fakeWorld.getCamera().lookAt(fakeWorld.getCamera().getPosition(), GameEngine.ZERO);
		fakeWorld.getCamera().updateMatrix();
	}

	public SingleTexture renderIcon(
			GameEngine engine,
			GameObject obj,
			int size,
			Vector3f lightColor,
			Vector3f lightDir,
			float ambientLight) {
		final CacheManager cache = engine.getCache();

		fakeWorld.addEntity(obj);

		// super.render(engine, fakeWorld, null);

		renderResolution.set(size);
		outputResolution.set(size);

		resizeFramebuffer(worldFramebuffer, renderResolution);

		renderWorldScene(cache, fakeWorld, renderResolution, true);

		renderMaterials(cache, fakeWorld, renderResolution, true);

		renderOutlines(cache, renderResolution, true);

		blitToScreen(cache, engine.getWindow().getSize(), true);

		fakeWorld.getEntities().clear();

		return outputTxt;
	}

}
