/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversion.pdf.util;

import java.io.Serializable;

/**
 *
 * @author Gertjan
 */
public class TekenPlus implements Serializable{
    /*De klasse tekenplus heeft een char en info velden*/
    /*Voorlopig moeten al deze velden verplicht ingevuld worden... niet erg algemeen voor een util mabon.*/
    /*Je kan een teken ook uitschrijven...*/
    private String teken;
    
    private double posX,posY,fontsize,xscale,heigt,space,width;
    public enum type{LETTER,CIJFER,SPLITTING, NONSPLITTING}
    private type type;

    public type getType() {
        return type;
    }

    public void setType(type type) {
        this.type = type;
    }
    
    
    public String getTeken() {
        return teken;
    }

    public void setTeken(String teken) {
        this.teken = teken;
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public double getFontsize() {
        return fontsize;
    }

    public void setFontsize(double fontsize) {
        this.fontsize = fontsize;
    }

    public double getXscale() {
        return xscale;
    }

    public void setXscale(double xscale) {
        this.xscale = xscale;
    }

    public double getHeigt() {
        return heigt;
    }

    public void setHeigt(double heigt) {
        this.heigt = heigt;
    }

    public double getSpace() {
        return space;
    }

    public void setSpace(double space) {
        this.space = space;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }
    
    
    public void schrijf(){
        System.out.println(teken);
    }

    @Override
    public String toString() {
        //oke niet zo proper want ja ma bon...
        schrijfPlus();
        return "";
    }
    
    public void schrijfPlus(){
        System.out.println(teken + " " + posX + ","
                + posY + " fs=" + fontsize + " xscale="
                + xscale + " height=" + heigt + " space="
                + space + " width="
                + width );
    }
}
