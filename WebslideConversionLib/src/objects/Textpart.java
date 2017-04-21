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
 * @author Karel
 */
public class Textpart implements PPTObject {

    private String font;
    private HashSet<FontDecoration> type;
    private int size;
    private String content = "";
    private int charachterSpacing;
    private String color;
    private boolean dirty = false;
    private boolean err = false;

    public Textpart() {
        type = new HashSet<>();
        this.font = "Calibri";
    }

    public Textpart(Textpart tp) {
        this.font = tp.getFont();
        this.type = tp.getType();
        this.size = tp.getSize();
        this.content = tp.getContent();
        this.charachterSpacing = tp.getCharachterSpacing();
        this.color = tp.getColor();
        this.dirty = tp.isDirty();
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void addType(FontDecoration f) {
        type.add(f);
    }

    @Override
    public String toString() {

        return this.getClass() + ": " + content + "  |font: " + font +  "  |size: " + size + "  |type: " + Arrays.toString(type.toArray())+ "  |charachterSpacing: " + charachterSpacing;
        
       /* try {
            if (content != null || content.equals("")) {
                return content;
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }*/

    }

    public HashSet<FontDecoration> getType() {
        return type;
    }

    public void setType(HashSet<FontDecoration> type) {
        this.type = type;
    }

    public int getCharachterSpacing() {
        return charachterSpacing;
    }

    public void setCharachterSpacing(int charachterSpacing) {
        this.charachterSpacing = charachterSpacing;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public boolean isErr() {
        return err;
    }

    public void setErr(boolean err) {
        this.err = err;
    }
}
