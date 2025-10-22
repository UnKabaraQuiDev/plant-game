package lu.kbra.plant_game.engine.scene.ui;

import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.scene.Scene2D;
import lu.kbra.standalone.gameengine.scene.camera.Camera;

public class UIScene extends Scene2D {

	private CacheManager uiCache;

	public UIScene(String name, CacheManager parent) {
		super(name);
		setCamera(Camera.orthographicCamera3D());
		this.uiCache = new CacheManager(name, parent);
	}

	public CacheManager getCache() {
		return uiCache;
	}

}
