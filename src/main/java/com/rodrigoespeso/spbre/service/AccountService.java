package com.rodrigoespeso.spbre.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class AccountService {
	
	public String findAccountByName(@PathVariable String name) {
		return "Found account";
	}
	
	public String create(String provInput) {
		return "New account created";
	}
	
	public String access(String provInput) {
		return "Account modified";
	}
	
	public String transfer(String provInput) {
		return "Transfer done";
	}
	
}
