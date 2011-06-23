/*******************************************************************************
 * Copyright (c) 2008,2009,2010 Industrial TSI and Maarten Meijer.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  	Maarten Meijer - initial code in other places
 *  	Industrial TSI - API and implementation
 *******************************************************************************/
package com.industrialtsi.mylyn.core.persistence;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.mylyn.commons.net.AuthenticationCredentials;
import org.eclipse.mylyn.commons.net.AuthenticationType;
import org.eclipse.mylyn.tasks.core.TaskRepository;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.industrialtsi.mylyn.core.IndustrialCore;
import com.industrialtsi.mylyn.core.dto.IndustrialAttachment;
import com.industrialtsi.mylyn.core.dto.IndustrialComment;
import com.industrialtsi.mylyn.core.dto.IndustrialQueryParams;
import com.industrialtsi.mylyn.core.dto.IndustrialTask;

/**
 * <code>IbatisPersistor</code> : uses the old Ibatis interface that acts as
 * default connection mechanism.
 *
 * @author Maarten Meijer
 * @author Ahmed Aadel
 *
 * @since 0.8.0
 */
public class IbatisPersistor extends PersistorAdapter {

	private static final String USER_KEY = "user"; //$NON-NLS-1$
	private static final String PASSWORD_KEY = "password"; //$NON-NLS-1$

	@Override
	@SuppressWarnings("unchecked")
	public Map<String, String> fetchAdditional(TaskRepository repository,
			String... keys) throws CoreException, SQLException {
		SqlMapClient tasksMap = IndustrialCore.getDefault()
				.getTaskSqlMapConfig(repository);
		Map<String, String> resultMap = (Map<String, String>) tasksMap
				.queryForObject("Tasks.additionalForKey", keys[0]); //$NON-NLS-1$

		return resultMap;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<IndustrialAttachment> fetchAttachments(
			TaskRepository repository, String... keys) throws SQLException,
			CoreException {
		SqlMapClient tasksMap = IndustrialCore.getDefault()
				.getTaskSqlMapConfig(repository);
		List<IndustrialAttachment> attachments = tasksMap.queryForList(
				"Comments.getAttachmentForKey", keys[0]); //$NON-NLS-1$
		return attachments;

	}

	@Override
	@SuppressWarnings("unchecked")
	public List<IndustrialComment> fetchComments(TaskRepository repository,
			String... keys) throws SQLException, CoreException {
		SqlMapClient tasksMap = IndustrialCore.getDefault()
				.getTaskSqlMapConfig(repository);
		return tasksMap.queryForList("Comments.getForKey", keys[0]); //$NON-NLS-1$

	}

	@Override
	public IndustrialTask fetchTask(TaskRepository repository, String... keys)
			throws CoreException, SQLException {
		IndustrialTask task = new IndustrialTask(repository.getUrl(), keys[0],
				""); //$NON-NLS-1$
		SqlMapClient tasksMap = IndustrialCore.getDefault()
				.getTaskSqlMapConfig(repository);
		tasksMap.queryForObject("Tasks.getForKey", keys[0], task); //$NON-NLS-1$

		return task;

	}

	@Override
	public String persistTask(TaskRepository repository,
			IndustrialTask toPersistTask) throws CoreException, SQLException {
		SqlMapClient tasksMap = IndustrialCore.getDefault()
				.getTaskSqlMapConfig(repository);
		Object result = tasksMap.insert("Tasks.newTask", toPersistTask); //$NON-NLS-1$
		// return toPersistTask.getTaskId();
		if (result instanceof String) {
			return (String) result;
		} else if (result instanceof Integer) {
			return Integer.toString(((Integer) result).intValue());
		}
		return "<missing taskId>"; //$NON-NLS-1$

	}

	@Override
	public void persistComment(TaskRepository repository,
			IndustrialComment resultCollector) throws SQLException,
			CoreException {
		SqlMapClient tasksMap = IndustrialCore.getDefault()
				.getTaskSqlMapConfig(repository);
		tasksMap.insert("Comments.addComment", resultCollector); //$NON-NLS-1$

	}

	@Override
	public void persistAttachment(TaskRepository repository,
			IndustrialAttachment attachment) throws CoreException, SQLException {
		SqlMapClient tasksMap = IndustrialCore.getDefault()
				.getTaskSqlMapConfig(repository);
		tasksMap.insert("Comments.addAttachment", attachment); //$NON-NLS-1$

	}

	@Override
	public void updateTask(TaskRepository repository,
			IndustrialTask toUpdateTask) throws CoreException, SQLException {
		SqlMapClient tasksMap = IndustrialCore.getDefault()
				.getTaskSqlMapConfig(repository);
		tasksMap.update("Tasks.updateForkey", toUpdateTask); //$NON-NLS-1$

	}

	@Override
	public byte[] fetchAttachmentBlob(TaskRepository repository,
			String attachmentId) throws CoreException, SQLException {
		IndustrialAttachment resultCollector = new IndustrialAttachment();
		SqlMapClient tasksMap = IndustrialCore.getDefault()
				.getTaskSqlMapConfig(repository);
		tasksMap
				.queryForObject(
						"Comments.getAttachmentDataForKey", attachmentId, resultCollector); //$NON-NLS-1$
		return resultCollector.getBlob();

	}

	@Override
	@SuppressWarnings("unchecked")
	public List<String> findTasks(TaskRepository repository,
			IndustrialQueryParams criteria) throws SQLException, CoreException {
		SqlMapClient tasksMap = IndustrialCore.getDefault()
				.getTaskSqlMapConfig(repository);
		return tasksMap.queryForList("Tasks.searchForKey", criteria); //$NON-NLS-1$

	}

	@Override
	@SuppressWarnings("unchecked")
	public List<String> getLegalOwners(TaskRepository repository)
			throws CoreException, SQLException {
		SqlMapClient tasksMap = IndustrialCore.getDefault()
				.getTaskSqlMapConfig(repository);
		return tasksMap.queryForList("Repository.legalOwners", null); //$NON-NLS-1$
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<String> getLegalPriorities(TaskRepository repository)
			throws SQLException, CoreException {
		SqlMapClient tasksMap = IndustrialCore.getDefault()
				.getTaskSqlMapConfig(repository);
		return tasksMap.queryForList("Repository.legalPriority", null); //$NON-NLS-1$
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<String> getLegalProducts(TaskRepository repository)
			throws SQLException, CoreException {
		SqlMapClient tasksMap = IndustrialCore.getDefault()
				.getTaskSqlMapConfig(repository);
		return tasksMap.queryForList("Repository.legalProducts", null); //$NON-NLS-1$
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<String> getLegalIssueStatus(TaskRepository repository)
			throws SQLException, CoreException {
		SqlMapClient tasksMap = IndustrialCore.getDefault()
				.getTaskSqlMapConfig(repository);
		return tasksMap.queryForList("Repository.legalIssueStatus", null); //$NON-NLS-1$
	}

	@Override
	public boolean validate(TaskRepository repository) throws SQLException,
			CoreException {
		SqlMapClient tasksMap = IndustrialCore.getDefault()
				.getTaskSqlMapConfig(repository);
		Object result = tasksMap.queryForObject("Repository.validate"); //$NON-NLS-1$
		if (result instanceof Integer) {
			return ((Integer) result).intValue() >= 0;
		} else if (result instanceof Boolean) {
			return ((Boolean) result).booleanValue();
		} else if (result instanceof String) {
			return !("FALSE".equalsIgnoreCase((String) result)); //$NON-NLS-1$
		}
		return false;
	}

	@Override
	public boolean isAuthenticated(TaskRepository repository)
			throws SQLException, CoreException {
		Map<String, String> properties = new HashMap<String, String>();
		AuthenticationCredentials credentials = repository
				.getCredentials(AuthenticationType.REPOSITORY);
		if (null != credentials && null != credentials.getPassword()) {
			properties.put(USER_KEY, credentials.getUserName());
			properties.put(PASSWORD_KEY, credentials.getPassword());
		}

		SqlMapClient tasksMap = IndustrialCore.getDefault()
				.getTaskSqlMapConfig(repository);
		Object result = tasksMap.queryForObject("Repository.authenticate"); //$NON-NLS-1$
		if (result instanceof Integer) {
			return ((Integer) result).intValue() >= 0;
		} else if (result instanceof Boolean) {
			return ((Boolean) result).booleanValue();
		} else if (result instanceof String) {
			return !("FALSE".equalsIgnoreCase((String) result)); //$NON-NLS-1$
		}
		return false;
	}

	@Override
	public boolean canInitialize(TaskRepository repository)
			throws SQLException, CoreException {
		// TODO how to vary this between connectors? move to extension point!
		return false;
	}

	@Override
	public void initialize(TaskRepository repository) throws SQLException,
			CoreException {
		SqlMapClient tasksMap = IndustrialCore.getDefault()
				.getTaskSqlMapConfig(repository);
		Object result = tasksMap.queryForObject("Repository.initialize"); //$NON-NLS-1$
		assert null == result;
	}

	@Override
	public void deleteTask(TaskRepository repository, IndustrialTask task)
			throws SQLException, CoreException {
		SqlMapClient tasksMap = IndustrialCore.getDefault()
				.getTaskSqlMapConfig(repository);
		tasksMap.delete("Tasks.deleteForkey", task.getTaskId()); //$NON-NLS-1$
	}

}
