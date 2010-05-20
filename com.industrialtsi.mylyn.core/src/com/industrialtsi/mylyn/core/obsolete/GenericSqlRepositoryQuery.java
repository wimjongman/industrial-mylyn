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
package com.industrialtsi.mylyn.core.obsolete;

import com.industrialtsi.mylyn.core.IndustrialCore;
import com.industrialtsi.mylyn.core.dto.IndustrialQueryOptions;
import com.industrialtsi.mylyn.core.dto.IndustrialQueryParams;

/**
 * @author Maarten Meijer
 */
public class GenericSqlRepositoryQuery {

	private IndustrialQueryOptions params;

	private String titleText;

	private String repositoryUrl;

	private String queryUrl;

	/**
	 * @param repositoryUrl
	 * @param queryUrl
	 * @param titleText
	 */
	public GenericSqlRepositoryQuery(String repositoryUrl, String queryUrl, String titleText) {

		this.repositoryUrl = repositoryUrl;
		this.titleText = titleText;
		// assert queryUrl.startsWith(repositoryUrl + QUERY_URL);
		// FIXME ensure the need for this goes away!
		if (!queryUrl.contains(IndustrialQueryParams.QUERY_URL)) {
			setQueryUrl(repositoryUrl + IndustrialQueryParams.QUERY_URL + queryUrl);
		} else {
			setQueryUrl(queryUrl);
		}
	}

	public void setTitleText(String titleText) {
		this.titleText = titleText;
	}

	public void setRepositoryUrl(String repositoryUrl) {
		this.repositoryUrl = repositoryUrl;
	}

	public String getTitleText() {
		return titleText;
	}

	public String getRepositoryUrl() {
		return repositoryUrl;
	}

	private void setQueryUrl(String queryUrl) {
		this.queryUrl = queryUrl;

	}

	public String getQueryUrl() {
		return queryUrl;
	}

	public String getRepositoryKind() {
		return IndustrialCore.CONNECTOR_KIND;
	}

	/**
	 * @return the params
	 */
	public IndustrialQueryOptions getParams() {
		return params;
	}

	/**
	 * @param params
	 *            the params to set
	 */
	public void setParams(IndustrialQueryOptions params) {
		this.params = params;
	}

}
