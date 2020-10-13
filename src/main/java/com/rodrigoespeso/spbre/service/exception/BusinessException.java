package com.rodrigoespeso.spbre.service.exception;

public class BusinessException extends RuntimeException{

	private static final long serialVersionUID = -3288332723476680611L;

	public BusinessException(String message) {
		super(message);
	}
	
	public BusinessException(String message, Throwable e) {
		super(message, e);
	}
}
