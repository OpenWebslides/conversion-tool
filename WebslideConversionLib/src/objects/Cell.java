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

    //What's inside of a cell
    private String content;
    
    //Lookup colspan on google
    private int colspan;
    
    //Loopkup rowspan on google
    private int rowspan;

    /**
     * Create a cell with it's content, colspan and rowspan
     * @param content String
     * @param colspan int
     * @param rowspan int
     */
    public Cell(String content, int colspan, int rowspan) {
        this.content = content;
        this.colspan = colspan;
        this.rowspan = rowspan;
        
    }
    
    public Cell(String content){
        this(content,1,1);
    }

    @Override
    /**
     * Return the content of a Cell
     */
    public String getContent() {
        return content;
    }

    /**
     * Set the content
     * @param content String
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Get the colspan
     * If you dont know what colspan is, look it up on google
     * @return int
     */
    public int getColspan() {
        return colspan;
    }

    /**
     * Set the colspan
     * @param colspan int
     */
    public void setColspan(int colspan) {
        this.colspan = colspan;
    }

    /**
     * Get the rowspan
     * If you dont know what rowspan is, look it up on google
     * @return int
     */
    public int getRowspan() {
        return rowspan;
    }

    /**
     * Set the rowspan
     * @param rowspan int
     */
    public void setRowspan(int rowspan) {
        this.rowspan = rowspan;
    }
    
    @Override
    /**
     * Return a string which describes the cell
     * This string contains:
     * <ul>
     * <li>cntent : what's inside of the cell</li>
     * <li>colspan : you'll find on google what this is</li>
     * <li>rowspan : you'll find on google what this is</li>
     */
    public String toString(){
        return "cell:" + content + "(" + colspan + "," + rowspan + ")";
    }
    

}
