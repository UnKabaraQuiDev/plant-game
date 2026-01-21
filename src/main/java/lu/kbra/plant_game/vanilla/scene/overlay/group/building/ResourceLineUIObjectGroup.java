package lu.kbra.plant_game.vanilla.scene.overlay.group.building;

import lu.kbra.plant_game.engine.entity.ui.data.Direction2d;
import lu.kbra.plant_game.engine.scene.ui.layout.Layout;
import lu.kbra.plant_game.engine.scene.world.data.resource.ResourceType;
import lu.kbra.plant_game.vanilla.scene.overlay.group.impl.BoundedUIObjectGroup;

public class ResourceLineUIObjectGroup extends BoundedUIObjectGroup {

	protected ResourceType resourceType;

	public ResourceLineUIObjectGroup(final String str, final Layout layout, final Direction2d dir, final ResourceType resourceType) {
		super(str, layout, dir);
		this.resourceType = resourceType;
	}

	public ResourceType getResourceType() {
		return this.resourceType;
	}

	public void setResourceType(final ResourceType resourceType) {
		this.resourceType = resourceType;
	}

	@Override
	public String toString() {
		return "ResourceLineUIObjectGroup@" + System.identityHashCode(this) + " [resourceType=" + this.resourceType + ", dir=" + this.dir
				+ ", bounds=" + this.bounds + ", layout=" + this.layout + ", subEntitiesLock=" + this.subEntitiesLock + ", subEntities="
				+ this.subEntities + ", computedBounds=" + this.computedBounds + ", transform=" + this.transform + ", active=" + this.active
				+ ", name=" + this.name + "]";
	}

}
