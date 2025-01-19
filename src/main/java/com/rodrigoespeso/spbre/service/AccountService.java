package com.rodrigoespeso.spbre.service;

import java.math.BigDecimal;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.convert.CurrencyConversion;
import javax.money.convert.MonetaryConversions;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rodrigoespeso.spbre.model.entity.AccountEntity;
import com.rodrigoespeso.spbre.model.entity.TmCurrencyEntity;
import com.rodrigoespeso.spbre.model.repository.AccountRepository;
import com.rodrigoespeso.spbre.model.repository.TmCurrencyRepository;
import com.rodrigoespeso.spbre.service.exception.BusinessException;
import com.rodrigoespeso.spbre.service.exception.BusinessLogicException;
import com.rodrigoespeso.spbre.service.exception.BusinessNotFoundException;
import com.rodrigoespeso.spbre.service.vo.AccountAccessVO;
import com.rodrigoespeso.spbre.service.vo.AccountVO;

@Service
public class AccountService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);
	
	@Autowired
	private AccountRepository repo;

	@Autowired
	private TmCurrencyRepository currencyRepo;
	
	@Autowired
	private AuditProducer auditProducer;

	
	public AccountVO findAccountByName(String name) throws BusinessNotFoundException {
		AccountEntity e = repo.findByName(name)
				.orElseThrow(() -> new BusinessNotFoundException("The account '"+name+"'not exists."));
		return fromEntityToVO(e);
	}
	
	@Transactional(rollbackOn = Exception.class)
	public String create(AccountVO input) throws BusinessLogicException {
		AccountEntity e = fromVOToEntity(input);
		checkTreasuryBalance(e);
		repo.save(e);
		return "Created account with id " + e.getId();
	}
	
	@Transactional(rollbackOn = Exception.class)
	public String access(AccountAccessVO input) throws BusinessException {
		AccountEntity e = repo.findByName(input.getName())
				.orElseThrow(() -> new BusinessNotFoundException("The account '"+input.getName()+"'not exists."));
		TmCurrencyEntity c = currencyRepo.findByCode(input.getCurrency())
				.orElseThrow(() -> new BusinessNotFoundException("Currency Not Found."));
		
		e.setCurrency(c);
		e.setBalance(input.getBalance());
		
		checkTreasuryBalance(e);
		
		repo.save(e);

		return "Changed account '" + e.getName() + "'.";
		
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
	 * @param currencyCode The currency of the money to be transfered
	 * @param amount The amount of money
	 * 
	 * @return Message explaining the process
	 * @throws BusinessException If something is going wrong
	 */
	@Transactional(rollbackOn = Exception.class)
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
			outMsg.append("Transfer ammount: ").append(amountMA.toString()).append("\n");
			outMsg.append("Issuer balance pre-transfer: ").append(issuerMA.toString()).append("\n");
			outMsg.append("Receiver balance pre-transfer: ").append(receiverMA.toString()).append("\n");
		
			LOGGER.info(outMsg.toString());
			
			issuerMA = issuerMA.subtract(amountMA);
			receiverMA = receiverMA.add(amountMA);
			subOutMsg.append("Issuer balance post-transfer: ").append(issuerMA.toString()).append("\n");
			subOutMsg.append("Receiver balance post-transfer: ").append(receiverMA.toString()).append("\n");
			
			// Check treasury property and save the updated entities
			checkAndSave(issuerEntity, receiverEntity, issuerMA, receiverMA);
			
			LOGGER.info(subOutMsg.toString());
			outMsg.append(subOutMsg);
		}else {
			// Define conversions
			LOGGER.info("INTERNATIONAL OPERATION: (it will take a little while...)");
			CurrencyConversion issuerConverter = MonetaryConversions.getExchangeRateProvider()
					.getCurrencyConversion(issuerCurrency);
			CurrencyConversion receiverConverter = MonetaryConversions.getExchangeRateProvider()
					.getCurrencyConversion(receiverCurrency);
			
			LOGGER.info("Issuer balance pre-transfer: {}", issuerMA.toString());
			LOGGER.info("Receiver balance pre-transfer: {}", receiverMA.toString());
			
			outMsg.append("INTERNATIONAL OPERATION:").append("\n");
			outMsg.append("Transfer amount: ").append(amountMA.toString()).append("\n");
			outMsg.append("Issuer balance pre-transfer: ").append(issuerMA.toString()).append("\n");
			outMsg.append("Receiver balance pre-transfer:" ).append(receiverMA.toString()).append("\n");
			
			// Prepare subtraction
			MonetaryAmount toSub = amountMA.with(issuerConverter);
			LOGGER.info("Transfer amount in issuer currency {}", toSub.toString());
			
			// Subtract to the issuer
			issuerMA = issuerMA.subtract(toSub);
			LOGGER.info("Issuer balance post-transfer: {}", issuerMA.toString());

			// Prepare addition
			MonetaryAmount toAdd = amountMA.with(receiverConverter);
			LOGGER.info("Transfer amount in receiver currency {}", toSub.toString());
			
			// Subtract to the issuer
			receiverMA = receiverMA.add(toAdd);
			LOGGER.info("Receiver balance post-transfer: {}", issuerMA.toString());

			// Check treasury property and save the updated entities
			checkAndSave(issuerEntity, receiverEntity, issuerMA, receiverMA);
			
			outMsg.append("Issuer balance post-transfer: ").append(issuerMA.toString()).append("\n");
			outMsg.append("Receiver balance post-transfer: ").append(receiverMA.toString()).append("\n");
		}
		
	    // Crear mensaje de auditoría
	    String auditMessage = String.format("Transfer completed: %s -> %s | Amount: %s %s",
	            issuer, receiver, amount, currencyCode);

	    // Enviar mensaje al tópico Kafka
	    auditProducer.sendMessage("transfer-audit", auditMessage);
		
		return outMsg.toString();
	}

	private void checkTreasuryBalance(AccountEntity a) throws BusinessLogicException {
		if(!a.getTreasury().booleanValue() && BigDecimal.ZERO.compareTo(a.getBalance())>0)
			throw new BusinessLogicException("'"+a.getName()+"': This type of account cannot have negative balance.");
	}

	private void checkAndSave(AccountEntity issuerEntity, AccountEntity receiverEntity, MonetaryAmount issuerMA,
			MonetaryAmount receiverMA) throws BusinessLogicException {
		BigDecimal newIssuerBalance = issuerMA.getNumber().numberValue(BigDecimal.class);
		BigDecimal newReceiverBalance = receiverMA.getNumber().numberValue(BigDecimal.class);
		
		issuerEntity.setBalance(newIssuerBalance);
		receiverEntity.setBalance(newReceiverBalance);
		
		checkTreasuryBalance(issuerEntity);
		checkTreasuryBalance(receiverEntity);
		
		repo.save(issuerEntity);
		repo.save(receiverEntity);
	}

	/*
	 * Private converters:
	 */
	
	private AccountVO fromEntityToVO(AccountEntity e) {
		AccountVO vo = new AccountVO();
		vo.setName(e.getName());
		vo.setCurrency(e.getCurrency().getCode());
		vo.setBalance(e.getBalance());
		vo.setIsTreasury(e.getTreasury());
		return vo;
	}

	private AccountEntity fromVOToEntity(AccountVO vo) throws BusinessLogicException {
		TmCurrencyEntity c = currencyRepo.findByCode(vo.getCurrency())
				.orElseThrow(() -> new BusinessLogicException("Currency Not Found."));
	
		AccountEntity e = new AccountEntity();
		e.setName(vo.getName());
		e.setCurrency(c);
		e.setBalance(vo.getBalance());
		e.setTreasury(vo.getIsTreasury());
		return e;
	}
	
}
