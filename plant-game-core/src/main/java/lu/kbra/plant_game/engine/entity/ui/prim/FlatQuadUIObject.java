package lu.kbra.plant_game.engine.entity.ui.prim;

import org.joml.Vector4f;
import org.joml.Vector4fc;

import lu.kbra.plant_game.engine.entity.impl.TintOwner;
import lu.kbra.plant_game.engine.entity.impl.TransparentEntity;
import lu.kbra.plant_game.engine.entity.ui.text.ProgrammaticUIObject;
import lu.kbra.plant_game.engine.mesh.TexturedQuadMesh;
import lu.kbra.plant_game.generated.ColorMaterial;

public class FlatQuadUIObject extends TexturedQuadMeshUIObject implements TintOwner, ProgrammaticUIObject, TransparentEntity {

	protected Vector4f color;

	public FlatQuadUIObject(final String str, final TexturedQuadMesh mesh) {
		super(str, mesh);
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

	@Override
	public String toString() {
		return "FlatQuadUIObject [color=" + this.color + ", bounds=" + this.bounds + ", mesh=" + this.mesh + ", transform=" + this.transform
				+ ", active=" + this.active + ", name=" + this.name + "]";
	}

}
