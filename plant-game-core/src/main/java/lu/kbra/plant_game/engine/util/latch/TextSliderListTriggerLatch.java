package lu.kbra.plant_game.engine.util.latch;

import lu.kbra.pclib.concurrency.ListTriggerLatch;
import lu.kbra.plant_game.engine.entity.impl.ObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.entity.ui.text.TextUIObject;
import lu.kbra.plant_game.engine.scene.ui.UIScene;
import lu.kbra.plant_game.engine.scene.ui.layout.LayoutOwner;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;

public class TextSliderListTriggerLatch<A extends TextUIObject, B extends UIObject, V extends UIObject> extends ListTriggerLatch<V> {

	public TextSliderListTriggerLatch(final Dispatcher workers, final ObjectGroup<V> parent, final Class<A> text, final Class<B> value) {
		super(2, l -> workers.post(() -> {
			final Object a = l.get(0);
			final Object b = l.get(1);

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

			if (parent instanceof final LayoutOwner lp) {
				lp.doLayout();
			}
		}));
	}

	public TextSliderListTriggerLatch(final Dispatcher workers, final UIScene parent, final Class<A> text, final Class<B> value) {
		super(2, l -> workers.post(() -> {
			final Object a = l.get(0);
			final Object b = l.get(1);

			final A objA = text.isInstance(a) ? text.cast(a)
					: text.isInstance(b) ? text.cast(b)
					: null;
			final B objB = value.isInstance(a) ? value.cast(a)
					: value.isInstance(b) ? value.cast(b)
					: null;

			if (objA != null) {
				parent.add(objA);
			}
			if (objB != null) {
				parent.add(objB);
			}

			if (parent instanceof final LayoutOwner lp) {
				lp.doLayout();
			}
		}));
	}

}
