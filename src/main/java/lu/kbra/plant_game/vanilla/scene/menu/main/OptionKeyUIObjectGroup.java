package lu.kbra.plant_game.vanilla.scene.menu.main;

import java.util.Optional;
import java.util.OptionalInt;

import org.joml.Vector2f;

import lu.pcy113.pclib.concurrency.ObjectTriggerLatch;

import lu.kbra.plant_game.engine.entity.ui.data.Direction2d;
import lu.kbra.plant_game.engine.entity.ui.factory.UIObjectFactory;
import lu.kbra.plant_game.engine.entity.ui.group.UIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.impl.DebugBoundsColor;
import lu.kbra.plant_game.engine.entity.ui.text.AnchoredProgrammaticTextUIObject;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor;
import lu.kbra.plant_game.engine.scene.ui.layout.AnchorLayout;
import lu.kbra.plant_game.engine.window.input.KeyOption;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.plant_game.generated.ColorMaterial;
import lu.kbra.plant_game.vanilla.scene.overlay.group.impl.BoundedUIObjectGroup;

public class OptionKeyUIObjectGroup extends BoundedUIObjectGroup implements DebugBoundsColor {

	protected KeyOption keyOption;

	public OptionKeyUIObjectGroup(final KeyOption keyOption, final UIObjectGroup parent) {
		super("option" + keyOption.toString(), new AnchorLayout(), parent, Direction2d.VERTICAL);
		this.keyOption = keyOption;
	}

	public ObjectTriggerLatch<OptionKeyUIObjectGroup> init(final WindowInputHandler inputHandler) {
		final ObjectTriggerLatch<OptionKeyUIObjectGroup> latch = new ObjectTriggerLatch<>(2, this);

		final float charSize = 0.1f;

		UIObjectFactory.createText(AnchoredProgrammaticTextUIObject.class, charSize, "key." + this.keyOption.toString().toLowerCase())
				.set(i -> i.setAnchors(Anchor.CENTER_LEFT, Anchor.CENTER_LEFT))
				.add(this)
				.latch(latch)
				.push();

		UIObjectFactory
				.createText(AnchoredProgrammaticTextUIObject.class,
						OptionalInt.of(10),
						Optional.of(new Vector2f(charSize)),
						Optional.empty(),
						Optional.empty(),
						Optional.empty())
				.set(i -> i.setAnchors(Anchor.CENTER_RIGHT, Anchor.CENTER_RIGHT))
				.set(i -> i.setText("[" + inputHandler.getInputName(this.keyOption.getPhysicalKey()) + "]"))
				.postInit(AnchoredProgrammaticTextUIObject::flushText)
				.add(this)
				.latch(latch)
				.push();

		return latch;
	}

	@Override
	public ColorMaterial getBoundsColor() {
		return ColorMaterial.RED;
	}

	@Override
	public String toString() {
		return "OptionKeyUIObjectGroup@" + System.identityHashCode(this) + " [keyOption=" + this.keyOption + ", dir=" + this.dir
				+ ", bounds=" + this.bounds + ", layout=" + this.layout + ", subEntitiesLock=" + this.subEntitiesLock + ", subEntities="
				+ this.subEntities + ", computedBounds=" + this.computedBounds + ", transform=" + this.transform + ", active=" + this.active
				+ ", name=" + this.name + "]";
	}

}
