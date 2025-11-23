package lu.kbra.plant_game.engine.mesh.loader;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;

import lu.pcy113.pclib.PCUtils;
import lu.pcy113.pclib.logger.GlobalLogger;

public final class MeshLoaderLocks {

	private static final Map<String, Object> locks = new ConcurrentHashMap<>();
	private static final long LOAD_WAIT_TIMEOUT = 1000;

	public static void waitOrCreateLock(final String meshName) throws InterruptedException {
		final String source = PCUtils.getCallerClassName(true);
		final AtomicBoolean creator = new AtomicBoolean(false);

		final Object lock = locks.computeIfAbsent(meshName, k -> {
			creator.set(true);
			return new Object();
		});

		if (creator.get()) {
			GlobalLogger
					.log(Level.FINEST,
							"Thread: " + Thread.currentThread().getName() + " created lock on: " + meshName + " (" + source + ") "
									+ PCUtils.toSimpleIdentityString(lock));
			return;
		}

		synchronized (lock) {
			GlobalLogger
					.log(Level.FINEST,
							"Thread " + Thread.currentThread().getName() + " waiting on: " + meshName + " (" + source + ") "
									+ PCUtils.toSimpleIdentityString(lock));
			lock.wait(LOAD_WAIT_TIMEOUT);
		}
	}

	public static void releaseLock(final String meshName) {
		final String source = PCUtils.getCallerClassName(true);

		final Object lock = locks.remove(meshName);
		if (lock != null) {
			synchronized (lock) {
				lock.notifyAll();
			}
			GlobalLogger
					.log(Level.FINEST,
							"Thread " + Thread.currentThread().getName() + " released lock: " + meshName + " (" + source + ") "
									+ PCUtils.toSimpleIdentityString(lock));
		} else {
			// GlobalLogger.severe("Lock wasn't held for: " + meshName + " (" + source + ")");
		}
	}

}
