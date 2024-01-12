package com.chipchippoker.backend.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@JsonPropertyOrder({"code", "message", "data"})
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse<T> {

    private final String code;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public static ApiResponse<?> success() {
        return new ApiResponse<>("성공", HttpStatus.OK.getReasonPhrase());
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("성공", HttpStatus.OK.getReasonPhrase(), data);
    }

    public static ApiResponse<?> error(ErrorBase errorBase) {
        return new ApiResponse<>(errorBase.getCode(), errorBase.getMessage());
    }

    public static ApiResponse<Exception> error(ErrorBase errorBase, Exception e) {
        return new ApiResponse<>(errorBase.getCode(), errorBase.getMessage(), e);
    }

    public static <T> ApiResponse<T> error(ErrorBase errorBase, T data) {
        return new ApiResponse<>(errorBase.getCode(), errorBase.getMessage(), data);
    }
}
