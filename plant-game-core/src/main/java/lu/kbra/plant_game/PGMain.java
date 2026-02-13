package lu.kbra.plant_game;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

import com.codedisaster.steamworks.SteamAPI;
import com.codedisaster.steamworks.SteamException;

import lu.kbra.pclib.PCUtils;
import lu.kbra.pclib.logger.GlobalLogger;
import lu.kbra.standalone.gameengine.GameEngine;
import lu.kbra.standalone.gameengine.graph.window.WindowOptions;
import lu.kbra.standalone.gameengine.impl.GameLogic;

public class PGMain {

	public static final String SKIP_STEAM_PROPERTY = PGMain.class.getSimpleName() + ".skip_steam";
	public static final boolean SKIP_STEAM = Boolean.getBoolean(SKIP_STEAM_PROPERTY);

	public static boolean STEAM_LAUCHED = false;
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

	public static void main(final String[] args) throws IOException, SteamException {
		GlobalLogger.INIT_DEFAULT_IF_NOT_INITIALIZED = false;

		final Properties props = new Properties();
		props.load(new StringReader(PCUtils.readStringSource("classpath:/config/main.properties")));

		if (!SKIP_STEAM) {
			SteamAPI.loadLibraries();

			if (SteamAPI.isSteamRunning()) {
				if (SteamAPI.restartAppIfNecessary(Integer.parseInt((String) props.get("steam.appId")))) {
//				STEAM_LAUCHED = false;
//				APP_DIR = getAppDataDir("satisplantory");
					System.err.println("Not started in steam app, exitting.");
					return;
				}
				STEAM_LAUCHED = true;
				APP_DIR = new File("./data/");
				if (!SteamAPI.init()) {
					throw new IllegalStateException("Failed to initialize Steam API.");
				}
				System.err.println("Started with steam support.");
			} else {
				STEAM_LAUCHED = false;
				APP_DIR = getAppDataDir("satisplantory");
				System.err.println("Started without steam support.");
			}
		}

		GlobalLogger.init(PCUtils.readStringSource(props.getProperty("logs.config.file")).replace("%APP_DIR%", APP_DIR.getPath()));

		GlobalLogger.info("App dir: " + APP_DIR.getAbsolutePath());
		GlobalLogger.info("Removed " + PCUtils.deleteOldFiles(GlobalLogger.getLogger().getLogFile().getParentFile(), 20)
				+ " entries from the logs directory.");

		final GameLogic gameLogic = new PGLogic();

		final GameEngine engine = new GameEngine("plant-game", gameLogic, new WindowOptions(props, "windowOptions"));
		engine.start();

		if (STEAM_LAUCHED) {
			SteamAPI.shutdown();
		}
	}

}
