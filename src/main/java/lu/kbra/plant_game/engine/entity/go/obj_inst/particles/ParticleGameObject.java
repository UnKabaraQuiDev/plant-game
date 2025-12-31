package lu.kbra.plant_game.engine.entity.go.obj_inst.particles;

import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.IntFunction;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import lu.kbra.plant_game.engine.entity.go.GOCreatingTaskFuture;
import lu.kbra.plant_game.engine.entity.go.factory.GameObjectFactory;
import lu.kbra.plant_game.engine.entity.go.impl.InstanceGameObject;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.plant_game.generated.ColorMaterial;
import lu.kbra.standalone.gameengine.cache.attrib.Vec3fPaddedAttribArray;
import lu.kbra.standalone.gameengine.geom.instance.InstanceEmitter;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.TaskFuture;
import lu.kbra.standalone.gameengine.scene.Scene3D;
import lu.kbra.standalone.gameengine.utils.GameEngineUtils;
import lu.kbra.standalone.gameengine.utils.gl.consts.BufferType;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

@DataPath("classpath:/models/cube.json")
public class ParticleGameObject extends InstanceGameObject {

	public ParticleGameObject(final String str, final InstanceEmitter instanceEmitter) {
		super(str, instanceEmitter);
	}

	public static final GOCreatingTaskFuture<GravityParticleGameObject> createFloating(
			final Dispatcher workers,
			final Scene3D scene,
			final int count,
			final ColorMaterial color,
			final Transform3D transform,
			final float density,
			final IntFunction<Float> acceleration,
			final IntFunction<Vector3f> position,
			final IntFunction<Quaternionf> rotation,
			final IntFunction<Float> scale) {

		final Vector3f[] accArr = new Vector3f[count];

		final IntFunction<Vector3f> pos = position == null ? i -> new Vector3f() : position;
		final IntFunction<Quaternionf> rot = rotation == null ? i -> new Quaternionf() : rotation;
		final IntFunction<Float> sc = scale == null ? i -> 1f : scale;

		return new TaskFuture<Void, Object>(workers, (Runnable) () -> {
			for (int i = 0; i < count; i++) {
				if (acceleration != null) {
					accArr[i] = GameEngineUtils.randomVector3f().mul(acceleration.apply(i));
				}
			}
		}).then(GameObjectFactory.createInstances(GravityParticleGameObject.class,
				i -> new Transform3D(pos.apply(i), rot.apply(i), new Vector3f(sc.apply(i))),
				OptionalInt.of(count),
				Optional.of(GravityParticleGameObject.class.getSimpleName() + "#" + System.nanoTime()),
				() -> new Vec3fPaddedAttribArray("velocity",
						GravityParticleGameObject.VELOCITY_BUFFER_INDEX,
						new Vector3f[count],
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
						4)))
				.set(i -> i.setMaterial(color))
				.set(i -> i.setTransform(transform))
				.set(i -> i.setEnforceMinY(false))
				.set(i -> i.setApplyDrag(true))
				.set(i -> i.setDensity(density))
				.add(scene);
	}

	public static final GOCreatingTaskFuture<GravityParticleGameObject> createGravity(
			final Dispatcher workers,
			final Scene3D scene,
			final int count,
			final ColorMaterial color,
			final Transform3D transform,
			final boolean hasMinY,
			final float minY,
			final float density,
			final IntFunction<Vector3f> velocity,
			final IntFunction<Vector3f> acceleration,
			final IntFunction<Vector3f> position,
			final IntFunction<Quaternionf> rotation,
			final IntFunction<Float> scale) {

		final Vector3f[] accArr = new Vector3f[count];
		final Vector3f[] velArr = new Vector3f[count];

		final IntFunction<Vector3f> pos = position == null ? i -> new Vector3f() : position;
		final IntFunction<Quaternionf> rot = rotation == null ? i -> new Quaternionf() : rotation;
		final IntFunction<Float> sc = scale == null ? i -> 1f : scale;

		return new TaskFuture<Void, Object>(workers, (Runnable) () -> {
			for (int i = 0; i < count; i++) {
				if (velocity != null) {
					velArr[i] = velocity.apply(i);
				}
				if (acceleration != null) {
					accArr[i] = acceleration.apply(i);
				}
			}
		}).then(GameObjectFactory.createInstances(GravityParticleGameObject.class,
				i -> new Transform3D(pos.apply(i), rot.apply(i), new Vector3f(sc.apply(i))),
				OptionalInt.of(count),
				Optional.of(GravityParticleGameObject.class.getSimpleName() + "#" + System.nanoTime()),
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
						4)))
				.set(i -> i.setMaterial(color))
				.set(i -> i.setTransform(transform))
				.set(i -> i.setEnforceMinY(hasMinY))
				.set(i -> i.setMinY(minY))
				.set(i -> i.setDensity(density))
				.add(scene);
	}

}
