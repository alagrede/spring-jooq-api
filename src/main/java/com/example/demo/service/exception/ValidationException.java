package com.example.demo.service.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.FieldError;

public class ValidationException extends Exception {
	
	private List<FieldError> errors = new ArrayList<>();
	
	public ValidationException(List<FieldError> errors) {
		super();
		this.errors = errors;
	}

	public List<FieldError> getErrors() {
		return errors;
	}
	
}
