package com.laboki.eclipse.plugin.smartclose.preferences;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.osgi.service.prefs.BackingStoreException;

import com.laboki.eclipse.plugin.smartclose.main.EditorContext;

public enum Store {
	INSTANCE;

	private static final int DEFAULT_NUMBER_OF_TABS = 5;
	private static final String NUMBER_OF_TABS = "SmartCloseNumberOfTabs";
	private static final long DEFAULT_DELAY_TIME = 300000;
	private static final String DELAY_TIME = "SmartCloseDelayTime";
	private static final Logger LOGGER = Logger.getLogger(Store.class.getName());

	public static int
	getNumberOfTabs() {
		return Store.getInt(Store.NUMBER_OF_TABS, Store.DEFAULT_NUMBER_OF_TABS);
	}

	public static void
	setNumberOfTabs(final int numberTabs) {
		Store.setInt(Store.NUMBER_OF_TABS, numberTabs);
	}

	public static long
	getDelayTime() {
		return Store.getLong(Store.DELAY_TIME, Store.DEFAULT_DELAY_TIME);
	}

	public static void
	setDelayTime(final long delayTime) {
		Store.setLong(Store.DELAY_TIME, delayTime);
	}

	private static int
	getInt(final String key, final int defaultValue) {
		final IEclipsePreferences pref = Store.getPreferences();
		Store.update(pref);
		return pref.getInt(key, defaultValue);
	}

	private static long
	getLong(final String key, final long defaultValue) {
		final IEclipsePreferences pref = Store.getPreferences();
		Store.update(pref);
		return pref.getLong(key, defaultValue);
	}

	private static void
	setInt(final String key, final int value) {
		final IEclipsePreferences pref = Store.getPreferences();
		pref.putInt(key, value);
		Store.update(pref);
	}

	private static void
	setLong(final String key, final long value) {
		final IEclipsePreferences pref = Store.getPreferences();
		pref.putLong(key, value);
		Store.update(pref);
	}

	public static void
	clear() {
		try {
			Store.tryToClear();
		}
		catch (final BackingStoreException e) {
			Store.LOGGER.log(Level.OFF, e.getMessage(), e);
		}
	}

	private static void
	tryToClear() throws BackingStoreException {
		final IEclipsePreferences pref = Store.getPreferences();
		pref.clear();
		Store.update(pref);
	}

	public static IEclipsePreferences
	getPreferences() {
		return ConfigurationScope.INSTANCE.getNode(EditorContext.PLUGIN_NAME);
	}

	private static void
	update(final IEclipsePreferences preferences) {
		try {
			Store.tryToUpdate(preferences);
		}
		catch (final BackingStoreException e) {
			Store.LOGGER.log(Level.OFF, e.getMessage(), e);
		}
	}

	private static void
	tryToUpdate(final IEclipsePreferences preferences)
		throws BackingStoreException {
		preferences.flush();
		preferences.sync();
	}
}
