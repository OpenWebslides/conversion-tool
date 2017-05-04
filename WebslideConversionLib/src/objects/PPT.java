package objects;

import java.util.ArrayList;
import java.util.List;

public class PPT {

    //metadata
    private final List<Slide> slides;
    private PPTInsight insight;

    /**
     * Create a PPT object
     */
    public PPT() {
        slides = new ArrayList<>();
        insight = new PPTInsight();
    }

    /**
     * Return the slides, this will be an editable list.
     * Any changes made to this list will be saved in the PPT object
     * @return List slides
     */
    public List<Slide> getSlides() {
        return slides;
    }

    public PPTInsight getInsight() {
        return insight;
    }

    public void setInsight(PPTInsight insight) {
        this.insight = insight;
    }
    
    @Override
    public String toString(){
        String toret = "";
        for(Slide slide : slides){
            toret += (slide.toString());
        }
        return toret;
    }
    
}
