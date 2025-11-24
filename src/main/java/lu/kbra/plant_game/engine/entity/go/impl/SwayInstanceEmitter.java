package lu.kbra.plant_game.engine.entity.go.impl;

import java.util.function.Function;

import lu.kbra.plant_game.engine.render.SwayMesh;
import lu.kbra.standalone.gameengine.cache.attrib.AttribArray;
import lu.kbra.standalone.gameengine.geom.instance.InstanceEmitter;
import lu.kbra.standalone.gameengine.utils.transform.Transform;

public class SwayInstanceEmitter extends InstanceEmitter {

	public SwayInstanceEmitter(
			final String name,
			final SwayMesh mesh,
			final int count,
			final Function<Integer, Transform> baseTransform,
			final AttribArray... attribs) {
		super(name, mesh, count, baseTransform, attribs);
		// TODO Auto-generated constructor stub
	}

	public SwayInstanceEmitter(
			final String name,
			final SwayMesh mesh,
			final int count,
			final Transform baseTransform,
			final AttribArray... attribs) {
		super(name, mesh, count, baseTransform, attribs);
		// TODO Auto-generated constructor stub
	}

}
