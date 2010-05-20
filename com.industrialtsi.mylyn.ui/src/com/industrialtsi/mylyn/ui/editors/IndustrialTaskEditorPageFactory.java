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
package com.industrialtsi.mylyn.ui.editors;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.mylyn.tasks.ui.TasksUiUtil;
import org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPageFactory;
import org.eclipse.mylyn.tasks.ui.editors.TaskEditor;
import org.eclipse.mylyn.tasks.ui.editors.TaskEditorInput;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.editor.IFormPage;

import com.industrialtsi.mylyn.core.IndustrialCore;
import com.industrialtsi.mylyn.ui.IndustrialUI;

public class IndustrialTaskEditorPageFactory extends AbstractTaskEditorPageFactory {

	private static final String INDUSTRIAL_LOGO = "icons/industrial-tsi.ico"; //$NON-NLS-1$
	private ImageRegistry registry = null;

	private Image getPageImage(String name) {
		if(null == registry) {
			registry = new ImageRegistry(Display.getCurrent());
		}
		if (null == registry.get(name)) {
			ImageDescriptor image = IndustrialUI.imageDescriptorFromPlugin(IndustrialUI.PLUGIN_ID, name);
			registry.put(name, image);
		}
		
		return registry.get(name);
	}
	
	public IndustrialTaskEditorPageFactory() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean canCreatePageFor(TaskEditorInput input) {
		if (input.getTask().getConnectorKind().equals(IndustrialCore.CONNECTOR_KIND)) {
			return true;
		} else if (TasksUiUtil.isOutgoingNewTask(input.getTask(), IndustrialCore.CONNECTOR_KIND)) {
			return true;
		}
		return false;
	}

	@Override
	public Image getPageImage() {
		return getPageImage(INDUSTRIAL_LOGO);
	}

	@Override
	public String getPageText() {
		return IndustrialCore.REPOSITORY_SHORT;
	}

	@Override
	public IFormPage createPage(TaskEditor parentEditor) {
		return new IndustrialEditorPage(parentEditor);
	}

	@Override
	public int getPriority() {
		// Make sure the rich editor opens frontmost
		return AbstractTaskEditorPageFactory.PRIORITY_TASK;
	}
	
}
