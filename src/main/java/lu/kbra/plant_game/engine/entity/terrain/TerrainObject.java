package lu.kbra.plant_game.engine.entity.terrain;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector4f;

import lu.kbra.plant_game.engine.entity.impl.GameObject;
import lu.kbra.plant_game.engine.mesh.AttributeLocation;
import lu.kbra.standalone.gameengine.scene.camera.Camera3D;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class TerrainObject extends GameObject {

	public TerrainObject(String str, TerrainMesh mesh) {
		super(str, mesh, new Transform3D());
		setEntityMaterialId(false);
		setObjectIdLocation(AttributeLocation.MESH);
	}

	public Vector2i pickTerrainCell(Camera3D cam, Vector2f mousePos, int windowWidth, int windowHeight) {
		final TerrainMesh tMesh = this.getMesh();
		final int cellSize = tMesh.getCellSize();

		// 1. Convert mouse to NDC [-1,1]
		float ndcX = ((float) mousePos.x / windowWidth) * 2f - 1f;
		float ndcY = 1f - ((float) mousePos.y / windowHeight) * 2f;

		// 2. Unproject to world space
		Matrix4f invProj = new Matrix4f(cam.getProjection().getProjectionMatrix()).invert();
		Matrix4f invView = new Matrix4f(cam.getViewMatrix()).invert();

		Vector4f clipCoords = new Vector4f(ndcX, ndcY, -1f, 1f);
		Vector4f eyeCoords = invProj.transform(clipCoords);
		eyeCoords.z = -1f;
		eyeCoords.w = 0f;

		Vector4f rayWorld4 = invView.transform(eyeCoords);
		Vector3f rayDir = new Vector3f(rayWorld4.x, rayWorld4.y, rayWorld4.z).normalize();
		Vector3f rayOrigin = cam.getPosition();

		// 3. Transform ray into mesh-local space
		Matrix4f invMesh = new Matrix4f(this.getTransform().getMatrix()).invert();
		Vector4f localOrigin4 = invMesh.transform(new Vector4f(rayOrigin, 1f));
		Vector3f localOrigin = new Vector3f(localOrigin4.x, localOrigin4.y, localOrigin4.z);

		Vector4f localDir4 = invMesh
				.transform(new Vector4f(rayOrigin.x + rayDir.x, rayOrigin.y + rayDir.y, rayOrigin.z + rayDir.z, 1f));
		Vector3f localDir = new Vector3f(localDir4.x - localOrigin.x, localDir4.y - localOrigin.y,
				localDir4.z - localOrigin.z).normalize();

		// Convert hit point on XZ plane to grid
		// Ray-plane intersection at Y = max height
		int yMax = tMesh.getMaxHeight() * cellSize;
		int yMin = tMesh.getMinHeight() * cellSize;

		// Sweep from top to bottom
		for (int yLevel = yMax; yLevel >= yMin; yLevel--) {
			float levelY = yLevel * cellSize;

			if (localDir.y == 0f) {
				continue; // parallel, skip
			}

			float t = (levelY - localOrigin.y) / localDir.y;
			if (t < 0) {
				continue; // behind camera
			}

			Vector3f hitLocal = new Vector3f(localOrigin.x + localDir.x * t, levelY, localOrigin.z + localDir.z * t);

			int gridX = Math.floorDiv((int) hitLocal.x, tMesh.getCellSize());
			int gridZ = Math.floorDiv((int) hitLocal.z, tMesh.getCellSize());

			if (!tMesh.isInBounds(gridX, gridZ))
				continue;

			int cellHeight = tMesh.getCellHeight(gridX, gridZ);
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

}
