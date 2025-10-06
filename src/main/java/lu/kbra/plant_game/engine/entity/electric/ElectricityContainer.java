package lu.kbra.plant_game.engine.entity.electric;

public interface ElectricityContainer {

	long getMaxPower();
	
	void getCurrentPower();
	
	void removePower(long power);
	
	void addPower(long power);
	
}
