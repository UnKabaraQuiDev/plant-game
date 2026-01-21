package lu.kbra.plant_game.engine.entity.ui.impl;

public interface IndexOwner extends Comparable<IndexOwner> {

	int getIndex();

	@Override
	default int compareTo(final IndexOwner o) {
		return Integer.compare(this.getIndex(), o.getIndex());
	}

}
