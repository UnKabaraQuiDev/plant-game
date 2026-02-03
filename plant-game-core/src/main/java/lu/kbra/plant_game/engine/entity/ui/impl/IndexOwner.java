package lu.kbra.plant_game.engine.entity.ui.impl;

import java.util.Comparator;

public interface IndexOwner /* extends Comparable<IndexOwner> */ {

	Comparator<IndexOwner> COMPARATOR = Comparator.comparingInt(IndexOwner::getIndex);

	int getIndex();

	/*
	 * @Override default int compareTo(final IndexOwner o) { return Integer.compare(this.getIndex(),
	 * o.getIndex()); }
	 */

}
