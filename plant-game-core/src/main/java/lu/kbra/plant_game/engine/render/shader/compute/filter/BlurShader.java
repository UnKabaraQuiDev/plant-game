package lu.kbra.plant_game.engine.render.shader.compute.filter;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import lu.kbra.plant_game.engine.render.FilterShaderConfiguration;
import lu.kbra.standalone.gameengine.graph.shader.part.AbstractShaderPart;
import lu.kbra.standalone.gameengine.utils.gl.consts.DataType;
import lu.kbra.standalone.gameengine.utils.gl.consts.TexelFormat;
import lu.kbra.standalone.gameengine.utils.gl.consts.TexelInternalFormat;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextureFilter;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextureWrap;

public class BlurShader extends FilterShader<BlurShader> {

	public static class BlurShaderConfiguration extends FilterShaderConfiguration<BlurShader> {

		protected float threshold = 0;
		protected boolean horizontal = true;

		protected boolean shouldBlit = false;

		@Override
		public Class<BlurShader> getShaderClass() {
			return BlurShader.class;
		}

		@Override
		public void apply(final BlurShader filterShader) {
			filterShader.setUniform(BlurShader.THRESHOLD, this.threshold);
			filterShader.setUniform(BlurShader.HORIZONTAL, this.horizontal);
		}

		public void setThreshold(final float threshold) {
			this.threshold = threshold;
		}

		public void setHorizontal(final boolean horizontal) {
			this.horizontal = horizontal;
		}

		public void setShouldBlit(final boolean shouldBlit) {
			this.shouldBlit = shouldBlit;
		}

		@Override
		public String toString() {
			return "BlurShaderConfiguration@" + System.identityHashCode(this) + " [threshold=" + this.threshold + ", horizontal="
					+ this.horizontal + ", shouldBlit=" + this.shouldBlit + "]";
		}

	}

	public static final String THRESHOLD = "threshold";
	public static final String HORIZONTAL = "horizontal";

	public BlurShader() {
		super(AbstractShaderPart.load("classpath:/shaders/filter.vert"), AbstractShaderPart.load("classpath:/shaders/filter_blur.frag"));
	}

	@Override
	public void createUniforms() {
		super.createUniforms();
		this.createUniform(HORIZONTAL);
		this.createUniform(THRESHOLD);
	}

	@Override
	public BlurShaderConfiguration newConfigurationInstance() {
		return new BlurShaderConfiguration();
	}

	@Override
	public boolean blitOutputTexture(final FilterShaderConfiguration<BlurShader> fsc) {
		return fsc instanceof BlurShaderConfiguration bsc ? bsc.shouldBlit : false;
	}

	@Override
	public List<TextureOutputConfig> getTextureOutputs() {
		return List.of(new TextureOutputConfig(TXT0,
				OptionalInt.of(0),
				"out_FragColor",
				TextureFilter.NEAREST,
				Optional.empty(),
				TextureWrap.CLAMP_TO_EDGE,
				DataType.FLOAT,
				TexelFormat.RGBA,
				TexelInternalFormat.RGBA16F));
	}

}
