package objects;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PPT {

    //metadata
    private final List<Slide> slides;

    /**
     * Create a PPT object
     */
    public PPT() {
        slides = new ArrayList<>();
    }

    /**
     * Return the slides, this will be an editable list.
     * Any changes made to this list will be saved in the PPT object
     * @return 
     */
    public List<Slide> getSlides() {
        return slides;
    }
    
}
