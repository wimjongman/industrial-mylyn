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
package com.industrialtsi.mylyn.ui.editors;

import java.util.Set;

import org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPage;
import org.eclipse.mylyn.tasks.ui.editors.AttributeEditorFactory;
import org.eclipse.mylyn.tasks.ui.editors.TaskEditor;
import org.eclipse.mylyn.tasks.ui.editors.TaskEditorPartDescriptor;

import com.industrialtsi.mylyn.core.IndustrialCore;

public class IndustrialEditorPage extends AbstractTaskEditorPage {

	public IndustrialEditorPage(TaskEditor editor) {
		super(editor, IndustrialCore.CONNECTOR_KIND);
	}

	@Override
	protected Set<TaskEditorPartDescriptor> createPartDescriptors() {
		Set<TaskEditorPartDescriptor> descriptors = super.createPartDescriptors();
		// remove unnecessary default editor parts
//		for (Iterator<TaskEditorPartDescriptor> it = descriptors.iterator(); it.hasNext();) {
//			TaskEditorPartDescriptor taskEditorPartDescriptor = it.next();
//			 if (taskEditorPartDescriptor.getId().equals(ID_PART_PEOPLE)) {
//			  it.remove();
//			 }
//		}
		// add custom editor parts
		return descriptors;
	}

	@Override
	protected AttributeEditorFactory createAttributeEditorFactory() {
		return super.createAttributeEditorFactory();
	}
	
	@Override
	public void doSubmit() {
		// TODO Add pre submit checks here
		super.doSubmit();
	}
	
}
