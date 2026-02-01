package lu.kbra.plant_game.plugin;

import java.util.List;

import lu.kbra.plant_game.VersionMatcher;

public final class PluginDescriptor {

	public static class Registries {

		protected String building;
		protected String resource;

		public String getBuilding() {
			return this.building;
		}

		public String getResource() {
			return this.resource;
		}

	}

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
	protected String mainClass;
	protected Registries registries;
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

	public String getMainClass() {
		return this.mainClass;
	}

	public Registries getRegistries() {
		return this.registries;
	}

	public Dependencies getDependencies() {
		return this.dependencies;
	}

	@Override
	public String toString() {
		return this.displayName + " (" + this.internalName + ":" + this.version + ")";
	}

}