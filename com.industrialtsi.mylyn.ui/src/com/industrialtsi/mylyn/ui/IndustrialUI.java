/*******************************************************************************
 * Copyright (c) 2008,2009,2010 Industrial TSI and Maarten Meijer.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Maarten Meijer - initial API and implementation
 *******************************************************************************/
package com.industrialtsi.mylyn.ui;

import java.net.MalformedURLException;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.mylyn.tasks.core.RepositoryStatus;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class IndustrialUI extends AbstractUIPlugin {

	
	public static final String PLUGIN_ID = "com.industrialtsi.mylyn.ui"; //$NON-NLS-1$

	// The shared instance
	private static IndustrialUI plugin;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static IndustrialUI getDefault() {
		return plugin;
	}

	public static IStatus toStatus(Throwable e, TaskRepository repository) {
		if (e instanceof ClassCastException) {
			return new RepositoryStatus(IStatus.ERROR, PLUGIN_ID, RepositoryStatus.ERROR_IO,
					"Unexpected server response: " + e.getMessage(), e); //$NON-NLS-1$
		} else if (e instanceof MalformedURLException) {
			return new RepositoryStatus(IStatus.ERROR, PLUGIN_ID, RepositoryStatus.ERROR_IO,
					"Repository URL is invalid", e); //$NON-NLS-1$
		} else {
			return new RepositoryStatus(IStatus.ERROR, PLUGIN_ID, RepositoryStatus.ERROR_INTERNAL, "Unexpected error", //$NON-NLS-1$
					e);
		}
	}


}
