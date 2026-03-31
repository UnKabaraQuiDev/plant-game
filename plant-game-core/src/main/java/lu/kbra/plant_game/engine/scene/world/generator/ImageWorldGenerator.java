package lu.kbra.plant_game.engine.scene.world.generator;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Optional;

import javax.imageio.ImageIO;

import lu.kbra.plant_game.generated.ColorMaterial;
import lu.kbra.plant_game.plugin.PluginDescriptor;
import lu.kbra.standalone.gameengine.utils.GameEngineUtils;

public class ImageWorldGenerator extends WorldGenerator {

	private boolean colorMaterial = false;
	private BufferedImage image;
	private Optional<BufferedImage> materialImage;
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
		this.materialImage = Optional.empty();

		this.setSize(this.image.getWidth(null), this.image.getHeight(null), (int) (255 * scale));
	}

	public ImageWorldGenerator(
			final PluginDescriptor plugin,
			final String location,
			final String materialLocation,
			final boolean colorMaterial,
			final int height) {
		this(plugin, location, materialLocation, colorMaterial, (float) height / 255);
	}

	public ImageWorldGenerator(
			final PluginDescriptor plugin,
			final String location,
			final String materialLocation,
			final boolean colorMaterial,
			final float scale) {
		this.scale = scale;
		this.colorMaterial = colorMaterial;

		try (InputStream is = this.getClass().getResourceAsStream(location)) {
			this.image = ImageIO.read(is);
		} catch (final IOException e) {
			throw new RuntimeException("Error while reading: " + location, e);
		}

		try (InputStream is = this.getClass().getResourceAsStream(materialLocation)) {
			this.materialImage = Optional.of(ImageIO.read(is));
		} catch (final IOException e) {
			throw new RuntimeException("Error while reading: " + materialLocation, e);
		}

		this.setSize(this.image.getWidth(null), this.image.getHeight(null), (int) (255 * scale));
	}

	@Override
	protected ColorMaterial getCellMaterial(final int x, final int z, final int cellHeight) {
		return this.materialImage
				.map(c -> this.colorMaterial ? ColorMaterial.getClosest(GameEngineUtils.rgbToVec4f(c.getRGB(x, z)))
						: ColorMaterial.byId(c.getRGB(x, z) & 0xff))
				.orElseGet(() -> this.getCellHeight(x, z) <= 0 ? ColorMaterial.GRAY // rocks
						: this.getCellHeight(x, z) <= 1 ? ColorMaterial.LIGHT_BROWN // sand
						: Math.random() < 0.5 ? ColorMaterial.BROWN // dirts
						: ColorMaterial.DARK_BROWN);
	}

	@Override
	protected Integer genNoise(final int x, final int z) {
		return Math.round((this.image.getRGB(x, z) & 0xff) * this.scale);
	}

	@Override
	public String toString() {
		return "ImageWorldGenerator@" + System.identityHashCode(this) + " [image=" + this.image + ", materialImage=" + this.materialImage
				+ ", scale=" + this.scale + ", materialType=" + Arrays.toString(this.materialType) + ", noiseCompute="
				+ Arrays.toString(this.noiseCompute) + "]";
	}

}
