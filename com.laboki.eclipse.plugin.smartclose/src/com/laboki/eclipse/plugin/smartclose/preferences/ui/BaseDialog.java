package com.laboki.eclipse.plugin.smartclose.preferences.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.laboki.eclipse.plugin.smartclose.events.FocusDialogSpinnerEvent;
import com.laboki.eclipse.plugin.smartclose.events.PreferencePageDisposedEvent;
import com.laboki.eclipse.plugin.smartclose.instance.EventBusInstance;
import com.laboki.eclipse.plugin.smartclose.instance.Instance;
import com.laboki.eclipse.plugin.smartclose.main.EventBus;
import com.laboki.eclipse.plugin.smartclose.task.AsyncTask;

public abstract class BaseDialog extends EventBusInstance {

	private static final int SPINNER_GRID_LAYOUT_COLUMNS = 3;
	private static final int MARGIN_SIZE = 10;
	final Shell dialog;

	public BaseDialog(final Composite parent) {
		this.dialog =
			new Shell(parent.getShell(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
	}

	@Override
	public Instance
	start() {
		this.setupDialog();
		this.arrangeWidgets();
		this.show();
		return super.start();
	}

	private void
	setupDialog() {
		this.dialog.setLayout(BaseDialog.createLayout());
		this.dialog.setText(this.getDialogTitle());
		this.dialog.addShellListener(new DialogShellListener());
	}

	protected abstract String
	getDialogTitle();

	private static GridLayout
	createLayout() {
		final GridLayout layout = new GridLayout(1, false);
		layout.marginBottom = BaseDialog.MARGIN_SIZE;
		layout.marginTop = BaseDialog.MARGIN_SIZE;
		layout.marginLeft = BaseDialog.MARGIN_SIZE;
		layout.marginRight = BaseDialog.MARGIN_SIZE;
		return layout;
	}

	private void
	arrangeWidgets() {
		this.addLabel();
		this.addSpinnerSection();
		this.dialog.pack();
	}

	private void
	addLabel() {
		final String text = BaseDialog.getDialogLabel();
		final StyledText fieldText =
			new StyledText(this.dialog, SWT.LEFT | SWT.WRAP | SWT.READ_ONLY);
		this.setLabelProperties(text, fieldText);
		BaseDialog.setLabelStyle(text, fieldText);
	}

	protected static String
	getDialogLabel() {
		return "Press ESC or ENTER to close window.";
	}

	private void
	setLabelProperties(final String text, final StyledText fieldText) {
		fieldText.setText(text);
		fieldText.setEditable(false);
		fieldText.setCaret(null);
		fieldText.setBackground(this.dialog.getBackground());
		fieldText.setLayoutData(new GridData());
	}

	private static void
	setLabelStyle(final String text, final StyledText fieldText) {
		final StyleRange styleRange = new StyleRange();
		styleRange.start = 0;
		styleRange.length = text.length();
		styleRange.fontStyle = SWT.BOLD;
		fieldText.setStyleRange(styleRange);
	}

	private void
	addSpinnerSection() {
		final Composite composite = this.createSpinnerComposite();
		BaseDialog.createLabel(composite, this.getPrefixSpinnerLabel());
		this.createSpinner(composite);
		BaseDialog.createLabel(composite, this.getSufixSpinnerLabel());
	}

	protected abstract void
	createSpinner(final Composite composite);

	protected abstract String
	getPrefixSpinnerLabel();

	protected abstract String
	getSufixSpinnerLabel();

	private static void
	createLabel(final Composite composite, final String name) {
		final Label label = new Label(composite, SWT.NONE);
		label.setText(name);
	}

	private Composite
	createSpinnerComposite() {
		final Composite composite = new Composite(this.dialog, SWT.NONE);
		composite.setLayout(new GridLayout(BaseDialog.SPINNER_GRID_LAYOUT_COLUMNS,
			false));
		composite.setLayoutData(new GridData());
		return composite;
	}

	public void
	show() {
		this.dialog.open();
	}

	@Override
	public Instance
	stop() {
		if ((this.dialog == null) || this.dialog.isDisposed()) return super.stop();
		this.dialog.dispose();
		return super.stop();
	}

	@Subscribe
	@AllowConcurrentEvents
	public void
	eventHandler(final PreferencePageDisposedEvent event) {
		new AsyncTask() {

			@Override
			public void
			execute() {
				BaseDialog.this.stop();
			}
		}.start();
	}

	private final class DialogShellListener implements ShellListener {

		public DialogShellListener() {}

		@Override
		public void
		shellActivated(final ShellEvent arg0) {
			EventBus.post(new FocusDialogSpinnerEvent());
		}

		@Override
		public void
		shellClosed(final ShellEvent event) {
			event.doit = false;
			BaseDialog.this.dialog.setVisible(false);
		}

		@Override
		public void
		shellDeactivated(final ShellEvent arg0) {}

		@Override
		public void
		shellDeiconified(final ShellEvent arg0) {}

		@Override
		public void
		shellIconified(final ShellEvent arg0) {}
	}
}
