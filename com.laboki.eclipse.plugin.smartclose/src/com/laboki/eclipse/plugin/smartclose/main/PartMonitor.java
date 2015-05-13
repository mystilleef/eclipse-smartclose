package com.laboki.eclipse.plugin.smartclose.main;

import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IPartService;
import org.eclipse.ui.IWorkbenchPart;

import com.google.common.base.Optional;
import com.laboki.eclipse.plugin.smartclose.instance.Instance;

public enum PartMonitor implements Instance {
	INSTANCE;

	private static final Optional<IPartService> SERVICE =
		EditorContext.getPartService();
	private static final PartListener LISTENER = new PartListener();

	private static final class PartListener implements IPartListener {

		public PartListener() {}

		@Override
		public void
		partActivated(final IWorkbenchPart part) {}

		@Override
		public void
		partClosed(final IWorkbenchPart part) {}

		@Override
		public void
		partBroughtToTop(final IWorkbenchPart part) {}

		@Override
		public void
		partDeactivated(final IWorkbenchPart part) {}

		@Override
		public void
		partOpened(final IWorkbenchPart part) {}
	}

	@Override
	public Instance
	start() {
		if (!PartMonitor.SERVICE.isPresent()) return this;
		PartMonitor.SERVICE.get().addPartListener(PartMonitor.LISTENER);
		return this;
	}

	@Override
	public Instance
	stop() {
		if (!PartMonitor.SERVICE.isPresent()) return this;
		PartMonitor.SERVICE.get().removePartListener(PartMonitor.LISTENER);
		return this;
	}
}
