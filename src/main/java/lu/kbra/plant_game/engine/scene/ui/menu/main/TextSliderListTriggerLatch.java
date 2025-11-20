package lu.kbra.plant_game.engine.scene.ui.menu.main;

import lu.pcy113.pclib.concurrency.ListTriggerLatch;

import lu.kbra.plant_game.engine.entity.ui.group.ObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.impl.TextUIObject;
import lu.kbra.plant_game.engine.entity.ui.impl.UIObject;
import lu.kbra.plant_game.engine.scene.ui.UIScene;
import lu.kbra.plant_game.engine.scene.ui.layout.LayoutParent;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;

public class TextSliderListTriggerLatch<A extends TextUIObject, B extends UIObject, V extends UIObject> extends ListTriggerLatch<V> {

	public TextSliderListTriggerLatch(final Dispatcher workers, final ObjectGroup<V> parent, final Class<A> text, final Class<B> value) {
		super(2, l -> workers.post(() -> {
			final Object a = l.get(0);
			final Object b = l.get(1);

			// determine which object matches which class
			final A objA = text.isInstance(a) ? text.cast(a)
					: text.isInstance(b) ? text.cast(b)
					: null;
			final B objB = value.isInstance(a) ? value.cast(a)
					: value.isInstance(b) ? value.cast(b)
					: null;

			if (objA != null) {
				parent.add((V) objA);
			}
			if (objB != null) {
				parent.add((V) objB);
			}

			if (parent instanceof final LayoutParent lp) {
				lp.doLayout();
			}
		}));
	}

	public TextSliderListTriggerLatch(final Dispatcher workers, final UIScene parent, final Class<A> text, final Class<B> value) {
		super(2, l -> workers.post(() -> {
			final Object a = l.get(0);
			final Object b = l.get(1);

			// determine which object matches which class
			final A objA = text.isInstance(a) ? text.cast(a)
					: text.isInstance(b) ? text.cast(b)
					: null;
			final B objB = value.isInstance(a) ? value.cast(a)
					: value.isInstance(b) ? value.cast(b)
					: null;

			if (objA != null) {
				parent.addEntity(objA);
			}
			if (objB != null) {
				parent.addEntity(objB);
			}

			if (parent instanceof final LayoutParent lp) {
				lp.doLayout();
			}
		}));
	}

}
