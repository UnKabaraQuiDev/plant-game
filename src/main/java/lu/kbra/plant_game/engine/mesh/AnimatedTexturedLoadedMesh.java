package lu.kbra.plant_game.engine.mesh;

import java.util.Arrays;

import org.joml.Vector3f;

import lu.kbra.plant_game.engine.loader.AnimatedMeshLoader.AnimationData;
import lu.kbra.standalone.gameengine.cache.attrib.UIntAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.Vec3fAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.impl.AttribArray;
import lu.kbra.standalone.gameengine.graph.material.Material;
import lu.kbra.standalone.gameengine.graph.texture.SingleTexture;

public class AnimatedTexturedLoadedMesh extends OffsetLoadedMesh implements TexturedMesh, AnimatedMesh {

	protected SingleTexture texture;
	protected AnimationData animation;

	public AnimatedTexturedLoadedMesh(
			String name,
			Material mat,
			Vector3f origin,
			SingleTexture texture,
			AnimationData data,
			Vec3fAttribArray vertices,
			UIntAttribArray indices,
			AttribArray... attribs) {
		super(name, mat, origin, vertices, indices, attribs);
		this.texture = texture;
		this.animation = data;
	}

	@Override
	public AnimationData getAnimation() {
		return animation;
	}

	@Override
	public void setAnimation(AnimationData a) {
		this.animation = a;
	}

	@Override
	public SingleTexture getTexture() {
		return texture;
	}

	@Override
	public void setTexture(SingleTexture texture) {
		this.texture = texture;
	}

	@Override
	public String toString() {
		return "AnimatedTexturedLoadedMesh [texture=" + texture + ", animation=" + animation + ", name=" + name + ", vao=" + vao + ", vbo="
				+ vbo + ", material=" + material + ", vertices=" + vertices + ", indices=" + indices + ", attribs="
				+ Arrays.toString(attribs) + ", vertexCount=" + vertexCount + ", indicesCount=" + indicesCount + ", isValid()=" + isValid()
				+ "]";
	}

}
