package lu.kbra.plant_game;
import java.io.File;

import org.joml.Vector2f;
import org.joml.Vector4f;

import lu.kbra.standalone.gameengine.GameEngine;
import lu.kbra.standalone.gameengine.graph.composition.Compositor;
import lu.kbra.standalone.gameengine.graph.composition.SceneRenderLayer;
import lu.kbra.standalone.gameengine.graph.material.text.TextShader;
import lu.kbra.standalone.gameengine.graph.material.text.TextShader.TextMaterial;
import lu.kbra.standalone.gameengine.graph.render.Scene3DRenderer;
import lu.kbra.standalone.gameengine.graph.texture.SingleTexture;
import lu.kbra.standalone.gameengine.impl.GameLogic;
import lu.kbra.standalone.gameengine.objs.entity.components.TextEmitterComponent;
import lu.kbra.standalone.gameengine.objs.entity.components.Transform3DComponent;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;
import lu.kbra.standalone.gameengine.scene.Scene3D;
import lu.kbra.standalone.gameengine.utils.consts.Consts;

public class TestGameLogic extends GameLogic {

	private Scene3D scene;
	private SceneRenderLayer renderLayer;
	private Compositor compositor;
	private TextEmitter textEmitter;

	@Override
	public void init(GameEngine e) {
		scene = new Scene3D("scene3d");
		cache.addScene(scene);

		renderLayer = new SceneRenderLayer("scene3d_render", scene, () -> cache);
		cache.addRenderLayer(renderLayer);
		
		cache.addRenderer(new Scene3DRenderer());

		compositor = new Compositor();
		compositor.addRenderLayer(0, renderLayer);

		SingleTexture txt1 = new SingleTexture("text", new File(Consts.BAKES_RES_DIR, "QuinqueFive.ttf"));
		TextShader shader = new TextShader();
		TextMaterial material = new TextMaterial(shader, txt1);
		textEmitter = new TextEmitter("text", material, 12, "hellow orld", new Vector2f(1));
		cache.addRenderShader(shader);
		cache.addMaterial(material);
		cache.addTextEmitter(textEmitter);

		scene.addEntity("entity text", new TextEmitterComponent(textEmitter), new Transform3DComponent());
	}

	@Override
	public void input(float dTime) {

	}

	@Override
	public void update(float dTime) {

	}

	@Override
	public void render(float dTime) {
		compositor.setBackground(new Vector4f(1, 1, 0, 1));
		compositor.render(cache, engine);
	}

}
