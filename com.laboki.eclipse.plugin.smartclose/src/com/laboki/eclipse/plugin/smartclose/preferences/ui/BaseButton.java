package com.laboki.eclipse.plugin.smartclose.preferences.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.laboki.eclipse.plugin.smartclose.events.PreferencePageDisposedEvent;
import com.laboki.eclipse.plugin.smartclose.events.PreferencesChangedEvent;
import com.laboki.eclipse.plugin.smartclose.instance.EventBusInstance;
import com.laboki.eclipse.plugin.smartclose.instance.Instance;
import com.laboki.eclipse.plugin.smartclose.task.AsyncTask;
import com.laboki.eclipse.plugin.smartclose.task.TaskMutexRule;

public abstract class BaseButton extends EventBusInstance {

	protected final Button button;
	private final Composite parent;
	private SelectionListener listener;
	private static final TaskMutexRule RULE = new TaskMutexRule();

	public BaseButton(final Composite parent) {
		this.parent = parent;
		this.button = new Button(parent, SWT.FLAT);
		this.setProperties();
	}

	private void
	setProperties() {
		final GridData data = new GridData();
		data.verticalAlignment = SWT.CENTER;
		data.horizontalAlignment = SWT.BEGINNING;
		data.grabExcessHorizontalSpace = false;
		data.grabExcessVerticalSpace = false;
		this.button.setLayoutData(data);
		this.parent.layout();
		this.parent.pack();
	}

	@Override
	public Instance
	start() {
		this.startListening();
		return super.start();
	}

	private void
	startListening() {
		this.listener = new SelectionListener() {

			@Override
			public void
			widgetSelected(final SelectionEvent e) {
				BaseButton.this.showDialog();
			}

			@Override
			public void
			widgetDefaultSelected(final SelectionEvent e) {
				this.widgetSelected(e);
			}
		};
		this.button.addSelectionListener(this.listener);
	}

	protected abstract void
	showDialog();

	@Subscribe
	public void
	eventHandler(final PreferencesChangedEvent event) {
		new AsyncTask() {

			@Override
			public void
			execute() {
				BaseButton.this.updateText();
			}
		}.setRule(BaseButton.RULE).start();
	}

	@Subscribe
	@AllowConcurrentEvents
	public void
	eventHandler(final PreferencePageDisposedEvent event) {
		new AsyncTask() {

			@Override
			public void
			execute() {
				BaseButton.this.stop();
			}
		}.setRule(BaseButton.RULE).start();
	}

	protected abstract void
	updateText();

	@Override
	public Instance
	stop() {
		this.stopListening();
		return super.stop();
	}

	private void
	stopListening() {
		if ((this.button == null) || this.button.isDisposed()) return;
		this.button.removeSelectionListener(this.listener);
	}
}
