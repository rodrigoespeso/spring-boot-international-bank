package com.rodrigoespeso.spbre.controller;

import java.math.BigDecimal;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.rodrigoespeso.spbre.service.AccountService;
import com.rodrigoespeso.spbre.service.exception.BusinessException;
import com.rodrigoespeso.spbre.service.exception.BusinessLogicException;
import com.rodrigoespeso.spbre.service.exception.BusinessNotFoundException;

@Validated
@RestController
@RequestMapping("/account")
public class AccountController {

	@Autowired
	private AccountService service;
	
	@GetMapping("/find/{name}")
	public String findAccount(@PathVariable String name) {
		return "Found account";
	}
	
	@PostMapping("create/")
	public String create(String provInput) {
		return "New account created";
	}
	
	@PostMapping("access/")
	public String access(String provInput) {
		return "Account modified";
	}
	
	@PostMapping("transfer/")
	public String transfer(@RequestParam @NotEmpty String issuer, @RequestParam @NotEmpty String receiver,
			@RequestParam @NotEmpty @Size(min = 3, max = 3) String currencyCode,
			@Positive @NotNull @RequestParam BigDecimal amount) {
		try {
			return service.transfer(issuer, receiver, currencyCode, amount);
		} catch (BusinessException e) {
			throw handledException(e);
		}
	}
	
	/*
	 * Little handler
	 */
	private ResponseStatusException handledException(BusinessException e) {
		if(e instanceof BusinessNotFoundException)
			return new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		if(e instanceof BusinessLogicException)
			return new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
}
