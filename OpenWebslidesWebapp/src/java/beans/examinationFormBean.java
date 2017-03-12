/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Gertjan
 */
@ManagedBean
@ViewScoped
public class examinationFormBean implements Serializable{
    
    private String comment;
    private String radioVal = "ok";
    
    
    public examinationFormBean(){
    }
    
    public String getComment() {
        return comment;
    }
    public void makeIssue(){
        //mag enkel 2e keer er geklikt wordt (bij opstarten doet hij dat blijkbaar ook al eens...)
        
        radioVal = "haveissue";
        
    }
    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRadioVal() {
        return radioVal;
    }

    public void setRadioVal(String radioVal) {
        this.radioVal = radioVal;
    }

}
