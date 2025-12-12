package org.unibl.etf.exceptions;

public class InvalidCredentialsException extends Exception {
	private static final long serialVersionUID = 7271566165436641592L;

	public InvalidCredentialsException() {
		super();
	}
	
	public InvalidCredentialsException(String message) {
		super(message);
	}
}
