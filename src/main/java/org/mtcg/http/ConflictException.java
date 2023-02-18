package org.mtcg.http;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
