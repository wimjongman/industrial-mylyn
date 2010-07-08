/*******************************************************************************
 * Copyright (c) 2010 Industrial TSI and Maarten Meijer.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Wim Jongman - initial API and implementation
 *******************************************************************************/
package com.industrialtsi.mylyn.ui.query;

import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.wizards.AbstractRepositoryQueryPage;

public class RepositoryQueryPageFactory {

	public String getPageDescription() {
		return "Default Form Based Page";
	}

	public AbstractRepositoryQueryPage[] getPages(TaskRepository repository,
			IRepositoryQuery queryToEdit) {
		return new AbstractRepositoryQueryPage[] { new IndustrialFormQueryPage(
				repository, queryToEdit) };
	}
}
