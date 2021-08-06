package com.playtomic.tests.wallet.exception;

import java.util.Objects;

public class AppError {

    private String message;

    public AppError(String message) {
        this.message = message;
    }

    public AppError() {
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppError appError = (AppError) o;
        return Objects.equals(message, appError.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message);
    }
}
