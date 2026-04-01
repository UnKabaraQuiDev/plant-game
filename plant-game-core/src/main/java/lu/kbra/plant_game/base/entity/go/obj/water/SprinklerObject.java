package lu.kbra.plant_game.base.entity.go.obj.water;

import java.util.Arrays;

import org.joml.Vector2i;
import org.joml.Vector2ic;

import lu.kbra.pclib.PCUtils;
import lu.kbra.pclib.logger.GlobalLogger;
import lu.kbra.plant_game.base.data.DefaultResourceType;
import lu.kbra.plant_game.engine.entity.go.impl.PlaceableObject;
import lu.kbra.plant_game.engine.scene.world.WorldLevelScene;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.standalone.gameengine.utils.consts.Direction;

public interface SprinklerObject extends NeedsRandomTick, PlaceableObject, WateringFootprintOwner {

	@Override
	default void randomTick(final WindowInputHandler inputHandler, final WorldLevelScene worldLevelScene) {
		if (this.getTile() == null) {
			return;
		}
		final float amount = PCUtils.randomFloatRange(this.getMinSprinkledWater(), this.getMaxSprinkledWater());
		if (!worldLevelScene.getResourceBuffer().tryConsume(DefaultResourceType.WATER, amount)) {
			return;
		}

		final int currentTileIndex = this.getCurrentTileIndex();
		final Vector2ic[] offsets = this.getOffsets(this.getRotation());
		if (offsets == null) {
			GlobalLogger.info("(" + getClass().getSimpleName() + ") Invalid offsets for sprinkling.");
			return;
		}
		final Vector2ic offset = offsets[currentTileIndex];
		if (offset == null) {
			GlobalLogger.info("(" + getClass().getSimpleName() + ") Invalid offset for sprinkling: " + Arrays.deepToString(offsets));
			return;
		}
		this.setCurrentTileIndex((currentTileIndex + 1) % offsets.length);

		worldLevelScene.getTerrain().addWater(this.getTile().add(offset.x(), offset.y(), new Vector2i()), amount);
	}

	default Vector2ic[] getOffsets(final Direction rotation) {
		final Vector2ic[][] cachedOffsets = this.getCachedOffsets();
		System.err.println(getClass().getSimpleName() + ": " + Arrays.deepToString(cachedOffsets));
		if (cachedOffsets[rotation.getIndex()] != null) {
			return cachedOffsets[rotation.getIndex()];
		}
		return cachedOffsets[rotation.getIndex()] = this.getWateringFootprint().computeLocalCells(rotation).toArray(Vector2ic[]::new);
	}

	Vector2ic[][] getCachedOffsets();

	void setCurrentTileIndex(final int currentTileIndex);

	int getCurrentTileIndex();

	int getMinSprinkledWater();

	int getMaxSprinkledWater();

}
