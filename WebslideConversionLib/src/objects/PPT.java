package objects;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Karel
 * The base object of a PowerPoint
 */
public class PPT {

    //The slides of the PowerPoint
    private final List<Slide> slides;
    
    //Insights of the PowerPoint
    private PPTInsight insight;

    /**
     * Create a PPT object. 
     * The PPT object is an approximation of a PowerPoint, but in-memory and with a custom built object set
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

    /**
     * Return the insights of the PowerPoint
     * @return  PPTInsight
     */
    public PPTInsight getInsight() {
        return insight;
    }

    /**
     * Set the insight of the PowerPoint
     * @param insight PPTInsight
     */
    public void setInsight(PPTInsight insight) {
        this.insight = insight;
    }
    
    @Override
    /**
     * Return a String with the data in the PPT object
     */
    public String toString(){
        String toret = "";
        for(Slide slide : slides){
            toret += (slide.toString());
        }
        return toret;
    }
    
}
