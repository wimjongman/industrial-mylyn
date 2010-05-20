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
package com.industrialtsi.mylyn.ui.internal;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

/**
 * Select a file anywhere in the file system or enter manually.
 * 
 * Needs to be flattened: see <a
 * href="http://www.jroller.com/eu/entry/groups_and_borders">euxx on Groups and
 * Borders</a>
 * 
 * 
 * @author Maarten Meijer
 */
public class FileSelector extends Composite {

	private static final String EMPTY_STRING = ""; //$NON-NLS-1$

	/**
	 * 
	 */
	private final String label = "Choose file";

	private final Group itemListGroup;

	private final Button browseItemList;

	final Text itemListPath;

	private String[] extensions = { EMPTY_STRING }; 

	private final class ItemListBrowser extends org.eclipse.swt.events.MouseAdapter {
		@Override
		public void mouseUp(final org.eclipse.swt.events.MouseEvent e) {
			FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
			// TODO make work, works on windows only anyway
			// dialog.setFilterExtensions(new String[] {
			// "*.txt;*.csv;errors.log" });
			String temp = dialog.open();
			if (null != temp && isItemSource(temp)) {
				itemListPath.setText(temp);
			}
		}

		/**
		 * @param temp
		 * @return
		 */
		private boolean isItemSource(String temp) {
			for (String extension : extensions) {
				if (temp.endsWith(extension))
					return true;
			}
			return false;
		}
	}

	/**
	 * @param parent
	 * @param style
	 */
	public FileSelector(Composite parent, int style) {
		super(parent, style);

		GridData pathData = new GridData();
		pathData.horizontalAlignment = GridData.FILL;
		pathData.grabExcessHorizontalSpace = true;
		// pathData.verticalAlignment = GridData.CENTER;
		pathData.minimumWidth = 300;
		GridData groupData = new GridData();
		groupData.horizontalAlignment = GridData.FILL;
		groupData.grabExcessHorizontalSpace = true;
		// groupData.verticalAlignment = GridData.CENTER;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		itemListGroup = new Group(parent, SWT.NONE);
		itemListGroup.setText(label);
		itemListGroup.setLayoutData(groupData);
		itemListGroup.setLayout(gridLayout);
		itemListPath = new Text(itemListGroup, SWT.BORDER);
		itemListPath.setLayoutData(pathData);
		// itemListPath.addModifyListener(readyToStart);

		// itemListPath.addFocusListener(editMenuUpdate);
		browseItemList = new Button(itemListGroup, SWT.NONE | SWT.PUSH);
		browseItemList.setText("Browse...");
		browseItemList.addMouseListener(new ItemListBrowser());
	}

	/**
	 * @return the file path selected
	 */
	public String getFilePath() {
		return itemListPath.getText();
	}

	/**
	 * @param path
	 *            the path or null
	 */
	public void setFilePath(String path) {
		// assert (null != path);

		itemListPath.setText(null == path ? EMPTY_STRING : path);
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return itemListGroup.getText();
	}

	/**
	 * @param label
	 *            the label to set or null
	 */
	public void setLabel(String label) {
		// assert (null != label);

		itemListGroup.setText(null == label ? EMPTY_STRING : label);
		itemListGroup.update();
	}

	/**
	 * @return the extensions
	 */
	public String[] getExtensions() {
		return extensions;
	}

	/**
	 * @param extensions
	 *            the extensions to set
	 */
	public void setExtensions(String[] extensions) {
		assert (null != extensions);

		this.extensions = extensions;
	}

	/**
	 * @return
	 */
	public boolean notSet() {
		// TODO Auto-generated method stub
		String path = itemListPath.getText();
		if (EMPTY_STRING.equals(path))
			return false;
		File f = new File(path);
		if (!f.exists() || !f.canRead() || !f.isFile())
			return false;
		return true;
	}
}
