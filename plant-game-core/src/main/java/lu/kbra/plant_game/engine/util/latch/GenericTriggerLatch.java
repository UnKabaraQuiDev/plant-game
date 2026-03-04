package lu.kbra.plant_game.engine.util.latch;

/**
 * Minimal trigger latch used by the object factory wrappers.
 */
public interface GenericTriggerLatch<T> {
	void trigger(T value);
}
