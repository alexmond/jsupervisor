package org.alexmond.jsupervisor.exception;


import lombok.Getter;

@Getter
public class JSupervisorException extends RuntimeException {
    private String message;
    private String error;

    public JSupervisorException(String message) {
        super(message);
        this.message = message;
    }

    public JSupervisorException(String message, String error) {
        super(message);
        this.message = message;
        this.error = error;
    }

}