package lu.kbra.plant_game;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import com.codedisaster.steamworks.SteamAPI;
import com.codedisaster.steamworks.SteamException;
import com.codedisaster.steamworks.SteamLibraryLoader;
import com.codedisaster.steamworks.SteamLibraryLoaderLwjgl3;

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
	public static File APP_DATA_DIR;
	public static File CONFIG_DIR;

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

		return new File(path);
	}

	public static void main(final String[] args) throws IOException, SteamException {
		GlobalLogger.INIT_DEFAULT_IF_NOT_INITIALIZED = false;

		final Properties props = new Properties();
		props.load(new StringReader(PCUtils.readStringSource("classpath:/config/main.properties")));

		if (SKIP_STEAM) {
			STEAM_LAUCHED = false;
			APP_DIR = getAppDataDir("satisplantory");
			System.err.println("Started without steam support (forced).");
		} else {
			final SteamLibraryLoader loader = new SteamLibraryLoaderLwjgl3();
			if (!SteamAPI.loadLibraries(loader)) {
				throw new IllegalStateException("Failed to load native libraries.");
			}

			if (SteamAPI.init() && SteamAPI.isSteamRunning()) {
				if (SteamAPI.restartAppIfNecessary(Integer.parseInt((String) props.get("steam.appId")))) {
					System.err.println("Not started in steam app, exitting.");
					return;
				}
				if (System.getenv("STEAM_COMPAT_INSTALL_PATH") == null || System.getenv("STEAM_COMPAT_INSTALL_PATH").isBlank()) {
					throw new IllegalArgumentException("Steam path not found in env.");
				}

				STEAM_LAUCHED = true;
				APP_DIR = new File(System.getenv("STEAM_COMPAT_INSTALL_PATH"));

				System.err.println("Started with steam support.");
			} else {
				STEAM_LAUCHED = false;
				APP_DIR = getAppDataDir("satisplantory");
				System.err.println("Started without steam support.");
			}
		}

		APP_DATA_DIR = new File(APP_DIR, "data");
		Files.createDirectories(Paths.get(APP_DATA_DIR.getPath()));
		CONFIG_DIR = new File(APP_DATA_DIR, "config");
		Files.createDirectories(Paths.get(CONFIG_DIR.getPath()));

		GlobalLogger.init(PCUtils.readStringSource(props.getProperty("logs.config.file")).replace("%APP_DIR%", APP_DIR.getPath()));

		GlobalLogger.info("App dir: " + APP_DIR.getAbsolutePath());
		GlobalLogger.info("Started with steam: " + STEAM_LAUCHED + " (Force disabled: " + SKIP_STEAM + ")");
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
