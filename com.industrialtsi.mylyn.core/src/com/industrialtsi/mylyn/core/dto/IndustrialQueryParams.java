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
package com.industrialtsi.mylyn.core.dto;

import java.util.Date;

import com.industrialtsi.mylyn.core.internal.CoreLogger;

/**
 * Allow generic parameters to be passed into an SQL query.
 * <dl>
 * <dt>taskId</dt>
 * <dd>id or range</dd>
 * <dt>product</dt>
 * <dd>VIRTUAL parameter describing the product</dd>
 * <dt>owner</dt>
 * <dd>select from a list of names</dd>
 * <dt>summary</dt>
 * <dd>free text search</dd>
 * <dt>notes</dt>
 * <dd>free text search</dd>
 * <dt>comments</dt>
 * <dd>free text search</dd>
 * <dt>priority (as P1, P2, P3, P4 or P5)</dt>
 * <dd>select from a list of legal priorities</dd>
 * <dt>completed</dt>
 * <dd>before and after</dd>
 * <dt>completionDate (null is not completed)</dt>
 * <dd>before and after</dd>
 * <dt>creationDate</dt>
 * <dd>before and after</dd>
 * <dt>dueDate</dt>
 * <dd>before and after</dd>
 * </dl>
 * 
 * The query is saved as a String in the url field.
 * 
 * 
 * @author Maarten Meijer
 */
public class IndustrialQueryParams extends IndustrialQueryOptions {

	public static final String COMPLETION_AFTER = "completionAfter="; //$NON-NLS-1$

	public static final String COMPLETION_BEFORE = "completionBefore="; //$NON-NLS-1$

	public static final String DUE_AFTER = "dueAfter="; //$NON-NLS-1$

	public static final String DUE_BEFORE = "dueBefore="; //$NON-NLS-1$

	public static final String CREATION_AFTER = "creationAfter="; //$NON-NLS-1$

	public static final String CREATION_BEFORE = "creationBefore="; //$NON-NLS-1$

	public static final String NOTES = "notes="; //$NON-NLS-1$

	public static final String SUMMARY = "summary="; //$NON-NLS-1$

	public static final String COMMENTS = "comments="; //$NON-NLS-1$

	public static final String ISSUE_STATUS = "issueStatus="; //$NON-NLS-1$

	public static final String PRIORITY = "priority="; //$NON-NLS-1$

	public static final String PRODUCT = "product="; //$NON-NLS-1$

	public static final String OWNER = "owner="; //$NON-NLS-1$

	private Date completionDateBefore;

	private Date completionDateAfter;

	private Date creationDateBefore;

	private Date creationDateAfter;

	private Date dueDateBefore;

	private Date dueDateAfter;

	public static final String QUERY_URL = "[IBATIS-QUERY]"; //$NON-NLS-1$


	public IndustrialQueryParams(String url) {
		super();

		if(null != url)
			fromUrl(url);
	}

	public String asUrl() {
		StringBuffer sb = new StringBuffer();

		appendMultiple(sb, owner, OWNER);

		appendMultiple(sb, product, PRODUCT);

		appendMultiple(sb, priority, PRIORITY);

		appendMultiple(sb, issueStatus, ISSUE_STATUS);

		appendSingle(sb, summary, SUMMARY);

		appendSingle(sb, notes, NOTES);

		appendSingle(sb, getComments(), COMMENTS);

		appendDate(sb, creationDateBefore, CREATION_BEFORE);
		
		appendDate(sb, creationDateAfter, CREATION_AFTER);

		appendDate(sb, dueDateBefore, DUE_BEFORE);
		
		appendDate(sb, dueDateAfter, DUE_AFTER);

		appendDate(sb, completionDateBefore, COMPLETION_BEFORE);

		appendDate(sb, completionDateAfter, COMPLETION_AFTER);

		String url = sb.toString().replace("|&", "&"); //$NON-NLS-1$ //$NON-NLS-2$
		return 0 == url.length() ? "" : url.substring(0, url.length() - 1); //$NON-NLS-1$
	}

	private void appendDate(StringBuffer sb, Date date, String prefix) {
		if (null != date) {
			sb.append(prefix);
			sb.append(Long.toString(date.getTime()));
			sb.append("&"); //$NON-NLS-1$
		}

	}

	private void appendMultiple(StringBuffer sb, String[] items, String prefix) {
		if (null != items && items.length != 0) {
			sb.append(prefix);
			for (String item : items) {
				sb.append(item);
				sb.append("|"); //$NON-NLS-1$
			}
			sb.append("&"); //$NON-NLS-1$
		}
	}

	private void appendSingle(StringBuffer sb, String item, String prefix) {
		if (null != item && !"".equals(item)) { //$NON-NLS-1$
			sb.append(prefix);
			sb.append(item);
			sb.append("&"); //$NON-NLS-1$
		}
	}
	
	/**
	 * @param fullUrl
	 * @return
	 */
	public boolean fromUrl(String fullUrl) {

		// FIXME this is a kludge!!
		int index = fullUrl.lastIndexOf(IndustrialQueryParams.QUERY_URL);
		if (index != -1) {
			index += IndustrialQueryParams.QUERY_URL.length();
		} else {
			index = 0;
		}

		String url = fullUrl.substring(index);

		String[] fields = url.split("&"); //$NON-NLS-1$

		for (String field : fields) {
			if (0 == field.trim().length()) {
				; // zero arguments to query
			} else if (field.startsWith(OWNER)) {
				owner = parseMulti(field, OWNER);
			} else if (field.startsWith(PRODUCT)) {
				product = parseMulti(field, PRODUCT);
			} else if (field.startsWith(PRIORITY)) {
				priority = parseMulti(field, PRIORITY);
			} else if (field.startsWith(ISSUE_STATUS)) {
				issueStatus = parseMulti(field, ISSUE_STATUS);
			} else if (field.startsWith(SUMMARY)) {
				summary = field.substring(SUMMARY.length());
			} else if (field.startsWith(NOTES)) {
				notes = field.substring(NOTES.length());
			} else if (field.startsWith(COMMENTS)) {
				setComments(field.substring(COMMENTS.length()));
			} else if (field.startsWith(CREATION_BEFORE)) {
				creationDateBefore = parseDate(field, CREATION_BEFORE);
			} else if (field.startsWith(CREATION_AFTER)) {
				creationDateAfter = parseDate(field, CREATION_AFTER);
			} else if (field.startsWith(COMPLETION_BEFORE)) {
				completionDateBefore = parseDate(field, COMPLETION_BEFORE);
			} else if (field.startsWith(COMPLETION_AFTER)) {
				completionDateAfter = parseDate(field, COMPLETION_AFTER);
			} else if (field.startsWith(DUE_BEFORE)) {
				dueDateBefore = parseDate(field, DUE_BEFORE);
			} else if (field.startsWith(DUE_AFTER)) {
				dueDateAfter = parseDate(field, DUE_AFTER);
			} else {
				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append("Unknown key in query: '"); //$NON-NLS-1$
				stringBuilder.append(field);
				stringBuilder.append("'"); //$NON-NLS-1$
				CoreLogger.logError(stringBuilder.toString(), new IllegalArgumentException(field)); 
			}

		}
		return true;
	}
	
	private Date parseDate(String field, String prefix) {
		long mills = Long.parseLong(field.substring(prefix.length()));
		return new Date(mills);		
	}

	private String[] parseMulti(String field, String prefix) {
		return field.substring(prefix.length()).split("\\|");		 //$NON-NLS-1$
	}

	/**
	 * @return the completionDateBefore
	 */
	public Date getCompletionDateBefore() {
		return completionDateBefore;
	}

	/**
	 * @param completionDateBefore
	 *            the completionDateBefore to set
	 */
	public void setCompletionDateBefore(Date completionDateBefore) {
		this.completionDateBefore = completionDateBefore;
	}

	/**
	 * @return the completionDateAfter
	 */
	public Date getCompletionDateAfter() {
		return completionDateAfter;
	}

	/**
	 * @param completionDateAfter
	 *            the completionDateAfter to set
	 */
	public void setCompletionDateAfter(Date completionDateAfter) {
		this.completionDateAfter = completionDateAfter;
	}

	/**
	 * @return the creationDateBefore
	 */
	public Date getCreationDateBefore() {
		return creationDateBefore;
	}

	/**
	 * @param creationDateBefore
	 *            the creationDateBefore to set
	 */
	public void setCreationDateBefore(Date creationDateBefore) {
		this.creationDateBefore = creationDateBefore;
	}

	/**
	 * @return the creationDateAfter
	 */
	public Date getCreationDateAfter() {
		return creationDateAfter;
	}

	/**
	 * @param creationDateAfter
	 *            the creationDateAfter to set
	 */
	public void setCreationDateAfter(Date creationDateAfter) {
		this.creationDateAfter = creationDateAfter;
	}

	/**
	 * @return the dueDateBefore
	 */
	public Date getDueDateBefore() {
		return dueDateBefore;
	}

	/**
	 * @param dueDateBefore
	 *            the dueDateBefore to set
	 */
	public void setDueDateBefore(Date dueDateBefore) {
		this.dueDateBefore = dueDateBefore;
	}

	/**
	 * @return the dueDateAfter
	 */
	public Date getDueDateAfter() {
		return dueDateAfter;
	}

	/**
	 * @param dueDateAfter
	 *            the dueDateAfter to set
	 */
	public void setDueDateAfter(Date dueDateAfter) {
		this.dueDateAfter = dueDateAfter;
	}
}
