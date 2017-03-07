/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openwebslides.objects;

import java.util.ArrayList;
import java.util.List;


public class Slide implements PPTObject {

    private List<PPTObject> pptObjects;

    public Slide() {
        pptObjects = new ArrayList<>();
    }

    @Override
    public String toHtml(int indentation) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
