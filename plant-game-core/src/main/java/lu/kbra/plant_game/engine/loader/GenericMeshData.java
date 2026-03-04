package lu.kbra.plant_game.engine.loader;

import org.joml.Vector3f;

/**
 * Parsed mesh definition.
 * <p>
 * This record is used by the object provider system and the existing mesh
 * loaders.
 */
public record GenericMeshData(
		String filePath,
		Vector3f origin,
		boolean textureMaterial,
		String texturePath,
		float deformRatio,
		float speedRatio,
		boolean bloomTextureMaterial,
		String bloomTexturePath,
		float bloomStrength) {
}
