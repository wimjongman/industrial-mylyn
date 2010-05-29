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

package com.industrialtsi.mylyn.core.config;

/**
 * <code>RepositoryAttributesConfig</code> holder for the attributes as defined in plugin.xml.
 *
 * @author maarten
 *
 */
public class RepositoryAttributesConfig {
	private boolean canCreateNewTasks;

	private boolean canCreateTaskFromKey;
	
	private boolean canQueryRepository;

	private boolean canSynchronizeTasks;

	private boolean canGetAttachments;

	private boolean canPostAttachments;

	private boolean canInitializeSchema;

	private boolean canDeleteTasks;

	private boolean canDeleteComments;
	
	private boolean canDeleteAttachments;

	/**
	 * @since 0.9.6 negative default is false
	 */
	private boolean canNotCreateComments;

	/**
	 * @return the canCreateNewTasks
	 */
	public final boolean isCanCreateNewTasks() {
		return canCreateNewTasks;
	}

	/**
	 * @param canCreateNewTasks the canCreateNewTasks to set
	 */
	public final void setCanCreateNewTasks(boolean canCreateNewTasks) {
		this.canCreateNewTasks = canCreateNewTasks;
	}

	/**
	 * @return the canCreateTaskFromKey
	 */
	public final boolean isCanCreateTaskFromKey() {
		return canCreateTaskFromKey;
	}

	/**
	 * @param canCreateTaskFromKey the canCreateTaskFromKey to set
	 */
	public final void setCanCreateTaskFromKey(boolean canCreateTaskFromKey) {
		this.canCreateTaskFromKey = canCreateTaskFromKey;
	}

	/**
	 * @return the canQueryRepository
	 */
	public final boolean isCanQueryRepository() {
		return canQueryRepository;
	}

	/**
	 * @param canQueryRepository the canQueryRepository to set
	 */
	public final void setCanQueryRepository(boolean canQueryRepository) {
		this.canQueryRepository = canQueryRepository;
	}

	/**
	 * @return the canSynchronizeTasks
	 */
	public final boolean isCanSynchronizeTasks() {
		return canSynchronizeTasks;
	}

	/**
	 * @param canSynchronizeTasks the canSynchronizeTasks to set
	 */
	public final void setCanSynchronizeTasks(boolean canSynchronizeTasks) {
		this.canSynchronizeTasks = canSynchronizeTasks;
	}

	/**
	 * @return the canGetAttachments
	 */
	public final boolean isCanGetAttachments() {
		return canGetAttachments;
	}

	/**
	 * @param canGetAttachments the canGetAttachments to set
	 */
	public final void setCanGetAttachments(boolean canGetAttachments) {
		this.canGetAttachments = canGetAttachments;
	}

	/**
	 * @return the canPostAttachments
	 */
	public final boolean isCanPostAttachments() {
		return canPostAttachments;
	}

	/**
	 * @param canPostAttachments the canPostAttachments to set
	 */
	public final void setCanPostAttachments(boolean canPostAttachments) {
		this.canPostAttachments = canPostAttachments;
	}

	/**
	 * @return the canInitializeSchema
	 */
	public final boolean isCanInitializeSchema() {
		return canInitializeSchema;
	}

	/**
	 * @param canInitializeSchema the canInitializeSchema to set
	 */
	public final void setCanInitializeSchema(boolean canInitializeSchema) {
		this.canInitializeSchema = canInitializeSchema;
	}

	/**
	 * @return the canDeleteTasks
	 */
	public final boolean isCanDeleteTasks() {
		return canDeleteTasks;
	}

	/**
	 * @param canDeleteTasks the canDeleteTasks to set
	 */
	public final void setCanDeleteTasks(boolean canDeleteTasks) {
		this.canDeleteTasks = canDeleteTasks;
	}

	/**
	 * @return the canDeleteComments
	 */
	public final boolean isCanDeleteComments() {
		return canDeleteComments;
	}

	/**
	 * @param canDeleteComments the canDeleteComments to set
	 */
	public final void setCanDeleteComments(boolean canDeleteComments) {
		this.canDeleteComments = canDeleteComments;
	}

	/**
	 * @return the canDeleteAttachments
	 */
	public final boolean isCanDeleteAttachments() {
		return canDeleteAttachments;
	}

	/**
	 * @param canDeleteAttachments the canDeleteAttachments to set
	 */
	public final void setCanDeleteAttachments(boolean canDeleteAttachments) {
		this.canDeleteAttachments = canDeleteAttachments;
	}

	/**
	 * @param canCreateComments the canCreateComments to set
	 */
	public void setCannotCreateComments(boolean canCreateComments) {
		this.canNotCreateComments = canCreateComments;
	}

	/**
	 * @return the canCreateComments
	 */
	public boolean isCannotCreateComments() {
		return canNotCreateComments;
	}

}
