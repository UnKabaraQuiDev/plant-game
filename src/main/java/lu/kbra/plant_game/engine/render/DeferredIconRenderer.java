package lu.kbra.plant_game.engine.render;

import org.joml.Vector3f;

import lu.kbra.plant_game.engine.entity.go.GameObject;
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

	public DeferredIconRenderer(GameEngine engine, Thread ownerThread) {
		super(engine, ownerThread);

		fakeWorld = new WorldLevelScene("fakeWorld", engine.getCache());

		fakeWorld.getCamera().getProjection().setPerspective(false);
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

		final Mesh mesh = obj.getMesh();
		final BoundingBox bb = mesh.getBoundingBox();

		final float radius = 0.5f * new Vector3f(bb.getMax()).sub(bb.getMin()).length();

		final float fovY = fakeWorld.getCamera().getProjection().getFov();
		final float distance = radius / (float) Math.sin(fovY / 2f);

		// final float fovX = fovY * fakeWorld.getCamera().getProjection().getAspectRatio();
		// distance = radius / (float) Math.sin(fovX / 2f);

		final Vector3f forwardVector = new Vector3f(1).normalize().negate();
		final Vector3f cameraPos = new Vector3f(bb.getCenter()).sub(forwardVector.mul(distance, new Vector3f()));
		fakeWorld.getCamera().lookAt(cameraPos, bb.getCenter());
		fakeWorld.getCamera().updateMatrix();

		if (!fakeWorld.getCamera().getProjection().isPerspective()) {
			fakeWorld.getCamera().getProjection().setSize(0.3f);
		}

		fakeWorld.add(obj);

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
		
		return texture;
	}

}
