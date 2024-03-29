package db;

public class DbIntegrityException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DbIntegrityException() { }
	
	public DbIntegrityException(String message) {
		super(message);
	}
}