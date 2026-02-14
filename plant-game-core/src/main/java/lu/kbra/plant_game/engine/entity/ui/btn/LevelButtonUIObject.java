package lu.kbra.plant_game.engine.entity.ui.btn;

import org.joml.Vector4fc;

import lu.kbra.pclib.PCUtils;
import lu.kbra.pclib.logger.GlobalLogger;
import lu.kbra.plant_game.base.scene.menu.main.MainMenuUIScene;
import lu.kbra.plant_game.engine.entity.impl.TintOwner;
import lu.kbra.plant_game.engine.entity.ui.TexturedQuadMeshUIObject;
import lu.kbra.plant_game.engine.entity.ui.data.HoverState;
import lu.kbra.plant_game.engine.entity.ui.impl.NeedsClick;
import lu.kbra.plant_game.engine.entity.ui.impl.NeedsHover;
import lu.kbra.plant_game.engine.entity.ui.impl.NeedsUpdate;
import lu.kbra.plant_game.engine.entity.ui.impl.UISceneParentAware;
import lu.kbra.plant_game.engine.mesh.TexturedQuadMesh;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.plant_game.engine.util.annotation.TextureOption;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.plant_game.plugin.registry.LevelRegistry.LevelDefinition;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextureFilter;

@DataPath("image:classpath:/icons/star-32.png")
@TextureOption(textureFilter = TextureFilter.NEAREST)
public class LevelButtonUIObject extends TexturedQuadMeshUIObject
		implements NeedsClick, NeedsHover, NeedsUpdate, TintOwner, Comparable<LevelButtonUIObject>, UISceneParentAware {

	private LevelDefinition levelDefinition;
	private float angularSpeed = PCUtils.randomFloatRange(-5, 5) / 200;

	public LevelButtonUIObject(final String str, final TexturedQuadMesh mesh) {
		super(str, mesh);
	}

	@Override
	public void click(final WindowInputHandler input) {
		this.getUISceneParent().filter(MainMenuUIScene.class::isInstance).map(MainMenuUIScene.class::cast).ifPresentOrElse(scene -> {
			scene.getResumeInfoGroup().accept(this.levelDefinition);
			scene.startTransition(MainMenuUIScene.RESUME);
		}, () -> GlobalLogger.warning("No MainMenuUIScene in hierarchy."));
	}

	@Override
	public boolean hover(final WindowInputHandler input, final HoverState hoverState) {
		if (hoverState == HoverState.ENTER) {
			this.getUISceneParent()
					.filter(MainMenuUIScene.class::isInstance)
					.map(MainMenuUIScene.class::cast)
					.ifPresentOrElse(scene -> scene.getPlayInfoGroup().accept(this.levelDefinition),
							() -> GlobalLogger.warning("No MainMenuUIScene in hierarchy."));
		} else if (hoverState == HoverState.LEAVE) {
			this.getUISceneParent()
					.filter(MainMenuUIScene.class::isInstance)
					.map(MainMenuUIScene.class::cast)
					.ifPresentOrElse(scene -> scene.getPlayInfoGroup().setActive(false),
							() -> GlobalLogger.warning("No MainMenuUIScene in hierarchy."));
		}
		return true;
	}

	@Override
	public void update(final WindowInputHandler input) {
		if (!this.hasTransform()) {
			return;
		}
		this.getTransform().getRotation().rotateY(this.angularSpeed * input.dTime());
		this.getTransform().updateMatrix();
	}

//	@Override
//	public Shape getTransformedBounds() {
//		return TransformedBoundsOwner.getTransformedBounds(this.getBounds(),
//				new Matrix4f().translation(this.getTransform().getTranslation()));
//	}

	@Override
	public Vector4fc getTint() {
		return this.levelDefinition.getLevelState().getColor();
	}

	@Override
	public void setTint(final Vector4fc tint) {
		throw new UnsupportedOperationException();
	}

	public float getAngularSpeed() {
		return this.angularSpeed;
	}

	public void setAngularSpeed(final float angularSpeed) {
		this.angularSpeed = angularSpeed;
	}

	public void setLevelDefinition(final LevelDefinition levelDefinition) {
		this.levelDefinition = levelDefinition;
	}

	public LevelDefinition getLevelDefinition() {
		return this.levelDefinition;
	}

	@Override
	public int compareTo(final LevelButtonUIObject o) {
		return this.levelDefinition.compareTo(o.levelDefinition);
	}

	@Override
	public String toString() {
		return "LevelButtonUIObject@" + System.identityHashCode(this) + " [levelDefinition=" + this.levelDefinition + ", angularSpeed="
				+ this.angularSpeed + "]";
	}

}
