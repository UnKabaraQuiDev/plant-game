package lu.kbra.plant_game.engine.entity.ui.prim;

import java.awt.geom.Rectangle2D;

import javax.swing.GroupLayout.Alignment;

import lu.kbra.plant_game.engine.entity.impl.QuadMeshOwner;
import lu.kbra.plant_game.engine.entity.ui.GenericUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.ProgrammaticUIObject;
import lu.kbra.standalone.gameengine.geom.QuadMesh;
import lu.kbra.standalone.gameengine.utils.GameEngineUtils;

public class QuadMeshUIObject extends GenericUIObject implements QuadMeshOwner, ProgrammaticUIObject {

	protected Rectangle2D.Float bounds;

	protected QuadMesh mesh;

	public QuadMeshUIObject(final String str, final QuadMesh mesh) {
		super(str);
		this.setQuadMesh(mesh);
	}

	@Override
	public QuadMesh getQuadMesh() {
		return this.mesh;
	}

	@Override
	public void setQuadMesh(final QuadMesh mesh) {
		this.mesh = mesh;
		this.recomputeBounds();
	}

	protected void recomputeBounds() {
		this.bounds = GameEngineUtils.toRectangleBounds(this.getQuadMesh().getSize(), Alignment.CENTER, Alignment.CENTER);
	}

	@Override
	public Rectangle2D.Float getBounds() {
		return this.bounds;
	}

	@Override
	public String toString() {
		return "QuadUIObject [bounds=" + this.bounds + ", mesh=" + this.mesh + ", transform=" + this.transform + ", active=" + this.active
				+ ", name=" + this.name + "]";
	}

}
