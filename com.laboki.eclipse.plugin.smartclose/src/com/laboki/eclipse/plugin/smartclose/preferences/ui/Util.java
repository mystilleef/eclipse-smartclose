package com.laboki.eclipse.plugin.smartclose.preferences.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public enum Util {
	INSTANCE;

	/**
	 * Creates composite control and sets the default layout data.
	 *
	 * @param parent the parent of the new composite
	 * @param numColumns the number of columns for the new composite
	 * @return the newly-created composite
	 */
	public static Composite
	createComposite(final Composite parent, final int numColumns) {
		final Composite composite = new Composite(parent, SWT.NULL);
		// GridLayout
		final GridLayout layout = new GridLayout();
		layout.numColumns = numColumns;
		layout.horizontalSpacing = 10;
		layout.verticalSpacing = 10;
		composite.setLayout(layout);
		// GridData
		final GridData data = new GridData();
		data.verticalAlignment = SWT.FILL;
		data.horizontalAlignment = SWT.FILL;
		data.grabExcessHorizontalSpace = true;
		data.grabExcessVerticalSpace = true;
		composite.setLayoutData(data);
		return composite;
	}

	/**
	 * Utility method that creates a label instance and sets the default
	 * layout data.
	 *
	 * @param parent the parent for the new label
	 * @param text the text for the new label
	 * @return the new label
	 */
	public static Label
	createLabel(final Composite parent, final int span, final String text) {
		final Label label = new Label(parent, SWT.RIGHT);
		label.setText(text);
		final GridData data = new GridData();
		data.horizontalSpan = span;
		data.verticalAlignment = SWT.CENTER;
		data.horizontalAlignment = SWT.FILL;
		data.grabExcessHorizontalSpace = false;
		data.grabExcessVerticalSpace = false;
		data.widthHint = parent.getMonitor().getClientArea().width / 8;
		label.setLayoutData(data);
		parent.layout();
		parent.pack();
		return label;
	}

	public static Button
	createButton(final Composite parent, final int span, final String text) {
		final Button button = new Button(parent, SWT.FLAT);
		button.setText(text);
		final GridData data = new GridData();
		data.horizontalSpan = span;
		data.verticalAlignment = SWT.CENTER;
		data.horizontalAlignment = SWT.BEGINNING;
		data.grabExcessHorizontalSpace = false;
		data.grabExcessVerticalSpace = false;
		button.setLayoutData(data);
		parent.layout();
		parent.pack();
		return button;
	}
}
