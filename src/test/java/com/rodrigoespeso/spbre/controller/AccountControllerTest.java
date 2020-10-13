package com.rodrigoespeso.spbre.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.rodrigoespeso.spbre.service.AccountService;
import com.rodrigoespeso.spbre.service.exception.BusinessLogicException;
import com.rodrigoespeso.spbre.service.exception.BusinessNotFoundException;
import com.rodrigoespeso.spbre.service.vo.AccountVO;

@RunWith(SpringRunner.class)
@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mvc;
 
    @MockBean
    private AccountService service;
    
    @Test
    public void find_whenNotFound_thenNotFoundCode() throws Exception {
    	// When
    	when(service.findAccountByName(any())).thenThrow(BusinessNotFoundException.class);
    	// Then
    	 mvc.perform(get("/account/find/A"))
    		      .andExpect(status().isNotFound());
    }
    
    @Test
    public void find_whenServiceReturnsResults_thenShowThemAndOkCode() throws Exception {
    	// When
    	when(service.findAccountByName(any())).thenReturn(new AccountVO());
    	// Then
    	 mvc.perform(get("/account/find/A"))
    		    .andExpect(status().isOk());
    }
    
    @Test
    @Ignore
    public void create_whenInvalidDataIsGiven_thenNotFoundCode() throws Exception {
    	String json = "{\n" + 
    			"    \"name\": \"F\",\n" + 
    			"    \"currency\": \"EUR\",\n" + 
    			"    \"balance\": 560,\n" + 
    			"    \"treasury\": false\n" + 
    			"}";
    	// When
    	when(service.create(any())).thenThrow(BusinessLogicException.class);
    	// Then
    	 mvc.perform(post("/account/create/").content(json))
    		      .andExpect(status().isBadRequest());
    }
    
    @Test
    @Ignore
    public void create_whenValidDataIsGiven_thenCreateNewAccountAndOkCode() throws Exception {
    	String json = "{\n" + 
    			"    \"name\": \"F\",\n" + 
    			"    \"currency\": \"EUR\",\n" + 
    			"    \"balance\": 560,\n" + 
    			"    \"treasury\": false\n" + 
    			"}";
    	AccountVO vo = new AccountVO();
    	vo.setName("F");
    	vo.setCurrency("EUR");
    	vo.setBalance(new BigDecimal(560));
    	vo.setTreasury(false);
    	
    	// When
    	when(service.create(any())).thenReturn("Ok");
    	// Then
    	 mvc.perform(post("/account/create/").content(json))
    		      .andExpect(status().isOk());
    }
    
    @Test
    public void access_whenInvalidDataIsGiven_thenNotFoundCode() {
    	
    }
    
    @Test
    public void access_whenValidDataIsGiven_thenUpdateAccountAndOkCode() {
    	
    }
    
    @Test
    @Ignore
    public void transfer_whenBadTransfer_thenBadRequest() {
    	
    }
    
    @Test
    @Ignore
    public void access_whenGoodTransfer_thenOk() {
    	
    }
	
}
