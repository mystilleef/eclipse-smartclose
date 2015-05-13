package com.laboki.eclipse.plugin.smartclose.events;

import org.eclipse.ui.IEditorPart;

public final class PartCloseTimerEvent {

	private final IEditorPart part;

	public PartCloseTimerEvent(final IEditorPart part) {
		this.part = part;
	}

	public IEditorPart
	getPart() {
		return this.part;
	}
}
