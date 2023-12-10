package com.example.myapplication.Model;

import java.util.List;

public class OTPmodel {

    private String errorCode;
    private String errorMessage;
    private String jobId;
    private List<MessageData> messageData;


    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public List<MessageData> getMessageData() {
        return messageData;
    }

    public void setMessageData(List<MessageData> messageData) {
        this.messageData = messageData;
    }

    public class MessageData{

        private String number;
        private String messageId;
        private String message;

    }
}

