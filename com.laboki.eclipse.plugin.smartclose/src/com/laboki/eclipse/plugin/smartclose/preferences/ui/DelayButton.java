package com.laboki.eclipse.plugin.smartclose.preferences.ui;

import java.util.concurrent.TimeUnit;

import org.eclipse.swt.widgets.Composite;

import com.laboki.eclipse.plugin.smartclose.events.ShowDelayDialogEvent;
import com.laboki.eclipse.plugin.smartclose.instance.Instance;
import com.laboki.eclipse.plugin.smartclose.main.EventBus;
import com.laboki.eclipse.plugin.smartclose.preferences.Store;

public final class DelayButton extends BaseButton {

	public DelayButton(final Composite parent) {
		super(parent);
	}

	@Override
	protected void
	showDialog() {
		EventBus.post(new ShowDelayDialogEvent());
	}

	@Override
	protected void
	updateText() {
		final long delayTime =
			DelayButton.milliSecondsToMinutes(Store.getDelayTime());
		this.button.setText(delayTime + DelayButton.getSuffix(delayTime));
		this.button.pack();
		this.button.update();
	}

	private static long
	milliSecondsToMinutes(final long delayTime) {
		return TimeUnit.MILLISECONDS.toMinutes(delayTime);
	}

	private static String
	getSuffix(final long delayTime) {
		if (delayTime <= 1) return " minute";
		return " minutes";
	}

	@Override
	public Instance
	start() {
		this.updateText();
		return super.start();
	}
}
