/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import logger.Logger;
import output.Output;
import output.StdOutput;

/**
 *
 * @author Karel
 */
public class DbManagement {
    
    private Connection connection;
    private PreparedStatement statement;
    private ResultSet resultSet;
    
    private Output output;
    
    /**
     * Create a new DbManagement instance. Possible to add output to constructor, default will be stdOutput
     * @param query
     * @param parameters 
     */
    public DbManagement(Output output){
        try{
            this.output = output;
        }
        catch(Exception e){
            output.println(Logger.error("There was an error while creating the DataSource object", e));
        }    
    }
    
     /**
     * Create a new DbManagement instance. The output will default to StdOutput (console)
     * @param query
     * @param parameters 
     */
    public DbManagement(){
        try{
            this.output =  new StdOutput();
        }
        catch(Exception e){
            output.println(Logger.error("There was an error while creating the DataSource object", e));
        }    
    }
    
    /*
    private void setConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");  
            connection=DriverManager.getConnection(  
                    DbConstants.DB_URL,DbConstants.DB_LOGIN,DbConstants.DB_PASSW);  
        } catch (Exception e) {
            output.println(Logger.error("There was an error opening the DbConnection", e));
        }
    }
    */
     private void setConnection() {
        try {
            if(connection!=null){
                close();
            }
            
            StackTraceElement[] st = Thread.currentThread().getStackTrace();
            System.out.println(  "create connection called from " + st[2] );
             connection = DataSource.getInstance().getConnection();
        } catch (Exception e) {
            output.println(Logger.error("There was an error opening the DbConnection", e));
        }
    }
    
    private void setStatement(String query) {
        try {
            if (query.contains("SELECT") && !query.contains("LIMIT")) {
                query += " LIMIT " + DbConstants.LIMIT_DB;
            }
            statement = connection.prepareStatement(query);
        } catch (SQLException e) {
            output.println(Logger.error("There was an error executing the statement: " + query, e));
        }
    }
        
    private void setParameters(List<Map.Entry<String, Object>> parameters) {
        if (parameters == null) {
            return;
        }
        try {
            int i = 1;
            for (Map.Entry<String, Object> pair : parameters) {
                switch (pair.getKey()) {
                    case "String":
                        if (pair.getValue() != null) {
                            String s = (String) pair.getValue();
                            statement.setString(i, s);
                        } else {
                            statement.setNull(i, Types.NULL);
                        }   i++;
                        break;
                    case "Integer":
                        Integer nr = 0;
                        if (pair.getValue() != null) {
                            nr = (Integer) pair.getValue();
                            statement.setInt(i, nr);
                        } else {
                            statement.setNull(i, Types.NULL);
                        }   i++;
                        break;
                    case "boolean":
                        boolean b = false;
                        if (pair.getValue() != null) {
                            b = (Boolean) pair.getValue();
                        }   statement.setBoolean(i, b);
                        i++;
                        break;
                    case "Double":
                        {
                            Double d = 0.0;
                            if (pair.getValue() != null) {
                                d = (Double) pair.getValue();
                                statement.setDouble(i, d);
                            } else {
                                statement.setNull(i, Types.NULL);
                            }       i++;
                            break;
                        }
                    case "double":
                        {
                            double d = 0.0;
                            if (pair.getValue() != null) {
                                d = (Double) pair.getValue();
                            }       statement.setDouble(i, d);
                            i++;
                            break;
                        }
                    case "Date":
                        java.sql.Timestamp sqlDate = null;
                        if (pair.getValue() != null) {
                            sqlDate = new java.sql.Timestamp(((java.util.Date) pair.getValue()).getTime());
                            statement.setTimestamp(i, sqlDate);
                        } else {
                            statement.setNull(i, Types.NULL);
                        }   i++;
                        break;
                    case "Blob":
                        if (pair.getValue() != null) {
                            byte[] data = ((byte[]) pair.getValue());
                            statement.setObject(i, data);
                        } else {
                            statement.setNull(i, Types.NULL);
                        }   i++;
                        break;
                    default:
                        throw new SQLException();
                }
            }
        } catch (Exception e) {
            try{
                String error = "There was an error adding the statement parameters: \n";
                
                for (Map.Entry<String, Object> pair : parameters) {
                    error += pair.getKey() + " -> " + pair.getValue() + "\n";
                }
                output.println(Logger.error(error, e));
            }
            catch(Exception ex){
                output.println(Logger.error("There was an error adding the statement parameters", ex));
            }
        }
    }

   
    private void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }

            if (statement != null) {
                statement.close();
            }

            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
                output.println(Logger.error("There was an error closing the connection", e));
            
        }
    }

    
    /**
     * Execute update
     * @param query
     * @param parameters
     * @return 
     */
    public boolean executeUpdate(String query, List<Map.Entry<String,Object>> parameters) { 
        boolean success;
        try {
            setConnection();
            setStatement(query);
            setParameters(parameters);
            statement.executeUpdate();
            success = true;
        } catch (SQLException e) {
            output.println(Logger.error("There was an error executing the update: " + query, e));
            success = false;
        } finally {
            close();
        }
        return success;
    }

    /**
     * Execute an update, return the auto generated ID
     * @param query
     * @param parameters
     * @return 
     */
    public Integer executeUpdateReturnNewId(String query, List<Map.Entry<String,Object>> parameters) {  
        Integer id = null;
        try {            
            setConnection();
            setStatement(query);
            setParameters(parameters);
            int i = statement.executeUpdate();
            ResultSet result = this.connection.createStatement().executeQuery("SELECT LAST_INSERT_ID()");            
            if (result.next()) {
                id = result.getInt(1);
            }
        } catch (SQLException e) {
            output.println(Logger.error("There was an error executing the update with autogenerated ID: " + query, e));
        } finally {
            close();
        }
        return id;
    }

    public ArrayList<DbRow> executeQueryDbRowList(String query, List<Map.Entry<String,Object>> parameters) {
       ArrayList<DbRow> table = new ArrayList<>();
        try { 
            ResultSet result;        
            setConnection();
            setStatement(query);
            setParameters(parameters);
            result = statement.executeQuery();
            while(result.next()){
                int i = 0;
                DbRow row = new DbRow();
                while(i < result.getMetaData().getColumnCount()){
                    i++;
                    row.getRow().put(result.getMetaData().getColumnName(i), result.getObject(i));
                }
                table.add(row);
            }
        } catch (SQLException e) {
            output.println(Logger.error("There was an error executing the query " + query, e));
        } finally {
            close();
        }
        return table;
    }  
    
    public DbRow executeQueryDbRow(String query, List<Map.Entry<String,Object>> parameters) {
        DbRow row = new DbRow();
        try {
            ResultSet result;
            setConnection();
            setStatement(query);
            setParameters(parameters);
            result = statement.executeQuery();
            result.next();
            int i = 0;
            while(i < result.getMetaData().getColumnCount()){
                    i++;
                    row.getRow().put(result.getMetaData().getColumnName(i), result.getObject(i));
                }
                
        } catch (SQLException e) {
            output.println(Logger.error("There was an error executing the query " + query, e));
        } finally {
            close();
        }
        return row;
    }  
    
}
