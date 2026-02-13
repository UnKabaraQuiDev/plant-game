package lu.kbra.plant_game.plugin.registry;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lu.kbra.pclib.PCUtils;
import lu.kbra.plant_game.PGLogic;
import lu.kbra.plant_game.PGMain;
import lu.kbra.plant_game.engine.scene.world.GameData;
import lu.kbra.plant_game.engine.scene.world.LevelState;
import lu.kbra.plant_game.engine.scene.world.data.LevelData;
import lu.kbra.plant_game.plugin.PluginDescriptor;
import lu.kbra.plant_game.plugin.exception.RegistryFailedException;

public abstract class LevelRegistry extends PluginRegistry {

	public class LevelDefinition implements Comparable<LevelDefinition> {

		protected int idx;
		protected String levelId;
		protected LevelData levelData;
		protected LevelState levelState = LevelState.NOT_STARTED;
		protected byte progress = 0;
		protected Optional<GameData> gameData;

		public LevelDefinition(final int idx, final String levelId, final LevelData levelData) {
			this.idx = idx;
			this.levelId = levelId;
			this.levelData = levelData;
		}

		public LevelDefinition(final int idx, final String levelId, final LevelData levelData, final LevelState levelState,
				final byte progress, final Optional<GameData> gameData) {
			this.idx = idx;
			this.levelId = levelId;
			this.levelData = levelData;
			this.levelState = levelState;
			this.progress = progress;
			this.gameData = gameData;
		}

		public String getInternalName() {
			return this.getPluginDescriptor().getInternalName() + "." + this.levelId;
		}

		public int getIdx() {
			return this.idx;
		}

		public PluginDescriptor getPluginDescriptor() {
			return LevelRegistry.super.getPluginDescriptor();
		}

		public String getLevelId() {
			return this.levelId;
		}

		public LevelData getLevelData() {
			return this.levelData;
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

		@Override
		public int compareTo(final LevelDefinition o) {
			return Integer.compare(this.idx, o.idx);
		}

		@Override
		public String toString() {
			return "LevelDefinition@" + System.identityHashCode(this) + " [idx=" + this.idx + ", levelId=" + this.levelId + ", levelData="
					+ this.levelData + ", levelState=" + this.levelState + ", progress=" + this.progress + ", gameData=" + this.gameData
					+ "]";
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
			final LevelDefinition ld = new LevelDefinition(idx, id, levelData);
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

	protected Optional<GameData> readProgress(final LevelDefinition ld) throws IOException {
		final File dataFile = new File(PGMain.APP_DIR, "/games/" + ld.getInternalName() + "/game.json");
		if (!dataFile.exists()) {
			return Optional.empty();
		}
		return Optional.ofNullable(PGLogic.OBJECT_MAPPER.readValue(dataFile, GameData.class));
	}

}
