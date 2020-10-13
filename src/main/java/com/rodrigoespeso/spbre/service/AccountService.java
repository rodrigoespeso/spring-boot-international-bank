package com.rodrigoespeso.spbre.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.rodrigoespeso.spbre.model.repository.AccountRepository;
import com.rodrigoespeso.spbre.model.repository.TmCurrencyRepository;

@Service
public class AccountService {
	
	@Autowired
	private AccountRepository repo;

	@Autowired
	private TmCurrencyRepository currencyRrepo;
	
	public String findAccountByName(@PathVariable String name) {
		return "Found account";
	}
	
	public String create(String provInput) {
		return "New account created";
	}
	
	public String access(String provInput) {
		return "Account modified";
	}
	
	public String transfer(String issuer, String receiver, String transferCurrency, String amount) {
		return repo.findById(BigDecimal.ONE).get().getName();
	}
	
}
