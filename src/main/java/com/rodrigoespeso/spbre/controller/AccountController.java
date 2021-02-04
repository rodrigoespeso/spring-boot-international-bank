package com.rodrigoespeso.spbre.controller;

import java.math.BigDecimal;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.rodrigoespeso.spbre.service.AccountService;
import com.rodrigoespeso.spbre.service.exception.BusinessException;
import com.rodrigoespeso.spbre.service.exception.BusinessLogicException;
import com.rodrigoespeso.spbre.service.exception.BusinessNotFoundException;
import com.rodrigoespeso.spbre.service.vo.AccountAccessVO;
import com.rodrigoespeso.spbre.service.vo.AccountVO;

@Validated
@RestController
@RequestMapping("/account")
public class AccountController {

	@Autowired
	private AccountService service;
	
	@GetMapping("/find/{name}")
	public ResponseEntity<AccountVO> findAccount(@PathVariable @NotEmpty String name) {
		try {
			return ResponseEntity.ok(service.findAccountByName(name));
		} catch (BusinessException e) {
			throw handledException(e);
		}
	}
	
	@PostMapping(path = "create/")
	public ResponseEntity<String> create(@RequestBody @Valid AccountVO input) {
		try {
			return ResponseEntity.ok(service.create(input));
		} catch (BusinessException e) {
			throw handledException(e);
		}
	}
	
	@PutMapping("access/")
	public ResponseEntity<String> access(@RequestBody @Valid AccountAccessVO input) {
		try {
			return ResponseEntity.ok(service.access(input));
		} catch (BusinessException e) {
			throw handledException(e);
		}
	}
	
	@PostMapping("transfer/")
	public ResponseEntity<String> transfer(@RequestParam @NotEmpty String issuer, @RequestParam @NotEmpty String receiver,
			@RequestParam @NotEmpty @Size(min = 3, max = 3) String currencyCode,
			@Positive @NotNull @RequestParam BigDecimal amount) {
		try {
			return ResponseEntity.ok(service.transfer(issuer, receiver, currencyCode, amount));
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
