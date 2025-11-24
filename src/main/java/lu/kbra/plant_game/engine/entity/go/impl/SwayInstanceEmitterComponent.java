package lu.kbra.plant_game.engine.entity.go.impl;

import lu.kbra.standalone.gameengine.objs.entity.Component;

public class SwayInstanceEmitterComponent extends Component {

	protected SwayInstanceEmitter instanceEmitter;

	public SwayInstanceEmitterComponent(final SwayInstanceEmitter instanceEmitter) {
		this.instanceEmitter = instanceEmitter;
	}

	public SwayInstanceEmitter getInstanceEmitter() {
		return this.instanceEmitter;
	}

	public void setInstanceEmitter(final SwayInstanceEmitter instanceEmitter) {
		this.instanceEmitter = instanceEmitter;
	}

}
