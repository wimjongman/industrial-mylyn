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

package com.industrialtsi.mylyn.core.obsolete;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.mylyn.tasks.core.TaskRepository;

/**
 * <code>AbstractDatabaseSchemaInitializer</code> : Initialize a database using
 * standard JDBC code.
 * 
 * @author maarten
 * 
 * @deprecated Use other extension point instead
 */
@Deprecated
public abstract class AbstractDatabaseSchemaInitializer {

	/**
	 * Initialize the <code>repository</code> using whatever specific DB code is
	 * required. This code is always in a fragment so can depend on the driver
	 * code being present.
	 * 
	 * @param repository
	 *            to initialize.
	 * @return true when initialization is successful, false when not.
	 * @throws CoreException
	 */
	public abstract boolean initialize(TaskRepository repository) throws CoreException;

}
