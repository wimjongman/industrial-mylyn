/*******************************************************************************
 * Copyright (c) 2008 Industrial TSI and Maarten Meijer.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * 		Industrial TSI - initial API and implementation
 *******************************************************************************/

package com.industrialtsi.mylyn.core.persistence;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.mylyn.tasks.core.TaskRepository;

import com.industrialtsi.mylyn.core.dto.IndustrialAttachment;
import com.industrialtsi.mylyn.core.dto.IndustrialComment;
import com.industrialtsi.mylyn.core.dto.IndustrialQueryParams;
import com.industrialtsi.mylyn.core.dto.IndustrialTask;

/**
 * @author Ahmed Aadel
 * 
 * @since 0.8.0
 * 
 * @noimplement Extend {@link PersistorAdapter} instead for more robust API
 *              changes.
 * 
 *              TODO remove the throwing of {@link SQLException} and replace
 *              with PersistorException.
 */
public interface IPersistor {

	/**
	 * This task is new, so it has no ID to be identified with among other tasks
	 * in this repository. Persist this new task and return its id.
	 * 
	 * @param repository
	 *            the repository to which this task belongs.
	 * @param taskToPersist
	 *            the task to persist.
	 * @return the id of this newly created and persisted task.
	 * @throws SQLException
	 * @throws CoreException
	 */
	String persistTask(TaskRepository repository, IndustrialTask taskToPersist)
			throws SQLException, CoreException;

	/**
	 * Persist this comment state
	 * 
	 * @param repository
	 *            The repository to which this comment belongs.
	 * @param comment
	 *            The comment to persist
	 * @throws SQLException
	 * @throws CoreException
	 */
	void persistComment(TaskRepository repository, IndustrialComment comment)
			throws SQLException, CoreException;

	/**
	 * This method is only called when extension
	 * <code>repository-attributes</code> <code>can-post-attachments</code> is
	 * <code>true</code>.
	 * 
	 * @param repository
	 *            The repository to which this attachment belongs.
	 * @param attachment
	 *            The attachment to persist
	 * @throws SQLException
	 * @throws CoreException
	 */
	void persistAttachment(TaskRepository repository,
			IndustrialAttachment attachment) throws SQLException, CoreException;

	/**
	 * Persist this existing task in the repository.
	 * 
	 * @param repository
	 *            The repository to which this task belongs.
	 * @param task
	 *            The task to update
	 * @throws SQLException
	 * @throws CoreException
	 */
	void updateTask(TaskRepository repository, IndustrialTask task)
			throws SQLException, CoreException;

	/**
	 * 
	 * @param repository
	 *            The repository to which this task belongs.
	 * @param key
	 *            The Key of the task to fetch.
	 * @return the task to fetch
	 * @throws SQLException
	 * @throws CoreException
	 */
	IndustrialTask fetchTask(TaskRepository repository, String... taskId)
			throws SQLException, CoreException;

	/**
	 * Given a repository and one or more task keys, return all comments
	 * belonging to the task with the given keys.
	 * 
	 * @param repository
	 * @param keys
	 *            one or more keys of the tasks owning these comments
	 * @return the comments for the tasks
	 * @throws SQLException
	 * @throws CoreException
	 */
	List<IndustrialComment> fetchComments(TaskRepository repository,
			String... taskId) throws SQLException, CoreException;

	/**
	 * Given a repository and a task key, return all attachments belonging to
	 * the task with the given key.
	 * 
	 * @param repository
	 *            The repository to which the task owning these attachments
	 *            belongs.
	 * @param key
	 *            The key of the task owning these attachments.
	 * @return the attachment proxies for this task
	 * @throws SQLException
	 * @throws CoreException
	 */
	List<IndustrialAttachment> fetchAttachments(TaskRepository repository,
			String... taskId) throws SQLException, CoreException;

	/**
	 * Fetches additional Mylyn attributes from the repository.
	 * <p>
	 * Until the Mylyn attribute <code>task.common.user.reporter.name</code> is
	 * supported by its own method, it can be supplied by this method.
	 * 
	 * @param repository
	 * @param key
	 *            of the task
	 * @return Map with additional attributes and values.
	 * @throws SQLException
	 * @throws CoreException
	 */
	Map<String, String> fetchAdditional(TaskRepository repository,
			String... taskId) throws SQLException, CoreException;

	/**
	 * Return the blob content of the attachment with the given key. The passed
	 * id of the attachment is read from the task so this method should not
	 * return null.
	 * 
	 This method is only called when extension
	 * <code>repository-attributes</code> <code>can-get-attachments</code> is
	 * <code>true</code>.

	 * @param repository
	 *            The repository to which the task owning this attachment blob
	 *            belongs.
	 * @param attachmentId
	 *            The key of the attachment whose blob must be fetched.
	 * @return byte[] which contains the attachment content, should not be null
	 * @throws SQLException
	 * @throws CoreException
	 */
	byte[] fetchAttachmentBlob(TaskRepository repository, String attachmentId)
			throws SQLException, CoreException;

	/**
	 * Return all keys/id's of the tasks/issues handled by this very repository.
	 * 
	 * @param repository
	 * @param criteria
	 * @return
	 * @throws SQLException
	 * @throws CoreException
	 */
	/**
	 * Search for all tasks obeying the given criteria and return their keys.
	 * 
	 * @param repository
	 * @param criteria
	 * @return List of keys/id's of found tasks if any. Empty list otherwise.
	 * @throws SQLException
	 * @throws CoreException
	 */
	List<String> findTasks(TaskRepository repository,
			IndustrialQueryParams criteria) throws SQLException, CoreException;

	/**
	 * This list of owners is also used to populate the Owner task attribute and
	 * to query the repository.
	 * 
	 * @param repository
	 * @return a list of owners' names that are valid to use in this repository.
	 * @throws SQLException
	 * @throws CoreException
	 */
	List<String> getLegalOwners(TaskRepository repository) throws SQLException,
			CoreException;

	/**
	 * This list of products is also used to populate the Product task attribute
	 * and to query the repository.
	 * 
	 * @param repository
	 * @return a list of products that are valid to use in this repository.
	 * @throws SQLException
	 * @throws CoreException
	 */
	List<String> getLegalProducts(TaskRepository repository)
			throws SQLException, CoreException;

	/**
	 * This list of priorities is also used to populate a Priority task
	 * attribute and to query the repository.
	 * 
	 * @param repository
	 * @return a list of priorities that are valid to be used in this
	 *         repository.
	 * @throws SQLException
	 * @throws CoreException
	 */
	List<String> getLegalPriorities(TaskRepository repository)
			throws SQLException, CoreException;

	/**
	 * This list of status is also used to populate a Status task attribute and
	 * to query the repository.
	 * 
	 * @param repository
	 * @return a list of status that are valid to be used in this repository.
	 * @throws SQLException
	 * @throws CoreException
	 */
	List<String> getLegalIssueStatus(TaskRepository repository)
			throws SQLException, CoreException;

	/**
	 * Validate this repository configuration.
	 * 
	 * @param repository
	 * @return true if valid, false otherwise.
	 * @throws SQLException
	 * @throws CoreException
	 */
	boolean validate(TaskRepository repository) throws SQLException,
			CoreException;

	/**
	 * Defines if this repository can be intitialized. This means that the class
	 * defined in the
	 * <code>org.eclipse.mylyn.sql.core.database.dbinitializer</code> may be
	 * called to do initialization work. Whatever that may be is up to the
	 * implementor of that class.
	 * 
	 * @param repository
	 * @return true if the initialization class can be called otherwise false.
	 * @throws SQLException
	 * @throws CoreException
	 */
	// FIXME is this really necessary and how do we connect the database to the
	// extension point because multiple persistors may exist and this is not
	// used in the extension point Move to extension point!
	boolean canInitialize(TaskRepository repository) throws SQLException,
			CoreException;

	/**
	 * Initialize this repository configuration.
	 * 
	 * @param repository
	 * @throws SQLException
	 * @throws CoreException
	 */
	// FIXME please make clear what this method is doing here. Also describe the
	// relationship with dbinitializer extension point.
	void initialize(TaskRepository repository) throws SQLException,
			CoreException;

	/**
	 * Update an existing comment.
	 * 
	 * @param repository
	 * @param comment
	 * @throws SQLException
	 * @throws CoreException
	 * 
	 * @since 0.8.2
	 */
	void updateComment(TaskRepository repository, IndustrialComment comment)
			throws SQLException, CoreException;

	/**
	 * Update an existing attachment.
	 * 
	 * @param repository
	 * @param attachment
	 * @throws SQLException
	 * @throws CoreException
	 * 
	 * @since 0.8.2
	 */
	void updateAttachment(TaskRepository repository,
			IndustrialAttachment attachment) throws SQLException, CoreException;

	/**
	 * Delete a task from repository.
	 * 
	 * @param repository
	 * @param task
	 * @throws SQLException
	 * @throws CoreException
	 * 
	 * @since 0.8.2
	 */
	void deleteTask(TaskRepository repository, IndustrialTask task)
			throws SQLException, CoreException;

	/**
	 * Delete a comment from repository.
	 * 
	 * @param repository
	 * @param comment
	 * @throws SQLException
	 * @throws CoreException
	 * 
	 * @since 0.8.2
	 */
	void deleteComment(TaskRepository repository, IndustrialComment comment)
			throws SQLException, CoreException;

	/**
	 * Delete a attachment from repository.
	 * 
	 * @param repository
	 * @param attachment
	 * @throws SQLException
	 * @throws CoreException
	 * 
	 * @since 0.8.2
	 */
	void deleteAttachment(TaskRepository repository,
			IndustrialAttachment attachment) throws SQLException, CoreException;

	/**
	 * @param repository
	 * @return true if user and password are a valid User combination for this
	 *         repository.
	 * @throws SQLException
	 * @throws CoreException
	 * 
	 * @since 0.9.2
	 */
	boolean isAuthenticated(TaskRepository repository) throws SQLException,
			CoreException;

}
