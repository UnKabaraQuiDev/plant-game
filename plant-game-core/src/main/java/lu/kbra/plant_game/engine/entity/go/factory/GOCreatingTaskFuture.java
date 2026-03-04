package lu.kbra.plant_game.engine.entity.go.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import lu.kbra.plant_game.engine.entity.go.GameObject;
import lu.kbra.plant_game.engine.entity.impl.NeedsPostConstruct;
import lu.kbra.plant_game.engine.util.latch.DeferredTriggerLatch;
import lu.kbra.plant_game.engine.util.latch.GenericTriggerLatch;
import lu.kbra.pclib.pointer.ObjectPointer;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.TaskFuture;
import lu.kbra.standalone.gameengine.scene.EntityContainer;

/**
 * Post-processing wrapper for GameObject creation.
 * <p>
 * This future expects the GameObject instance as input and applies hooks.
 */
public class GOCreatingTaskFuture<T extends GameObject> extends TaskFuture<T, T> {

	protected final Class<T> clazz;
	protected final List<Consumer<T>> postCreateHooks = new ArrayList<>();
	protected final List<Consumer<T>> postInitHooks = new ArrayList<>();

	public GOCreatingTaskFuture(final Dispatcher dispatcher, final Class<T> clazz) {
		super(dispatcher);
		this.clazz = clazz;
		super.task = instance -> {
			final T v = this.clazz.cast(instance);
			this.postCreateHooks.forEach(h -> h.accept(v));
			if (v instanceof final NeedsPostConstruct npc) {
				npc.init();
			}
			this.postInitHooks.forEach(h -> h.accept(v));
			return v;
		};
	}

	public GOCreatingTaskFuture<T> set(final Consumer<T> setter) {
		this.postCreateHooks.add(setter);
		return this;
	}

	public GOCreatingTaskFuture<T> add(final EntityContainer<? super T> parent) {
		Objects.requireNonNull(parent);
		this.postInitHooks.add(parent::add);
		return this;
	}

	public GOCreatingTaskFuture<T> get(final ObjectPointer<? super T> ptr) {
		Objects.requireNonNull(ptr);
		this.postInitHooks.add(ptr::set);
		return this;
	}

	public GOCreatingTaskFuture<T> postInit(final Consumer<T> postInit) {
		this.postInitHooks.add(postInit);
		return this;
	}

	public GOCreatingTaskFuture<T> latch(final GenericTriggerLatch<? super T> latch) {
		Objects.requireNonNull(latch);
		this.postInitHooks.add(latch::trigger);
		return this;
	}

	public DeferredTriggerLatch<T> pushAsLatch() {
		final DeferredTriggerLatch<T> latch = new DeferredTriggerLatch<>();
		this.latch(latch);
		this.push();
		return latch;
	}
}
