package com.cloud.google.exception;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebInputException;

import com.cloud.google.utility.APIResponse;
import com.cloud.google.utility.EmployeeResponse;

@RestControllerAdvice
public class RestExceptionHandler {
	private final String DOUBLE_QUOTE_STRING = "\"";
	private final String BLANK_STRING = "";

	@ExceptionHandler(WebExchangeBindException.class)
	public ResponseEntity<APIResponse> handleWebExchangeBindException(WebExchangeBindException webExchangeBindException) {
		Set<String> errorSet = webExchangeBindException.getBindingResult().getFieldErrors().stream()
				.map(fieldError -> fieldError.getDefaultMessage()).collect(Collectors.toSet());

		
		 return new ResponseEntity<>(EmployeeResponse.builder().errors(errorSet)
					.statusCode(webExchangeBindException.getStatus().value()).build(), null, webExchangeBindException.getStatus());
		
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<APIResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
		Set<String> errorSet = exception.getBindingResult().getFieldErrors().stream()
				.map(fieldError -> fieldError.getDefaultMessage()).collect(Collectors.toSet());
		 return new ResponseEntity<>(EmployeeResponse.builder().message(errorSet)
					.statusCode(HttpStatus.BAD_REQUEST.value()).build(), null, HttpStatus.BAD_REQUEST);
		 
	}	
	@ExceptionHandler(RestApiException.class)
	public ResponseEntity<APIResponse> handleContentEdCustomException(RestApiException exception) {

		 return new ResponseEntity<>(EmployeeResponse.builder().message(exception.getErrorMessage())
					.statusCode(exception.getHttpStatus().value()).build(), null, exception.getHttpStatus());
		 
	}

	@ExceptionHandler(ServerWebInputException.class)
	public ResponseEntity<APIResponse> handleServerWebInputException(ServerWebInputException serverWebInputException) {

		 return new ResponseEntity<>(EmployeeResponse.builder()
					.message(serverWebInputException.getMessage().replaceAll(DOUBLE_QUOTE_STRING, BLANK_STRING))
					.statusCode(serverWebInputException.getStatus().value()).build(), null, serverWebInputException.getStatus());
	}
}
