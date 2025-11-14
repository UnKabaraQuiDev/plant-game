package lu.kbra.plant_game.engine.locale;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;

public class LocalizationService {

	public static LocalizationService INSTANCE;

	private Locale currentLocale;
	private Properties props;

	public LocalizationService(Locale locale) throws IOException {
		this.currentLocale = locale;

		reload();
	}

	public void reload() throws IOException {
		final String resourcePath = "localization/" + currentLocale.toString() + ".properties";
		props = new Properties();

		final ClassLoader cl = Thread.currentThread().getContextClassLoader();
		final Enumeration<URL> resources = cl.getResources(resourcePath);

		while (resources.hasMoreElements()) {
			final URL url = resources.nextElement();
			try (InputStream in = url.openStream()) {
				final Properties cprop = new Properties();
				cprop.load(in);
				props.putAll(cprop);
			}
		}
	}

	public LocalizationService(Locale locale, Properties props) {
		this.currentLocale = locale;
		this.props = props;
	}

	public String get_(String key) {
		return props.getProperty(key, key);
	}

	public static String get(String key) {
		return key.isEmpty() ? "" : INSTANCE.get_(key);
	}

	public Properties getProps() {
		return props;
	}

	public Locale getCurrentLocale() {
		return currentLocale;
	}

	public void setCurrentLocale(Locale currentLocale) {
		this.currentLocale = currentLocale;
	}

	public void setProps(Properties props) {
		this.props = props;
	}

}
