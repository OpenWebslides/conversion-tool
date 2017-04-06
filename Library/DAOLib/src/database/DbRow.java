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

    public DbRow(){
        row = new HashMap<>();
    }
    
    public Object getCellByName(String name){
        if(row.containsKey(name))
            return row.get(name);
        return null;
    }

    public Map<String,Object> getRow(){
        return row;
    }
    
    public String toString(){
        String toret = "";
        for(String key : row.keySet()){
            toret += key + " -> " + row.get(key) + "\t";
        }
        return toret;
    }

}
