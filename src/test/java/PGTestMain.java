
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

import lu.pcy113.pclib.PCUtils;

import lu.kbra.plant_game.GameObjectRegistryGenMain;
import lu.kbra.plant_game.PGLogic;
import lu.kbra.plant_game.UIObjectRegistryGenMain;
import lu.kbra.standalone.gameengine.GameEngine;
import lu.kbra.standalone.gameengine.graph.window.WindowOptions;
import lu.kbra.standalone.gameengine.impl.GameLogic;

public class PGTestMain {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		new UIObjectRegistryGenMain().genRegistry();
		new GameObjectRegistryGenMain().genRegistry();

		final GameLogic gameLogic = new PGLogic();

		final Properties props = new Properties();
		props.load(new StringReader(PCUtils.readStringSource("classpath:/config/main.properties")));

		final GameEngine engine = new GameEngine("plant-game", gameLogic, new WindowOptions(props, "windowOptions"));
		engine.start();
	}

}
