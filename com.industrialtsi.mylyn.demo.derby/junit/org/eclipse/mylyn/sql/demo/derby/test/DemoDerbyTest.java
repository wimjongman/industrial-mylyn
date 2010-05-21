/*******************************************************************************
 * Copyright (c) 2008 Industrial TSI and Maarten Meijer.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Maarten Meijer - initial API and implementation
 *     Industrial TSI - improvements
 *******************************************************************************/package org.eclipse.mylyn.sql.demo.derby.test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import com.industrialtsi.mylyn.core.IndustrialCore;
import com.industrialtsi.mylyn.core.dto.IndustrialQueryParams;
import com.industrialtsi.mylyn.test.db.IbatisTest;

import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * run this with -DIBATIS_REPOSITORY_URL=jdbc:derby:/Users/maarten/Desktop/tasks/mylynDB
 * and -DIBATIS_INCLUDEDCONFIG=derby_local_demo
 *
 * @author maarten
 *
 */
public class DemoDerbyTest extends IbatisTest {


	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		initTestRepository();
	}



	@SuppressWarnings("unchecked")
	public void testRepositoryConfig() {

		List<String> results = new ArrayList<String>();

		IndustrialQueryParams options = new IndustrialQueryParams("");

		SqlMapClient tasksMap;
		try {
			tasksMap = IndustrialCore.getDefault().getTaskSqlMapConfig(repository);
		// FIXME add all remaining search parameters
		results = tasksMap.queryForList("Repository.validate", null);	
		assertEquals("Table inited", 0, results.get(0));
		
			
		results = tasksMap.queryForList("Repository.legalOwners", null);
		options.setOwner(results.toArray(new String[0]));

		assertTrue("Owners in DB", results.isEmpty());

		results = tasksMap.queryForList("Repository.legalProducts", null);
		options.setProduct(results.toArray(new String[0]));

		assertTrue("Products in DB", results.isEmpty());

		results = tasksMap.queryForList("Repository.legalIssueStatus", null);
		options.setIssueStatus(results.toArray(new String[0]));

		assertTrue("Issue Status in DB", results.isEmpty());

		results = tasksMap.queryForList("Repository.legalPriority", null);
		options.setPriority(results.toArray(new String[0]));

		assertTrue("Priorities in DB", results.isEmpty());

		} catch (SQLException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (CoreException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}


}
