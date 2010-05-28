/*******************************************************************************
 * Copyright (c) 2008,2009,2010 Industrial TSI and Maarten Meijer.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Industrial TSI - initial API and implementation   
 *******************************************************************************/
package com.industrialtsi.mylyn.core.persistence;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.spi.RegistryContributor;
import org.eclipse.mylyn.tasks.core.TaskRepository;

import com.industrialtsi.mylyn.core.IndustrialCore;
import com.industrialtsi.mylyn.core.config.IbatisConfig;
import com.industrialtsi.mylyn.core.config.PersistorConfig;
import com.industrialtsi.mylyn.core.internal.ConnectorXmlConfig;
import com.industrialtsi.mylyn.core.internal.CoreLogger;
import com.industrialtsi.mylyn.core.internal.RepositoryUtils;

/**
 * <code>PersistorsManager</code>, intermediates between a repository and its
 * declared persistor; that is, a class implementing
 * <code>com.industrialtsi.mylyn.core.persistence.IPersistor</code>.
 * 
 * 
 * @author Ahmed Aadel
 * 
 * @since 0.8.0
 * 
 */

public class PersistorsManager {

//	private static final String JDBC_DEFAULT_PATTERN = "^jdbc:[a-zA-Z0-9]+:[/:a-zA-Z0-9\\.]*(|:\\d+)*[/a-zA-Z0-9].*$";
	
	private static final String TRUE = "true"; //$NON-NLS-1$

	private static final String CLASS = "class"; //$NON-NLS-1$

	private static final String JDBC_URL_RE_ID = "jdbc-url-re"; //$NON-NLS-1$

	private static final String JDBC_URL_TEMPLATE_ID = "jdbc-url-template"; //$NON-NLS-1$

	private static final String JDBC_DEFAULT_TEMPLATE = "jdbc:driver://server:port/database"; //$NON-NLS-1$

	private static final String JDBC_DEFAULT_PATTERN = "^jdbc:.*$"; //$NON-NLS-1$

	public static final String PERSISTOR_ID = "persistor"; //$NON-NLS-1$

	public static final String CONFIG_ID = "persistor-config"; //$NON-NLS-1$

	private static final String EXTENSION_POINT = com.industrialtsi.mylyn.core.IndustrialCore.PLUGIN_ID
			+ "." + PERSISTOR_ID; //$NON-NLS-1$

	private static PersistorsManager singleton;

	private static IConfigurationElement[] config;

	/*
	 * Map of pairs such as (IPersistor implementor's fully qualified path used
	 * as id, the IPersistor object read from extension point then
	 * instantiated).
	 */
	private static Map<String, IPersistor> persistorsMap;

	private static Map<String, PersistorConfig> configsMap;

	/**
	 * singleton, so private constructor
	 */
	private PersistorsManager() {
	}

	static {

		IStatus status = Status.OK_STATUS;
		persistorsMap = new HashMap<String, IPersistor>();
		configsMap = new HashMap<String, PersistorConfig>();
		config = Platform.getExtensionRegistry().getConfigurationElementsFor(
				EXTENSION_POINT);
		for (IConfigurationElement elem : config) {
			try {
				if (elem.getName().equals(CONFIG_ID)) {
					// prefer extension point
					PersistorConfig conf = parsePersistorConfig(elem);
					if (null != conf) {
						configsMap.put(conf.getName(), conf);
						CoreLogger.logInfo("Installed persistor-config : " + conf.getName()); //$NON-NLS-1$
					} else {
						CoreLogger.log(CoreLogger.createStatus(IStatus.WARNING, "Failed to parse persistor-config", null)); //$NON-NLS-1$
					}
				} else if (elem.getName().equals(PERSISTOR_ID)) {
					// tell the user about it
					IContributor contributor = elem.getContributor();
					String contributorName = contributor.getName();
					if  (contributor instanceof RegistryContributor)
					    contributorName = ((RegistryContributor) contributor).getActualName();
					IStatus obsolete = CoreLogger.createStatus(IStatus.WARNING, 0, "Using old style Persistor extension point in " + contributorName, null); //$NON-NLS-1$
					CoreLogger.log(obsolete);
					// fall back to connector.xml
					IPersistor persistor = (IPersistor) elem
							.createExecutableExtension(CLASS); 
					if (persistor != null) {
						persistorsMap
								.put(elem.getAttribute(CLASS), persistor); 
						CoreLogger.logInfo("Installed persistor : " + elem.getAttribute(CLASS)); //$NON-NLS-1$ 
						// go ahead and parse the connector.xml file
						ConnectorXmlConfig.getInstance()
								.parseConnectorXmlFile();
					} else {
						CoreLogger.log(CoreLogger.createStatus(IStatus.WARNING, "Failed to create persistor : " + elem.getAttribute(CLASS), null)); //$NON-NLS-1$
					}
				}

			} catch (CoreException e) {
				status = CoreLogger.createStatus(IStatus.ERROR, e.getMessage(),
						e.getCause());
				CoreLogger.log(status);
			}
		}
		// make sure at least one persistor object is provided at our extension
		// point.
		//Assert.isLegal(!persistorsMap.isEmpty(),
		//		"No extension found at extension point: " + EXTENSION_POINT);

	}

	public static PersistorsManager getManager() {
		if (singleton == null) {
			singleton = new PersistorsManager();
		}
		return singleton;
	}

	private static PersistorConfig parsePersistorConfig(
			IConfigurationElement elem) throws CoreException {
		// TODO Auto-generated method stub
		PersistorConfig persistorConfig = new PersistorConfig();

		persistorConfig.setId(getStringAttribute(elem, "id", null)); //$NON-NLS-1$
		persistorConfig.setName(getStringAttribute(elem, "name", "<not set>")); //$NON-NLS-1$ //$NON-NLS-2$
		persistorConfig.setPersistorFQCN(getStringAttribute(elem, "persistor", //$NON-NLS-1$
				PersistorAdapter.class.getName()));
		persistorConfig.setJdbcUrlTemplate(getStringAttribute(elem,
				JDBC_URL_TEMPLATE_ID, JDBC_DEFAULT_TEMPLATE));
		persistorConfig.setJdbcUrlRegularExpression(getStringAttribute(elem,
				JDBC_URL_RE_ID, JDBC_DEFAULT_PATTERN));

		// remove creation here with lazy initialization once needed.
		persistorConfig.setPersistor((IPersistor) elem
				.createExecutableExtension("persistor")); //$NON-NLS-1$

		for (IConfigurationElement child : elem.getChildren()) {
			// add the repository attributes
			if (child.getName().equals("repository-attributes")) { //$NON-NLS-1$
				parseRepositoryAttributes(child, persistorConfig);
			}

			// add the task attributes
			if (child.getName().equals("task-attributes")) { //$NON-NLS-1$
				parseTaskAttributes(persistorConfig, child);
			}

			// add the ibatis config data
			if (child.getName().equals("ibatis-config")) { //$NON-NLS-1$
				IbatisConfig config = parserIbatisConfig(child);
				persistorConfig.addIbatisConfig(config);
			}
		}

		return persistorConfig;

	}

	private static void parseTaskAttributes(PersistorConfig persistorConfig,
			IConfigurationElement child) {
		for (String key: child.getAttributeNames()) {
			String value = child.getAttribute(key);
			// add the repository attributes
			if (value.equalsIgnoreCase("readonly")) { //$NON-NLS-1$
				persistorConfig.addAttribute(key + "_readOnly", TRUE); //$NON-NLS-1$ 
			}
		}
	}

	private static void parseRepositoryAttributes(IConfigurationElement child,
			PersistorConfig persistorConfig) {
		for (String key: child.getAttributeNames()) {
			String value = child.getAttribute(key);
			// add the repository attributes
			if (value.equals(TRUE)) { 
				persistorConfig.addAttribute(key, value);
			}
		}
	}

	private static IbatisConfig parserIbatisConfig(IConfigurationElement elem) {
		IbatisConfig config = new IbatisConfig();
		config.setDriver(getStringAttribute(elem, "driver", null)); //$NON-NLS-1$
		config.setName(getStringAttribute(elem, "name", null)); //$NON-NLS-1$
		config.setMaps(getStringAttribute(elem, "maps", null)); //$NON-NLS-1$
		config.setUsername(getStringAttribute(elem, "username", "name")); //$NON-NLS-1$ //$NON-NLS-2$
		config.setPassword(getStringAttribute(elem, "password", "secret"));  //$NON-NLS-1$//$NON-NLS-2$
		return config;
	}

	private static String getStringAttribute(IConfigurationElement elem,
			String name, String defVal) {
		String value = elem.getAttribute(name);
		if (null != value)
			return value;
		if (null != defVal)
			return defVal;
		throw new IllegalArgumentException("Missing " + name + " attribute"); //$NON-NLS-1$ //$NON-NLS-2$
	}


	/**
	 * @return A set of repositories names as defined in the
	 *         <code>connector.xml</code> file.
	 */
	public Set<String> getRepositoryNames() {
		// FIXME add extension point based names
		Set<String> result = new HashSet<String>();
		result.addAll(configsMap.keySet());
		result.addAll(ConnectorXmlConfig.getInstance().getRepositoryPersistorPairs()
				.keySet());
		return result;
	}

	/**
	 * @return an array of parsed repository names
	 */
	public String[] getRepositoryNamesAsArray() {
		String names[] = new String[getRepositoryNames().size()];
		// FIXME add extension point based names
		return getRepositoryNames().toArray(names);
	}

	/**
	 * @return A collection of id's of <code>IPersistor</code> implementers as
	 *         defined in the <code>connector.xml</code> file within the element
	 *         <code>class</code>. The fully qualified path is used as id.
	 *         
	 * @deprecated is never used till version 0.9.4
	 */
	@Deprecated
	public Collection<String> getPersistorsIds() {
		// FIXME add extension point based names
		return ConnectorXmlConfig.getInstance().getRepositoryPersistorPairs()
				.values();
	}

	/**
	 * @param repositoryName
	 *            The repository looking for its persistor
	 * @return The <code>IPersistor</code> object if found. Unchecked exception
	 *         is thrown, otherwise.
	 */
	public IPersistor getPersistor(TaskRepository repository) {
		String repositoryName = repository
				.getProperty(IndustrialCore.REPOSITORY_CONFIG_NAME);
		return getPersistor(repositoryName);
	}

	/**
	 * @param repositoryName
	 *            Repository name
	 * @return This repository persistor if it exists.
	 */
	public IPersistor getPersistor(String repositoryName) {

		IPersistor p = null;
		// FIXME add extension point based persistors
		if(configsMap.keySet().contains(repositoryName)) {
			PersistorConfig item = configsMap.get(repositoryName);
			p = item.getPersistor();
		} else {
			String pid = ConnectorXmlConfig.getInstance()
			.getRepositoryPersistorPairs().get(repositoryName);
			p = persistorsMap.get(pid);
		}
		
		Assert
				.isNotNull(
						p,
						"No persistor for repository: \"" //$NON-NLS-1$
								+ repositoryName
								+ "\" can be found.\n Please make sure it is indeed added as extension to: " //$NON-NLS-1$
								+ EXTENSION_POINT);
		return p;
	}

	/**
	 * @param i
	 *            Index of the chosen repository configuration in the setting
	 *            page user-interface
	 * @return true if the selected repository persistor is of type
	 *         <code>IbatisPersistor</code>
	 */
	public boolean isIbatisRepository(int i) {
		return getPersistor(getRepositoryNamesAsArray()[i]) instanceof IbatisPersistor;
	}

	public String[] getIbatisMapLocations(String persistorConfigName) {
		if(configsMap.keySet().contains(persistorConfigName)) {
			PersistorConfig item = configsMap.get(persistorConfigName);
			return item.getIbatisConfigNames().toArray(new String[0]);
		} else {
			// Old crude method that shows all
			return RepositoryUtils.getRepositoryIDs();
		}
	}
	
	/**
	 * @param persistorConfigName
	 * @return the jdbc url example to enter in the url field
	 */
	public String getJdbcUrlTemplate(String persistorConfigName) {
		PersistorConfig item = configsMap.get(persistorConfigName);
		return null == item ? null : item.getJdbcUrlTemplate();
	}

	/**
	 * @param persistorConfigName
	 * @return the regular expression string to match the entered URL against
	 */
	public String getJdbcUrlRegularExpression(String persistorConfigName) {
		PersistorConfig item = configsMap.get(persistorConfigName);
		return null == item ? null : item.getJdbcUrlRegularExpression();
	}

	/**
	 * @param repository the taskRepository
	 * @param map the chosen maps configuration
	 * @return the (optional) IbatisConfig element
	 */
	public IbatisConfig getIbatisConfig(TaskRepository repository, String map) {
		String name = repository.getProperty(IndustrialCore.REPOSITORY_CONFIG_NAME);
		assert null != name;
		return getIbatisConfig(name, map);
		
	}

	/**
	 * @param name the configuration name
	 * @param map the chosen maps configuration
	 * @return the ibatis config
	 */
	public IbatisConfig getIbatisConfig(String name, String map) {
		PersistorConfig item = configsMap.get(name);
		return null == item ? null : item.getIbatisConfig(map);
	}

	/**
	 * @param repository
	 * @return the properties map from the connector for this repository
	 */
	public Map<String, String> getPersistorProperties(TaskRepository repository) {
		String name = repository.getProperty(IndustrialCore.REPOSITORY_CONFIG_NAME);
		assert null != name;
		return getPersistorProperties(name);
	}

	/**
	 * @param persistorConfigName
	 * @return the properties map from the connector for this persistorConfigName
	 */
	public Map<String, String> getPersistorProperties(String persistorConfigName) {
		PersistorConfig item = configsMap.get(persistorConfigName);
		return null == item ? null : item.getAttributes();
	}

	/**
	 * Adds the properties from the template to the repository
	 * @param repository
	 */
	public void initialize(TaskRepository repository) {
		Map<String, String> properties = getPersistorProperties(repository);
		// we are initializing from extension point
		if(null != properties) {
			for(Entry<String, String> entry: properties.entrySet()) {
				repository.setProperty(entry.getKey(), entry.getValue());
			}
			// TODO add more props?
//			IbatisConfig template = PersistorsManager.getManager()
//			.getIbatisConfig(repository, include);

		} else {
			ConnectorXmlConfig.getInstance().populateTaskRepositoryFromXml(
					repository);
		}
	}
	
}
