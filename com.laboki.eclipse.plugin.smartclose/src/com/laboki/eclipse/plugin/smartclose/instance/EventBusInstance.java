package com.laboki.eclipse.plugin.smartclose.instance;

import com.laboki.eclipse.plugin.smartclose.main.EventBus;

public abstract class EventBusInstance implements Instance {

	private boolean isRegistered = false;

	protected EventBusInstance() {}

	@Override
	public Instance
	start() {
		if (this.isRegistered) return this;
		EventBus.register(this);
		this.isRegistered = true;
		return this;
	}

	@Override
	public Instance
	stop() {
		if (!this.isRegistered) return this;
		EventBus.unregister(this);
		this.isRegistered = false;
		return this;
	}
}
