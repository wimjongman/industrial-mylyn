/*******************************************************************************
 * Copyright (c) 2009 Maarten Meijer.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Created to demonstrate Industrial SQL Connector for Mylyn
 * update site :http://bugs.industrial-tsi.com/mylyndb/
 * blog: http://eclipsophy.blogspot.com/
 *
 * Contributors:
 *     Maarten Meijer - initial API and implementation
 *******************************************************************************/
package com.industrialtsi.mylyn.twitter.persistor;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.mylyn.commons.net.AuthenticationCredentials;
import org.eclipse.mylyn.commons.net.AuthenticationType;
import org.eclipse.mylyn.tasks.core.TaskRepository;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

import com.industrialtsi.mylyn.core.dto.IndustrialQueryParams;
import com.industrialtsi.mylyn.core.dto.IndustrialTask;
import com.industrialtsi.mylyn.core.internal.CoreLogger;
import com.industrialtsi.mylyn.core.persistence.PersistorAdapter;

public class TwitterPersistor extends PersistorAdapter {

	private static final String RETWEETS_OF = "Retweets Of";

	private static final String RETWEETS_BY = "Retweets By";


	public final static String ID = "com.industrialtsi.mylyn.twitter"; //$NON-NLS-1$

	private static final String MENTIONS = "Mentions";

	private Twitter twitter;

	public TwitterPersistor() {
	}

	/**
	 * @return the twitter, initialize if needed
	 */
	public Twitter getTwitter(TaskRepository repository) {
		if (null == twitter) {
			AuthenticationCredentials credentials = repository
					.getCredentials(AuthenticationType.REPOSITORY);
			String twitterPassword = credentials.getPassword();
			String twitterID = credentials.getUserName();
			twitter = new Twitter(twitterID, twitterPassword);
		}
		return twitter;
	}

	@Override
	public List<String> getLegalIssueStatus(TaskRepository repository)
			throws SQLException, CoreException {

		ArrayList<String> result = new ArrayList<String>();
		result.add(MENTIONS);
		result.add(RETWEETS_BY);
		result.add(RETWEETS_OF);

		return result;
	}

	@Override
	public IndustrialTask fetchTask(TaskRepository repository, String... taskId)
			throws SQLException, CoreException {
		Twitter t = getTwitter(repository);

		long id = Long.parseLong(taskId[0]);

		try {
			twitter4j.Status result = t.showStatus(id);
			IndustrialTask tweet = new IndustrialTask(repository.getUrl(),
					taskId[0], result.getText());

			tweet.setOwner(result.getUser().getName());
			tweet.setCreationDate(result.getCreatedAt());
			tweet.setNotes(result.getText());
			return tweet;
		} catch (TwitterException e) {
			IStatus status = CoreLogger.createStatus(IStatus.ERROR, e);
			throw new CoreException(status);
		}
	}

	@Override
	public List<String> getLegalOwners(TaskRepository repository)
			throws SQLException, CoreException {

		Twitter t = getTwitter(repository);

		List<String> result = new ArrayList<String>();
		List<User> friends;
		try {
			friends = t.getFriendsStatuses();
			for (User friend : friends) {
				result.add(friend.getName() + " (" + friend.getScreenName()
						+ ")");
			}
			Object[] res = result.toArray();
			Arrays.sort(res);
			result = new ArrayList<String>();
			for (Object object : res) {
				result.add((String) object);
			}
			return result;
		} catch (TwitterException e) {
			IStatus status = CoreLogger.createStatus(IStatus.ERROR, e);
			throw new CoreException(status);
		}
	}

	@Override
	public List<String> findTasks(TaskRepository repository,
			IndustrialQueryParams criteria) throws SQLException, CoreException {
		Twitter t = getTwitter(repository);

		List<String> result = new ArrayList<String>();
		try {
			for (String user : criteria.getOwnerOrEmpty()) {
				List<twitter4j.Status> timeline;
				timeline = t.getUserTimeline(getScreenName(user));
				for (twitter4j.Status s : timeline) {
					if (matches(s, criteria))
						result.add(Long.toString(s.getId()));
				}
			}
			return result;
		} catch (TwitterException e) {
			IStatus status = CoreLogger.createStatus(IStatus.ERROR, e);
			throw new CoreException(status);
		}
	}

	private boolean matches(twitter4j.Status s, IndustrialQueryParams criteria) {

		// Test Summary text
		String find = criteria.getSummaryOrEmpty();
		if (!contains(s, find))
			return false;

		// Status types
		boolean selected = false;
		for (String status : criteria.getIssueStatusOrEmpty()) {
			if (status.equals(MENTIONS)
					&& containsScreenName(criteria.getOwnerOrEmpty(),
							s.getInReplyToScreenName()))
				selected = true;
			else if (status.equals(RETWEETS_BY) && s.isRetweet())
				selected = true;
			else if (status.equals(RETWEETS_OF)
					&& containsScreenName(criteria.getOwnerOrEmpty(),
							s.getInReplyToScreenName()))
				selected = true;

		}

		if (selected == false && criteria.getIssueStatusOrEmpty().length > 0)
			return false;

		return true;

	}

	private boolean containsScreenName(String[] ownerOrEmpty,
			String inReplyToScreenName) {
		for (String name : ownerOrEmpty)
			if (getScreenName(name).equals(inReplyToScreenName))
				return true;
		return false;
	}

	private String getScreenName(String name) {
		return name.split("\\(")[1].split("\\)")[0];
	}

	private boolean contains(twitter4j.Status s, String find) {
		if (!find.isEmpty())
			if (!s.getText().contains(find))
				return false;
		return true;
	}

	@Override
	public boolean validate(TaskRepository repository) throws SQLException,
			CoreException {
		return null != getTwitter(repository);
	}

	@Override
	public boolean isAuthenticated(TaskRepository repository)
			throws SQLException, CoreException {

		try {
			getTwitter(repository).verifyCredentials();
			return true;
		} catch (TwitterException e) {
			Status status = new Status(IStatus.ERROR, TwitterPersistor.ID,
					"Cannot validate Twitter"); //$NON-NLS-1$
			throw new CoreException(status);
		}
	}

}
