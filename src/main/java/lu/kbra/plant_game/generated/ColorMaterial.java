// @formatter:off
package lu.kbra.plant_game.generated;

import java.lang.Integer;
import java.util.HashMap;
import java.util.Map;
import org.joml.Vector4f;
import org.joml.Vector4fc;

public enum ColorMaterial {
	DARK_BLACK(0.0f, 0.0f, 0.0f, 1f),

	GRAY(0.5019608f, 0.5019608f, 0.5019608f, 1f),

	LIGHT_ORANGE(1.0f, 1.0f, 0.0f, 1f),

	DARK_DARK_GRAY(0.17254902f, 0.17254902f, 0.17254902f, 1f),

	BLUE(0.0f, 0.0f, 1.0f, 1f),

	LIGHT_BLACK(0.011764706f, 0.011764706f, 0.011764706f, 1f),

	LIGHT_GREEN(0.0f, 1.0f, 0.0f, 1f),

	DARK_LIGHT_GRAY(0.5254902f, 0.5254902f, 0.5254902f, 1f),

	PINK(1.0f, 0.6862745f, 0.6862745f, 1f),

	DARK_YELLOW(0.69803923f, 0.69803923f, 0.0f, 1f),

	DARK_RED(0.69803923f, 0.0f, 0.0f, 1f),

	DARK_MAGENTA(0.69803923f, 0.0f, 0.69803923f, 1f),

	DARK_BROWN(0.37254903f, 0.21960784f, 0.10980392f, 1f),

	BLACK(0.0f, 0.0f, 0.0f, 1f),

	LIGHT_MAGENTA(1.0f, 0.0f, 1.0f, 1f),

	LIGHT_BROWN(0.7647059f, 0.4509804f, 0.22745098f, 1f),

	BROWN(0.5372549f, 0.31764707f, 0.16078432f, 1f),

	ORANGE(1.0f, 0.78431374f, 0.0f, 1f),

	WHITE(1.0f, 1.0f, 1.0f, 1f),

	LIGHT_RED(1.0f, 0.0f, 0.0f, 1f),

	DARK_CYAN(0.0f, 0.69803923f, 0.69803923f, 1f),

	LIGHT_PINK(1.0f, 0.98039216f, 0.98039216f, 1f),

	LIGHT_LIGHT_GRAY(1.0f, 1.0f, 1.0f, 1f),

	LIGHT_DARK_GRAY(0.35686275f, 0.35686275f, 0.35686275f, 1f),

	DARK_PINK(0.69803923f, 0.47843137f, 0.47843137f, 1f),

	GREEN(0.0f, 1.0f, 0.0f, 1f),

	DARK_BLUE(0.0f, 0.0f, 0.69803923f, 1f),

	LIGHT_YELLOW(1.0f, 1.0f, 0.0f, 1f),

	RED(1.0f, 0.0f, 0.0f, 1f),

	LIGHT_GRAY(0.7137255f, 0.7137255f, 0.7137255f, 1f),

	LIGHT_BLUE(0.0f, 0.0f, 1.0f, 1f),

	LIGHT_WHITE(1.0f, 1.0f, 1.0f, 1f),

	MAGENTA(1.0f, 0.0f, 1.0f, 1f),

	LIGHT_CYAN(0.0f, 1.0f, 1.0f, 1f),

	YELLOW(1.0f, 1.0f, 0.0f, 1f),

	DARK_GREEN(0.0f, 0.69803923f, 0.0f, 1f),

	CYAN(0.0f, 1.0f, 1.0f, 1f),

	DARK_GRAY(0.34901962f, 0.34901962f, 0.34901962f, 1f),

	DARK_WHITE(0.69803923f, 0.69803923f, 0.69803923f, 1f),

	DARK_ORANGE(0.69803923f, 0.54901963f, 0.0f, 1f);

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

	private final Vector4fc color;

	private ColorMaterial(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
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
		float dr = Math.max(0f, r * 0.7f);
		float dg = Math.max(0f, g * 0.7f);
		float db = Math.max(0f, b * 0.7f);
		final ColorMaterial c = ColorMaterial.valueOf(dr, dg, db, a);
		return c != null ? c : ColorMaterial.BLACK;
	}

	public ColorMaterial lighter() {
		float lr = Math.min(1f, r * 1.3f);
		float lg = Math.min(1f, g * 1.3f);
		float lb = Math.min(1f, b * 1.3f);
		final ColorMaterial c = ColorMaterial.valueOf(lr, lg, lb, a);
		return c != null ? c : ColorMaterial.WHITE;
	}

	public short getId() {
		return (short) (ordinal() + 1);
	}

	public Vector4fc getColor() {
		return this.color;
	}
}
