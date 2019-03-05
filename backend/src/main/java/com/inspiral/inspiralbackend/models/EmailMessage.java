package com.inspiral.inspiralbackend.models;

public class EmailMessage {

    private String to_address;
    private String subject;
    private String body;

    public String getTo_address() {
        return to_address;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public void setTo_address(String to_address) {
        this.to_address = to_address;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setBody(String body) {
        this.body = body;
    }

}
