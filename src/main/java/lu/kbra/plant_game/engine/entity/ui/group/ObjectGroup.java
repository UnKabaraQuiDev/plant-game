package lu.kbra.plant_game.engine.entity.ui.group;

import java.util.Collection;
import java.util.List;

import lu.kbra.plant_game.engine.entity.ui.impl.UIObject;
import lu.kbra.standalone.gameengine.objs.entity.Entity;
import lu.kbra.standalone.gameengine.objs.entity.ParentAware;
import lu.kbra.standalone.gameengine.objs.entity.components.SubEntitiesComponent;

public interface ObjectGroup<T extends Entity> extends Iterable<T>, ParentAware {

	T get(int index);

	boolean addAll(ObjectGroup<? extends UIObject> c);

	boolean addAll(Collection<? extends UIObject> c);

	<V extends T> V add(V e);

	boolean contains(UIObject o);

	Object getSubEntitiesLock();

	List<UIObject> getSubEntities();

	SubEntitiesComponent<UIObject> getSubEntitiesComponent();

}
