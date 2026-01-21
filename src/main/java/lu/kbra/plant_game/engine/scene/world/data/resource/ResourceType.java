package lu.kbra.plant_game.engine.scene.world.data.resource;

import lu.kbra.plant_game.engine.entity.ui.TexturedQuadMeshUIObject;

public interface ResourceType {

	String getName();

	Class<? extends TexturedQuadMeshUIObject> getIconClass();

}
