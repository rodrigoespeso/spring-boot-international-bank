package com.rodrigoespeso.spbre.service.exception;

public class BusinessNotFoundException extends BusinessException{

	private static final long serialVersionUID = 7521367751450528293L;

	public BusinessNotFoundException(String message) {
		super(message);
	}
	
	public BusinessNotFoundException(String message, Throwable e) {
		super(message, e);
	}
}
