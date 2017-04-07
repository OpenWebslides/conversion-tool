/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import org.apache.commons.dbcp2.BasicDataSource;



/**
 *
 * @author Karel
 */
public class DataSource {

    private static DataSource datasource;
    private final BasicDataSource ds;

    private DataSource() throws IOException, SQLException, PropertyVetoException {
        ds = new BasicDataSource();
        ds.setDriverClassName(DbConstants.DB_DRIVER);
        ds.setUsername(DbConstants.DB_LOGIN);
        ds.setPassword(DbConstants.DB_PASSW);
        ds.setUrl(DbConstants.DB_URL);
       
     // the settings below are optional -- dbcp can work with defaults
        ds.setMinIdle(0);
        ds.setMaxIdle(2);
        ds.setMaxOpenPreparedStatements(5);

    }

    public static DataSource getInstance() throws IOException, SQLException, PropertyVetoException {
        if (datasource == null) {
            datasource = new DataSource();
            return datasource;
        } else {
            return datasource;
        }
    }

    public Connection getConnection() throws SQLException {
        return this.ds.getConnection();
    }

}