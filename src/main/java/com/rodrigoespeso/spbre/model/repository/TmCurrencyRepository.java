package com.rodrigoespeso.spbre.model.repository;

import java.math.BigDecimal;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.rodrigoespeso.spbre.model.entity.AccountEntity;

@Repository
public interface TmCurrencyRepository extends CrudRepository<AccountEntity, BigDecimal>{
	
	
}
