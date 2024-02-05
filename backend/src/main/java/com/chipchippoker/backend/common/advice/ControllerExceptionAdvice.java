package com.chipchippoker.backend.common.advice;

import static com.chipchippoker.backend.common.dto.ErrorBase.*;
import static java.util.stream.Collectors.*;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.chipchippoker.backend.common.dto.ApiResponse;
import com.chipchippoker.backend.common.exception.ChipChipPokerBaseException;
import com.chipchippoker.backend.common.exception.UnAuthorizedException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ControllerExceptionAdvice extends ResponseEntityExceptionHandler {

	/**
	 * 400 BadRequest
	 * Dto Validation & Binding
	 */
	@Override
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
		HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		String errorMessage = e.getBindingResult().getFieldErrors().stream()
			.map(DefaultMessageSourceResolvable::getDefaultMessage)
			.collect(joining("\n"));
		log.error("BindException: {}", errorMessage);

		return ResponseEntity.status(E400_INVALID_VALIDATION.getStatus())
			.body(ApiResponse.error(E400_INVALID_VALIDATION, errorMessage));
	}

	/**
	 * 400 BadRequest
	 * RequestParam 필수 필드가 입력되지 않은 경우 발생하는 Exception
	 */
	@Override
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException e,
		HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		log.warn(e.getMessage());

		return ResponseEntity.status(E400_MISSING_PARAMETER.getStatus())
			.body(
				ApiResponse.error(E400_MISSING_PARAMETER, String.format("필수 파라미터 (%s)를 입력해주세요", e.getParameterName())));
	}

	/**
	 * 400 BadRequest
	 * RequestPart 필수 필드가 입력되지 않은 경우 발생하는 Exception
	 */
	@Override
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException e,
		HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		log.warn(e.getMessage());

		return ResponseEntity.status(E400_MISSING_PARAMETER.getStatus())
			.body(ApiResponse.error(E400_MISSING_MULTIPART,
				String.format("Multipart (%s)를 입력해주세요", e.getRequestPartName())));
	}

	/**
	 * 400 BadRequest
	 * 필수 Path Variable 가 입력되지 않은 경우 발생하는 Exception
	 */
	@Override
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException e, HttpHeaders headers,
		HttpStatusCode status, WebRequest request) {
		log.warn(e.getMessage());

		return ResponseEntity.status(E400_MISSING_PATH_VARIABLE.getStatus())
			.body(
				ApiResponse.error(E400_MISSING_PATH_VARIABLE, String.format("Path (%s)를 입력해주세요", e.getVariableName())));
	}

	/**
	 * 405 Method Not Allowed
	 * 잘못된 HTTP method 호출 할 경우 발생하는 Exception
	 */
	@Override
	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e,
		HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		log.warn(e.getMessage());

		return ResponseEntity.status(E405_METHOD_NOT_ALLOWED.getStatus())
			.body(ApiResponse.error(E405_METHOD_NOT_ALLOWED));
	}

	/**
	 * 500 Internal Server
	 */

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	private ResponseEntity<ApiResponse<Object>> handleException(Exception exception, HttpServletRequest request) {
		log.error(exception.getMessage(), exception);

		return ResponseEntity.status(E500_INTERNAL_SERVER.getStatus())
			.body(ApiResponse.error(E500_INTERNAL_SERVER));
	}

	/**
	 * Bonheur Custom Exception
	 */
	@ExceptionHandler(ChipChipPokerBaseException.class)
	private ResponseEntity<ApiResponse<Object>> handleBaseException(ChipChipPokerBaseException exception,
		HttpServletRequest request) {
		log.error(exception.getMessage(), exception);

		return ResponseEntity.status(exception.getErrorBase().getStatus())
			.body(ApiResponse.error(exception.getErrorBase()));
	}

	@ExceptionHandler(UnAuthorizedException.class)
	private ResponseEntity<ApiResponse<Object>> handleException(ChipChipPokerBaseException exception,
		HttpServletRequest request) {
		log.error(exception.getMessage(), exception);

		return ResponseEntity.status(exception.getErrorBase().getStatus())
			.body(ApiResponse.error(exception.getErrorBase(), request.getAttribute("new-token")));
	}
}
