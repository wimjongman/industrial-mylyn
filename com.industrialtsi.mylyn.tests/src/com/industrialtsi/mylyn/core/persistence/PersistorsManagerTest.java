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

import java.util.Collection;
import java.util.Set;


import com.industrialtsi.mylyn.core.persistence.IPersistor;
import com.industrialtsi.mylyn.core.persistence.PersistorsManager;
import com.industrialtsi.mylyn.test.db.IbatisTest;

/**
 * <code>TestPersistorsManagerTest</code> : TODO describe.
 * 
 * @author maarten
 * 
 */
public class PersistorsManagerTest extends IbatisTest {

	/* @see junit.framework.TestCase#setUp() */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		initTestRepository();
	}

	/* @see junit.framework.TestCase#tearDown() */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetPersistorManager() {
		PersistorsManager manager = PersistorsManager.getManager();
		assertNotNull("PersistorManager not found", manager);

		Set<String> names = manager.getRepositoryNames();
		assertTrue("No names specified", !names.isEmpty());

		Collection<String> ids = manager.getPersistorsIds();
		assertTrue("Connector.xml is obsolete", ids.isEmpty());

		// fail("not yet implemented");
	}

	public void testGetPersistor() {
		PersistorsManager manager = PersistorsManager.getManager();
		assertNotNull("PersistorManager not found", manager);

		Set<String> names = manager.getRepositoryNames();
		assertTrue("No names specified", !names.isEmpty());
		for (String name : names) {
			IPersistor rep = manager.getPersistor(name);
			System.out.println("getting persistor for " + name);
			assertNotNull("repository " + name + "not initialized properly",
					rep);
		}
		// fail("not yet implemented");
	}
}
