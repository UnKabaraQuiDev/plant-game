package lu.kbra.plant_game.engine.scene.menu.pause;

import java.util.Optional;
import java.util.OptionalInt;

import org.joml.Vector2f;
import org.joml.Vector2fc;

import lu.kbra.pclib.concurrency.ObjectTriggerLatch;
import lu.kbra.pclib.pointer.ObjectPointer;
import lu.kbra.plant_game.engine.entity.ui.bar.AnchoredProgressBarUIObject;
import lu.kbra.plant_game.engine.entity.ui.btn.ResumeButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.btn.ReturnButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.factory.UIObjectFactory;
import lu.kbra.plant_game.engine.entity.ui.group.MarginAnchoredUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.play.PlayInfoUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.resume.ResumeResourcesUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.stat_line.integer.ExtAnchoredIntegerStatLine;
import lu.kbra.plant_game.engine.scene.ui.UIScene;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor2D;
import lu.kbra.plant_game.engine.scene.ui.layout.FlowLayout;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextAlignment;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class PauseUIScene extends UIScene {

	protected MarginAnchoredUIObjectGroup buttonsMenuGroup = new MarginAnchoredUIObjectGroup("resume.buttons",
			new FlowLayout(true, 0.06f, Anchor2D.LEADING),
			this,
			Anchor.CENTER_LEFT,
			Anchor.CENTER_LEFT,
			0.25f);

	protected PlayInfoUIObjectGroup playInfoUIObjectGroup;
	protected ResumeResourcesUIObjectGroup resourcesGroup = new ResumeResourcesUIObjectGroup(this);

	protected AnchoredProgressBarUIObject progressBar;
	protected ExtAnchoredIntegerStatLine progressGroup;

	protected ObjectPointer<ResumeButtonUIObject> resumeButtonObject = new ObjectPointer<>();
	protected ObjectPointer<ReturnButtonUIObject> returnButtonObject = new ObjectPointer<>();

	public PauseUIScene(final CacheManager parent) {
		super("game-pause", parent);
	}

	public ObjectTriggerLatch<PauseUIScene> init() {
		final Optional<Vector2fc> SMALL_TEXT_CHAR_SIZE = Optional.of(new Vector2f(0.2f));
		final Optional<TextAlignment> SMALL_TEXT_TEXT_ALIGNMENT = Optional.of(TextAlignment.TEXT_CENTER);

		final ObjectTriggerLatch<PauseUIScene> latch = new ObjectTriggerLatch<>(7, this);

		this.playInfoUIObjectGroup = new PlayInfoUIObjectGroup(this);

		UIObjectFactory
				.createText(ResumeButtonUIObject.class,
						OptionalInt.empty(),
						SMALL_TEXT_CHAR_SIZE,
						SMALL_TEXT_TEXT_ALIGNMENT,
						Optional.empty(),
						Optional.empty())
				.set(i -> i.setTransform(new Transform3D()))
				.get(this.resumeButtonObject)
				.add(this.buttonsMenuGroup)
				.latch(latch)
				.push();

		return latch;
	}

}
