package lu.kbra.plant_game.engine.util.exceptions;

public class GameObjectException extends RuntimeException {

	public GameObjectException() {
	}

	public GameObjectException(final String message, final Throwable cause, final boolean enableSuppression,
			final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public GameObjectException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public GameObjectException(final String message) {
		super(message);
	}

	public GameObjectException(final Throwable cause) {
		super(cause);
	}

}
