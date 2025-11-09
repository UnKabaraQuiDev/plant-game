package lu.kbra.plant_game.engine.entity.go.impl;

public interface EnergyContainer {

	long getMaxPower();
	
	void getCurrentPower();
	
	void removePower(long power);
	
	void addPower(long power);
	
}
