package com.example.equity.position.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.BeanUtils;

import com.example.equity.position.dto.TradeOperation;
import com.example.equity.position.dto.TradeType;
import com.example.equity.position.po.Trade;
import com.example.equity.position.po.Transaction;

/**
 * Trade 的 Insert/Update/Cancel，
 * Insert要求 Version 从1开始
 * Update 要求Version 加1，不允许修改Cacel的Trade
 * Cacel 要求Version 加1
 * @author lqupf
 *
 */
public class TradeRepositoryImpl implements TradeRepositoryExtends {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Trade insertTrade(Trade trade) {
		trade.setTradeID(null);
		trade.setVersion(1);
		trade.setOperation(TradeOperation.INSERT.toString());
		entityManager.persist(trade);
		Transaction transaction = new Transaction();
		BeanUtils.copyProperties(trade, transaction);
		entityManager.persist(transaction);
		return trade;
	}

	@Override
	public Trade updateTrade(Trade trade) {
		Trade currentTrade = entityManager.find(trade.getClass(), trade.getTradeID());
		if(TradeOperation.CANCEL.toString().equals(currentTrade.getOperation())) {
			throw new CancelCanotModifyException();
		}
		trade.setOperation(TradeOperation.UPDATE.toString());
		trade.setVersion(currentTrade.getVersion() + 1);
		entityManager.merge(trade);
		Transaction transaction = new Transaction();
		BeanUtils.copyProperties(trade, transaction);
		entityManager.persist(transaction);
		return trade;
	}

	@Override
	public Trade cancelTrade(Trade trade) {
		Trade currentTrade = entityManager.find(trade.getClass(), trade.getTradeID());
		currentTrade.setOperation(TradeOperation.CANCEL.toString());
		currentTrade.setVersion(currentTrade.getVersion() + 1);
		entityManager.merge(currentTrade);
		Transaction transaction = new Transaction();
		BeanUtils.copyProperties(currentTrade, transaction);
		entityManager.persist(transaction);
		return currentTrade;
	}
}
