package lu.kbra.plant_game.engine.render.shader.compute.filter;

import lu.kbra.plant_game.engine.render.FilterShaderConfiguration;
import lu.kbra.standalone.gameengine.graph.shader.part.AbstractShaderPart;

public class BlurShader extends FilterShader<BlurShader> {

	public static class BlurShaderConfiguration extends FilterShaderConfiguration<BlurShader> {

		protected float threshold;
		protected boolean horizontal;

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

		@Override
		public String toString() {
			return "BlurShaderConfiguration@" + System.identityHashCode(this) + " [threshold=" + this.threshold + ", horizontal="
					+ this.horizontal + "]";
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

}
