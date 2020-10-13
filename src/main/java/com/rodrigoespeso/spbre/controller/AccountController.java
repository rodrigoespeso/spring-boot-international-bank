package com.rodrigoespeso.spbre.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rodrigoespeso.spbre.service.AccountService;
import com.rodrigoespeso.spbre.service.exception.BusinessException;

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
	public String transfer(String issuer, String receiver, String currencyCode, BigDecimal amount) throws BusinessException {
		return service.transfer(issuer, receiver, currencyCode, amount);
	}
	
}
