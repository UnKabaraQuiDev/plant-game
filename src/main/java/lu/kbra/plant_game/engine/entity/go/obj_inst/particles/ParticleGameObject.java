package lu.kbra.plant_game.engine.entity.go.obj_inst.particles;

import java.util.Arrays;
import java.util.function.Function;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector3ic;

import lu.kbra.plant_game.engine.entity.go.factory.GameObjectFactory;
import lu.kbra.plant_game.engine.entity.go.factory.GameObjectFactory.InstanceData;
import lu.kbra.plant_game.engine.entity.go.impl.InstanceGameObject;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.plant_game.generated.ColorMaterial;
import lu.kbra.standalone.gameengine.cache.attrib.Vec3fPaddedAttribArray;
import lu.kbra.standalone.gameengine.geom.instance.InstanceEmitter;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.TaskFuture;
import lu.kbra.standalone.gameengine.scene.Scene3D;
import lu.kbra.standalone.gameengine.utils.gl.consts.BufferType;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

@DataPath("classpath:/models/cube.json")
public class ParticleGameObject extends InstanceGameObject {

	public ParticleGameObject(final String str, final InstanceEmitter instanceEmitter, final short materialId) {
		super(str, instanceEmitter, materialId);
	}

	public ParticleGameObject(final String str, final InstanceEmitter instanceEmitter, final Transform3D transform,
			final Vector3ic objectId, final short materialId) {
		super(str, instanceEmitter, transform, objectId, materialId);
	}

	public ParticleGameObject(final String str, final InstanceEmitter instanceEmitter, final Transform3D transform,
			final Vector3ic objectId) {
		super(str, instanceEmitter, transform, objectId);
	}

	public ParticleGameObject(final String str, final InstanceEmitter instanceEmitter, final Transform3D transform,
			final short materialId) {
		super(str, instanceEmitter, transform, materialId);
	}

	public ParticleGameObject(final String str, final InstanceEmitter instanceEmitter, final Transform3D transform) {
		super(str, instanceEmitter, transform);
	}

	public ParticleGameObject(final String str, final InstanceEmitter instanceEmitter) {
		super(str, instanceEmitter);
	}

	public static final TaskFuture<?, GravityParticleGameObject> createGravity(
			final Dispatcher workers,
			final Scene3D scene,
			final int count,
			final ColorMaterial color,
			final Transform3D transform,
			final boolean hasMinY,
			final float minY,
			final float density,
			final Function<Integer, Vector3f> velocity,
			final Function<Integer, Vector3f> acceleration,
			final Function<Integer, Vector3f> position,
			final Function<Integer, Quaternionf> rotation,
			final Function<Integer, Float> scale) {

		final Vector3f[] accArr = new Vector3f[count];
		final Vector3f[] velArr = new Vector3f[count];

		return new TaskFuture<Void, Object>(workers, (Runnable) () -> {
			for (int i = 0; i < count; i++) {
				if (velocity != null) {
					velArr[i] = velocity.apply(i);
				}
				if (acceleration != null) {
					accArr[i] = acceleration.apply(i);
				}
			}
		}).then(GameObjectFactory.create(GravityParticleGameObject.class,
				scene,
				new InstanceData(i -> new Transform3D(position.apply(i), rotation.apply(i), new Vector3f(scale.apply(i))),
						count,
						Arrays.asList(
								() -> new Vec3fPaddedAttribArray("velocity",
										GravityParticleGameObject.VELOCITY_BUFFER_INDEX,
										velArr,
										BufferType.ARRAY,
										true,
										1,
										4),
								() -> new Vec3fPaddedAttribArray("acceleration",
										GravityParticleGameObject.ACCELERATION_BUFFER_INDEX,
										accArr,
										BufferType.ARRAY,
										true,
										1,
										4))),
				transform,
				color.getId(),
				true,
				hasMinY,
				minY,
				density));
	}

}
