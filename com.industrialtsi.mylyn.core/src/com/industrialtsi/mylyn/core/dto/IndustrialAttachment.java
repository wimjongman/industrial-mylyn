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
 * Mapping for the attributes: <br>
 * <ul>
 * <li>USER_REPORTED</li>
 * <li>USER_ASSIGNED</li>
 * <li>ATTACHMENT_ID = "task.common.attachment.id";</li>
 * <li>ATTACHMENT_TYPE = "task.common.attachment.type";</li>
 * <li>ATTACHMENT_CTYPE = "task.common.attachment.ctype";</li>
 * <li>ATTACHMENT_DATE = "task.common.attachment.date";</li>
 * <li>ATTACHMENT_URL = "task.common.attachment.url";</li>
 * <li>ATTACHMENT_FILENAME = "filename";</li>
 * <li>ATTACHMENT_SIZE = "task.common.attachment.size";</li>
 * </ul>
 * 
 * @author maarten
 * 
 * @since 0.5.0
 */
public class IndustrialAttachment {
	private String id;

	private String description;

	private String ctype;

	private Date date;

	private String url;

	private String filename;

	private String author;

	private Long size;

	private String taskId;

	private byte[] blob;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the ctype
	 */
	public String getCtype() {
		return ctype;
	}

	/**
	 * @param ctype
	 *            the ctype to set
	 */
	public void setCtype(String ctype) {
		this.ctype = ctype;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * @param filename
	 *            the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * @return the size
	 */
	public Long getSize() {
		return size;
	}

	/**
	 * @param l
	 *            the size to set
	 */
	public void setSize(Long l) {
		this.size = l;
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
	 * @param blob
	 *            the blob to set
	 */
	public void setBlob(byte[] blob) {
		this.blob = blob;
	}

	/**
	 * @return the blob
	 */
	public byte[] getBlob() {
		return blob;
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

}
