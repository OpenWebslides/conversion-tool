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
public class Row implements PPTObject {

    //A row has multiple cells
    private ArrayList<Cell> cells;
    
    /**
     * Create an instance of the Row object
     */
    public Row(){
        cells = new ArrayList<>();
    }

    /**
     * Get the cells of the row. This is an editable ArrayList, if you remove/change items this will be saved in the table
     * @return ArrayList
     */
    public ArrayList<Cell> getCells() {
        return cells;
    }
    
    @Override
    /**
     * Return a String which contains the data of the row (toString() of the cells)
     */
    public String toString(){
        String toret = "";
        for(Cell cell : cells){
                toret += cell.toString() + "\t";
        }
        return toret;
    }

    @Override
    /**
     * Return a String which contains the content of all of its cells
     */
    public String getContent() {
        String toret = "";
        for(Cell cell : cells){
                toret += cell.getContent()+ "\t";
        }
        return toret;
    }

}
