package com.laboki.eclipse.plugin.smartclose.preferences.ui;

import org.eclipse.swt.widgets.Composite;

public final class DelayDialog extends BaseDialog {

	public DelayDialog(final Composite parent) {
		super(parent);
	}

	@Override
	protected String
	getDialogTitle() {
		return "Delay Time";
	}

	@Override
	protected void
	createSpinner(final Composite composite) {
		new DelayDialogSpinner(composite).start();
	}

	@Override
	protected String
	getPrefixSpinnerLabel() {
		return "Delay before closing tabs: ";
	}

	@Override
	protected String
	getSufixSpinnerLabel() {
		return "minute(s)";
	}
}
