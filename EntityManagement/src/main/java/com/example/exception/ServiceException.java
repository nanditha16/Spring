package com.example.exception;

public class ServiceException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4844432412602431725L;

	public ServiceException (String msg) {
        super(msg);
    }
}
