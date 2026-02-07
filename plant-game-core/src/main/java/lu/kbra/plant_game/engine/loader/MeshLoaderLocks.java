package lu.kbra.plant_game.engine.loader;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import lu.kbra.pclib.PCUtils;
import lu.kbra.pclib.logger.GlobalLogger;
import lu.kbra.pclib.pointer.prim.BooleanPointer;
import lu.kbra.standalone.gameengine.impl.GameLogic;
import lu.kbra.standalone.gameengine.impl.future.YieldExecutionThrowable;

public final class MeshLoaderLocks {

	public static final String DEBUG_LOCKS_PROPERTY = MeshLoaderLocks.class.getSimpleName() + ".debug_locks";
	public static boolean DEBUG_LOCKS = Boolean.getBoolean(DEBUG_LOCKS_PROPERTY);

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

		if (bp.getValue()) {
			log(Level.FINEST, "Thread: " + Thread.currentThread().getName() + " created lock on: " + meshName + " (" + source + ") ");
			return true;
		}

		if (Thread.currentThread() == GameLogic.INSTANCE.getGameEngine().getRenderThread()) {
			throw new IllegalStateException("Render thread can't create nor wait on locks (" + source + ")");
		}

//		if (Thread.currentThread() == lock.owner) {
//			throw new IllegalStateException(
//					"Lock: " + meshName + " is being held by current thread: " + Thread.currentThread().getName() + " (" + lock + ")");
//		}

		if (lock.getValue()) {
			log(Level.FINEST, "Thread: " + Thread.currentThread().getName() + " skipped lock: " + meshName + " (" + source + ") " + lock);
			return true;
		}

		log(Level.FINEST, "Thread: " + Thread.currentThread().getName() + " waiting on: " + meshName + " (" + source + ") " + lock);

		return false;
	}

	public static void releaseLock(final String meshName) {
		final String source = PCUtils.getCallerClassName(true);

		final ObjectLock lock = locks.remove(meshName);
		if (lock != null) {
			lock.setValue(true);
			log(Level.FINEST, "Thread " + Thread.currentThread().getName() + " released lock: " + meshName + " (" + source + ") " + lock);
		} else {
			log(Level.SEVERE, "Lock wasn't held for: " + meshName + " (" + source + ")");
		}
	}

	public static void log(final Level l, final String str) {
		if (DEBUG_LOCKS) {
			GlobalLogger.log(l, str);
		}
	}

}
