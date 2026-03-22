package lu.kbra.plant_game.engine.scene.world.modal;

public interface ActiveModalController extends ActiveModalOwner, ModalsOwner {

	default void registerModal(final Modal m) {
		m.setParent(this);
		this.getModals().put(m.getClass(), m);
	}

	default void stopModal() {
		if (this.getActiveModal() == null) {
			throw new IllegalStateException("No active modal to stop.");
		}
		this.getActiveModal().stop();
		this.setActiveModal(null);
	}

	default void startModal(final Modal m) {
		if (this.getActiveModal() != null) {
			throw new IllegalStateException("Modal: " + this.getActiveModal() + " already active.");
		}
		this.setActiveModal(m);
		this.startModal();
	}

	default void startModal() {
		if (this.getActiveModal() == null) {
			throw new IllegalStateException("No active modal to start.");
		}
		this.getActiveModal().start();
	}

	default void cancelModal() {
		if (this.getActiveModal() == null) {
			throw new IllegalStateException("No active modal to cancel.");
		}
		this.getActiveModal().cancel();
		this.getActiveModal().stop();
		this.setActiveModal(null);
	}

}
