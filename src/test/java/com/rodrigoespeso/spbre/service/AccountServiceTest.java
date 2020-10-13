package com.rodrigoespeso.spbre.service;

import static org.junit.Assert.assertEquals;
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
import com.rodrigoespeso.spbre.model.repository.AccountRepository;
import com.rodrigoespeso.spbre.model.repository.TmCurrencyRepository;

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
	private TmCurrencyRepository currencyRrepo;
	
	@Autowired
	private AccountService service;
	
	@Test
	public void transfer_test() {
		// Given
		AccountEntity a = new AccountEntity();
		a.setName("Z");
		
		// When
		when(repo.findById(any())).thenReturn(Optional.of(a));
		
		// Then
		assertEquals("Z", service.transfer("A", "B", "EUR", BigDecimal.ONE));
	}
	
}
