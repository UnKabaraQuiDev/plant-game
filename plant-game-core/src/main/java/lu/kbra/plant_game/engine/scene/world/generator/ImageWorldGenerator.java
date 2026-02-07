package lu.kbra.plant_game.engine.scene.world.generator;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import lu.kbra.pclib.PCUtils;

public class ImageWorldGenerator extends WorldGenerator {

	private BufferedImage image;
	private final float scale;

	public ImageWorldGenerator(final String location, final int height) {
		this(location, (float) height / 255);
	}

	public ImageWorldGenerator(final String location, final float scale) {
		this.scale = scale;

		try (ByteArrayInputStream bais = new ByteArrayInputStream(PCUtils.readBytesSource(location))) {
			this.image = ImageIO.read(bais);
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
