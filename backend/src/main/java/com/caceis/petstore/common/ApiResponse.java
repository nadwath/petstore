package com.caceis.petstore.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    private Integer code;
    private String message;
    private String timestamp;
    private String path;
    private Map<String, String> error;
    private T data;

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .code(HttpStatus.OK.value())
                .message("success")
                .timestamp(LocalDateTime.now().toString())
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> created(T data) {
        return ApiResponse.<T>builder()
                .code(HttpStatus.CREATED.value())
                .message("created")
                .timestamp(LocalDateTime.now().toString())
                .data(data)
                .build();
    }

    public static ApiResponse<Void> error(Integer code, String message) {
        return ApiResponse.<Void>builder()
                .code(code)
                .message(message)
                .timestamp(LocalDateTime.now().toString())
                .build();
    }

    public static ApiResponse<Void> error(Integer code, String message, Map<String, String> error) {
        return ApiResponse.<Void>builder()
                .code(code)
                .message(message)
                .timestamp(LocalDateTime.now().toString())
                .error(error)
                .build();
    }
}
