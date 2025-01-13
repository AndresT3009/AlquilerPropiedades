package com.alquiler.AlquilerPropiedades.exceptions;

public class ErrorResponse {
    private String userMessage;
    private String technicalMessage;

    public ErrorResponse(String userMessage, String technicalMessage) {
        this.userMessage = userMessage;
        this.technicalMessage = technicalMessage;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    public String getTechnicalMessage() {
        return technicalMessage;
    }

    public void setTechnicalMessage(String technicalMessage) {
        this.technicalMessage = technicalMessage;
    }
}
