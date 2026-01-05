package lu.kbra.plant_game.engine.util;

import lu.kbra.standalone.gameengine.utils.gl.consts.TextureFilter;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextureWrap;

public final class BuildingOption {

	private final String dataPath;
	private final TextureFilter textureFilter;
	private final TextureWrap textureWrap;
	private final int bufferSize;

	public BuildingOption(final String dataPath, final TextureFilter textureFilter, final TextureWrap textureWrap, final int bufferSize) {
		this.dataPath = dataPath;
		this.textureFilter = textureFilter;
		this.textureWrap = textureWrap;
		this.bufferSize = bufferSize;
	}

	public String getDataPath() {
		return this.dataPath;
	}

	public TextureFilter getTextureFilter() {
		return this.textureFilter;
	}

	public TextureWrap getTextureWrap() {
		return this.textureWrap;
	}

	public int getBufferSize() {
		return this.bufferSize;
	}

	@Override
	public String toString() {
		return "BuildingOption [dataPath=" + this.dataPath + ", textureWrap=" + this.textureWrap + ", bufferSize=" + this.bufferSize + "]";
	}

}
