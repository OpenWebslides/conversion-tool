/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openwebslides.writer;

import java.util.ArrayList;
import java.util.List;
import objects.PPTObject;

/**
 * This class is used as an internal representation of PPTLists for the 
 * HTMLWriter class. This DeepPPTList can hold other lists of PPTObjects.
 * @author Jonas
 */
public class DeepPPTList extends ArrayList<List<PPTObject>> implements PPTObject{

    /**
     * whether the list is ordered or unordered.
     */
    private boolean ordered;
    
    /**
     * Returns the last item inside the list. If the list was empty, a new
     * item is added and is returned.
     * @return The last available item in the list.
     */
    public List<PPTObject> getLastItem(){
        if(this.size()==0){
            return getNewItem();
        }
        return this.get(this.size()-1);
    }
    
    /**
     * Creates a new item in the list and returns it.
     * @return A new item in the list.
     */
    public List<PPTObject> getNewItem(){
        this.add(new ArrayList<>());
        return this.get(this.size()-1);
    }
    
    @Override
    public String getContent() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public String toString(){
        String res = "";
        for(int i=0; i<this.size(); i++){
            res += "("+(i+1)+") ";
            for(PPTObject obj : this.get(i)){
                res += "* "+obj.toString()+"\n";
            }
            res += "\n";
        }
        return res;
    }

    public boolean isOrdered() {
        return ordered;
    }

    public void setOrdered(boolean ordered) {
        this.ordered = ordered;
    }
    
    
}
