/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import java.util.Arrays;
import java.util.HashSet;

/**
 *
 * @author Karel + Gertjan
 * Is a series of charachters with the same details (font, size,)
 */
public class Textpart implements PPTObject {

    //The font of the Textpart
    private String font;
    
    //Bold/Underlined/Italic/Strikethrough
    private HashSet<FontDecoration> type;
    
    //The size of the Textpart
    private int size;
    
    //The charachters of the Textpart
    private String content = "";
    
    //The space between the charachters
    private int charachterSpacing;
    
    //The color
    private String color;
    
    //Used for determining space charachters
    private boolean dirty = false;
    
    //Used for determining space charachters
    private boolean err = false;
    
    //The location of the textpart
    private double XPosition, YPosition;

    /**
     * Get the Y Position of the Textpart
     * @return double
     */
    public double getYPosition() {
        return YPosition;
    }

    /**
     * Set the Y Position of the Textpart
     * @param YPosition double
     */
    public void setYPosition(double YPosition) {
        this.YPosition = YPosition;
    }

     /**
     * Get the X Position of the Textpart
     * @return double
     */
    public double getXPosition() {
        return XPosition;
    }
    
    /**
     * Set the X Position of the Textpart
     * @param XPosition double
     */
    public void setXPosition(double XPosition) {
        this.XPosition = XPosition;
    }

    /**
     * Create a new Textpart instance
     * Default font will be Calibri
     */
    public Textpart() {
        type = new HashSet<>();
        this.font = "Calibri";
    }

    /**
     * Copyconstructor of a Textpart
     * @param tp Textpart
     */
    public Textpart(Textpart tp) {
        this.font = tp.getFont();
        this.type = tp.getType();
        this.size = tp.getSize();
        this.content = tp.getContent();
        this.charachterSpacing = tp.getCharachterSpacing();
        this.color = tp.getColor();
        this.dirty = tp.isDirty();
        this.XPosition = tp.getXPosition();
        this.YPosition = tp.getYPosition();
    }

    /**
     * Returns the font of a Textpart
     * @return String
     */
    public String getFont() {
        return font;
    }

    /**
     * Set the font of a Textpart
     * @param font String
     */
    public void setFont(String font) {
        this.font = font;
    }

    /**
     * Get the size of a Textpart
     * @return int
     */
    public int getSize() {
        return size;
    }

    /**
     * Set the size of a Textpart
     * @param size int
     */
    public void setSize(int size) {
        this.size = size;
    }

    @Override
    /**
     * Return the content of the Textpart
     */
    public String getContent() {
        return content;
    }

    /**
     * Set the content of the Textpart
     * @param content String
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Add a fontdecoration type
     * @param f FontDecoration
     */
    public void addType(FontDecoration f) {
        type.add(f);
    }

    @Override
    /**
     * Return the data behind a Textpart: content, font, size,...
     */
    public String toString() {
        return this.getClass() + ": " + content + "  |font: " + font + "  |size: " + size + "  |type: " + Arrays.toString(type.toArray()) + "  |charachterSpacing: " + charachterSpacing;
    }

    /**
     * Return a HashSet with the types of a Textpart
     * @return HashSet
     */
    public HashSet<FontDecoration> getType() {
        return type;
    }

    /**
     * Set the font decoration types
     * @param type HashSet
     */
    public void setType(HashSet<FontDecoration> type) {
        this.type = type;
    }

    /**
     * Get the space between charachters
     * @return int
     */
    public int getCharachterSpacing() {
        return charachterSpacing;
    }

    /**
     * Set the space between charachters
     * @param charachterSpacing int
     */
    public void setCharachterSpacing(int charachterSpacing) {
        this.charachterSpacing = charachterSpacing;
    }

    /**
     * Get the color of a Textpart
     * @return String
     */
    public String getColor() {
        return color;
    }

    /**
     * Set the color of a Textpart
     * @param color String
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Used to determine spaces in between charachters
     * @return boolean
     */
    public boolean isDirty() {
        return dirty;
    }

    /**
     * Used to determine spaces in between charachters
     * @param dirty boolean
     */
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    /**
     * Used to determine spaces in between charachters
     * @return boolean
     */
    public boolean isErr() {
        return err;
    }

    /**
     * Used to determine spaces in between charachters
     * @param err boolean
     */
    public void setErr(boolean err) {
        this.err = err;
    }
}
