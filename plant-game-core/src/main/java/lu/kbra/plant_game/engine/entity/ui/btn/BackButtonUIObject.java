package lu.kbra.plant_game.engine.entity.ui.btn;

import lu.kbra.plant_game.engine.entity.ui.data.Scale2dDir;
import lu.kbra.plant_game.engine.entity.ui.impl.AnchorOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.MarginOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.NeedsClick;
import lu.kbra.plant_game.engine.entity.ui.impl.UISceneParentAware;
import lu.kbra.plant_game.engine.entity.ui.text.GrowOnHoverTextUIObject;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.plant_game.vanilla.scene.menu.main.MainMenuUIScene;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;

@DataPath("localization:btn.back")
public class BackButtonUIObject extends GrowOnHoverTextUIObject implements NeedsClick, UISceneParentAware, AnchorOwner, MarginOwner {

	protected Anchor objectAnchor, targetAnchor;
	protected float margin;

	public BackButtonUIObject(final String str, final TextEmitter text) {
		super(str, text);
		this.setDir(Scale2dDir.HORIZONTAL);
	}

	@Override
	public void click(final WindowInputHandler input) {
		this.getUISceneParent()
				.filter(MainMenuUIScene.class::isInstance)
				.map(MainMenuUIScene.class::cast)
				.ifPresent(c -> c.startTransition(MainMenuUIScene.MAIN));
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
