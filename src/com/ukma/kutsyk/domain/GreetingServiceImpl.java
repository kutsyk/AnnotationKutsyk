package com.ukma.kutsyk.domain;

public class GreetingServiceImpl implements GreetingService {
    private String message;
    
    public GreetingServiceImpl(String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}
