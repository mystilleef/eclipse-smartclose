package com.laboki.eclipse.plugin.smartclose.events;

import org.eclipse.ui.IEditorPart;

public final class StartCloseTimerEvent {

	private final IEditorPart part;

	public StartCloseTimerEvent(final IEditorPart part) {
		this.part = part;
	}

	public IEditorPart
	getPart() {
		return this.part;
	}
}
