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
package com.industrialtsi.mylyn.ui.internal;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.industrialtsi.mylyn.core.IndustrialCore;
import com.industrialtsi.mylyn.ui.IndustrialUI;

public class UILogger {

	public static void logInfo(String message) {
		log(IStatus.INFO, IStatus.OK, message, null);
	}

	public static void logError(Throwable exception) {
		logError("Unexpected exception", exception); //$NON-NLS-1$
	}

	public static void logError(String message, Throwable exception) {
		log(IStatus.ERROR, IStatus.OK, message, exception);
	}

	public static void log(int severity, int code, String message, Throwable exception) {
		log(createStatus(severity, code, message, exception));
	}

	public static IStatus createStatus(int severity, int code, String message, Throwable exception) {
		return new Status(severity, IndustrialUI.PLUGIN_ID, code, message, exception);
	}

	public static IStatus createStatus(int severity, String message, Throwable exception) {
		return new Status(severity, IndustrialUI.PLUGIN_ID, message, exception);
	}

	public static IStatus createStatus(int severity, Throwable exception) {
		return new Status(severity, IndustrialUI.PLUGIN_ID, exception.getMessage(), exception);
	}

	public static void log(IStatus status) {
		IndustrialCore.getDefault().getLog().log(status);
	}

}
