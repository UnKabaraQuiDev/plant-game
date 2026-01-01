package lu.kbra.plant_game.engine.entity.go.obj;

import lu.kbra.plant_game.engine.entity.go.impl.Footprint;
import lu.kbra.plant_game.engine.entity.impl.AnimatedMeshOwner;
import lu.kbra.plant_game.engine.entity.impl.FootprintOwner;

public interface AnimatedMeshFootprintOwner extends AnimatedMeshOwner, FootprintOwner {

	Footprint getAnimatedMeshFootprint();

}
