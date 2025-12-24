package lu.kbra.plant_game.engine.util;

import org.joml.Vector3f;

import lu.kbra.plant_game.engine.mesh.AnimatedLoadedMesh;
import lu.kbra.plant_game.engine.mesh.AnimatedTexturedLoadedMesh;
import lu.kbra.plant_game.engine.mesh.GradientLoadedMesh;
import lu.kbra.plant_game.engine.mesh.OffsetLoadedMesh;
import lu.kbra.plant_game.engine.mesh.SwayLoadedMesh;
import lu.kbra.plant_game.engine.mesh.LoadedTexturedMesh;
import lu.kbra.plant_game.engine.mesh.TexturedSwayLoadedMesh;
import lu.kbra.plant_game.engine.mesh.loader.AnimatedMeshLoader.AnimationData;
import lu.kbra.standalone.gameengine.geom.utils.ObjLoader;
import lu.kbra.standalone.gameengine.graph.material.Material;
import lu.kbra.standalone.gameengine.graph.texture.SingleTexture;

public class DelegatingObjLoader {

	public static OffsetLoadedMesh loadOffsetMesh(final String name, final Material material, final String path, final Vector3f origin) {
		return ObjLoader
				.loadMesh(name, material, path, t -> new OffsetLoadedMesh(t.name(), null, origin, t.vertices(), t.indices(), t.attribs()));
	}

	public static SwayLoadedMesh loadSwayMesh(
			final String name,
			final Material material,
			final String path,
			final float deformRatio,
			final float speedRatio) {
		return ObjLoader
				.loadMesh(name,
						material,
						path,
						t -> new SwayLoadedMesh(t.name(), null, deformRatio, speedRatio, t.vertices(), t.indices(), t.attribs()));
	}

	public static AnimatedLoadedMesh loadAnimatedMesh(
			final String name,
			final Material material,
			final String path,
			final Vector3f origin,
			final AnimationData animationData) {
		return ObjLoader
				.loadMesh(name,
						material,
						path,
						t -> new AnimatedLoadedMesh(t.name(), null, origin, animationData, t.vertices(), t.indices(), t.attribs()));
	}

	public static LoadedTexturedMesh loadTexturedMesh(
			final String name,
			final Material material,
			final String path,
			final Vector3f origin,
			final SingleTexture texture) {
		return ObjLoader
				.loadMesh(name,
						material,
						path,
						t -> new LoadedTexturedMesh(t.name(), null, origin, texture, t.vertices(), t.indices(), t.attribs()));
	}

	public static TexturedSwayLoadedMesh loadTexturedSwayMesh(
			final String name,
			final Material material,
			final String path,
			final float deformRatio,
			final float speedRatio,
			final SingleTexture texture) {
		return ObjLoader
				.loadMesh(name,
						material,
						path,
						t -> new TexturedSwayLoadedMesh(
								t.name(),
								null,
								deformRatio,
								speedRatio,
								texture,
								t.vertices(),
								t.indices(),
								t.attribs()));
	}

	public static AnimatedTexturedLoadedMesh loadAnimatedTexturedMesh(
			final String name,
			final Material material,
			final String path,
			final Vector3f origin,
			final SingleTexture texture,
			final AnimationData animationData) {
		return ObjLoader
				.loadMesh(name,
						material,
						path,
						t -> new AnimatedTexturedLoadedMesh(
								t.name(),
								null,
								origin,
								texture,
								animationData,
								t.vertices(),
								t.indices(),
								t.attribs()));
	}

	public static GradientLoadedMesh loadGradientMesh(final String name, final Material material, final String path) {
		return ObjLoader
				.loadMesh(name, material, path, t -> new GradientLoadedMesh(name, material, t.vertices(), t.indices(), t.attribs()));
	}

}
