package lu.kbra.plant_game.engine.scene.world;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiConsumer;

import lu.kbra.plant_game.engine.scene.world.data.resource.ResourceType;

public class ResourceBuffer {

	private final Map<ResourceType, Float> beginning = new HashMap<>();
	private final Map<ResourceType, Float> produced = new HashMap<>();

	public void copyFrom(final Map<ResourceType, Float> p) {
		this.beginning.clear();
		this.beginning.putAll(p);
		this.produced.clear();
	}

	public void copyFrom(final ResourceBuffer rb) {
		this.copyFrom(rb.produced);
	}

	public void copyFrom(final GameData gameData) {
		this.copyFrom(gameData.getResources());
	}

	public boolean has(final ResourceType type, final float amount) {
		return this.get(type) >= amount;
	}

	public float get(final ResourceType type) {
		return this.beginning.getOrDefault(type, 0f) + this.produced.getOrDefault(type, 0f);
	}

	public boolean tryConsume(final ResourceType type, final float amount) {
		final float current = this.get(type);

		if (current < amount) {
			return false;
		}

		this.produced.merge(type, -amount, Float::sum);
		return true;
	}

	@Deprecated
	public void consume(final ResourceType type, final int amount) {
		this.produced.merge(type, (float) -amount, Float::sum);
	}

	@Deprecated
	public void consume(final ResourceType type, final float amount) {
		this.produced.merge(type, -amount, Float::sum);
	}

	public void add(final ResourceType type, final int amount) {
		this.produced.merge(type, (float) amount, Float::sum);
	}

	public void add(final ResourceType type, final float amount) {
		this.produced.merge(type, amount, Float::sum);
	}

	public void clear() {
		this.produced.clear();
	}

	public int size() {
		return this.produced.size();
	}

	public Set<ResourceType> keySet() {
		return this.produced.keySet();
	}

	public Collection<Float> values() {
		return this.produced.values();
	}

	public Set<Entry<ResourceType, Float>> entrySet() {
		return this.produced.entrySet();
	}

	public void forEach(final BiConsumer<? super ResourceType, ? super Float> action) {
		this.produced.forEach(action);
	}

	public Map<ResourceType, Float> getBeginning() {
		return this.beginning;
	}

	public Map<ResourceType, Float> getProduced() {
		return this.produced;
	}

	@Override
	public String toString() {
		return "ResourceBuffer@" + System.identityHashCode(this) + " [produced=" + this.produced + "]";
	}

}
