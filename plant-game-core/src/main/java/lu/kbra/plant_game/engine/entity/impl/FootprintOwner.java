package lu.kbra.plant_game.engine.entity.impl;

import lu.kbra.plant_game.engine.entity.go.data.Footprint;
import lu.kbra.standalone.gameengine.utils.consts.Direction;

public interface FootprintOwner {

	Footprint getFootprint();

	Direction getRotation();

	// void setRotation(Direction dir);

}
