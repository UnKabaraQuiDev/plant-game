package lu.kbra.plant_game.engine.scene.ui.menu.main;

import java.awt.Shape;

import javax.swing.GroupLayout.Alignment;

import org.joml.Vector2f;

import lu.kbra.plant_game.engine.entity.ui.impl.UIObject;
import lu.kbra.plant_game.engine.locale.NoMeshObject;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.utils.GameEngineUtils;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class SpacerUIObject extends UIObject implements NoMeshObject {

	private final Vector2f size;

	public SpacerUIObject(final String str, final Mesh mesh, final Vector2f size) {
		super(str, mesh);
		this.size = size;
	}

	public SpacerUIObject(final String str, final Mesh mesh, final Transform3D transform, final Vector2f size) {
		super(str, mesh, transform);
		this.size = size;
	}

	@Override
	public Shape getBounds() {
		return GameEngineUtils.toRectangleBounds(this.size, Alignment.CENTER, Alignment.CENTER);
	}

}
