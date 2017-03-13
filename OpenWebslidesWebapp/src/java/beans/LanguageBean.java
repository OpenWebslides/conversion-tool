/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

/**
 *
 * @author Laurens
 */
public class LanguageBean implements Serializable {

    /**
     * Creates a new instance of LanguageBean
     */
    private static final long serialVersionUID = 1L;
    private String localeCode;
    
    private final Map<String, Object> countries;

    public LanguageBean(){
        countries = new LinkedHashMap<>();        
        countries.put("nl",new Locale("nl","BE"));
        countries.put("en", Locale.ENGLISH);        
    }

    public Map<String, Object> getCountriesInMap() {        
        return countries;
    }

    public String getLocaleCode() {
        return localeCode;
    }

    public void setLocaleCode(String localeCode) {
        this.localeCode = localeCode;
    }

    public void countryLocaleCodeChanged(ValueChangeEvent e) {
        
        String newLocaleValue = e.getNewValue().toString();        
        
        //loop country map to compare the locale code
        for (Map.Entry<String, Object> entry : countries.entrySet()) {

            if (entry.getValue().toString().equals(newLocaleValue)) {
                FacesContext.getCurrentInstance().getViewRoot().setLocale((Locale) entry.getValue());
            }
        }
    }

}
