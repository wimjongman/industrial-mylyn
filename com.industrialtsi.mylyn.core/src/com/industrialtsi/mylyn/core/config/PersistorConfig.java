/*******************************************************************************
 * Copyright (c) 2008 Industrial TSI and Maarten Meijer.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Maarten Meijer - initial API and implementation   
 */
package com.industrialtsi.mylyn.core.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.industrialtsi.mylyn.core.persistence.IPersistor;

public class PersistorConfig {
	private String id;
	
	private String name;
	
	private String persistorFQCN;
	
	private String jdbcUrlTemplate;

	private String jdbcUrlRegularExpression;

	private IPersistor persistor = null;
	
	private HashMap<String, String> attributes;
	
	private HashMap<String, IbatisConfig> ibatisConfigs;
	
	public PersistorConfig() {
		super();
		attributes = new HashMap<String, String>();
		ibatisConfigs = new HashMap<String, IbatisConfig>();
	}

	/**
	 * @return the id
	 */
	public final String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public final void setId(String id) {
		this.id = id;
	}


	/**
	 * @return the name
	 */
	public final String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public final void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the persistor
	 */
	public final String getPersistorFQCN() {
		return persistorFQCN;
	}

	/**
	 * @param persistorClassName the persistor to set
	 */
	public final void setPersistorFQCN(String persistorClassName) {
		this.persistorFQCN = persistorClassName;
	}

	
	
	
	/**
	 * @return the jdbcUrlTemplate
	 */
	public final String getJdbcUrlTemplate() {
		return jdbcUrlTemplate;
	}

	/**
	 * @param jdbcUrlTemplate the jdbcUrlTemplate to set
	 */
	public final void setJdbcUrlTemplate(String jdbcUrlTemplate) {
		this.jdbcUrlTemplate = jdbcUrlTemplate;
	}

	/**
	 * @param jdbcUrlRegularExpression the jdbcUrlRegularExpression to set
	 */
	public void setJdbcUrlRegularExpression(String jdbcUrlRegularExpression) {
		this.jdbcUrlRegularExpression = jdbcUrlRegularExpression;
	}

	/**
	 * @return the jdbcUrlRegularExpression
	 */
	public String getJdbcUrlRegularExpression() {
		return jdbcUrlRegularExpression;
	}

	/**
	 * @return the {@link IPersistor} based on fcqn;
	 */
	public IPersistor getPersistor() {
		return persistor;
	}

	/**
	 * Set the persistor.
	 * TODO replace with lazy initialization in {@link #getPersistor()}.
	 * 
	 * @param persistor
	 */
	public void setPersistor(IPersistor persistor) {
		this.persistor = persistor;
	}

	public void addAttribute(String key, String value) {
		if(!attributes.containsKey(key))
			attributes.put(key, value);
	}
	
	public Map<String, String> getAttributes() {
		return Collections.unmodifiableMap(attributes);
	}

	public void addIbatisConfig(IbatisConfig config) {
		if(!ibatisConfigs.containsKey(config.getName()))
			ibatisConfigs.put(config.getName(), config);
	}

	public Set<String> getIbatisConfigNames() {
		return ibatisConfigs.keySet();
	}
	
	public IbatisConfig getIbatisConfig(String name) {
		return ibatisConfigs.get(name);
	}
}
