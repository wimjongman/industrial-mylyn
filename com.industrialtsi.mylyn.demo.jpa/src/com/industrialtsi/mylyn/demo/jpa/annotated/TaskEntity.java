/*******************************************************************************
 * Copyright (c) 2008,2009,2010 Industrial TSI and Maarten Meijer.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Industrial TSI - initial API and implementation
 *******************************************************************************/
package com.industrialtsi.mylyn.demo.jpa.annotated;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.industrialtsi.mylyn.core.dto.IndustrialTask;

/**
 * @author Ahmed Aadel
 * @since 0.8.0
 * 
 */
@Entity(name = "ISSUES")
public class TaskEntity implements Serializable {

	@Transient
	private IndustrialTask industrialTask;

	@Id
	@Column(name = "BUG_ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long taskId;

	@Column(name = "BUG_OWNER")
	private String owner;

	@Column(name = "BUG_PRODUCT")
	private String product;

	@Column(name = "BUG_SUMMARY")
	private String summary;

	@Column(name = "BUG_PRIORITY")
	private String priority;

	@Column(name = "BUG_STATUS")
	String status;

	@Column(name = "BUG_NOTES")
	private String notes;

	@Column(name = "BUG_CREATED")
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;

	@Column(name = "BUG_SCHEDULED")
	@Temporal(TemporalType.TIMESTAMP)
	private Date scheduledDate;

	@Column(name = "BUG_DUE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dueDate;

	@Column(name = "BUG_CLOSED")
	@Temporal(TemporalType.TIMESTAMP)
	private Date completionDate;

	@Column(name = "BUG_TIME_ESTIMATED")
	private int estimatedTime;

	@Column(name = "BUG_TIME_ACTUAL")
	private int actualTime;

	private static final long serialVersionUID = 1L;

	public TaskEntity() {
		industrialTask = new IndustrialTask("", taskId + "", "");
	}

	@Transient
	public IndustrialTask getIndustrialTask() {

		this.industrialTask.setTaskId(taskId + "");
		this.industrialTask.setProduct(product);
		this.industrialTask.setCompletionDate(completionDate);
		this.industrialTask.setCreationDate(creationDate);
		this.industrialTask.setOwner(owner);
		this.industrialTask.setSummary(summary);
		this.industrialTask.setPriority(priority);
		this.industrialTask.setIssueStatus(status);
		this.industrialTask.setScheduledForDate(scheduledDate);
		this.industrialTask.setDueDate(dueDate);
		this.industrialTask.setEstimatedTimeHours(estimatedTime);
		this.industrialTask.setNotes(notes);

		return industrialTask;
	}

	@Transient
	public void setIndustrialTask(IndustrialTask it) {

		this.industrialTask = it;
		this.product = it.getProduct();
		this.creationDate = Calendar.getInstance().getTime();
		this.owner = it.getOwner();
		this.summary = it.getSummary();
		this.priority = it.getPriority();
		this.status = it.getIssueStatus();
		this.completionDate = it.getCompletionDate();
		this.scheduledDate = it.getScheduledForDate();
		this.dueDate = it.getDueDate();
		this.estimatedTime = it.getEstimatedTimeHours() / 60;
		this.notes = it.getNotes();

	}

	@Transient
	public void update(IndustrialTask it) {
		this.taskId = new Long(it.getTaskId());
		setIndustrialTask(it);

	}
}
