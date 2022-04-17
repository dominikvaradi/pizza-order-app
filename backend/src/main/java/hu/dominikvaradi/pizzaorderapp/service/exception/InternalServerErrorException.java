package hu.dominikvaradi.pizzaorderapp.service.exception;

public class InternalServerErrorException extends Exception {
    public InternalServerErrorException(String message) {
        super(message);
    }
}
