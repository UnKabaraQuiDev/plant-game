package lu.kbra.plant_game.engine.render.shader.compute.filter;

import org.joml.Vector2f;
import org.joml.Vector3f;

import lu.kbra.plant_game.engine.render.FilterShaderConfiguration;
import lu.kbra.standalone.gameengine.graph.shader.part.AbstractShaderPart;

public class VignetteShader extends FilterShader<VignetteShader> {

	public static class VignetteShaderConfiguration extends FilterShaderConfiguration<VignetteShader> {

		protected Vector3f color = new Vector3f(1, 0, 0);
		protected float radius = 1.4f;
		protected float softness = 0.8f;
		protected float strength = 1f;
		protected Vector2f center = new Vector2f(0);
		protected boolean followAspectRatio = false;
		protected boolean edgeStyle = true;
		protected boolean posterize = true;
		protected int posterizeLevels = 8;

		@Override
		public Class<VignetteShader> getShaderClass() {
			return VignetteShader.class;
		}

		@Override
		public void apply(final VignetteShader filterShader) {
			filterShader.setUniform(VignetteShader.VIGNETTE_COLOR, this.color);
			filterShader.setUniform(VignetteShader.VIGNETTE_RADIUS, this.radius);
			filterShader.setUniform(VignetteShader.VIGNETTE_SOFTNESS, this.softness);
			filterShader.setUniform(VignetteShader.VIGNETTE_STRENGTH, this.strength);
			filterShader.setUniform(VignetteShader.FOLLOW_ASPECT_RATIO, this.followAspectRatio);

			filterShader.setUniform(VignetteShader.EDGE_STYLE, this.edgeStyle);

			filterShader.setUniform(VignetteShader.POSTERIZE, this.posterize);
			filterShader.setUniform(VignetteShader.POSTERIZE_LEVELS, this.posterizeLevels);
		}

		public void setColor(final Vector3f color) {
			this.color = color;
		}

		public void setRadius(final float radius) {
			this.radius = radius;
		}

		public void setSoftness(final float softness) {
			this.softness = softness;
		}

		public void setStrength(final float strength) {
			this.strength = strength;
		}

		public void setCenter(final Vector2f center) {
			this.center = center;
		}

		public void setFollowAspectRatio(final boolean followAspectRatio) {
			this.followAspectRatio = followAspectRatio;
		}

		public void setEdgeStyle(final boolean edgeStyle) {
			this.edgeStyle = edgeStyle;
		}

		public void setPosterize(final boolean posterize) {
			this.posterize = posterize;
		}

		public void setPosterizeLevels(final int posterizeLevels) {
			this.posterizeLevels = posterizeLevels;
		}

		@Override
		public String toString() {
			return "VignetteShaderConfiguration@" + System.identityHashCode(this) + " [color=" + this.color + ", radius=" + this.radius
					+ ", softness=" + this.softness + ", strength=" + this.strength + ", center=" + this.center + ", followAspectRatio="
					+ this.followAspectRatio + ", edgeStyle=" + this.edgeStyle + ", posterize=" + this.posterize + ", posterizeLevels="
					+ this.posterizeLevels + "]";
		}

	}

	public static final String VIGNETTE_COLOR = "vignetteColor";
	public static final String VIGNETTE_RADIUS = "vignetteRadius";
	public static final String VIGNETTE_SOFTNESS = "vignetteSoftness";
	public static final String VIGNETTE_STRENGTH = "vignetteStrength";
	public static final String VIGNETTE_CENTER = "vignetteCenter";
	public static final String FOLLOW_ASPECT_RATIO = "followAspectRatio";

	public static final String EDGE_STYLE = "edgeStyle";

	public static final String POSTERIZE = "posterize";
	public static final String POSTERIZE_LEVELS = "posterizeLevels";

	public VignetteShader() {
		super(AbstractShaderPart.load("classpath:/shaders/filter.vert"),
				AbstractShaderPart.load("classpath:/shaders/filter_vignette.frag"));
	}

	@Override
	public void createUniforms() {
		super.createUniforms();

		super.createUniform(VIGNETTE_COLOR);
		super.createUniform(VIGNETTE_RADIUS);
		super.createUniform(VIGNETTE_SOFTNESS);
		super.createUniform(VIGNETTE_STRENGTH);
		super.createUniform(VIGNETTE_CENTER);
		super.createUniform(FOLLOW_ASPECT_RATIO);
		super.createUniform(EDGE_STYLE);
		super.createUniform(POSTERIZE);
		super.createUniform(POSTERIZE_LEVELS);
	}

	@Override
	public VignetteShaderConfiguration newConfigurationInstance() {
		return new VignetteShaderConfiguration();
	}

}
