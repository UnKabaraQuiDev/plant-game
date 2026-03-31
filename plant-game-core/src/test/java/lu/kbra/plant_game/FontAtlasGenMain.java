package lu.kbra.plant_game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.junit.Test;
import org.lwjgl.BufferUtils;

import lu.kbra.pclib.logger.GlobalLogger;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;

public class FontAtlasGenMain extends GenMainConsts {

	public static final int[] DEFAULT_SIZES = new int[] { 8, 16, 24, 32, 48, 64, 128, 256 };
	public static final int FIRST_CHAR = 32;
	public static final int LAST_CHAR = 126;

	public static class Atlas {
		public final int textureId;
		public final int width;
		public final int height;
		public final int tileSize;
		public final Map<Character, float[]> uv; // u0,v0,u1,v1 per char

		public Atlas(int textureId, int width, int height, int tileSize, Map<Character, float[]> uv) {
			this.textureId = textureId;
			this.width = width;
			this.height = height;
			this.tileSize = tileSize;
			this.uv = uv;
		}
	}

	public static void generateAtlases(File fontFilePath) throws Exception {
		generateAtlases(fontFilePath, DEFAULT_SIZES, FIRST_CHAR, LAST_CHAR);
	}

	public static void generateAtlases(File fontFilePath, int[] sizes, int firstChar, int lastChar) throws Exception {
		final Font baseFont = Font.createFont(Font.TRUETYPE_FONT, fontFilePath);
		final Map<Integer, Atlas> result = new LinkedHashMap<>();
		final int charCount = TextEmitter.STRING.length();

		for (int tileSize : sizes) {
			float fontPoint = tileSize * 0.75f; // adjust to fit nicely inside tile
			final Font font = baseFont.deriveFont(Font.PLAIN, fontPoint);

			int atlasW = tileSize * charCount; // nextPowerOfTwo(charCount * tileSize);
			int atlasH = tileSize; // nextPowerOfTwo(tileSize);

			BufferedImage atlasImage = new BufferedImage(atlasW, atlasH, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = atlasImage.createGraphics();

			// rendering hints
			// g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			// g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

			g.setFont(font);
			g.setColor(new Color(0, 0, 0, 0));
			g.fillRect(0, 0, atlasW, atlasH);
			g.setColor(Color.WHITE);

			FontRenderContext frc = g.getFontRenderContext();
			Map<Character, float[]> uvmap = new HashMap<>(charCount);

			int i = 0;
			for (char ch : TextEmitter.STRING.toCharArray()) {
				int x = i * tileSize;
				int y = 0;

				GlyphVector gv = font.createGlyphVector(frc, new char[] { ch });
				Rectangle2D gb = gv.getGlyphVisualBounds(0).getBounds2D();

				Rectangle2D bounds = gb;
				if (bounds.getWidth() <= 0 || bounds.getHeight() <= 0) {
					bounds = gv.getLogicalBounds();
				}

				double gw = Math.max(1, bounds.getWidth());
				double gh = Math.max(1, bounds.getHeight());

				double margin = Math.max(1, tileSize * 0.08);
				double maxW = tileSize - 2 * margin;
				double maxH = tileSize - 2 * margin;
				double scale = Math.min(maxW / gw, maxH / gh);

				double drawW = gw * scale;
				double drawH = gh * scale;

				// center glyph horizontally and vertically within the tile
				double dx = x + (tileSize - drawW) / 2.0 - bounds.getX() * scale;
				double dy = y + (tileSize - drawH) / 2.0 - bounds.getY() * scale;

				g.translate(dx, dy);
				g.scale(scale, scale);
				g.fill(gv.getOutline());
				g.scale(1.0 / scale, 1.0 / scale);
				g.translate(-dx, -dy);

				// UV coordinates normalized
				float u0 = (float) x / atlasW;
				float v0 = 0f;
				float u1 = (float) (x + tileSize) / atlasW;
				float v1 = 1f;

				uvmap.put(ch, new float[] { u0, v0, u1, v1 });
				i++;
			}

			g.dispose();

			// Optional: write atlas image to disk for debug
			final File outputFile = new File(SRC_MAIN_RESOURCES_BAKES_FONTS_DIR, fontFilePath.getName() + "/" + tileSize + ".png");
			outputFile.getParentFile().mkdirs();
			if (!outputFile.exists())
				outputFile.createNewFile();
			ImageIO.write(atlasImage, "PNG", outputFile);
		}

	}

	private static ByteBuffer bufferedImageToByteBuffer(BufferedImage img) {
		int w = img.getWidth();
		int h = img.getHeight();
		int[] pixels = new int[w * h];
		img.getRGB(0, 0, w, h, pixels, 0, w);

		ByteBuffer buffer = BufferUtils.createByteBuffer(w * h * 4);

		// convert ARGB (Java) to RGBA (OpenGL)
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				int argb = pixels[y * w + x];
				int a = (argb >> 24) & 0xFF;
				int r = (argb >> 16) & 0xFF;
				int g = (argb >> 8) & 0xFF;
				int b = (argb) & 0xFF;
				buffer.put((byte) r);
				buffer.put((byte) g);
				buffer.put((byte) b);
				buffer.put((byte) a);
			}
		}
		buffer.flip();
		return buffer;
	}

	private static int nextPowerOfTwo(int v) {
		int p = 1;
		while (p < v)
			p <<= 1;
		return p;
	}

	public static void main(String[] args) throws Exception {
		for (final Path font : Files.list(Paths.get(SRC_MAIN_RESOURCES_FONTS_DIR.getPath())).toList()) {
			GlobalLogger.info("Generating font atlas for: " + font);
			generateAtlases(font.toFile());
		}
	}

	@Test
	public void test() throws Exception {
		main(new String[0]);
	}

}
