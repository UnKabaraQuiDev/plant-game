package lu.kbra.plant_game.plugin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import lu.kbra.pclib.datastructure.tree.dependency.DependencyOwner;

public final class PluginDescriptor implements DependencyOwner<String> {

	public static class InternalDependencies {

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

		@JsonSetter(nulls = Nulls.AS_EMPTY)
		protected List<VersionnedPluginDescriptor> optional = new ArrayList<>();
		@JsonSetter(nulls = Nulls.AS_EMPTY)
		protected List<VersionnedPluginDescriptor> required = new ArrayList<>();

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
	@JsonProperty("dependencies")
	@JsonSetter(nulls = Nulls.AS_EMPTY)
	protected InternalDependencies internalDependencies = new InternalDependencies();
	@JsonIgnore
	protected boolean shared = false;
	@JsonIgnore
	protected String sourceJar;
	@JsonIgnore
	protected int loadId;

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

	void setPluginClass(final Class<? extends PluginMain> pluginClass) {
		this.pluginClass = pluginClass;
	}

	public List<String> getRegistries() {
		return this.registries;
	}

	public InternalDependencies getInternalDependencies() {
		return this.internalDependencies;
	}

	@Override
	public Set<String> getDependencies() {
		final Set<String> all = new HashSet<>();
		this.internalDependencies.optional.stream().map(InternalDependencies.VersionnedPluginDescriptor::getInternalName).forEach(all::add);
		this.internalDependencies.required.stream().map(InternalDependencies.VersionnedPluginDescriptor::getInternalName).forEach(all::add);
		return all;
	}

	@Override
	public String getKey() {
		return this.internalName;
	}

	public boolean isShared() {
		return this.shared;
	}

	void setShared(final boolean shared) {
		this.shared = shared;
	}

	public String getSourceJar() {
		return this.sourceJar;
	}

	void setSourceJar(final String sourceJar) {
		this.sourceJar = sourceJar;
	}

	public int getLoadId() {
		return this.loadId;
	}

	void setLoadId(final int loadId) {
		this.loadId = loadId;
	}

	@Override
	public String toString() {
		return this.displayName + " (" + this.internalName + ":" + this.version + ")";
	}

	public String relativeClassPath(final String className) {
		return className == null || className.isBlank() ? null : this.package_ + "." + className;
	}

}