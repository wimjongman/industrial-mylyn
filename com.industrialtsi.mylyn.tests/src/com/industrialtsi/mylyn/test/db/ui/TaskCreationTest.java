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
package com.industrialtsi.mylyn.test.db.ui;

import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.mylyn.internal.tasks.core.AbstractTask;
import org.eclipse.mylyn.internal.tasks.core.DefaultTaskMapping;
import org.eclipse.mylyn.internal.tasks.ui.TasksUiPlugin;
import org.eclipse.mylyn.internal.tasks.ui.util.TasksUiInternal;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.core.ITask.SynchronizationState;
import org.eclipse.mylyn.tasks.core.data.ITaskDataWorkingCopy;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.eclipse.mylyn.tasks.core.data.TaskDataModel;
import org.eclipse.mylyn.tasks.core.sync.SubmitJob;
import org.eclipse.mylyn.tasks.ui.TasksUiUtil;

import com.industrialtsi.mylyn.core.IndustrialCore;
import com.industrialtsi.mylyn.core.internal.IndustrialAttributeMapper;
import com.industrialtsi.mylyn.test.db.IbatisTest;

/**
 * @author maarten
 *
 */
public class TaskCreationTest extends IbatisTest {

	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		initTestRepository();
	}

	@SuppressWarnings("restriction")
	public void testProgrammaticCreation() {

		try {

			assertNotNull("repository null", repository);

			ITask newTask = TasksUiUtil.createOutgoingNewTask(IndustrialCore.CONNECTOR_KIND, IBATIS_REPOSITORY_URL);
			assertNotNull("Task creation failed", newTask);
			assertNotNull("Task creation failed - PERSISTENCE_DELEGATOR_ID", newTask.getTaskId());

			IndustrialAttributeMapper mapper = new IndustrialAttributeMapper(repository);
			assertNotNull("Mapper creation failed", mapper);

			TaskData data = new TaskData(mapper, IndustrialCore.CONNECTOR_KIND, IBATIS_REPOSITORY_URL, newTask
					.getTaskId());
			assertNotNull("TaskData creation failed", mapper);

			boolean success = connector.getTaskDataHandler().initializeTaskData(repository, data,
					new DefaultTaskMapping(), new NullProgressMonitor());
			assertTrue("TaskData not initialized", success);


			Map<String, TaskAttribute> dataAttributes = data.getRoot().getAttributes();
			assertNotNull("TaskData no attributes", dataAttributes);
			System.out.println(dataAttributes.toString());

			Map<String, String> taskAttributes = newTask.getAttributes();
			assertNotNull("TaskData no attributes", taskAttributes);
			System.out.println(taskAttributes.toString());

			TasksUiUtil.openTask(newTask);

			connector.getTaskDataHandler().postTaskData(repository, data, null, new NullProgressMonitor());

			TaskDataModel model = createModel(newTask);

			submit(model);
		} catch (CoreException e) {
			fail(e.getMessage());
		}
	}

	protected TaskDataModel createModel(ITask task) throws CoreException {
		ITaskDataWorkingCopy taskDataState = getWorkingCopy(task);
		return new TaskDataModel(repository, task, taskDataState);
	}

	@SuppressWarnings("restriction")
	protected ITaskDataWorkingCopy getWorkingCopy(ITask task) throws CoreException {
		return TasksUiPlugin.getTaskDataManager().getWorkingCopy(task);
	}

	@SuppressWarnings("restriction")
	protected void submit(TaskDataModel model) {
		SubmitJob submitJob = TasksUiInternal.getJobFactory().createSubmitTaskJob(connector, model.getTaskRepository(),
				model.getTask(), model.getTaskData(), model.getChangedOldAttributes());
		submitJob.schedule();
		try {
			submitJob.join();
		} catch (InterruptedException e) {
			fail(e.getMessage());
		}
	}

	@SuppressWarnings( { "restriction", "deprecation" })
	protected void submit(ITask task, TaskData taskData, Set<TaskAttribute> changedAttributes) throws CoreException {
		connector.getTaskDataHandler().postTaskData(repository, taskData, changedAttributes, new NullProgressMonitor());
		((AbstractTask) task).setSubmitting(true);
	}

	@SuppressWarnings("restriction")
	protected void synchAndAssertState(Set<AbstractTask> tasks, SynchronizationState state) {
		for (AbstractTask task : tasks) {
			TasksUiInternal.synchronizeTask(connector, task, true, null);
			assertEquals(task.getSynchronizationState(), state);
		}
	}

}
