package lu.kbra.plant_game.engine.loader;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;

import lu.kbra.pclib.PCUtils;
import lu.kbra.pclib.logger.GlobalLogger;

public final class TextLoaderLocks {

	private static final Map<String, Object> locks = new ConcurrentHashMap<>();
	private static final long LOAD_WAIT_TIMEOUT = 1000;

	public static void waitOrCreateLock(String meshName) throws InterruptedException {
		final String source = PCUtils.getCallerClassName(true);
		final AtomicBoolean creator = new AtomicBoolean(false);

		final Object lock = locks.computeIfAbsent(meshName, k -> {
			creator.set(true);
			GlobalLogger.log(Level.FINEST,
					"Thread: " + Thread.currentThread().getName() + " created lock on: " + meshName + " (" + source + ")");
			return new Object();
		});

		if (creator.get()) {
			return;
		}

		synchronized (lock) {
			int iter = 0;
			while (locks.containsKey(meshName) && locks.get(meshName) == lock && iter++ < 5) {
				GlobalLogger.log(Level.FINEST,
						"Thread " + Thread.currentThread().getName() + " waiting on: " + meshName + " (" + source + ")");
				lock.wait(LOAD_WAIT_TIMEOUT);
			}
			if (iter > 5) {
				throw new IllegalStateException("Timeout waiting for mesh: " + meshName);
			}
		}
	}

	public static void releaseLock(String meshName) {
		final String source = PCUtils.getCallerClassName(true);

		final Object lock = locks.remove(meshName);
		if (lock != null) {
			synchronized (lock) {
				lock.notifyAll();
			}
			GlobalLogger.log(Level.FINEST,
					"Thread " + Thread.currentThread().getName() + " released lock: " + meshName + " (" + source + ")");
		} else {
			GlobalLogger.severe("Lock wasn't held for: " + meshName + " (" + source + ")");
		}
	}

}
