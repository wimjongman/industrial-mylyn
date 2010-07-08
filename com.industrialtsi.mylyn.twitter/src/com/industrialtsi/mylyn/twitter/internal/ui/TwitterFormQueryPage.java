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
package com.industrialtsi.mylyn.twitter.internal.ui;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.wizards.AbstractRepositoryQueryPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;

import com.industrialtsi.mylyn.core.IndustrialCore;
import com.industrialtsi.mylyn.core.IndustrialRepositoryConnector;
import com.industrialtsi.mylyn.core.dto.IndustrialQueryParams;
import com.industrialtsi.mylyn.ui.IndustrialUiImages;
import com.industrialtsi.mylyn.ui.internal.UILogger;

public class TwitterFormQueryPage extends AbstractRepositoryQueryPage {

	private static final String EMPTY_STRING = ""; //$NON-NLS-1$

	private static final String QUERY_DELIMITER = "|"; //$NON-NLS-1$

	private static final String QUERY_SEPARATOR = "&"; //$NON-NLS-1$

	private List friendsList;

	private static final String TITLE = "Twitter Query";

	private static final String DESCRIPTION = "Create a Query based on your criteria";

	private static final String TITLE_QUERY_TITLE = "Query Title:";

	private final IRepositoryQuery query;

	private final TaskRepository repository;

	private Text titleText;

	private IndustrialQueryParams legal;

	private Button updateButton;

	private List statusList;

	private Text tweetText;

	/**
	 * Return a set of strings joined into a single string separated by
	 * delimiter.
	 * 
	 * @param array
	 * @param delimiter
	 * @return
	 */
	public static String join(String[] array, String delimiter) {
		java.util.List<String> items = Arrays.asList(array);
		return join(items, delimiter);
	}
@Override
public Image getImage() {
	// TODO Auto-generated method stub
	return super.getImage();
}
	public static String join(Collection<String> s, String delimiter) {
		StringBuffer buffer = new StringBuffer();
		Iterator<String> iter = s.iterator();
		while (iter.hasNext()) {
			String next = iter.next();
			buffer.append(next);
			if ((null == next || 0 != next.length()) && iter.hasNext()) {
				buffer.append(delimiter);
			}
		}
		return buffer.toString();
	}

	public TwitterFormQueryPage(TaskRepository repository,
			IRepositoryQuery queryToEdit) {
		super(TITLE, repository, queryToEdit);
		this.repository = repository;
		this.query = queryToEdit;
		setTitle(TITLE);

		// TODO update attributes ??

		setDescription(DESCRIPTION);
		setImageDescriptor(IndustrialUiImages.BANNER_REPOSITORY);
	}

	@Override
	public boolean canFlipToNextPage() {
		return false;
	}

	@Override
	public boolean isPageComplete() {
		if (titleText != null && titleText.getText().length() > 0) {
			return true;
		}
		return false;
	}

	public String getQueryUrl(String repositoryUrl) {

		String[] fields = {
				getCriterium(tweetText, IndustrialQueryParams.SUMMARY),
				getCriterium(friendsList, IndustrialQueryParams.OWNER),
				getCriterium(statusList, IndustrialQueryParams.ISSUE_STATUS), };

		StringBuilder sb = new StringBuilder();
		sb.append(repositoryUrl);
		sb.append(IndustrialQueryParams.QUERY_URL);
		sb.append(join(fields, QUERY_SEPARATOR));
		return sb.toString();
	}

	private String getTitleText() {
		return (titleText != null) ? titleText.getText() : "<search>"; //$NON-NLS-1$
	}

	private void createTitleGroup(Composite control) {

		final Composite topComposite = new Composite(control, SWT.NONE);
		final GridData gd_topComposite = new GridData(SWT.FILL, SWT.CENTER,
				true, false);
		topComposite.setLayoutData(gd_topComposite);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		topComposite.setLayout(gridLayout);

		Label titleLabel = new Label(topComposite, SWT.NONE);
		titleLabel.setText(TITLE_QUERY_TITLE);

		titleText = new Text(topComposite, SWT.BORDER);
		titleText
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		titleText.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				// ignore
			}

			public void keyReleased(KeyEvent e) {
				getContainer().updateButtons();
			}
		});

		Label summaryLabel = new Label(topComposite, SWT.NONE);
		summaryLabel.setText("Tweet Contains:");

		tweetText = new Text(topComposite, SWT.BORDER);
		tweetText
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

	}

	protected void createUpdateButton(final Composite control) {

		final Composite updateComposite = new Composite(control, SWT.NONE);
		final GridData gd_updateComposite = new GridData(SWT.FILL, SWT.CENTER,
				true, false);
		updateComposite.setLayoutData(gd_updateComposite);
		updateComposite.setLayout(new GridLayout());

		updateButton = new Button(updateComposite, SWT.PUSH);
		updateButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true,
				false));
		updateButton.setText("Fetch new Friends");
		updateButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (repository != null) {
					updateAttributesFromRepository(true);
				} else {
					MessageDialog.openInformation(Display.getCurrent()
							.getActiveShell(), "Twitter Connector Error",
							"Cannot Find Repository");
				}
			}
		});
	}

	private void updateAttributesFromRepository(final boolean force) {
		final IndustrialRepositoryConnector connector = IndustrialCore
				.getDefault().getConnector();

		String attributes = repository
				.getProperty(IndustrialCore.REPOSITORY_ATTRIBUTES);

		if (null == attributes || force) {
			try {
				IRunnableWithProgress runnable = new IRunnableWithProgress() {
					public void run(IProgressMonitor monitor)
							throws InvocationTargetException,
							InterruptedException {
						try {
							monitor.beginTask("Updating search options...",
									IProgressMonitor.UNKNOWN);
							connector.updateRepositoryConfiguration(repository,
									monitor);
						} catch (Exception e) {
							throw new InvocationTargetException(e);
						}
					}
				};

				if (getContainer() != null) {
					getContainer().run(true, true, runnable);
				} else {
					IProgressService service = PlatformUI.getWorkbench()
							.getProgressService();
					service.busyCursorWhile(runnable);
				}
			} catch (InvocationTargetException e) {
				// StatusHandler.displayStatus("Error updating attributes",
				// IndustrialCore.toStatus(e.getCause(),
				// repository));
				return;
			} catch (InterruptedException e) {
				return;
			}
			attributes = repository
					.getProperty(IndustrialCore.REPOSITORY_ATTRIBUTES);
		}

		legal = new IndustrialQueryParams(null != attributes ? attributes
				: EMPTY_STRING);

		if (null != legal) {
			UILogger.logInfo("RETRIEVED:" + legal.asUrl()); //$NON-NLS-1$
			friendsList.setItems(legal.getOwnerOrEmpty());
			statusList.setItems(legal.getIssueStatusOrEmpty());
		} else {
			IStatus status = UILogger.createStatus(IStatus.ERROR,
					"Failed to retrieve legal values for parameters", null); //$NON-NLS-1$
			UILogger.log(status);
		}

	}

	public void createControl(Composite parent) {
		getLegalValues();

		Composite control = new Composite(parent, SWT.NONE);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 400;
		control.setLayoutData(gd);
		GridLayout layout = new GridLayout(1, false);
		control.setLayout(layout);

		createTitleGroup(control);

		createFieldSelectors(control);

		createUpdateButton(control);

		// Initialize from existing
		initializeFieldsFromQuery();

		setControl(control);

	}

	private void createFieldSelectors(Composite control) {
		final SashForm sashForm = new SashForm(control, SWT.HORIZONTAL);

		final Composite selectors = new Composite(sashForm, SWT.NONE);
		final GridLayout gridLayout_1 = new GridLayout();
		gridLayout_1.makeColumnsEqualWidth = true;
		gridLayout_1.numColumns = 2;
		selectors.setLayout(gridLayout_1);

		/* owners */
		final Composite friendSelector = new Composite(selectors, SWT.NONE);
		friendSelector
				.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true));
		friendSelector.setLayout(new GridLayout());

		final Label label_2 = new Label(friendSelector, SWT.NONE);
		label_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		label_2.setText("Friends");

		friendsList = new List(friendSelector, SWT.BORDER | SWT.MULTI
				| SWT.V_SCROLL);
		friendsList
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		friendsList.setItems(legal.getOwnerOrEmpty());

		/* issue status */
		final Composite statusSelector = new Composite(selectors, SWT.NONE);
		statusSelector
				.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true));
		statusSelector.setLayout(new GridLayout());

		final Label statusLabel = new Label(statusSelector, SWT.NONE);
		statusLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false));
		statusLabel.setText("Tweet Type");

		statusList = new List(statusSelector, SWT.BORDER | SWT.MULTI
				| SWT.V_SCROLL);
		statusList
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		statusList.setItems(legal.getIssueStatusOrEmpty());

		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		sashForm.setWeights(new int[] { 1 });

		sashForm.pack();
	}

	private void initializeFieldsFromQuery() {
		if (null != this.query) {
			IndustrialQueryParams params = new IndustrialQueryParams(
					this.query.getUrl());
			tweetText.setText(params.getSummaryOrEmpty());

			tweetText.setText(params.getSummaryOrEmpty());

			friendsList.setSelection(params.getOwnerOrEmpty());

			statusList.setSelection(params.getIssueStatusOrEmpty());

			titleText.setText(query.getSummary());
		}
	}

	private void getLegalValues() {
		String attributes = repository
				.getProperty(IndustrialCore.REPOSITORY_ATTRIBUTES);

		if (null == attributes) {
			updateAttributesFromRepository(false);
		}

		legal = new IndustrialQueryParams(null != attributes ? attributes
				: EMPTY_STRING);
	}

	@Override
	public void applyTo(IRepositoryQuery query) {
		// TODO Auto-generated method stub
		query.setUrl(getQueryUrl(repository.getUrl()));
		query.setSummary(getTitleText());
	}

	@Override
	public String getQueryTitle() {
		// TODO Auto-generated method stub
		return getTitleText();
	}

	private String getCriterium(List list, String fieldName) {
		java.util.List<String> items = Arrays.asList(list.getSelection());
		if (!items.isEmpty())
			return fieldName + join(items, QUERY_DELIMITER);
		return EMPTY_STRING;
	}

	private String getCriterium(Text text, String fieldName) {
		if (text.getText().trim().length() != 0)
			return fieldName + text.getText();
		return EMPTY_STRING;
	}
}
