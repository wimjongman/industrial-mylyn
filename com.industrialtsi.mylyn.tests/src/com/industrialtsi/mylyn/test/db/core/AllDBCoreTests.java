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

import junit.framework.Test;
import junit.framework.TestSuite;

import com.industrialtsi.mylyn.core.persistence.IbatisPersistorTest;
import com.industrialtsi.mylyn.core.persistence.PersistorsManagerTest;

/**
 * @author Maarten Meijer
 */
public class AllDBCoreTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for com.industrialtsi.mylyn.test.core");
		// $JUnit-BEGIN$
		suite.addTestSuite(IbatisCorePluginTest.class);
		suite.addTestSuite(GenericQueryParamsTest.class);
		suite.addTestSuite(IbatisPersistorTest.class);
		suite.addTestSuite(PersistorsManagerTest.class);
		// $JUnit-END$
		return suite;
	}

}
