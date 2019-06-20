package com.platform.common.exception;

/**
 * 
 * @Version 1.0.0
 * 
 * @JDK version used 6.0
 * 
 * @Modification history none
 * 
 * @Modified by none
 */
public class PersistenceException extends Exception {

	private static final long serialVersionUID = -2332513847638058277L;

	public PersistenceException() {
		super();
	}

	public PersistenceException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public PersistenceException(String arg0) {
		super(arg0);
	}

	public PersistenceException(Throwable arg0) {
		super(arg0);
	}

}
