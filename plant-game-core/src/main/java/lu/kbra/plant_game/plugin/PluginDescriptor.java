package lu.kbra.plant_game.plugin;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class PluginDescriptor {

	public static class Dependencies {

		public static class VersionnedPluginDescriptor {

			protected final String internalName;
			protected final VersionMatcher version;

			public VersionnedPluginDescriptor(final String internalName, final VersionMatcher version) {
				this.internalName = internalName;
				this.version = version;
			}

			public String getInternalName() {
				return this.internalName;
			}

			public VersionMatcher getVersion() {
				return this.version;
			}

		}

		protected List<VersionnedPluginDescriptor> optional;
		protected List<VersionnedPluginDescriptor> required;

		public List<VersionnedPluginDescriptor> getOptional() {
			return this.optional;
		}

		public List<VersionnedPluginDescriptor> getRequired() {
			return this.required;
		}

	}

	protected String displayName;
	protected String internalName;
	protected String version;
	@JsonProperty("package")
	protected String package_;
	protected String mainClass;
	@JsonIgnore
	protected Class<? extends PluginMain> pluginClass;
	protected List<String> registries;
	protected Dependencies dependencies;

	public String getDisplayName() {
		return this.displayName;
	}

	public String getInternalName() {
		return this.internalName;
	}

	public String getVersion() {
		return this.version;
	}

	public String getPackage() {
		return this.package_;
	}

	public String getMainClass() {
		return this.mainClass;
	}

	public Class<? extends PluginMain> getPluginClass() {
		return this.pluginClass;
	}

	public void setPluginClass(final Class<? extends PluginMain> pluginClass) {
		this.pluginClass = pluginClass;
	}

	public List<String> getRegistries() {
		return this.registries;
	}

	public Dependencies getDependencies() {
		return this.dependencies;
	}

	@Override
	public String toString() {
		return this.displayName + " (" + this.internalName + ":" + this.version + ")";
	}

	public String relativePath(final String buildingReg) {
		return buildingReg == null || buildingReg.isBlank() ? null : this.package_ + "." + buildingReg;
	}

}