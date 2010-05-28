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

import java.util.Date;

import junit.framework.TestCase;

import com.industrialtsi.mylyn.core.dto.IndustrialQueryParams;

/**
 * @author Maarten Meijer
 */
public class GenericQueryParamsTest extends TestCase {

	private static final String PRIORITY_P1_P2_P3 = "priority=P1|P2|P3";

	private static final String PRODUCT_IBATIS_MYLYN = "product=IBATIS|MYLYN";

	private static final String OWNER_WIM_MAARTEN = "owner=WIM|MAARTEN";

	private static final String SUMMARY_LONG = "summary=special";
	
	private static final Date NOW = new Date();
	
	private static final String CREATED_TODAY = "creationBefore=" + Long.toString(NOW.getTime());
	
	
	
	

	public void testAsUrlOwner() {
		IndustrialQueryParams test = new IndustrialQueryParams(OWNER_WIM_MAARTEN);

		assertEquals("owner", "WIM", test.getOwner()[0]);
		assertEquals("owner", "MAARTEN", test.getOwner()[1]);

		assertEquals("reverse", OWNER_WIM_MAARTEN, test.asUrl());

	}

	public void testAsUrlProduct() {
		IndustrialQueryParams test = new IndustrialQueryParams(PRODUCT_IBATIS_MYLYN);

		assertEquals("product", "IBATIS", test.getProduct()[0]);
		assertEquals("product", "MYLYN", test.getProduct()[1]);

		assertEquals("reverse", PRODUCT_IBATIS_MYLYN, test.asUrl());

	}

	public void testAsUrlPriority() {

		IndustrialQueryParams test = new IndustrialQueryParams(PRIORITY_P1_P2_P3);

		assertEquals("priority", "P1", test.getPriority()[0]);
		assertEquals("priority", "P2", test.getPriority()[1]);
		assertEquals("priority", "P3", test.getPriority()[2]);

		assertEquals("reverse", PRIORITY_P1_P2_P3, test.asUrl());

	}

	public void testAsUrlSummary() {

		IndustrialQueryParams test = new IndustrialQueryParams(SUMMARY_LONG);

		assertEquals("summary", "special", test.getSummary());

		assertEquals("reverse", SUMMARY_LONG, test.asUrl());

	}

	
	public void testAsUrlOwnerPriority() {

		IndustrialQueryParams test = new IndustrialQueryParams(OWNER_WIM_MAARTEN + "&" + PRIORITY_P1_P2_P3);

		assertEquals("owner", "WIM", test.getOwner()[0]);
		assertEquals("owner", "MAARTEN", test.getOwner()[1]);

		assertEquals("priority", "P1", test.getPriority()[0]);
		assertEquals("priority", "P2", test.getPriority()[1]);
		assertEquals("priority", "P3", test.getPriority()[2]);

		assertEquals("reverse", OWNER_WIM_MAARTEN + "&" + PRIORITY_P1_P2_P3, test.asUrl());

	}

	public void testAsUrlOwnerProductPriority() {

		IndustrialQueryParams test = new IndustrialQueryParams(OWNER_WIM_MAARTEN + "&" + PRODUCT_IBATIS_MYLYN + "&"
				+ PRIORITY_P1_P2_P3);

		assertEquals("owner", "WIM", test.getOwner()[0]);
		assertEquals("owner", "MAARTEN", test.getOwner()[1]);

		assertEquals("product", "IBATIS", test.getProduct()[0]);
		assertEquals("product", "MYLYN", test.getProduct()[1]);

		assertEquals("priority", "P1", test.getPriority()[0]);
		assertEquals("priority", "P2", test.getPriority()[1]);
		assertEquals("priority", "P3", test.getPriority()[2]);

		assertEquals("reverse", OWNER_WIM_MAARTEN + "&" + PRODUCT_IBATIS_MYLYN + "&" + PRIORITY_P1_P2_P3, test.asUrl());

	}
	
	public void testAsUrlCreationDate() {

		IndustrialQueryParams test = new IndustrialQueryParams(CREATED_TODAY);

		assertEquals("creationDate", NOW, test.getCreationDateBefore());

		assertEquals("reverse", CREATED_TODAY, test.asUrl());
	}
}
