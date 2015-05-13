package com.laboki.eclipse.plugin.smartclose.events;

import org.eclipse.ui.IEditorPart;

public final class PartDeactivatedEvent {

	private final IEditorPart part;

	public PartDeactivatedEvent(final IEditorPart part) {
		this.part = part;
	}

	public IEditorPart
	getPart() {
		return this.part;
	}
}
