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

    private ArrayList<Cell> cells;
    
    public Row(){
        cells = new ArrayList<>();
    }

    public ArrayList<Cell> getCells() {
        return cells;
    }
    
    public String toString(){
        String toret = "";
        for(Cell cell : cells){
                toret += cell.toString() + "\t";
        }
        return toret;
    }

    @Override
    public String getContent() {
        String toret = "";
        for(Cell cell : cells){
                toret += cell.getContent()+ "\t";
        }
        return toret;
    }

}
