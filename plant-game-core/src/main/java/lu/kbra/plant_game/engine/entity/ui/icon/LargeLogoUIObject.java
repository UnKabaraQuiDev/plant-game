package lu.kbra.plant_game.engine.entity.ui.icon;

import lu.kbra.plant_game.engine.entity.ui.TexturedQuadMeshUIObject;
import lu.kbra.plant_game.engine.entity.ui.impl.AnchorOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.MarginOwner;
import lu.kbra.plant_game.engine.mesh.TexturedQuadMesh;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.plant_game.engine.util.annotation.TextureOption;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextureFilter;

@DataPath("image:classpath:/icons/large_logo-256.png")
@TextureOption(textureFilter = TextureFilter.LINEAR)
public class LargeLogoUIObject extends TexturedQuadMeshUIObject implements AnchorOwner, MarginOwner {

	protected Anchor objectAnchor;
	protected Anchor targetAnchor;
	protected float margin;

	public LargeLogoUIObject(final String str, final TexturedQuadMesh mesh) {
		super(str, mesh);
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
		return "LargeLogoUIObject@" + System.identityHashCode(this) + " [objectAnchor=" + this.objectAnchor + ", targetAnchor="
				+ this.targetAnchor + ", margin=" + this.margin + ", bounds=" + this.bounds + ", mesh=" + this.mesh + ", transform="
				+ this.transform + ", active=" + this.active + ", name=" + this.name + "]";
	}

}
