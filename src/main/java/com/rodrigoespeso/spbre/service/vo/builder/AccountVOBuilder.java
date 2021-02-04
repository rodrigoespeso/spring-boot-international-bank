package com.rodrigoespeso.spbre.service.vo.builder;

import java.math.BigDecimal;
import java.util.UUID;

import com.rodrigoespeso.spbre.service.vo.AccountVO;

/**
 * Builder class for {@link AccountVO}
 * 
 * @author Rodrigo Espeso
 *
 */
public class AccountVOBuilder {
	
	private String name;
	private String currency;
	private BigDecimal balance;
	private Boolean isTreasury;
	
	public AccountVOBuilder withName(String name) {
		this.name = name;
		return this;
	}
	
	public AccountVOBuilder withCurrency(String currency) {
		this.currency = currency;
		return this;
	}
	
	public AccountVOBuilder withBalance(BigDecimal balance) {
		this.balance = balance;
		return this;
	}
	
	public AccountVOBuilder withIsTreasury(Boolean isTreasury) {
		this.isTreasury = isTreasury;
		return this;
	}
	
	public AccountVO build() {
		AccountVO vo = new AccountVO();
		vo.setName(name);
		vo.setCurrency(currency);
		vo.setBalance(balance);
		vo.setIsTreasury(isTreasury);
		return vo;
	}
	
	public AccountVO defaultBuild() {
		AccountVO vo = new AccountVO();
		vo.setName(UUID.randomUUID().toString());
		vo.setCurrency("EUR");
		vo.setBalance(new BigDecimal(0));
		vo.setIsTreasury(false);
		return vo;
	}
}
