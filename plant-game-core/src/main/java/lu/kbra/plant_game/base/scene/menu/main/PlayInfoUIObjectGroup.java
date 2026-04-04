package lu.kbra.plant_game.base.scene.menu.main;

import java.lang.ref.WeakReference;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Consumer;

import org.joml.Vector2f;
import org.joml.Vector2fc;

import lu.kbra.pclib.concurrency.ObjectTriggerLatch;
import lu.kbra.pclib.logger.GlobalLogger;
import lu.kbra.pclib.pointer.ObjectPointer;
import lu.kbra.plant_game.base.scene.overlay.group.impl.AnchoredLayoutUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.FlatQuadUIObject;
import lu.kbra.plant_game.engine.entity.ui.bar.ProgressBarUIObject;
import lu.kbra.plant_game.engine.entity.ui.factory.UIObjectFactory;
import lu.kbra.plant_game.engine.entity.ui.group.UIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.impl.MarginOwner;
import lu.kbra.plant_game.engine.entity.ui.text.IndexedProgrammaticTextUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.ProgrammaticTextUIObject;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor2D;
import lu.kbra.plant_game.engine.scene.ui.layout.FlowLayout;
import lu.kbra.plant_game.engine.scene.ui.layout.LayoutOwner;
import lu.kbra.plant_game.generated.ColorMaterial;
import lu.kbra.plant_game.plugin.registry.LevelRegistry.LevelDefinition;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextAlignment;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class PlayInfoUIObjectGroup extends AnchoredLayoutUIObjectGroup implements Consumer<LevelDefinition>, MarginOwner {

	protected WeakReference<LevelDefinition> levelDef;

	protected ObjectPointer<ProgrammaticTextUIObject> titleObject = new ObjectPointer<>();
	protected ObjectPointer<ProgrammaticTextUIObject> authorObject = new ObjectPointer<>();
	protected ProgressBarUIObject progressBar = new IndexedProgressBarUIObject("progress", this, new Transform3D(), 2);

	protected float margin = 0.1f;

	public PlayInfoUIObjectGroup(final UIObjectGroup playMenuGroup) {
		super(playMenuGroup
				.getId(), new FlowLayout(true, 0.02f, Anchor2D.TRAILING), playMenuGroup, Anchor.BOTTOM_RIGHT, Anchor.BOTTOM_RIGHT);
	}

	@Override
	public void accept(final LevelDefinition t) {
		super.setActive(true);

		this.getFirstParentMatching(LayoutOwner.class, false)
				.ifPresentOrElse(LayoutOwner::doLayout, () -> GlobalLogger.warning("No LayoutOwner found in hierarchy."));

		if (this.levelDef != null && this.levelDef.get() == t) {
			return;
		}

		this.levelDef = new WeakReference<>(t);

		this.titleObject.ifSet(i -> i.setText(t.getLevelData().getLevelName()).flushText());
		this.authorObject.ifSet(i -> i.setText(t.getLevelData().getAuthor()).flushText());
		this.progressBar.setValue(t.getProgress() / 100f).updateScaling();
	}

	public ObjectTriggerLatch<? extends PlayInfoUIObjectGroup> init() {
		final ObjectTriggerLatch<? extends PlayInfoUIObjectGroup> latch = new ObjectTriggerLatch<>(3, this);

		final OptionalInt NAME_LENGTH = OptionalInt.of(15);
		final Optional<Vector2fc> SIZE = Optional.of(new Vector2f(0.1f));
		final Optional<Vector2fc> HALF_SIZE = Optional.of(SIZE.get().mul(0.75f, new Vector2f()));
		final Optional<TextAlignment> ALIGNMENT = Optional.of(TextAlignment.TEXT_RIGHT);

		UIObjectFactory
				.createText(IndexedProgrammaticTextUIObject.class,
						NAME_LENGTH,
						SIZE,
						ALIGNMENT,
						Optional.<String>empty(),
						Optional.<String>empty())
				.set(i -> i.setIndex(0))
				.set(i -> i.setTransform(new Transform3D()))
				.get(this.titleObject)
				.add(this)
				.latch(latch)
				.push();

		UIObjectFactory
				.createText(IndexedProgrammaticTextUIObject.class,
						NAME_LENGTH,
						HALF_SIZE,
						ALIGNMENT,
						Optional.<String>empty(),
						Optional.<String>empty())
				.set(i -> i.setIndex(1))
				.set(i -> i.setTransform(new Transform3D()))
				.get(this.authorObject)
				.add(this)
				.latch(latch)
				.push();

		this.progressBar.init(FlatQuadUIObject.class, FlatQuadUIObject.class).then((Consumer<ProgressBarUIObject>) c -> {
			final float maxWidth = SIZE.get().x() * 10; // NAME_LENGTH.getAsInt();
			c.getTransform()
					.scaleSet(maxWidth / (float) c.getBounds().getBounds2D().getWidth())
					.scaleSetZ(HALF_SIZE.get().y())
					.updateMatrix();
			this.progressBar.setMargin(0.0125f);
			this.progressBar.setForegroundColor(ColorMaterial.GREEN);
		}).latch(latch);

		return latch;
	}

	@Override
	public float getMargin() {
		return this.margin;
	}

	@Override
	public void setMargin(final float margin) {
		this.margin = margin;
	}

	@Override
	public String toString() {
		return "PlayInfoUIObjectGroup@" + System.identityHashCode(this) + " [titleObject=" + this.titleObject + ", authorObject="
				+ this.authorObject + ", margin=" + this.margin + ", objectAnchor=" + this.objectAnchor + ", targetAnchor="
				+ this.targetAnchor + ", layout=" + this.layout + ", subEntitiesLock=" + this.subEntitiesLock + ", subEntities="
				+ this.subEntities + ", computedBounds=" + this.computedBounds + ", transform=" + this.transform + ", active=" + this.active
				+ ", name=" + this.name + "]";
	}

}
