package lu.kbra.plant_game.engine.entity.impl;

import lu.kbra.plant_game.engine.mesh.AnimatedMesh;

public interface AnimatedMeshOwner extends AnimatedTransformOwner {

	AnimatedMesh getAnimatedMesh();

	void setAnimatedMesh(AnimatedMesh m);

}
