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
package com.industrialtsi.mylyn.core.internal;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Date;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.AbstractTaskAttachmentHandler;
import org.eclipse.mylyn.tasks.core.data.AbstractTaskAttachmentSource;
import org.eclipse.mylyn.tasks.core.data.TaskAttachmentMapper;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;

import com.industrialtsi.mylyn.core.IndustrialCore;
import com.industrialtsi.mylyn.core.dto.IndustrialAttachment;
import com.industrialtsi.mylyn.core.persistence.PersistorsManager;

public class IndustrialAttachmentHandler extends AbstractTaskAttachmentHandler {

	private static final String TRUE = "true"; //$NON-NLS-1$

	@Override
	public boolean canGetContent(TaskRepository repository, ITask task) {
		// defaults to false
		String can = repository.getProperty(IndustrialCore.CAN_GET_ATTACHMENTS);
		return (null != can && TRUE.equalsIgnoreCase(can));
	}

	@Override
	public boolean canPostContent(TaskRepository repository, ITask task) {
		// defaults to false
		String can = repository.getProperty(IndustrialCore.CAN_POST_ATTACHMENTS);
		return (null != can && TRUE.equalsIgnoreCase(can));
	}

	@Override
	public InputStream getContent(TaskRepository repository, ITask task, TaskAttribute attachmentAttribute,
			IProgressMonitor monitor) throws CoreException {
		assert (null != repository);

		TaskAttachmentMapper taskAttachment = TaskAttachmentMapper.createFrom(attachmentAttribute);
		String url = taskAttachment.getUrl();
		if (null == url || "".equals(url)) { //$NON-NLS-1$
//			System.out.println("IndustrialAttachmentHandler.getContent() retrieve from BLOB"); //$NON-NLS-1$

			// TODO read the BLOB from the database and serve as stream
			// FIXME get a Map directly from repository, fall back to object
			// when null
			try {
				IndustrialAttachment params = new IndustrialAttachment();

				params.setId(taskAttachment.getAttachmentId());
				params.setDate(new Date());
				params.setCtype(taskAttachment.getContentType());
				params.setFilename(taskAttachment.getFileName());
				params.setDescription(taskAttachment.getDescription());
				params.setAuthor(IndustrialCore.getDefault().getLocalUserName(repository));

				byte[] blob = PersistorsManager.getManager().getPersistor(repository).fetchAttachmentBlob(repository,
						taskAttachment.getAttachmentId());
				params.setBlob(blob);
				assert (params.getBlob() != null);

				InputStream in = new ByteArrayInputStream(params.getBlob());
				return in;
			} catch (SQLException e) {
				// TODO package in Status and throw CoreException
				IStatus status = CoreLogger.createStatus(IStatus.ERROR,
						"An SQL problem occured with reading an attachment from " + repository.getUrl(), e); //$NON-NLS-1$
				CoreLogger.log(status);
				throw new CoreException(status);
			}

		} else {
//			System.out.println("IndustrialAttachmentHandler.getContent() retrieve from URL"); //$NON-NLS-1$

		}

		return null;
	}

	@Override
	public void postContent(TaskRepository repository, ITask task, AbstractTaskAttachmentSource source, String comment,
			TaskAttribute attachmentAttribute, IProgressMonitor monitor) throws CoreException {
//		System.out.println("IndustrialAttachmentHandler.postContent()"); //$NON-NLS-1$

		try {
			IndustrialAttachment params = new IndustrialAttachment();
			params.setTaskId(task.getTaskId());
			params.setDate(new Date());

			if (null == attachmentAttribute) { // it is a context
				params.setCtype(source.getContentType());
				params.setFilename(source.getName());
				params.setDescription(source.getDescription());

			} else { // it is a file
				TaskAttachmentMapper taskAttachment = TaskAttachmentMapper.createFrom(attachmentAttribute);

				params.setCtype(taskAttachment.getContentType());
				params.setFilename(taskAttachment.getFileName());
				params.setDescription(taskAttachment.getDescription());
			}

			params.setSize(source.getLength());

			params.setUrl(""); // is file based so store in BLOB //$NON-NLS-1$

			// TODO get individual name of submitter
			params.setAuthor(IndustrialCore.getDefault().getLocalUserName(repository));

			InputStream in = source.createInputStream(monitor);

			byte[] blob = new byte[(int) source.getLength()];
			int read = in.read(blob, 0, (int) source.getLength());
			assert read == source.getLength();
			params.setBlob(blob);

			// params);

			PersistorsManager.getManager().getPersistor(repository).persistAttachment(repository, params);

		} catch (SQLException e) {
			// TODO package in Status and throw CoreException
			IStatus status = CoreLogger.createStatus(IStatus.ERROR,
					"An SQL problem occured with writing an attachment on " + repository.getUrl(), e); //$NON-NLS-1$
			CoreLogger.log(status);
			throw new CoreException(status);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			IStatus status = CoreLogger.createStatus(IStatus.ERROR,
					"An File IO problem occured with writing an attachment on " + repository.getUrl(), e); //$NON-NLS-1$
			CoreLogger.log(status);
			throw new CoreException(status);
		}

	}

}
