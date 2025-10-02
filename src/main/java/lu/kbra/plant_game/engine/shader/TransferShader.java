package lu.kbra.plant_game.engine.shader;

import org.joml.Vector3f;

import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.graph.material.Material;
import lu.kbra.standalone.gameengine.graph.shader.RenderShader;
import lu.kbra.standalone.gameengine.graph.shader.annotation.AssociatedShader;
import lu.kbra.standalone.gameengine.graph.shader.part.AbstractShaderPart;
import lu.kbra.standalone.gameengine.impl.Renderable;
import lu.kbra.standalone.gameengine.utils.gl.consts.FaceMode;

public class TransferShader extends RenderShader {

	public static final String MATERIAL_ID = "materialId";
	public static final String OBJECT_ID = "objectId";

	public TransferShader() {
		super(true, AbstractShaderPart.load("classpath:/shaders/gbuffer.vert"),
				AbstractShaderPart.load("classpath:/shaders/gbuffer.frag"));
		setFaceMode(FaceMode.FRONT_AND_BACK);
	}

	@Override
	public void createUniforms() {
		createSceneUniforms();

		createUniform(MATERIAL_ID);
		createUniform(OBJECT_ID);
	}

	@AssociatedShader(TransferShader.class)
	public static class TransferMaterial extends Material {

		private int materialId;
		private Vector3f objectId;

		public TransferMaterial(RenderShader shader) {
			super(TransferMaterial.class.getName(), shader);
		}

		public TransferMaterial(String name, RenderShader shader) {
			super(name, shader);
		}

		@Override
		public void bindProperties(CacheManager cache, Renderable scene) {
			setProperty(MATERIAL_ID, materialId);
			setProperty(OBJECT_ID, objectId);

			super.bindProperties(cache, scene);
		}

		public int getMaterialId() {
			return materialId;
		}

		public void setMaterialId(int materialId) {
			this.materialId = materialId;
		}

		public Vector3f getObjectId() {
			return objectId;
		}

		public void setObjectId(Vector3f objectId) {
			this.objectId = objectId;
		}

	}

}