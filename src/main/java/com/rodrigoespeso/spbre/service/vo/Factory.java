package com.rodrigoespeso.spbre.service.vo;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class Factory {
	enum PayType {
		CASH, CARD;
	}
	
	final static Map<PayType, Supplier<OldWayPaymentStrategy>> map = new HashMap<>();

	static {
		map.put(PayType.CASH, CashPaymentStrategy::new);
		map.put(PayType.CARD, CreditCardStrategy::new);
	}

	public OldWayPaymentStrategy getInstancia(PayType tipo) {

		Supplier<OldWayPaymentStrategy> sup = map.get(tipo);
		if (sup != null) {
			return sup.get();
		}		
	throw new IllegalArgumentException("Tipo no disponible");
	}
}
