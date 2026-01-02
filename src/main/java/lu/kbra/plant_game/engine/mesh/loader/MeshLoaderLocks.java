package lu.kbra.plant_game.engine.mesh.loader;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import lu.pcy113.pclib.PCUtils;
import lu.pcy113.pclib.logger.GlobalLogger;
import lu.pcy113.pclib.pointer.prim.BooleanPointer;

import lu.kbra.plant_game.PGLogic;
import lu.kbra.standalone.gameengine.impl.future.YieldExecutionThrowable;

public final class MeshLoaderLocks {

	protected static final class ObjectLock extends BooleanPointer {

		Thread owner;

		public ObjectLock(final Thread owner) {
			super(false);
			this.owner = owner;
		}

		@Override
		public String toString() {
			return "ObjectLock@" + System.identityHashCode(this) + " [owner=" + this.owner + ", get()=" + this.get() + "]";
		}

	}

	private static final Map<String, ObjectLock> locks = new ConcurrentHashMap<>();
	private static final long LOAD_WAIT_TIMEOUT = 5;
	private static final int RETRIES = 3;

	public static boolean waitOrCreateLock(final String meshName) throws YieldExecutionThrowable {
		final String source = PCUtils.getCallerClassName(true);
		final BooleanPointer bp = new BooleanPointer(false);

		final ObjectLock lock = locks.computeIfAbsent(meshName, k -> {
			bp.set(true);
			return new ObjectLock(Thread.currentThread());
		});

		if (/* Thread.currentThread() == lock.owner */ bp.getValue()) {
			GlobalLogger.log(Level.FINEST,
					"Thread: " + Thread.currentThread().getName() + " created lock on: " + meshName + " (" + source + ") ");
			return true;
		}

		if (Thread.currentThread() == PGLogic.INSTANCE.getGameEngine().getRenderThread()) {
			throw new IllegalStateException("Render thread can't create nor wait on locks (" + source + ")");
		}

//		if (Thread.currentThread() == lock.owner) {
//			throw new IllegalStateException(
//					"Lock: " + meshName + " is being held by current thread: " + Thread.currentThread().getName() + " (" + lock + ")");
//		}

		if (lock.getValue()) {
			GlobalLogger.log(Level.FINEST,
					"Thread: " + Thread.currentThread().getName() + " skipped lock: " + meshName + " (" + source + ") " + lock);
			return true;
		}

		GlobalLogger.log(Level.FINEST,
				"Thread: " + Thread.currentThread().getName() + " waiting on: " + meshName + " (" + source + ") " + lock);

//		if (lock.waitForTrue(100)) {
//			return;
//		}

//		throw new YieldExecutionThrowable(lock);
		return false;

//		if (!lock.waitForTrue(LOAD_WAIT_TIMEOUT)) {
//			if (Thread.currentThread().isInterrupted()) {
//				Thread.interrupted();
//				throw new RuntimeException("Thread: " + Thread.currentThread().getName() + " interrupted while waiting on: " + meshName
//						+ " (" + source + "), lock held by: " + lock.owner);
//			}
//
//			throw new YieldExecutionThrowable();
//
////			throw new IllegalStateException(
////					"Thread: " + Thread.currentThread() + " exceeded timeout when waiting for " + meshName + " (" + lock + ")");
//		}

//		if (Thread.interrupted()) {
//			final int lockId = System.identityHashCode(lock);
//			final String threadName = Arrays.stream(ManagementFactory.getThreadMXBean().dumpAllThreads(true, true))
//					.filter(info -> (info.getLockedMonitors().length > 0 || info.getLockInfo() != null)
//							&& Arrays.stream(info.getLockedMonitors()).anyMatch(monitor -> monitor.getIdentityHashCode() == lockId))
//					.findFirst()
//					.map(ThreadInfo::getThreadName)
//					.orElse(null);
//			throw new RuntimeException("Thread: " + Thread.currentThread().getName() + " interrupted while waiting on: " + meshName + " ("
//					+ source + "), lock held by: " + lock.owner);
//		}
	}

	public static void releaseLock(final String meshName) {
		final String source = PCUtils.getCallerClassName(true);

		final ObjectLock lock = locks.remove(meshName);
		if (lock != null) {
			lock.setValue(true);
			GlobalLogger.log(Level.FINEST,
					"Thread " + Thread.currentThread().getName() + " released lock: " + meshName + " (" + source + ") " + lock);
		} else {
			GlobalLogger.severe("Lock wasn't held for: " + meshName + " (" + source + ")");
		}
	}

}
