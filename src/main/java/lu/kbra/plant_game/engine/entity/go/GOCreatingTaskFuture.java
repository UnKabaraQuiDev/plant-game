package lu.kbra.plant_game.engine.entity.go;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import lu.pcy113.pclib.PCUtils;

import lu.kbra.plant_game.engine.entity.impl.NeedsPostConstruct;
import lu.kbra.plant_game.generated.GameObjectRegistry;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.TaskFuture;
import lu.kbra.standalone.gameengine.scene.EntityContainer;

public class GOCreatingTaskFuture<T extends GameObject> extends TaskFuture<List<Object>, T> {

	protected Class<T> clazz;

	protected List<Consumer<T>> postCreateHooks = new ArrayList<>();
	protected EntityContainer parent;

	public GOCreatingTaskFuture(final Dispatcher dispatcher, final Class<T> clazz) {
		super(dispatcher);
		this.clazz = clazz;
		super.task = list -> {
			final T instance = GameObjectRegistry.create(clazz,
					PCUtils.combineArrays(new Object[] { clazz.getSimpleName() + "#" + System.nanoTime() },
							list == null ? new Object[0] : list.toArray()));
			this.postCreateHooks.forEach(pch -> pch.accept(instance));
			if (this.parent != null) {
				this.parent.add(instance);
			}
			if (instance instanceof final NeedsPostConstruct npc) {
				npc.init();
			}
			return instance;
		};
	}

	public GOCreatingTaskFuture<T> set(final Consumer<T> setter) {
		this.postCreateHooks.add(setter);
		return this;
	}

	public GOCreatingTaskFuture<T> add(final EntityContainer parent) {
		this.parent = parent;
		return this;
	}

}
