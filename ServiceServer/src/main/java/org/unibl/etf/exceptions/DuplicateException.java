package org.unibl.etf.exceptions;

public class DuplicateException extends Exception {
	private static final long serialVersionUID = 6591198026702302308L;
	
	public DuplicateException() {
		super();
	}
	
	public DuplicateException(String message) {
		super(message);
	}

}
