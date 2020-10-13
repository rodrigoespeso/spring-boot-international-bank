package com.rodrigoespeso.spbre.service.vo;

import java.math.BigDecimal;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AccountVO {
	
	@NotEmpty
	private String name;
	
	@NotNull
	@Size (min=3, max=3)
	private String currency;
	
	@NotNull
	private BigDecimal balance;
	
	@NotNull
	private Boolean treasury;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public Boolean getTreasury() {
		return treasury;
	}

	public void setTreasury(Boolean treasury) {
		this.treasury = treasury;
	}
	
	
}
