package objects;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PPT {

    //metadata
    private List<Slide> slides;

    public PPT() {
        slides = new ArrayList<>();
    }

    public String toHTML() {
        String temp = "";
        for(PPTObject obj : slides){
             temp += obj.toHtml(0);
        }
        return temp;
    }

    public List<Slide> getSlides() {
        return slides;
    }
    
}
