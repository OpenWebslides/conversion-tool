/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

/**
 *
 * @author Joann
 */
public interface PPTObject {
    
    /**
     * Return the html from this PPTObject
     * @param indentation
     * @return 
     */
    public String toHtml(int indentation);
}
