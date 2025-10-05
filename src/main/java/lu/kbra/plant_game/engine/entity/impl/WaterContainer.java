package lu.kbra.plant_game.engine.entity.impl;

public interface WaterContainer {

	long getWater();

	boolean hasWater(long val);

	void addWater(long val);

	void removeWater(long val);

}
