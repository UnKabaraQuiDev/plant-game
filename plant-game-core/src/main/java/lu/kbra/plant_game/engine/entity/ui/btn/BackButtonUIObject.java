package lu.kbra.plant_game.engine.entity.ui.btn;

import org.joml.Vector3f;
import org.joml.Vector3fc;

import lu.kbra.pclib.logger.GlobalLogger;
import lu.kbra.plant_game.engine.entity.impl.AnchorOwner;
import lu.kbra.plant_game.engine.entity.impl.MarginOwner;
import lu.kbra.plant_game.engine.entity.impl.NeedsClick;
import lu.kbra.plant_game.engine.entity.impl.UISceneParentAware;
import lu.kbra.plant_game.engine.entity.ui.text.GrowOnHoverTextUIObject;
import lu.kbra.plant_game.engine.scene.menu.main.MainMenuUIScene;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.standalone.gameengine.GameEngine;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;

@DataPath("localization:btn.back")
public class BackButtonUIObject extends GrowOnHoverTextUIObject implements NeedsClick, UISceneParentAware, AnchorOwner, MarginOwner {

	private static final Vector3fc TARGET_SCALE = new Vector3f(1.2f, 1, 0.8f);

	protected Anchor objectAnchor;
	protected Anchor targetAnchor;
	protected float margin;

	public BackButtonUIObject(final String str, final TextEmitter text) {
		super(str, text);
	}

	@Override
	public Vector3fc getTargetScale(final boolean grow) {
		return grow ? TARGET_SCALE : GameEngine.IDENTITY_VECTOR3F;
	}

	@Override
	public void click(final WindowInputHandler input) {
		this.getUISceneParent()
				.filter(MainMenuUIScene.class::isInstance)
				.map(MainMenuUIScene.class::cast)
				.ifPresentOrElse(c -> c.startTransition(MainMenuUIScene.BACK_INDICES[c.getCurrentGroup()]),
						() -> GlobalLogger.severe("No " + MainMenuUIScene.class.getSimpleName() + " found in hierarchy."));
	}

	@Override
	public Anchor getObjectAnchor() {
		return this.objectAnchor;
	}

	@Override
	public void setObjectAnchor(final Anchor objectAnchor) {
		this.objectAnchor = objectAnchor;
	}

	@Override
	public Anchor getTargetAnchor() {
		return this.targetAnchor;
	}

	@Override
	public void setTargetAnchor(final Anchor targetAnchor) {
		this.targetAnchor = targetAnchor;
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
		return "BackButtonUIObject@" + System.identityHashCode(this) + " [objectAnchor=" + this.objectAnchor + ", targetAnchor="
				+ this.targetAnchor + ", margin=" + this.margin + ", dir=" + this.dir + ", progress=" + this.progress + ", transform="
				+ this.transform + ", active=" + this.active + ", name=" + this.name + "]";
	}

}
