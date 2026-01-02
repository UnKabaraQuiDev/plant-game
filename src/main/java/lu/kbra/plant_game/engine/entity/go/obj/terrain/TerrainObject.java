package lu.kbra.plant_game.engine.entity.go.obj.terrain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector4f;

import lu.kbra.plant_game.engine.entity.go.GameObject;
import lu.kbra.plant_game.engine.entity.go.MeshGameObject;
import lu.kbra.plant_game.engine.entity.go.impl.SynchronizedEntityContainer;
import lu.kbra.plant_game.engine.entity.go.mesh.terrain.TerrainMesh;
import lu.kbra.plant_game.engine.mesh.data.AttributeLocation;
import lu.kbra.standalone.gameengine.objs.entity.ParentAwareComponent;
import lu.kbra.standalone.gameengine.scene.camera.Camera3D;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class TerrainObject extends MeshGameObject implements SynchronizedEntityContainer<GameObject> {

	protected Object subEntitiesLock = new Object();
	protected List<GameObject> subEntities = Collections.synchronizedList(new ArrayList<>());

	protected ParentAwareComponent parent;

	protected TerrainEdgeObject terrainEdgeObject;
	protected TerrainHighlightObject terrainHighlightObject;
	protected GameObject terrainWaterObject;

	public TerrainObject(final String str, final TerrainMesh mesh) {
		super(str, mesh);
		this.setIsEntityMaterialId(false);
		this.setObjectIdLocation(AttributeLocation.MESH);
		this.setTransform(new Transform3D());
	}

	public Vector2i pickTerrainCell(final Camera3D cam, final Vector2f mousePos, final int windowWidth, final int windowHeight) {
		final TerrainMesh tMesh = this.getMesh();
		final float cellSize = 1f;

		// 1. Convert mouse to NDC [-1,1]
		final float ndcX = (mousePos.x / windowWidth) * 2f - 1f;
		final float ndcY = 1f - (mousePos.y / windowHeight) * 2f;

		// 2. Unproject to world space
		final Matrix4f invProj = new Matrix4f(cam.getProjection().getProjectionMatrix()).invert();
		final Matrix4f invView = new Matrix4f(cam.getViewMatrix()).invert();

		final Vector4f clipCoords = new Vector4f(ndcX, ndcY, -1f, 1f);
		final Vector4f eyeCoords = invProj.transform(clipCoords);
		eyeCoords.z = -1f;
		eyeCoords.w = 0f;

		final Vector4f rayWorld4 = invView.transform(eyeCoords);
		final Vector3f rayDir = new Vector3f(rayWorld4.x, rayWorld4.y, rayWorld4.z).normalize();
		final Vector3f rayOrigin = cam.getPosition();

		// 3. Transform ray into mesh-local space
		final Matrix4f invMesh = new Matrix4f(this.getTransform().getMatrix()).invert();
		final Vector4f localOrigin4 = invMesh.transform(new Vector4f(rayOrigin, 1f));
		final Vector3f localOrigin = new Vector3f(localOrigin4.x, localOrigin4.y, localOrigin4.z);

		final Vector4f localDir4 = invMesh
				.transform(new Vector4f(rayOrigin.x + rayDir.x, rayOrigin.y + rayDir.y, rayOrigin.z + rayDir.z, 1f));
		final Vector3f localDir = new Vector3f(localDir4.x - localOrigin.x, localDir4.y - localOrigin.y, localDir4.z - localOrigin.z)
				.normalize();

		// Convert hit point on XZ plane to grid
		// Ray-plane intersection at Y = max height
		final float yMax = tMesh.getMaxHeight() * cellSize;
		final float yMin = tMesh.getMinHeight() * cellSize;

		// Sweep from top to bottom
		for (float yLevel = yMax; yLevel >= yMin; yLevel -= cellSize) {
			final float levelY = yLevel * cellSize;

			if (localDir.y == 0f) {
				continue; // parallel, skip
			}

			final float t = (levelY - localOrigin.y) / localDir.y;
			if (t < 0) {
				continue; // behind camera
			}

			final Vector3f hitLocal = new Vector3f(localOrigin.x + localDir.x * t, levelY, localOrigin.z + localDir.z * t);

			final int gridX = (int) Math.floor(hitLocal.x / cellSize);
			final int gridZ = (int) Math.floor(hitLocal.z / cellSize);

			if (!tMesh.isInBounds(gridX, gridZ)) {
				continue;
			}

			final int cellHeight = tMesh.getCellHeight(gridX, gridZ);
			if (yLevel <= cellHeight) {
				// Found first cube that intersects
				return new Vector2i(gridX, gridZ);
			}
		}

		return null; // nothing hit
	}

	public Vector3f getCellPosition(final Vector2i tile) {
		final Vector3f meshTranslation = super.getTransform().getTranslation();
		final int cellHeight = this.getMesh().getCellHeight(tile.x, tile.y);
		return new Vector3f(meshTranslation.x + tile.x + 0.5f, meshTranslation.y + cellHeight, meshTranslation.z + tile.y + 0.5f);
	}

	public Vector2i getCellPosition(final Vector3f pos) {
		final Vector3f scaledPos = pos.sub(0.5f, 0, 0.5f, new Vector3f()).sub(this.getTransform().getTranslation());
		return new Vector2i((int) scaledPos.x, (int) scaledPos.z);
	}

	@Override
	public TerrainMesh getMesh() {
		return (TerrainMesh) super.getMesh();
	}

	public void setTerrainHighlightEntity(final TerrainHighlightObject terrainHighlightObject) {
		this.replace(this.terrainHighlightObject, terrainHighlightObject);
		this.terrainHighlightObject = terrainHighlightObject;
	}

	public void setTerrainEdgeEntity(final TerrainEdgeObject terrainEdgeObject) {
		this.replace(this.terrainEdgeObject, terrainEdgeObject);
		this.terrainEdgeObject = terrainEdgeObject;
	}

	public TerrainEdgeObject getTerrainEdgeObject() {
		return this.terrainEdgeObject;
	}

	public TerrainHighlightObject getTerrainHighlightObject() {
		return this.terrainHighlightObject;
	}

	public <T extends GameObject> void setWaterLevel(final T terrainWaterObject) {
		this.replace(this.terrainWaterObject, terrainWaterObject);
		this.terrainWaterObject = terrainWaterObject;
	}

	@Override
	public <T extends ParentAwareComponent> void setParent(final T e) {
		this.parent = e;
	}

	@Override
	public ParentAwareComponent getParent() {
		return this.parent;
	}

	@Override
	public Object getEntitiesLock() {
		return this.subEntitiesLock;
	}

	@Override
	public List<GameObject> getWEntities() {
		return this.subEntities;
	}

	@Override
	public List<GameObject> getROEntities() {
		return List.copyOf(this.subEntities);
	}

}
