package com.rodrigoespeso.spbre.service.paystrategy;

public class CashPaytrategy implements OldWayPayStrategy{
    @Override
    public double pay(double amount) {
        double serviceCharge = 5.00;
        return amount + serviceCharge;
    }
}
