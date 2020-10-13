package com.rodrigoespeso.spbre.service;

import java.math.BigDecimal;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.rodrigoespeso.spbre.model.entity.AccountEntity;
import com.rodrigoespeso.spbre.model.repository.AccountRepository;
import com.rodrigoespeso.spbre.model.repository.TmCurrencyRepository;
import com.rodrigoespeso.spbre.service.exception.BusinessException;
import com.rodrigoespeso.spbre.service.exception.BusinessLogicException;
import com.rodrigoespeso.spbre.service.exception.BusinessNotFoundException;

@Service
public class AccountService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);
	
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
	 * @param currencyCode The currency of the money o be transfered
	 * @param amount The amount of money
	 * 
	 * @return Message explaining the process
	 * @throws BusinessException If something is going wrong
	 */
	@Transactional
	public String transfer(String issuer, String receiver, String currencyCode, BigDecimal amount) throws BusinessException {
		// Validations
		if(amount.compareTo(BigDecimal.ZERO)<0)
			throw new BusinessLogicException("You cannot transfer negative money");
		
		if(!currencyRepo.findByCode(currencyCode).isPresent())
			throw new BusinessNotFoundException(
				"This currency is not available to make transfers or do not exists.");
		AccountEntity issuerEntity = repo.findByName(issuer).orElseThrow(
				() -> new BusinessNotFoundException(String.format("The account '%s' do not exists.", issuer)));
		AccountEntity receiverEntity = repo.findByName(receiver).orElseThrow(
				() -> new BusinessNotFoundException(String.format("The account '%s' do not exists.", receiver)));

		// Define useful variables
		StringBuilder outMsg = new StringBuilder(); // used to return as a message and log the process to
		StringBuilder subOutMsg = new StringBuilder();
		
		String issuerCurrency = issuerEntity.getCurrency().getCode();
		String receiverCurrency = receiverEntity.getCurrency().getCode();
		
		boolean nationalOperation = issuerCurrency.equals(receiverCurrency)
				&& issuerCurrency.equals(currencyCode);
		
		MonetaryAmount amountMA = Monetary.getDefaultAmountFactory().setCurrency(currencyCode)
				.setNumber(amount).create();
		MonetaryAmount issuerMA = Monetary.getDefaultAmountFactory().setCurrency(issuerCurrency)
				.setNumber(issuerEntity.getBalance()).create();
		MonetaryAmount receiverMA = Monetary.getDefaultAmountFactory().setCurrency(receiverCurrency)
				.setNumber(receiverEntity.getBalance()).create();
		
		if(nationalOperation) {
			outMsg.append("NATIONAL OPERATION:").append("\n");
			outMsg.append("Tranfer ammount: ").append(amountMA.toString()).append("\n");
			outMsg.append("Issuer balance pre-transfer: ").append(issuerMA.toString()).append("\n");
			outMsg.append("Receiver balance pre-transfer: ").append(receiverMA.toString()).append("\n");
		
			LOGGER.info(outMsg.toString());
			
			issuerMA = issuerMA.subtract(amountMA);
			receiverMA = receiverMA.add(amountMA);
			subOutMsg.append("Issuer balance post-transfer: ").append(issuerMA.toString()).append("\n");
			subOutMsg.append("Receiver balance post-transfer: ").append(receiverMA.toString()).append("\n");
			
//			check treasury property and save the updated entities
			
			LOGGER.info(subOutMsg.toString());
			outMsg.append(subOutMsg);
		}
		
		return outMsg.toString();
	}
	
}
