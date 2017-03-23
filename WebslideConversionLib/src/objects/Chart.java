/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Karel
 */
public class Chart implements PPTObject {

    private String title;
    private final HashMap<String, ArrayList<Double>> content;
    private String chartType;
    

    /**
     * Create a Chart object
     * An empty HashMap of its content will be created
     */
    public Chart() {
       this.content = new HashMap<>();
    }

    /**
     * Return the title of the chart
     * @return 
     */
    public String getTitle() {
        return title;
    }

    /**
     * Return the content of the chart, this is an editable HashMap
     * Changes to this HashMap will be changed in the Chart object
     * @return 
     */
    public HashMap<String, ArrayList<Double>> getContent() {
        return content;
    }

    /**
     * Return the Chart type
     * @return 
     */
    public String getChartType() {
        return chartType;
    }

    /**
     * Set the Chart title
     * @param title 
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Set the Chart type
     * @param chartType 
     */
    public void setChartType(String chartType) {
        this.chartType = chartType;
    }
    
    
    @Override
    /**
     * Return a string containing:
     * <ul>
     *      <li>Title</li>
     *      <li>Type</li>
     *      <li>Content</li>
     * </ul>
     */
    public String toString(){
        return title;
    }
    
}
