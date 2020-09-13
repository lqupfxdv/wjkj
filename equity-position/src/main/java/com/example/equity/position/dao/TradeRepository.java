package com.example.equity.position.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.equity.position.po.Trade;

public interface TradeRepository extends JpaRepository<Trade, Integer>, TradeRepositoryExtends {

}
