// @formatter:off
package lu.kbra.plant_game.generated;

import java.util.HashMap;
import java.util.Map;

import org.joml.Vector4f;
import org.joml.Vector4fc;

public enum ColorMaterial {
	WHITE(1.0f, 1.0f, 1.0f, 1f, false, false),

	BLACK(0.0f, 0.0f, 0.0f, 1f, false, false),

	RED(1.0f, 0.0f, 0.0f, 1f, false, false),

	GRAY(0.5019608f, 0.5019608f, 0.5019608f, 1f, false, false),

	PINK(1.0f, 0.6862745f, 0.6862745f, 1f, false, false),

	BROWN(0.5372549f, 0.31764707f, 0.16078432f, 1f, false, false),

	ORANGE(1.0f, 0.78431374f, 0.0f, 1f, false, false),

	YELLOW(1.0f, 1.0f, 0.0f, 1f, false, false),

	GREEN(0.0f, 1.0f, 0.0f, 1f, false, false),

	CYAN(0.0f, 1.0f, 1.0f, 1f, false, false),

	BLUE(0.0f, 0.0f, 1.0f, 1f, false, false),

	MAGENTA(1.0f, 0.0f, 1.0f, 1f, false, false),

	LIGHT_WHITE(1.0f, 1.0f, 1.0f, 1f, true, false),

	LIGHT_BLACK(0.011764706f, 0.011764706f, 0.011764706f, 1f, true, false),

	LIGHT_RED(1.0f, 0.0f, 0.0f, 1f, true, false),

	LIGHT_GRAY(0.7137255f, 0.7137255f, 0.7137255f, 1f, true, false),

	LIGHT_PINK(1.0f, 0.98039216f, 0.98039216f, 1f, true, false),

	LIGHT_BROWN(0.7647059f, 0.4509804f, 0.22745098f, 1f, true, false),

	LIGHT_ORANGE(1.0f, 1.0f, 0.0f, 1f, true, false),

	LIGHT_YELLOW(1.0f, 1.0f, 0.0f, 1f, true, false),

	LIGHT_GREEN(0.0f, 1.0f, 0.0f, 1f, true, false),

	LIGHT_CYAN(0.0f, 1.0f, 1.0f, 1f, true, false),

	LIGHT_BLUE(0.0f, 0.0f, 1.0f, 1f, true, false),

	LIGHT_MAGENTA(1.0f, 0.0f, 1.0f, 1f, true, false),

	DARK_WHITE(0.69803923f, 0.69803923f, 0.69803923f, 1f, false, true),

	DARK_BLACK(0.0f, 0.0f, 0.0f, 1f, false, true),

	DARK_RED(0.69803923f, 0.0f, 0.0f, 1f, false, true),

	DARK_GRAY(0.34901962f, 0.34901962f, 0.34901962f, 1f, false, true),

	DARK_PINK(0.69803923f, 0.47843137f, 0.47843137f, 1f, false, true),

	DARK_BROWN(0.37254903f, 0.21960784f, 0.10980392f, 1f, false, true),

	DARK_ORANGE(0.69803923f, 0.54901963f, 0.0f, 1f, false, true),

	DARK_YELLOW(0.69803923f, 0.69803923f, 0.0f, 1f, false, true),

	DARK_GREEN(0.0f, 0.69803923f, 0.0f, 1f, false, true),

	DARK_CYAN(0.0f, 0.69803923f, 0.69803923f, 1f, false, true),

	DARK_BLUE(0.0f, 0.0f, 0.69803923f, 1f, false, true),

	DARK_MAGENTA(0.69803923f, 0.0f, 0.69803923f, 1f, false, true);

	static final int BASE_COLOR_COUNT = 12;

	private static final Map<Integer, ColorMaterial> COLORS_BY_ID = new HashMap<>();

	static {
		for (ColorMaterial cm : ColorMaterial.values()) {
			COLORS_BY_ID.put((int) cm.getId(), cm);
		}
	}

	private final float r;

	private final float g;

	private final float b;

	private final float a;

	private final boolean light;

	private final boolean dark;

	private final Vector4fc color;

	private ColorMaterial(float r, float g, float b, float a, boolean l, boolean d) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
		this.light = l;
		this.dark = d;
		this.color = new Vector4f(r, g, b, a);
	}

	public static ColorMaterial byId(int id) {
		return COLORS_BY_ID.get(id);
	}

	public ColorMaterial next() {
		int nextId = getId() % ColorMaterial.values().length + 1;
		return byId(nextId);
	}

	public ColorMaterial previous() {
		int prevId = (getId() - 2 + ColorMaterial.values().length) % ColorMaterial.values().length + 1;
		return byId(prevId);
	}

	public static ColorMaterial valueOf(float r, float g, float b, float a) {
		for (ColorMaterial m : values()) {
			if (m.r == r && m.g == g && m.b == b && m.a == a) {
				return m;
			}
		}
		return null;
	}

	public ColorMaterial darker() {
		return dark ? ColorMaterial.BLACK : light ? byId(getId() - BASE_COLOR_COUNT) : byId(getId() + 2 * BASE_COLOR_COUNT);
	}

	public ColorMaterial lighter() {
		return light ? ColorMaterial.WHITE : dark ? byId(getId() - 2 * BASE_COLOR_COUNT) : byId(getId() + BASE_COLOR_COUNT);
	}

	public short getId() {
		return (short) (ordinal() + 1);
	}

	public Vector4fc getColor() {
		return this.color;
	}
}
