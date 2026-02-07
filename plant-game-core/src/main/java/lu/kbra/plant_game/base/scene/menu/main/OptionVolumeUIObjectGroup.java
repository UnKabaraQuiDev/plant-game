package lu.kbra.plant_game.base.scene.menu.main;

import lu.kbra.pclib.concurrency.ObjectTriggerLatch;
import lu.kbra.plant_game.base.scene.overlay.group.impl.BoundedUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.entity.ui.data.Direction2d;
import lu.kbra.plant_game.engine.entity.ui.factory.UIObjectFactory;
import lu.kbra.plant_game.engine.entity.ui.group.UIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.impl.AnchorOwner;
import lu.kbra.plant_game.engine.entity.ui.text.TextEmitterOwner;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor;
import lu.kbra.plant_game.engine.scene.ui.layout.AnchorLayout;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class OptionVolumeUIObjectGroup extends BoundedUIObjectGroup {

	public OptionVolumeUIObjectGroup(final UIObjectGroup parent) {
		super("options.volume", new AnchorLayout(), parent, Direction2d.VERTICAL);
	}

	public OptionVolumeUIObjectGroup() {
		super("options.volume", new AnchorLayout(), Direction2d.VERTICAL);
	}

	public <T extends UIObject & TextEmitterOwner & AnchorOwner, S extends UIObject & TextEmitterOwner & AnchorOwner> ObjectTriggerLatch<? extends OptionVolumeUIObjectGroup> init(
			final Class<T> textClazz,
			final Class<S> sliderClazz,
			final float charSize) {
		final ObjectTriggerLatch<? extends OptionVolumeUIObjectGroup> latch = new ObjectTriggerLatch<>(2, this);

		UIObjectFactory.createText(textClazz, charSize)
				.set(i -> i.setTransform(new Transform3D()))
				.set(i -> i.setAnchors(Anchor.CENTER_LEFT, Anchor.CENTER_LEFT))
				.add(this)
				.latch(latch)
				.push();
		UIObjectFactory.createText(sliderClazz, charSize)
				.set(i -> i.setTransform(new Transform3D()))
				.set(i -> i.setAnchors(Anchor.CENTER_RIGHT, Anchor.CENTER_RIGHT))
				.add(this)
				.latch(latch)
				.push();

		return latch;
	}

}
