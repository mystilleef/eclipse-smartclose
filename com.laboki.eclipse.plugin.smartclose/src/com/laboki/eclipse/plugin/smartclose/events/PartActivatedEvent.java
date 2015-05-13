package com.laboki.eclipse.plugin.smartclose.events;

import org.eclipse.ui.IEditorPart;

public final class PartActivatedEvent {

	private final IEditorPart part;

	public PartActivatedEvent(final IEditorPart part) {
		this.part = part;
	}

	public IEditorPart
	getPart() {
		return this.part;
	}
}
