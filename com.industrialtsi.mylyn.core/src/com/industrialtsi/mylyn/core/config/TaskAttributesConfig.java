/*******************************************************************************
 * Copyright (c) 2008 Industrial TSI and Maarten Meijer.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Maarten Meijer - initial API and implementation   
 */
package com.industrialtsi.mylyn.core.config;

public class TaskAttributesConfig {
	
	private boolean summary;
	
	private boolean product;
	
	private boolean status;
	
	private boolean userAssigned;
	
	private boolean dateDue;
	
	private boolean dateCreated;
	
	/**
	 * @return <code>true</code> if summary is readonly
	 */
	public final boolean isSummary() {
		return summary;
	}

	/**
	 * @param summary the summary to set
	 */
	public final void setSummary(boolean summary) {
		this.summary = summary;
	}

	/**
	 * @return <code>true</code> if product is readonly
	 */
	public final boolean isProduct() {
		return product;
	}

	/**
	 * @param product the product to set
	 */
	public final void setProduct(boolean product) {
		this.product = product;
	}

	/**
	 * @return <code>true</code> if status is readonly
	 */
	public final boolean isStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public final void setStatus(boolean status) {
		this.status = status;
	}

	/**
	 * @return <code>true</code> if userAssigned is readonly
	 */
	public final boolean isUserAssigned() {
		return userAssigned;
	}

	/**
	 * @param userAssigned the userAssigned to set
	 */
	public final void setUserAssigned(boolean userAssigned) {
		this.userAssigned = userAssigned;
	}

	/**
	 * @return <code>true</code> if dateDue is readonly
	 */
	public final boolean isDateDue() {
		return dateDue;
	}

	/**
	 * @param dateDue the dateDue to set
	 */
	public final void setDateDue(boolean dateDue) {
		this.dateDue = dateDue;
	}

	/**
	 * @return <code>true</code> if dateCreated is readonly
	 */
	public final boolean isDateCreated() {
		return dateCreated;
	}

	/**
	 * @param dateCreated the dateCreated to set
	 */
	public final void setDateCreated(boolean dateCreated) {
		this.dateCreated = dateCreated;
	}

	/**
	 * @return <code>true</code> if dateModified is readonly
	 */
	public final boolean isDateModified() {
		return dateModified;
	}

	/**
	 * @param dateModified the dateModified to set
	 */
	public final void setDateModified(boolean dateModified) {
		this.dateModified = dateModified;
	}

	/**
	 * @return <code>true</code> if priority is readonly
	 */
	public final boolean isPriority() {
		return priority;
	}

	/**
	 * @param priority the priority to set
	 */
	public final void setPriority(boolean priority) {
		this.priority = priority;
	}

	/**
	 * @return <code>true</code> if description is readonly
	 */
	public final boolean isDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public final void setDescription(boolean description) {
		this.description = description;
	}

	/**
	 * @return <code>true</code> if reporter is readonly
	 */
	public final boolean isReporter() {
		return reporter;
	}

	/**
	 * @param reporter the reporter to set
	 */
	public final void setReporter(boolean reporter) {
		this.reporter = reporter;
	}

	private boolean dateModified;
	
	private boolean priority;
	
	private boolean description;
	
	private boolean reporter;

}
