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

public class IbatisConfig {
	private String id;
	
	private String name;
	
	private String driver;
	
	private String maps;
	
	private String username;
	
	private String password;

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
	 * @return the driver
	 */
	public final String getDriver() {
		return driver;
	}

	/**
	 * @param driver the driver to set
	 */
	public final void setDriver(String driver) {
		this.driver = driver;
	}

	/**
	 * @return the maps
	 */
	public final String getMaps() {
		return maps;
	}

	/**
	 * @param maps the maps to set
	 */
	public final void setMaps(String maps) {
		this.maps = maps;
	}

	/**
	 * @return the username
	 */
	public final String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public final void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public final String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public final void setPassword(String password) {
		this.password = password;
	}

}
