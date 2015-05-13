package com.laboki.eclipse.plugin.smartclose.main;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;

import com.google.common.base.Optional;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.laboki.eclipse.plugin.smartclose.events.PartActivatedEvent;
import com.laboki.eclipse.plugin.smartclose.events.PartClosedEvent;
import com.laboki.eclipse.plugin.smartclose.events.PreferencesChangedEvent;
import com.laboki.eclipse.plugin.smartclose.instance.EventBusInstance;
import com.laboki.eclipse.plugin.smartclose.instance.Instance;
import com.laboki.eclipse.plugin.smartclose.preferences.Store;
import com.laboki.eclipse.plugin.smartclose.task.AsyncTask;
import com.laboki.eclipse.plugin.smartclose.task.Task;
import com.laboki.eclipse.plugin.smartclose.task.TaskMutexRule;

public final class ClosePartTimer extends EventBusInstance {

	private static final TaskMutexRule RULE = new TaskMutexRule();
	protected int delay = Store.getDelayTime();
	protected final IEditorPart part;
	protected Task closeTimer;

	public ClosePartTimer(final IEditorPart part) {
		this.part = part;
		this.closeTimer = this.startTimer();
	}

	private Task
	startTimer() {
		return new AsyncTask() {

			@Override
			public void
			execute() {
				if (this.editorIsDirty()) this.reschedule(ClosePartTimer.this.delay);
				else this.closePart(ClosePartTimer.this.part);
			}

			private boolean
			editorIsDirty() {
				return ClosePartTimer.this.part.isDirty();
			}

			private void
			closePart(final IEditorPart part) {
				final Optional<IWorkbenchPage> page = this.getPage();
				if (!page.isPresent()) return;
				if (page.get().isEditorPinned(part)) return;
				page.get().closeEditor(part, false);
			}

			private Optional<IWorkbenchPage>
			getPage() {
				return EditorContext.getActivePage(EditorContext.getActiveWorkbenchWindow());
			}
		};
	}

	@Override
	public Instance
	start() {
		this.closeTimer.setDelay(this.delay).start();
		return super.start();
	}

	@Subscribe
	@AllowConcurrentEvents
	public void
	eventHandler(final PreferencesChangedEvent event) {
		new Task() {

			@Override
			public void
			execute() {
				ClosePartTimer.this.delay = Store.getDelayTime();
				ClosePartTimer.this.closeTimer.reschedule(ClosePartTimer.this.delay);
			}
		}.setRule(ClosePartTimer.RULE).start();
	}

	@Subscribe
	@AllowConcurrentEvents
	public void
	eventHandler(final PartClosedEvent event) {
		this.stopTimerTask(event.getPart());
	}

	@Subscribe
	@AllowConcurrentEvents
	public void
	eventHandler(final PartActivatedEvent event) {
		this.stopTimerTask(event.getPart());
	}

	private void
	stopTimerTask(final IEditorPart part) {
		new Task() {

			@Override
			public void
			execute() {
				ClosePartTimer.this.stopTimer(part);
			}
		}.setPriority(Job.INTERACTIVE).start();
	}

	protected void
	stopTimer(final IEditorPart part) {
		if (part == this.part) this.stop();
	}

	@Override
	public Instance
	stop() {
		this.closeTimer.stop();
		return super.stop();
	}
}
