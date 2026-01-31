package lu.kbra.plant_game;

import java.util.List;

public class PluginDescriptor {

	public static class Registries {

		private String building;

		public String getBuilding() {
			return this.building;
		}

	}

	public static class Dependencies {

		public static class VersionnedPluginDescriptor {

			private final String internalName;
			private final VersionMatcher version;

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

		private List<VersionnedPluginDescriptor> optional;
		private List<VersionnedPluginDescriptor> required;

		public List<VersionnedPluginDescriptor> getOptional() {
			return this.optional;
		}

		public List<VersionnedPluginDescriptor> getRequired() {
			return this.required;
		}

	}

	private String displayName;
	private String internalName;
	private String version;
	private String mainClass;
	private Registries registries;
	private Dependencies dependencies;

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

}