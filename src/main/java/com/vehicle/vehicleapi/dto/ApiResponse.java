package com.vehicle.vehicleapi.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;

    //Due to lombok again
    // public ApiResponse(){}

    // public ApiResponse(
    //         boolean success,
    //         String message,
    //         T data
    // ){
    //     this.success = success;
    //     this.message = message;
    //     this.data = data;
    // }

    // // getters/setters

    // public boolean isSuccess() {
    //     return success;
    // }

    // public void setSuccess(boolean success) {
    //     this.success = success;
    // }

    // public String getMessage() {
    //     return message;
    // }

    // public void setMessage(String message) {
    //     this.message = message;
    // }

    // public T getData() {
    //     return data;
    // }

    // public void setData(T data) {
    //     this.data = data;
    // }
}
