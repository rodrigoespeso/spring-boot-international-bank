package com.rodrigoespeso.spbre.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountController {

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
	public String transfer(String provInput) {
		return "Transfer done";
	}
	
}
