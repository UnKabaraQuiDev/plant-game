package lu.kbra.plant_game.engine.entity.impl;

import java.util.ArrayDeque;
import java.util.Deque;

import org.joml.Matrix4f;

import lu.kbra.standalone.gameengine.objs.entity.ParentAwareNode;

public interface AbsoluteTransform3DOwner extends Transform3DOwner, ParentAwareNode {

	default Matrix4f getAbsoluteTransform() {
		final Matrix4f combined = new Matrix4f().identity();

		final Deque<Matrix4f> stack = new ArrayDeque<>();
		Object current = this;
		while (current instanceof ParentAwareNode) {
			if (current instanceof final Transform3DOwner trOwner) {
				final Matrix4f matrix = trOwner.getTransform().getMatrix();
				stack.push(matrix);
			}
			current = ((ParentAwareNode) current).getParent();
		}

		while (!stack.isEmpty()) {
			combined.mul(stack.pop());
		}

		return combined;
	}

	static <T extends Transform3DOwner & ParentAwareNode> Matrix4f getAbsoluteTransform(final T entity) {
		if (entity instanceof AbsoluteTransform3DOwner ato) {
			return ato.getAbsoluteTransform();
		}

		final Matrix4f combined = new Matrix4f().identity();

		final Deque<Matrix4f> stack = new ArrayDeque<>();
		Object current = entity;
		while (current instanceof ParentAwareNode) {
			if (current instanceof final TransformOwner trOwner) {
				final Matrix4f matrix = trOwner.getTransform().getMatrix();
				stack.push(matrix);
			}
			current = ((ParentAwareNode) current).getParent();
		}

		while (!stack.isEmpty()) {
			combined.mul(stack.pop());
		}

		return combined;
	}

}
