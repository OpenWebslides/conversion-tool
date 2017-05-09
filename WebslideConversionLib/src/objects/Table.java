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
    
    //A Table contains rows
    private ArrayList<Row> rows;

    /**
     * Create an instance of Table object
     */
    public Table(){
        rows = new ArrayList<>();
    }
    
    /**
     * Get the rows of the Table
     * This is an editable ArrayList, changes will be saved in the Table
     * @return ArrayList
     */
    public ArrayList<Row> getRows() {
        return rows;
    }
    
    @Override
    /**
     * Return the data behind the Table, this contains all of the toString methods of its rows
     */
    public String toString(){
        String toret = "";
        for(Row row : rows){
            toret += row.toString() + "\n";
        }
        return toret;
    }

    @Override
    /**
     * Return the content of the Table, this contains all of the getContent methods of its rows
     */
    public String getContent() {
        String toret = "\n";
        for(Row row : rows){
            toret += row.getContent()+ "\n";
        }
        return toret;
        
    }
    
    
    
}

