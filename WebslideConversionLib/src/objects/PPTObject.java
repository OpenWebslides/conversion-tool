/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

/**
 *
 * @author Joann
 */
public interface PPTObject {
    
    /**
     * For testing, this will be different for each PPTObject instance<p>
     * Method created by KVH Industries® Copyright © Karel Vanden Houte</p>
     * <ul> 
     *      <li>A PPTList will return the toString() method of all its bullets
     * and the class of each of the bullets. Also it will return whether the list is ordered or not</li>
     *      <li>A Chart will return its title, type and content</li>
     *      <li>An Image will return it's filename, size and location</li>
     *      <li>A Slide will return the toString() of all of its objects</li>
     *      <li>A Text item will return the toString() method of all of its textparts</li>
     *      <li>A Textpart will return it's content, and extra details if he has them</li>
     * </ul>
     * @return String toString
     */
    @Override
    public String toString();
}
