package com.rodrigoespeso.spbre.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.rodrigoespeso.spbre.model.entity.AccountEntity;
import com.rodrigoespeso.spbre.model.repository.AccountRepository;
import com.rodrigoespeso.spbre.model.repository.TmCurrencyRepository;
import com.rodrigoespeso.spbre.service.exception.BusinessLogicException;
import com.rodrigoespeso.spbre.service.exception.BusinessNotFoundException;

@Service
public class AccountService {
	
	@Autowired
	private AccountRepository repo;

	@Autowired
	private TmCurrencyRepository currencyRepo;
	
	public String findAccountByName(@PathVariable String name) {
		return "Found account";
	}
	
	public String create(String provInput) {
		return "New account created";
	}
	
	public String access(String provInput) {
		return "Account modified";
	}
	
	/**
	 * Do a transfer in few steps, if its a national transfer:
	 * 1. Subtract the amount to the issuer balance.
	 * 2. Add the amount to the receiver balance.
	 * 3. Check the treasury property of both of them and if their balances are not negative 
	 * save the accounts with their new balances.
	 *
	 * If its a international transfer, it take a little while because the conversion values are obtained online:
	 * 1. Convert the amount to be subtracted to the issuer currency.
	 * 2. Convert this amount to the receiver currency too.
	 * 3. Subtract to the issuer with the calculated value.
	 * 4. Add to the receiver balance the other calculated value.
	 * 3. Check the treasury property of both of them and if their balances are not negative 
	 * save the accounts with their new balances.
	 * 
	 * @param issuer The account name which makes the operation
	 * @param receiver The receiver account name
	 * @param transferCurrency The currency of the money o be transfered
	 * @param amount The amount of money
	 * 
	 * @return Message explaining the process
	 */
	public String transfer(String issuer, String receiver, String transferCurrency, BigDecimal amount) {
		// Validations
		if(amount.compareTo(BigDecimal.ZERO)<0)
			throw new BusinessLogicException("You cannot transfer negative money");
		
		if(!currencyRepo.findByCode(transferCurrency).isPresent())
			throw new BusinessNotFoundException(
				"This currency is not available to make transfers or do not exists.");
		AccountEntity issuerEntity = repo.findByName(issuer).orElseThrow(
				() -> new BusinessNotFoundException(String.format("The account '%s' do not exists.", issuer)));
		AccountEntity receiverEntity = repo.findByName(receiver).orElseThrow(
				() -> new BusinessNotFoundException(String.format("The account '%s' do not exists.", receiver)));
			
		return "Ok";
	}
	
}
