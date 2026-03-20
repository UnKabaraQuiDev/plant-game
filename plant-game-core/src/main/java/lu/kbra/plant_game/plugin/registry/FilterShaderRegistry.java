package lu.kbra.plant_game.plugin.registry;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import lu.kbra.plant_game.engine.render.shader.compute.filter.FilterShader;
import lu.kbra.plant_game.plugin.PluginDescriptor;
import lu.kbra.plant_game.plugin.exception.RegistryFailedException;

public abstract class FilterShaderRegistry extends PluginRegistry {

	public static final List<Supplier<? extends FilterShader<?>>> FILTER_SHADERS = new ArrayList<>();

	public FilterShaderRegistry(final PluginDescriptor pluginDescriptor) {
		super(pluginDescriptor);
	}

	protected <T extends FilterShader<T>> void register(final Supplier<T> supplier) {
		FILTER_SHADERS.add(() -> {
			final FilterShader fs = supplier.get();
			fs.setPluginDescriptor(this.pluginDescriptor);
			return fs;
		});
	}

	protected <T extends FilterShader<T>> void register(final Function<PluginDescriptor, T> supplier) {
		FILTER_SHADERS.add(() -> supplier.apply(this.pluginDescriptor));
	}

	@Deprecated
	protected <T extends FilterShader<T>> void register(final Class<T> clazz) throws RegistryFailedException {
		try {
			final Constructor<T> cons = clazz.getDeclaredConstructor(PluginDescriptor.class);
			FILTER_SHADERS.add(() -> {
				try {
					return cons.newInstance(this.pluginDescriptor);
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					throw new RuntimeException("Couldn't instance FilterShader: " + clazz, e);
				}
			});
		} catch (IllegalArgumentException | NoSuchMethodException | SecurityException e) {
			try {
				final Constructor<T> cons = clazz.getDeclaredConstructor();
				FILTER_SHADERS.add(() -> {
					try {
						final FilterShader<T> fs = cons.newInstance();
						fs.setPluginDescriptor(this.pluginDescriptor);
						return fs;
					} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e2) {
						throw new RuntimeException("Couldn't instance FilterShader: " + clazz, e2);
					}
				});
			} catch (IllegalArgumentException | NoSuchMethodException | SecurityException e1) {
				throw new RegistryFailedException(this.pluginDescriptor, "No suitable constructor found for FilterShader: " + clazz, e);
			}
		}
	}

	@Override
	public int getPriority() {
		return 600;
	}

}
