/*******************************************************************************
 * Copyright (c) 2008,2009,2010 Industrial TSI and Maarten Meijer.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Maarten Meijer - initial API and implementation
 *******************************************************************************/
package com.industrialtsi.mylyn.core.dto;

import java.util.Date;

/**
 * @author Maarten Meijer
 */
/**
 * <code>IndustrialTask</code> : TODO describe.
 * 
 * @author maarten
 * 
 */
public class IndustrialTask {
	
	// FIXME add resolution to model

	private String product;

	private String issueStatus;

	private Date completionDate;

	private Date creationDate;

	private Date dueDate;

	private Date modificationDate;

	private Date scheduledForDate;

	private String owner;

	private String reporter;

	private String priority;

	private String summary;

	private String taskKey;

	private String taskKind;

	private String url;

	private int estimatedTimeHours;

	private boolean active;

	private String notes;

	private String taskId;

	/**
	 * @param url
	 * @param taskId
	 * @param summary
	 */
	public IndustrialTask(String url, String taskId, String summary) {
		this.url = url;
		// TODO Auto-generated constructor stub
		this.taskId = taskId;
		this.summary = summary;
	}

	/**
	 * Many repositories have some sort of notion of a product. This is an
	 * abstraction for that as Mylyn does not provide that in its generic model.
	 * 
	 * @param product
	 *            the product to set
	 */
	public void setProduct(String product) {
		this.product = product;
	}

	/**
	 * Many repositories have some sort of notion of a product. This is an
	 * abstraction for that as Mylyn does not provide that in its generic model.
	 * 
	 * @return the product
	 */
	public String getProduct() {
		return product;
	}

	/**
	 * @param issueStatus
	 *            the issueStatus to set
	 */
	public void setIssueStatus(String issueStatus) {
		this.issueStatus = issueStatus;
	}

	/**
	 * @return the issueStatus
	 */
	public String getIssueStatus() {
		return issueStatus;
	}

	/**
	 * @return completion date or null
	 */
	public final Date getCompletionDate() {
		return completionDate;
	}

	/**
	 * @param completionDate
	 */
	public final void setCompletionDate(Date completionDate) {
		this.completionDate = completionDate;
	}

	/**
	 * @return creation date or null
	 */
	public final Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate
	 */
	public final void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @return due date or null
	 */
	public final Date getDueDate() {
		return dueDate;
	}

	/**
	 * @param dueDate
	 */
	public final void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	/**
	 * @return modification date or null
	 */
	public final Date getModificationDate() {
		return modificationDate;
	}

	/**
	 * @param modificationDate
	 */
	public final void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}

	/**
	 * @return owner or null
	 */
	public final String getOwner() {
		return owner;
	}

	/**
	 * @param owner
	 */
	public final void setOwner(String owner) {
		this.owner = owner;
	}

	/**
	 * @return priority as P1, P2, P3, P4 or P5
	 */
	public final String getPriority() {
		return priority;
	}

	/**
	 * @param priority
	 */
	public final void setPriority(String priority) {
		this.priority = priority;
	}

	/**
	 * @return summary or null
	 * 
	 *         TODO consider returning EMPTY
	 */
	public final String getSummary() {
		return summary;
	}

	/**
	 * @param summary
	 */
	public final void setSummary(String summary) {
		this.summary = summary;
	}

	/**
	 * @return task key
	 */
	public final String getTaskKey() {
		return taskKey;
	}

	/**
	 * @param taskKey
	 */
	public final void setTaskKey(String taskKey) {
		this.taskKey = taskKey;
	}

	/**
	 * @return task kind
	 */
	public final String getTaskKind() {
		return taskKind;
	}

	/**
	 * @param taskKind
	 */
	public final void setTaskKind(String taskKind) {
		this.taskKind = taskKind;
	}

	/**
	 * @return url
	 */
	public final String getUrl() {
		return url;
	}

	/**
	 * @param url
	 */
	public final void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @param estimatedTimeHours
	 */
	public void setEstimatedTimeHours(int estimatedTimeHours) {
		this.estimatedTimeHours = estimatedTimeHours;
	}

	/**
	 * @return estimated hours or 0
	 */
	public int getEstimatedTimeHours() {
		return estimatedTimeHours;
	}

	/**
	 * @param active
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * @return true when active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param notes
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}

	/**
	 * @return notes or NULL
	 */
	public String getNotes() {
		return notes;
	}

	/**
	 * @param taskId
	 */
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	/**
	 * @return task id
	 */
	public String getTaskId() {
		return taskId;
	}

	/**
	 * @param scheduledForDate
	 */
	public void setScheduledForDate(Date scheduledForDate) {
		this.scheduledForDate = scheduledForDate;
	}

	/**
	 * @return scheduled for date
	 */
	public Date getScheduledForDate() {
		return scheduledForDate;
	}

	/**
	 * @param reporter
	 */
	public void setReporter(String reporter) {
		this.reporter = reporter;
	}

	/**
	 * @return reporter name or null
	 */
	public String getReporter() {
		return reporter;
	}
}
