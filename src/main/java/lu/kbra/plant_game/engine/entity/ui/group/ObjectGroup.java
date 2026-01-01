package lu.kbra.plant_game.engine.entity.ui.group;

import lu.kbra.plant_game.engine.entity.go.impl.SynchronizedEntityContainer;
import lu.kbra.standalone.gameengine.objs.entity.ParentAware;
import lu.kbra.standalone.gameengine.objs.entity.SceneEntity;

public interface ObjectGroup<T extends SceneEntity> extends Iterable<T>, ParentAware, SynchronizedEntityContainer<T> {

	T get(int index);

	<V extends T> boolean addChildren(ObjectGroup<? extends V> c);

	void doSort();

}
