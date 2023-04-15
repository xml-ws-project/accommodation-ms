package com.vima.accommodation.exception;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class BaseException extends RuntimeException {

	protected HttpStatus status;
	protected int statusCode;

	public BaseException(String message, HttpStatus status, int code) {
		super(message);
		this.status = status;
		this.statusCode = code;
	}
}
