package lu.kbra.plant_game;

public class UpdateFrameState {

	public boolean uiSceneCaughtMouseInput;
	public boolean uiSceneCaughtKeyboardInput;

	public void reset() {
		uiSceneCaughtMouseInput = false;
		uiSceneCaughtKeyboardInput = false;
	}

	@Override
	public String toString() {
		return "UpdateFrameState [uiSceneCaughtMouseInput=" + uiSceneCaughtMouseInput + ", uiSceneCaughtKeyboardInput="
				+ uiSceneCaughtKeyboardInput + "]";
	}

}
