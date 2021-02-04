package com.rodrigoespeso.spbre.service.paystrategy;

public interface PayStrategy {

	final double SERVICECHARGE = 5.00;
	final double CREDITCARDFEE = 10.00;
	
	double pay(double amount);

	static PayStrategy payInCash() {
		return amount -> SERVICECHARGE;
	}

	static PayStrategy payByCredit() {
		return amount -> SERVICECHARGE + CREDITCARDFEE;
	}
}
