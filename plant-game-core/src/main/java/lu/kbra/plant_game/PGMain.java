package lu.kbra.plant_game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

import lu.kbra.pclib.PCUtils;
import lu.kbra.pclib.logger.GlobalLogger;
import lu.kbra.standalone.gameengine.GameEngine;
import lu.kbra.standalone.gameengine.graph.window.WindowOptions;
import lu.kbra.standalone.gameengine.impl.GameLogic;

public class PGMain {

	public static File APP_DIR;

	public static File getAppDataDir(final String appName) {
		final String os = System.getProperty("os.name").toLowerCase();
		String path;

		if (os.contains("win")) {
			// Windows: %APPDATA%
			path = System.getenv("APPDATA");
			if (path == null || path.isEmpty()) {
				// fallback
				path = System.getProperty("user.home") + "\\AppData\\Roaming";
			}
			path += "\\" + appName;
		} else if (os.contains("mac")) {
			// macOS: ~/Library/Application Support/
			path = System.getProperty("user.home") + "/Library/Application Support/" + appName;
		} else {
			// Linux / Unix: ~/.<appName>
			path = System.getProperty("user.home") + "/." + appName.toLowerCase();
		}

		final File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs(); // create if missing
		}

		return dir;
	}

	public static void main(final String[] args) throws FileNotFoundException, IOException {
		APP_DIR = getAppDataDir("satisplantory");

		final Properties props = new Properties();
		props.load(new StringReader(PCUtils.readStringSource("classpath:/config/main.properties")));

		GlobalLogger.INIT_DEFAULT_IF_NOT_INITIALIZED = false;
		GlobalLogger.init(PCUtils.readStringSource(props.getProperty("logs.config.file")));
		GlobalLogger.info("Removed " + PCUtils.deleteOldFiles(new File("./logs/"), 20) + " entries from the logs directory.");

		final GameLogic gameLogic = new PGLogic();

		final GameEngine engine = new GameEngine("plant-game", gameLogic, new WindowOptions(props, "windowOptions"));
		engine.start();
	}

}
