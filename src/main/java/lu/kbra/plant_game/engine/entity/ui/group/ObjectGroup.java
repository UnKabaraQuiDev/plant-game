package lu.kbra.plant_game.engine.entity.ui.group;

import java.util.List;
import java.util.stream.Stream;

import lu.kbra.standalone.gameengine.objs.entity.ParentAware;
import lu.kbra.standalone.gameengine.objs.entity.SceneEntity;
import lu.kbra.standalone.gameengine.objs.entity.components.SubEntitiesComponent;
import lu.kbra.standalone.gameengine.scene.EntityContainer;

public interface ObjectGroup<T extends SceneEntity> extends Iterable<T>, ParentAware, EntityContainer<T> {

	T get(int index);

	<V extends T> boolean addChildren(ObjectGroup<? extends V> c);

	Object getSubEntitiesLock();

	List<T> getSubEntities();

	SubEntitiesComponent<T> getSubEntitiesComponent();

	Stream<T> parallelStream();

	Stream<T> stream();

	void doSort();

}
