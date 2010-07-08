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
package com.industrialtsi.mylyn.ui.query;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.wizards.AbstractRepositoryQueryPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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

	private static final String TITLE = "Choose query type";

	private static final String DESCRIPTION = "Select from the available query types.";

	private Composite composite;

	private final TaskRepository repository;

	private HashMap<String, AbstractRepositoryQueryPage[]> pageMap = new HashMap<String, AbstractRepositoryQueryPage[]>();

	protected String selectedPage;

	/**
	 * @param queryToEdit
	 * @param pageName
	 */
	public IndustrialQueryTypeWizardPage(TaskRepository repository,
			IRepositoryQuery queryToEdit) {
		super(TITLE);
		this.repository = repository;
		setTitle(TITLE);
		setDescription(DESCRIPTION);
		setImageDescriptor(IndustrialUiImages.BANNER_REPOSITORY);
		loadPages(repository, queryToEdit);

	}

	private void loadPages(TaskRepository repository2,
			IRepositoryQuery queryToEdit) {

		IConfigurationElement[] configurationElements = Platform
				.getExtensionRegistry().getConfigurationElementsFor(
						"com.industrialtsi.mylyn.ui", "repositoryPageFactory");
		for (IConfigurationElement element : configurationElements) {
			if (element.getAttribute("persistor").equals(
					repository2.getProperty("repository_config_name")))
				try {
					RepositoryQueryPageFactory factory = (RepositoryQueryPageFactory) element
							.createExecutableExtension("factory");
					pageMap.put(factory.getPageDescription(),
							factory.getPages(repository2, queryToEdit));
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}

		if (pageMap.isEmpty()) {
			RepositoryQueryPageFactory factory = new RepositoryQueryPageFactory();
			pageMap.put(factory.getPageDescription(),
					factory.getPages(repository2, queryToEdit));
		}
	}

	public void createControl(Composite parent) {

		composite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.grabExcessVerticalSpace = false;
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(1, false));

		boolean first = true;
		String[] keys = pageMap.keySet().toArray(new String[0]);
		for (String key : keys) {
			Button buttonForm = new Button(composite, SWT.RADIO);
			buttonForm.setText(key);
			if (first) {
				buttonForm.setSelection(true);
				first = false;
				selectedPage = key;
			}

			buttonForm.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					selectedPage = ((Button) e.widget).getText();
				}
			});
		}

		setPageComplete(true);
		setControl(composite);

		updateAttributesFromRepository(false);
	}

	@Override
	public IWizardPage getNextPage() {

		assert null != getWizard();
		assert getWizard() instanceof Wizard;

		AbstractRepositoryQueryPage[] pages = pageMap.get(selectedPage);

		((Wizard) getWizard()).addPage(pages[0]);
		pages[0].setWizard(getWizard());
		return pages[0];

	}

	private void updateAttributesFromRepository(final boolean force) {
		final IndustrialRepositoryConnector connector = IndustrialCore
				.getDefault().getConnector();

		String attributes = repository
				.getProperty(IndustrialCore.REPOSITORY_ATTRIBUTES);

		if (null == attributes || force) {
			try {
				IRunnableWithProgress runnable = new IRunnableWithProgress() {
					public void run(IProgressMonitor monitor)
							throws InvocationTargetException,
							InterruptedException {
						try {
							monitor.beginTask("Updating search options...",
									IProgressMonitor.UNKNOWN);
							connector.updateRepositoryConfiguration(repository,
									monitor);
						} catch (Exception e) {
							throw new InvocationTargetException(e);
						}
					}
				};

				if (getContainer() != null) {
					getContainer().run(true, true, runnable);
				} else {
					IProgressService service = PlatformUI.getWorkbench()
							.getProgressService();
					service.busyCursorWhile(runnable);
				}
			} catch (InvocationTargetException e) {
				IStatus status = UILogger.createStatus(IStatus.ERROR,
						"Failed to update legal values for parameters", e);
				UILogger.log(status);
				return;
			} catch (InterruptedException e) {
				return;
			}
			// attributes =
			// repository.getProperty(IndustrialCore.REPOSITORY_ATTRIBUTES);
		}
	}

}
