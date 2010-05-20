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
package com.industrialtsi.mylyn.demo.jpa.annotated;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.industrialtsi.mylyn.core.dto.IndustrialAttachment;

/**
 * @author Ahmed Aadel
 * @since 0.8.0
 * 
 */
@Entity(name = "ATTACHMENTS")
public class AttachmentEntity implements Serializable {

	@Id
	@Column(name = "ATT_ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long attachmentId;

	private static final long serialVersionUID = 1L;

	@Transient
	private IndustrialAttachment industrialAttachment;

	@Column(name = "ATT_TASK")
	private String taskId;

	@Column(name = "ATT_DESC")
	private String description;

	@Column(name = "ATT_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	@Column(name = "ATT_URL")
	private String url;

	@Column(name = "ATT_CTYPE")
	private String ctype;

	@Column(name = "ATT_FILENAME")
	private String filename;

	@Column(name = "ATT_NAME")
	private String name;

	@Column(name = "ATT_SIZE")
	private Long size;

	@Column(name = "ATT_BLOB")
	@Lob
	private byte[] blob;

	@Transient
	private String author = "Unknown";

	public AttachmentEntity() {
		industrialAttachment = new IndustrialAttachment();
	}

	@Transient
	public IndustrialAttachment getIndustrialAttachment() {
		industrialAttachment.setTaskId(taskId);
		industrialAttachment.setAuthor(author);
		industrialAttachment.setId(attachmentId + "");
		industrialAttachment.setDescription(description);
		industrialAttachment.setDate(date);
		industrialAttachment.setUrl(url);
		industrialAttachment.setCtype(ctype);
		industrialAttachment.setFilename(filename);
		industrialAttachment.setSize(size);
		industrialAttachment.setBlob(blob);

		return industrialAttachment;
	}

	@Transient
	public void setIndustrialAttachment(IndustrialAttachment ia) {

		this.industrialAttachment = ia;
		author = ia.getAuthor();
		this.taskId = ia.getTaskId();
		this.description = ia.getDescription();
		this.date = ia.getDate();
		this.url = ia.getUrl();
		this.ctype = ia.getCtype();
		this.filename = ia.getFilename();
		this.size = ia.getSize();
		this.blob = ia.getBlob();

	}

	@Transient
	public byte[] getBlob() {
		return blob;
	}

}
