package com.example.equity.position.dao;

import javax.transaction.Transactional;

import com.example.equity.position.po.Trade;

public interface TradeRepositoryExtends {
	@Transactional
	Trade insertTrade(Trade trade);
	@Transactional
	Trade updateTrade(Trade trade);
	@Transactional
	Trade cancelTrade(Trade trade);
}
