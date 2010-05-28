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
package com.industrialtsi.mylyn.core.internal;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import com.industrialtsi.mylyn.core.IndustrialCore;

/**
 * Various utils
 * 
 * @author Wim Jongman
 * @author Maarten Meijer
 */
public class RepositoryUtils {

	private static final String MAPS_LOCATION = "/maps"; //$NON-NLS-1$
	private static final String DOT = "."; //$NON-NLS-1$

	/**
	 * @return the list of selectable repository ids
	 */
	public static String[] getRepositoryIDs() {

		ArrayList<String> result = new ArrayList<String>();
		File maps = null;
		try {
			// This construct is needed to also get access to the stuff defined
			// in Fragments
			IPath path = new Path(MAPS_LOCATION);
			URL[] urls = FileLocator.findEntries(IndustrialCore.getDefault().getBundle(), path);

			// assert urls.length > 0; // not any more with reduced Ibatis dependency

			for (URL url : urls) {
				maps = new File(FileLocator.toFileURL(url).getFile());
				File[] files = maps.listFiles();
				for (int i = 0; i < files.length; i++) {
					if (files[i].isDirectory()
					// ignore Mac OSX stuff in hidden files
							&& !files[i].getName().startsWith(DOT)) {
						result.add(files[i].getName());
					}
				}

			}
		} catch (IOException e) {
		}

		return result.toArray(new String[result.size()]);
	}

}
