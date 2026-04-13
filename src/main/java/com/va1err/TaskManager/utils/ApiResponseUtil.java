package com.va1err.TaskManager.utils;

import com.va1err.TaskManager.dto.ApiResponse;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ApiResponseUtil {

    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static  <T> ApiResponse<T> success(T data) {
        return success(data, "Success");
    }

}
