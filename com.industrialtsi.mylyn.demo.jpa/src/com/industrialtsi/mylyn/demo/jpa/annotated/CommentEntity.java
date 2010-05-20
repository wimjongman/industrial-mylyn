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
import java.util.Calendar;
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

import com.industrialtsi.mylyn.core.dto.IndustrialComment;

/**
 * @author Ahmed Aadel
 * @since 0.8.0
 * 
 */

@Entity(name = "COMMENTS")
public class CommentEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Transient
	IndustrialComment industrialComment;

	@Id
	@Column(name = "CMT_ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long commentId;

	@Column(name = "CMT_BUG_ID")
	private long taskId;

	@Column(name = "CMT_TEXT")
	@Lob
	private String content;

	@Column(name = "CMT_AUTHOR")
	private String author;

	@Column(name = "CMT_AUTHOR_NAME")
	private String authorName;

	@Column(name = "CMT_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date commentDate;

	@Column(name = "CMT_DESC")
	@Lob
	private String desc;

	public CommentEntity() {
		industrialComment = new IndustrialComment();
	}

	@Transient
	public IndustrialComment getIndustrialComment() {
		industrialComment.setTaskId(taskId + "");
		industrialComment.setText(content);
		industrialComment.setAuthor(author);
		industrialComment.setAuthorName(authorName);
		industrialComment.setDate(commentDate);
		industrialComment.setDescription(desc);
		industrialComment.setGroupKey(commentDate==null?Calendar.getInstance().getTime().toString():commentDate.toString());

		return industrialComment;
	}

	@Transient
	public void setIndustrialComment(IndustrialComment ic) {

		taskId = new Long(ic.getTaskId());
		content = ic.getText();
		author = ic.getAuthor();
		authorName = ic.getAuthorName();
		commentDate = ic.getDate();
		desc = ic.getDescription();

	}

}
