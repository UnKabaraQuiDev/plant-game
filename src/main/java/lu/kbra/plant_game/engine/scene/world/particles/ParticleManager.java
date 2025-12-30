package lu.kbra.plant_game.engine.scene.world.particles;

import java.util.HashSet;
import java.util.Set;

import org.joml.Vector3i;
import org.joml.Vector3ic;

import lu.kbra.plant_game.engine.entity.go.obj_inst.particles.GravityParticleGameObject;
import lu.kbra.plant_game.engine.render.DeferredCompositor;
import lu.kbra.plant_game.engine.scene.world.particles.shader.GravityParticleComputeShader;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.generated.gl_wrapper.GL_W;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.geom.instance.InstanceEmitter;
import lu.kbra.standalone.gameengine.scene.Scene3D;
import lu.kbra.standalone.gameengine.utils.gl.consts.BufferType;

public class ParticleManager {

	public static final String GL_FORCE_SYNC_COMPUTE_SHADERS_PROPERTY = DeferredCompositor.class.getSimpleName()
			+ ".gl_force_sync_compute_shaders";
	public static final boolean GL_FORCE_SYNC_COMPUTE_SHADERS = Boolean.getBoolean(GL_FORCE_SYNC_COMPUTE_SHADERS_PROPERTY);

	protected int maxActiveParticleEmitters = 32;

	protected Scene3D scene;

	protected final Object objectLocks = new Object();
	protected Set<GravityParticleGameObject> activeObjects = new HashSet<>();
	protected Set<GravityParticleGameObject> inactiveObjects = new HashSet<>();

	protected GravityParticleComputeShader computeShader;

	public ParticleManager(final CacheManager cache, final Scene3D scene) {
		this.scene = scene;

		cache.addAbstractShader(this.computeShader = new GravityParticleComputeShader());
	}

	public void render(final float dTime) {
		this.computeShader.bind();

		this.computeShader.setUniform(GravityParticleComputeShader.D_TIME, dTime);

		for (final GravityParticleGameObject object : this.activeObjects) {

			final InstanceEmitter emitter = object.getInstanceEmitter();
			final Mesh mesh = emitter.getParticleMesh();
			final int count = emitter.getParticleCount();

			final Vector3ic groups = this.computeShader.getGlobalGroup(new Vector3i(count, 0, 0));

			System.err.println(count + " " + groups.x());

			assert groups.x() != 0;

			this.computeShader.setUniformUnsigned(GravityParticleComputeShader.COUNT, count);
			this.computeShader.setUniform(GravityParticleComputeShader.ACCELERATION, object.getAcceleration());
			this.computeShader.setUniform(GravityParticleComputeShader.APPLY_ACCELERATION, object.isApplyAcceleration());
			this.computeShader.setUniform(GravityParticleComputeShader.RESET_ACCELERATION, object.isResetAcceleration());

			GL_W.glBindBufferBase(BufferType.SHADER_STORAGE.getGlId(), 0, object.getVelocityGlId());
			// mesh.getVbo().get(GravityParticleGameObject.ACCELERATION_BUFFER_INDEX)
			GL_W.glBindBufferBase(BufferType.SHADER_STORAGE.getGlId(), 1, object.getAccelerationGlId());
			GL_W.glBindBufferBase(BufferType.SHADER_STORAGE.getGlId(), 2, emitter.getParticleTransforms().getGlId());

			GL_W.glDispatchCompute(groups.x(), 1, 1);

			if (GL_FORCE_SYNC_COMPUTE_SHADERS) {
				GL_W.glMemoryBarrier(GL_W.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT | GL_W.GL_VERTEX_ATTRIB_ARRAY_BARRIER_BIT);
			}
		}

		if (!GL_FORCE_SYNC_COMPUTE_SHADERS) {
			GL_W.glMemoryBarrier(GL_W.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT | GL_W.GL_VERTEX_ATTRIB_ARRAY_BARRIER_BIT);
		}
	}

	public Scene3D getScene() {
		return this.scene;
	}

	public void setScene(final Scene3D scene) {
		this.scene = scene;
	}

	public Set<GravityParticleGameObject> getActiveObjects() {
		return this.activeObjects;
	}

	public Set<GravityParticleGameObject> getInactiveObjects() {
		return this.inactiveObjects;
	}

	public int getTotalObjectCount() {
		return this.activeObjects.size() + this.inactiveObjects.size();
	}

	public int getMaxActiveParticleEmitters() {
		return this.maxActiveParticleEmitters;
	}

}
