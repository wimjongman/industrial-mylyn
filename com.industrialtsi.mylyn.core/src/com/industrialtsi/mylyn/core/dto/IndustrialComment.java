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

/**
 * This class handles the mapping of database task comments to Mylyn task
 * comments through the CommentsMap.xml Ibatis map file.
 * 
 * @author Maarten Meijer
 * @author Wim Jongman
 */
public class IndustrialComment {

	/**
	 * taskId maps to the task id.
	 */
	private String taskId;

	/**
	 * The comments are grouped by the groupKey if the comment consists of
	 * multiple rows. For each new block of comment you need to supply a new
	 * group key. Can be null if only one block of comments is available.
	 */
	private String groupKey;

	/**
	 * This is the comment block. If the comments are fetched from the database
	 * and more then one row exists for a comment block, as indicated by the
	 * groupKey, then the texts are concatenated with a "\n" separation. Null
	 * values are ignored.
	 */
	/**
	 *
	 */
	public IndustrialComment() {
		super();
		// TODO Auto-generated constructor stub
		this.date = new Date();
	}

	private String text;

	/**
	 * The author of the comment, can be null.
	 */
	private String author;

	/**
	 * The name of the author, can be null.
	 */
	private String authorName;

	/**
	 * The date the comment was added. Can be null.
	 */
	private Date date;

	/**
	 * The description header of the comment. Can be null.
	 */
	private String description;

	/**
	 * @param text
	 *            the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param author
	 *            the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * @param authorName
	 *            the authorName to set
	 */
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	/**
	 * @return the authorName
	 */
	public String getAuthorName() {
		return authorName;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param taskId
	 *            the taskId to set
	 */
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	/**
	 * @return the taskId
	 */
	public String getTaskId() {
		return taskId;
	}

	/**
	 * The groupkey defines one group of comments in case the comments come in
	 * groups of separated strings (rows).
	 * 
	 * @return the groupKey
	 */
	public String getGroupKey() {
		return groupKey;
	}

	/**
	 * The groupkey defines one group of comments in case the comments come in
	 * groups of separated strings (rows).
	 * 
	 * @param groupKey
	 * 
	 */
	public void setGroupKey(String groupKey) {
		this.groupKey = groupKey;
	}
}
