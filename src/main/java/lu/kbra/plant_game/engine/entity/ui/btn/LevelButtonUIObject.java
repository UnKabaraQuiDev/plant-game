package lu.kbra.plant_game.engine.entity.ui.btn;

import org.joml.Vector4fc;

import lu.pcy113.pclib.PCUtils;

import lu.kbra.plant_game.engine.entity.ui.impl.NeedsClick;
import lu.kbra.plant_game.engine.entity.ui.impl.NeedsUpdate;
import lu.kbra.plant_game.engine.entity.ui.impl.TextureOption;
import lu.kbra.plant_game.engine.entity.ui.texture.TextureUIObject;
import lu.kbra.plant_game.engine.mesh.TexturedQuadMesh;
import lu.kbra.plant_game.engine.mesh.TintOwner;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.standalone.gameengine.scene.Scene;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextureFilter;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

@DataPath("image:classpath:/icons/star-32.png")
@TextureOption(textureFilter = TextureFilter.NEAREST)
public class LevelButtonUIObject extends TextureUIObject implements NeedsClick, NeedsUpdate, TintOwner, Comparable<LevelButtonUIObject> {

	private LevelState state = LevelState.NOT_STARTED;
	private final String levelId;
	private final float angularSpeed = PCUtils.randomFloatRange(0.01f, 0.05f) / 50;

	public LevelButtonUIObject(final String str, final TexturedQuadMesh mesh, final String levelId) {
		super(str, mesh);
		this.levelId = levelId;
	}

	public LevelButtonUIObject(final String str, final TexturedQuadMesh mesh, final Transform3D transform, final String levelId) {
		super(str, mesh, transform);
		this.levelId = levelId;
	}

	public LevelButtonUIObject(final String str, final TexturedQuadMesh mesh, final String levelId, final LevelState state) {
		super(str, mesh);
		this.levelId = levelId;
		this.state = state;
	}

	public LevelButtonUIObject(
			final String str,
			final TexturedQuadMesh mesh,
			final Transform3D transform,
			final String levelId,
			final LevelState state) {
		super(str, mesh, transform);
		this.levelId = levelId;
		this.state = state;
	}

	@Override
	public void click(final WindowInputHandler input, final float dTime, final Scene scene) {
	}

	@Override
	public void update(final float dTime, final Scene scene) {
		if (!this.hasTransform()) {
			return;
		}
		this.getTransform().getRotation().rotateY(this.angularSpeed);
		this.getTransform().updateMatrix();
	}

	@Override
	public Vector4fc getTint() {
		return this.state.getColor();
	}

	@Override
	public void setTint(final Vector4fc tint) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int compareTo(final LevelButtonUIObject o) {
		if (this == o) {
			return 0;
		}
		return this.levelId.compareTo(o.levelId);
	}

	public String getLevelId() {
		return this.levelId;
	}

}
