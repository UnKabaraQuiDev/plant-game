package lu.kbra.plant_game.engine.render.shader.compute.filter;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import org.joml.Vector2i;

import lu.kbra.plant_game.engine.render.filter.FilterShaderConfiguration;
import lu.kbra.plant_game.plugin.PluginDescriptor;
import lu.kbra.standalone.gameengine.graph.shader.ComputeShader;
import lu.kbra.standalone.gameengine.graph.shader.RenderShader;
import lu.kbra.standalone.gameengine.graph.shader.part.AbstractShaderPart;
import lu.kbra.standalone.gameengine.utils.gl.consts.DataType;
import lu.kbra.standalone.gameengine.utils.gl.consts.TexelFormat;
import lu.kbra.standalone.gameengine.utils.gl.consts.TexelInternalFormat;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextureFilter;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextureWrap;

public abstract class FilterShader<T extends FilterShader<T>> extends RenderShader implements PluginDescriptorOwner {

	public static final class TextureDependencyConfig {

		public Class<? extends FilterShader<?>> filterShaderSource;
		public String name;
		public String uniformName;

		public TextureDependencyConfig(
				final Class<? extends FilterShader<?>> filterShaderSource,
				final String name,
				final String uniformName) {
			this.filterShaderSource = filterShaderSource;
			this.name = name;
			this.uniformName = uniformName;
		}

		@Override
		public String toString() {
			return "TextureDependencyConfig@" + System.identityHashCode(this) + " [filterShaderSource=" + this.filterShaderSource
					+ ", name=" + this.name + ", uniformName=" + this.uniformName + "]";
		}

	}

	public static final class TextureOutputConfig {

		public String name;
		public OptionalInt loc;
		public String targetName;
		public TextureFilter filter = TextureFilter.NEAREST;
		public Optional<Vector2i> textureSize;
		public TextureWrap wrap;
		public DataType dataType;
		public TexelFormat texelFormat;
		public TexelInternalFormat texelInternalFormat;

		public TextureOutputConfig(
				final String name,
				final OptionalInt loc,
				final String targetName,
				final TextureFilter filter,
				final Optional<Vector2i> textureSize,
				final TextureWrap wrap,
				final DataType dataType,
				final TexelFormat texelFormat,
				final TexelInternalFormat texelInternalFormat) {
			this.name = name;
			this.loc = loc;
			this.targetName = targetName;
			this.filter = filter;
			this.textureSize = textureSize;
			this.wrap = wrap;
			this.dataType = dataType;
			this.texelFormat = texelFormat;
			this.texelInternalFormat = texelInternalFormat;
		}

		@Override
		public String toString() {
			return "TextureOutputConfig@" + System.identityHashCode(this) + " [name=" + this.name + ", loc=" + this.loc + ", filter="
					+ this.filter + ", textureSize=" + this.textureSize + ", wrap=" + this.wrap + ", dataType=" + this.dataType
					+ ", texelFormat=" + this.texelFormat + ", texelInternalFormat=" + this.texelInternalFormat + "]";
		}

	}

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

	@Deprecated
	public List<TextureDependencyConfig> getTextureDependencies() {
		return Collections.emptyList();
	}

	@Deprecated
	public List<TextureOutputConfig> getTextureOutputs() {
		return Collections.emptyList();
	}

	@Deprecated
	public boolean blitOutputTexture(final FilterShaderConfiguration<T> fsc) {
		return false;
	}

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
