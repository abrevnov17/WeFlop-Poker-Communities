package com.weflop.GameService.REST.Errors;

/**
 * Runtime exceptions thrown when a user tries to perform an operation that they do not
 * have permission to do.
 * 
 * @author abrevnov
 */
public class ForbiddenOperationException extends RuntimeException {

	private static final long serialVersionUID = 7993416488376381728L;
	
	public ForbiddenOperationException(String message) {
        super(message);
    }
}
