package com.rodrigoespeso.spbre.model.repository;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.rodrigoespeso.spbre.model.entity.TmCurrencyEntity;

@Repository
public interface TmCurrencyRepository extends JpaRepository<TmCurrencyEntity, BigDecimal>{
	
	Optional<TmCurrencyEntity> findByCode(String code);
	
}
