package lu.kbra.plant_game.vanilla.scene.overlay.group.building;

import lu.kbra.plant_game.engine.entity.ui.impl.MarginOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.NeedsClick;
import lu.kbra.plant_game.engine.entity.ui.impl.UISceneParentAware;
import lu.kbra.plant_game.engine.mesh.TexturedQuadMesh;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.plant_game.engine.util.annotation.TextureOption;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.plant_game.vanilla.scene.overlay.OverlayUIScene;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextureFilter;

@DataPath("image:classpath:/icons/Arrow-128.png")
@TextureOption(textureFilter = TextureFilter.LINEAR)
public class BuildingPanelToggleButtonUIObject extends ExtAnchoredTexturedQuadMeshUIObject
		implements NeedsClick, UISceneParentAware, MarginOwner {

	public BuildingPanelToggleButtonUIObject(final String str, final TexturedQuadMesh mesh) {
		super(str, mesh);
	}

	@Override
	public void click(final WindowInputHandler input) {
		this.getUISceneParent().filter(OverlayUIScene.class::isInstance).map(OverlayUIScene.class::cast).ifPresent(c -> {
			this.getTransform().rotationAdd(0, (float) Math.PI, 0).updateMatrix();
			c.getBuildingPanel().setActive(!c.getBuildingPanel().isActive());
			if (c.getBuildingPanel().isActive()) {
				this.setAnchors(Anchor.BOTTOM_CENTER, Anchor.TOP_CENTER);
				this.setTarget(c.getBuildingPanel());
			} else {
				this.setAnchors(Anchor.BOTTOM_CENTER, Anchor.BOTTOM_CENTER);
				this.setTarget(null);
			}
			c.doLayout();
		});
	}

	@Override
	public String toString() {
		return "BuildingPanelShowButtonUIObject@" + System.identityHashCode(this) + " [target=" + this.target + ", objectAnchor="
				+ this.objectAnchor + ", targetAnchor=" + this.targetAnchor + ", bounds=" + this.bounds + ", mesh=" + this.mesh
				+ ", transform=" + this.transform + ", active=" + this.active + ", name=" + this.name + "]";
	}

	@Override
	public float getMargin() {
		return 0.02f;
	}

	@Override
	public void setMargin(final float m) {
		throw new UnsupportedOperationException();
	}

}
