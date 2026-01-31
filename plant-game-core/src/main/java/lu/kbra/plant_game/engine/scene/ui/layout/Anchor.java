package lu.kbra.plant_game.engine.scene.ui.layout;

public enum Anchor {

	TOP_LEFT(-1f, -1f),
	TOP_CENTER(0f, -1f),
	TOP_RIGHT(1f, -1f),

	CENTER_LEFT(-1f, 0f),
	CENTER_CENTER(0f, 0f),
	CENTER_RIGHT(1f, 0f),

	BOTTOM_LEFT(-1f, 1f),
	BOTTOM_CENTER(0f, 1f),
	BOTTOM_RIGHT(1f, 1f);

	public final float x;
	public final float z;

	Anchor(final float x, final float z) {
		this.x = x;
		this.z = z;
	}
}
