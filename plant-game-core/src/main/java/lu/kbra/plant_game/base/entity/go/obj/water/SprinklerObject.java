package lu.kbra.plant_game.base.entity.go.obj.water;

import java.util.Arrays;
import java.util.Map;

import org.joml.Vector2i;
import org.joml.Vector2ic;

import lu.kbra.pclib.PCUtils;
import lu.kbra.pclib.logger.GlobalLogger;
import lu.kbra.plant_game.base.data.DefaultResourceType;
import lu.kbra.plant_game.engine.entity.go.impl.EnergyConsumer;
import lu.kbra.plant_game.engine.entity.go.impl.PlaceableObject;
import lu.kbra.plant_game.engine.entity.go.impl.WaterConsumer;
import lu.kbra.plant_game.engine.scene.world.WorldLevelScene;
import lu.kbra.plant_game.engine.scene.world.data.resource.ResourceType;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.standalone.gameengine.utils.consts.Direction;

public interface SprinklerObject extends NeedsRandomTick, PlaceableObject, WateringFootprintOwner, EnergyConsumer, WaterConsumer {

	@Override
	default void randomTick(final WindowInputHandler inputHandler, final WorldLevelScene worldLevelScene) {
		if (this.getTile() == null) {
			return;
		}

		final Map<ResourceType, Float> neededResources = this.getConsumedRate();
		// TODO: Maybe set the multiplier to 1
		if (!worldLevelScene.getResourceBuffer().tryConsume(neededResources, this.getRandomTickDuration() / 1_000f)) {
			this.setWorking(false);
			return;
		}
		this.setWorking(true);

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

		worldLevelScene.getTerrain()
				.addWater(this.getTile().add(offset.x(), offset.y(), new Vector2i()), neededResources.get(DefaultResourceType.WATER));
	}

	default Vector2ic[] getOffsets(final Direction rotation) {
		final Vector2ic[][] cachedOffsets = this.getCachedOffsets();

		if (cachedOffsets[rotation.getIndex()] != null) {
			return cachedOffsets[rotation.getIndex()];
		}
		return cachedOffsets[rotation.getIndex()] = this.getWateringFootprint().computeLocalCells(rotation).toArray(Vector2ic[]::new);
	}

	Vector2ic[][] getCachedOffsets();

	void setCurrentTileIndex(final int currentTileIndex);

	int getCurrentTileIndex();

	float getMinSprinkledWater();

	float getMaxSprinkledWater();

	@Override
	default int getRandomTickDuration() {
		return 1000;
	}

	@Override
	default float getConsumedWater() {
		return PCUtils.randomFloatRange(this.getMinSprinkledWater(), this.getMaxSprinkledWater());
	}

	@Override
	default Map<ResourceType, Float> getConsumedRate() {
		return Map.of(DefaultResourceType.WATER, this.getConsumedWater(), DefaultResourceType.ENERGY, this.getConsumedEnergy());
	}

}
