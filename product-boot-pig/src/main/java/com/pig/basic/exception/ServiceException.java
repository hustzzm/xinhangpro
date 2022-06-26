package com.pig.basic.exception;

public class ServiceException extends RuntimeException {

    private String returnMessage;
    private String code = "500";

    public ServiceException() {}

    public ServiceException(String returnMessage) {
        this.returnMessage = returnMessage;
    }

    public ServiceException(String returnMessage,String code) {
        this.returnMessage = returnMessage;
        this.code = code;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
