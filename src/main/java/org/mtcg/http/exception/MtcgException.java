package org.mtcg.http.exception;

import org.mtcg.http.HttpStatus;

public class MtcgException extends RuntimeException {
    private HttpStatus status;

    public MtcgException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
