/*******************************************************************************
 * Copyright (c) 2008 Industrial TSI and Maarten Meijer.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Industrial TSI - initial API and implementation
 *******************************************************************************/

package com.industrialtsi.mylyn.memory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.mylyn.tasks.core.TaskRepository;

import com.industrialtsi.mylyn.core.dto.IndustrialAttachment;
import com.industrialtsi.mylyn.core.dto.IndustrialComment;
import com.industrialtsi.mylyn.core.dto.IndustrialQueryParams;
import com.industrialtsi.mylyn.core.dto.IndustrialTask;
import com.industrialtsi.mylyn.core.persistence.PersistorAdapter;

/**
 * On-The-Fly (Memory) implementation of the interface
 * <code>com.industrialtsi.mylyn.core.persistence.IPersistor</code>
 * 
 * @author Wim Jongman
 * @since 0.8.0
 * 
 */

public class MemoryPersistor extends PersistorAdapter {

	private HashMap<String, IndustrialTask> tasks = new HashMap<String, IndustrialTask>();

	private HashMap<String, ArrayList<IndustrialComment>> comments = new HashMap<String, ArrayList<IndustrialComment>>();

	private Map<String, byte[]> blobs = new HashMap<String, byte[]>();

	private Map<String, List<IndustrialAttachment>> attachements = new HashMap<String, List<IndustrialAttachment>>();

	public MemoryPersistor() {
	}

	public boolean canInitialize(TaskRepository repository) throws SQLException, CoreException {
		return false;
	}

	public Map<String, String> fetchAdditional(TaskRepository repository, String... key) throws SQLException,
			CoreException {

		Map<String, String> result = new HashMap<String, String>();
		result.put("task.common.user.reporter.name", System.getProperty("user.name"));
		return result;
	}

	public byte[] fetchAttachmentBlob(TaskRepository repository, String attachmentId) throws SQLException,
			CoreException {
		return blobs.get(attachmentId);
	}

	public List<IndustrialAttachment> fetchAttachments(TaskRepository repository, String... taskId)
			throws SQLException, CoreException {

		List<IndustrialAttachment> as = attachements.get(taskId[0]);
		return (as != null) ? as : new ArrayList<IndustrialAttachment>();

	}

	public List<IndustrialComment> fetchComments(TaskRepository repository, String... taskId) throws SQLException,
			CoreException {
		List<IndustrialComment> cs = comments.get(taskId[0]);
		return (cs != null) ? cs : new ArrayList<IndustrialComment>();
	}

	public IndustrialTask fetchTask(TaskRepository repository, String... taskId) throws SQLException, CoreException {
		return tasks.get(taskId[0]);
	}

	public List<String> findTasks(TaskRepository repository, IndustrialQueryParams criteria) throws SQLException,
			CoreException {
		Set<String> keys = tasks.keySet();
		if (keys != null)
			return new ArrayList<String>(keys);
		return new ArrayList<String>();
	}

	public List<String> getLegalIssueStatus(TaskRepository repository) throws SQLException, CoreException {
		return Arrays.asList(new String[] { "OPEN", "CLOSED" });
	}

	public List<String> getLegalOwners(TaskRepository repository) throws SQLException, CoreException {
		return Arrays.asList(new String[] { System.getProperty("user.name"), "Somebody", "Everybody", "Anybody", "Nobody" });
	}

	public List<String> getLegalPriorities(TaskRepository repository) throws SQLException, CoreException {
		return Arrays.asList(new String[] { "High", "Medium", "Low" });
	}

	public List<String> getLegalProducts(TaskRepository repository) throws SQLException, CoreException {
		return Arrays.asList(new String[] { "ECLIPSE", ".NET", "NETBEANS" });
	}

	public void initialize(TaskRepository repository) throws SQLException, CoreException {
	}

	public void persistAttachment(TaskRepository repository, IndustrialAttachment attachment) throws SQLException,
			CoreException {
		List<IndustrialAttachment> list = attachements.get(attachment.getTaskId());
		if (list == null) {
			list = new ArrayList<IndustrialAttachment>();
		}

		list.add(attachment);
		attachment.setId("" + (list.size() + 1));
		attachements.put(attachment.getTaskId(), list);

	}

	public void persistComment(TaskRepository repository, IndustrialComment comment) throws SQLException, CoreException {

		comment.setGroupKey(Calendar.getInstance().getTime().toString());
		ArrayList<IndustrialComment> list = comments.get(comment.getTaskId());
		if (list == null) {
			list = new ArrayList<IndustrialComment>();
		}
		list.add(comment);
		comments.put(comment.getTaskId(), list);

	}

	public String persistTask(TaskRepository repository, IndustrialTask toPersisTask) throws SQLException,
			CoreException {
		String id = "MemoryTask" + (tasks.size() + 1);
		toPersisTask.setTaskId(id);
		tasks.put(id, toPersisTask);
		return id;
	}

	public void updateTask(TaskRepository repository, IndustrialTask task) throws SQLException, CoreException {
		tasks.put(task.getTaskId(), task);
	}

	public boolean validate(TaskRepository repository) throws SQLException, CoreException {
		return true;
	}

}
