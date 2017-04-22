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
 *
 * @author Jonas
 */
public class DeepPPTList extends ArrayList<List<PPTObject>> implements PPTObject{

    private boolean ordered;
    
    /*public DeepPPTList(){
        this.add(new ArrayList<>());
    }*/
    
    public List<PPTObject> getLastItem(){
        if(this.size()==0){
            return getNewItem();
        }
        return this.get(this.size()-1);
    }
    
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
