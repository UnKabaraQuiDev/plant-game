package lu.kbra.plant_game.engine.entity.factory;

import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;

public record ResolveContext(CacheManager cache, Dispatcher loader, Dispatcher render) {
}
