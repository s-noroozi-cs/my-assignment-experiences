package com.backbase.rest.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "root")
public class ErrorDto {
    private boolean response = false;
    private String error;

    public ErrorDto() {
    }

    public ErrorDto(String error) {
        this.error = error;
    }

    @XmlAttribute(name = "response")
    public boolean isResponse() {
        return response;
    }

    public void setResponse(boolean response) {
        this.response = response;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
