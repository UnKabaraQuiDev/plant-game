package lu.kbra.plant_game.engine.scene.ui.layout;

public enum Anchor2D {

	/**
	 * TOP, LEFT
	 */
	LEADING(-1),
	/**
	 * CENTER
	 */
	CENTER(0),
	/**
	 * BOTTOM, RIGHT
	 */
	TRAILING(1);

	public final float t;

	private Anchor2D(final float t) {
		this.t = t;
	}

	public float getT() {
		return this.t;
	}

	static Anchor2D byT(final int x) {
		return switch (x) {
		case -1 -> LEADING;
		case 0 -> CENTER;
		case 1 -> TRAILING;
		default -> throw new IllegalArgumentException("Unexpected value: " + x);
		};
	}

}
