package com.rodrigoespeso.spbre.service.paystrategy;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class PayStrategyFactory {
	enum PayType {
		CASH, CARD;
	}
	
	final static Map<PayType, Supplier<OldWayPayStrategy>> map = new HashMap<>();

	static {
		map.put(PayType.CASH, CashPaytrategy::new);
		map.put(PayType.CARD, CreditCardPayStrategy::new);
	}

	public OldWayPayStrategy getInstancia(PayType tipo) {

		Supplier<OldWayPayStrategy> sup = map.get(tipo);
		if (sup != null) {
			return sup.get();
		}		
	throw new IllegalArgumentException("Tipo no disponible");
	}
}
