package com.laboki.eclipse.plugin.smartclose.main;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IPartService;
import org.eclipse.ui.IWorkbenchPart;

import com.google.common.base.Optional;
import com.laboki.eclipse.plugin.smartclose.events.PartActivatedEvent;
import com.laboki.eclipse.plugin.smartclose.events.PartClosedEvent;
import com.laboki.eclipse.plugin.smartclose.events.PartDeactivatedEvent;
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
		partActivated(final IWorkbenchPart part) {
			if (PartListener.isNotEditorPart(part)) return;
			EventBus.post(new PartActivatedEvent((IEditorPart) part));
		}

		@Override
		public void
		partClosed(final IWorkbenchPart part) {
			if (PartListener.isNotEditorPart(part)) return;
			EventBus.post(new PartClosedEvent((IEditorPart) part));
		}

		@Override
		public void
		partBroughtToTop(final IWorkbenchPart part) {}

		@Override
		public void
		partDeactivated(final IWorkbenchPart part) {
			if (PartListener.isNotEditorPart(part)) return;
			EventBus.post(new PartDeactivatedEvent((IEditorPart) part));
		}

		@Override
		public void
		partOpened(final IWorkbenchPart part) {}

		private static boolean
		isNotEditorPart(final IWorkbenchPart part) {
			return !PartListener.isEditorPart(part);
		}

		private static boolean
		isEditorPart(final IWorkbenchPart part) {
			return part instanceof IEditorPart;
		}
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
