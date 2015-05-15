package com.laboki.eclipse.plugin.smartclose.preferences.ui;

import org.eclipse.swt.widgets.Composite;

public final class TabsDialog extends BaseDialog {

	public TabsDialog(final Composite parent) {
		super(parent);
	}

	@Override
	protected String
	getDialogTitle() {
		return "Tabs";
	}

	@Override
	protected void
	createSpinner(final Composite composite) {
		new TabsDialogSpinner(composite).start();
	}

	@Override
	protected String
	getPrefixSpinnerLabel() {
		return "Number of tabs to leave open: ";
	}

	@Override
	protected String
	getSufixSpinnerLabel() {
		return " tab(s)";
	}
}
