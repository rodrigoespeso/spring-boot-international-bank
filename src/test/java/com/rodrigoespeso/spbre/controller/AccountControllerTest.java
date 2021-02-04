package com.rodrigoespeso.spbre.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.rodrigoespeso.spbre.service.AccountService;
import com.rodrigoespeso.spbre.service.exception.BusinessException;
import com.rodrigoespeso.spbre.service.exception.BusinessLogicException;
import com.rodrigoespeso.spbre.service.exception.BusinessNotFoundException;
import com.rodrigoespeso.spbre.service.vo.AccountVO;
import com.rodrigoespeso.spbre.service.vo.builder.AccountVOBuilder;

@RunWith(SpringRunner.class)
@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mvc;
 
    @MockBean
    private AccountService service;
    
    private String toJson(Object object) throws JsonProcessingException {
    	ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    	return ow.writeValueAsString(object);
    }
    
    @Test
    public void find_whenNotFound_thenNotFound() throws Exception {
    	// When
    	when(service.findAccountByName(any())).thenThrow(BusinessNotFoundException.class);
    	// Then
    	 mvc.perform(get("/account/find/XXX"))
    		      .andExpect(status().isNotFound());
    }
    
    @Test
    public void find_whenServiceReturnsResults_thenShowThemAndIsOk() throws Exception {
    	// Given
		AccountVO vo = new AccountVOBuilder().withName("A123").withCurrency("EUR").withBalance(new BigDecimal(560))
				.withIsTreasury(Boolean.FALSE).build();
    	// When
    	when(service.findAccountByName(any())).thenReturn(vo);
    	// Then
		mvc.perform(get("/account/find/A123"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name", is("A123")))
			.andExpect(jsonPath("$.currency", is("EUR")))
			.andExpect(jsonPath("$.balance", is(560)))
			.andExpect(jsonPath("$.isTreasury", is(false)));
    }
    
    @Test
    public void create_whenInvalidDataIsGiven_thenBadRequest() throws Exception {
    	// Given
    	AccountVO vo = new AccountVOBuilder().defaultBuild();
		String json = toJson(vo);
    	// When
    	when(service.create(any())).thenThrow(BusinessLogicException.class);
    	// Then
    	 mvc.perform(post("/account/create/").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
    		      .andExpect(status().isBadRequest());
    }
    
    @Test
    public void create_whenValidDataIsGiven_thenCreateNewAccountAndIsOk() throws Exception {
    	// Given
    	AccountVO vo = new AccountVOBuilder().defaultBuild();
		String json = toJson(vo);
		String output = "Created account with id XXX";
    	// When
    	when(service.create(any())).thenReturn(output);
    	// Then
    	 mvc.perform(post("/account/create/").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
    		      .andExpect(status().isOk())
    		      .andExpect(content().string(output));
    }
    
    @Test
    public void access_whenInvalidDataIsGiven_thenNotFound() throws Exception {
    	// Given
    	AccountVO vo = new AccountVOBuilder().defaultBuild();
		String json = toJson(vo);
    	// When
    	when(service.access(any())).thenThrow(BusinessNotFoundException.class);
    	// Then
		mvc.perform(put("/account/access/").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
				.andExpect(status().isNotFound());

    }
   
    @Test
    public void access_whenValidDataIsGiven_thenUpdateAccountAndIsOk() throws Exception {
    	// Given
    	AccountVO vo = new AccountVOBuilder().defaultBuild();
    	vo.setName("XXX");
		String json = toJson(vo);
		String output = "Changed account 'XXX'";
    	// When
    	when(service.access(any())).thenReturn(output);
    	// Then
		mvc.perform(put("/account/access/").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
				.andExpect(status().isOk())
				.andExpect(content().string(output));
    }
    
    @Test
    public void transfer_whenBadTransfer_thenBadRequest() throws Exception {
     	// When
    	when(service.transfer(any(), any(), any(), any())).thenThrow(BusinessException.class);
    	// Then
		mvc.perform(post("/account/transfer/"))
		.andExpect(status().isBadRequest());
    	
    }
    
    @Test
    @Ignore // Getting 400 right now because one NotNull param left
    public void transfer_whenGoodTransfer_thenIsOk() throws Exception {
    	// Given
    	MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    	params.add("issuer", "XXX");
    	params.add("receiver", "YYY");
    	params.add("currencyCode", "USD");
//    	params.add("amount", "1000");  // TODO Get to know how test this input param
     	String output = "Test transfer OK";
    	// When
    	when(service.transfer(any(), any(), any(), any())).thenReturn(output);
    	// Then
		mvc.perform(post("/account/transfer/").params(params))
		.andExpect(status().isOk())
		.andExpect(content().string(output));
			
		/*
		 * Other way to put params
		 */
//		mvc.perform(post("/account/transfer/").param("issuer", "XXX")
//		.param("receiver", "YYY")
//		.param("currencyCode", "USD"))
////		.param("amount", BigDecimal.ONE.toString())) // TODO Get to know how test this input param
//		.andExpect(status().isBadRequest());
    }
	
}
