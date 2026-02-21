package lu.kbra.plant_game.engine.scene.world.data.building.requirement;

import java.text.MessageFormat;

import com.fasterxml.jackson.annotation.JsonTypeName;

import lu.kbra.plant_game.engine.scene.world.SunLightOwner;
import lu.kbra.plant_game.plugin.registry.ResourceRegistry;

@JsonTypeName("MIN_RESOURCE")
public class MinResourceUnlockRequirement implements BuildingRequirement {

	private int count;
	private String resource;

	public MinResourceUnlockRequirement() {
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
	public boolean isFulfilled(final SunLightOwner scene) {
		return false;
	}

	@Override
	public String getLocalizationKey() {
		return "req.unlock.min.resource";
	}

	@Override
	public String getLocalizationValue() {
		return MessageFormat.format(BuildingRequirement.super.getLocalizationValue(),
				this.count,
				ResourceRegistry.RESOURCE_TYPE_DEFS.get(this.resource).getLocalizationValue());
	}

}
