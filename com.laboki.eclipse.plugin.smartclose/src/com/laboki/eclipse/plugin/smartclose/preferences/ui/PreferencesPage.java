package com.laboki.eclipse.plugin.smartclose.preferences.ui;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.laboki.eclipse.plugin.smartclose.events.PreferencePageDisposedEvent;
import com.laboki.eclipse.plugin.smartclose.main.EventBus;

public final class PreferencesPage extends PreferencePage
	implements
		IWorkbenchPreferencePage {

	private DialogListener dialogListener;

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
		this.startDialogListener(parent);
		final Composite composite = Util.createComposite(parent, 2);
		PreferencesPage.createTabsLabel(composite);
		PreferencesPage.createTabsButton(composite);
		PreferencesPage.createDelayLabel(composite);
		PreferencesPage.createDelayButton(composite);
		return composite;
	}

	private void
	startDialogListener(final Composite parent) {
		this.dialogListener = new DialogListener(parent);
		this.dialogListener.start();
	}

	private static void
	createTabsLabel(final Composite parent) {
		final String string = "Number of tabs to leave open:";
		Util.createLabel(parent, 1, string);
	}

	private static void
	createTabsButton(final Composite parent) {
		new TabsButton(parent).start();
	}

	private static void
	createDelayLabel(final Composite parent) {
		final String string = "Delay before closing unused tabs:";
		Util.createLabel(parent, 1, string);
	}

	private static void
	createDelayButton(final Composite parent) {
		new DelayButton(parent).start();
	}

	@Override
	public void
	dispose() {
		this.dialogListener.stop();
		EventBus.post(new PreferencePageDisposedEvent());
		super.dispose();
	}
}
