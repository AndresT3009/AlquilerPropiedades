package com.alquiler.AlquilerPropiedades.infrastructure.exceptions;

public class ErrorResponse {
    private String technicalMessage;
    private String userMessage;

    public ErrorResponse(String technicalMessage,String userMessage) {
        this.technicalMessage = technicalMessage;
        this.userMessage = userMessage;
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
