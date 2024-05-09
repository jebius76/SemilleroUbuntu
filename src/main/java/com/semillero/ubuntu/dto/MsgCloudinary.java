package com.semillero.ubuntu.dto;

public class MsgCloudinary {
    private String message;

    public MsgCloudinary(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
