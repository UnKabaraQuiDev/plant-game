package lu.kbra.plant_game;

import java.io.File;

import org.joml.Vector2f;

import lu.kbra.plant_game.engine.SimpleCompositor;
import lu.kbra.standalone.gameengine.GameEngine;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.graph.material.text.TextShader;
import lu.kbra.standalone.gameengine.graph.material.text.TextShader.TextMaterial;
import lu.kbra.standalone.gameengine.graph.texture.SingleTexture;
import lu.kbra.standalone.gameengine.impl.GameLogic;
import lu.kbra.standalone.gameengine.objs.entity.components.TextEmitterComponent;
import lu.kbra.standalone.gameengine.objs.entity.components.Transform3DComponent;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;
import lu.kbra.standalone.gameengine.scene.Scene2D;
import lu.kbra.standalone.gameengine.scene.Scene3D;
import lu.kbra.standalone.gameengine.utils.consts.Consts;

public class TestGameLogic extends GameLogic {

	private Scene3D worldScene = new Scene3D("world");
	private Scene2D uiScene;
	private CacheManager worldCache;
	private CacheManager uiCache;
	private SimpleCompositor simpleCompositor;
	private TextEmitter textEmitter;

	@Override
	public void init(GameEngine e) {
		simpleCompositor = new SimpleCompositor();

		SingleTexture txt1 = new SingleTexture("text", new File(Consts.BAKES_RES_DIR, "QuinqueFive.ttf"));
		TextShader shader = new TextShader();
		TextMaterial material = new TextMaterial(shader, txt1);
		textEmitter = new TextEmitter("text", material, 12, "hellow orld", new Vector2f(1));
		cache.addRenderShader(shader);
		cache.addMaterial(material);
		cache.addTextEmitter(textEmitter);

		worldScene.addEntity("entity text", new TextEmitterComponent(textEmitter), new Transform3DComponent());
	}

	@Override
	public void input(float dTime) {

	}

	@Override
	public void update(float dTime) {

	}

	@Override
	public void render(float dTime) {
		simpleCompositor.render(engine, worldScene, uiScene, worldCache, uiCache);
	}

}
