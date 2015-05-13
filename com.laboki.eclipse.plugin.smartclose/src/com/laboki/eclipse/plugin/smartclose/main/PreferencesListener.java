package com.laboki.eclipse.plugin.smartclose.main;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent;

import com.laboki.eclipse.plugin.smartclose.events.PreferencesChangedEvent;
import com.laboki.eclipse.plugin.smartclose.instance.EventBusInstance;
import com.laboki.eclipse.plugin.smartclose.instance.Instance;
import com.laboki.eclipse.plugin.smartclose.preferences.Store;
import com.laboki.eclipse.plugin.smartclose.task.Task;
import com.laboki.eclipse.plugin.smartclose.task.TaskMutexRule;

public final class PreferencesListener extends EventBusInstance
	implements
		IPreferenceChangeListener {

	private static final int DELAY = 125;
	private static final TaskMutexRule RULE = new TaskMutexRule();
	private static final IEclipsePreferences PREFERENCES =
		Store.getPreferences();

	@Override
	public void
	preferenceChange(final PreferenceChangeEvent event) {
		new Task() {

			@Override
			public void
			execute() {
				EventBus.post(new PreferencesChangedEvent());
			}
		}.setRule(PreferencesListener.RULE)
			.setDelay(PreferencesListener.DELAY)
			.start();
	}

	@Override
	public Instance
	start() {
		PreferencesListener.PREFERENCES.addPreferenceChangeListener(this);
		return super.start();
	}

	@Override
	public Instance
	stop() {
		PreferencesListener.PREFERENCES.removePreferenceChangeListener(this);
		return super.stop();
	}
}
