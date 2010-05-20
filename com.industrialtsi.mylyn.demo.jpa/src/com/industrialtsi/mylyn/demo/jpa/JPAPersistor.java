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
package com.industrialtsi.mylyn.demo.jpa;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.mylyn.tasks.core.TaskRepository;

import com.industrialtsi.mylyn.core.dto.IndustrialAttachment;
import com.industrialtsi.mylyn.core.dto.IndustrialComment;
import com.industrialtsi.mylyn.core.dto.IndustrialQueryParams;
import com.industrialtsi.mylyn.core.dto.IndustrialTask;
import com.industrialtsi.mylyn.core.persistence.PersistorAdapter;
import com.industrialtsi.mylyn.demo.jpa.annotated.AttachmentEntity;
import com.industrialtsi.mylyn.demo.jpa.annotated.CommentEntity;
import com.industrialtsi.mylyn.demo.jpa.annotated.TaskEntity;

/**
 * JPA Implementation of the interface
 * <code>com.industrialtsi.mylyn.core.persistence.IPersistor</code>
 * 
 * @author Ahmed Aadel
 * @since 0.8.0
 * 
 */
public class JPAPersistor extends PersistorAdapter {

	private EntityManager entityManager;

	private final Model model;

	public JPAPersistor() {
		model = new Model();
		// drop and create all tables anew for demo sake. Handy for first time
		// to create database and populate it. Comment it to avoid this
		// repetitive behavior.
		model.init();
	}

	public void persistAttachment(TaskRepository repository, IndustrialAttachment a) throws SQLException, CoreException {
		AttachmentEntity attachmentEntity = new AttachmentEntity();
		attachmentEntity.setIndustrialAttachment(a);
		openTransaction();
		entityManager.persist(attachmentEntity);
		closeTransaction();
	}

	public void persistComment(TaskRepository repository, IndustrialComment ic) throws SQLException, CoreException {
		CommentEntity commentEntity = new CommentEntity();
		commentEntity.setIndustrialComment(ic);
		openTransaction();
		entityManager.persist(commentEntity);
		closeTransaction();
	}

	public String persistTask(TaskRepository repository, IndustrialTask it) throws SQLException, CoreException {
		TaskEntity taskEntity = new TaskEntity();
		taskEntity.setIndustrialTask(it);
		openTransaction();
		entityManager.persist(taskEntity);
		closeTransaction();
		return taskEntity.getIndustrialTask().getTaskId();
	}

	@SuppressWarnings("unchecked")
	public List<IndustrialComment> fetchComments(TaskRepository repository, String... keys) throws SQLException,
			CoreException {
		openTransaction();
		List<CommentEntity> comments = entityManager.createQuery(
				"select c from COMMENTS c where c.taskId=" + new Long(keys[0])).getResultList();
		closeTransaction();
		List<IndustrialComment> list = new ArrayList<IndustrialComment>();
		if (!comments.isEmpty()) {
			for (CommentEntity c : comments)
				list.add(c.getIndustrialComment());
		}
		return list;
	}

	public IndustrialTask fetchTask(TaskRepository repository, String... key) throws SQLException, CoreException {
		openTransaction();
		TaskEntity t = entityManager.find(TaskEntity.class, new Long(key[0]));
		entityManager.refresh(t);
		closeTransaction();
		return t.getIndustrialTask();
	}

	@SuppressWarnings("unchecked")
	public List<String> findTasks(TaskRepository repository, IndustrialQueryParams criteria) throws SQLException,
			CoreException {
		if (noCriteriaFound(criteria)) {
			// all tasks id's are returned as we don't get any search criteria
			// specified.
			openTransaction();
			List<String> r = stringify(entityManager.createQuery("select i.taskId from ISSUES i").getResultList());
			closeTransaction();
			return r;
		}
		// build a JPQL query to apply to the search criteria we have.
		String and = " and ";
		String queryString = "select i.taskId from ISSUES i where ";
		if (criteria.getOwner() != null)
			queryString += " i.owner in " + sqlArrayFormat(criteria.getOwner()) + and;
		if (criteria.getIssueStatus() != null)
			queryString += " i.status in " + sqlArrayFormat(criteria.getIssueStatus()) + and;
		if (criteria.getProduct() != null)
			queryString += " i.product in " + sqlArrayFormat(criteria.getProduct()) + and;
		if (criteria.getPriority() != null)
			queryString += " i.priority in " + sqlArrayFormat(criteria.getPriority()) + and;
		if (criteria.getSummary() != null)
			queryString += " lower(i.summary) like '%" + criteria.getSummary().toLowerCase() + "%'" + and;
		if (criteria.getCreationDateBefore() != null)
			queryString += " i.creationDate < ?1" + and;
		if (criteria.getCreationDateAfter() != null)
			queryString += " i.creationDate > ?2" + and;
		if (criteria.getCompletionDateBefore() != null)
			queryString += " i.completionDate < ?3" + and;
		if (criteria.getCompletionDateAfter() != null)
			queryString += " i.completionDate > ?4" + and;
		if (criteria.getDueDateBefore() != null)
			queryString += " i.dueDate < ?5" + and;
		if (criteria.getDueDateAfter() != null)
			queryString += " i.dueDate > ?6";

		openTransaction();
		Query query = entityManager.createQuery(trimLastAnd(queryString));
		if (queryString.contains("?1"))
			query.setParameter(1, criteria.getCreationDateBefore());
		if (queryString.contains("?2"))
			query.setParameter(2, criteria.getCreationDateAfter());
		if (queryString.contains("?3"))
			query.setParameter(3, criteria.getCompletionDateBefore());
		if (queryString.contains("?4"))
			query.setParameter(4, criteria.getCompletionDateAfter());
		if (queryString.contains("?5"))
			query.setParameter(5, criteria.getDueDateBefore());
		if (queryString.contains("?6"))
			query.setParameter(6, criteria.getDueDateAfter());
		List list = query.getResultList();
		closeTransaction();

		return stringify(list);

	}

	private String trimLastAnd(String queryString) {
		int length = queryString.trim().length();
		if ("and".equalsIgnoreCase(queryString.substring(length - 3, length)))
			return queryString.substring(0, length - 3);
		return queryString;
	}

	private boolean noCriteriaFound(IndustrialQueryParams c) {
		return (c.getCompletionDateAfter() == null && c.getCompletionDateBefore() == null
				&& c.getCreationDateAfter() == null && c.getCreationDateBefore() == null && c.getOwner() == null
				&& c.getSummary() == null && c.getIssueStatus() == null && c.getPriority() == null
				&& c.getProduct() == null && c.getDueDateAfter() == null && c.getDueDateBefore() == null);
	}

	private List<String> stringify(List<Object> l) {
		List<String> list = new ArrayList<String>();
		for (Object o : l)
			list.add(o + "");
		return list;
	}

	private String sqlArrayFormat(String[] ss) {
		String sa = "(";
		for (String s : ss) {
			sa += "'" + s + "'" + ",";
		}
		return sa.substring(0, sa.length() - 1) + ")";
	}

	@SuppressWarnings("unchecked")
	public List<String> getLegalIssueStatus(TaskRepository repository) throws SQLException, CoreException {
		openTransaction();
		List<String> list = entityManager.createQuery("select distinct s.status from STATUS s").getResultList();
		closeTransaction();
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<String> getLegalOwners(TaskRepository repository) throws SQLException, CoreException {
		openTransaction();
		List<String> list = entityManager.createQuery("select distinct i.owner from ISSUES i").getResultList();
		closeTransaction();
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<String> getLegalPriorities(TaskRepository repository) throws SQLException, CoreException {
		openTransaction();
		List<String> list = entityManager.createQuery("select distinct p.priority from PRIORITY p").getResultList();
		closeTransaction();
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<String> getLegalProducts(TaskRepository repository) throws SQLException, CoreException {
		openTransaction();
		List<String> list = entityManager.createQuery("select distinct p.product from PRODUCTS p").getResultList();
		closeTransaction();
		return list;
	}

	public void updateTask(TaskRepository repository, IndustrialTask it) throws SQLException, CoreException {
		openTransaction();
		TaskEntity taskEntity = entityManager.find(TaskEntity.class, new Long(it.getTaskId()));
		taskEntity.setIndustrialTask(it);
		entityManager.flush();
		closeTransaction();

	}

	public byte[] fetchAttachmentBlob(TaskRepository repository, String attachmentId) throws SQLException,
			CoreException {
		openTransaction();
		AttachmentEntity attachmentEntity = entityManager.find(AttachmentEntity.class, new Long(attachmentId));
		byte[] blob = attachmentEntity.getBlob();
		closeTransaction();
		return blob;
	}

	@SuppressWarnings("unchecked")
	public List<IndustrialAttachment> fetchAttachments(TaskRepository repository, String... key) throws SQLException,
			CoreException {
		openTransaction();
		List<AttachmentEntity> as = entityManager.createQuery(
				"select a from ATTACHMENTS a where a.taskId=" + new Long(key[0])).getResultList();
		closeTransaction();
		List<IndustrialAttachment> list = new ArrayList<IndustrialAttachment>();
		if (!as.isEmpty()) {
			for (AttachmentEntity a : as) {
				list.add(a.getIndustrialAttachment());
			}
		}
		return list;
	}

	public boolean validate(TaskRepository repository) throws SQLException, CoreException {
		// put some serious code here to validate your repository configuration
		return true;
	}

	public Map<String, String> fetchAdditional(TaskRepository repository, String... keys) throws SQLException,
			CoreException {
		openTransaction();
		TaskEntity te = entityManager.find(TaskEntity.class, new Long(keys[0]));
		entityManager.refresh(te);
		closeTransaction();
		Map<String, String> additional = new HashMap<String, String>();
		additional.put("task_common_user_reporter_name", te.getIndustrialTask().getOwner());
		return additional;
	}

	private void openTransaction() {
		if (entityManager == null)
			entityManager = model.getEntityManager();
		entityManager.getTransaction().begin();
	}

	private void closeTransaction() {
		entityManager.getTransaction().commit();
		// entityManager.close();

	}

	public void dispose() {
		if (entityManager.isOpen())
			entityManager.close();
		model.dispose();
	}

	public boolean canInitialize(TaskRepository repository) throws SQLException, CoreException {
		// TODO Add your initialization check code here
		return false;
	}

	public void initialize(TaskRepository repository) throws SQLException, CoreException {
		// TODO Add your initialization code here

	}

}
