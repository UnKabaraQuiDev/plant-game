package lu.kbra.plant_game.engine.entity.go.obj.terrain;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector4f;

import lu.kbra.plant_game.engine.entity.go.impl.GameObject;
import lu.kbra.plant_game.engine.entity.go.mesh.terrain.TerrainMesh;
import lu.kbra.plant_game.engine.mesh.data.AttributeLocation;
import lu.kbra.standalone.gameengine.objs.entity.components.SubEntitiesComponent;
import lu.kbra.standalone.gameengine.scene.camera.Camera3D;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class TerrainObject extends GameObject {

	protected SubEntitiesComponent<GameObject> subEntitiesComponent;

	public TerrainObject(final String str, final TerrainMesh mesh) {
		super(str, mesh, new Transform3D());
		this.setEntityMaterialId(false);
		this.setObjectIdLocation(AttributeLocation.MESH);
		super.addComponent(this.subEntitiesComponent = new SubEntitiesComponent<>(new ArrayList<>()));
	}

	public Vector2i pickTerrainCell(final Camera3D cam, final Vector2f mousePos, final int windowWidth, final int windowHeight) {
		final TerrainMesh tMesh = this.getMesh();
		final int cellSize = tMesh.getCellSize();

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
		final int yMax = tMesh.getMaxHeight() * cellSize;
		final int yMin = tMesh.getMinHeight() * cellSize;

		// Sweep from top to bottom
		for (int yLevel = yMax; yLevel >= yMin; yLevel--) {
			final float levelY = yLevel * cellSize;

			if (localDir.y == 0f) {
				continue; // parallel, skip
			}

			final float t = (levelY - localOrigin.y) / localDir.y;
			if (t < 0) {
				continue; // behind camera
			}

			final Vector3f hitLocal = new Vector3f(localOrigin.x + localDir.x * t, levelY, localOrigin.z + localDir.z * t);

			final int gridX = Math.floorDiv((int) hitLocal.x, tMesh.getCellSize());
			final int gridZ = Math.floorDiv((int) hitLocal.z, tMesh.getCellSize());

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

	@Override
	public TerrainMesh getMesh() {
		return (TerrainMesh) super.getMesh();
	}

	public SubEntitiesComponent<GameObject> getSubEntitiesComponent() {
		return this.subEntitiesComponent;
	}

	public List<GameObject> getSubEntities() {
		return this.subEntitiesComponent == null ? null : this.subEntitiesComponent.getEntities();
	}

	public Object getSubEntitiesLock() {
		return this.subEntitiesComponent == null ? null : this.subEntitiesComponent.getEntitiesLock();
	}

}
