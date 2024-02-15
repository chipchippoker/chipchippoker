package com.chipchippoker.backend.common.dto;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@JsonPropertyOrder({"code", "message", "data"})
@RequiredArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

	private final String code;
	private final String message;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private T data;

	public static <T> ApiResponse<T> success() {
		return new ApiResponse<>("성공", HttpStatus.OK.getReasonPhrase());
	}

	public static <T> ApiResponse<T> success(T data) {
		return new ApiResponse<>("성공", HttpStatus.OK.getReasonPhrase(), data);
	}

	public static <T> ApiResponse<T> messageSuccess(MessageBase messageBase) {
		return new ApiResponse<>(messageBase.getCode(), messageBase.getMessage(), null);
	}

	public static <T> ApiResponse<T> messageSuccess(MessageBase messageBase, T data) {
		return new ApiResponse<>(messageBase.getCode(), messageBase.getMessage(), data);
	}

	public static <T> ApiResponse<T> error(ErrorBase errorBase) {
		return new ApiResponse<>(errorBase.getCode(), errorBase.getMessage());
	}

	public static ApiResponse<Exception> error(ErrorBase errorBase, Exception e) {
		return new ApiResponse<>(errorBase.getCode(), errorBase.getMessage(), e);
	}

	public static <T> ApiResponse<T> error(ErrorBase errorBase, T data) {
		return new ApiResponse<>(errorBase.getCode(), errorBase.getMessage(), data);
	}

	public static <T> ApiResponse<T> error(ErrorBase errorBase, String message) {
		return new ApiResponse<>(errorBase.getCode(), message, null);
	}

	public static ApiResponse<Exception> messageError(MessageBase messageBase) {
		return new ApiResponse<>(messageBase.getCode(), messageBase.getMessage(), null);
	}

	public static ApiResponse<Exception> messageError(MessageBase messageBase, String message) {
		return new ApiResponse<>(messageBase.getCode(), message, null);
	}
}
