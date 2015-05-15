package com.laboki.eclipse.plugin.smartclose.preferences.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Spinner;

import com.google.common.eventbus.Subscribe;
import com.laboki.eclipse.plugin.smartclose.events.FocusDialogSpinnerEvent;
import com.laboki.eclipse.plugin.smartclose.events.PreferencePageDisposedEvent;
import com.laboki.eclipse.plugin.smartclose.events.PreferencesChangedEvent;
import com.laboki.eclipse.plugin.smartclose.instance.EventBusInstance;
import com.laboki.eclipse.plugin.smartclose.instance.Instance;
import com.laboki.eclipse.plugin.smartclose.task.AsyncTask;
import com.laboki.eclipse.plugin.smartclose.task.TaskMutexRule;

public abstract class BaseSpinner extends EventBusInstance {

	private final Spinner spinner;
	private final ModifyListener modifyListener = new SpinnerModifyListener();
	private final SpinnerTraverseListener traverseListener =
		new SpinnerTraverseListener();
	private static final TaskMutexRule RULE = new TaskMutexRule();

	public BaseSpinner(final Composite parent) {
		this.spinner = new Spinner(parent, SWT.BORDER | SWT.RIGHT);
	}

	@Override
	public Instance
	start() {
		this.updateProperties();
		this.startListening();
		return super.start();
	}

	private void
	updateProperties() {
		this.spinner.setTextLimit(this.getTextLimit());
		this.spinner.setValues((int) this.getStoreValue(),
			this.getSpinnerMinimum(),
			this.getSpinnerMaximum(),
			this.getSpinnerDigits(),
			this.getSpinnerIncrements(),
			this.getSpinnerPageIncrements());
		this.focus();
	}

	protected abstract int
	getTextLimit();

	protected abstract int
	getSpinnerMinimum();

	protected abstract int
	getSpinnerMaximum();

	protected abstract int
	getSpinnerDigits();

	protected abstract int
	getSpinnerIncrements();

	protected abstract int
	getSpinnerPageIncrements();

	public void
	startListening() {
		this.spinner.addModifyListener(this.modifyListener);
		this.spinner.addTraverseListener(this.traverseListener);
	}

	@Subscribe
	public void
	eventHandler(final PreferencesChangedEvent event) {
		new AsyncTask() {

			@Override
			public void
			execute() {
				BaseSpinner.this.updateSelection();
			}
		}.setRule(BaseSpinner.RULE).start();
	}

	protected void
	updateSelection() {
		if ((this.spinner == null) || this.spinner.isDisposed()) return;
		if (this.spinner.getSelection() == this.getStoreValue()) return;
		this.spinner.removeModifyListener(this.modifyListener);
		this.spinner.setSelection((int) this.getStoreValue());
		this.spinner.addModifyListener(this.modifyListener);
	}

	protected abstract long
	getStoreValue();

	@Subscribe
	public void
	eventHandler(final PreferencePageDisposedEvent event) {
		new AsyncTask() {

			@Override
			public void
			execute() {
				BaseSpinner.this.stop();
			}
		}.start();
	}

	@Subscribe
	public void
	eventHandler(final FocusDialogSpinnerEvent event) {
		new AsyncTask() {

			@Override
			public void
			execute() {
				BaseSpinner.this.focus();
			}
		}.setRule(BaseSpinner.RULE).start();
	}

	void
	focus() {
		if ((this.spinner == null) || this.spinner.isDisposed()) return;
		this.spinner.setFocus();
		this.spinner.forceFocus();
	}

	@Override
	public Instance
	stop() {
		this.spinner.dispose();
		return super.stop();
	}

	private final class SpinnerModifyListener implements ModifyListener {

		public SpinnerModifyListener() {}

		@Override
		public void
		modifyText(final ModifyEvent event) {
			new AsyncTask() {

				@Override
				public void
				execute() {
					BaseSpinner.this.setStoreValue(BaseSpinner.this.spinner.getSelection());
				}
			}.setRule(BaseSpinner.RULE).start();
		}
	}

	protected abstract void
	setStoreValue(long value);

	private final class SpinnerTraverseListener implements TraverseListener {

		public SpinnerTraverseListener() {}

		@Override
		public void
		keyTraversed(final TraverseEvent event) {
			if (event.detail == SWT.TRAVERSE_RETURN) new AsyncTask() {

				@Override
				public void
				execute() {
					BaseSpinner.this.spinner.getShell().close();
				}
			}.setRule(BaseSpinner.RULE).start();
		}
	}
}
