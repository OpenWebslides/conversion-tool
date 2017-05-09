/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversion.pdf.util;

/**
 *This class serves to suppres warnings of a missmatch between the used libraries 
 * @author Gertjan
 */

    	






import technology.tabula.ObjectExtractor;
import technology.tabula.PageIterator;

public class myPageIterator extends PageIterator {

    public myPageIterator(ObjectExtractor oe, Iterable<Integer> pages) {
        super(oe, pages);
    }
    

}
 
