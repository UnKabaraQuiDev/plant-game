package lu.kbra.plant_game.engine.render;

import org.joml.Vector3f;

import lu.kbra.plant_game.engine.entity.impl.GameObject;
import lu.kbra.plant_game.engine.scene.world.WorldLevelScene;
import lu.kbra.standalone.gameengine.GameEngine;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.graph.texture.SingleTexture;
import lu.kbra.standalone.gameengine.utils.gl.consts.DataType;
import lu.kbra.standalone.gameengine.utils.gl.consts.TexelFormat;
import lu.kbra.standalone.gameengine.utils.gl.consts.TexelInternalFormat;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextureFilter;
import lu.kbra.standalone.gameengine.utils.gl.wrapper.GL_W;

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
		outputTxt.setSize(size);
		outputTxt.bind();
		outputTxt.resize();
		outputTxt.unbind();

		resizeFramebuffer(worldFramebuffer, renderResolution);

		renderWorldScene(cache, fakeWorld, renderResolution, true);

		renderMaterials(cache, fakeWorld, renderResolution, true);

		renderOutlines(cache, renderResolution, true);

		blitToScreen(cache, engine.getWindow().getSize(), true);

		fakeWorld.getEntities().clear();

		final SingleTexture texture = new SingleTexture(obj.getId(), renderResolution);
		texture.setDataType(DataType.UBYTE);
		texture.setFormat(TexelFormat.RGBA);
		texture.setInternalFormat(TexelInternalFormat.RGBA8);
		texture.setFilters(TextureFilter.NEAREST);
		texture.setGenerateMipmaps(false);
		texture.setup();

		GL_W
				.glCopyImageSubData(outputTxt.getGlId(),
						outputTxt.getTextureType().getGlId(),
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
		assert GL_W.checkError("CopyImageSubData()");

		return texture;
	}

}
