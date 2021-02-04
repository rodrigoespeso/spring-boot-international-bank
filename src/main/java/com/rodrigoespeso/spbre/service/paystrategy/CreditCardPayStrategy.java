package com.rodrigoespeso.spbre.service.paystrategy;

public class CreditCardPayStrategy implements OldWayPayStrategy{
    @Override
    public double pay(double amount) {
		double serviceCharge = 5.00;
		double creditCardFee = 10.00;
		return amount + serviceCharge + creditCardFee;
	} 	
}
