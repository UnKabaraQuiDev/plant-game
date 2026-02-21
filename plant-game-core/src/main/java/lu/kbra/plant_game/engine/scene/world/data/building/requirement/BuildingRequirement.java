package lu.kbra.plant_game.engine.scene.world.data.building.requirement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lu.kbra.plant_game.engine.data.locale.Localizable;
import lu.kbra.plant_game.engine.scene.world.WorldLevelScene;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type", visible = true)
@JsonSubTypes(
	{
			@JsonSubTypes.Type(value = MinBuildUnlockRequirement.class, name = "MIN_BUILD"),
			@JsonSubTypes.Type(value = MinResourceUnlockRequirement.class, name = "MIN_RESOURCE") }
)
@JsonIgnoreProperties({ "type" })
public interface BuildingRequirement extends Localizable {

	boolean isFulfilled(WorldLevelScene scene);

}
