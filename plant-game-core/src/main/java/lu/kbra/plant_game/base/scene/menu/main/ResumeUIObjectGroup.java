package lu.kbra.plant_game.base.scene.menu.main;

import java.lang.ref.WeakReference;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Consumer;

import org.joml.Vector2f;
import org.joml.Vector2fc;

import lu.kbra.pclib.concurrency.ObjectTriggerLatch;
import lu.kbra.pclib.pointer.ObjectPointer;
import lu.kbra.plant_game.base.scene.overlay.group.impl.MarginAnchoredUIObjectGroup;
import lu.kbra.plant_game.base.scene.overlay.group.impl.ParentUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.btn.BackButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.btn.RestartButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.btn.ResumeButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.btn.StartButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.factory.UIObjectFactory;
import lu.kbra.plant_game.engine.entity.ui.group.UIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.impl.DebugBoundsColor;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor2D;
import lu.kbra.plant_game.engine.scene.ui.layout.AnchorLayout;
import lu.kbra.plant_game.engine.scene.ui.layout.FlowLayout;
import lu.kbra.plant_game.engine.scene.ui.layout.NoLayout;
import lu.kbra.plant_game.generated.ColorMaterial;
import lu.kbra.plant_game.plugin.registry.LevelRegistry.LevelDefinition;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextAlignment;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class ResumeUIObjectGroup extends ParentUIObjectGroup implements DebugBoundsColor, Consumer<LevelDefinition>, NoLayout {

	protected WeakReference<LevelDefinition> levelDef;

	protected MarginAnchoredUIObjectGroup buttonsMenuGroup = new MarginAnchoredUIObjectGroup("resume.buttons",
			new FlowLayout(true, 0.06f, Anchor2D.LEADING),
			this,
			Anchor.CENTER_LEFT,
			Anchor.CENTER_LEFT,
			0.25f);

	protected PlayInfoUIObjectGroup playInfoUIObjectGroup = new PlayInfoUIObjectGroup(this);
	protected ResumeResourcesUIObjectGroup resourcesGroup = new ResumeResourcesUIObjectGroup(this);

	protected ObjectPointer<ResumeButtonUIObject> resumeButtonObject = new ObjectPointer<>();
	protected ObjectPointer<RestartButtonUIObject> restartButtonObject = new ObjectPointer<>();
	protected ObjectPointer<StartButtonUIObject> startButtonObject = new ObjectPointer<>();

	public ResumeUIObjectGroup(final UIObjectGroup parent) {
		super("resume-content", new AnchorLayout(), parent);
		this.setPadding(0.01f);
	}

	public ObjectTriggerLatch<? extends ResumeUIObjectGroup> init() {
		final Optional<Vector2fc> SMALL_TEXT_CHAR_SIZE = Optional.of(new Vector2f(0.2f));
		final Optional<TextAlignment> SMALL_TEXT_TEXT_ALIGNMENT = Optional.of(TextAlignment.TEXT_CENTER);

		final ObjectTriggerLatch<? extends ResumeUIObjectGroup> latch = new ObjectTriggerLatch<>(3 + 3, this);

		this.playInfoUIObjectGroup.init().latch(latch);
		this.resourcesGroup.init().latch(latch);

		UIObjectFactory.create(BackButtonUIObject.class)
				.set(i -> i.setTransform(new Transform3D()))
				.set(i -> i.setAnchors(Anchor.TOP_LEFT, Anchor.TOP_LEFT))
				.set(i -> i.setMargin(0.05f))
				.add(this)
				.latch(latch)
				.push();

		UIObjectFactory
				.createText(ResumeButtonUIObject.class,
						OptionalInt.empty(),
						SMALL_TEXT_CHAR_SIZE,
						SMALL_TEXT_TEXT_ALIGNMENT,
						Optional.empty(),
						Optional.empty())
				.set(i -> i.setTransform(new Transform3D()))
				.get(this.resumeButtonObject)
				.add(this.buttonsMenuGroup)
				.latch(latch)
				.push();

		UIObjectFactory
				.createText(StartButtonUIObject.class,
						OptionalInt.empty(),
						SMALL_TEXT_CHAR_SIZE,
						SMALL_TEXT_TEXT_ALIGNMENT,
						Optional.empty(),
						Optional.empty())
				.set(i -> i.setTransform(new Transform3D()))
				.get(this.startButtonObject)
				.add(this.buttonsMenuGroup)
				.latch(latch)
				.push();

		UIObjectFactory
				.createText(RestartButtonUIObject.class,
						OptionalInt.empty(),
						SMALL_TEXT_CHAR_SIZE,
						SMALL_TEXT_TEXT_ALIGNMENT,
						Optional.empty(),
						Optional.empty())
				.set(i -> i.setTransform(new Transform3D()))
				.get(this.restartButtonObject)
				.add(this.buttonsMenuGroup)
				.latch(latch)
				.push();

		this.playInfoUIObjectGroup.setAnchors(Anchor.TOP_RIGHT, Anchor.TOP_RIGHT);

		this.resourcesGroup.setMargin(this.playInfoUIObjectGroup.getMargin());

		return latch;
	}

	@Override
	public void accept(final LevelDefinition t) {
		if (this.levelDef != null && this.levelDef.get() == t) {
			return;
		}

		this.levelDef = new WeakReference<>(t);

		this.playInfoUIObjectGroup.accept(t);
		this.resourcesGroup.accept(t);

		if (t.isNew()) {
			this.startButtonObject.ifSet(i -> i.setActive(true));
			this.resumeButtonObject.ifSet(i -> i.setActive(false));
			this.restartButtonObject.ifSet(i -> i.setActive(false));
		} else {
			this.startButtonObject.ifSet(i -> i.setActive(false));
			this.resumeButtonObject.ifSet(i -> i.setActive(true));
			this.restartButtonObject.ifSet(i -> i.setActive(true));
		}
	}

	@Override
	public ColorMaterial getBoundsColor() {
		return ColorMaterial.GREEN;
	}

	@Override
	public String toString() {
		return "ResumeUIObjectGroup@" + System.identityHashCode(this) + " [levelDef=" + this.levelDef + ", playInfoUIObjectGroup="
				+ this.playInfoUIObjectGroup + ", resourcesGroup=" + this.resourcesGroup + ", padding=" + this.padding + ", layout="
				+ this.layout + ", subEntitiesLock=" + this.subEntitiesLock + ", subEntities=" + this.subEntities + ", computedBounds="
				+ this.computedBounds + ", transform=" + this.transform + ", active=" + this.active + ", name=" + this.name + "]";
	}

}
