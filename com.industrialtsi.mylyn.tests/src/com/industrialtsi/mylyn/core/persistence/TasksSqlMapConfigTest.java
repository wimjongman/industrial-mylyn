package com.industrialtsi.mylyn.core.persistence;

import static org.junit.Assert.fail;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.ibatis.sqlmap.client.SqlMapClient;

public class TasksSqlMapConfigTest {

	private TaskRepository dummy;
	private TaskRepository failer;

	@Before
	public void setUp() throws Exception {
		dummy = new TaskRepository("industrial", "jdbc:derby:/Users/maarten/tasksDB;create=true");
		dummy.setRepositoryLabel("MOCK");
		dummy.setProperty(TasksSqlMapConfig.SQLMAP_INCLUDED, "derby_local_demo");
		System.out.println("TasksSqlMapConfigTest.setUp()"
				+ dummy.getRepositoryUrl());

		failer = new TaskRepository("industrial",
				"jdbc:derby:/Users/maarten/tasksDB;create=true");
		failer.setRepositoryLabel("FAIL");

	}

	@After
	public void tearDown() throws Exception {
		TasksSqlMapConfig.forgetMaps();
	}

	@Test
	public final void getSqlMapInstance() throws Exception {
		SqlMapClient mock = TasksSqlMapConfig.getSqlMapInstance(dummy);
		Assert.assertNotNull(mock);
	}

	@Test
	public final void getSqlMapInstanceFails() throws Exception {
		SqlMapClient mock;
		try {
			mock = TasksSqlMapConfig.getSqlMapInstance(failer);
			fail("Should fail");
		} catch (CoreException e) {
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public final void forgetMap() throws Exception {
		SqlMapClient first = TasksSqlMapConfig.getSqlMapInstance(dummy);
		Assert.assertNotNull(first);
		TasksSqlMapConfig.forgetMap(dummy);
		SqlMapClient second = TasksSqlMapConfig.getSqlMapInstance(dummy);
		Assert.assertNotNull(second);
		Assert.assertNotEquals(first, second);
	}

	@Test
	public final void forgetMaps() throws Exception {
		SqlMapClient first = TasksSqlMapConfig.getSqlMapInstance(dummy);
		Assert.assertNotNull(first);
		TasksSqlMapConfig.forgetMaps();
		SqlMapClient second = TasksSqlMapConfig.getSqlMapInstance(dummy);
		Assert.assertNotNull(second);
		Assert.assertNotEquals(first, second);
	}

}
