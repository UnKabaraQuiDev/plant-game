package lu.kbra.plant_game.engine.render.shader.compute.filter;

import lu.kbra.plant_game.engine.render.FilterShaderConfiguration;
import lu.kbra.plant_game.plugin.PluginDescriptor;
import lu.kbra.standalone.gameengine.graph.shader.ComputeShader;
import lu.kbra.standalone.gameengine.graph.shader.RenderShader;
import lu.kbra.standalone.gameengine.graph.shader.part.AbstractShaderPart;

public abstract class FilterShader<T extends FilterShader<T>> extends RenderShader implements PluginDescriptorOwner {

	public static final String INPUT_SIZE = ComputeShader.INPUT_SIZE;
	public static final String OUTPUT_SIZE = ComputeShader.OUTPUT_SIZE;
	public static final String TXT0 = "txt0";

	protected PluginDescriptor pluginDescriptor;

	public FilterShader(final AbstractShaderPart... parts) {
		super(parts);
	}

	public FilterShader(final boolean transparent, final AbstractShaderPart... parts) {
		super(transparent, parts);
	}

	public FilterShader(final String name, final AbstractShaderPart... parts) {
		super(name, parts);
	}

	public FilterShader(final String name, final boolean transparent, final AbstractShaderPart... parts) {
		super(name, transparent, parts);
	}

//	public abstract Class<FilterShaderConfiguration<T>> getConfigurationClass();

	public abstract FilterShaderConfiguration<T> newConfigurationInstance();

	@Override
	public void createUniforms() {
		super.createSceneUniforms();

		this.createUniform(TXT0);
		super.createUniform(INPUT_SIZE);
		super.createUniform(OUTPUT_SIZE);
	}

	@Override
	public PluginDescriptor getPluginDescriptor() {
		return this.pluginDescriptor;
	}

	@Override
	public void setPluginDescriptor(final PluginDescriptor pluginDescriptor) {
		this.pluginDescriptor = pluginDescriptor;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "<FilterShader@" + System.identityHashCode(this) + " [pluginDescriptor="
				+ this.pluginDescriptor + ", transparent=" + this.transparent + ", beginMode=" + this.beginMode + ", faceMode="
				+ this.faceMode + ", name=" + this.name + ", spid=" + this.spid + ", parts=" + this.parts + ", uniforms=" + this.uniforms
				+ ", knownNotExistsUniforms=" + this.knownNotExistsUniforms + ", cleanable=" + this.cleanable + "]";
	}

}
