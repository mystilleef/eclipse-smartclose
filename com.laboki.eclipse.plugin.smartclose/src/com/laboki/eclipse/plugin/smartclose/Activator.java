package com.laboki.eclipse.plugin.smartclose;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class Activator extends AbstractUIPlugin {

	private static Activator plugin;
	public static final String PLUGIN_ID =
		"com.laboki.eclipse.plugin.smartclose";

	public Activator() {}

	@Override
	public void
	start(final BundleContext context) throws Exception {
		super.start(context);
		Activator.plugin = this;
	}

	@Override
	public void
	stop(final BundleContext context) throws Exception {
		Activator.plugin = null;
		super.stop(context);
	}

	public static Activator
	getDefault() {
		return Activator.plugin;
	}
}
