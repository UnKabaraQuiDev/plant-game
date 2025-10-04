package lu.kbra.plant_game.engine.render.shader;

import java.util.HashMap;

import org.joml.Vector2i;

import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.graph.material.TextureMaterial;
import lu.kbra.standalone.gameengine.graph.shader.ComputeShader;
import lu.kbra.standalone.gameengine.graph.shader.RenderShader;
import lu.kbra.standalone.gameengine.graph.shader.annotation.AssociatedShader;
import lu.kbra.standalone.gameengine.graph.shader.part.AbstractShaderPart;
import lu.kbra.standalone.gameengine.graph.texture.Texture;
import lu.kbra.standalone.gameengine.impl.Renderable;

public class BlitShader extends RenderShader {

	public static final String TXT0 = "txt0";

	public BlitShader() {
		this(null);
	}

	public BlitShader(String name) {
		super(name, AbstractShaderPart.load("classpath:/shaders/blit.frag"), AbstractShaderPart.load("classpath:/shaders/blit.vert"));
	}

	@Override
	public void createUniforms() {
		super.createUniform(ComputeShader.SCREEN_SIZE);

		super.createUniform(TXT0);
	}

	@AssociatedShader(BlitShader.class)
	public static class BlitMaterial extends TextureMaterial {

		private Vector2i screenSize;

		public BlitMaterial(BlitShader shader, Texture singleTexture) {
			super(null, shader, new HashMap<String, Texture>() {
				{
					put(TXT0, singleTexture);
				}
			});
		}

		public BlitMaterial(String name, BlitShader shader, Texture singleTexture) {
			super(null, shader, new HashMap<String, Texture>() {
				{
					put(TXT0, singleTexture);
				}
			});
		}

		@Override
		public void bindProperties(CacheManager cache, Renderable scene) {
			super.bindProperties(cache, scene);

			shader.setUniform(ComputeShader.SCREEN_SIZE, screenSize);
		}

		public void setScreenSize(Vector2i screenSize) {
			this.screenSize = screenSize;
		}

	}

}
