/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blp.util;

import java.io.Serializable;

/**
 *
 * @author Abir
 */
public class BLPException extends Exception implements Serializable {
    
   /**
    * Constructor
    * @param Error message
    */
    public BLPException(String message){
        super(message);      
    }
    
}
