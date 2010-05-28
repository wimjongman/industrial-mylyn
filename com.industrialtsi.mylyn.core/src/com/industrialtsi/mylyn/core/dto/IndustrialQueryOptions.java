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

/**
 * @author Maarten Meijer
 */
public class IndustrialQueryOptions {

	private static final String[] EMPTY = new String[0];

	private static final String EMPTYSTRING = ""; //$NON-NLS-1$

	protected String[] owner;

	protected String[] product;

	protected String[] issueStatus;

	protected String summary;

	protected String notes;

	private String comments;

	protected String[] priority;

	public final String[] getOwner() {
		return owner;
	}

	public final String[] getProduct() {
		return product;
	}

	public final String[] getIssueStatus() {
		return issueStatus;
	}

	public final String[] getPriority() {
		return priority;
	}

	public final void setCompleted(String completed) {
		this.completed = completed;
	}

	protected String completed;


	/**
	 * 
	 */
	public IndustrialQueryOptions() {
		super();
	}

	/**
	 * @return the owner
	 */
	public String[] getOwnerOrEmpty() {
		return null != owner ? owner : EMPTY;
	}

	/**
	 * @param owner
	 *            the owner to set
	 */
	public void setOwner(String[] owner) {
		this.owner = owner;
	}

	/**
	 * @return the summary
	 */
	public String getSummary() {
		return summary;
	}

	/**
	 * @return the summary
	 */
	public String getSummaryOrEmpty() {
		return null != summary ? summary : EMPTYSTRING;
	}

	/**
	 * @param summary
	 *            the summary to set
	 */
	public void setSummary(String summary) {
		this.summary = summary;
	}

	/**
	 * @return the notes
	 */
	public String getNotes() {
		return notes;
	}

	/**
	 * @return the notes
	 */
	public String getNotesOrEmpty() {
		return null != notes ? notes : EMPTYSTRING;
	}

	/**
	 * @param notes
	 *            the notes to set
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}

	/**
	 * @param comments the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * @return the comments
	 */
	public String getCommentsOrEmpty() {
		return null != comments ? comments : EMPTYSTRING;
	}
	
	/**
	 * @return the priority
	 */
	public String[] getPriorityOrEmpty() {
		return null != priority ? priority : EMPTY;
	}

	/**
	 * @param priority
	 *            the priority to set
	 */
	public void setPriority(String[] priority) {
		this.priority = priority;
	}

	/**
	 * @return the completed
	 * @deprecated
	 */
	@Deprecated
	public String getCompleted() {
		return completed;
	}

	/**
	 * @param completed
	 *            the completed to set
	 * @deprecated
	 */
	@Deprecated
	public void setCompleted(boolean completed) {
		this.completed = completed ? "true" : "false";  //$NON-NLS-1$//$NON-NLS-2$
	}

	/**
	 * @param product
	 *            the product to set
	 */
	public void setProduct(String[] product) {
		this.product = product;
	}

	/**
	 * @return the product
	 */
	public String[] getProductOrEmpty() {
		return null != product ? product : EMPTY;
	}

	/**
	 * @param issueStatus
	 *            the issueStatus to set
	 */
	public void setIssueStatus(String[] issueStatus) {
		this.issueStatus = issueStatus;
	}

	/**
	 * @return the issueStatus
	 */
	public String[] getIssueStatusOrEmpty() {
		return null != issueStatus ? issueStatus : EMPTY;
	}

}