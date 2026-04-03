package lu.kbra.plant_game.engine.entity.go.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import lu.kbra.pclib.PCUtils;
import lu.kbra.pclib.concurrency.DeferredTriggerLatch;
import lu.kbra.pclib.concurrency.GenericTriggerLatch;
import lu.kbra.pclib.pointer.ObjectPointer;
import lu.kbra.plant_game.engine.entity.go.GameObject;
import lu.kbra.plant_game.engine.entity.impl.NeedsPostConstruct;
import lu.kbra.plant_game.plugin.registry.GameObjectRegistry;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.TaskFuture;
import lu.kbra.standalone.gameengine.scene.EntityContainer;

public class GOCreatingTaskFuture<T extends GameObject> extends TaskFuture<List<Object>, T> {

	public static final String KEEP_SOURCE_PROPERTY = GOCreatingTaskFuture.class.getSimpleName() + ".keep_source";
	public static boolean KEEP_SOURCE = Boolean.getBoolean(KEEP_SOURCE_PROPERTY);

	protected Throwable source;

	protected Class<T> clazz;

	protected List<Consumer<T>> postCreateHooks = new ArrayList<>();
	protected List<Consumer<T>> postInitHooks = new ArrayList<>();

	public GOCreatingTaskFuture(final Dispatcher dispatcher, final Class<T> clazz) {
		super(dispatcher);
		this.clazz = clazz;
		this.source = new Throwable().fillInStackTrace();
		super.task = list -> {
			try {
				final T instance = GameObjectRegistry.create(clazz,
						PCUtils.combineArrays(new Object[] { clazz.getSimpleName() + "#" + System.nanoTime() },
								list == null ? new Object[0] : list.toArray()));
				this.postCreateHooks.forEach(pch -> pch.accept(instance));
				if (instance instanceof final NeedsPostConstruct npc) {
					npc.postConstruct();
				}
				this.postInitHooks.forEach(pch -> pch.accept(instance));
				return instance;
			} catch (Throwable t) {
				t.addSuppressed(this.source);
				throw t;
			}
		};
	}

	public GOCreatingTaskFuture<T> set(final Consumer<T> setter) {
		this.postCreateHooks.add(setter);
		return this;
	}

	public GOCreatingTaskFuture<T> add(final EntityContainer<? super T> parent) {
		if (parent == null) {
			return this;
		}
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
		if (latch == null) {
			return this;
		}
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
