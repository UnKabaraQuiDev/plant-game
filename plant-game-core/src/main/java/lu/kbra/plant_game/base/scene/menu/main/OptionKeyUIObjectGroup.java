package lu.kbra.plant_game.base.scene.menu.main;

import java.util.Optional;
import java.util.OptionalInt;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.lwjgl.glfw.GLFW;

import lu.kbra.pclib.concurrency.ObjectTriggerLatch;
import lu.kbra.pclib.pointer.ObjectPointer;
import lu.kbra.plant_game.PGLogic;
import lu.kbra.plant_game.base.scene.overlay.group.impl.BoundedUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.data.Direction2d;
import lu.kbra.plant_game.engine.entity.ui.factory.UIObjectFactory;
import lu.kbra.plant_game.engine.entity.ui.group.UIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.impl.GrowOnHover;
import lu.kbra.plant_game.engine.entity.ui.text.AnchoredProgrammaticTextUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.TextUIObject;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor;
import lu.kbra.plant_game.engine.scene.ui.layout.AnchorLayout;
import lu.kbra.plant_game.engine.window.input.KeyOption;
import lu.kbra.plant_game.engine.window.input.MappingInputHandler;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.plant_game.plugin.registry.KeyRegistry;
import lu.kbra.standalone.gameengine.GameEngine;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextAlignment;
import lu.kbra.standalone.gameengine.utils.interpolation.Interpolator;
import lu.kbra.standalone.gameengine.utils.interpolation.Interpolators;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class OptionKeyUIObjectGroup extends BoundedUIObjectGroup implements GrowOnHover, NeedsFocusInput {

	protected static final Vector3fc TARGET_SCALE = new Vector3f(1.1f, 1, 1);

	protected float progress = 0f;

	protected KeyOption keyOption;

	protected ObjectPointer<TextUIObject> key = new ObjectPointer<>();
	protected ObjectPointer<TextUIObject> value = new ObjectPointer<>();

	protected boolean focused;

	public OptionKeyUIObjectGroup(final KeyOption keyOption, final UIObjectGroup parent) {
		super("option" + keyOption.toString(), new AnchorLayout(), parent, new Transform3D(), Direction2d.VERTICAL);
		this.keyOption = keyOption;
	}

	@Override
	public boolean focusInput(final WindowInputHandler inputHandler) {
		if (!(inputHandler instanceof MappingInputHandler mapping)) {
			assert false : inputHandler;
			return false;
		}

		if (inputHandler.hasPressedKeyChar()) {
			final int pressedKey = inputHandler.getPressedKey();
			if (pressedKey != GLFW.GLFW_KEY_ESCAPE) {
				mapping.remapKey(this.keyOption.getPhysicalKey(), pressedKey);
			}
			return false;
		}
		if (inputHandler.isKeyPressedOrRepeat(GLFW.GLFW_KEY_BACKSPACE)) {
			// unset
			mapping.unsetKey(this.keyOption.getPhysicalKey());
			mapping.unsetMouseButton(this.keyOption.getPhysicalKey());

			return false;
		}
		if (inputHandler.hasPressedMouseOnce()) {
			final int pressedBtn = inputHandler.getPressedMouse();
			mapping.remapMouseButton(this.keyOption.getPhysicalKey(), pressedBtn);

			return false;
		}

		return true;
	}

	@Override
	public void giveFocus() {
		NeedsFocusInput.super.giveFocus();
		this.setValue(" ");
	}

	@Override
	public void removeFocus() {
		NeedsFocusInput.super.removeFocus();
		this.setValue(Optional.ofNullable(PGLogic.INSTANCE.getInputHandler().getInputName(this.keyOption)).orElse(" "));
	}

	protected void setValue(final String keyName) {
		this.value.ifSet(i -> i.setText("[" + keyName + "]").flushText());
	}

	public ObjectTriggerLatch<OptionKeyUIObjectGroup> init(final WindowInputHandler inputHandler, final float charSize) {
		final ObjectTriggerLatch<OptionKeyUIObjectGroup> latch = new ObjectTriggerLatch<>(2, this);

		UIObjectFactory.createText(AnchoredProgrammaticTextUIObject.class, charSize, KeyRegistry.getLocalizationKey(this.keyOption))
				.set(i -> i.setTransform(new Transform3D()))
				.set(i -> i.setAnchors(Anchor.CENTER_LEFT, Anchor.CENTER_LEFT))
				.add(this)
				.get(this.key)
				.latch(latch)
				.push();

		UIObjectFactory
				.createText(AnchoredProgrammaticTextUIObject.class,
						OptionalInt.of(10),
						Optional.of(new Vector2f(charSize)),
						Optional.of(TextAlignment.TEXT_RIGHT),
						Optional.of("key-value." + this.keyOption.toString().toLowerCase()),
						Optional.empty())
				.set(i -> i.setTransform(new Transform3D()))
				.set(i -> i.setAnchors(Anchor.CENTER_RIGHT, Anchor.CENTER_RIGHT))
				.set(i -> i.setText("[" + inputHandler.getInputName(this.keyOption.getPhysicalKey()) + "]"))
				.postInit(AnchoredProgrammaticTextUIObject::flushText)
				.add(this)
				.get(this.value)
				.latch(latch)
				.push();

		return latch;
	}

//	@Override
//	public void grow(final float dTime, final boolean grow) {
//		final Vector3f scale = this.getTransform().getScale();
//		final Vector3fc start = this.getTargetScale(false);
//		final Vector3fc end = this.getTargetScale(true);
//
//		this.compute(dTime, grow);
//
//		final float interpT = this.getInterpolator(grow).evaluate(this.getGrowthProgress());
//
//		start.lerp(end, interpT, scale);
//
//		this.getTransform().update();
//
//		this.recomputeLayout();
//	}

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
	public Vector3fc getTargetScale(final boolean grow) {
		return grow ? TARGET_SCALE : GameEngine.IDENTITY_VECTOR3F;
	}

	@Override
	public Interpolator getInterpolator(final boolean grow) {
		return grow ? Interpolators.CUBIC_OUT : Interpolators.CIRC_OUT;
	}

	@Override
	public boolean hasFocus() {
		return this.focused;
	}

	@Override
	public void setFocused(final boolean focused) {
		this.focused = focused;
	}

	@Override
	public String toString() {
		return "OptionKeyUIObjectGroup@" + System.identityHashCode(this) + " [keyOption=" + this.keyOption + ", dir=" + this.dir
				+ ", bounds=" + this.bounds + ", layout=" + this.layout + ", subEntitiesLock=" + this.subEntitiesLock + ", subEntities="
				+ this.subEntities + ", computedBounds=" + this.computedBounds + ", transform=" + this.transform + ", active=" + this.active
				+ ", name=" + this.name + "]";
	}

}
