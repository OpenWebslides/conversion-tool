package objects;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PPT {

    //metadata
    private List<Slide> slides;
    private File ppt;

    public PPT(File file) {
        ppt = file;
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


    public File getPpt() {
        return ppt;
    }

    
}
