package org.pkframework.core.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesUtils {

	private static Logger LOGGER = LoggerFactory.getLogger(PropertiesUtils.class);

	private static final String DEFAULT_PROPERTIES = "config.properties";
	private static final int DEFAULT_INT_VALUE = 0;

	private static final String TRUE = "true";
	private static final String COMMA = ",";

	private static Map<String, Properties> propertiesMap = new HashMap<String, Properties>();

	private static Properties getProperties(String propertiesName) {
		if (!propertiesMap.containsKey(propertiesName)) {
			Properties properties = new Properties();

			try {
				InputStream propertiesStream = PropertiesUtils.class.getResourceAsStream("/config/" + propertiesName);

				properties.load(propertiesStream);
			} catch (IOException e) {
				LOGGER.error("cannot find properties file", e);
			}

			propertiesMap.put(propertiesName, properties);
		}

		return propertiesMap.get(propertiesName);
	}

	public static String getString(String key) {
		return getString(DEFAULT_PROPERTIES, key);
	}

	public static String getString(String propertiesName, String key) {
		return StringUtils.defaultIfEmpty(getProperties(propertiesName).getProperty(key), StringUtils.EMPTY);
	}

	public static String[] getStringArray(String key) {
		return getStringArray(DEFAULT_PROPERTIES, key, COMMA);
	}

	public static String[] getStringArray(String key, String separator) {
		return getStringArray(DEFAULT_PROPERTIES, key, separator);
	}

	public static String[] getStringArray(String propertiesName, String key, String separator) {
		String rawString = getString(propertiesName, key);

		if (rawString == null) {
			return null;
		}

		return StringUtils.split(rawString, separator);
	}

	public static int getInt(String key) {
		return getInt(DEFAULT_PROPERTIES, key, DEFAULT_INT_VALUE);
	}

	public static int getInt(String key, int defaultValue) {
		return getInt(DEFAULT_PROPERTIES, key, defaultValue);
	}

	public static int getInt(String propertiesName, String key) {
		return getInt(propertiesName, key, DEFAULT_INT_VALUE);
	}

	public static int getInt(String propertiesName, String key, int defaultValue) {
		String value = getString(propertiesName, key);

		try {
			defaultValue = Integer.parseInt(value);
		} catch (NumberFormatException e) {
			LOGGER.warn("invalid number type", e);
		}

		return defaultValue;
	}

	public static boolean getBoolean(String key) {
		return getBoolean(DEFAULT_PROPERTIES, key);
	}

	public static boolean getBoolean(String propertiesName, String key) {
		return TRUE.equals(getProperties(propertiesName).getProperty(key));
	}

}
