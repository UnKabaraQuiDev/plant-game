package lu.kbra.plant_game.base.entity.go.obj;

import java.util.HashMap;
import java.util.Map;

import lu.kbra.plant_game.base.data.DefaultResourceType;
import lu.kbra.plant_game.engine.entity.go.impl.EnergyContainer;
import lu.kbra.plant_game.engine.entity.go.impl.WaterContainer;
import lu.kbra.plant_game.engine.entity.go.obj.PlaceableAnimatedGameObject;
import lu.kbra.plant_game.engine.entity.impl.StarterPodObject;
import lu.kbra.plant_game.engine.mesh.AnimatedMesh;
import lu.kbra.plant_game.engine.scene.world.data.LevelData;
import lu.kbra.plant_game.engine.scene.world.data.resource.ResourceType;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.geom.Mesh;

@DataPath("classpath:/models/starter-pod-small.json")
public class StarterPodSmallObject extends PlaceableAnimatedGameObject implements WaterContainer, EnergyContainer, StarterPodObject {

	protected Map<ResourceType, Integer> maxResources = new HashMap<>();

	public StarterPodSmallObject(final String str, final Mesh mesh, final AnimatedMesh animatedMesh) {
		super(str, mesh, animatedMesh);
	}

	@Override
	public void accept(final LevelData t) {
		t.getGame().getStartResources().forEach((k, v) -> this.maxResources.put(k, (int) (v * 1.5)));
	}

	@Override
	public int getMaxEnergy() {
		return this.maxResources.getOrDefault(DefaultResourceType.ENERGY, 0);
	}

	@Override
	public int getMaxWater() {
		return this.maxResources.getOrDefault(DefaultResourceType.WATER, 0);
	}

	@Override
	public ResourceType[] getAllowedResources() {
		return this.getMaxResources().keySet().toArray(ResourceType[]::new);
	}

	@Override
	public Map<ResourceType, Integer> getMaxResources() {
		return this.maxResources;
	}

	@Override
	public String toString() {
		return "StarterPodSmallObject@" + System.identityHashCode(this) + " [maxResources=" + this.maxResources + ", staticMeshFootprint="
				+ this.staticMeshFootprint + ", animatedMeshFootprint=" + this.animatedMeshFootprint + ", footprint=" + this.footprint
				+ ", rotation=" + this.rotation + ", tile=" + this.tile + ", animatedTransform=" + this.animatedTransform
				+ ", animatedMesh=" + this.animatedMesh + ", materialId=" + this.materialId + ", isEntityMaterialId="
				+ this.isEntityMaterialId + ", objectId=" + this.objectId + ", objectIdLocation=" + this.objectIdLocation + ", mesh="
				+ this.mesh + ", transform=" + this.transform + ", active=" + this.active + ", name=" + this.name + "]";
	}

}
