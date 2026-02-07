
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

import lu.kbra.pclib.PCUtils;
import lu.kbra.pclib.logger.GlobalLogger;

import lu.kbra.plant_game.PGLogic;
import lu.kbra.standalone.gameengine.GameEngine;
import lu.kbra.standalone.gameengine.graph.window.WindowOptions;
import lu.kbra.standalone.gameengine.impl.GameLogic;

public class PGTestMain {

	public static void main(final String[] args) throws FileNotFoundException, IOException {
		final Properties props = new Properties();
		props.load(new StringReader(PCUtils.readStringSource("classpath:/config/main.properties")));

		GlobalLogger.INIT_DEFAULT_IF_NOT_INITIALIZED = false;
		GlobalLogger.init(PCUtils.readStringSource(props.getProperty("logs.config.file")));
		GlobalLogger.info("Removed " + PCUtils.deleteOldFiles(new File("./logs/"), 20) + " entries from the logs directory.");

//		new UIObjectRegistryGenMain().genRegistry();
//		new GameObjectRegistryGenMain().genRegistry();

		final GameLogic gameLogic = new PGLogic();

		final GameEngine engine = new GameEngine("plant-game", gameLogic, new WindowOptions(props, "windowOptions"));
		engine.start();
	}

}
