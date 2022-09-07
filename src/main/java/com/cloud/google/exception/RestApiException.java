package com.cloud.google.exception;


import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;

public class RestApiException extends RuntimeException {

	private static final long serialVersionUID = -9106365546089010911L;
	private final HttpStatus httpStatus;
	private List<String> errorMessage=new ArrayList<>();
	
	public RestApiException(Object message, HttpStatus httpStatus) {
		super(message.toString());
		this.httpStatus = httpStatus;
		this.errorMessage.add(message.toString());
	}
	
	public RestApiException(String message, HttpStatus httpStatus) {
		super(message);
		this.httpStatus = httpStatus;
		this.errorMessage.add(message);
	}
	public RestApiException(List<String> message, HttpStatus httpStatus) {
		super(message.toString());
		this.httpStatus = httpStatus;
		this.errorMessage.addAll(message);		
	}
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}
	public List<String> getErrorMessage() {
		return errorMessage;
	}

	 
}
