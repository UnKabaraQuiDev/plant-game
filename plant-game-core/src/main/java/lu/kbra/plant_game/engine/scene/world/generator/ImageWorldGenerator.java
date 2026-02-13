package lu.kbra.plant_game.engine.scene.world.generator;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import lu.kbra.plant_game.plugin.PluginDescriptor;

public class ImageWorldGenerator extends WorldGenerator {

	private BufferedImage image;
	private final float scale;

	public ImageWorldGenerator(final PluginDescriptor plugin, final String location, final int height) {
		this(plugin, location, (float) height / 255);
	}

	public ImageWorldGenerator(final PluginDescriptor plugin, final String location, final float scale) {
		this.scale = scale;

		try (InputStream is = this.getClass().getResourceAsStream(location)) {
			this.image = ImageIO.read(is);
		} catch (final IOException e) {
			throw new RuntimeException("Error while reading: " + location, e);
		}

		this.setSize(this.image.getWidth(null), this.image.getHeight(null), (int) (255 * scale));
	}

	@Override
	protected Integer genNoise(final int x, final int z) {
		return (int) Math.round((this.image.getRGB(x, z) & 0xff) * this.scale);
	}

}
