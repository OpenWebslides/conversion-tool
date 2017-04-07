/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Karel
 */
public class DbRow {
    
    private Map<String,Object> row;

    /**
     * Create a DbRow instance
     */
    public DbRow(){
        row = new HashMap<>();
    }
    
    /**
     * Get the cellvalue with a certain columname
     * @param name
     * @return 
     */
    public Object getCellByName(String name){
        if(row.containsKey(name))
            return row.get(name);
        return null;
    }

    /**
     * Get the row map, this is an editable map
     * @return 
     */
    public Map<String,Object> getRow(){
        return row;
    }
    
    /**
     * Returns a string with all the keys and values in the dbrow
     * @return 
     */
    public String toString(){
        String toret = "";
        for(String key : row.keySet()){
            toret += key + " -> " + row.get(key) + "\t";
        }
        return toret;
    }

}
