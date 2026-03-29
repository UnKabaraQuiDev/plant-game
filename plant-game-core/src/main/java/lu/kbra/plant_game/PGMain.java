package lu.kbra.plant_game;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import com.codedisaster.steamworks.SteamApps;
import com.codedisaster.steamworks.SteamException;
import com.codedisaster.steamworks.SteamSupport;

import lu.kbra.pclib.PCUtils;
import lu.kbra.pclib.logger.GlobalLogger;
import lu.kbra.standalone.gameengine.GameEngine;
import lu.kbra.standalone.gameengine.graph.window.WindowOptions;
import lu.kbra.standalone.gameengine.impl.GameLogic;

public class PGMain {

	public static final String SKIP_STEAM_PROPERTY = PGMain.class.getSimpleName() + ".skip_steam";
	public static final boolean SKIP_STEAM = Boolean.getBoolean(SKIP_STEAM_PROPERTY);

	public static File APP_DIR;
	public static File APP_DATA_DIR;
	public static File CONFIG_DIR;

	public static void main(final String[] args) throws IOException, SteamException {
		GlobalLogger.INIT_DEFAULT_IF_NOT_INITIALIZED = false;

		final Properties props = new Properties();
		props.load(new StringReader(PCUtils.readStringSource("classpath:/config/main.properties")));

		SteamSupport.init(SKIP_STEAM, (String) props.get("steam.appId"));

		if (SteamSupport.STEAM_LAUCHED) {
			System.err.println(SteamSupport.get(SteamApps.class).getCurrentGameLanguage());
		}

		APP_DATA_DIR = new File(APP_DIR, "data");
		Files.createDirectories(Paths.get(APP_DATA_DIR.getPath()));
		CONFIG_DIR = new File(APP_DATA_DIR, "config");
		Files.createDirectories(Paths.get(CONFIG_DIR.getPath()));

		GlobalLogger.init(PCUtils.readStringSource(props.getProperty("logs.config.file")).replace("%APP_DIR%", APP_DIR.getPath()));

		GlobalLogger.info("App dir: " + APP_DIR.getAbsolutePath());
		GlobalLogger.info("Started with steam: " + SteamSupport.STEAM_LAUCHED + " (Force disabled: " + SKIP_STEAM + ")");
		GlobalLogger.info("Removed " + PCUtils.deleteOldFiles(GlobalLogger.getLogger().getLogFile().getParentFile(), 20)
				+ " entries from the logs directory.");

		final GameLogic gameLogic = new PGLogic();

		final GameEngine engine = new GameEngine("plant-game", gameLogic, new WindowOptions(props, "windowOptions"));
		engine.start();

		SteamSupport.dispose();
	}

}
