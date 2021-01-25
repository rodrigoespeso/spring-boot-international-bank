package com.rodrigoespeso.spbre.service.vo;

public class CreditCardStrategy implements OldWayPaymentStrategy{
    @Override
    public double pay(double amount) {
		double serviceCharge = 5.00;
		double creditCardFee = 10.00;
		return amount + serviceCharge + creditCardFee;
	} 	
}
