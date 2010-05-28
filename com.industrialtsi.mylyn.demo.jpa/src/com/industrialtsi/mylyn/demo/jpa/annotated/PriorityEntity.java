/*******************************************************************************
 * Copyright (c) 2008,2009,2010 Industrial TSI and Maarten Meijer.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Industrial TSI - initial API and implementation
 *******************************************************************************/
package com.industrialtsi.mylyn.demo.jpa.annotated;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "PRIORITY")
public class PriorityEntity {
	/**
	 * @author Ahmed Aadel
	 * @since 0.8.0
	 * 
	 */
	@Id
	@Column(name = "PRIORITY_ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long priorityId;

	private String priority;

	public long getPriorityId() {
		return priorityId;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

}
