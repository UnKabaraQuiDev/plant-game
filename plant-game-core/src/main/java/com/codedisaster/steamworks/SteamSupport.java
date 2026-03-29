package com.codedisaster.steamworks;

import static lu.kbra.plant_game.PGMain.APP_DIR;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public final class SteamSupport {

	public static boolean STEAM_LAUCHED = false;
	public static Map<Class<? extends SteamInterface>, SteamInterface> STEAM_INTERFACES = new HashMap<>();

	public static File getAppDataDir(final String appName) {
		final String os = System.getProperty("os.name").toLowerCase();

		if (os.contains("win")) {
			String base = System.getenv("APPDATA");

			if (base == null || base.isBlank()) {
				base = Paths.get(System.getProperty("user.home"), "AppData", "Roaming").toString();
			}

			return Paths.get(base, appName).toFile();
		}
		if (os.contains("mac")) {
			return Paths.get(System.getProperty("user.home"), "Library", "Application Support", appName).toFile();
		}
		return Paths.get(System.getProperty("user.home"), "." + appName.toLowerCase()).toFile();
	}

	public static void init(final boolean skipSteam, final String appId) throws SteamException {
		if (skipSteam) {
			STEAM_LAUCHED = false;
			APP_DIR = getAppDataDir("satisplantory");
			System.err.println("Started without steam support (forced).");
		} else {
			final SteamLibraryLoader loader = new SteamLibraryLoaderLwjgl3();
			if (!SteamAPI.loadLibraries(loader)) {
				throw new IllegalStateException("Failed to load native libraries.");
			}

			if (SteamAPI.init() && SteamAPI.isSteamRunning()) {
				if (SteamAPI.restartAppIfNecessary(Integer.parseInt(appId))) {
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

		System.err.println(SteamJNI.getInstallDir(Integer.parseInt(appId)));
	}

	public static <T extends SteamInterface> T get(final Class<T> clazz) {
		return (T) STEAM_INTERFACES.computeIfAbsent(clazz, (k) -> {
			try {
				return (T) k.getDeclaredConstructor().newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
	}

	public static void dispose() {
		if (STEAM_LAUCHED) {
			STEAM_INTERFACES.values().forEach(e -> e.dispose());

			SteamAPI.shutdown();
		}
	}

}
