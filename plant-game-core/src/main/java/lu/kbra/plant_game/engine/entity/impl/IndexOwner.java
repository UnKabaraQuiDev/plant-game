package lu.kbra.plant_game.engine.entity.impl;

import java.util.Comparator;

public interface IndexOwner {

	Comparator<IndexOwner> COMPARATOR = Comparator.comparingInt(IndexOwner::getIndex);

	int getIndex();

}
