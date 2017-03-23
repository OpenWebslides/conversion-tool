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
    private HashMap<String, ArrayList<Double>> content;
    private String chartType;
    

    public Chart() {
       this.content = new HashMap<>();
    }

    public String getTitle() {
        return title;
    }

    public HashMap<String, ArrayList<Double>> getContent() {
        return content;
    }

    public String getChartType() {
        return chartType;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setChartType(String chartType) {
        this.chartType = chartType;
    }
    public String toString(){
        return title;
    }
    
}
