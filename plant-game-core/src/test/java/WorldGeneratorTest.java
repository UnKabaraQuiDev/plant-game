import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.joml.Vector2f;
import org.junit.Test;

import lu.kbra.plant_game.engine.scene.world.generator.WorldGenerator;
import lu.kbra.standalone.gameengine.utils.MathUtils;
import lu.kbra.standalone.gameengine.utils.gl.consts.Consts;
import lu.kbra.standalone.gameengine.utils.interpolation.Interpolators;
import lu.kbra.standalone.gameengine.utils.noise.NoiseGenerator;

public class WorldGeneratorTest {

	private WorldGenerator worldGenerator;

	@Test
	public void testGenerateWorldSimplex() throws IOException {
		final int width = 50, height = 30;
		worldGenerator = new WorldGenerator(width, height, 0);

		draw(worldGenerator, width, height, "simplex-0.png");
	}

	@Test
	public void testGenerateWorldCustom() throws IOException {
		final int width = 50, height = 30;
		worldGenerator = new WorldGenerator(width, height, 0) {
			private NoiseGenerator noise = new NoiseGenerator(1234, 10);

			@Override
			protected Integer genNoise(int x, int z) {
				final float oct1 = MathUtils.map(Interpolators.SINE_OUT.evaluate(noise.noise(x + 0.5f, z + 0.5f)), 0, 1, 0, 1);
				final float oct2 = MathUtils
						.map(Interpolators.BOUNCE_OUT
								.evaluate(noise.noise(MathUtils.rotate(new Vector2f(x + 0.5f, (z + 0.5f) * 0.5f), 45))), 0, 1, -1, 1);
				return (int) Math.floor(Math.max(-1, Math.pow(oct1 * 2 + oct2 * 3, 3) + 3));
			}
		};
		draw(worldGenerator, width, height, "custom-0.png");
	}

	private void draw(WorldGenerator worldGenerator2, int width, int height, String name) throws IOException {
		worldGenerator.compute();

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		// First, find min/max to normalize heights
		float minH = Float.MAX_VALUE;
		float maxH = -Float.MAX_VALUE;
		float[][] heights = new float[width][height];

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				float h = worldGenerator.getCellHeight(x, y);
				heights[x][y] = h;
				if (h < minH)
					minH = h;
				if (h > maxH)
					maxH = h;
			}
		}

		// Write normalized heights to image
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				float h = (heights[x][y] - minH) / (maxH - minH); // normalize 0..1
				int value = (int) (h * 255f);
				int argb = (255 << 24) | (value << 16) | (value << 8) | value; // grayscale
				image.setRGB(x, height - 1 - y, argb); // flip Y
			}
		}

		File outFile = new File(Consts.BAKES_RES_DIR, name);
		ImageIO.write(image, "png", outFile);
		System.out.println("Heightmap saved to " + outFile.getAbsolutePath());
	}

}
