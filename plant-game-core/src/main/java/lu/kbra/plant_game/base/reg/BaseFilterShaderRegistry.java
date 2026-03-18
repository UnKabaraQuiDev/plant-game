package lu.kbra.plant_game.base.reg;

import lu.kbra.plant_game.engine.render.shader.compute.filter.BlurShader;
import lu.kbra.plant_game.engine.render.shader.compute.filter.VignetteShader;
import lu.kbra.plant_game.plugin.PluginDescriptor;
import lu.kbra.plant_game.plugin.exception.RegistryFailedException;
import lu.kbra.plant_game.plugin.registry.FilterShaderRegistry;

public class BaseFilterShaderRegistry extends FilterShaderRegistry {

	public BaseFilterShaderRegistry(final PluginDescriptor pluginDescriptor) {
		super(pluginDescriptor);
	}

	@Override
	public void register() throws RegistryFailedException {
		this.register(BlurShader::new);
		this.register(VignetteShader::new);
	}

}
