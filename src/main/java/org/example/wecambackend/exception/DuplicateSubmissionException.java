package org.example.wecambackend.exception;

public class DuplicateSubmissionException extends RuntimeException {
    public DuplicateSubmissionException(String message) {
        super(message);
    }
}