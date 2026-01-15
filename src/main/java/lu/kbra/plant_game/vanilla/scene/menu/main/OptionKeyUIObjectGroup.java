package lu.kbra.plant_game.vanilla.scene.menu.main;

import java.util.Optional;
import java.util.OptionalInt;

import org.joml.Vector2f;
import org.joml.Vector3fc;

import lu.pcy113.pclib.concurrency.ObjectTriggerLatch;

import lu.kbra.plant_game.engine.entity.ui.data.Direction2d;
import lu.kbra.plant_game.engine.entity.ui.factory.UIObjectFactory;
import lu.kbra.plant_game.engine.entity.ui.group.UIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.impl.GrowOnHover;
import lu.kbra.plant_game.engine.entity.ui.text.AnchoredProgrammaticTextUIObject;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor;
import lu.kbra.plant_game.engine.scene.ui.layout.AnchorLayout;
import lu.kbra.plant_game.engine.window.input.KeyOption;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.plant_game.vanilla.scene.overlay.group.impl.BoundedUIObjectGroup;
import lu.kbra.standalone.gameengine.GameEngine;
import lu.kbra.standalone.gameengine.utils.interpolation.Interpolator;
import lu.kbra.standalone.gameengine.utils.interpolation.Interpolators;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class OptionKeyUIObjectGroup extends BoundedUIObjectGroup implements GrowOnHover {

	protected float progress = 0f;
	protected KeyOption keyOption;

	public OptionKeyUIObjectGroup(final KeyOption keyOption, final UIObjectGroup parent) {
		super("option" + keyOption.toString(), new AnchorLayout(), parent, Direction2d.VERTICAL);
		this.keyOption = keyOption;
	}

	public ObjectTriggerLatch<OptionKeyUIObjectGroup> init(final WindowInputHandler inputHandler, final float charSize) {
		final ObjectTriggerLatch<OptionKeyUIObjectGroup> latch = new ObjectTriggerLatch<>(2, this);

		UIObjectFactory.createText(AnchoredProgrammaticTextUIObject.class, charSize, "key." + this.keyOption.toString().toLowerCase())
				.set(i -> i.setTransform(new Transform3D()))
				.set(i -> i.setAnchors(Anchor.CENTER_LEFT, Anchor.CENTER_LEFT))
				.add(this)
				.latch(latch)
				.push();

		UIObjectFactory
				.createText(AnchoredProgrammaticTextUIObject.class,
						OptionalInt.of(10),
						Optional.of(new Vector2f(charSize)),
						Optional.empty(),
						Optional.of("key-value." + this.keyOption.toString().toLowerCase()),
						Optional.empty())
				.set(i -> i.setTransform(new Transform3D()))
				.set(i -> i.setAnchors(Anchor.CENTER_RIGHT, Anchor.CENTER_RIGHT))
				.set(i -> i.setText("[" + inputHandler.getInputName(this.keyOption.getPhysicalKey()) + "]"))
				.postInit(AnchoredProgrammaticTextUIObject::flushText)
				.add(this)
				.latch(latch)
				.push();

		return latch;
	}

	@Override
	public float getGrowthRate(final boolean grow) {
		return 2f;
	}

	@Override
	public float getGrowthProgress() {
		return this.progress;
	}

	@Override
	public void setGrowthProgress(final float f) {
		this.progress = f;
	}

	@Override
	public Interpolator getInterpolator(final boolean grow) {
		return grow ? Interpolators.CUBIC_OUT : Interpolators.CIRC_OUT;
	}

	@Override
	public Vector3fc getTargetScale(final boolean grow) {
		return grow ? BOTH_GROWTH_SCALE : GameEngine.IDENTITY_VECTOR3F;
	}

	@Override
	public String toString() {
		return "OptionKeyUIObjectGroup@" + System.identityHashCode(this) + " [keyOption=" + this.keyOption + ", dir=" + this.dir
				+ ", bounds=" + this.bounds + ", layout=" + this.layout + ", subEntitiesLock=" + this.subEntitiesLock + ", subEntities="
				+ this.subEntities + ", computedBounds=" + this.computedBounds + ", transform=" + this.transform + ", active=" + this.active
				+ ", name=" + this.name + "]";
	}

}
