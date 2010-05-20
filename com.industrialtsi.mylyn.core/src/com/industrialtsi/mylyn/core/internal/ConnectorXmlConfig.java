/*******************************************************************************
 * Copyright (c) 2008 Industrial TSI and Maarten Meijer.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Industrial TSI - initial API and implementation   
 *******************************************************************************/
package com.industrialtsi.mylyn.core.internal;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.industrialtsi.mylyn.core.IndustrialCore;

/**
 * <code>ConnectorXmlConfig</code>. Parses connector.xml file an makes its
 * content available.
 * 
 * @author Ahmed Aadel
 * @since 0.8.0
 * 
 * @deprecated use extension point instead
 */

@Deprecated
public class ConnectorXmlConfig {
	private final Map<String, XMLRepository> repositories;

	private HashMap<String, String> repositoryPersistorPairs;

	public static final String CONNECTOR_XML = "connector.xml"; //$NON-NLS-1$

	/**
	 * @return unmodifiable map of property name, value pairs
	 */
	public Map<String, String> getRepositoryPersistorPairs() {
		if(null != repositoryPersistorPairs) {
			return Collections.unmodifiableMap(repositoryPersistorPairs);
		} else {
			return Collections.emptyMap();
		}
	}

	// A node list of all repository elements read from the connector.xml file
	static NodeList repositoryNodes;

	private static ConnectorXmlConfig instance;

	private static Document document;

	/**
	 * @return the singleton instance
	 */
	public static ConnectorXmlConfig getInstance() {
		return (instance != null) ? instance : (instance = new ConnectorXmlConfig());
	}

	private ConnectorXmlConfig() {
		repositories = new HashMap<String, XMLRepository>();
	}

	/**
	 * 
	 */
	public void parseConnectorXmlFile() {
		repositoryPersistorPairs = new HashMap<String, String>();
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setNamespaceAware(true);
		DocumentBuilder documentBuilder = null;
		// try locating the connector.xml files
		URL connectorXmlUrl[] = FileLocator.findEntries(IndustrialCore.getDefault().getBundle(),
				new Path(CONNECTOR_XML), null);
		if (connectorXmlUrl.length == 0) {
			CoreLogger.logError("Missing file:\"" + CONNECTOR_XML + "\"", null); //$NON-NLS-1$ //$NON-NLS-2$
			return;
		}
		// try resolving the connector.xml paths
		URL resolved = null;
		for (URL resolveme : connectorXmlUrl) {
			try {
				resolved = FileLocator.resolve(resolveme);
			} catch (IOException ioe) {
				CoreLogger.logError(
						"Path \"" + resolveme + "\" to: \"" + CONNECTOR_XML + "\" file cannot be resolved.", ioe); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
			if ("file".equals(resolved.getProtocol())) { //$NON-NLS-1$
				try {
					documentBuilder = documentBuilderFactory.newDocumentBuilder();
				} catch (ParserConfigurationException e) {
					CoreLogger.logError("Can't parse: \"" + CONNECTOR_XML //$NON-NLS-1$
							+ "\" file. Please check it's a well-formed and valid xml file.", e); //$NON-NLS-1$
				}
			}
			// try parsing connector.xml file
			document = null;
			try {
				document = documentBuilder.parse(new File(FileLocator.toFileURL(resolved).getFile()));
			} catch (SAXException e) {
				CoreLogger.logError("Can't parse: \"" + CONNECTOR_XML //$NON-NLS-1$
						+ "\" file. Please check it's a well-formed and valid xml file.", e); //$NON-NLS-1$

			} catch (IOException e) {
				CoreLogger.logError("Can't read from: \"" + CONNECTOR_XML + "\" file.", e); //$NON-NLS-1$ //$NON-NLS-2$
			}
			Assert.isNotNull(document);
			repositoryNodes = document.getElementsByTagName("repository"); //$NON-NLS-1$
			// make sure at least one repository element is defined in the
			// connector.xml
			AssertRepositoryXmled();
			// pair a repository name to its persistor class name as defined in
			// connector.xml.
			parsePopulate();
		}
	}

	/**
	 * Parse the connector.xml file
	 */
	private void parsePopulate() {
		parseRepository();
	}

	/**
	 * Pair a repository name to its persistor name as defined in connector.xml.
	 * Make the whole ready as a map.
	 * 
	 */
	private void parseRepository() {

		for (int i = 0; i < repositoryNodes.getLength(); i++) {
			Element element = (Element) repositoryNodes.item(i);
			// parse repository "name" attribute
			String repositoryName = element.getAttributes().getNamedItem("name").getNodeValue(); //$NON-NLS-1$
			// parse repository "class" element
			String persistorId = element.getElementsByTagName("class").item(0).getFirstChild().getNodeValue(); //$NON-NLS-1$
			Assert.isNotNull(repositoryName);
			Assert.isNotNull(persistorId);
			repositoryPersistorPairs.put(repositoryName.trim(), persistorId.trim());
			parseRepositoryProperties(repositoryName, element);
			parseTaskProperties(repositoryName, element);
		}
	}

	/**
	 * @param repositoryName
	 *            , the parsed repository name as in connector.xml file
	 * @param taskPropertyElement
	 *            , the <code>Task</code> xml element to be yet parsed.
	 */
	private void parseTaskProperties(String repositoryName, Element taskPropertyElement) {
		// check whether a "task" element is defined within this repository
		if (taskPropertyElement.getElementsByTagName("task").item(0) == null) //$NON-NLS-1$
			return;
		// parse "task-attributes" node
		NodeList propertiesNodes = ((Element) taskPropertyElement.getElementsByTagName("task").item(0)) //$NON-NLS-1$
				.getElementsByTagName("task-attributes"); //$NON-NLS-1$
		NodeList nodes = ((Element) propertiesNodes.item(0)).getElementsByTagName("attribute"); //$NON-NLS-1$
		for (int j = 0; j < nodes.getLength(); j++) {
			Element element = (Element) nodes.item(j);
			// parse property name and value and store them in a map
			String propertyName = element.getAttributes().getNamedItem("id").getNodeValue(); //$NON-NLS-1$
			String propertyValue = element.getAttributes().getNamedItem("readonly").getNodeValue(); //$NON-NLS-1$
			addProperty(repositoryName, propertyName + "_readOnly", propertyValue); //$NON-NLS-1$
		}
	}

	/**
	 * @param repositoryName
	 *            , the parsed repository name as in connector.xml file
	 * @param repositoryPropertyElement
	 *            , the repository xml element
	 *            <code>repository-properties</code> to be yet parsed.
	 */
	private void parseRepositoryProperties(String repositoryName, Element repositoryPropertyElement) {
		NodeList propertiesNodes = repositoryPropertyElement.getElementsByTagName("repository-properties"); //$NON-NLS-1$
		// check whether a "repository-properties" element is defined within
		// this repository in the connector.xml file
		if (propertiesNodes.item(0) == null)
			return;
		NodeList nodes = ((Element) propertiesNodes.item(0)).getElementsByTagName("property"); //$NON-NLS-1$
		for (int j = 0; j < nodes.getLength(); j++) {
			Element element = (Element) nodes.item(j);
			String propertyName = element.getAttributes().getNamedItem("name").getNodeValue(); //$NON-NLS-1$
			String propertyValue = element.getAttributes().getNamedItem("value").getNodeValue(); //$NON-NLS-1$
			addProperty(repositoryName, propertyName, propertyValue);

		}

	}

	private void AssertRepositoryXmled() {
		Assert.isLegal((repositoryNodes != null && repositoryNodes.getLength() >= 0),
				"No repository definition can be found within: \"" + CONNECTOR_XML + "\" file."); //$NON-NLS-1$ //$NON-NLS-2$

	}

	/**
	 * @param repositoryName
	 * @return properties as name, value paairs
	 */
	public Map<String, String> getRepositoryProperties(String repositoryName) {
		return repositories != null ? repositories.get(repositoryName) : null;
	}

	/**
	 * @param repository
	 * @return properties as name, value paairs
	 */
	public Map<String, String> getXMLRepositoryProperties(TaskRepository repository) {
		String name = repository.getProperty(IndustrialCore.REPOSITORY_CONFIG_NAME);
		return getRepositoryProperties(name);
	}

	/**
	 * <code>XMLRepository</code> encapsulates a "repository" element data as
	 * read from the connector.xml file.
	 */
	private class XMLRepository extends HashMap<String, String> {
		private static final long serialVersionUID = 1L;
		@SuppressWarnings("unused")
		private final String repositoryName;

		XMLRepository(String repositoryName, String key, String value) {
			this.repositoryName = repositoryName;
			this.put(key, value);
			repositories.put(repositoryName, this);
		}
	}

	/**
	 * Add a key/value pair to the <code>XMLRepository</code> with the given
	 * name. Create it first if not found.
	 * 
	 * @param name
	 *            <code>XMLRepository</code> name
	 * @param key
	 *            Attribute id
	 * @param value
	 *            Value
	 */
	private void addProperty(String name, String key, String value) {
		if (repositories.containsKey(name)) {
			repositories.get(name).put(key, value);
		} else
			new XMLRepository(name, key, value);

	}

	/**
	 * @param repository
	 * @param attId
	 *            , Attribute id
	 * @return True if this attribute is configured as read-only in the file
	 *         connector.xml file, false otherwise.
	 *         
	 * @deprecated use repository.hasProperty(key + "_readOnly"); instead, stored as property.
	 */
	@Deprecated
	public boolean isAttributeReadOnly(TaskRepository repository, String attId) {
		if (getXMLRepositoryProperties(repository) == null) {
			parseConnectorXmlFile();
		}
		if (getXMLRepositoryProperties(repository) == null) {
			return false;
		}

		String readonlyValue = getXMLRepositoryProperties(repository).get(attId);
		return (readonlyValue == null) ? false : Boolean.parseBoolean(readonlyValue);
	}

	/**
	 * Set parsed values from connector.xml file as repository properties during configuration / setup.
	 * 
	 * @param repository
	 */
	public void populateTaskRepositoryFromXml(TaskRepository repository) {
		// TODO add if can initialize from extension point. IndustrialCore.REPOSITORY_CONFIG_NAME is set at this point.
		if (getXMLRepositoryProperties(repository) == null) {
			parseConnectorXmlFile();
		}
		if (getXMLRepositoryProperties(repository) == null) {
			return;
		}
		Set<Entry<String, String>> es = getXMLRepositoryProperties(repository).entrySet();
		for (Entry<String, String> e : es) {
			repository.setProperty(e.getKey(), e.getValue());
		}

	}

}
