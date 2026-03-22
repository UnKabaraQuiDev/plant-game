package lu.kbra.plant_game.engine.render.filter;

import lu.kbra.plant_game.engine.render.shader.compute.filter.FilterShader;

public abstract class FilterShaderConfiguration<T extends FilterShader<?>> {

	public abstract Class<T> getShaderClass();

	public abstract void apply(T filterShader);

}
