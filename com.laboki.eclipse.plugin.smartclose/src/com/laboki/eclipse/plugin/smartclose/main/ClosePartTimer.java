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

	private final IEditorPart part;
	private long delay = Store.getDelayTime();
	private static final TaskMutexRule RULE = new TaskMutexRule();
	private final Task closer = new Closer();

	public ClosePartTimer(final IEditorPart part) {
		this.part = part;
	}

	@Override
	public Instance
	start() {
		new Task() {

			@Override
			public void
			execute() {
				ClosePartTimer.this.closer.setDelay(ClosePartTimer.this.delay).start();
			}
		}.start();
		return super.start();
	}

	@Subscribe
	@AllowConcurrentEvents
	public void
	eventHandler(final PreferencesChangedEvent event) {
		new AsyncTask() {

			@Override
			public void
			execute() {
				ClosePartTimer.this.delay = Store.getDelayTime();
				ClosePartTimer.this.closer.stop();
				ClosePartTimer.this.closer.setDelay(ClosePartTimer.this.delay).start();
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
		this.closer.stop();
		return super.stop();
	}

	private final class Closer extends AsyncTask {

		@Override
		public void
		execute() {
			if (this.editorInUse()) this.reschedule(ClosePartTimer.this.delay);
			else this.closePart(ClosePartTimer.this.part);
		}

		private boolean
		editorInUse() {
			return this.editorIsDirty() || this.editorIsPinned();
		}

		private boolean
		editorIsDirty() {
			return ClosePartTimer.this.part.isDirty();
		}

		private boolean
		editorIsPinned() {
			final Optional<IWorkbenchPage> page = this.getPage();
			if (!page.isPresent()) return false;
			return page.get().isEditorPinned(ClosePartTimer.this.part);
		}

		private void
		closePart(final IEditorPart part) {
			final Optional<IWorkbenchPage> page = this.getPage();
			if (!page.isPresent()) return;
			page.get().closeEditor(part, false);
		}

		private Optional<IWorkbenchPage>
		getPage() {
			return EditorContext.getActivePage(EditorContext.getActiveWorkbenchWindow());
		}
	}
}
