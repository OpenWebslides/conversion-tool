package objects;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PPT {

    //metadata
    private final List<Slide> slides;

    /**
     * Create an PPT objectS
     */
    public PPT() {
        slides = new ArrayList<>();
    }

    /**
     * Return the slides
     * @return 
     */
    public List<Slide> getSlides() {
        return slides;
    }
    
}
