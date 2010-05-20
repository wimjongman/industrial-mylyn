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

package com.industrialtsi.mylyn.core.persistence;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.mylyn.tasks.core.TaskRepository;

import com.industrialtsi.mylyn.core.dto.IndustrialAttachment;
import com.industrialtsi.mylyn.core.dto.IndustrialComment;
import com.industrialtsi.mylyn.core.dto.IndustrialQueryParams;
import com.industrialtsi.mylyn.core.dto.IndustrialTask;
import com.industrialtsi.mylyn.core.internal.CoreLogger;

/**
 * <code>PersistorAdapter</code> A base implementation that just logs all method
 * calls as INFO messages
 * 
 * @author maarten
 * 
 * @since 0.8.2
 * @extend this class instead of implementing {@link IPersistor}.
 * 
 */
public abstract class PersistorAdapter implements IPersistor {

	private static final String NULLSTRING = "<null>"; //$NON-NLS-1$

	public boolean canInitialize(TaskRepository repository)
			throws SQLException, CoreException {
		CoreLogger.logInfo("CALLED: canInitialize(repository)"); //$NON-NLS-1$
		return false;
	}

	public Map<String, String> fetchAdditional(TaskRepository repository,
			String... taskId) throws SQLException, CoreException {
		CoreLogger.logInfo("CALLED: fetchAdditional(repository, taskId)"); //$NON-NLS-1$
		return Collections.emptyMap();
	}

	public byte[] fetchAttachmentBlob(TaskRepository repository,
			String attachmentId) throws SQLException, CoreException {
		CoreLogger
				.logInfo("CALLED: fetchAttachmentBlob(repository, attachmentId)"); //$NON-NLS-1$
		return new byte[0];
	}

	public List<IndustrialAttachment> fetchAttachments(
			TaskRepository repository, String... taskId) throws SQLException,
			CoreException {
		CoreLogger.logInfo("CALLED: fetchAttachments(repository, taskId)"); //$NON-NLS-1$
		return Collections.emptyList();
	}

	public List<IndustrialComment> fetchComments(TaskRepository repository,
			String... taskId) throws SQLException, CoreException {
		CoreLogger.logInfo("CALLED: fetchComments(repository, taskId)"); //$NON-NLS-1$
		return Collections.emptyList();
	}

	public IndustrialTask fetchTask(TaskRepository repository, String... taskId)
			throws SQLException, CoreException {
		CoreLogger.logInfo("CALLED: fetchTask(repository, taskId)"); //$NON-NLS-1$
		return null;
	}

	public List<String> findTasks(TaskRepository repository,
			IndustrialQueryParams criteria) throws SQLException, CoreException {
		CoreLogger.logInfo("CALLED: findTasks(repository, criteria)"); //$NON-NLS-1$
		return Collections.emptyList();
	}

	public List<String> getLegalIssueStatus(TaskRepository repository)
			throws SQLException, CoreException {
		CoreLogger.logInfo("CALLED: getLegalIssueStatus(repository)"); //$NON-NLS-1$
		return Collections.emptyList();
	}

	public List<String> getLegalOwners(TaskRepository repository)
			throws SQLException, CoreException {
		CoreLogger.logInfo("CALLED: getLegalOwners(repository)"); //$NON-NLS-1$
		return Collections.emptyList();
	}

	public List<String> getLegalPriorities(TaskRepository repository)
			throws SQLException, CoreException {
		CoreLogger.logInfo("CALLED: getLegalPriorities(repository)"); //$NON-NLS-1$
		return Collections.emptyList();
	}

	public List<String> getLegalProducts(TaskRepository repository)
			throws SQLException, CoreException {
		CoreLogger.logInfo("CALLED: getLegalProducts(repository)"); //$NON-NLS-1$
		return Collections.emptyList();
	}

	public void initialize(TaskRepository repository) throws SQLException,
			CoreException {
		CoreLogger.logInfo("CALLED: initialize(repository)"); //$NON-NLS-1$
	}

	public void persistAttachment(TaskRepository repository,
			IndustrialAttachment attachment) throws SQLException, CoreException {
		CoreLogger.logInfo("CALLED: persistAttachment(repository, attachment)"); //$NON-NLS-1$
	}

	public void persistComment(TaskRepository repository,
			IndustrialComment comment) throws SQLException, CoreException {
		CoreLogger.logInfo("CALLED: persistComment(repository, comment)"); //$NON-NLS-1$
	}

	public String persistTask(TaskRepository repository,
			IndustrialTask taskToPersist) throws SQLException, CoreException {
		CoreLogger.logInfo("CALLED: persistTask(repository, taskToPersist)"); //$NON-NLS-1$
		return NULLSTRING;
	}

	public void updateTask(TaskRepository repository, IndustrialTask task)
			throws SQLException, CoreException {
		CoreLogger.logInfo("CALLED: updateTask(repository, task)"); //$NON-NLS-1$
	}

	public boolean validate(TaskRepository repository) throws SQLException,
			CoreException {
		CoreLogger.logInfo("CALLED: validate(repository)"); //$NON-NLS-1$
		return false;
	}

	public boolean isAuthenticated(TaskRepository repository) throws SQLException,
			CoreException {
		CoreLogger.logInfo("CALLED: isAuthenticated(repository)"); //$NON-NLS-1$
		// default is yes, to allow transition
		return true;
	}

	public void deleteAttachment(TaskRepository repository,
			IndustrialAttachment attachment) throws SQLException, CoreException {
		CoreLogger.logInfo("CALLED: deleteAttachment(repository, attachment)"); //$NON-NLS-1$
	}

	public void deleteComment(TaskRepository repository,
			IndustrialComment comment) throws SQLException, CoreException {
		CoreLogger.logInfo("CALLED: deleteComment(repository, comment)"); //$NON-NLS-1$
	}

	public void deleteTask(TaskRepository repository, IndustrialTask task)
			throws SQLException, CoreException {
		CoreLogger.logInfo("CALLED: deleteTask(repository, task)"); //$NON-NLS-1$
	}

	public void updateAttachment(TaskRepository repository,
			IndustrialAttachment attachment) throws SQLException, CoreException {
		CoreLogger.logInfo("CALLED: updateAttachment(repository, attachment)"); //$NON-NLS-1$
	}

	public void updateComment(TaskRepository repository,
			IndustrialComment comment) throws SQLException, CoreException {
		CoreLogger.logInfo("CALLED: updateComment(repository, comment)"); //$NON-NLS-1$
	}

}
