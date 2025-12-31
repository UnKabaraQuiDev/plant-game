package lu.kbra.plant_game.engine.entity.go.impl;

import java.util.function.IntFunction;

import lu.kbra.plant_game.engine.render.SwayMesh;
import lu.kbra.standalone.gameengine.cache.attrib.impl.AttribArray;
import lu.kbra.standalone.gameengine.geom.instance.InstanceEmitter;
import lu.kbra.standalone.gameengine.utils.transform.Transform;

@Deprecated
public class SwayInstanceEmitter extends InstanceEmitter {

	@Deprecated
	public SwayInstanceEmitter(final String name, final SwayMesh mesh, final int count, final IntFunction<Transform> baseTransform,
			final AttribArray... attribs) {
		super(name, mesh, count, baseTransform, attribs);
	}

	@Deprecated
	public SwayInstanceEmitter(final String name, final SwayMesh mesh, final int count, final Transform baseTransform,
			final AttribArray... attribs) {
		super(name, mesh, count, baseTransform, attribs);
	}

}
