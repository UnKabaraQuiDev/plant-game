package lu.kbra.plant_game.engine.mesh.loader;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;

import lu.pcy113.pclib.PCUtils;
import lu.pcy113.pclib.logger.GlobalLogger;

public final class MeshLoaderLocks {

	private static final Map<String, Object> locks = new ConcurrentHashMap<>();
	private static final long LOAD_WAIT_TIMEOUT = 5000;

	public static void waitOrCreateLock(final String meshName) {
		final String source = PCUtils.getCallerClassName(true);
		final AtomicBoolean creator = new AtomicBoolean(false);

		final Object lock = locks.computeIfAbsent(meshName, k -> {
			creator.set(true);
			return new Object();
		});

		if (creator.get()) {
			GlobalLogger.log(Level.FINEST,
					"Thread: " + Thread.currentThread().getName() + " created lock on: " + meshName + " (" + source + ") "
							+ PCUtils.toSimpleIdentityString(lock));
			return;
		}
		try {
			synchronized (lock) {
				GlobalLogger.log(Level.FINEST,
						"Thread: " + Thread.currentThread().getName() + " waiting on: " + meshName + " (" + source + ") "
								+ PCUtils.toSimpleIdentityString(lock));

				lock.wait(LOAD_WAIT_TIMEOUT);
			}
		} catch (final InterruptedException e) {
			final int lockId = System.identityHashCode(lock);
			final String threadName = Arrays.stream(ManagementFactory.getThreadMXBean().dumpAllThreads(true, true))
					.filter(info -> (info.getLockedMonitors().length > 0 || info.getLockInfo() != null)
							&& Arrays.stream(info.getLockedMonitors()).anyMatch(monitor -> monitor.getIdentityHashCode() == lockId))
					.findFirst()
					.map(ThreadInfo::getThreadName)
					.get();
			throw new RuntimeException(
					"Thread: " + Thread.currentThread().getName() + " interrupted while waiting on: " + meshName + " (" + source
							+ "), lock held by: " + threadName,
					e);
		}
	}

	public static void releaseLock(final String meshName) {
		final String source = PCUtils.getCallerClassName(true);

		final Object lock = locks.remove(meshName);
		if (lock != null) {
			synchronized (lock) {
				lock.notifyAll();
			}
			GlobalLogger.log(Level.FINEST,
					"Thread " + Thread.currentThread().getName() + " released lock: " + meshName + " (" + source + ") "
							+ PCUtils.toSimpleIdentityString(lock));
		} else {
			// GlobalLogger.severe("Lock wasn't held for: " + meshName + " (" + source + ")");
		}
	}

}
