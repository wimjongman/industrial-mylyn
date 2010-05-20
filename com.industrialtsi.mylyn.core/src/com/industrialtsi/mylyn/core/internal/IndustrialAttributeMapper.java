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
package com.industrialtsi.mylyn.core.internal;

import java.util.HashMap;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;

import com.industrialtsi.mylyn.core.IndustrialCore;
import com.industrialtsi.mylyn.core.dto.IndustrialQueryOptions;
import com.industrialtsi.mylyn.core.dto.IndustrialQueryParams;

/**
 * @author Maarten Meijer
 */
public class IndustrialAttributeMapper extends TaskAttributeMapper {

	private static final String EMPTY_STRING = ""; //$NON-NLS-1$

	private static HashMap<String, String> labels = new HashMap<String, String>();

	private TaskRepository repository = null;

	private final IndustrialQueryOptions legal;

	static {
		// labels.put(TaskAttribute.USER_OWNER, "Owner:");
		labels.put(TaskAttribute.USER_CC, "CC:");
		labels.put(TaskAttribute.USER_REPORTER, "Reporter contact:");
		// labels.put(TaskAttribute.USER_REPORTER_NAME, "Reporter name:");
		labels.put(TaskAttribute.COMMENT_NEW, "task.common.comment.new");
		labels.put(TaskAttribute.COMMENT_TEXT, "Comment:");
		labels.put(TaskAttribute.COMMENT_DATE, "Date:");
		labels.put(TaskAttribute.COMMENT_AUTHOR, "Commenter contact:");
		// labels.put(TaskAttribute.COMMENT_AUTHOR_NAME, "Commenter name:");
		labels.put(TaskAttribute.DESCRIPTION, "Description:");
		labels.put(TaskAttribute.ATTACHMENT_ID, "Attachement id:");
		// labels.put(TaskAttribute.ATTACHMENT_TYPE, "Attachment type:");
		// labels.put(TaskAttribute.ATTACHMENT_CTYPE, "Attachment ctype:");
		labels.put(TaskAttribute.ATTACHMENT_DATE, "Attachment date:");
		labels.put(TaskAttribute.ATTACHMENT_URL, "Attachment url:");
		labels.put(TaskAttribute.ATTACHMENT_FILENAME, "Filename:");
		labels.put(TaskAttribute.ATTACHMENT_SIZE, "Attachment.size:");
		labels.put(TaskAttribute.USER_ASSIGNED, "Assigned:");
		labels.put(TaskAttribute.USER_REPORTER, "Reporter:");
		// labels.put(TaskAttribute.USER_ASSIGNED_NAME, "Assignee name:");
		labels.put(TaskAttribute.RESOLUTION, "Resolution:");
		labels.put(TaskAttribute.STATUS, "Status:");
		labels.put(TaskAttribute.PRIORITY, "Priority:");
		// labels.put(TaskAttribute.DATE_MODIFIED, "Date modified:");
		labels.put(TaskAttribute.SUMMARY, "Summary:");
		labels.put(TaskAttribute.PRODUCT, "Product:");
		labels.put(TaskAttribute.DATE_CREATION, "Date created:");
		labels.put(TaskAttribute.DATE_COMPLETION, "Date completed:");
		labels.put(TaskAttribute.DATE_DUE, "Date due:");
		labels.put(TaskAttribute.DATE_MODIFICATION, "Date modified:");
		labels.put(TaskAttribute.KEYWORDS, "Keywords:");
		/**
		 * Boolean attribute. If true, repository user needs to be added to the
		 * cc list.
		 */
		labels.put(TaskAttribute.ADD_SELF_CC, "Add self to CC:");
		// labels.put(TaskAttribute.NEW_CC, "New CC:");
		// labels.put(TaskAttribute.REMOVE_CC, "Remove CC:");
		labels.put(TaskAttribute.TASK_KEY, "Task key:");
	}

	public IndustrialAttributeMapper(TaskRepository repository) {
		super(repository);
		this.repository = repository;
		// TODO Auto-generated constructor stub
		String attributes = repository.getProperty(IndustrialCore.REPOSITORY_ATTRIBUTES);
		if (null == attributes) {
			try {
				IndustrialCore.getDefault().getConnector().updateRepositoryConfiguration(repository, null);
			} catch (CoreException e) {
				CoreLogger.logError(e);
			}
		}
		legal = new IndustrialQueryParams(null != attributes ? attributes : EMPTY_STRING);
	}

	public String getName(String key) {
		assert null != key;
		// TODO determine the external name for this key
		return null != labels.get(key) ? labels.get(key) : key;
	}

	public TaskRepository getRepository() {
		return repository;
	}

	/*
	@Deprecated
	public Date getDateForAttributeType(String attributeKey, String dateString) {
		// TODO Auto-generated method stub
		try {
			// FIXME add more dates in a generic way
			if (!EMPTY_STRING.equals(dateString)) {
				SimpleDateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.US); //$NON-NLS-1$
				Date date = formatter.parse(dateString);
				return date;
			}
		} catch (Exception e) {
			CoreLogger.logError(e);
		}
		return new Date();
	}

	@Deprecated
	public boolean isHidden(String key) {
		assert null != key;
		if (TaskAttribute.SUMMARY.equals(key) || TaskAttribute.DESCRIPTION.equals(key)
				|| TaskAttribute.STATUS.equals(key) || TaskAttribute.PRIORITY.equals(key)
				|| TaskAttribute.DATE_CREATION.equals(key)) {
			return true;
		}
		if (TaskAttribute.ADD_SELF_CC.equals(key)) {
			return true;
		}
		return false;
	}

	@Deprecated
	public boolean isReadOnly(String key) {
		assert null != key;
		// TODO Create logic to determine what fields can be written
		return isReadOnlyPropertySet(key);
	}

	@Deprecated
	private boolean isReadOnlyPropertySet(String key) {
		if (null == this.repository)
			return true;

		String readonly = repository.getProperty(key + ".ro"); //$NON-NLS-1$
		boolean result = (null != readonly) && "true".equalsIgnoreCase(readonly); //$NON-NLS-1$
//		System.out.println(key + ".ro : " + Boolean.toString(result));
		return result;

	}

	@Deprecated
	public String mapCommonAttributeKey(String key) {
		assert null != key;
		return key;
	}


	@Deprecated
	public IndustrialQueryOptions getLegal() {
		return legal;
	}
*/

}
