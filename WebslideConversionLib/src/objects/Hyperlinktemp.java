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
public class Hyperlinktemp extends Textpart {
    
    private String rid;
    
    public Hyperlinktemp(Textpart textpart) {
        this.setFont(textpart.getFont());
        this.setType(textpart.getType());
        this.setSize(textpart.getSize());
        this.setContent(textpart.getContent());
        this.setCharachterSpacing(textpart.getCharachterSpacing());
        this.setColor(textpart.getColor());
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }
}
