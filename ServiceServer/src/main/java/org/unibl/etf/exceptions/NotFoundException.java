package org.unibl.etf.exceptions;

public class NotFoundException extends Exception {
	private static final long serialVersionUID = -2108941649329950945L;

	public NotFoundException() {
		super();
	}
	
	public NotFoundException(String message) {
		super(message);
	}
}
