package lu.kbra.plant_game.engine.render.shader;

import org.joml.Vector3f;

import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.graph.material.Material;
import lu.kbra.standalone.gameengine.graph.shader.RenderShader;
import lu.kbra.standalone.gameengine.graph.shader.annotation.AssociatedShader;
import lu.kbra.standalone.gameengine.graph.shader.part.AbstractShaderPart;
import lu.kbra.standalone.gameengine.impl.Renderable;
import lu.kbra.standalone.gameengine.utils.gl.consts.PolygonMode;

public class InstanceTransferShader extends RenderShader {

	@Deprecated
	public static final String MATERIAL_ID = "materialId";
	@Deprecated
	public static final String OBJECT_ID = "objectId";
	@Deprecated
	public static final String COMPOSITE_MATERIAL_ID = "compositeMaterialId";
	@Deprecated
	public static final String COMPOSITE_OBJECT_ID = "compositeObjectId";

	public InstanceTransferShader() {
		super(true,
				AbstractShaderPart.load("classpath:/shaders/gbuffer_inst.vert"),
				AbstractShaderPart.load("classpath:/shaders/gbuffer.frag"));
		setFaceMode(PolygonMode.FRONT_AND_BACK);
	}

	@Override
	public void createUniforms() {
		createSceneUniforms();

		createUniform(MATERIAL_ID);
		createUniform(OBJECT_ID);

		createUniform(COMPOSITE_MATERIAL_ID);
		createUniform(COMPOSITE_OBJECT_ID);
	}

	@AssociatedShader(InstanceTransferShader.class)
	@Deprecated
	public static class InstanceTransferMaterial extends Material {

		private int materialId;
		private Vector3f objectId;

		public InstanceTransferMaterial(RenderShader shader) {
			super(InstanceTransferMaterial.class.getName(), shader);
		}

		public InstanceTransferMaterial(String name, RenderShader shader) {
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