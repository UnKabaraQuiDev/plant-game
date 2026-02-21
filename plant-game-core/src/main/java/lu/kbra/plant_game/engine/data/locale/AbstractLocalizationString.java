package lu.kbra.plant_game.engine.data.locale;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractLocalizationString implements ComposedLocalizable {

	protected final List<Localizable> params = new ArrayList<>();
	protected String computed;

	@Override
	public List<Localizable> getParams() {
		return this.params;
	}

	@Override
	public abstract String getLocalizationKey();

	@Override
	public AbstractLocalizationString addParam(final Localizable param) {
		this.params.add(param);
		this.computed = null;
		return this;
	}

	@Override
	public String getLocalizationValue() {
		if (this.computed == null) {
			String base = ComposedLocalizable.super.getLocalizationValue();

			for (int i = 0; i < this.params.size(); i++) {
				final String placeholder = "{" + i + "}";
				final String value = this.params.get(i).getLocalizationValue();
				base = base.replace(placeholder, value);
			}

			this.computed = base;
		}

		return this.computed;
	}

	public ComposedLocalizable clearComputed() {
		this.computed = null;
		return this;
	}

	@Override
	public String toString() {
		return "AbstractLocalizationString@" + System.identityHashCode(this) + " [params=" + this.params + ", computed=" + this.computed
				+ ", getLocalizationKey()=" + this.getLocalizationKey() + "]";
	}

}
