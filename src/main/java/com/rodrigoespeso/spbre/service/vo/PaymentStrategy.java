package com.rodrigoespeso.spbre.service.vo;

public interface PaymentStrategy {

	final double SERVICECHARGE = 5.00;
	final double CREDITCARDFEE = 10.00;
	
	double pay(double amount);

	static PaymentStrategy payInCash() {
		return amount -> SERVICECHARGE;
	}

	static PaymentStrategy payByCredit() {
		return amount -> SERVICECHARGE + CREDITCARDFEE;
	}
}
