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
package com.industrialtsi.mylyn.test.db.core;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.junit.Before;
import org.junit.Test;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.industrialtsi.mylyn.core.IndustrialCore;
import com.industrialtsi.mylyn.core.internal.RepositoryUtils;
import com.industrialtsi.mylyn.core.persistence.TasksSqlMapConfig;
import com.industrialtsi.mylyn.test.db.AbstractIbatisTestCase;

/**
 * @author Maarten Meijer
 */
public class IbatisCorePluginTest extends AbstractIbatisTestCase {

	@Test
	public void testGetTaskSqlMapConfigExternalPath() {

		assertNotNull("IBATIS_CONFIGPATH environment not set",
				IBATIS_CONFIGPATH);
		assertFalse("IBATIS_CONFIGPATH environment not set",
				!"".equals(IBATIS_CONFIGPATH));

		assertNotNull("Repository Url should be set", IBATIS_REPOSITORY_URL);
		assertNotNull("Username should be set", USERNAME);
		assertNotNull("Password should be set", PASSWORD);

		// try {
			TaskRepository repository = new TaskRepository(IndustrialCore.CONNECTOR_KIND, IBATIS_REPOSITORY_URL);
			assertNotNull("Repository creation failed", repository);

			setDefaultCredentials(repository);

			repository.setProperty(TasksSqlMapConfig.SQLMAP_CONFIG, IBATIS_CONFIGPATH + "SqlMapConfig.xml");
			repository.setProperty(TasksSqlMapConfig.SQLMAP_PROPS, IBATIS_CONFIGPATH + "db.properties");
			repository.setProperty(TasksSqlMapConfig.SQLMAP_PATH, IBATIS_CONFIGPATH);

			// FIXME fix beyond here
			// SqlMapClient test =
			// IndustrialCore.getDefault().getTaskSqlMapConfig(repository);
			// assertNotNull(test);
//		} catch (CoreException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			failDeep(e);
		// }
	}

	@Test
	public void testGetTaskSqlMapConfigResourceMap() {

		assertNotNull("IBATIS_INCLUDEDCONFIG environment not set", IBATIS_INCLUDEDCONFIG);
		assertTrue("IBATIS_INCLUDEDCONFIG environment not set", !"".equals(IBATIS_INCLUDEDCONFIG));

		String[] existing = RepositoryUtils.getRepositoryIDs();

		assertTrue("IBATIS_INCLUDEDCONFIG set to unknown value: " + IBATIS_INCLUDEDCONFIG, Arrays.asList(existing)
				.contains(IBATIS_INCLUDEDCONFIG));

		assertNotNull("Repository Url should be set", IBATIS_REPOSITORY_URL);
		assertNotNull("Username should be set", USERNAME);
		assertNotNull("Password should be set", PASSWORD);

		try {
			TaskRepository repository = new TaskRepository(IndustrialCore.CONNECTOR_KIND, IBATIS_REPOSITORY_URL);
			assertNotNull("Repository creation failed", repository);

			setDefaultCredentials(repository);

			repository.setProperty(TasksSqlMapConfig.SQLMAP_INCLUDED, IBATIS_INCLUDEDCONFIG);

			SqlMapClient test = IndustrialCore.getDefault().getTaskSqlMapConfig(repository);
			assertNotNull(test);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testGetTaskSqlMapConfigDefault() {

		assertFalse("IBATIS_INCLUDEDCONFIG environment is set",
				"".equals(IBATIS_INCLUDEDCONFIG));
		assertTrue("IBATIS_CONFIGPATH environment is set", "".equals(IBATIS_CONFIGPATH));

		assertNotNull("Repository Url should be set", IBATIS_REPOSITORY_URL);
		assertNotNull("Username should be set", USERNAME);
		assertNotNull("Password should be set", PASSWORD);

		try {
			TaskRepository repository = new TaskRepository(IndustrialCore.CONNECTOR_KIND, IBATIS_REPOSITORY_URL);
			assertNotNull("Repository creation failed", repository);

			setDefaultCredentials(repository);

			repository.setProperty(TasksSqlMapConfig.SQLMAP_INCLUDED, "derby_local_demo");

			SqlMapClient test = IndustrialCore.getDefault().getTaskSqlMapConfig(repository);
			assertNotNull("client not initialized", test);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testLog4Jprops() {
		File props = null;
		try {
			props = Resources.getResourceAsFile("log4j.properties");

			assertNotNull("log4j.properties not found on class path", props);

		} catch (IOException e) {
		}

	}

	@Override
	@Before
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		IndustrialCore.getDefault().forgetTaskSqlMapConfigs();
	}

}
