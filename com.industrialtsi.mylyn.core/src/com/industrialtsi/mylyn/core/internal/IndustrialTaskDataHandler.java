/*******************************************************************************
 * Copyright (c) 2008,2009,2010 Industrial TSI and Maarten Meijer.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  	Maarten Meijer - initial API and implementation 
 *  	Industrial TSI - improvements
 *******************************************************************************/
package com.industrialtsi.mylyn.core.internal;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.mylyn.tasks.core.IRepositoryPerson;
import org.eclipse.mylyn.tasks.core.ITaskMapping;
import org.eclipse.mylyn.tasks.core.RepositoryResponse;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.RepositoryResponse.ResponseKind;
import org.eclipse.mylyn.tasks.core.data.AbstractTaskDataHandler;
import org.eclipse.mylyn.tasks.core.data.TaskAttachmentMapper;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;
import org.eclipse.mylyn.tasks.core.data.TaskCommentMapper;
import org.eclipse.mylyn.tasks.core.data.TaskData;

import com.industrialtsi.mylyn.core.IndustrialCore;
import com.industrialtsi.mylyn.core.dto.IndustrialAttachment;
import com.industrialtsi.mylyn.core.dto.IndustrialComment;
import com.industrialtsi.mylyn.core.dto.IndustrialQueryOptions;
import com.industrialtsi.mylyn.core.dto.IndustrialQueryParams;
import com.industrialtsi.mylyn.core.dto.IndustrialTask;
import com.industrialtsi.mylyn.core.persistence.PersistorsManager;

/**
 * <code>IndustrialTaskDataHandler</code> : handles all conersion between the task attributes and the IndustrialTask DTO.
 *
 * @author maarten meijer
 *
 */
public class IndustrialTaskDataHandler extends AbstractTaskDataHandler {

	private static final String EOL = "\n"; //$NON-NLS-1$

	private static final String DOT = "."; //$NON-NLS-1$

	private static final String UNDERSCORE = "_"; //$NON-NLS-1$

	private static final String USER_NAME = "user.name"; //$NON-NLS-1$

	private static final String EMPTY_STRING = ""; //$NON-NLS-1$

	private IndustrialAttributeMapper attributeMapper;

	private TaskData data;

	@Override
	public TaskAttributeMapper getAttributeMapper(TaskRepository taskRepository) {
		if (null == attributeMapper) {
			attributeMapper = new IndustrialAttributeMapper(taskRepository);
		}
		return attributeMapper;
	}

	@Override
	public boolean initializeTaskData(TaskRepository repository, TaskData data,
			ITaskMapping initializationData, IProgressMonitor monitor)
			throws CoreException {
		String attributes = repository
				.getProperty(IndustrialCore.REPOSITORY_ATTRIBUTES);

		if (null == attributes) {
			IndustrialCore.getDefault().getConnector()
					.updateRepositoryConfiguration(repository, monitor);
			attributes = repository
					.getProperty(IndustrialCore.REPOSITORY_ATTRIBUTES);
		}
		IndustrialQueryOptions legal = new IndustrialQueryParams(
				null != attributes ? attributes : EMPTY_STRING);
		createDefaultAttributes((IndustrialAttributeMapper) data
				.getAttributeMapper(), data, legal, data.isNew());
		return true;
	}

	@Override
	public RepositoryResponse postTaskData(TaskRepository repository,
			TaskData taskData, Set<TaskAttribute> oldAttributes,
			IProgressMonitor monitor) throws CoreException {

		RepositoryResponse response = null;
		TaskAttribute attribute = taskData.getRoot().getAttribute(
				TaskAttribute.COMMENT_NEW);
		String comment = null == attribute ? EMPTY_STRING : attribute
				.getValue();

		if (taskData.isNew()) {
			response = createNewTask(repository, taskData);
		} else if (EMPTY_STRING.equals(comment)) {
			// maybe submit context...
			response = updateTask(repository, taskData, oldAttributes);
		} else {
			// add a comment
			response = addCommentToTask(repository, taskData, comment);
		}

		return response;
	}

	/**
	 * @param repository
	 * @param taskData
	 * @param comment comment text to add
	 * @return response to show success/failure
	 * @throws CoreException
	 */
	private RepositoryResponse addCommentToTask(TaskRepository repository,
			TaskData taskData, String comment) throws CoreException {

		IndustrialComment added = new IndustrialComment();

		added.setTaskId(taskData.getTaskId());
		added.setText(comment);
		added.setDate(new Date());
		String userName = repository.getUserName();
		if (null == userName || EMPTY_STRING.equals(userName)) {
			userName = System.getProperty(USER_NAME);
		}
		added.setAuthor(userName);
		added.setAuthorName(userName);
		// TODO set description here for adding a context
		added.setDescription("empty"); //$NON-NLS-1$

		RepositoryResponse response;

		try {
			PersistorsManager.getManager().getPersistor(repository)
					.persistComment(repository, added);
			response = new RepositoryResponse(ResponseKind.TASK_UPDATED,
					taskData.getTaskId());
		} catch (SQLException e) {
			IStatus status = CoreLogger.createStatus(IStatus.ERROR, e
					.getMessage(), e.getCause());
			CoreLogger.log(status);
			throw new CoreException(status);
		}

		return response;
	}

	private RepositoryResponse updateTask(TaskRepository repository,
			TaskData taskData, Set<TaskAttribute> oldAttributes) throws CoreException {
		RepositoryResponse response;
		// try {
		IndustrialTask taskDto = new IndustrialTask(repository.getUrl(), taskData
				.getTaskId(), EMPTY_STRING);

		taskDto.setSummary(safeValue(taskData, TaskAttribute.SUMMARY));
		taskDto.setPriority(safeValue(taskData, TaskAttribute.PRIORITY));
		taskDto.setProduct(safeValue(taskData, TaskAttribute.PRODUCT));
		taskDto.setIssueStatus(safeValue(taskData, TaskAttribute.STATUS));
		taskDto.setOwner(safeValue(taskData, TaskAttribute.USER_ASSIGNED));
		taskDto.setReporter(safeValue(taskData, TaskAttribute.USER_REPORTER));

		taskDto.setNotes(safeValue(taskData, TaskAttribute.DESCRIPTION));

		// TODO set all date values ...
		TaskAttribute dueDate = taskData.getRoot().getAttribute(TaskAttribute.DATE_DUE);
		if (null != dueDate)
			taskDto.setDueDate(getAttributeMapper(repository).getDateValue(
					dueDate));
		TaskAttribute completionDate = taskData.getRoot().getAttribute(
				TaskAttribute.DATE_COMPLETION);
		if (null != completionDate)
			taskDto.setCompletionDate(getAttributeMapper(repository)
					.getDateValue(
							completionDate));
		TaskAttribute creationDate = taskData.getRoot()
				.getAttribute(TaskAttribute.DATE_CREATION);
		if (null != creationDate)
			taskDto.setCreationDate(getAttributeMapper(repository).getDateValue(
					creationDate));
		TaskAttribute modificationDate = taskData.getRoot().getAttribute(
				TaskAttribute.DATE_MODIFICATION);
		if (null != modificationDate)
			taskDto.setModificationDate(getAttributeMapper(repository)
					.getDateValue(
							modificationDate));

		try {
			PersistorsManager.getManager().getPersistor(repository).updateTask(
					repository, taskDto);
		} catch (SQLException e) {
			IStatus status = CoreLogger.createStatus(IStatus.ERROR, e
					.getMessage(), e.getCause());
			CoreLogger.log(status);
			throw new CoreException(status);

		}

		response = new RepositoryResponse(ResponseKind.TASK_UPDATED, taskData
				.getTaskId());
		// TODO store all additional attributes

		return response;
	}


	private RepositoryResponse createNewTask(TaskRepository repository,
			TaskData taskData) throws CoreException {
		RepositoryResponse response;
		// try {
		IndustrialTask taskDto = new IndustrialTask(repository.getUrl(),
				EMPTY_STRING, EMPTY_STRING);

		// FIXME get a Map directly from repository, fall back to object
		// when null

		taskDto.setSummary(safeValue(taskData, TaskAttribute.SUMMARY));
		taskDto.setNotes(safeValue(taskData, TaskAttribute.DESCRIPTION));
		taskDto.setPriority(safeValue(taskData, TaskAttribute.PRIORITY));
		taskDto.setProduct(safeValue(taskData, TaskAttribute.PRODUCT));
		taskDto.setIssueStatus(safeValue(taskData, TaskAttribute.STATUS));
		// TODO set scheduled for date...
		taskDto.setDueDate(safeDate(taskData, TaskAttribute.DATE_DUE));
		
		
		String userName = repository.getUserName();
		if (null == userName || EMPTY_STRING.equals(userName)) {
			userName = System.getProperty(USER_NAME);
		}

		taskDto.setOwner(userName);

		// FIXME the date string created by toString() defaults to
		// Locale.US
		// format
		String taskId;
		try {
			taskId = PersistorsManager.getManager().getPersistor(repository)
					.persistTask(repository, taskDto);
		} catch (SQLException e) {
			IStatus status = CoreLogger.createStatus(IStatus.ERROR, e
					.getMessage(), e.getCause());
			CoreLogger.log(status);
			throw new CoreException(status);

		}
		response = new RepositoryResponse(ResponseKind.TASK_CREATED, taskId);
		// TODO store all additional attributes

		return response;
	}

	/**
	 * @param taskData
	 * @param key specifies {@link TaskAttribute} 
	 * @return the value if the attribute exists, or null if it does not exist
	 */
	private String safeValue(TaskData taskData, String key) {
		if (null != taskData.getRoot().getAttribute(key)) {
			return taskData.getRoot().getAttribute(key).getValue();
		}
		return null;
	}

	/**
	 * @param taskData
	 * @param key specifies {@link TaskAttribute} 
	 * @return the Date if the attribute exists, or null if it does not exist
	 */
	private Date safeDate(TaskData taskData, String key) {
		TaskAttribute attribute = taskData.getRoot().getMappedAttribute(key);
		if (attribute != null) {
//			System.out.println("IndustrialTaskDataHandler.safeDate("+key+")");
			return taskData.getAttributeMapper().getDateValue(attribute);
		}
		return null;
	}

	
	public TaskData getTaskData(final TaskRepository repository, String taskId,
			IProgressMonitor monitor) throws CoreException {
		// System.out.println("IndustrialTaskDataHandler.getTaskData() " +
		// taskId);

		if (null == attributeMapper) {
			attributeMapper = new IndustrialAttributeMapper(repository);
		}
		// if (null == commentMapper) {
		// commentMapper = new TaskCommentMapper();
		// }

		IndustrialQueryOptions legal = new IndustrialQueryParams(repository
				.getProperty(IndustrialCore.REPOSITORY_ATTRIBUTES));

		data = createTaskData(repository, taskId);

		createDefaultAttributes(attributeMapper, data, legal, taskId != null);
		try {

			addAttributes(repository, taskId, data);

			addComments(repository, taskId, data);

			addAttachments(repository, taskId, data);

		} catch (SQLException e) {
			Status status = new Status(IStatus.ERROR, IndustrialCore.PLUGIN_ID,
					e.getMessage(), e.getCause());
			// StatusHandler.log(status);
			throw new CoreException(status);
		}
		return data;
	}

	private void addAttributes(final TaskRepository repository, String taskId,
			TaskData data) throws CoreException {
		IndustrialTask dummy;
		// FIXME get a Map directly from repository, fall back to object
		// when null

		try {
			dummy = PersistorsManager.getManager().getPersistor(repository)
					.fetchTask(repository, taskId);
		} catch (SQLException e) {
			IStatus status = CoreLogger.createStatus(IStatus.ERROR, e
					.getMessage(), e.getCause());
			CoreLogger.log(status);
			throw new CoreException(status);
		}
		if(null == dummy) {
			String fqcn = PersistorsManager.getManager().getPersistor(repository).getClass().getCanonicalName();
			IStatus status = CoreLogger.createStatus(IStatus.ERROR, 
					fqcn + ".fetchTask()\nfailed to return data.\n\nHave you implemented it already?", null); //$NON-NLS-1$
			CoreLogger.log(status);
			throw new CoreException(status);
			
		}
		dummy.setUrl(repository.getUrl());
		dummy.setTaskId(taskId);

		fillAttribute(data, dummy.getNotes(), TaskAttribute.DESCRIPTION);
		fillAttribute(data, dummy.getSummary(), TaskAttribute.SUMMARY);
		fillAttribute(data, dummy.getPriority(), TaskAttribute.PRIORITY);
		fillAttribute(data, dummy.getProduct(), TaskAttribute.PRODUCT);
		fillAttribute(data, dummy.getIssueStatus(), TaskAttribute.STATUS);
		fillAttribute(data, dummy.getOwner(), TaskAttribute.USER_ASSIGNED);
		fillAttribute(data, dummy.getReporter(), TaskAttribute.USER_REPORTER);
		// FIXME make difference between not using and not setting
		if (null != dummy.getCreationDate())
			fillAttribute(data, Long.toString(dummy.getCreationDate().getTime()),
					TaskAttribute.DATE_CREATION);
		if (null != dummy.getModificationDate())
			fillAttribute(data, Long.toString(dummy.getModificationDate()
					.getTime()), TaskAttribute.DATE_MODIFICATION);
		if (null != dummy.getDueDate())
			fillAttribute(data, Long.toString(dummy.getDueDate().getTime()),
					TaskAttribute.DATE_DUE);
		if (null != dummy.getCompletionDate())
			fillAttribute(data, Long.toString(dummy.getCompletionDate()
					.getTime()), TaskAttribute.DATE_COMPLETION);

		// read additional attributes
		Map<String, String> atts = Collections.emptyMap();
		try {

			atts = PersistorsManager.getManager().getPersistor(repository)
					.fetchAdditional(repository, taskId);

		} catch (SQLException e) {
			IStatus status = CoreLogger.createStatus(IStatus.ERROR, e
					.getMessage(), e.getCause());
			CoreLogger.log(status);
			throw new CoreException(status);
		}

		if ((null != atts) && !atts.isEmpty()) {
			for (Entry<String, String> entry : atts.entrySet()) {
				String mylynKey = entry.getKey().replaceAll(UNDERSCORE, DOT).toLowerCase();
				String value = entry.getValue();
				if(null == value) {
					StringBuilder stringBuilder = new StringBuilder();
					stringBuilder.append("<null> value for Mylyn attribute : "); //$NON-NLS-1$
					stringBuilder.append(mylynKey);
					CoreLogger.logInfo(stringBuilder.toString()); 
				}
				fillAttribute(data, null == value ? "" : value, mylynKey); //$NON-NLS-1$
			}
		}
	}

	private void addComments(final TaskRepository repository, String taskId,
			TaskData data) throws SQLException, CoreException {
		// Store the comments which are loaded in IndustrialComment grouped by
		// groupKey. groupKey could be null which indicates it was not set.
		//

		List<IndustrialComment> commentsList = PersistorsManager.getManager()
				.getPersistor(repository).fetchComments(repository, taskId);
		IndustrialComment[] comments = commentsList
				.toArray(new IndustrialComment[0]);
		int commentCounter = 1;

		for (int i = 0; i < comments.length; i++) {
			String groupKey = comments[i].getGroupKey();

			TaskCommentMapper mapper = new TaskCommentMapper();
			mapper.setAuthor(repository.createPerson(comments[i].getAuthor()));
			mapper.setCreationDate(comments[i].getDate());

			StringBuffer commentText = new StringBuffer();
			while (comments[i].getGroupKey() == null
					|| comments[i].getGroupKey().equals(groupKey)) {
				if (null != comments[i].getText())
					commentText.append(comments[i].getText() + EOL);
				i++;
				if (i >= comments.length) {
					// one more to make sure we stop the loop because we
					// subtract one later if this is not the last but a
					// group change.
					i++;
					break;
				}
			}
			i--;

			mapper.setText(commentText.toString());
			// TODO mapper.setUrl();
			mapper.setNumber(commentCounter);

			if (commentText.toString().length() > 0) {
				TaskAttribute attribute = data.getRoot().createAttribute(
						TaskAttribute.PREFIX_COMMENT + commentCounter);
				mapper.applyTo(attribute);
				commentCounter++;
			}
		}
	}

	private void addAttachments(final TaskRepository repository, String taskId,
			TaskData data) throws CoreException {

		List<IndustrialAttachment> attachments = Collections.emptyList();
		try {
			attachments = PersistorsManager.getManager().getPersistor(
					repository).fetchAttachments(repository, taskId);
		} catch (SQLException e) {
			// TODO package in Status and throw CoreException
			IStatus status = CoreLogger
					.createStatus(
							IStatus.ERROR,
							"An SQL problem occured with writing an attachment on " + repository.getUrl(), e); //$NON-NLS-1$
			CoreLogger.log(status);
			throw new CoreException(status);
		}

		int i = 0;
		for (final IndustrialAttachment attachment : attachments) {
			// ignore empty comments...
			if (null != attachment && null != attachment.getId()) {

				TaskAttribute attribute = data.getRoot().createAttribute(
						TaskAttribute.PREFIX_ATTACHMENT + i);
				TaskAttachmentMapper taskAttachment = TaskAttachmentMapper
						.createFrom(attribute);
				
				IRepositoryPerson person = repository.createPerson(attachment.getAuthor());

				taskAttachment.setAuthor(person);

				taskAttachment.setAttachmentId(attachment.getId());
				taskAttachment.setDescription(attachment.getDescription());
				taskAttachment.setCreationDate(attachment.getDate());

				taskAttachment.setFileName(attachment.getFilename());
				taskAttachment.setContentType(attachment.getCtype());
				taskAttachment.setLength(attachment.getSize());

				taskAttachment.setUrl(attachment.getUrl());

				taskAttachment.applyTo(attribute);
				i++;
			}

		}
	}

	private void fillAttribute(TaskData data, String value, String attrId) {
		if ((null == value || EMPTY_STRING.equals(value))
				&& !attrId.equals(TaskAttribute.DESCRIPTION)
				&& !attrId.equals(TaskAttribute.SUMMARY)) {
			data.getRoot().removeAttribute(attrId);
			return;
		}
		if (null == data.getRoot().getAttribute(attrId)) {
			TaskAttribute attr = data.getRoot().createAttribute(attrId);
			// FIXME handle person data specially here
			if(TaskAttribute.USER_ASSIGNED.equals(attrId) ||
					TaskAttribute.USER_REPORTER.equals(attrId) ||
					TaskAttribute.USER_CC.equals(attrId)) {
				attr.getMetaData().setKind(TaskAttribute.KIND_PEOPLE);
				attr.getMetaData().setType(TaskAttribute.TYPE_PERSON);
				attr.getMetaData().setLabel(attributeMapper.getName(attr.getId()));
				attr.getMetaData().setReadOnly(TaskAttribute.USER_REPORTER.equals(attrId));
			}
			data.getRoot().getAttribute(attrId).getMetaData().setKind(
					TaskAttribute.KIND_DEFAULT);
		}
		if (null == value) {
			CoreLogger.logInfo("<null> value in attribute : " + attrId); //$NON-NLS-1$
		}
		data.getRoot().getAttribute(attrId)
				.setValue(null == value ? "" : value); //$NON-NLS-1$
	}

	private TaskData createTaskData(TaskRepository repository, String taskId) {
		TaskData data = new TaskData(new IndustrialAttributeMapper(repository),
				IndustrialCore.CONNECTOR_KIND, repository.getUrl(), taskId);
		try {
			initializeTaskData(repository, data, null, null);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}

	private void createDefaultAttributes(IndustrialAttributeMapper factory,
			TaskData data, IndustrialQueryOptions legal, boolean existingTask) {
		TaskAttribute attr;

		if (data == this.data
				&& null == data.getRoot().getAttribute(
						TaskAttribute.COMMENT_NEW)) {
			attr = createAttribute(factory, data, TaskAttribute.COMMENT_NEW,
					null);
			attr.getMetaData().setType(TaskAttribute.TYPE_LONG_RICH_TEXT);
			attr.getMetaData().setLabel(factory.getName(attr.getId()));
			return;
		}

		attr = createAttribute(factory, data, TaskAttribute.USER_REPORTER,
				legal.getOwner());
		attr.getMetaData().setKind(TaskAttribute.KIND_PEOPLE);
		attr.getMetaData().setType(TaskAttribute.TYPE_PERSON);
		attr.getMetaData().setLabel(factory.getName(attr.getId()));

		if (existingTask && !EMPTY_STRING.equals(data.getTaskId())) {
			attr = createAttribute(factory, data, TaskAttribute.COMMENT_NEW,
					null);
			attr.getMetaData().setType(TaskAttribute.TYPE_LONG_RICH_TEXT);
			attr.getMetaData().setLabel(factory.getName(attr.getId()));

		} else {

			// TODO initialize reporter name with default
			;
		}
		attr = createAttribute(factory, data, TaskAttribute.SUMMARY, null);
		attr.getMetaData().setType(TaskAttribute.TYPE_SHORT_TEXT);
		attr.getMetaData().setLabel(factory.getName(attr.getId()));

		attr = createAttribute(factory, data, TaskAttribute.DESCRIPTION, null);
		attr.getMetaData().setType(TaskAttribute.TYPE_LONG_RICH_TEXT);
		attr.getMetaData().setLabel(factory.getName(attr.getId()));

		attr = createAttribute(factory, data, TaskAttribute.PRIORITY, legal
				.getPriority());
		attr.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
		attr.getMetaData().setType(TaskAttribute.TYPE_SINGLE_SELECT);
		attr.getMetaData().setLabel(factory.getName(attr.getId()));
		// TODO set the default priority from config

		attr = createAttribute(factory, data, TaskAttribute.PRODUCT, legal
				.getProduct());
		attr.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
		attr.getMetaData().setType(TaskAttribute.TYPE_SINGLE_SELECT);
		attr.getMetaData().setLabel(factory.getName(attr.getId()));

		attr = createAttribute(factory, data, TaskAttribute.STATUS, legal
				.getIssueStatus());
		attr.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
		attr.getMetaData().setType(TaskAttribute.TYPE_SINGLE_SELECT);
		attr.getMetaData().setLabel(factory.getName(attr.getId()));
		// TODO set the default status from config

		attr = createAttribute(factory, data, TaskAttribute.DATE_CREATION, null);
		factory.setDateValue(attr, new Date());
		attr.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
		attr.getMetaData().setType(TaskAttribute.TYPE_DATE);
		attr.getMetaData().setReadOnly(true);
		attr.getMetaData().setLabel(factory.getName(attr.getId()));

		attr = createAttribute(factory, data, TaskAttribute.DATE_DUE, null);
		attr.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
		attr.getMetaData().setType(TaskAttribute.TYPE_DATE);
		attr.getMetaData().setLabel(factory.getName(attr.getId()));

		attr = createAttribute(factory, data, TaskAttribute.DATE_MODIFICATION,
				null);
		attr.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
		attr.getMetaData().setType(TaskAttribute.TYPE_DATE);
		attr.getMetaData().setReadOnly(true);
		attr.getMetaData().setLabel(factory.getName(attr.getId()));

		attr = createAttribute(factory, data, TaskAttribute.USER_ASSIGNED,
				legal.getOwner());
		attr.getMetaData().setKind(TaskAttribute.KIND_PEOPLE);
		attr.getMetaData().setType(TaskAttribute.TYPE_PERSON);
		attr.getMetaData().setLabel(factory.getName(attr.getId()));

	}

	private TaskAttribute createAttribute(IndustrialAttributeMapper factory,
			TaskData data, String key, String[] values) {
		TaskRepository repository = factory.getRepository();
		TaskAttribute attr = data.getRoot().createAttribute(key);
		// FIXME should NOT set to read only for newly created tasks

		// boolean isReadonly =
		// ConnectorXmlConfig.getInstance().isAttributeReadOnly(repository, key
		// + "_readOnly");
		boolean isReadonly = repository.hasProperty(key + "_readOnly"); //$NON-NLS-1$
		if (!data.isNew()) {
			attr.getMetaData().setReadOnly(isReadonly);
		}

		if (data.isNew() || !attr.getMetaData().isReadOnly()) {
			// add options for priority;
			if (null != values) {
				for (String val : values) {
					attr.putOption(val, val);
				}
			}
			// data.addAttribute(key, attr);

		}
		return attr;
	}
	
	

}
