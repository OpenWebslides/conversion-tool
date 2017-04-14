/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

/**
 *
 * @author Karel
 */
public class Cell implements PPTObject {

    private String content;
    private int colspan;
    private int rowspan;

    public Cell(String content, int colspan, int rowspan) {
        this.content = content;
        this.colspan = colspan;
        this.rowspan = rowspan;
        
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getColspan() {
        return colspan;
    }

    public void setColspan(int colspan) {
        this.colspan = colspan;
    }

    public int getRowspan() {
        return rowspan;
    }

    public void setRowspan(int rowspan) {
        this.rowspan = rowspan;
    }
    
    public String toString(){
        return "cell:" + content + "(" + colspan + "," + rowspan + ")";
    }

}
