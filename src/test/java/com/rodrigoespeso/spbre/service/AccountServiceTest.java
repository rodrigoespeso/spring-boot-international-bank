package com.rodrigoespeso.spbre.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rodrigoespeso.spbre.model.entity.AccountEntity;
import com.rodrigoespeso.spbre.model.entity.TmCurrencyEntity;
import com.rodrigoespeso.spbre.model.repository.AccountRepository;
import com.rodrigoespeso.spbre.model.repository.TmCurrencyRepository;
import com.rodrigoespeso.spbre.service.exception.BusinessException;

@RunWith(SpringJUnit4ClassRunner.class)
public class AccountServiceTest {
	
    @TestConfiguration
    static class AccountServiceTestConfiguration {
 
        @Bean
        public AccountService service() {
            return new AccountService();
        }
    }
	
	@MockBean
	private AccountRepository repo;

	@MockBean
	private TmCurrencyRepository currencyRepo;
	
	@Autowired
	private AccountService service;
	
	@Test (expected = BusinessException.class)
	public void transfer_whenNotExistingCurrency_thenThrowBusinessException() throws BusinessException {
		// When
		when(currencyRepo.findByCode(any())).thenReturn(Optional.empty());
		
		// Then
		service.transfer("A", "B", "EUR", BigDecimal.ONE);
	}
	
	@Test (expected = BusinessException.class)
	public void transfer_whenNotExistingAccount_thenThrowBusinessException() throws BusinessException {
		// Given
		TmCurrencyEntity c = new TmCurrencyEntity();
		c.setCode("EUR");
		c.setId(BigDecimal.ONE);
		c.setName("Euro");
		
		// When
		when(currencyRepo.findByCode(any())).thenReturn(Optional.of(c));
		when(repo.findByName(any())).thenReturn(Optional.empty());
		
		// Then
		service.transfer("A", "B", "EUR", BigDecimal.ONE);
	}
	
	@Test
	public void transfer_whenCorrectDataForANationalTransferIsGiven_thenReturnMessage() throws BusinessException {
		// Given
		TmCurrencyEntity c = new TmCurrencyEntity();
		c.setCode("EUR");
		c.setId(BigDecimal.ONE);
		c.setName("Euro");
		AccountEntity issuer = new AccountEntity();
		issuer.setName("A");
		issuer.setBalance(new BigDecimal(100));
		issuer.setCurrency(c);;
		issuer.setId(BigDecimal.ONE);
		issuer.setTreasury(false);
		AccountEntity receiver = new AccountEntity();
		receiver.setName("B");
		receiver.setBalance(new BigDecimal(900));
		receiver.setCurrency(c);;
		receiver.setId(new BigDecimal(2));
		receiver.setTreasury(false);
		
		
		// When
		when(currencyRepo.findByCode(any())).thenReturn(Optional.of(c));
		when(repo.findByName("A")).thenReturn(Optional.of(issuer));
		when(repo.findByName("B")).thenReturn(Optional.of(receiver));
		
		// Then
		assertTrue(service.transfer("A", "B", "EUR", BigDecimal.ONE).startsWith("NATIONAL OPERATION:"));
	}
	
}
