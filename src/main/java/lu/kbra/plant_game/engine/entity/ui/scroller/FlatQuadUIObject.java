package lu.kbra.plant_game.engine.entity.ui.scroller;

import org.joml.Vector4f;
import org.joml.Vector4fc;

import lu.kbra.plant_game.engine.entity.ui.prim.QuadUIObject;
import lu.kbra.plant_game.engine.mesh.TexturedQuadMesh;
import lu.kbra.plant_game.engine.mesh.TintOwner;
import lu.kbra.plant_game.generated.ColorMaterial;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class FlatQuadUIObject extends QuadUIObject implements TintOwner {

	protected Vector4f color;

	public FlatQuadUIObject(final String str, final TexturedQuadMesh mesh) {
		super(str, mesh);
	}

	public FlatQuadUIObject(final String str, final TexturedQuadMesh mesh, final Transform3D transform) {
		super(str, mesh, transform);
	}

	public FlatQuadUIObject(final String str, final TexturedQuadMesh mesh, final Vector4f color) {
		super(str, mesh);
		this.color = color;
	}

	public FlatQuadUIObject(final String str, final TexturedQuadMesh mesh, final Transform3D transform, final Vector4f color) {
		super(str, mesh, transform);
		this.color = color;
	}

	public Vector4f getColor() {
		return this.color;
	}

	public void setColor(final Vector4f color) {
		this.color = color;
	}

	@Override
	public Vector4fc getTint() {
		return this.color;
	}

	@Override
	public void setTint(final Vector4fc tint) {
		if (tint == null) {
			this.setTint(ColorMaterial.BLACK.getColor());
			return;
		}

		if (this.color == null) {
			this.color = new Vector4f(tint);
		} else {
			this.color.set(tint);
		}
	}

}
