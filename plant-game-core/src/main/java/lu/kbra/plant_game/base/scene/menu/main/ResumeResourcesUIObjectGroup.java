package lu.kbra.plant_game.base.scene.menu.main;

import java.lang.ref.WeakReference;
import java.util.function.Consumer;

import lu.kbra.pclib.concurrency.ObjectTriggerLatch;
import lu.kbra.plant_game.base.scene.overlay.group.building.ResourceLineUIObjectGroup;
import lu.kbra.plant_game.base.scene.overlay.group.impl.AnchoredLayoutUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.group.UIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.impl.MarginOwner;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor2D;
import lu.kbra.plant_game.engine.scene.ui.layout.FlowLayout;
import lu.kbra.plant_game.engine.scene.world.LevelState;
import lu.kbra.plant_game.plugin.registry.LevelRegistry.LevelDefinition;
import lu.kbra.plant_game.plugin.registry.ResourceRegistry;

public class ResumeResourcesUIObjectGroup extends AnchoredLayoutUIObjectGroup implements MarginOwner, Consumer<LevelDefinition> {

	protected WeakReference<LevelDefinition> levelDef;

	protected float margin;

	public ResumeResourcesUIObjectGroup(final UIObjectGroup parent) {
		super(parent.getId() + "-resources", new FlowLayout(true, 0.1f, Anchor2D.LEADING), parent, Anchor.CENTER_LEFT, Anchor.CENTER_LEFT);
	}

	@Override
	public void accept(final LevelDefinition t) {
		if (this.levelDef != null && this.levelDef.get() == t) {
			return;
		}

		this.levelDef = new WeakReference<>(t);

		if (t.getLevelState() == LevelState.NOT_STARTED || t.getLevelState() == LevelState.LOST) {
			this.stream().filter(ResourceLineUIObjectGroup.class::isInstance).map(ResourceLineUIObjectGroup.class::cast).forEach(c -> {
				if (t.getLevelData().getGame().getStartResources().containsKey(c.getResourceType())) {
					c.getValueObject().set(t.getLevelData().getGame().getStartResources().get(c.getResourceType()));
					c.setActive(true);
				} else {
					c.setActive(false);
				}
			});
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
	public String toString() {
		return "ResumeResourcesUIObjectGroup@" + System.identityHashCode(this) + " [levelDef=" + this.levelDef + ", margin=" + this.margin
				+ ", objectAnchor=" + this.objectAnchor + ", targetAnchor=" + this.targetAnchor + ", layout=" + this.layout
				+ ", subEntitiesLock=" + this.subEntitiesLock + ", subEntities=" + this.subEntities + ", computedBounds="
				+ this.computedBounds + ", transform=" + this.transform + ", active=" + this.active + ", name=" + this.name + "]";
	}

}
