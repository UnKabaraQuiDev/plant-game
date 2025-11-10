package lu.kbra.plant_game.engine.render;

import org.joml.Vector2f;
import org.joml.Vector4f;

public interface GradientOwner {

	GradientDirection getDirection();

	void setDirection(GradientDirection dir);

	Vector2f getRange();

	void setRange(Vector2f range);

	Vector4f getStartColor();

	void setStartColor(Vector4f color);

	Vector4f getEndColor();

	void setEndColor(Vector4f color);

}
