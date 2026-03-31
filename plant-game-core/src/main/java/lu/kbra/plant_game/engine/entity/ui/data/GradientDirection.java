package lu.kbra.plant_game.engine.entity.ui.data;

public enum GradientDirection {

	UV_X(1),
	UV_Y(2),
	OBJ_X(3),
	OBJ_Y(4),
	OBJ_Z(5),
	WORLD_X(6),
	WORLD_Y(7),
	WORLD_Z(8),
	VIEW_X(9),
	VIEW_Y(10),
	VIEW_Z(11);

	private int id;

	private GradientDirection(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

}
