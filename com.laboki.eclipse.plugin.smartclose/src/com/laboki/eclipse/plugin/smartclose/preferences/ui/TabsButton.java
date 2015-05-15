package com.laboki.eclipse.plugin.smartclose.preferences.ui;

import org.eclipse.swt.widgets.Composite;

import com.laboki.eclipse.plugin.smartclose.events.ShowTabsDialogEvent;
import com.laboki.eclipse.plugin.smartclose.instance.Instance;
import com.laboki.eclipse.plugin.smartclose.main.EventBus;
import com.laboki.eclipse.plugin.smartclose.preferences.Store;

public final class TabsButton extends BaseButton {

	public TabsButton(final Composite parent) {
		super(parent);
	}

	@Override
	protected void
	showDialog() {
		EventBus.post(new ShowTabsDialogEvent());
	}

	@Override
	protected void
	updateText() {
		final int numberOfTabs = Store.getNumberOfTabs();
		this.button.setText(numberOfTabs + TabsButton.getSuffix(numberOfTabs));
		this.button.pack();
		this.button.update();
	}

	private static String
	getSuffix(final int numberOfTabs) {
		if (numberOfTabs > 1) return " tabs";
		return " tab";
	}

	@Override
	public Instance
	start() {
		this.updateText();
		return super.start();
	}
}
