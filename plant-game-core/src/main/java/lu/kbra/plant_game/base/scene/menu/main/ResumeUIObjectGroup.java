package lu.kbra.plant_game.base.scene.menu.main;

import java.lang.ref.WeakReference;
import java.util.function.Consumer;

import lu.kbra.pclib.concurrency.ObjectTriggerLatch;
import lu.kbra.plant_game.base.scene.overlay.group.impl.ParentUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.group.UIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.impl.DebugBoundsColor;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor;
import lu.kbra.plant_game.engine.scene.ui.layout.AnchorLayout;
import lu.kbra.plant_game.generated.ColorMaterial;
import lu.kbra.plant_game.plugin.registry.LevelRegistry.LevelDefinition;

public class ResumeUIObjectGroup extends ParentUIObjectGroup implements DebugBoundsColor, /* MarginOwner, */ Consumer<LevelDefinition> {

	protected WeakReference<LevelDefinition> levelDef;

	protected PlayInfoUIObjectGroup playInfoUIObjectGroup = new PlayInfoUIObjectGroup(this);
	protected ResumeResourcesUIObjectGroup resourcesGroup = new ResumeResourcesUIObjectGroup(this);

//	protected float margin;

	public ResumeUIObjectGroup(final UIObjectGroup parent) {
		super("resume-content", new AnchorLayout(), parent);
//		super("resume-content", new FlowLayout(true, 0.1f, Anchor2D.TRAILING), parent, Anchor.TOP_RIGHT, Anchor.TOP_RIGHT);
//		this.setMargin(0.4f);
	}

	public ObjectTriggerLatch<? extends ResumeUIObjectGroup> init() {
		final ObjectTriggerLatch<? extends ResumeUIObjectGroup> latch = new ObjectTriggerLatch<>(2, this) {
			public void trigger(final Object value) {
				System.err.println("triggered");
				super.trigger(value);
			};
		};

		this.playInfoUIObjectGroup.init().latch(latch);
		this.resourcesGroup.init().latch(latch);

		this.playInfoUIObjectGroup.setAnchors(Anchor.TOP_RIGHT, Anchor.TOP_RIGHT);

		this.resourcesGroup.setMargin(this.playInfoUIObjectGroup.getMargin());

		return latch;
	}

	@Override
	public void accept(final LevelDefinition t) {
//		if (this.levelDef != null && this.levelDef.get() == t) {
//			return;
//		}
//
//		this.levelDef = new WeakReference<>(t);
//
//		this.titleObject.ifSet(i -> i.setText(t.getLevelData().getLevelName()).flushText());
//		this.authorObject.ifSet(i -> i.setText(t.getLevelData().getAuthor()).flushText());
//		this.progressBar.setValue(50 / 100f).updateScaling();

		this.playInfoUIObjectGroup.accept(t);
	}

//	@Override
//	public float getMargin() {
//		return this.margin;
//	}
//
//	@Override
//	public void setMargin(final float m) {
//		this.margin = m;
//	}

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
