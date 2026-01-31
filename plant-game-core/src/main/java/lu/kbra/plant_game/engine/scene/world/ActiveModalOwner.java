package lu.kbra.plant_game.engine.scene.world;

public interface ActiveModalOwner {

	Modal getActiveModal();

	void setActiveModal(Modal activeModal);

	default boolean hasActiveModal() {
		return this.getActiveModal() != null;
	}

}
