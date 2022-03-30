package main.exception;

public class WardIsUsedException extends Exception {
    public WardIsUsedException(String message) {
        super(message);
    }
}
