/*******************************************************************************
 * Copyright (c) 2008 Industrial TSI and Maarten Meijer.
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
import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;

/**
 * <code>IndustrialUIImages</code> : Holder for all images used.
 * 
 * @author maarten
 * 
 */
public class IndustrialUiImages {
	private static final URL baseURL = IndustrialUI.getDefault().getBundle().getEntry("/icons/"); //$NON-NLS-1$

	private static final String WIZBAN = "wizban"; //$NON-NLS-1$

	public static final ImageDescriptor BANNER_REPOSITORY = create(WIZBAN, "banner-repository.gif"); //$NON-NLS-1$

	public static final ImageDescriptor BANNER_REPOSITORY_SETTINGS = create(WIZBAN, "banner-repository-settings.gif"); //$NON-NLS-1$

	public static final ImageDescriptor BANNER_REPOSITORY_CONTEXT = create(WIZBAN, "banner-repository-context.gif"); //$NON-NLS-1$

	public static ImageDescriptor create(String prefix, String name) {
		try {
			return ImageDescriptor.createFromURL(makeIconFileURL(prefix, name));
		} catch (MalformedURLException e) {
			return ImageDescriptor.getMissingImageDescriptor();
		}
	}

	private static URL makeIconFileURL(String prefix, String name) throws MalformedURLException {
		if (baseURL == null) {
			throw new MalformedURLException();
		}

		StringBuffer buffer = new StringBuffer(prefix);
		buffer.append('/');
		buffer.append(name);
		return new URL(baseURL, buffer.toString());
	}
}
