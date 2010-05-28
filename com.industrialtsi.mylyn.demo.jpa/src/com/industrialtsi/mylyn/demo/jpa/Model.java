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
package com.industrialtsi.mylyn.demo.jpa;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.eclipse.persistence.jpa.osgi.PersistenceProvider;

import com.industrialtsi.mylyn.demo.jpa.setup.DatabaseInit;

/**
 * @author Ahmed Aadel
 * @since 0.8.0
 * 
 */
public class Model {

	public static final String PU_NAME = "jpaDemo";

	private EntityManagerFactory factory;

	public Model() {
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(PersistenceUnitProperties.CLASSLOADER, this.getClass().getClassLoader());
		PersistenceProvider provider = new org.eclipse.persistence.jpa.osgi.PersistenceProvider(this.getClass()
				.getName());

		factory = provider.createEntityManagerFactory(PU_NAME, properties);

	}

	public EntityManager getEntityManager() {
		return factory.createEntityManager();
	}

	/**
	 * Drop all tables, create and populate them anew
	 */
	public void init() {
		new DatabaseInit().init();
	}

	public void dispose() {
		if (factory.isOpen())
			factory.close();

	}

}
