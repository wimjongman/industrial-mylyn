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
package com.industrialtsi.mylyn.core;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector;
import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.AbstractTaskAttachmentHandler;
import org.eclipse.mylyn.tasks.core.data.AbstractTaskDataHandler;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.eclipse.mylyn.tasks.core.data.TaskDataCollector;
import org.eclipse.mylyn.tasks.core.data.TaskMapper;
import org.eclipse.mylyn.tasks.core.sync.ISynchronizationSession;

import com.industrialtsi.mylyn.core.dto.IndustrialQueryParams;
import com.industrialtsi.mylyn.core.dto.IndustrialTask;
import com.industrialtsi.mylyn.core.internal.CoreLogger;
import com.industrialtsi.mylyn.core.internal.IndustrialAttachmentHandler;
import com.industrialtsi.mylyn.core.internal.IndustrialTaskDataHandler;
import com.industrialtsi.mylyn.core.persistence.IPersistor;
import com.industrialtsi.mylyn.core.persistence.PersistorAdapter;
import com.industrialtsi.mylyn.core.persistence.PersistorsManager;

/**
 * <code>IndustrialRepositoryConnector</code> : connector from Mylyn to mainly
 * SQL databases using Ibatis, or using any other {@link IPersistor} extending
 * {@link PersistorAdapter}.
 * 
 * @author maarten meijer
 * 
 */
public class IndustrialRepositoryConnector extends AbstractRepositoryConnector {

	private static final String EMPTY_STRING = ""; //$NON-NLS-1$

	private final IndustrialTaskDataHandler taskDataHandler;

	private final IndustrialAttachmentHandler attachmentHandler;

	public static final String IBATIS_SEPARATOR = "#"; //$NON-NLS-1$

	@Override
	public boolean canCreateNewTask(TaskRepository repository) {
		String flag = repository
				.getProperty(IndustrialCore.CAN_CREATE_NEW_TASK);
		return flag != null && "true".equalsIgnoreCase(flag); //$NON-NLS-1$
	}

	@Override
	public boolean canCreateTaskFromKey(TaskRepository repository) {
		String flag = repository
				.getProperty(IndustrialCore.CAN_CREATE_TASK_FROM_KEY);
		return flag != null && "true".equalsIgnoreCase(flag); //$NON-NLS-1$
	}

	@Override
	public boolean canQuery(TaskRepository repository) {
		String flag = repository
				.getProperty(IndustrialCore.CAN_QUERY_REPOSITORY);
		return flag != null && "true".equalsIgnoreCase(flag); //$NON-NLS-1$
	}

	// @Override
	public boolean canDeleteTask(TaskRepository repository, ITask task) {
		String flag = repository.getProperty(IndustrialCore.CAN_DELETE_TASKS);
		return flag != null && "true".equalsIgnoreCase(flag); //$NON-NLS-1$
		// TODO add an implement logic to check specific task as well
	}

	@Override
	public boolean canSynchronizeTask(TaskRepository repository, ITask task) {
		String flag = repository
				.getProperty(IndustrialCore.CAN_SYNCHRONIZE_TASKS);
		return flag != null && "true".equalsIgnoreCase(flag); //$NON-NLS-1$
		// TODO add an implement logic to check specific task as well
	}

	@Override
	public String getConnectorKind() {
		return IndustrialCore.CONNECTOR_KIND;
	}

	@Override
	public String getLabel() {
		return IndustrialCore.CONNECTOR_LABEL;
	}

//	@Override
	public IStatus deleteTask(TaskRepository repository, ITask task,
			IProgressMonitor monitor) throws CoreException {
		IStatus status = Status.OK_STATUS;
		try {

			IndustrialTask dto = new IndustrialTask(task.getUrl(), task
					.getTaskId(), task.getSummary());

			PersistorsManager.getManager().getPersistor(repository).deleteTask(
					repository, dto);
		} catch (SQLException e) {
			status = CoreLogger.createStatus(IStatus.ERROR, e.getMessage(), e
					.getCause());
			// CoreLogger.log(status);
		}
		return status;
	}

	@Override
	public String getRepositoryUrlFromTaskUrl(String taskFullUrl) {
		if (taskFullUrl == null) {
			return null;
		}
		int index = taskFullUrl
				.lastIndexOf(IndustrialRepositoryConnector.IBATIS_SEPARATOR);
		return index == -1 ? null : taskFullUrl.substring(0, index);
	}

	@Override
	public TaskData getTaskData(TaskRepository repository, String taskId,
			IProgressMonitor monitor) throws CoreException {
		return taskDataHandler.getTaskData(repository, taskId, monitor);

	}

	@Override
	public AbstractTaskAttachmentHandler getTaskAttachmentHandler() {
		return attachmentHandler;
	}

	@Override
	public AbstractTaskDataHandler getTaskDataHandler() {
		return taskDataHandler;
	}

	public IndustrialRepositoryConnector() {
		if (IndustrialCore.getDefault() != null) {
			IndustrialCore.getDefault().setConnector(this);
			// IPath path =
			// IndustrialCore.getDefault().getRepositoryAttributeCachePath();
			// repositoryConfigurationCacheFile = path.toFile();
		}
		taskDataHandler = new IndustrialTaskDataHandler();
		attachmentHandler = new IndustrialAttachmentHandler();
	}

	@Override
	public String getTaskIdFromTaskUrl(String taskFullUrl) {
		if (taskFullUrl == null) {
			return null;
		}
		int index = taskFullUrl
				.lastIndexOf(IndustrialRepositoryConnector.IBATIS_SEPARATOR);
		return index == -1 ? null : taskFullUrl.substring(index
				+ IndustrialRepositoryConnector.IBATIS_SEPARATOR.length());
	}

	@Override
	public String getTaskUrl(String repositoryUrl, String taskId) {
		return repositoryUrl + IndustrialRepositoryConnector.IBATIS_SEPARATOR
				+ taskId;
	}

	@Override
	public boolean hasTaskChanged(TaskRepository taskRepository, ITask task,
			TaskData taskData) {
		TaskMapper mapper = new TaskMapper(taskData);
		return mapper.hasChanges(task);
	}

	@Override
	public boolean hasRepositoryDueDate(TaskRepository taskRepository,
			ITask task, TaskData taskData) {
		// TODO implement support for repository due date
		return super.hasRepositoryDueDate(taskRepository, task, taskData);
	}

	@Override
	public boolean hasLocalCompletionState(TaskRepository taskRepository,
			ITask task) {
		// TODO implement support for local completion state
		return super.hasLocalCompletionState(taskRepository, task);
	}

	@Override
	public IStatus performQuery(TaskRepository repository,
			IRepositoryQuery query, TaskDataCollector resultCollector,
			ISynchronizationSession event, IProgressMonitor monitor) {
		List<String> tickets = new ArrayList<String>();

		IStatus status = Status.OK_STATUS;

		try {
			String queryUrl = query.getUrl();
			if (queryUrl.startsWith(repository.getUrl()
					+ IndustrialQueryParams.QUERY_URL)) {
				assert queryUrl.startsWith(repository.getUrl()
						+ IndustrialQueryParams.QUERY_URL);

				IndustrialQueryParams criteria = new IndustrialQueryParams(
						queryUrl.substring(queryUrl
								.lastIndexOf(IndustrialQueryParams.QUERY_URL)
								+ IndustrialQueryParams.QUERY_URL.length()));

				tickets = PersistorsManager.getManager().getPersistor(
						repository).findTasks(repository, criteria);
			}

			monitor.beginTask("Performing query...", tickets.size());
			for (String taskId : tickets) {
				monitor.subTask("Retrieving " + taskId);
				monitor.worked(1);

				TaskData data = getTaskData(repository, taskId, monitor);
				resultCollector.accept(data);
			}
		} catch (SQLException e) {
			status = CoreLogger.createStatus(IStatus.ERROR, e.getMessage(), e
					.getCause());
			CoreLogger.log(status);
		} catch (CoreException ce) {
			status = ce.getStatus(); // do not repackage existing status
		} catch (Throwable e) {
			status = CoreLogger.createStatus(IStatus.ERROR,
					"A problem occured on " + repository.getUrl(), e); //$NON-NLS-1$
			CoreLogger.log(status);
		} finally {
			monitor.done();
		}
		return status;
	}

	@Override
	public void updateRepositoryConfiguration(TaskRepository repository,
			IProgressMonitor monitor) throws CoreException {

		List<String> results = new ArrayList<String>();

		refreshRepositoryProperties(repository);

		try {
			// Also update the mapping, that can be remote or if file date >
			// last update
			IndustrialCore.getDefault().forgetTaskSqlMapConfig(repository);

			// FIXME add all remaining search parameters
			if (canQuery(repository)) {
				refreshQueryProperties(repository);
			}

			repository.setConfigurationDate(new Date());

		} catch (SQLException e) {
			IStatus status = CoreLogger.createStatus(IStatus.ERROR, e
					.getMessage(), e.getCause());
			CoreLogger.log(status);
			throw new CoreException(status);
		}

	}

	private void refreshQueryProperties(TaskRepository repository)
			throws SQLException, CoreException {
		List<String> results;
		IndustrialQueryParams options = new IndustrialQueryParams(EMPTY_STRING);

		results = PersistorsManager.getManager().getPersistor(repository)
				.getLegalOwners(repository);
		options.setOwner(results.toArray(new String[0]));

		results = PersistorsManager.getManager().getPersistor(repository)
				.getLegalProducts(repository);
		options.setProduct(results.toArray(new String[0]));

		results = PersistorsManager.getManager().getPersistor(repository)
				.getLegalIssueStatus(repository);
		options.setIssueStatus(results.toArray(new String[0]));

		results = PersistorsManager.getManager().getPersistor(repository)
				.getLegalPriorities(repository);
		options.setPriority(results.toArray(new String[0]));

		repository.setProperty(IndustrialCore.REPOSITORY_ATTRIBUTES, options
				.asUrl());
	}

	private void refreshRepositoryProperties(TaskRepository repository) {
		Map<String, String> existing = repository.getProperties();
		for (Entry<String, String> e : existing.entrySet()) {
			if (e.getKey().startsWith("can-") || e.getKey().startsWith("task.common")) {//$NON-NLS-1$ //$NON-NLS-2$
				repository.removeProperty(e.getKey());
			}
		}
		repository.removeProperty(IndustrialCore.REPOSITORY_ATTRIBUTES);

		Map<String, String> refreshed = PersistorsManager.getManager()
				.getPersistorProperties(repository);
		if(refreshed != null) // Old style
			for (Entry<String, String> e : refreshed.entrySet()) {
				repository.setProperty(e.getKey(), e.getValue());
			}
	}

	@Override
	public void updateTaskFromTaskData(TaskRepository taskRepository,
			ITask task, TaskData taskData) {
		TaskMapper mapper = new TaskMapper(taskData);
		mapper.applyTo(task);
	}

	public void stop() {
		IndustrialCore.getDefault().forgetTaskSqlMapConfigs();

	}

}
