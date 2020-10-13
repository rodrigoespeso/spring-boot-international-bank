package com.rodrigoespeso.spbre.service.exception;

public class BusinessLogicException extends BusinessException{

	private static final long serialVersionUID = 7667547711068438200L;

	public BusinessLogicException(String message) {
		super(message);
	}
	
	public BusinessLogicException(String message, Throwable e) {
		super(message, e);
	}
}
