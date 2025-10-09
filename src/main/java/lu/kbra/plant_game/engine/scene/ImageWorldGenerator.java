package lu.kbra.plant_game.engine.scene;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import lu.pcy113.pclib.PCUtils;

public class ImageWorldGenerator extends WorldGenerator {

	private BufferedImage image;
	private float scale;

	public ImageWorldGenerator(String location, float scale) {
		this.scale = scale;

		try (ByteArrayInputStream bais = new ByteArrayInputStream(PCUtils.readBytesSource(location))) {
			image = ImageIO.read(bais);
		} catch (IOException e) {
			throw new RuntimeException("Error while reading: " + location, e);
		}

		setSize(image.getWidth(null), image.getHeight(null), (int) (255 / scale));
	}

	@Override
	protected Integer genNoise(int x, int z) {
		return (int) Math.round((image.getRGB(x, z) & 0xff) * scale);
	}

}
