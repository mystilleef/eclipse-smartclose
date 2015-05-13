package com.laboki.eclipse.plugin.smartclose.main;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.laboki.eclipse.plugin.smartclose.instance.Instance;

public final class Services implements Instance {

	private final List<Instance> instances = Lists.newArrayList();

	@Override
	public Instance
	start() {
		this.startServices();
		return this;
	}

	private void
	startServices() {
		this.startService(new PartCloser());
		this.startService(new PartTracker());
		this.startService(PartMonitor.INSTANCE);
		this.startService(new PreferencesListener());
	}

	private void
	startService(final Instance instance) {
		instance.start();
		this.instances.add(instance);
	}

	@Override
	public Instance
	stop() {
		Services.cancelTasks();
		this.stopServices();
		this.instances.clear();
		return this;
	}

	private static void
	cancelTasks() {
		EditorContext.cancelEventTasks();
		EditorContext.cancelPluginTasks();
	}

	private void
	stopServices() {
		for (final Instance instance : ImmutableList.copyOf(this.instances))
			this.stopService(instance);
	}

	private void
	stopService(final Instance instance) {
		instance.stop();
		this.instances.remove(instance);
	}
}
