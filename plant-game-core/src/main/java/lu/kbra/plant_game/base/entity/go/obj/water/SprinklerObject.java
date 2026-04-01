package lu.kbra.plant_game.base.entity.go.obj.water;

import org.joml.Vector2i;

import lu.kbra.pclib.PCUtils;
import lu.kbra.plant_game.base.data.DefaultResourceType;
import lu.kbra.plant_game.engine.entity.go.impl.PlaceableObject;
import lu.kbra.plant_game.engine.scene.world.WorldLevelScene;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;

public interface SprinklerObject extends NeedsRandomTick, PlaceableObject {

	@Override
	default void randomTick(final WindowInputHandler inputHandler, final WorldLevelScene worldLevelScene) {
		if (this.getTile() == null) {
			return;
		}
		final float amount = PCUtils.randomFloatRange(2, 8);
		if (!worldLevelScene.getResourceBuffer().tryConsume(DefaultResourceType.WATER, amount)) {
			return;
		}

		final int currentTileIndex = this.getCurrentTileIndex();
		final Vector2i[] offsets = this.getOffsets();
		final Vector2i offset = offsets[currentTileIndex];
		this.setCurrentTileIndex((currentTileIndex + 1) % offsets.length);

		worldLevelScene.getTerrain().addWater(this.getTile().add(offset.x, offset.y, new Vector2i()), amount);
	}

	Vector2i[] getOffsets();

	void setCurrentTileIndex(final int currentTileIndex);

	int getCurrentTileIndex();

}
