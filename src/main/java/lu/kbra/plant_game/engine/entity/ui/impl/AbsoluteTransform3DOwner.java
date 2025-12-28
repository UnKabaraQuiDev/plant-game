package lu.kbra.plant_game.engine.entity.ui.impl;

import java.util.ArrayDeque;
import java.util.Deque;

import org.joml.Matrix4f;

import lu.kbra.plant_game.engine.entity.impl.Transform3DOwner;
import lu.kbra.standalone.gameengine.objs.entity.ParentAware;
import lu.kbra.standalone.gameengine.objs.entity.SceneEntity;
import lu.kbra.standalone.gameengine.objs.entity.components.Transform3DComponent;

public interface AbsoluteTransform3DOwner extends Transform3DOwner, ParentAware {

	default Matrix4f getAbsoluteTransform() {
//		final Matrix4f combined = new Matrix4f().identity();
//
//		final Deque<Matrix4f> stack = new ArrayDeque<>();
//		Object current = this;
//		while (current instanceof ParentAware) {
//			if (current instanceof final Transform3DOwner trOwner) {
//				final Matrix4f matrix = trOwner.getTransform().getMatrix();
//				stack.push(matrix);
//			}
//			current = ((ParentAware) current).getParent();
//		}
//
//		while (!stack.isEmpty()) {
//			combined.mul(stack.pop());
//		}
//
//		return combined;

		return getAbsoluteTransform(this);
	}

	static <T extends Transform3DOwner & ParentAware> Matrix4f getAbsoluteTransform(final T entity) {
		final Matrix4f combined = new Matrix4f().identity();

		final Deque<Matrix4f> stack = new ArrayDeque<>();
		Object current = entity;
		while (current instanceof ParentAware) {
			if (current instanceof final Transform3DOwner trOwner) {
				final Matrix4f matrix = trOwner.getTransform().getMatrix();
				stack.push(matrix);
			} else if (current instanceof final SceneEntity sceneEntity && sceneEntity.hasComponentMatching(Transform3DComponent.class)) {
				final Matrix4f matrix = sceneEntity.getComponentMatching(Transform3DComponent.class).getTransform().getMatrix();
				stack.push(matrix);
			}
			current = ((ParentAware) current).getParent();
		}

		while (!stack.isEmpty()) {
			combined.mul(stack.pop());
		}

		return combined;
	}

}
