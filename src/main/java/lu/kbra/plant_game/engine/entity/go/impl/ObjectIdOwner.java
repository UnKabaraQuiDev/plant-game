package lu.kbra.plant_game.engine.entity.go.impl;

import org.joml.Vector3ic;

import lu.kbra.plant_game.engine.mesh.data.AttributeLocation;

public interface ObjectIdOwner {

	AttributeLocation getObjectIdLocation();

	Vector3ic getObjectId();

}
