package com.laboki.eclipse.plugin.smartclose.main;

import java.util.List;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.IEditorPart;

import com.google.common.collect.Lists;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.laboki.eclipse.plugin.smartclose.events.PartActivatedEvent;
import com.laboki.eclipse.plugin.smartclose.events.PartCloseTimerEvent;
import com.laboki.eclipse.plugin.smartclose.events.PartClosedEvent;
import com.laboki.eclipse.plugin.smartclose.instance.EventBusInstance;
import com.laboki.eclipse.plugin.smartclose.task.Task;
import com.laboki.eclipse.plugin.smartclose.task.TaskMutexRule;

public final class PartCloser extends EventBusInstance {

	private static final TaskMutexRule RULE = new TaskMutexRule();
	protected final List<IEditorPart> parts = Lists.newArrayList();

	@Subscribe
	@AllowConcurrentEvents
	public void
	eventHandler(final PartCloseTimerEvent event) {
		new Task() {

			@Override
			public void
			execute() {
				final IEditorPart part = event.getPart();
				if (PartCloser.this.parts.contains(part)) return;
				PartCloser.this.parts.add(part);
				new ClosePartTimer(part).start();
			}
		}.setRule(PartCloser.RULE).start();
	}

	@Subscribe
	@AllowConcurrentEvents
	public void
	eventHandler(final PartClosedEvent event) {
		this.removeUpdate(event.getPart());
	}

	@Subscribe
	@AllowConcurrentEvents
	public void
	eventHandler(final PartActivatedEvent event) {
		this.removeUpdate(event.getPart());
	}

	private void
	removeUpdate(final IEditorPart part) {
		new Task() {

			@Override
			public void
			execute() {
				PartCloser.this.parts.remove(part);
			}
		}.setPriority(Job.INTERACTIVE).setRule(PartCloser.RULE).start();
	}
}
