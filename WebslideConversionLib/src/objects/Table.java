/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import java.util.ArrayList;

/**
 *
 * @author Karel
 */
public class Table implements PPTObject {
    
    private ArrayList<Row> rows;

    public Table(){
        rows = new ArrayList<>();
    }
    
    public ArrayList<Row> getRows() {
        return rows;
    }
    
    public String toString(){
        String toret = "";
        for(Row row : rows){
            toret += row.toString() + "\n";
        }
        return toret;
    }
    
    
    
}

