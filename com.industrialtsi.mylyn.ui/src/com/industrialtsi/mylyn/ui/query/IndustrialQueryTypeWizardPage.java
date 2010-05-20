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
package com.industrialtsi.mylyn.ui.query;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;

import com.industrialtsi.mylyn.core.IndustrialCore;
import com.industrialtsi.mylyn.core.IndustrialRepositoryConnector;
import com.industrialtsi.mylyn.ui.IndustrialUiImages;
import com.industrialtsi.mylyn.ui.internal.UILogger;

/**
 * @author maarten
 */
public class IndustrialQueryTypeWizardPage extends WizardPage {

	private static final String BUTTON_LABEL_WHERE_CLAUSE = "Create query with SQL WHERE clause";

	private static final String BUTTON_LABEL_SUPER = "&Create super duper custom query using Wim's wizard";

	private static final String BUTTON_LABEL_QUERY = "&Create query from existing URL";

	private static final String BUTTON_LABEL_FORM = "Cre&ate query using form";

	private static final String TITLE = "Choose query type";

	private static final String DESCRIPTION = "Select from the available query types.";

	private Button buttonCustom;

	private Button buttonForm;

	private Composite composite;

	private IndustrialFormQueryPage formPage = null;

	private final TaskRepository repository;

	/**
	 * @param queryToEdit
	 * @param pageName
	 */
	public IndustrialQueryTypeWizardPage(TaskRepository repository, IRepositoryQuery queryToEdit) {
		super(TITLE);
		this.repository = repository;
		setTitle(TITLE);
		setDescription(DESCRIPTION);
		setImageDescriptor(IndustrialUiImages.BANNER_REPOSITORY);
		formPage = new IndustrialFormQueryPage(repository, queryToEdit);
	}

	public void createControl(Composite parent) {
			
		composite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.grabExcessVerticalSpace = false;
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(1, false));

		buttonForm = new Button(composite, SWT.RADIO);
		buttonForm.setText(BUTTON_LABEL_FORM);
		buttonForm.setSelection(true);

		buttonCustom = new Button(composite, SWT.RADIO);
		buttonCustom.setText(BUTTON_LABEL_SUPER);

		setPageComplete(true);
		setControl(composite);

		final Button urlButton = new Button(composite, SWT.RADIO);
		urlButton.setText(BUTTON_LABEL_QUERY);

		final Button sqlButton = new Button(composite, SWT.RADIO);
		sqlButton.setText(BUTTON_LABEL_WHERE_CLAUSE);

		updateAttributesFromRepository(false);
	}

	@Override
	public IWizardPage getNextPage() {
		if (buttonForm.getSelection()) {
			assert null != getWizard();
			assert getWizard() instanceof Wizard;

			if (getWizard() instanceof Wizard && null == getWizard().getPage(formPage.getName())) {
				((Wizard) getWizard()).addPage(formPage);
			}
			formPage.setWizard(this.getWizard());
			return formPage;
		}
		// // add other pages here
		// // customPage.setWizard(this.getWizard());
		// // return customPage;
		// return (IWizardPage) formPage;
		return null;
	}
	
	private void updateAttributesFromRepository(final boolean force) {
		final IndustrialRepositoryConnector connector = IndustrialCore.getDefault().getConnector();

		String attributes = repository.getProperty(IndustrialCore.REPOSITORY_ATTRIBUTES);

		if (null == attributes || force) {
			try {
				IRunnableWithProgress runnable = new IRunnableWithProgress() {
					public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
						try {
							monitor.beginTask("Updating search options...", IProgressMonitor.UNKNOWN);
							connector.updateRepositoryConfiguration(repository, monitor);
						} catch (Exception e) {
							throw new InvocationTargetException(e);
						}
					}
				};

				if (getContainer() != null) {
					getContainer().run(true, true, runnable);
				} else {
					IProgressService service = PlatformUI.getWorkbench().getProgressService();
					service.busyCursorWhile(runnable);
				}
			} catch (InvocationTargetException e) {
				IStatus status = UILogger.createStatus(IStatus.ERROR, "Failed to update legal values for parameters", e);
				UILogger.log(status );
				return;
			} catch (InterruptedException e) {
				return;
			}
//			attributes = repository.getProperty(IndustrialCore.REPOSITORY_ATTRIBUTES);
		}
	}
	
	
}
