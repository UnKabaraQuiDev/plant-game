package lu.kbra.plant_game.engine.scene.ui.overlay;

import java.util.List;

import org.joml.Vector3f;
import org.joml.Vector4f;

import lu.pcy113.pclib.concurrency.ListTriggerLatch;
import lu.pcy113.pclib.concurrency.ObjectTriggerLatch;

import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.entity.ui.factory.UIObjectFactory;
import lu.kbra.plant_game.engine.entity.ui.group.LayoutOffsetUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.impl.Direction2d;
import lu.kbra.plant_game.engine.entity.ui.prim.AnchoredFlatQuadUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.AnchoredProgrammaticTextUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.IntegerTextUIObject;
import lu.kbra.plant_game.engine.entity.ui.texture.MoneyIconUIObject;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor;
import lu.kbra.plant_game.engine.scene.ui.layout.AnchorLayout;
import lu.kbra.plant_game.engine.scene.ui.layout.FlowLayout;
import lu.kbra.plant_game.generated.ColorMaterial;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class BuildingInfoUIObjectGroup extends FixedBoundsUIObjectGroup {

	public static final float FONT_HEIGHT = FixedIntegerStatLine.GAP;
	public static final int COLUMN_COUNT = 25;

	public BuildingInfoUIObjectGroup() {
		super("building-info", new AnchorLayout(), Direction2d.VERTICAL, FONT_HEIGHT * COLUMN_COUNT);
//		this.getTransform().scaleSet(0.25f).update();
	}

	public ObjectTriggerLatch<? extends BuildingInfoUIObjectGroup> init(final Dispatcher workers, final Dispatcher render) {
		System.err.println("creating...");

		final ObjectTriggerLatch<? extends BuildingInfoUIObjectGroup> latch = new ObjectTriggerLatch<>(2, this);

		final LayoutOffsetUIObjectGroup content = new AnchoredLayoutUIObjectGroup(super.getId() + "-content",
				new FlowLayout(true, 0.05f),
				Anchor.CENTER_CENTER,
				Anchor.CENTER_CENTER);
		this.add(content);

		/* cost */
		final ListTriggerLatch<UIObject> cost = new ListTriggerLatch<>(2, (final List<UIObject> list) -> {
			System.err.println(list);
			final BoundedUIObjectGroup costLine = new BoundedUIObjectGroup("cost-line", new AnchorLayout(), content, Direction2d.VERTICAL);
			costLine.addAll(list);
//			content.add(costLine);
			this.doLayout();
		}).latch(latch);

		UIObjectFactory.createText(AnchoredProgrammaticTextUIObject.class, FONT_HEIGHT, "text.cost")
				.set(i -> i.setTransform(new Transform3D()))
				.set(i -> i.setAnchors(Anchor.CENTER_LEFT, Anchor.CENTER_LEFT))
				.latch(cost)
				.postInit(c -> System.err.println("text cost: " + c))
				.push();

		final AnchoredFixedIntegerStatLine costValue = new AnchoredFixedIntegerStatLine("text.cost-value",
				Anchor.CENTER_RIGHT,
				Anchor.CENTER_RIGHT);
		costValue.init(workers, render, FONT_HEIGHT, MoneyIconUIObject.class, IntegerTextUIObject.class)
				.latch(cost)
				.then(AnchoredFixedIntegerStatLine::flushValue);

		/* backdrop */
		UIObjectFactory.create(AnchoredFlatQuadUIObject.class)
				.set(i -> i.setTransform(new Transform3D(new Vector3f(0, -0.1f, 0))))
				.set(i -> i.setColor(new Vector4f(ColorMaterial.DARK_GRAY.getColor()).mul(1, 1, 1, 0.5f)))
				.set(i -> i.setAnchors(Anchor.CENTER_CENTER, Anchor.CENTER_CENTER))
				.latch(latch)
				.postInit(c -> System.err.println("backdrop cost: " + c))
				.push();

		return latch;
	}

}
