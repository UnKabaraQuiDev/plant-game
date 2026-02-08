package lu.kbra.plant_game.engine.scene.ui.layout;

public enum Anchor {

	TOP_LEFT(-1, -1),
	TOP_CENTER(0, -1),
	TOP_RIGHT(1, -1),

	CENTER_LEFT(-1, 0),
	CENTER_CENTER(0, 0),
	CENTER_RIGHT(1, 0),

	BOTTOM_LEFT(-1, 1),
	BOTTOM_CENTER(0, 1),
	BOTTOM_RIGHT(1, 1);

	public final int x;
	public final int z;

	Anchor(final int x, final int z) {
		this.x = x;
		this.z = z;
	}

	public int getX() {
		return this.x;
	}

	public int getZ() {
		return this.z;
	}

	Anchor2D getHorizontal() {
		return Anchor2D.byT(this.x);
	}

	Anchor2D getVertical() {
		return Anchor2D.byT(this.z);
	}

	static Anchor byAnchors2D(final Anchor2D x, final Anchor2D z) {
		return switch (z) {
		case LEADING -> switch (x) {
		case LEADING -> TOP_LEFT;
		case CENTER -> TOP_CENTER;
		case TRAILING -> TOP_RIGHT;
		};
		case CENTER -> switch (x) {
		case LEADING -> CENTER_LEFT;
		case CENTER -> CENTER_CENTER;
		case TRAILING -> CENTER_RIGHT;
		};
		case TRAILING -> switch (x) {
		case LEADING -> BOTTOM_LEFT;
		case CENTER -> BOTTOM_CENTER;
		case TRAILING -> BOTTOM_RIGHT;
		};
		};
	}

}
