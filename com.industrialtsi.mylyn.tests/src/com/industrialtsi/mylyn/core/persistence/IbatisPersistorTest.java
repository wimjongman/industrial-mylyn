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

import java.sql.SQLException;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.industrialtsi.mylyn.core.dto.IndustrialAttachment;
import com.industrialtsi.mylyn.core.dto.IndustrialComment;
import com.industrialtsi.mylyn.core.dto.IndustrialQueryParams;
import com.industrialtsi.mylyn.core.dto.IndustrialTask;
import com.industrialtsi.mylyn.test.db.AbstractIbatisTestCase;

/**
 * <code>IbatisPersistorTest</code> : TODO describe.
 * 
 * @author Maarten
 * 
 */
public class IbatisPersistorTest extends AbstractIbatisTestCase {

	@Before
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		initTestRepository();
		assertNotNull("repository not inited", repository);
		setDefaultCredentials(repository);
	}

	@After
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	private IPersistor createPersistor() {
		// TODO Auto-generated method stub
		return new IbatisPersistor();
	}

	/**
	 * Test method for
	 * {@link com.industrialtsi.mylyn.core.persistence.IbatisPersistor#fetchAdditional(org.eclipse.mylyn.tasks.core.TaskRepository, java.lang.String[])}
	 * .
	 */
	@Test
	public void testFetchAdditional() {
		// FIXME fix beyond here
		// fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.industrialtsi.mylyn.core.persistence.IbatisPersistor#fetchAttachments(org.eclipse.mylyn.tasks.core.TaskRepository, java.lang.String[])}
	 * .
	 * 
	 * @throws CoreException
	 * @throws SQLException
	 */
	@Test
	public void testFetchAttachments() throws SQLException, CoreException {

		IPersistor persistor = createPersistor();

		try {
			List<IndustrialAttachment> found = persistor.fetchAttachments(
					repository, "1");

			assertNotNull("fetchAttachments returns null", found);
			assertTrue("fetchAttachments returns empty", !found.isEmpty());

			List<IndustrialAttachment> empty = persistor.fetchAttachments(
					repository, "2");

			assertNotNull("fetchAttachments returns null", empty);
			assertTrue("fetchAttachments returns not empty", empty.isEmpty());

			// TODO add more tests
		} catch (SQLException e) {
			fail(e.getMessage());
		} catch (CoreException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for
	 * {@link com.industrialtsi.mylyn.core.persistence.IbatisPersistor#fetchComments(org.eclipse.mylyn.tasks.core.TaskRepository, java.lang.String[])}
	 * .
	 */
	@Test
	public void testFetchComments() {
		// FIXME fix beyond here
		// fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.industrialtsi.mylyn.core.persistence.IbatisPersistor#fetchTask(org.eclipse.mylyn.tasks.core.TaskRepository, java.lang.String[])}
	 * .
	 */
	@Test
	public void testFetchTask() {
		IndustrialQueryParams criteria = new IndustrialQueryParams("priority=P3&status=NEW");

		IPersistor persistor = createPersistor();

		try {
			List<String> found = persistor.findTasks(repository, criteria);

			assertNotNull("findTasks returns null", found);
			assertTrue("findTasks returns empty", !found.isEmpty());

			for (String key : found) {
				IndustrialTask task = persistor.fetchTask(repository, key);

				assertNotNull("fetchTask returns null", task);
				assertEquals("Fetched task Priority wrong", "P3", task.getPriority());
				assertEquals("Fetched task Status wrong", "NEW", task.getIssueStatus());
			}

			// TODO add more tests
		} catch (SQLException e) {
			fail(e.getMessage());
		} catch (CoreException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for
	 * {@link com.industrialtsi.mylyn.core.persistence.IbatisPersistor#persistTask(org.eclipse.mylyn.tasks.core.TaskRepository, com.industrialtsi.mylyn.core.dto.IndustrialTask)}
	 * .
	 */
	@Test
	public void testPersistTask() {

		IPersistor persistor = createPersistor();
		IndustrialQueryParams all = new IndustrialQueryParams("");

		try {

			List<String> found = persistor.findTasks(repository, all);
			int before = found.size();

			IndustrialTask task = new IndustrialTask(repository.getUrl(), null, "Unit Test Generated");

			task.setPriority("P4");
			task.setIssueStatus("NEW");
			task.setOwner("junit");
			task.setNotes("Notes generated by code");
			task.setReporter("junit");
			task.setProduct("UNKNOWN");

			String id = persistor.persistTask(repository, task);

			assertNotNull("persistTask returned null", id);

			found = persistor.findTasks(repository, all);
			int after = found.size();

			assertEquals("Size not increased by one", before + 1, after);

		} catch (SQLException e) {
			fail(e.getMessage());
		} catch (CoreException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for
	 * {@link com.industrialtsi.mylyn.core.persistence.IbatisPersistor#persistComment(org.eclipse.mylyn.tasks.core.TaskRepository, com.industrialtsi.mylyn.core.dto.IndustrialComment)}
	 * .
	 */
	@Test
	public void testPersistComment() {
		IPersistor persistor = createPersistor();
		IndustrialQueryParams all = new IndustrialQueryParams("");

		try {

			List<String> found = persistor.findTasks(repository, all);
			String last = found.get(found.size() - 1);

			List<IndustrialComment> comments = persistor.fetchComments(repository, last);
			int before = comments.size();

			IndustrialComment comment = new IndustrialComment();

			comment.setAuthor("test");
			comment.setAuthorName("testPersistComment");
			comment.setDescription("Somem new comment");
			comment.setTaskId(last);
			comment.setText("some text");

			persistor.persistComment(repository, comment);

			comments = persistor.fetchComments(repository, last);
			int after = comments.size();

			assertEquals("Size not increased by one", before + 1, after);

		} catch (SQLException e) {
			fail(e.getMessage());
		} catch (CoreException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for
	 * {@link com.industrialtsi.mylyn.core.persistence.IbatisPersistor#persistAttachment(org.eclipse.mylyn.tasks.core.TaskRepository, com.industrialtsi.mylyn.core.dto.IndustrialAttachment)}
	 * .
	 */
	@Test
	public void testPersistAttachment() {
		// FIXME fix beyond here

		// fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.industrialtsi.mylyn.core.persistence.IbatisPersistor#updateTask(org.eclipse.mylyn.tasks.core.TaskRepository, com.industrialtsi.mylyn.core.dto.IndustrialTask)}
	 * .
	 */
	@Test
	public void testUpdateTask() {
		IndustrialQueryParams criteria = new IndustrialQueryParams("priority=P3&status=NEW");

		IPersistor persistor = createPersistor();

		try {
			List<String> found = persistor.findTasks(repository, criteria);

			assertNotNull("findTasks returns null", found);
			assertTrue("findTasks returns empty", !found.isEmpty());

			for (String key : found) {
				IndustrialTask task = persistor.fetchTask(repository, key);
				assertNotNull("fetchTask returns null", task);
				assertEquals("Fetched task Priority wrong", "P3", task.getPriority());
				task.setPriority("P5");
				persistor.updateTask(repository, task);
			}

			List<String> empty = persistor.findTasks(repository, criteria);

			assertNotNull("findTasks returns null", empty);
			assertTrue("findTasks returns non-empty", empty.isEmpty());

			for (String key : found) {
				IndustrialTask task = persistor.fetchTask(repository, key);
				assertNotNull("fetchTask returns null", task);
				assertEquals("Fetched task Priority wrong", "P5", task.getPriority());
				task.setPriority("P3");
				persistor.updateTask(repository, task);
			}

			empty = persistor.findTasks(repository, criteria);

			assertNotNull("findTasks returns null", empty);
			assertTrue("findTasks returns empty", !empty.isEmpty());

			// TODO add more tests
		} catch (SQLException e) {
			fail(e.getMessage());
		} catch (CoreException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for
	 * {@link com.industrialtsi.mylyn.core.persistence.IbatisPersistor#fetchAttachmentBlob(org.eclipse.mylyn.tasks.core.TaskRepository, java.lang.String)}
	 * .
	 */
	@Test
	public void testFetchAttachmentBlob() {


		IPersistor persistor = createPersistor();

		try {
			List<IndustrialAttachment> found = persistor.fetchAttachments(
					repository, "1");

			assertNotNull("fetchAttachments returns null", found);
			assertTrue("fetchAttachments returns empty", !found.isEmpty());

			for (IndustrialAttachment att : found) {
				byte[] blob = persistor.fetchAttachmentBlob(repository, att.getId());
				assertTrue("blob is not empty", null == blob);
			}

			// TODO add more tests
		} catch (SQLException e) {
			fail(e.getMessage());
		} catch (CoreException e) {
			fail(e.getMessage());
		}

	}

	/**
	 * Test method for
	 * {@link com.industrialtsi.mylyn.core.persistence.IbatisPersistor#findTasks(org.eclipse.mylyn.tasks.core.TaskRepository, com.industrialtsi.mylyn.core.dto.IndustrialQueryParams)}
	 * .
	 */
	@Test
	public void testFindTasks() {
		IndustrialQueryParams criteria = new IndustrialQueryParams("priority=P3&status=NEW");

		IPersistor persistor = createPersistor();

		try {
			List<String> found = persistor.findTasks(repository, criteria);

			assertNotNull("findTasks returns null", found);
			assertTrue("findTasks returns empty", !found.isEmpty());

			// TODO add more tests
		} catch (SQLException e) {
			fail(e.getMessage());
		} catch (CoreException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for
	 * {@link com.industrialtsi.mylyn.core.persistence.IbatisPersistor#getLegalOwners(org.eclipse.mylyn.tasks.core.TaskRepository)}
	 * .
	 */
	@Test
	public void testGetLegalOwners() {
		IPersistor persistor = createPersistor();

		try {
			List<String> found = persistor.getLegalOwners(repository);

			assertNotNull("getLegalOwners returns null", found);
			assertTrue("getLegalOwners returns empty", !found.isEmpty());

			// TODO add more tests
		} catch (SQLException e) {
			fail(e.getMessage());
		} catch (CoreException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for
	 * {@link com.industrialtsi.mylyn.core.persistence.IbatisPersistor#getLegalPriorities(org.eclipse.mylyn.tasks.core.TaskRepository)}
	 * .
	 */
	@Test
	public void testGetLegalPriorities() {
		IPersistor persistor = createPersistor();

		try {
			List<String> found = persistor.getLegalPriorities(repository);

			assertNotNull("getLegalPriorities returns null", found);
			assertTrue("getLegalPriorities returns empty", !found.isEmpty());

			assertEquals("getLegalPriorities is wrong size", 5, found.size());

			// TODO add more tests
		} catch (SQLException e) {
			fail(e.getMessage());
		} catch (CoreException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for
	 * {@link com.industrialtsi.mylyn.core.persistence.IbatisPersistor#getLegalProducts(org.eclipse.mylyn.tasks.core.TaskRepository)}
	 * .
	 */
	@Test
	public void testGetLegalProducts() {
		IPersistor persistor = createPersistor();

		try {
			List<String> found = persistor.getLegalProducts(repository);

			assertNotNull("getLegalProducts returns null", found);
			assertTrue("getLegalProducts returns empty", !found.isEmpty());

			assertEquals("getLegalProducts is wrong size", 6, found.size());

			// TODO add more tests
		} catch (SQLException e) {
			fail(e.getMessage());
		} catch (CoreException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for
	 * {@link com.industrialtsi.mylyn.core.persistence.IbatisPersistor#getLegalIssueStatus(org.eclipse.mylyn.tasks.core.TaskRepository)}
	 * .
	 */
	@Test
	public void testGetLegalIssueStatus() {
		IPersistor persistor = createPersistor();

		try {
			List<String> found = persistor.getLegalIssueStatus(repository);

			assertNotNull("getLegalIssueStatus returns null", found);
			assertTrue("getLegalIssueStatus returns empty", !found.isEmpty());

			assertEquals("getLegalIssueStatus is wrong size", 3, found.size());

			// TODO add more tests
		} catch (SQLException e) {
			fail(e.getMessage());
		} catch (CoreException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for
	 * {@link com.industrialtsi.mylyn.core.persistence.IbatisPersistor#validate(org.eclipse.mylyn.tasks.core.TaskRepository)}
	 * .
	 */
	@Test
	public void testValidate() {
		IPersistor persistor = createPersistor();

		try {
			boolean validated = persistor.validate(repository);

			assertTrue("Not validated", validated);

		} catch (SQLException e) {
			fail(e.getMessage());
		} catch (CoreException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for
	 * {@link com.industrialtsi.mylyn.core.persistence.IbatisPersistor#canInitialize(org.eclipse.mylyn.tasks.core.TaskRepository)}
	 * .
	 */
	@Test
	public void testCanInitialize() {
		IPersistor persistor = createPersistor();

		try {
			boolean canInit = persistor.canInitialize(repository);

			assertTrue("Cannot initialize", !canInit);

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
	@Test
	public void testInitialize() {
		// FIXME fix beyond here
		// fail("Not yet implemented");
	}

}
