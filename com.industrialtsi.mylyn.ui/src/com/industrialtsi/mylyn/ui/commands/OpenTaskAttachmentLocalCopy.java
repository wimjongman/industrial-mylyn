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

package com.industrialtsi.mylyn.ui.commands;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector;
import org.eclipse.mylyn.tasks.core.ITaskAttachment;
import org.eclipse.mylyn.tasks.core.data.AbstractTaskAttachmentHandler;
import org.eclipse.mylyn.tasks.ui.TasksUi;
import org.eclipse.mylyn.tasks.ui.TasksUiUtil;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * <code>OpenTaskAttachmentLocalCopy</code> : open a local copy of an
 * TaskAttchment without URL.<br />
 * Created in response to: bug 249021: OpenTaskAttachmentXXXHandlers do not work
 * for stream based attachments, only for URL based
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=2490211
 * 
 * @author maarten
 * 
 */
public class OpenTaskAttachmentLocalCopy extends AbstractHandler {

	private static final String DOT = "."; //$NON-NLS-1$
	private static final String EMPTY = ""; //$NON-NLS-1$

	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (selection instanceof IStructuredSelection) {
			List<?> items = ((IStructuredSelection) selection).toList();
			for (Object item : items) {
				if (item instanceof ITaskAttachment) {
					ITaskAttachment attachment = (ITaskAttachment) item;
					String url = (attachment).getUrl();
					if (null == url || EMPTY.equals(url)) {
						// download the file and create a file URL to it
						url = createLocalTempFile(attachment);
					}

					TasksUiUtil.openUrl(url);
				}
			}
		}
		return null;
	}

	private String createLocalTempFile(ITaskAttachment attachment) throws ExecutionException {
		// ignore
		AbstractRepositoryConnector connector = TasksUi.getRepositoryManager().getRepositoryConnector(
				attachment.getConnectorKind());

		AbstractTaskAttachmentHandler handler = connector.getTaskAttachmentHandler();

		if (handler.canGetContent(attachment.getTaskRepository(), attachment.getTask())) {
			// TODO should this be in a runnable with progress??
			try {
				String full = attachment.getFileName();
				int dot = full.lastIndexOf(DOT);
				String name = full.substring(0, dot);
				String extension = full.substring(dot);

				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append("cached-"); //$NON-NLS-1$
				stringBuilder.append(name);
				stringBuilder.append(DOT);
				File temp = File.createTempFile(stringBuilder.toString(), extension); 

				FileOutputStream copy = new FileOutputStream(temp);
				InputStream remote = handler.getContent(attachment.getTaskRepository(), attachment.getTask(),
						attachment.getTaskAttribute(), new NullProgressMonitor());

				byte[] buffer = new byte[2048];
				int bytesRead;
				while ((bytesRead = remote.read(buffer)) >= 0) {
					copy.write(buffer, 0, bytesRead);
				}
				copy.close();
				remote.close();

				return temp.toURL().toString();
			} catch (IOException e) {
				throw new ExecutionException("Cannot create local copy of " + attachment.getFileName(), e); //$NON-NLS-1$
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				throw new ExecutionException("Cannot get content of remote " + attachment.getFileName(), e); //$NON-NLS-1$
			}
		}

		return null;
	}


}
