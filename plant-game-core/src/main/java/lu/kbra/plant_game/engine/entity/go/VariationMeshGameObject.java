package lu.kbra.plant_game.engine.entity.go;

import org.joml.Vector3f;
import org.joml.Vector3fc;

import lu.kbra.plant_game.engine.entity.impl.VariationOwner;
import lu.kbra.standalone.gameengine.GameEngine;
import lu.kbra.standalone.gameengine.geom.Mesh;

public class VariationMeshGameObject extends MeshGameObject implements VariationOwner {

	protected Vector3f minVariation = new Vector3f(MIN_DEV);
	protected Vector3f maxVariation = new Vector3f(MAX_DEV);
	protected Vector3f variationCellSize = new Vector3f(GameEngine.IDENTITY_VECTOR3F);
	protected boolean hasVariation = true;

	public VariationMeshGameObject(final String str, final Mesh mesh) {
		super(str, mesh);
	}

	@Override
	public Vector3fc getMinVariation() {
		return this.minVariation;
	}

	public void setMinVariation(final Vector3fc minVariation) {
		if (this.minVariation == null) {
			this.minVariation = new Vector3f(minVariation);
		} else {
			this.minVariation.set(minVariation);
		}
	}

	@Override
	public Vector3fc getMaxVariation() {
		return this.maxVariation;
	}

	public void setMaxVariation(final Vector3fc maxVariation) {
		if (this.maxVariation == null) {
			this.maxVariation = new Vector3f(maxVariation);
		} else {
			this.maxVariation.set(maxVariation);
		}
	}

	@Override
	public Vector3fc getVariationCellSize() {
		return this.variationCellSize;
	}

	public void setVariationCellSize(final Vector3fc variationCellSize) {
		if (this.variationCellSize == null) {
			this.variationCellSize = new Vector3f(variationCellSize);
		} else {
			this.variationCellSize.set(variationCellSize);
		}
	}

	public boolean isHasVariation() {
		return this.hasVariation;
	}

	public void setHasVariation(final boolean hasVariation) {
		this.hasVariation = hasVariation;
	}

	@Override
	public String toString() {
		return "VariationMeshGameObject@" + System.identityHashCode(this) + " [minVariation=" + this.minVariation + ", maxVariation="
				+ this.maxVariation + ", variationCellSize=" + this.variationCellSize + ", hasVariation=" + this.hasVariation
				+ ", materialId=" + this.materialId + ", isEntityMaterialId=" + this.isEntityMaterialId + ", objectId=" + this.objectId
				+ ", objectIdLocation=" + this.objectIdLocation + ", mesh=" + this.mesh + ", transform=" + this.transform + ", active="
				+ this.active + ", name=" + this.name + "]";
	}

}
