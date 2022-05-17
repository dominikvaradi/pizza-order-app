package hu.dominikvaradi.pizzaorderapp.service.exception;

public class ConflictException extends Exception {
    public ConflictException(String message) {
        super(message);
    }
}
