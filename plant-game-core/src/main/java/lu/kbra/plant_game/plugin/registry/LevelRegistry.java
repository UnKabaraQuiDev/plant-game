package lu.kbra.plant_game.plugin.registry;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import lu.kbra.pclib.PCUtils;
import lu.kbra.plant_game.PGLogic;
import lu.kbra.plant_game.PGMain;
import lu.kbra.plant_game.engine.scene.world.data.GameData;
import lu.kbra.plant_game.engine.scene.world.data.LevelData;
import lu.kbra.plant_game.engine.scene.world.data.LevelState;
import lu.kbra.plant_game.plugin.PluginDescriptor;
import lu.kbra.plant_game.plugin.exception.RegistryFailedException;

public abstract class LevelRegistry extends PluginRegistry {

	public class LevelDefinition implements Comparable<LevelDefinition> {

		protected int idx;
		protected LevelData levelData;
		protected LevelState levelState = LevelState.NOT_STARTED;
		protected byte progress = 0;
		protected Optional<GameData> gameData;

		public LevelDefinition(final int idx, final LevelData levelData) {
			this.idx = idx;
			this.levelData = levelData;
		}

		public LevelDefinition(
				final int idx,
				final String levelId,
				final LevelData levelData,
				final LevelState levelState,
				final byte progress,
				final Optional<GameData> gameData) {
			this.idx = idx;
			this.levelData = levelData;
			this.levelState = levelState;
			this.progress = progress;
			this.gameData = gameData;
		}

		public String getInternalName() {
			return this.levelData.getInternalName();
		}

		public int getIdx() {
			return this.idx;
		}

		public PluginDescriptor getPluginDescriptor() {
			return LevelRegistry.super.getPluginDescriptor();
		}

		public String getLevelId() {
			return this.levelData.getLevelId();
		}

		public LevelData getLevelData() {
			return this.levelData;
		}

		public void setLevelData(final LevelData levelData) {
			this.levelData = levelData;
		}

		public LevelState getLevelState() {
			return this.levelState;
		}

		public byte getProgress() {
			return this.progress;
		}

		public Optional<GameData> getGameData() {
			return this.gameData;
		}

		public void setGameData(final GameData gameData2) {
			this.gameData = Optional.of(gameData2);
		}

		@Override
		public int compareTo(final LevelDefinition o) {
			return Integer.compare(this.idx, o.idx);
		}

		public boolean isNew() {
			return (this.getLevelState() == LevelState.NOT_STARTED || this.getLevelState() == LevelState.LOST);
		}

		public boolean hasPast() {
			return (this.getLevelState() == LevelState.STARTED || this.getLevelState() == LevelState.WON) && this.gameData.isPresent();
		}

		public void reload() throws IOException {
			this.gameData = LevelRegistry.this.readProgress(this);
			this.gameData.ifPresent(c -> {
				this.levelState = c.getLevelState();
				this.progress = c.getProgress();
			});
		}

		@Override
		public String toString() {
			return "LevelDefinition@" + System.identityHashCode(this) + " [idx=" + this.idx + ", levelData=" + this.levelData
					+ ", levelState=" + this.levelState + ", progress=" + this.progress + ", gameData=" + this.gameData
					+ ", getInternalName()=" + this.getInternalName() + "]";
		}

	}

	public static final String DEBUG_PROPERTY = LevelRegistry.class.getSimpleName() + ".debug";
	public static boolean DEBUG = Boolean.getBoolean(DEBUG_PROPERTY);

	public static final List<LevelDefinition> LEVELS = new ArrayList<>();

	public LevelRegistry(final PluginDescriptor pluginDescriptor) {
		super(pluginDescriptor);
	}

	protected void register(final int idx, final String id) throws RegistryFailedException {
		try {
			final LevelData levelData = PGLogic.OBJECT_MAPPER
					.readValue(PCUtils.readPackagedStringFile(this.getClass(), "/levels/" + id + ".json"), LevelData.class);
			levelData.setPluginDescriptor(this.pluginDescriptor);
			levelData.setLevelId(id);
			final LevelDefinition ld = new LevelDefinition(idx, levelData);
			final Optional<GameData> gd = this.readProgress(ld);
			gd.ifPresent(c -> {
				ld.levelState = c.getLevelState();
				ld.progress = c.getProgress();
			});
			ld.gameData = gd;
			LEVELS.add(ld);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RegistryFailedException(this.pluginDescriptor, e);
		}
	}

	@Override
	public void postConstruct() {
		LEVELS.sort(Comparator.comparingInt(LevelRegistry.LevelDefinition::getIdx));
	}

	protected Optional<GameData> readProgress(final LevelDefinition ld) throws IOException {
		final File dataFile = new File(PGMain.APP_DATA_DIR, "/games/" + ld.getInternalName() + "/game.json");
		if (!dataFile.exists()) {
			return Optional.empty();
		}
		final GameData obj = PGLogic.OBJECT_MAPPER.readValue(dataFile, GameData.class);
		obj.setLevelData(ld.levelData);
		return Optional.of(obj);
	}

	@Override
	public int getPriority() {
		return 500;
	}

}
