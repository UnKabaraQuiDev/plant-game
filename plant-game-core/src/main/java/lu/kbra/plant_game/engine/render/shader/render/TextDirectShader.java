package lu.kbra.plant_game.engine.render.shader.render;

import lu.pcy113.pclib.PCUtils;

import lu.kbra.standalone.gameengine.graph.shader.part.AbstractShaderPart;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;

public class TextDirectShader extends DirectShader {

	public static final String FG_COLOR = "fgColor";
	public static final String BG_COLOR = "bgColor";
	public static final String TEXT_LENGTH = "len";
	public static final String TRANSPARENT = "transparent";

	public static final String FONT_TEXTURE_NAME = "_FONT";

	public TextDirectShader() {
		super(AbstractShaderPart.load("classpath:/shaders/gbuffer_inst_text.vert"),
				AbstractShaderPart
						.load("classpath:/shaders/text.frag",
								PCUtils
										.hashMap("%CHAR_START%",
												(int) TextEmitter.STRING.charAt(0),
												"%CHAR_COUNT%",
												TextEmitter.STRING.length())));
	}

	@Override
	public void createUniforms() {
		super.createUniforms();

		this.createUniform(FG_COLOR);
		this.createUniform(BG_COLOR);
		this.createUniform(TRANSPARENT);

		this.createUniform(TEXT_LENGTH);
	}

}
