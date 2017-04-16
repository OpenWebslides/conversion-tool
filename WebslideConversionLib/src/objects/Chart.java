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
    private final HashMap<String, HashMap<String, ArrayList<Double>>> content;
    private ChartType chartType;
    private String id;
    

    /**
     * Create a Chart object
     * An empty HashMap of its content will be created
     * @param id String
     */
    public Chart(String id) {
       this.content = new HashMap<>();
       this.id = id;
    }

    /**
     * Return the title of the chart
     * @return String title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Return the content of the chart, this is an editable HashMap
     * Changes to this HashMap will be changed in the Chart object
     * @return HashMap content
     */
    public HashMap<String, HashMap<String, ArrayList<Double>>> getContent() {
        return content;
    }

    /**
     * Return the Chart type
     * @return ChartType charttype
     */
    public ChartType getChartType() {
        return chartType;
    }

    /**
     * Set the Chart title
     * @param title String
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Set the Chart type
     * @param chartType Charttype
     */
    public void setChartType(ChartType chartType) {
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
        String toret = title + "\n" + chartType + "\n";
        for(String serie : content.keySet()){
            toret += serie + "\n";
            for(String category : content.get(serie).keySet()){
                toret += "\t" + category + "\n";
                for(double d : content.get(serie).get(category)){
                toret += "\t\t" + d + "\n";
                }
            }
        }
        return toret;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
}
