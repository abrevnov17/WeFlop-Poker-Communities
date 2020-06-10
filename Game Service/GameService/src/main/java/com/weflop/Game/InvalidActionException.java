package com.weflop.Game;

/**
 * Exception describing an invalid Action type.
 * 
 * @author abrevnov
 *
 */
public class InvalidActionException extends Exception { 

	// used for serialization...can be ignored
	private static final long serialVersionUID = 3633383730449105142L;

	public InvalidActionException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}