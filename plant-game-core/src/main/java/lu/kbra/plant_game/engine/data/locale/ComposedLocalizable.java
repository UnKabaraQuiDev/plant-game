package lu.kbra.plant_game.engine.data.locale;

import java.util.List;

public interface ComposedLocalizable extends Localizable {

	List<Localizable> getParams();

	ComposedLocalizable addParam(final Localizable param);

}
