package lu.kbra.plant_game.engine.entity.ui.layout;

import java.awt.geom.Rectangle2D;

import javax.swing.GroupLayout.Alignment;

import org.joml.Vector2f;
import org.joml.Vector2fc;

import lu.kbra.plant_game.engine.entity.impl.DebugBoundsColor;
import lu.kbra.plant_game.engine.entity.impl.NoMeshObject;
import lu.kbra.plant_game.engine.entity.ui.GenericUIObject;
import lu.kbra.plant_game.engine.entity.ui.factory.UIObjectFactory;
import lu.kbra.plant_game.engine.entity.ui.factory.UOCreatingTaskFuture;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.plant_game.generated.ColorMaterial;
import lu.kbra.standalone.gameengine.utils.GameEngineUtils;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

@DataPath("")
public class SpacerUIObject extends GenericUIObject implements NoMeshObject, DebugBoundsColor {

	protected Rectangle2D.Float bounds = new Rectangle2D.Float(0, 0, 0, 0);
	private Vector2f size;

	public SpacerUIObject(final String str) {
		super(str);
	}

	public void setSize(final Vector2f size) {
		this.size = size;
		this.recomputeBounds();
	}

	public Vector2fc getSize() {
		return this.size;
	}

	public void recomputeBounds() {
		this.bounds.setFrame(GameEngineUtils.toRectangleBounds(this.size, Alignment.CENTER, Alignment.CENTER));
	}

	@Override
	public Rectangle2D.Float getBounds() {
		return this.bounds;
	}

	@Override
	public ColorMaterial getBoundsColor() {
		return ColorMaterial.GREEN;
	}

	public static UOCreatingTaskFuture<SpacerUIObject> createVerticalSpacer(final float f) {
		return UIObjectFactory.create(SpacerUIObject.class)
				.set(i -> i.setTransform(new Transform3D()))
				.set(i -> i.setSize(new Vector2f(1f, f)));
	}

	public static UOCreatingTaskFuture<SpacerUIObject> createHorizontalSpacer(final float f) {
		return UIObjectFactory.create(SpacerUIObject.class)
				.set(i -> i.setTransform(new Transform3D()))
				.set(i -> i.setSize(new Vector2f(f, 1f)));
	}

	public static UOCreatingTaskFuture<SpacerUIObject> createSpacer(final float x, final float y) {
		return UIObjectFactory.create(SpacerUIObject.class)
				.set(i -> i.setTransform(new Transform3D()))
				.set(i -> i.setSize(new Vector2f(x, y)));
	}

	public static UOCreatingTaskFuture<SpacerUIObject> createSpacer(final float s) {
		return UIObjectFactory.create(SpacerUIObject.class)
				.set(i -> i.setTransform(new Transform3D()))
				.set(i -> i.setSize(new Vector2f(s)));
	}

	public static SpacerUIObject getVerticalSpacer(final float f) {
		return createVerticalSpacer(f).exec();
	}

	public static SpacerUIObject getHorizontalSpacer(final float f) {
		return createHorizontalSpacer(f).exec();
	}

	public static SpacerUIObject getSpacer(final float x, final float y) {
		return createSpacer(x, y).exec();
	}

	public static SpacerUIObject getSpacer(final float s) {
		return createSpacer(s).exec();
	}

}
