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
package com.industrialtsi.mylyn.core;

import java.net.MalformedURLException;
import java.sql.SQLException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.mylyn.tasks.core.RepositoryStatus;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.osgi.framework.BundleContext;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.industrialtsi.mylyn.core.internal.CoreLogger;
import com.industrialtsi.mylyn.core.persistence.PersistorsManager;
import com.industrialtsi.mylyn.core.persistence.TasksSqlMapConfig;

/**
 * <code>IndustrialCore</code> : TODO describe.
 * 
 * @author maarten
 * 
 */
public class IndustrialCore extends Plugin {

	// the plugin id
	public static final String PLUGIN_ID = "com.industrialtsi.mylyn.core"; //$NON-NLS-1$

	// The plug-in kind
	public static final String CONNECTOR_KIND = "industrial"; //$NON-NLS-1$

	/**
	 * The label displayed in the New Repository Wizard
	 */
	public static final String CONNECTOR_LABEL = "Industrial"; //$NON-NLS-1$

	public static final String REPOSITORY_SHORT = "Industrial"; //$NON-NLS-1$

	/**
	 * all repository properties must identify their origins as a matter of good
	 * manners, see the repository spy View to know what I mean.
	 */
	private static final String CONNECTOR_PREFIX = CONNECTOR_KIND + "."; //$NON-NLS-1$

	// FIXME add connector prefix for the below properties
	public static final String CAN_CREATE_NEW_TASK = "can-create-new-task"; //$NON-NLS-1$

	public static final String CAN_POST_ATTACHMENTS = "can-post-attachments"; //$NON-NLS-1$

	public static final String CAN_GET_ATTACHMENTS = "can-get-attachments"; //$NON-NLS-1$

	public static final String CAN_CREATE_TASK_FROM_KEY = "can-create-task-from-key"; //$NON-NLS-1$

	// FIXME never used
	public static final String CAN_SYNCHRONIZE_TASKS = "can-synchronize-tasks"; //$NON-NLS-1$

	// FIXME never used
	public static final String CAN_QUERY_REPOSITORY = "can-query-repository"; //$NON-NLS-1$

	// FIXME never used
	public static final String CAN_DELETE_TASKS = "can-delete-tasks"; //$NON-NLS-1$

	
	public static final String REPOSITORY_CONFIG_NAME = "repository_config_name"; //$NON-NLS-1$

	public static final String SQLMAP_CONFIG = CONNECTOR_PREFIX
			+ "sqlmap_config"; //$NON-NLS-1$

	public static final String SQLMAP_PROPS = CONNECTOR_PREFIX
			+ "sqlmap_properties"; //$NON-NLS-1$

	public static final String SQLMAP_PATH = CONNECTOR_PREFIX + "sqlmap_path"; //$NON-NLS-1$

	public static final String SQLMAP_INCLUDED = CONNECTOR_PREFIX + "included"; //$NON-NLS-1$

	public static final String REPOSITORY_ATTRIBUTES = CONNECTOR_PREFIX + "attributes"; //$NON-NLS-1$
	
	public static final String REPOSITORY_SHORT_PROP = CONNECTOR_PREFIX + "shortname"; //$NON-NLS-1$
	
	// The shared instance
	private static IndustrialCore plugin;

	private IndustrialRepositoryConnector connector;

	/**
	 * The constructor
	 */
	public IndustrialCore() {
	}

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

		// FIXME enable logging of ibatis library here somewhere
		// com.ibatis.common.logging.LogFactory.selectLog4JLogging();
		// DBClientLog.initCommonsLoggingSettings();
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
		if (connector != null) {
			connector.stop();
			connector = null;
		}
		forgetTaskSqlMapConfigs();
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static IndustrialCore getDefault() {
		return plugin;
	}

	/**
	 * @param e
	 * @param repository
	 * @return IStatus containg repository label and exception
	 */
	public static IStatus toStatus(Throwable e, TaskRepository repository) {
		if (e instanceof ClassCastException) {
			return new RepositoryStatus(IStatus.ERROR, PLUGIN_ID, RepositoryStatus.ERROR_IO,
					repository
					.getRepositoryLabel()
					+ ": Unexpected server response: " + e.getMessage(), e); //$NON-NLS-1$
		} else if (e instanceof MalformedURLException) {
			return new RepositoryStatus(IStatus.ERROR, PLUGIN_ID, RepositoryStatus.ERROR_IO,
					repository
					.getRepositoryLabel()
					+ ": Repository URL is invalid", e); //$NON-NLS-1$
		} else {
			return new RepositoryStatus(IStatus.ERROR, PLUGIN_ID, RepositoryStatus.ERROR_INTERNAL, repository
					.getRepositoryLabel()
					+ ": Unexpected error", e); //$NON-NLS-1$
		}
	}

	/**
	 * @param repository
	 * @return the SqlMaps configuration for this repository.
	 * @throws CoreException
	 */
	public SqlMapClient getTaskSqlMapConfig(TaskRepository repository) throws CoreException {
		return TasksSqlMapConfig.getSqlMapInstance(repository);
	}

	/**
	 * Clear the cached configuration of this repository.
	 * 
	 * @param repository
	 *            to forget
	 * @throws CoreException
	 */
	public void forgetTaskSqlMapConfig(TaskRepository repository) throws CoreException {
		TasksSqlMapConfig.forgetMap(repository);
	}

	/**
	 * Clear all known connector repository configurations.
	 */
	public void forgetTaskSqlMapConfigs() {
		try {
			TasksSqlMapConfig.forgetMaps();
		} catch (CoreException e) {
			CoreLogger.logError(e);
		}
	}

	/**
	 * @param connector
	 *            to use
	 */
	public void setConnector(IndustrialRepositoryConnector connector) {
		this.connector = connector;
	}

	/**
	 * @return the connector
	 */
	public IndustrialRepositoryConnector getConnector() {
		return this.connector;
	}

	/**
	 * Returns the path to the file caching repository attributes.
	 */
	protected IPath getRepositoryAttributeCachePath() {
		IPath stateLocation = Platform.getStateLocation(getBundle());
		IPath cacheFile = stateLocation.append("repositoryConfigurations"); //$NON-NLS-1$
		return cacheFile;
	}

	/**
	 * @param repository
	 * @return the name of the user to be used in reporting
	 */
	public String getLocalUserName(TaskRepository repository) {
		// TODO either store a separate reporter name in the repository
		// properties or use this.
		return (null != repository.getUserName() ? repository.getUserName()
				: (null != System.getProperty("user.name") ? System.getProperty("user.name") : "NN"));   //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
	}

	/**
	 * @param repository
	 * @return true when this connector can initialize a new persistence store
	 * @throws CoreException
	 */
	public boolean canInitialize(TaskRepository repository) throws CoreException {
		try {
			return PersistorsManager.getManager().getPersistor(repository).canInitialize(repository);
		} catch (SQLException e) {
			IStatus status = CoreLogger.createStatus(IStatus.WARNING, "Cannot Initialize repository", e); //$NON-NLS-1$
			CoreLogger.log(status);
			// throw new CoreException(status);
		}
		return false;
	}
	
	
	
	
	/**
	 * @param repository
	 * @throws CoreException
	 */
	public void initializeRepository(TaskRepository repository) throws CoreException {
		// TODO check for existence of Extension Point, only initialize when
		// present
		try {
			PersistorsManager.getManager().getPersistor(repository).initialize(repository);
		} catch (SQLException e) {
			IStatus status = CoreLogger.createStatus(IStatus.WARNING, "Failed to initialize repository", e); //$NON-NLS-1$
			CoreLogger.log(status);
			// throw new CoreException(status);

		}

	}

	/**
	 * @param repository
	 * @return true when the validation succeeds
	 * @throws CoreException
	 */
	public boolean validateRepository(TaskRepository repository) throws CoreException {

		try {
			return PersistorsManager.getManager().getPersistor(repository).validate(repository);
		} catch (SQLException e) {
			IStatus status = CoreLogger.createStatus(IStatus.WARNING, "Failed to validate repository", e); //$NON-NLS-1$
			CoreLogger.log(status);
			// throw new CoreException(status);

		}
		return false;
	}

	/**
	 * @param repository
	 * @return true when the authetication succeeds
	 * @throws CoreException
	 */
	public boolean authenticateRepository(TaskRepository repository) throws CoreException {

		try {
			return PersistorsManager.getManager().getPersistor(repository).isAuthenticated(repository);
		} catch (SQLException e) {
			IStatus status = CoreLogger.createStatus(IStatus.WARNING, "Failed to autheticate with repository", e); //$NON-NLS-1$
			CoreLogger.log(status);
			// throw new CoreException(status);
		}
		return false;
	}
}
