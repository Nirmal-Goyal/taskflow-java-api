package com.nirmal.taskflow.dto;

import java.time.Instant;
import java.time.LocalDateTime;

public class ApiError {

    private int status;
    private String message;
    private Instant timestamp;

    public ApiError(int status, String message, Instant timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getMessage(){
        return message;
    }

    public int getStatus(){
        return status;
    }

    public Instant getTimestamp(){
        return timestamp;
    }
}
