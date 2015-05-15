package com.laboki.eclipse.plugin.smartclose.preferences.ui;

import org.eclipse.swt.widgets.Composite;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.laboki.eclipse.plugin.smartclose.events.ShowDelayDialogEvent;
import com.laboki.eclipse.plugin.smartclose.events.ShowTabsDialogEvent;
import com.laboki.eclipse.plugin.smartclose.instance.EventBusInstance;
import com.laboki.eclipse.plugin.smartclose.task.AsyncTask;

public final class DialogListener extends EventBusInstance {

	private final Composite parent;

	public DialogListener(final Composite parent) {
		this.parent = parent;
	}

	@Subscribe
	@AllowConcurrentEvents
	public void
	eventHandler(final ShowTabsDialogEvent event) {
		new AsyncTask() {

			@Override
			public void
			execute() {
				new TabsDialog(DialogListener.this.parent).start();
			}
		}.start();
	}

	@Subscribe
	@AllowConcurrentEvents
	public void
	eventHandler(final ShowDelayDialogEvent event) {
		new AsyncTask() {

			@Override
			public void
			execute() {
				new DelayDialog(DialogListener.this.parent).start();
			}
		}.start();
	}
}
