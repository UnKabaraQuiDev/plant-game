package lu.kbra.plant_game.engine.entity.ui.btn;

import org.joml.Vector4fc;

import lu.pcy113.pclib.PCUtils;

import lu.kbra.plant_game.engine.entity.ui.impl.NeedsClick;
import lu.kbra.plant_game.engine.entity.ui.impl.NeedsUpdate;
import lu.kbra.plant_game.engine.entity.ui.impl.TextureOption;
import lu.kbra.plant_game.engine.entity.ui.texture.TextureUIObject;
import lu.kbra.plant_game.engine.mesh.TexturedQuadMesh;
import lu.kbra.plant_game.engine.mesh.TintOwner;
import lu.kbra.plant_game.engine.scene.world.LevelState;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.standalone.gameengine.scene.Scene;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextureFilter;

@DataPath("image:classpath:/icons/star-32.png")
@TextureOption(textureFilter = TextureFilter.NEAREST)
public class LevelButtonUIObject extends TextureUIObject implements NeedsClick, NeedsUpdate, TintOwner, Comparable<LevelButtonUIObject> {

	private LevelState state = LevelState.NOT_STARTED;
	private String levelId;
	private float angularSpeed = PCUtils.randomFloatRange(0.01f, 0.05f) / 50;

	public LevelButtonUIObject(final String str, final TexturedQuadMesh mesh) {
		super(str, mesh);
	}

	@Override
	public void click(final WindowInputHandler input) {
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

	public LevelState getState() {
		return this.state;
	}

	public void setState(final LevelState state) {
		this.state = state;
	}

	public float getAngularSpeed() {
		return this.angularSpeed;
	}

	public void setAngularSpeed(final float angularSpeed) {
		this.angularSpeed = angularSpeed;
	}

	public void setLevelId(final String levelId) {
		this.levelId = levelId;
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

	@Override
	public String toString() {
		return "LevelButtonUIObject [state=" + this.state + ", levelId=" + this.levelId + ", angularSpeed=" + this.angularSpeed
				+ ", bounds=" + this.bounds + ", mesh=" + this.mesh + ", transform=" + this.transform + ", parent=" + this.parent
				+ ", active=" + this.active + ", name=" + this.name + "]";
	}

}
