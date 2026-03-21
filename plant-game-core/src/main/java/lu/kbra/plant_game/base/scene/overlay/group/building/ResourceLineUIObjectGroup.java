package lu.kbra.plant_game.base.scene.overlay.group.building;

import java.awt.geom.Rectangle2D;

import org.joml.Vector3f;

import lu.kbra.pclib.concurrency.ObjectTriggerLatch;
import lu.kbra.pclib.pointer.ObjectPointer;
import lu.kbra.plant_game.base.scene.overlay.stat_line.integer.AnchoredFixedIntegerStatLine;
import lu.kbra.plant_game.base.scene.overlay.stat_line.integer.FixedIntegerStatLine;
import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.entity.ui.factory.UIObjectFactory;
import lu.kbra.plant_game.engine.entity.ui.group.LayoutOffsetUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.text.IndexedAnchoredProgrammaticTextUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.IntegerTextUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.ProgrammaticTextUIObject;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor;
import lu.kbra.plant_game.engine.scene.ui.layout.FlowLayout;
import lu.kbra.plant_game.engine.scene.ui.layout.Layout;
import lu.kbra.plant_game.engine.scene.world.data.resource.ResourceType;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class ResourceLineUIObjectGroup extends LayoutOffsetUIObjectGroup implements MinBoundsOwner {

	public static final float FONT_HEIGHT = FixedIntegerStatLine.GAP;
	public static final int COLUMN_COUNT = 25;

	protected ResourceType resourceType;

	protected ObjectPointer<ProgrammaticTextUIObject> textObject = new ObjectPointer<>();
	protected AnchoredFixedIntegerStatLine valueObject;

	public ResourceLineUIObjectGroup(final String str, final Layout layout, final ResourceType resourceType) {
		super(str, layout);
		this.resourceType = resourceType;
	}

	public ResourceLineUIObjectGroup(final String str, final ResourceType resourceType) {
		super(str, new FlowLayout(false, 0.05f));
		this.resourceType = resourceType;
	}

	public ObjectTriggerLatch<? extends ResourceLineUIObjectGroup> init() {
		final ObjectTriggerLatch<? extends ResourceLineUIObjectGroup> latch = new ObjectTriggerLatch<>(2, this);

		this.valueObject = new AnchoredFixedIntegerStatLine(this.getId() + "-value", 0f, Anchor.CENTER_RIGHT, Anchor.CENTER_RIGHT);
		this.add(this.valueObject);

		UIObjectFactory.createText(IndexedAnchoredProgrammaticTextUIObject.class, FONT_HEIGHT, this.getResourceType().getLocalizationKey())
				.set(i -> i.setTransform(new Transform3D(new Vector3f(0, 0.2f, 0))))
				.set(i -> i.setAnchors(Anchor.CENTER_LEFT, Anchor.CENTER_LEFT))
				.set(i -> i.setIndex(-1)) // before the other
				.get(this.textObject)
				.add(this)
				.latch(latch)
				.push();

		this.valueObject.init(FONT_HEIGHT, this.getResourceType().getIconClass(), IntegerTextUIObject.class)
				.latch(latch)
				.then(AnchoredFixedIntegerStatLine::flushValue);

		return latch;
	}

	public ObjectTriggerLatch<? extends ResourceLineUIObjectGroup> init(final float fontSize) {
		final ObjectTriggerLatch<? extends ResourceLineUIObjectGroup> latch = new ObjectTriggerLatch<>(2, this);

		this.valueObject = new AnchoredFixedIntegerStatLine(this.getId() + "-value", 0f, Anchor.CENTER_RIGHT, Anchor.CENTER_RIGHT);
		this.add(this.valueObject);

		UIObjectFactory.createText(IndexedAnchoredProgrammaticTextUIObject.class, fontSize, this.getResourceType().getLocalizationKey())
				.set(i -> i.setTransform(new Transform3D(new Vector3f(0, 0.2f, 0))))
				.set(i -> i.setAnchors(Anchor.CENTER_LEFT, Anchor.CENTER_LEFT))
				.set(i -> i.setIndex(-1)) // before the other
				.get(this.textObject)
				.add(this)
				.latch(latch)
				.push();

		this.valueObject.init(FONT_HEIGHT, this.getResourceType().getIconClass(), IntegerTextUIObject.class)
				.latch(latch)
				.then(AnchoredFixedIntegerStatLine::flushValue);

		return latch;
	}

	@Override
	public Rectangle2D getMinBounds() {
		float width = 0;
		float height = 0;
		for (UIObject obj : this) {
			final Rectangle2D rect = obj.getLocalTransformedBounds().getBounds2D();
			width += rect.getWidth();
			height += rect.getHeight();
		}
		return new Rectangle2D.Float(-width / 2, -height, width, height);
	}

	public int getValue() {
		return this.valueObject.getValue();
	}

	public ResourceLineUIObjectGroup set(final int value) {
		this.valueObject.set(value);
		return this;
	}

	public ResourceLineUIObjectGroup flushValue() {
		this.valueObject.flushValue();
		return this;
	}

	public AnchoredFixedIntegerStatLine getValueObject() {
		return this.valueObject;
	}

	public ObjectPointer<ProgrammaticTextUIObject> getTextObject() {
		return this.textObject;
	}

	public ResourceType getResourceType() {
		return this.resourceType;
	}

	public void setResourceType(final ResourceType resourceType) {
		this.resourceType = resourceType;
	}

	@Override
	public String toString() {
		return "ResourceLineUIObjectGroup@" + System.identityHashCode(this) + " [resourceType=" + this.resourceType + ", textObject="
				+ this.textObject + ", valueObject=" + this.valueObject + ", layout=" + this.layout + ", subEntitiesLock="
				+ this.subEntitiesLock + ", subEntities=" + this.subEntities + ", computedBounds=" + this.computedBounds + ", transform="
				+ this.transform + ", active=" + this.active + ", name=" + this.name + "]";
	}

}
