package com.laboki.eclipse.plugin.smartclose.task;

import com.laboki.eclipse.plugin.smartclose.main.EditorContext;

public abstract class AsyncTask extends Task {

	@Override
	protected TaskJob
	newTaskJob() {
		return new TaskJob() {

			@Override
			protected void
			runTask() {
				EditorContext.asyncExec(() -> AsyncTask.this.execute());
			}
		};
	}
}
