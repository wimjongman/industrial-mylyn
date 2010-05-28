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

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.core.ITaskComment;
import org.eclipse.mylyn.tasks.core.ITaskMapping;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.AbstractRepositoryConnectorUi;
import org.eclipse.mylyn.tasks.ui.wizards.ITaskRepositoryPage;
import org.eclipse.mylyn.tasks.ui.wizards.RepositoryQueryWizard;

import com.industrialtsi.mylyn.core.IndustrialCore;
import com.industrialtsi.mylyn.ui.query.IndustrialFormQueryPage;
import com.industrialtsi.mylyn.ui.query.IndustrialQueryTypeWizardPage;

/**
 * @author maarten
 * 
 */
public class IndustrialConnectorUI extends AbstractRepositoryConnectorUi {

	public IndustrialConnectorUI() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getConnectorKind() {
		// TODO Auto-generated method stub
		return IndustrialCore.CONNECTOR_KIND;
	}

	@Override
	public IWizard getNewTaskWizard(TaskRepository taskRepository,
			ITaskMapping selection) {
		return new IndustrialNewTaskWizard(taskRepository, selection);
	}

	@Override
	public IWizard getQueryWizard(TaskRepository repository,
			IRepositoryQuery queryToEdit) {
		RepositoryQueryWizard wizard = new RepositoryQueryWizard(repository);
		if (null == queryToEdit) {
			IndustrialQueryTypeWizardPage typePage = new IndustrialQueryTypeWizardPage(
					repository, null);
			wizard.addPage(typePage);
			typePage.setWizard(wizard);
		} else {
			IndustrialFormQueryPage formPage = new IndustrialFormQueryPage(
					repository, queryToEdit);
			wizard.addPage(formPage);
			formPage.setWizard(wizard);
		}
		return wizard;
	}

	@Override
	public ITaskRepositoryPage getSettingsPage(TaskRepository taskRepository) {
		return new IndustrialRepositorySettingsPage(taskRepository);
	}

	@Override
	public boolean hasSearchPage() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getReplyText(TaskRepository taskRepository, ITask task,
			ITaskComment taskComment, boolean includeTask) {
		StringBuilder commentPrefix = new StringBuilder();
		commentPrefix.append("("); //$NON-NLS-1$
		commentPrefix.append("In reply to ");
		commentPrefix.append(taskComment.getAuthor());
		commentPrefix.append(" in comment #");
		commentPrefix.append(taskComment.getNumber());
		commentPrefix.append("):"); //$NON-NLS-1$
		return commentPrefix.toString();
	}
}
