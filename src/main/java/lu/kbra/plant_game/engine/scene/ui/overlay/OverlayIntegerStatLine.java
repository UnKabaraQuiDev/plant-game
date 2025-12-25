package lu.kbra.plant_game.engine.scene.ui.overlay;

import java.util.Collection;
import java.util.Comparator;
import java.util.function.Consumer;

import lu.pcy113.pclib.concurrency.FutureTriggerLatch;

import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.entity.ui.factory.UIObjectFactory;
import lu.kbra.plant_game.engine.entity.ui.factory.UIObjectFactory.TextData;
import lu.kbra.plant_game.engine.entity.ui.group.LayoutOffsetUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.group.ObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.group.UIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.layout.SpacerUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.IntegerTextUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.SignedIntegerTextUIObject;
import lu.kbra.plant_game.engine.entity.ui.texture.TextureUIObject;
import lu.kbra.plant_game.engine.scene.ui.layout.FlowLayout;
import lu.kbra.plant_game.generated.ColorMaterial;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextAlignment;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class OverlayIntegerStatLine extends LayoutOffsetUIObjectGroup {

	public static final float POPUP_TEXT_SCALE = 0.6f;
	public static final int MAX_ITEMS = 4;
	public static final int VALUE_LENGTH = 5, POPUP_LENGTH = 3;
	public static final ColorMaterial DEFAULT_TEXT_COLOR = ColorMaterial.WHITE;
	public static final float gap = 0.08f;

	protected TextureUIObject icon;
	protected IntegerTextUIObject value;
	protected SignedIntegerTextUIObject popup;

	protected Comparator<UIObject> comparator = (o1, o2) -> {
		if (o1 == o2) {
			return 0;
		}

		final int r1 = (o1 == this.icon) ? 0
				: (o1 == this.value) ? 1
				: (o1 instanceof SpacerUIObject) ? 2
				: (o1 == this.popup) ? 3
				: 4;

		final int r2 = (o2 == this.icon) ? 0
				: (o2 == this.value) ? 1
				: (o2 instanceof SpacerUIObject) ? 2
				: (o2 == this.popup) ? 3
				: 4;

		assert r1 != 4 : o1;
		assert r2 != 4 : o2;

		return Integer.compare(r1, r2);
	};

	public OverlayIntegerStatLine(final String str, final Transform3D transform, final UIObject... values) {
		super(str, new FlowLayout(false, gap), transform, values);
	}

	public OverlayIntegerStatLine(final String str, final UIObject... values) {
		super(str, new FlowLayout(false, gap), values);
	}

	public OverlayIntegerStatLine(final String str, final UIObjectGroup parent, final UIObject... values) {
		super(str, new FlowLayout(false, gap), parent, values);
	}

	public <T extends TextureUIObject, V extends IntegerTextUIObject, P extends SignedIntegerTextUIObject> FutureTriggerLatch<OverlayIntegerStatLine> init(
			final Dispatcher workers,
			final Dispatcher render,
			final float height,
			final Class<T> iconClazz,
			final Class<V> valueClazz,
			final Class<P> popupClazz) {
		final TextData td = new TextData(UIObjectFactory.DEFAULT_CHAR_SIZE, TextAlignment.LEFT, VALUE_LENGTH);

		final float iconHeightRatio = height / (float) TextureUIObject.SQUARE_1_UNIT.getBounds2D().getHeight();
		final float textHeightRatio = height / td.getCharSize().y;

		final FutureTriggerLatch<OverlayIntegerStatLine> latch = new FutureTriggerLatch<OverlayIntegerStatLine>(3, this);

		UIObjectFactory.create(iconClazz, new Transform3D().scaleMul(iconHeightRatio)).then(workers, (Consumer<T>) obj -> {
			this.icon = obj;
			this.add(obj);
			latch.countDown();
		}).push();
		UIObjectFactory
				.create(valueClazz,
						td,
						this.getId() + "-value",
						0,
						false, // force sign
						true, // padding
						false, // padding zero
						VALUE_LENGTH,
						DEFAULT_TEXT_COLOR,
						new Transform3D().scaleMul(textHeightRatio))
				.then(workers, (Consumer<V>) obj -> {
					this.value = obj;
					this.add(obj);
					latch.countDown();
				})
				.push();

		td.setBufferSize(POPUP_LENGTH + 1);

//		this.add(UIObjectFactory.createSpacer(height / 2, height));

		UIObjectFactory
				.create(popupClazz,
						td,
						this.getId() + "-popup",
						ColorMaterial.GRAY,
						ColorMaterial.RED,
						ColorMaterial.LIGHT_GREEN,
						new Transform3D().scaleMul(textHeightRatio * POPUP_TEXT_SCALE))
				.then(workers, (Consumer<P>) obj -> {
					this.popup = obj;
					this.add(obj);
					latch.countDown();
				})
				.push();

		return latch;
	}

	@Override
	public void doSort() {
		this.getSubEntitiesComponent().sort(this.comparator);
	}

	public TextureUIObject getIcon() {
		return this.icon;
	}

	public IntegerTextUIObject getValue() {
		return this.value;
	}

	public SignedIntegerTextUIObject getPopup() {
		return this.popup;
	}

	@Override
	public <V extends UIObject> V add(final V e) {
		if (this.size() >= MAX_ITEMS) {
			throw new UnsupportedOperationException("Max. " + MAX_ITEMS + " columns: " + this.getSubEntities());
		}
		return super.add(e);
	}

	@Override
	public boolean addAll(final Collection<? extends UIObject> c) {
		if (this.size() >= MAX_ITEMS || this.size() + c.size() > MAX_ITEMS) {
			throw new UnsupportedOperationException("Max. " + MAX_ITEMS + " columns: " + this.getSubEntities());
		}
		return super.addAll(c);
	}

	@Override
	public <V extends UIObject> V[] addAll(final V... e) {
		if (this.size() >= MAX_ITEMS || this.size() + e.length > MAX_ITEMS) {
			throw new UnsupportedOperationException("Max. " + MAX_ITEMS + " columns: " + this.getSubEntities());
		}
		return super.addAll(e);
	}

	@Override
	public boolean addChildren(final ObjectGroup<? extends UIObject> c) {
		if (this.size() >= MAX_ITEMS || this.size() + c.size() > MAX_ITEMS) {
			throw new UnsupportedOperationException("Max. " + MAX_ITEMS + " columns: " + this.getSubEntities());
		}
		return super.addChildren(c);
	}

}
