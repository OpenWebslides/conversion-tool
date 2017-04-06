/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;


/**
 *
 * @author Karel
 */
public class DbConstants {
    
    //Login and password
    public static final String DB_LOGIN = "webslides";
    public static final String DB_PASSW = "sliw3b";
    
    //Connection
    public static final String DB_URL = "jdbc:mysql://localhost:3306/webslides?useSSL=false";
    public static final String DB_DRIVER = "com.mysql.jdbc.Driver";
    
    //Query variables
    public static Integer LIMIT_DB = 100; // for crash prevention

}
