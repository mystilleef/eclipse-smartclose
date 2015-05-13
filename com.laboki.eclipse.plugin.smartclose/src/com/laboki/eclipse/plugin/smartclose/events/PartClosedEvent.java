package com.laboki.eclipse.plugin.smartclose.events;

import org.eclipse.ui.IEditorPart;

public final class PartClosedEvent {

	private final IEditorPart part;

	public PartClosedEvent(final IEditorPart part) {
		this.part = part;
	}

	public IEditorPart
	getPart() {
		return this.part;
	}
}
