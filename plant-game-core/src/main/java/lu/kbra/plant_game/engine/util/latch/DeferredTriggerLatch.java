package lu.kbra.plant_game.engine.util.latch;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Simple latch that can be triggered once and listened to.
 */
public final class DeferredTriggerLatch<T> implements GenericTriggerLatch<T> {

	private final List<Consumer<T>> listeners = new ArrayList<>();
	private boolean triggered;
	private T value;

	@Override
	public synchronized void trigger(final T value) {
		if (this.triggered) {
			return;
		}
		this.triggered = true;
		this.value = value;
		for (final Consumer<T> c : this.listeners) {
			c.accept(value);
		}
		this.listeners.clear();
	}

	public synchronized void then(final Consumer<T> consumer) {
		if (this.triggered) {
			consumer.accept(this.value);
			return;
		}
		this.listeners.add(consumer);
	}
}
