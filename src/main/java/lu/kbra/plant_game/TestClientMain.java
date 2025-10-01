package lu.kbra.plant_game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import lu.kbra.standalone.gameengine.GameEngine;
import lu.kbra.standalone.gameengine.graph.window.WindowOptions;
import lu.kbra.standalone.gameengine.impl.GameLogic;

public class TestClientMain {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		final GameLogic gameLogic = new TestGameLogic();
		final File propertyFile = new File("./config/main.properties");
		final Properties props = new Properties();
		props.load(new FileReader(propertyFile));

		final GameEngine engine = new GameEngine("test", gameLogic, new WindowOptions(props, "windowOptions"));
		engine.start();
	}

}
