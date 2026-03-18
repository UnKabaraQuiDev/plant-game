package lu.kbra.plant_game.engine.entity.ui.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import lu.kbra.pclib.PCUtils;
import lu.kbra.pclib.concurrency.DeferredTriggerLatch;
import lu.kbra.pclib.concurrency.GenericTriggerLatch;
import lu.kbra.pclib.pointer.ObjectPointer;
import lu.kbra.plant_game.engine.entity.impl.NeedsPostConstruct;
import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.plugin.registry.UIObjectRegistry;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.TaskFuture;
import lu.kbra.standalone.gameengine.scene.EntityContainer;

public class UOCreatingTaskFuture<T extends UIObject> extends TaskFuture<List<Object>, T> {

	protected Class<T> clazz;

	protected List<Consumer<T>> postCreateHooks = new ArrayList<>();
	protected List<Consumer<T>> postInitHooks = new ArrayList<>();

	public UOCreatingTaskFuture(final Dispatcher dispatcher, final Class<T> clazz) {
		super(dispatcher);
		this.clazz = clazz;
		super.task = list -> {
			final T instance = UIObjectRegistry.create(clazz,
					PCUtils.combineArrays(new Object[] { clazz.getSimpleName() + "#" + System.nanoTime() },
							list == null ? new Object[0] : list.toArray()));
			this.postCreateHooks.forEach(pch -> pch.accept(instance));
			if (instance instanceof final NeedsPostConstruct npc) {
				npc.postConstruct();
			}
			this.postInitHooks.forEach(pch -> pch.accept(instance));
			return instance;
		};
	}

	public UOCreatingTaskFuture<T> set(final Consumer<T> setter) {
		this.postCreateHooks.add(setter);
		return this;
	}

	public UOCreatingTaskFuture<T> add(final EntityContainer<? super T> parent) {
		Objects.requireNonNull(parent);
		this.postInitHooks.add(v -> parent.add(v));
		return this;
	}

	public UOCreatingTaskFuture<T> get(final ObjectPointer<? super T> ptr) {
		Objects.requireNonNull(ptr);
		this.postInitHooks.add(v -> ptr.set(v));
		return this;
	}

	public UOCreatingTaskFuture<T> postInit(final Consumer<T> postInit) {
		this.postInitHooks.add(postInit);
		return this;
	}

	public UOCreatingTaskFuture<T> latch(final GenericTriggerLatch<? super T> latch) {
		Objects.requireNonNull(latch);
		this.postInitHooks.add((final T v) -> latch.trigger(v));
		return this;
	}

	public DeferredTriggerLatch<T> pushAsLatch() {
		final DeferredTriggerLatch<T> latch = new DeferredTriggerLatch<>();
		this.latch(latch);
		this.push();
		return latch;
	}

}
