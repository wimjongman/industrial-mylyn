package com.industrialtsi.mylyn.twitter.internal.ui;

import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.wizards.AbstractRepositoryQueryPage;

import com.industrialtsi.mylyn.ui.query.RepositoryQueryPageFactory;

public class RepositoryPageFactory extends RepositoryQueryPageFactory {

	public RepositoryPageFactory() {

	}

	@Override
	public String getPageDescription() {
		return "Twitter Query";
	}

	@Override
	public AbstractRepositoryQueryPage[] getPages(TaskRepository repository,
			IRepositoryQuery queryToEdit) {
		return new AbstractRepositoryQueryPage[] { new TwitterFormQueryPage(
				repository, queryToEdit) };
	}
}
