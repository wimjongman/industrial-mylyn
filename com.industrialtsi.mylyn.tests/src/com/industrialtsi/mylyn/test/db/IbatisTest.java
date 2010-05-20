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
package com.industrialtsi.mylyn.test.db;

import junit.framework.TestCase;

import org.eclipse.mylyn.commons.net.AuthenticationCredentials;
import org.eclipse.mylyn.commons.net.AuthenticationType;
import org.eclipse.mylyn.tasks.core.TaskRepository;

import com.industrialtsi.mylyn.core.IndustrialCore;
import com.industrialtsi.mylyn.core.IndustrialRepositoryConnector;
import com.industrialtsi.mylyn.core.persistence.TasksSqlMapConfig;

/**
 * <code>IbatisTest</code> is an abstract super class for Ibatis unit tests
 * that sets up a number of constants from system properties. This allows easy
 * switching of test sets against local and remote issue databases. Parameters
 * set are:
 * <dl>
 * <dt>IBATIS_CONFIGPATH</dt>
 * <dd>the path to an <b>external</b> repository configuration , named
 * <code>SqlMapConfig.xml</code> and and properties files, named
 * <code>db.properties</code>.This path also transferred as a property into
 * the SqlMapConfig file and can be referenced as <code>${sqlmap_path}</code>.
 * For example:<br />
 * <code>&lt;sqlMap url="file://${sqlmap_path}/TaskMap.xml"/&gt;</code></dd>
 * <dt>IBATIS_INCLUDEDCONFIG</dt>
 * <dd>the pathname/menu selection to an <b>included</b> repository
 * configuration directory, containing <code>SqlMapConfig.xml</code> and and
 * properties files, named <code>db.properties</code>.This option is
 * translated into an absolute path and transferred as a property into the
 * SqlMapConfig file and can be referenced as <code>${sqlmap_path}</code>.
 * Usual reference to the maps is for example:<br />
 * <code>&lt;sqlMap file="OMS_Mysql/TaskMap.xml"/&gt;</code></dd>
 * <dt>IBATIS_USERNAME</dt>
 * <dd>the <code>username</code> to access the database.<br />
 * Can be referenced in <code>SqlMapConfix.xml</code> as <code>${user}</code>.</dd>
 * <dt>IBATIS_PASSWORD</dt>
 * <dd>the <code>password</code> to access the database.<br />
 * Can be referenced in <code>SqlMapConfix.xml</code> as
 * <code>${password}</code>.</dd>
 * <dt>IBATIS_REPOSITORY_URL</dt>
 * <dd>The <em>JDBC</em> url to acces the database.<br />
 * Can be referenced in <code>SqlMapConfix.xml</code> as
 * <code>${password}</code>. </dd>
 * </dl>
 * These properties can be set in Eclipse using the Arguments tab in the
 * Run/Debug configurations. Example:<br />
 * <br />
 * <b>-DIBATIS_CONFIGPATH=~/src/testconfig/</b><br />
 * <b>-DIBATIS_USERNAME=tester</b><br />
 * <b>-DIBATIS_PASSWORD=secret</b><br />
 * <b>-DIBATIS_REPOSITORY_URL=jdbc:mysql://localhost:3306/test</b><br />
 * <br />
 *
 * @author Maarten Meijer
 */
public abstract class IbatisTest extends TestCase {

	protected static final String IBATIS_REPOSITORY_URL = (null != System.getProperty("IBATIS_REPOSITORY_URL")) ? System
			.getProperty("IBATIS_REPOSITORY_URL")
			: "jdbc:derby:C:\\DerbyDatabases\\MyDB;create=true";

	protected static final String IBATIS_CONFIGPATH = (null != System.getProperty("IBATIS_CONFIGPATH")) ? System
			.getProperty("IBATIS_CONFIGPATH") : "";

	protected static final String IBATIS_INCLUDEDCONFIG = (null != System.getProperty("IBATIS_INCLUDEDCONFIG")) ? System
			.getProperty("IBATIS_INCLUDEDCONFIG")
			: "";

	protected static final String PASSWORD = (null != System.getProperty("IBATIS_PASSWORD")) ? System
			.getProperty("IBATIS_PASSWORD") : "secret";

	protected static final String USERNAME = (null != System.getProperty("IBATIS_USERNAME")) ? System
			.getProperty("IBATIS_USERNAME") : "App";

	protected IndustrialRepositoryConnector connector;

	protected TaskRepository repository;

	/**
	 *
	 */
	public IbatisTest() {
		super();
	}

	/**
	 * @param name
	 */
	public IbatisTest(String name) {
		super(name);
	}

	/**
	 * initialize a Test Ibatis repository to the defined parameters.
	 */
	protected void initTestRepository() {
		assertNull("Connector already initialized", connector);
		connector = new IndustrialRepositoryConnector();
//		connector.init(new TaskList());

		assertNull("Repository already initialized", repository);
		repository = new TaskRepository(IndustrialCore.CONNECTOR_KIND, IBATIS_REPOSITORY_URL);
		repository.setRepositoryLabel("JUnit Repository");
		repository.setProperty(TasksSqlMapConfig.SQLMAP_INCLUDED, IBATIS_INCLUDEDCONFIG);
		repository.setProperty(IndustrialCore.REPOSITORY_CONFIG_NAME, repository.getRepositoryLabel());
		// repository.setAuthenticationCredentials(USERNAME, PASSWORD);
		assertNotNull("Repository Name is null", repository.getProperty(IndustrialCore.REPOSITORY_CONFIG_NAME));
		
		assertNotNull("IBATIS_CONFIGPATH not set", IBATIS_CONFIGPATH);
		repository.setProperty(TasksSqlMapConfig.SQLMAP_PATH, IBATIS_CONFIGPATH);
	}

	/**
	 * Print the cause message if there is one, or the plain message.
	 *
	 * @param e
	 *            exception
	 */
	protected void failDeep(Exception e) {
		if (null != e.getCause())
			fail(e.getCause().getMessage());
		else
			fail(e.getMessage());
	}

	protected void setDefaultCredentials(TaskRepository repository) {
		AuthenticationCredentials credentials = repository.getCredentials(AuthenticationType.REPOSITORY);
		if (null == credentials) {
			credentials = new AuthenticationCredentials(USERNAME, PASSWORD);
			repository.setCredentials(AuthenticationType.REPOSITORY, credentials, true);
		}
	}

}