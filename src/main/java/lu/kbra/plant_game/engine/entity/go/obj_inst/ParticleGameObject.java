package lu.kbra.plant_game.engine.entity.go.obj_inst;

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
import lu.kbra.standalone.gameengine.cache.attrib.Vec3fAttribArray;
import lu.kbra.standalone.gameengine.geom.instance.InstanceEmitter;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.TaskFuture;
import lu.kbra.standalone.gameengine.scene.Scene;
import lu.kbra.standalone.gameengine.utils.gl.consts.BufferType;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

@DataPath("classpath:/models/cube.json")
public class ParticleGameObject extends InstanceGameObject {

	public static final int VELOCITY_BUFFER_INDEX = InstanceEmitter.FIRST_BUFFER_INDEX;
	public static final int ACCELERATION_BUFFER_INDEX = VELOCITY_BUFFER_INDEX + 1;

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
			final String name,
			final Scene scene,
			final int count,
			final ColorMaterial colors,
			final Vector3f gravity,
			final Transform3D transform,
			final Function<Integer, Vector3f> position,
			final Function<Integer, Vector3f> velocity,
			final Function<Integer, Vector3f> acceleration,
			final float scale) {

		final Vector3f[] posArr = new Vector3f[count];
		final Vector3f[] accArr = new Vector3f[count];
		final Vector3f[] velArr = new Vector3f[count];

		return new TaskFuture<Void, Object>(workers, (Runnable) () -> {
			for (int i = 0; i < count; i++) {
				if (position != null) {
					posArr[i] = position.apply(i);
				}
				if (velocity != null) {
					velArr[i] = velocity.apply(i);
				}
				if (acceleration != null) {
					accArr[i] = acceleration.apply(i);
				}
			}
		})
				.then(GameObjectFactory
						.create(GravityParticleGameObject.class,
								scene,
								new InstanceData(
										i -> new Transform3D(
												posArr[i] == null ? new Vector3f() : posArr[i], new Quaternionf(), new Vector3f(scale)),
										count,
										Arrays
												.asList(() -> new Vec3fAttribArray("velocity", ParticleGameObject.VELOCITY_BUFFER_INDEX, 1,
														new Vector3f[20], BufferType.ARRAY, false, 1),
														() -> new Vec3fAttribArray("acceleration",
																ParticleGameObject.ACCELERATION_BUFFER_INDEX, 1, new Vector3f[20],
																BufferType.ARRAY, false, 1))),
								transform,
								gravity));
	}

}
