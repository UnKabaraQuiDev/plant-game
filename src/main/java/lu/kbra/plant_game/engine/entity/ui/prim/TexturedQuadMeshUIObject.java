package lu.kbra.plant_game.engine.entity.ui.prim;

import java.awt.geom.Rectangle2D;

import javax.swing.GroupLayout.Alignment;

import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.mesh.TexturedQuadMesh;
import lu.kbra.standalone.gameengine.utils.GameEngineUtils;

public class TexturedQuadMeshUIObject extends UIObject implements TexturedQuadMeshOwner {

	protected Rectangle2D.Float bounds;
	protected TexturedQuadMesh mesh;

	public TexturedQuadMeshUIObject(final String str, final TexturedQuadMesh mesh) {
		super(str);
		this.setTexturedQuadMesh(mesh);
	}

	@Override
	public void setTexturedQuadMesh(final TexturedQuadMesh m) {
		this.mesh = m;
		this.recomputeBounds();
	}

	@Override
	public TexturedQuadMesh getTexturedQuadMesh() {
		return this.mesh;
	}

	protected void recomputeBounds() {
		this.bounds = GameEngineUtils.toRectangleBounds(this.mesh.getSize(), Alignment.CENTER, Alignment.CENTER);
	}

	@Override
	public Rectangle2D.Float getBounds() {
		return this.bounds;
	}

	@Override
	public String toString() {
		return "QuadUIObject [bounds=" + this.bounds + ", mesh=" + this.mesh + ", transform=" + this.transform + ", parent=" + this.parent
				+ ", active=" + this.active + ", name=" + this.name + "]";
	}

}
