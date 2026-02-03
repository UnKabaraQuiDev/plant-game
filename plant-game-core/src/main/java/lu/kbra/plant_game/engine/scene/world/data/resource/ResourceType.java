package lu.kbra.plant_game.engine.scene.world.data.resource;

import lu.kbra.plant_game.engine.data.locale.Localizable;
import lu.kbra.plant_game.engine.entity.ui.TexturedQuadMeshUIObject;

public interface ResourceType extends Localizable {

	String LOCALIZATION_KEY = "resource.";

	String getName();

	Class<? extends TexturedQuadMeshUIObject> getIconClass();

}
