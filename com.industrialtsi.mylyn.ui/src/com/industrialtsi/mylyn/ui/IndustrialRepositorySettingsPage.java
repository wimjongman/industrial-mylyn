/*******************************************************************************
 * Copyright (c) 2008,2009,2010 Industrial TSI and Maarten Meijer.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Maarten Meijer - initial API and implementation
 *     Industrial TSI - improvements
 *******************************************************************************/
package com.industrialtsi.mylyn.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.wizards.AbstractRepositorySettingsPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.PlatformUI;

import com.industrialtsi.mylyn.core.IndustrialCore;
import com.industrialtsi.mylyn.core.persistence.PersistorsManager;
import com.industrialtsi.mylyn.core.persistence.TasksSqlMapConfig;
import com.industrialtsi.mylyn.ui.internal.UILogger;

/**
 * @author maarten
 * 
 */
/**
 * <code>IndustrialRepositorySettingsPage</code> : TODO describe.
 * 
 * @author maarten
 * 
 */
public class IndustrialRepositorySettingsPage extends
		AbstractRepositorySettingsPage {

	private static final String JDBC_URL_DEFAULT = "jdbc:<driver>://<host>[:<port>]/<database>";

	private static final String EXAMPLE = "Example: ";

	private static final String DESCRIPTION = EXAMPLE
 + JDBC_URL_DEFAULT; //$NON-NLS-1$

	private static final String TITLE = "Industrial Repository Settings"; //$NON-NLS-1$

	/**
	 * @author Maarten Meijer
	 */
	private class IbatisValidator extends Validator {
		private final boolean includedConfig;

		private final TaskRepository repository;

		/**
		 * @param repository
		 * @param useIncludedConfig
		 * @param notSet
		 * @param notSet2
		 */
		public IbatisValidator(boolean includedConfig, TaskRepository repository) {
			// TODO Auto-generated constructor stub
			this.includedConfig = includedConfig;
			this.repository = repository;
		}

		@Override
		public void run(IProgressMonitor monitor) throws CoreException {

			// memory always works
			if (repository.getUrl().contains("memory")) { //$NON-NLS-1$
				UILogger.logInfo("Memory always validates"); //$NON-NLS-1$
				return;
			}

			if (!includedConfig) {
				if (ibatisConfig && includedIbatisConfigs.getVisible()
						&& "".equals(includedIbatisConfigs.getText())) {
					Status error = new Status(IStatus.ERROR,
							IndustrialCore.PLUGIN_ID,
							"Select one from Included Repository Config."); //$NON-NLS-1$
					// throw new CoreException(error);
					setStatus(error);
				}
			}

			// TODO validate real connection

			try {
				boolean inited = IndustrialCore.getDefault()
						.validateRepository(repository);
				if (!inited) {
					IStatus status = UILogger.createStatus(IStatus.ERROR,
							"The repository '" + repository.getUrl() //$NON-NLS-1$
									+ "' cannot be validated.", //$NON-NLS-1$
							null);
					setStatus(status);
					UILogger.log(status);
				}
				if (!inited
						&& IndustrialCore.getDefault()
								.canInitialize(repository)) {
					PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
						public void run() {
							if (MessageDialog
									.openQuestion(
											getShell(),
											"The repository is not initialized", //$NON-NLS-1$
											"Do you want to initialize the repository '" + repository.getUrl() + "'?")) { //$NON-NLS-1$ //$NON-NLS-2$
								try {
									applyTo(repository);
									IndustrialCore.getDefault()
											.forgetTaskSqlMapConfig(repository);
									IndustrialCore.getDefault()
											.initializeRepository(repository);
									// need to reinitialize...
								} catch (CoreException e) {
									UILogger.log(e.getStatus());
									setStatus(e.getStatus());
								}
							}
						}
					});
				}
				boolean authenticated = IndustrialCore.getDefault()
						.authenticateRepository(repository);
				if (!authenticated) {
					IStatus status = UILogger
							.createStatus(
									IStatus.ERROR,
									"Cannot autheticate user with repository '" + repository.getUrl() //$NON-NLS-1$
											+ "'", //$NON-NLS-1$
									null);
					setStatus(status);
					UILogger.log(status);
				}

				IndustrialCore.getDefault().getConnector()
						.updateRepositoryConfiguration(repository, monitor);
			} catch (final CoreException e) {
				PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
					public void run() {
						ErrorDialog.openError(getShell(), "Validation Error", e //$NON-NLS-1$
								.getMessage(), e.getStatus());
					}
				});
				UILogger.log(e.getStatus());
				setStatus(e.getStatus());
			}

		}

	}

	private Combo includedIbatisConfigs;

	private Combo configuredRepositoriesCombo;

	private Label ibatisConfigLabel;

	private final TaskRepository taskRepository;

	private boolean ibatisConfig;

	private Object previous;

	private Label repositoriesLabel;

	public IndustrialRepositorySettingsPage(TaskRepository taskRepository) {
		super(TITLE, DESCRIPTION, taskRepository);
		this.taskRepository = taskRepository;
		setNeedsAdvanced(true);
		setNeedsAnonymousLogin(true);
		setNeedsEncoding(false);
		setNeedsTimeZone(false);
		setNeedsHttpAuth(false);
		setNeedsProxy(false);
		setNeedsValidation(true);
	}

	@Override
	protected void createAdditionalControls(Composite parent) {
		GridData comboData = new GridData();

		repositoriesLabel = new Label(parent, SWT.None);
		repositoriesLabel.setText("Choose a Repository"); //$NON-NLS-1$

		comboData.horizontalAlignment = GridData.FILL;
		comboData.grabExcessHorizontalSpace = true;

		configuredRepositoriesCombo = new Combo(parent, SWT.NONE);
		configuredRepositoriesCombo.setLayoutData(comboData);

		String[] repositoryNames = PersistorsManager.getManager()
				.getRepositoryNamesAsArray();
		configuredRepositoriesCombo.setItems(repositoryNames);
		configuredRepositoriesCombo.select(0);
		if (null != repository && repositoryNames.length > 0) {
			String repositoryName = repository
					.getProperty(IndustrialCore.REPOSITORY_CONFIG_NAME);
			if (null != repositoryName) {
				configuredRepositoriesCombo.select(configuredRepositoriesCombo
						.indexOf(repositoryName));
			}
		}
		configuredRepositoriesCombo.pack();
		configuredRepositoriesCombo
				.addSelectionListener(new SelectionListener() {

					public void widgetDefaultSelected(SelectionEvent e) {
						widgetSelected(e);
					}

					public void widgetSelected(SelectionEvent e) {
						showHideIbatisMaps();
						if (null == repository
								|| "".equals(repository.getUrl())) { //$NON-NLS-1$
							setUrlTemplate();
						}
						getWizard().getContainer().updateButtons();
					}

				});
		// Add the id to get the correct ibatis mappings.

		ibatisConfigLabel = new Label(parent, SWT.None);
		ibatisConfigLabel.setText("Choose an SQLMap config"); //$NON-NLS-1$
		comboData = new GridData();
		comboData.horizontalAlignment = GridData.FILL;
		comboData.grabExcessHorizontalSpace = true;

		includedIbatisConfigs = new Combo(parent, SWT.NONE);
		includedIbatisConfigs.setLayoutData(comboData);
		showHideIbatisMaps();

	}

	@Override
	public void dispose() {
		safeDispose(includedIbatisConfigs);
		safeDispose(configuredRepositoriesCombo);
		safeDispose(ibatisConfigLabel);
		safeDispose(repositoriesLabel);
		super.dispose();
	}

	private void safeDispose(Widget w) {
		if (null != w && !w.isDisposed()) {
			w.dispose();
		}
	}

	@Override
	public String getConnectorKind() {
		return IndustrialCore.CONNECTOR_KIND;
	}

	@Override
	protected Validator getValidator(TaskRepository repository) {
		return new IbatisValidator(useIncludedConfig(), repository);
	}

	@Override
	protected boolean isValidUrl(String name) {
		// the form foreseen is a JDBC type URL consisting of a driver, a host
		// and a port number. jdbc:<driver>://<host>:<port>/<database>
		// FIXME Move this regex to some config file?

		if (name.contains("jdbc:memory")) //$NON-NLS-1$
			return true;

		if ("".equals(name)) { //$NON-NLS-1$
			setUrlTemplate();
			return true;
		}

		if (name.equals(previous)) {
			return true;
		} else {
			previous = name;
		}

		String urlRE = getMatchRegularExpression();

		Pattern p = Pattern.compile(urlRE);
		Matcher m = p.matcher(name);
		boolean matches = m.matches();
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Matching '"); //$NON-NLS-1$
		stringBuilder.append(name);
		stringBuilder.append("' against '"); //$NON-NLS-1$
		stringBuilder.append(urlRE);
		stringBuilder.append("' : "); //$NON-NLS-1$
		stringBuilder.append((matches ? "success" : "failed")); //$NON-NLS-1$ //$NON-NLS-2$
		UILogger.logInfo(stringBuilder.toString());
		return matches;
	}

	private String getMatchRegularExpression() {
		String urlRE = null;
		if (null != taskRepository) {
			String repositoryName = taskRepository
					.getProperty(IndustrialCore.REPOSITORY_CONFIG_NAME);
			urlRE = PersistorsManager.getManager().getJdbcUrlRegularExpression(
					repositoryName);
		}
		if (null == urlRE) {
			String config = configuredRepositoriesCombo.getText();
			urlRE = PersistorsManager.getManager().getJdbcUrlRegularExpression(
					config);
		}

		if (null == urlRE) {
			urlRE = "^jdbc:[a-zA-Z0-9]+:[/:a-zA-Z0-9\\.]*(|:\\d+)*[/a-zA-Z0-9].*$"; //$NON-NLS-1$
		}
		return urlRE;
	}

	/**
	 * FIXME intended to set an example URL from extension point
	 */
	private void setUrlTemplate() {
		if (null == configuredRepositoriesCombo) {
			setDescription(DESCRIPTION);
			setUrl(JDBC_URL_DEFAULT);
			return;
		}
		String config = configuredRepositoriesCombo.getText();
		if (null == config) {
			setDescription(DESCRIPTION);
			setUrl(JDBC_URL_DEFAULT);
			return;
		}
		String template = PersistorsManager.getManager().getJdbcUrlTemplate(
				config);
		setUrl(template);
		setDescription(EXAMPLE + template);
	}

	/**
	 * @return the selected include preconfig path.
	 */
	private String includedConfig() {
		return includedIbatisConfigs.getItem(includedIbatisConfigs
				.getSelectionIndex());
	}

	/**
	 * @return is a preconfigured items selected?
	 */
	private boolean useIncludedConfig() {
		return PersistorsManager.getManager().isIbatisRepository(
				configuredRepositoriesCombo.getSelectionIndex());
	}

	/**
	 * when it is an ibatis connector, display the maps combo
	 */
	private void showHideIbatisMaps() {
		boolean ibatis = PersistorsManager.getManager().isIbatisRepository(
				configuredRepositoriesCombo.getSelectionIndex());
		if (ibatis) {
			String selected = configuredRepositoriesCombo
					.getItem(configuredRepositoriesCombo.getSelectionIndex());
			String[] items = PersistorsManager.getManager()
					.getIbatisMapLocations(selected);
			includedIbatisConfigs.setItems(items);
			if (null != repository) {
				String include = repository
						.getProperty(TasksSqlMapConfig.SQLMAP_INCLUDED);
				if (null != include) {
					includedIbatisConfigs.select(includedIbatisConfigs.indexOf(
							include, 0));
				}
			} else {
				includedIbatisConfigs.select(0);
			}
		}
		includedIbatisConfigs.setVisible(ibatis);
		ibatisConfigLabel.setVisible(ibatis);
		ibatisConfig = ibatis;
	}

	@Override
	public void applyTo(TaskRepository repository) {
		super.applyTo(repository);
		String repositoryName = PersistorsManager.getManager()
				.getRepositoryNamesAsArray()[configuredRepositoriesCombo
				.getSelectionIndex()];
		repository.setProperty(IndustrialCore.REPOSITORY_CONFIG_NAME,
				repositoryName);
		PersistorsManager.getManager().initialize(repository);

		if (useIncludedConfig()) {
			repository.setProperty(TasksSqlMapConfig.SQLMAP_INCLUDED,
					includedConfig());

		} else {
			repository.removeProperty(TasksSqlMapConfig.SQLMAP_INCLUDED);
		}
	}

}
