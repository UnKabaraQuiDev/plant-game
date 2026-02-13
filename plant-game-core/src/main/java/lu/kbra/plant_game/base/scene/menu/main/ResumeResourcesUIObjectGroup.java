package lu.kbra.plant_game.base.scene.menu.main;

import java.lang.ref.WeakReference;
import java.util.function.Consumer;

import lu.kbra.pclib.concurrency.ObjectTriggerLatch;
import lu.kbra.pclib.logger.GlobalLogger;
import lu.kbra.plant_game.base.scene.overlay.group.building.ResourceLineUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.data.Direction2d;
import lu.kbra.plant_game.engine.entity.ui.group.UIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.impl.DebugBoundsColor;
import lu.kbra.plant_game.engine.entity.ui.impl.MarginOwner;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor2D;
import lu.kbra.plant_game.engine.scene.ui.layout.FlowLayout;
import lu.kbra.plant_game.engine.scene.world.GameData;
import lu.kbra.plant_game.generated.ColorMaterial;
import lu.kbra.plant_game.plugin.registry.LevelRegistry.LevelDefinition;
import lu.kbra.plant_game.plugin.registry.ResourceRegistry;

public class ResumeResourcesUIObjectGroup extends AnchoredFBUIObjectGroup
		/* AnchoredLayoutUIObjectGroup */ implements MarginOwner, Consumer<LevelDefinition>, DebugBoundsColor {

	protected WeakReference<LevelDefinition> levelDef;

	protected float margin;

	public ResumeResourcesUIObjectGroup(final UIObjectGroup parent) {
		super(parent.getId() + "-resources",
				new FlowLayout(true, 0.1f, Anchor2D.LEADING),
				parent,
				Direction2d.VERTICAL,
				2.5f,
				Anchor.BOTTOM_RIGHT,
				Anchor.BOTTOM_RIGHT);
	}

	@Override
	public void accept(final LevelDefinition t) {
		if (this.levelDef != null && this.levelDef.get() == t) {
			return;
		}

		this.levelDef = new WeakReference<>(t);

		if (t.isNew()) {
			this.stream().filter(ResourceLineUIObjectGroup.class::isInstance).map(ResourceLineUIObjectGroup.class::cast).forEach(c -> {
				if (t.getLevelData().getGame().getStartResources().containsKey(c.getResourceType())) {
					c.getValueObject().set(t.getLevelData().getGame().getStartResources().get(c.getResourceType())).flushValue();
					c.setActive(true);
				} else {
					c.setActive(false);
				}
			});
		} else if (t.hasPast()) {
			final GameData go = t.getGameData().get();
			this.stream().filter(ResourceLineUIObjectGroup.class::isInstance).map(ResourceLineUIObjectGroup.class::cast).forEach(c -> {
				if (go.getResources().containsKey(c.getResourceType())) {
					c.getValueObject().set(go.getResources().get(c.getResourceType())).flushValue();
					c.setActive(true);
				} else {
					c.setActive(false);
				}
			});
		} else {
			GlobalLogger.warning("Invalid level state: " + t);
		}
	}

	public ObjectTriggerLatch<? extends ResumeResourcesUIObjectGroup> init() {
		final ObjectTriggerLatch<? extends ResumeResourcesUIObjectGroup> latch = new ObjectTriggerLatch<>(
				ResourceRegistry.RESOURCE_TYPE_DEFS.size(),
				this);

		ResourceRegistry.RESOURCE_TYPE_DEFS.forEach((k, rt) -> {
			new ResourceLineUIObjectGroup("res-" + k, rt).init().then(this::add).latch(latch);
		});

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
	public ColorMaterial getBoundsColor() {
		return ColorMaterial.BROWN;
	}

	@Override
	public String toString() {
		return "ResumeResourcesUIObjectGroup@" + System.identityHashCode(this) + " [levelDef=" + this.levelDef + ", margin=" + this.margin
				+ ", objectAnchor=" + this.objectAnchor + ", targetAnchor=" + this.targetAnchor + ", layout=" + this.layout
				+ ", subEntitiesLock=" + this.subEntitiesLock + ", subEntities=" + this.subEntities + ", computedBounds="
				+ this.computedBounds + ", transform=" + this.transform + ", active=" + this.active + ", name=" + this.name + "]";
	}

}
