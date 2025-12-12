package exceptions;

public class InvalidLoginException extends Exception {
	private static final long serialVersionUID = 4274900835186862209L;

	public InvalidLoginException() {
		super();
	}
	
	public InvalidLoginException(String message) {
		super(message);
	}
}
