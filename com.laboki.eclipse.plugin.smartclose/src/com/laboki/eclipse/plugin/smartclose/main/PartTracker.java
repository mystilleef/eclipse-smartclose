package com.laboki.eclipse.plugin.smartclose.main;

import java.util.ArrayDeque;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.IEditorPart;

import com.google.common.base.Optional;
import com.google.common.collect.Queues;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.laboki.eclipse.plugin.smartclose.events.PartActivatedEvent;
import com.laboki.eclipse.plugin.smartclose.events.PartCloseTimerEvent;
import com.laboki.eclipse.plugin.smartclose.events.PartClosedEvent;
import com.laboki.eclipse.plugin.smartclose.events.PreferencesChangedEvent;
import com.laboki.eclipse.plugin.smartclose.instance.EventBusInstance;
import com.laboki.eclipse.plugin.smartclose.instance.Instance;
import com.laboki.eclipse.plugin.smartclose.preferences.Store;
import com.laboki.eclipse.plugin.smartclose.task.Task;
import com.laboki.eclipse.plugin.smartclose.task.TaskMutexRule;

public final class PartTracker extends EventBusInstance {

	protected int watermark = Store.getNumberOfTabs();
	private static final TaskMutexRule RULE = new TaskMutexRule();
	protected final ArrayDeque<IEditorPart> deque = Queues.newArrayDeque();

	@Override
	public Instance
	start() {
		final Optional<IEditorPart> part = EditorContext.getEditor();
		if (part.isPresent()) this.deque.addFirst(part.get());
		return super.start();
	}

	@Subscribe
	public void
	eventHandler(final PartActivatedEvent event) {
		new Task() {

			@Override
			public void
			execute() {
				PartTracker.this.addUpdate(event.getPart());
				this.trimDeque();
			}

			private void
			trimDeque() {
				if (PartTracker.this.deque.size() <= PartTracker.this.watermark) return;
				this.broadcastEvent(PartTracker.this.deque.removeLast());
				this.trimDeque();
			}

			private void
			broadcastEvent(final IEditorPart part) {
				EventBus.post(new PartCloseTimerEvent(part));
			}
		}.setPriority(Job.INTERACTIVE).setRule(PartTracker.RULE).start();
	}

	@Subscribe
	@AllowConcurrentEvents
	public void
	eventHandler(final PreferencesChangedEvent event) {
		new Task() {

			@Override
			public void
			execute() {
				PartTracker.this.watermark = Store.getNumberOfTabs();
			}
		}.setRule(PartTracker.RULE).start();
	}

	@Subscribe
	public void
	eventHandler(final PartClosedEvent event) {
		new Task() {

			@Override
			public void
			execute() {
				PartTracker.this.removeUpdate(event.getPart());
			}
		}.setPriority(Job.INTERACTIVE).setRule(PartTracker.RULE).start();
	}

	protected void
	addUpdate(final IEditorPart part) {
		this.removeUpdate(part);
		this.deque.addFirst(part);
	}

	protected void
	removeUpdate(final IEditorPart part) {
		if (this.dequeContains(part)) this.deque.remove(part);
	}

	private boolean
	dequeContains(final IEditorPart part) {
		return PartTracker.this.deque.contains(part);
	}

	@Override
	public Instance
	stop() {
		this.deque.clear();
		return super.stop();
	}
}
