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
package com.industrialtsi.mylyn.db;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;

public class DerbyTestDataExporter {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		// database connection
		try {
			@SuppressWarnings("unused")
			Class driverClass = Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			Connection jdbcConnection = DriverManager
					.getConnection(
					"jdbc:derby:/Users/maarten/Desktop/tasks/MylynTestDB",
							"APP", "secret");
			IDatabaseConnection connection = new DatabaseConnection(jdbcConnection);

			String schema = connection.getSchema();

			System.out.println(schema);


			// partial database export
			QueryDataSet partialDataSet = new QueryDataSet(connection);
			partialDataSet.addTable("issues");
			partialDataSet.addTable("comments");
			partialDataSet.addTable("attachments");
			partialDataSet.addTable("priority");
			partialDataSet.addTable("products");
			partialDataSet.addTable("status");
			FlatXmlDataSet.write(partialDataSet, new FileOutputStream("partial.xml"));

			// full database export
			IDataSet fullDataSet = connection.createDataSet();
			FlatXmlDataSet.write(fullDataSet, new FileOutputStream("full.xml"));

			// dependent tables database export: export table X and all tables
			// that
			// have a PK which is a FK on X, in the right order for insertion
			// String[] depTableNames =
			// TablesDependencyHelper.getAllDependentTables(connection,
			// "issues");
			// IDataSet depDataset = connection.createDataSet(depTableNames);
			// FlatXmlDataSet.write(depDataset, new
			// FileOutputStream("dependents.xml"));

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DatabaseUnitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Completed succesfully");
	}
}
