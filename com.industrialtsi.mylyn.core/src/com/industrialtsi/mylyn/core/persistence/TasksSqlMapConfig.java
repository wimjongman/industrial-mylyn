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

package com.industrialtsi.mylyn.core.persistence;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.mylyn.commons.net.AuthenticationCredentials;
import org.eclipse.mylyn.commons.net.AuthenticationType;
import org.eclipse.mylyn.tasks.core.TaskRepository;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;
import com.industrialtsi.mylyn.core.IndustrialCore;
import com.industrialtsi.mylyn.core.config.IbatisConfig;
import com.industrialtsi.mylyn.core.internal.CoreLogger;

/**
 * TasksSqlMapConfig -- read in the configuration and the database parameters
 * for SqlMaps for use with the <code>com.industrialtsi.mylyn.core</code> package
 * 
 * @author Maarten Meijer
 * 
 * @hidden
 * 
 */
public class TasksSqlMapConfig {

	private static final String DRIVER_KEY = "driver"; //$NON-NLS-1$

	private static final String DB_PASSWORD_KEY = "db_password"; //$NON-NLS-1$

	private static final String DB_USER_KEY = "db_user"; //$NON-NLS-1$

	private static final String PASSWORD_KEY = "password"; //$NON-NLS-1$

	private static final String USER_KEY = "user"; //$NON-NLS-1$

	private static final String EMPTY_STRING = ""; //$NON-NLS-1$

	private static final String SQLMAP_PATH_KEY = "sqlmap_path"; //$NON-NLS-1$

	public static final String SQLMAP_CONFIG = IndustrialCore.CONNECTOR_KIND
			+ ".sqlmap_config"; //$NON-NLS-1$

	public static final String SQLMAP_PROPS = IndustrialCore.CONNECTOR_KIND
			+ ".sqlmap_properties"; //$NON-NLS-1$

	public static final String SQLMAP_PATH = IndustrialCore.CONNECTOR_KIND
			+ ".sqlmap_path"; //$NON-NLS-1$

	public static final String SQLMAP_INCLUDED = IndustrialCore.CONNECTOR_KIND
			+ ".included"; //$NON-NLS-1$

	private static Map<String, SqlMapClient> sqlMaps = new HashMap<String, SqlMapClient>();

	/**
	 * @return SqlMap as used buy the various beans
	 * @throws CoreException
	 */
	public static SqlMapClient getSqlMapInstance(TaskRepository repository)
			throws CoreException {


		SqlMapClient sqlMap = sqlMaps.get(getRepositoryKey(repository));

		if (null == sqlMap) {
			sqlMap = initSqlMap(repository);
			sqlMaps.put(getRepositoryKey(repository), sqlMap);
		}
		return sqlMap;
	}

	private static SqlMapClient initSqlMap(TaskRepository repository) throws CoreException {

		String include = repository.getProperty(TasksSqlMapConfig.SQLMAP_INCLUDED);
		String config = repository.getProperty(TasksSqlMapConfig.SQLMAP_CONFIG);

		if (null == include && null == config) {
			Status init = new Status(IStatus.ERROR, IndustrialCore.PLUGIN_ID, "Ibatis Connector has no configuration parameters", new Exception()); //$NON-NLS-1$
			CoreLogger.log(init);
			throw new CoreException(init);
		}
		// FIXME clean up code below to reduce Cyclomatic Complexity
		
		SqlMapClient newSqlMap = null;
		
		InputStream resource = null;
		InputStream propStream = null;
		Properties properties = new Properties();
		try {
			// we are going to try to give the SqlConfigMap.xml and
			// properties to the Ibatis code.

			// retrieve the ibatis configuration from the extension point
			if (null != include) {
				IbatisConfig template = PersistorsManager.getManager()
						.getIbatisConfig(repository, include);

				if (null != template) {
					// we are initializing from extension point
					config = template.getMaps();

					properties.setProperty(DRIVER_KEY, template.getDriver());
					properties.setProperty(DB_USER_KEY, template.getUsername()); 
					properties.setProperty(DB_PASSWORD_KEY, template.getPassword()); 

					Map<String, String> persistorProperties = PersistorsManager
							.getManager().getPersistorProperties(repository);
					properties.putAll(persistorProperties);

				} else {
					// we are initializing from the old resources in jar
					config = "maps/" + include + "/SqlMapConfig.xml"; //$NON-NLS-1$ //$NON-NLS-2$

					String props = repository
							.getProperty(TasksSqlMapConfig.SQLMAP_PROPS);
					if (null == props || EMPTY_STRING.equals(props)) {
						// TODO remove and throw error
						props = "maps/" + include + "/db.properties"; //$NON-NLS-1$ //$NON-NLS-2$
						Path propPath = new Path(props);
						propStream = FileLocator.openStream(IndustrialCore
								.getDefault().getBundle(), propPath, false);
					} else {
						propStream = new FileInputStream(props);
					}
					properties.load(propStream);
				}

			}
			Path mapPath = new Path(config);
			resource = FileLocator.openStream(IndustrialCore.getDefault()
						.getBundle(), mapPath, false);


			// save for later use without username and password!
			for (Entry<Object, Object> entry : properties.entrySet()) {
				repository.setProperty((String) entry.getKey(), (String) entry
						.getValue());
			}

			String repositoryUrl = repository.getUrl();
			if (repositoryUrl.contains("mysql") //$NON-NLS-1$
					&& !repositoryUrl.contains("?allowMultiQueries")) { // to //$NON-NLS-1$
				// prevent jdbc URL pollution...
				properties
						.put("url", repositoryUrl + "?allowMultiQueries=true"); //$NON-NLS-1$ //$NON-NLS-2$
			} else {
				properties.put("url", repositoryUrl); //$NON-NLS-1$
			}

			if (null != repository.getProperty(TasksSqlMapConfig.SQLMAP_PATH)) {
				properties.put(SQLMAP_PATH_KEY, repository
						.getProperty(TasksSqlMapConfig.SQLMAP_PATH));
			} else {
				properties.put(SQLMAP_PATH_KEY, EMPTY_STRING);
			}

			AuthenticationCredentials credentials = repository
					.getCredentials(AuthenticationType.REPOSITORY);
			if (null != credentials && null != credentials.getPassword()) {
				properties.put(USER_KEY, credentials.getUserName());
				properties.put(PASSWORD_KEY, credentials.getPassword());
			}

			newSqlMap = SqlMapClientBuilder
					.buildSqlMapClient(resource, properties);
		} catch (Throwable e) {
			// TODO replace CoreException with SQLException?
			Status init = new Status(IStatus.ERROR, IndustrialCore.PLUGIN_ID, e
					.getMessage(), e.getCause());
			CoreLogger.log(init);
			throw new CoreException(init);
		} finally {
			try {
				if (null != propStream) {
					propStream.close();
				}
			} catch (IOException e) {
				CoreLogger.logError(e);
			}
		}
		return newSqlMap;
	}

	private static String getRepositoryKey(TaskRepository repository) {
		String repositoryUrl = repository.getUrl();
		String include = repository
				.getProperty(TasksSqlMapConfig.SQLMAP_INCLUDED);
		if (null != include) {
			return repositoryUrl + include.hashCode();
		}

		String config = repository.getProperty(TasksSqlMapConfig.SQLMAP_CONFIG);
		if (null != config) {
			return repositoryUrl + config.hashCode();
		}
		return repositoryUrl;
	}

	/**
	 * Forget the sqlmaps cached by ibatis.
	 * 
	 * @param repository
	 * @throws CoreException
	 */
	public static void forgetMap(TaskRepository repository)
			throws CoreException {
		String repositoryKey = getRepositoryKey(repository);
		forgetMap(repositoryKey);

	}

	/**
	 * Worker method doing the work.
	 * 
	 * @param repositoryKey
	 * @throws CoreException
	 */
	private static void forgetMap(String repositoryKey) throws CoreException {
		SqlMapClient client = sqlMaps.get(repositoryKey);
		if (null == client) {
			return;
		}
		try {
			Connection currentConnection = client.getCurrentConnection();
			if (null != currentConnection)
				currentConnection.close();
		} catch (SQLException e) {
			IStatus status = CoreLogger.createStatus(IStatus.ERROR, e);
			throw new CoreException(status);
		} finally {
			sqlMaps.remove(repositoryKey);
		}
	}

	/**
	 * Forget all maps cached by Ibatis.
	 * 
	 * @throws CoreException
	 */
	public static void forgetMaps() throws CoreException {
		// TODO should all maps be cleared with flush statements?
		for (String rep : sqlMaps.keySet()) {
			forgetMap(rep);
		}
		sqlMaps.clear();
	}

	private TasksSqlMapConfig() {
		super();
		// Singleton
	}

}
