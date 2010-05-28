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
package com.industrialtsi.mylyn.demo.jpa.setup;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory; 

import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.eclipse.persistence.jpa.osgi.PersistenceProvider;

import com.industrialtsi.mylyn.core.dto.IndustrialTask;
import com.industrialtsi.mylyn.demo.jpa.Model;
import com.industrialtsi.mylyn.demo.jpa.annotated.AttachmentEntity;
import com.industrialtsi.mylyn.demo.jpa.annotated.CommentEntity;
import com.industrialtsi.mylyn.demo.jpa.annotated.PriorityEntity;
import com.industrialtsi.mylyn.demo.jpa.annotated.ProductEntity;
import com.industrialtsi.mylyn.demo.jpa.annotated.StatusEntity;
import com.industrialtsi.mylyn.demo.jpa.annotated.TaskEntity;

/**
 * @author Ahmed Aadel
 * @since 0.8.0
 * 
 */
public class DatabaseInit {

	private EntityManager entityManager;

	private EntityManagerFactory factory;

	public void init() {
		// drop and create all tables anew for demo sake.
		dropAndCreate();
		populate();
	}

	/**
	 * Create an <code>EntityManagerFactory</code> for our persistence unit.
	 * Drop all tables if any and create them anew
	 */
	public void dropAndCreate() {
		Map<String, Object> properties = new HashMap<String, Object>();
		// drop existing tables if any
		properties.put("eclipselink.ddl-generation", "drop-and-create-tables");
		properties.put("eclipselink.ddl-generation.ouOtput-mode", "database");
		properties.put(PersistenceUnitProperties.CLASSLOADER, this.getClass().getClassLoader());
		PersistenceProvider provider = new org.eclipse.persistence.jpa.osgi.PersistenceProvider();
		factory = provider.createEntityManagerFactory(Model.PU_NAME, properties);
		entityManager = factory.createEntityManager();

	}

	private void populate() {
		Calendar calendar = Calendar.getInstance();

		// some tasks
		TaskEntity taskEntity = new TaskEntity();
		IndustrialTask it = new IndustrialTask("", "", "Some test issue");
		it.setNotes("some notes to describe this issue...");
		it.setDueDate(new Date());
		it.setActive(true);
		it.setIssueStatus("NEW");
		it.setPriority("P1");
		it.setOwner("Wim");
		it.setProduct("Product 4");
		taskEntity.setIndustrialTask(it);

		TaskEntity taskEntity2 = new TaskEntity();
		IndustrialTask it2 = new IndustrialTask("", "", "Another test issue");
		it2.setNotes("more details about this issue...");
		it2.setActive(true);
		calendar.add(Calendar.DAY_OF_MONTH, 9);
		it2.setDueDate(calendar.getTime());
		it2.setIssueStatus("OPEN");
		it2.setPriority("P3");
		it2.setOwner("Maarten");
		it2.setProduct("Product 2");
		taskEntity2.setIndustrialTask(it2);

		TaskEntity taskEntity3 = new TaskEntity();
		IndustrialTask it3 = new IndustrialTask("", "", "Yet another test issue");
		it3.setNotes("a more wordy description should be here...");
		calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -3);
		it3.setDueDate(calendar.getTime());
		it3.setActive(true);
		it3.setIssueStatus("CLOSED");
		it3.setPriority("P1");
		it3.setOwner("Ahmed");
		it3.setProduct("Product 3");
		taskEntity3.setIndustrialTask(it3);

		// some products
		ProductEntity productEntity = new ProductEntity();
		productEntity.setProduct("Product 1");
		ProductEntity productEntity2 = new ProductEntity();
		productEntity2.setProduct("Product 2");
		ProductEntity productEntity3 = new ProductEntity();
		productEntity3.setProduct("Product 3");
		ProductEntity productEntity4 = new ProductEntity();
		productEntity4.setProduct("Product 4");
		ProductEntity productEntity5 = new ProductEntity();
		productEntity5.setProduct("Product 5");
		ProductEntity productEntity6 = new ProductEntity();
		productEntity6.setProduct("UKNOWN");

		// some priorities
		PriorityEntity priority = new PriorityEntity();
		priority.setPriority("P1");
		PriorityEntity priority2 = new PriorityEntity();
		priority2.setPriority("P2");
		PriorityEntity priority3 = new PriorityEntity();
		priority3.setPriority("P3");
		PriorityEntity priority4 = new PriorityEntity();
		priority4.setPriority("P4");
		PriorityEntity priority5 = new PriorityEntity();
		priority5.setPriority("P5");

		// some status

		StatusEntity status = new StatusEntity();
		status.setStatus("NEW");
		status.setSort(1);
		StatusEntity status2 = new StatusEntity();
		status2.setStatus("OPEN");
		status.setSort(2);
		StatusEntity status3 = new StatusEntity();
		status.setSort(3);
		status3.setStatus("CLOSED");

		// persist them all
		entityManager.getTransaction().begin();
		entityManager.persist(taskEntity);
		entityManager.persist(taskEntity2);
		entityManager.persist(taskEntity3);

		entityManager.persist(productEntity);
		entityManager.persist(productEntity2);
		entityManager.persist(productEntity3);
		entityManager.persist(productEntity4);
		entityManager.persist(productEntity5);
		entityManager.persist(productEntity6);

		entityManager.persist(priority);
		entityManager.persist(priority2);
		entityManager.persist(priority3);
		entityManager.persist(priority4);
		entityManager.persist(priority5);

		entityManager.persist(status);
		entityManager.persist(status2);
		entityManager.persist(status3);

		entityManager.persist(new AttachmentEntity());
		entityManager.persist(new CommentEntity());
		entityManager.getTransaction().commit();
		// release
		entityManager.clear();
		entityManager.close();
		factory.close();

	}

}
