package lu.kbra.plant_game.engine.scene.world.data.resource;

import lu.kbra.plant_game.engine.data.locale.Localizable;
import lu.kbra.plant_game.engine.entity.ui.prim.TexturedQuadMeshUIObject;
import lu.kbra.plant_game.plugin.registry.ResourceRegistry;

public interface ResourceType extends Localizable {

	String LOCALIZATION_KEY = "resource.";

	String getName();

	Class<? extends TexturedQuadMeshUIObject> getIconClass();

	static Localizable getLocalizable(final ResourceType resource) {
		return Localizable.of(LOCALIZATION_KEY + "." + ResourceRegistry.getInternalName(resource).replace(":", "."));
	}

}
