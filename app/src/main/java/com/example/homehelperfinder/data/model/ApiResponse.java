package com.example.homehelperfinder.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Generic API response wrapper
 */
public class ApiResponse<T> {
    @SerializedName("success")
    private boolean success;

    @SerializedName("statusCode")
    private int statusCode;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private T data;

    @SerializedName("timestamp")
    private String timestamp;

    @SerializedName("requestId")
    private String requestId;

    // Default constructor
    public ApiResponse() {
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "success=" + success +
                ", statusCode=" + statusCode +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", timestamp='" + timestamp + '\'' +
                ", requestId='" + requestId + '\'' +
                '}';
    }
}
