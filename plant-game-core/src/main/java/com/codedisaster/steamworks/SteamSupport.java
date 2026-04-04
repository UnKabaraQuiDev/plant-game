package com.codedisaster.steamworks;

import static lu.kbra.plant_game.PGMain.APP_DIR;
import static lu.kbra.plant_game.PGMain.ROOT_DIR;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.lwjgl.system.Platform;

public final class SteamSupport {

	public static boolean STEAM_LAUCHED = false;
	public static Map<Class<? extends SteamInterface>, SteamInterface> STEAM_INTERFACES = new HashMap<>();

	public static File getAppDataDir(final String appName) {
		final String userHome = System.getProperty("user.home");

		return switch (Platform.get()) {
		case WINDOWS -> {
			String base = System.getenv("APPDATA");

			if (base == null || base.isBlank()) {
				base = Paths.get(userHome, "AppData", "Roaming").toString();
			}

			yield Paths.get(base, appName).toFile();
		}

		case MACOSX -> Paths.get(userHome, "Library", "Application Support", appName).toFile();

		case LINUX -> {
			String xdg = System.getenv("XDG_DATA_HOME");
			if (xdg == null || xdg.isBlank()) {
				xdg = Paths.get(userHome, ".local", "share").toString();
			}
			yield Paths.get(xdg, appName).toFile();
		}

		default -> Paths.get(userHome, "." + appName.toLowerCase()).toFile();
		};
	}

	public static void init(final boolean skipSteam, final String appId) throws SteamException {
		if (skipSteam) {
			STEAM_LAUCHED = false;
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

				STEAM_LAUCHED = true;

				System.err.println("Started with steam support.");
			} else {
				STEAM_LAUCHED = false;
				System.err.println("Started without steam support.");
			}
		}

		final String os = System.getProperty("os.name").toLowerCase();
		if (ROOT_DIR == null) {
			if (os.contains("win")) {
				APP_DIR = new File(".").getAbsoluteFile();
			} else {
				if (System.getenv("STEAM_COMPAT_INSTALL_PATH") == null || System.getenv("STEAM_COMPAT_INSTALL_PATH").isBlank()) {
					APP_DIR = new File(".").getAbsoluteFile();
				} else {
					APP_DIR = new File(System.getenv("STEAM_COMPAT_INSTALL_PATH")).getAbsoluteFile();
				}
			}
		} else {
			APP_DIR = new File(ROOT_DIR).getAbsoluteFile();
		}
		System.err.println("OS: [" + os + "] App dir: " + APP_DIR.getPath());
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

	public static <T extends SteamInterface> T get(final Class<T> clazz, final Supplier<T> supp) {
		return (T) STEAM_INTERFACES.computeIfAbsent(clazz, (k) -> supp.get());
	}

	public static void dispose() {
		if (STEAM_LAUCHED) {
			STEAM_INTERFACES.values().forEach(e -> e.dispose());

			SteamAPI.shutdown();
		}
	}

	public static SteamUser user() {
		return SteamSupport.get(SteamUser.class);
	}

	public static SteamUserStats userStats() {
		return SteamSupport.get(SteamUserStats.class);
	}

	public static SteamUtils utils() {
		return SteamSupport.get(SteamUtils.class);
	}

	public static SteamApps apps() {
		return SteamSupport.get(SteamApps.class);
	}

	public static SteamController controller() {
		return SteamSupport.get(SteamController.class);
	}

	public static SteamFriends friends() {
		return SteamSupport.get(SteamFriends.class);
	}

}
