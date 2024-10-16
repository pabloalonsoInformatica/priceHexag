package com.inditex.contexts.shared.domain;

import java.time.LocalDateTime;


public class AppError extends Error {
    private final int status;
    private final String date;

    public AppError(String message) {
        super(message);
        this.status = 500;
        this.date = LocalDateTime.now().toString();
    }

    public AppError(String message, int status) {
        super(message);
        this.status = status;
        this.date = LocalDateTime.now().toString();
    }

    public AppError(String message, int status, LocalDateTime date) {
        super(message);
        this.status = status;
        this.date = date.toString();
    }

    @Override
    public String toString() {
        return "AppError{" +
                "message=" + this.getMessage() +
                "status=" + status +
                ", date=" + date +
                '}';
    }

    public String toJson() {
        return "{ AppError{" +
                "\"message\":" + this.getMessage() +
                ", \"status\":" + status +
                ", \"date:" + date +
                "}}";
    }
}
