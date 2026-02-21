package lu.kbra.plant_game.engine.scene.world.data.building.requirement;

import com.fasterxml.jackson.annotation.JsonTypeName;

import lu.kbra.plant_game.engine.data.locale.AbstractLocalizationString;
import lu.kbra.plant_game.engine.data.locale.Localizable;
import lu.kbra.plant_game.engine.scene.world.WorldLevelScene;
import lu.kbra.plant_game.engine.scene.world.data.resource.ResourceType;
import lu.kbra.plant_game.plugin.registry.ResourceRegistry;

@JsonTypeName("MIN_RESOURCE")
public class MinResourceUnlockRequirement extends AbstractLocalizationString implements BuildingRequirement {

	private int count;
	private String resource;

	public MinResourceUnlockRequirement() {
	}

	@Deprecated
	public MinResourceUnlockRequirement(final int count, final String resource) {
		this.count = count;
		this.resource = resource;
		this.addParam(Localizable.raw(count));
		this.addParam(Localizable.of(resource));
	}

	public MinResourceUnlockRequirement(final int count, final ResourceType resource) {
		this.count = count;
		this.resource = ResourceRegistry.getInternalName(resource);
		this.addParam(Localizable.raw(count));
		this.addParam(ResourceType.getLocalizable(resource));
	}

	public int getCount() {
		return this.count;
	}

	public String getResource() {
		return this.resource;
	}

	public void setCount(final int count) {
		this.count = count;
	}

	public void setResource(final String resource) {
		this.resource = resource;
	}

	@Override
	public boolean isFulfilled(final WorldLevelScene scene) {
		return false;
	}

	@Override
	public String getLocalizationKey() {
		return "req.unlock.min.resource";
	}

	@Override
	public String toString() {
		return "MinResourceUnlockRequirement@" + System.identityHashCode(this) + " [count=" + this.count + ", resource=" + this.resource
				+ ", params=" + this.params + ", computed=" + this.computed + "]";
	}

}
