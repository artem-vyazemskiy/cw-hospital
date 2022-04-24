package main.exception;

public class PersonNotExistsException extends Exception {
    public PersonNotExistsException(String message) {
        super(message);
    }
}
