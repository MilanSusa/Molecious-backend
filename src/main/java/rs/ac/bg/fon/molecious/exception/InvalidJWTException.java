package rs.ac.bg.fon.molecious.exception;

public class InvalidJWTException extends RuntimeException {

    public InvalidJWTException(String message) {
        super(message);
    }
}
