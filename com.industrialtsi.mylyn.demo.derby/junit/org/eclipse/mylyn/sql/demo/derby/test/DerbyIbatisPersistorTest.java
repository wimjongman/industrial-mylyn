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

package org.eclipse.mylyn.sql.demo.derby.test;

import java.sql.SQLException;

import org.eclipse.core.runtime.CoreException;
import com.industrialtsi.mylyn.core.persistence.DerbyIbatisPersistor;
import com.industrialtsi.mylyn.core.persistence.IPersistor;
import com.industrialtsi.mylyn.core.persistence.IbatisPersistorTest;

/**
 * <code>IbatisPersistorTest</code> : TODO describe.
 * 
 * @author Maarten
 * 
 */
public class DerbyIbatisPersistorTest extends IbatisPersistorTest {

	/* @see junit.framework.TestCase#setUp() */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	/* @see junit.framework.TestCase#tearDown() */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	private IPersistor createPersistor() {
		return new DerbyIbatisPersistor();
	}

	/**
	 * Test method for
	 * {@link com.industrialtsi.mylyn.core.persistence.IbatisPersistor#canInitialize(org.eclipse.mylyn.tasks.core.TaskRepository)}
	 * .
	 */
	public void testCanInitialize() {
		IPersistor persistor = createPersistor();

		try {
			boolean canInit = persistor.canInitialize(repository);

			assertTrue("Can initialize", canInit);

		} catch (SQLException e) {
			fail(e.getMessage());
		} catch (CoreException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for
	 * {@link com.industrialtsi.mylyn.core.persistence.IbatisPersistor#initialize(org.eclipse.mylyn.tasks.core.TaskRepository)}
	 * .
	 */
	public void testInitialize() {
		IPersistor persistor = createPersistor();

		try {
			persistor.initialize(repository);

			assertTrue("Initialized but no validation", persistor.validate(repository));

		} catch (SQLException e) {
			fail(e.getMessage());
		} catch (CoreException e) {
			fail(e.getMessage());
		}
	}

}
