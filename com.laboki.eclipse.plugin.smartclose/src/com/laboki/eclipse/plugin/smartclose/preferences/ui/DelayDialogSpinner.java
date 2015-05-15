package com.laboki.eclipse.plugin.smartclose.preferences.ui;

import java.util.concurrent.TimeUnit;

import org.eclipse.swt.widgets.Composite;

import com.laboki.eclipse.plugin.smartclose.preferences.Store;

public final class DelayDialogSpinner extends BaseSpinner {

	public DelayDialogSpinner(final Composite parent) {
		super(parent);
	}

	@Override
	protected int
	getTextLimit() {
		return 2;
	}

	@Override
	protected int
	getSpinnerMinimum() {
		return 1;
	}

	@Override
	protected int
	getSpinnerMaximum() {
		return 60;
	}

	@Override
	protected int
	getSpinnerDigits() {
		return 0;
	}

	@Override
	protected int
	getSpinnerIncrements() {
		return 1;
	}

	@Override
	protected int
	getSpinnerPageIncrements() {
		return 5;
	}

	@Override
	protected long
	getStoreValue() {
		return TimeUnit.MILLISECONDS.toMinutes(Store.getDelayTime());
	}

	@Override
	protected void
	setStoreValue(final long value) {
		Store.setDelayTime(TimeUnit.MINUTES.toMillis(value));
	}
}
