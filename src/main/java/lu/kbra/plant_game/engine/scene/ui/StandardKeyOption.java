package lu.kbra.plant_game.engine.scene.ui;

import org.lwjgl.glfw.GLFW;

public enum StandardKeyOption implements KeyOption {

	FORWARD(GLFW.GLFW_KEY_W),
	BACKWARD(GLFW.GLFW_KEY_S),
	LEFT(GLFW.GLFW_KEY_A),
	RIGHT(GLFW.GLFW_KEY_D),
	ROTATE_LEFT(GLFW.GLFW_KEY_Q),
	ROTATE_RIGHT(GLFW.GLFW_KEY_E),
	TURN_CCW(GLFW.GLFW_KEY_R),
	TURN_CW(GLFW.GLFW_KEY_T),
	PLACE(GLFW.GLFW_MOUSE_BUTTON_LEFT),
	CANCEL(GLFW.GLFW_MOUSE_BUTTON_RIGHT);

	private final int physicalKey;

	private StandardKeyOption(int physicalKey) {
		this.physicalKey = physicalKey;
	}

	@Override
	public int getPhysicalKey() {
		return physicalKey;
	}

}
