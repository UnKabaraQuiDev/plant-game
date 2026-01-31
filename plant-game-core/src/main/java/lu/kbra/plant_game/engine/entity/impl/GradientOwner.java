package lu.kbra.plant_game.engine.entity.impl;

import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector4f;
import org.joml.Vector4fc;

import lu.kbra.plant_game.engine.entity.ui.data.GradientDirection;

public interface GradientOwner {

	GradientDirection getDirection();

	void setDirection(GradientDirection dir);

	Vector2f getRange();

	void setRange(Vector2fc range);

	Vector4f getStartColor();

	void setStartColor(Vector4fc color);

	Vector4f getEndColor();

	void setEndColor(Vector4fc color);

}
