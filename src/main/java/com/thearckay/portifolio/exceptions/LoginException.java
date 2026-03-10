package com.thearckay.portifolio.exceptions;

public class LoginException extends RuntimeException {
    private Integer status;

    public LoginException(String message, Integer statusCode) {
        super(message);
        this.status = statusCode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
