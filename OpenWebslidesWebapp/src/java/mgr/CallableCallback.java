/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgr;

/**
 *
 * @author Laurens
 */
public interface CallableCallback {

    /**
     *This method is used to signal completion of the Callable, this will provide a non-blocking way as opposed to the get() and isDone() methods of Callable
     * @param id the callable will fill this with its id
     * @param status a flag indicating whether the callable finished its job successfully or not
     */
    void callableComplete(long id,int status);
}
