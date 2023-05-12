package com.vima.accommodation.exception;

import org.springframework.http.HttpStatus;

public class SpecialInfoException extends BaseException {
	public SpecialInfoException() {
		super("Error while adding new special period.", HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value());
	}
}
