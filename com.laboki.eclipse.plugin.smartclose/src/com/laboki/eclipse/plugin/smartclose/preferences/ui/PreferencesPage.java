package com.laboki.eclipse.plugin.smartclose.preferences.ui;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public final class PreferencesPage extends PreferencePage
	implements
		IWorkbenchPreferencePage {

	public PreferencesPage() {}

	public PreferencesPage(final String title) {
		super(title);
	}

	public PreferencesPage(final String title, final ImageDescriptor image) {
		super(title, image);
	}

	@Override
	public void
	init(final IWorkbench workbench) {}

	@Override
	protected Control
	createContents(final Composite parent) {
		final Composite composite = Util.createComposite(parent, 2);
		return composite;
	}
}
