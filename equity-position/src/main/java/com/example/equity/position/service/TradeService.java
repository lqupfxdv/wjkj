package com.example.equity.position.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.example.equity.position.dao.TradeRepository;
import com.example.equity.position.dto.TradeOperation;
import com.example.equity.position.dto.TradeType;
import com.example.equity.position.po.Trade;

@Service
public class TradeService {
	
	@Autowired
	private TradeRepository tradeRepository;
	
	public Trade insertTrade(Trade trade) {
		if(checkSellOver(trade)) {
			throw new SellOverException();
		}
		return tradeRepository.insertTrade(trade);
	}
	
	public Trade updateTrade(Trade trade) {
		if(checkSellOver(trade)) {
			throw new SellOverException();
		}
		return tradeRepository.updateTrade(trade);
	}
	
	public Trade cancelTrade(Trade trade) {
		return tradeRepository.cancelTrade(trade);
	}
	
	public Map<String, Integer> getPosition(){
		List<Trade> trades = tradeRepository.findAll();
		Map<String, Integer> tradeMap = new HashMap<String, Integer>();
		for(Trade trade : trades) {
			if(Objects.equals(trade.getOperation(), TradeOperation.CANCEL.toString())) {
				continue;
			}
			Integer stockQuantity = tradeMap.get(trade.getSecurityCode());
			if(stockQuantity == null) {
				tradeMap.put(trade.getSecurityCode(), trade.getQuantity());
				continue;
			}
			if(Objects.equals(trade.getTradeType(), TradeType.BUY.toString())) {
				tradeMap.put(trade.getSecurityCode(), stockQuantity + trade.getQuantity());
			} else {
				tradeMap.put(trade.getSecurityCode(), stockQuantity - trade.getQuantity());
			}
		}
		return tradeMap;
	}
	
	private boolean checkSellOver(Trade trade) {
		Trade probe = new Trade();
		probe.setSecurityCode(trade.getSecurityCode());
		List<Trade> items = tradeRepository.findAll(Example.of(probe));
		Integer sumQuantity = 0;
		if (trade.getTradeID() == null || trade.getTradeID() <= 0)  { // 插入
			for(Trade it:items) {
				if(Objects.equals(it.getTradeType(), TradeType.BUY.toString())) {
					sumQuantity+=it.getQuantity();
				} else {
					sumQuantity-=it.getQuantity();
				}
			}
		} else { // 更新
			for(Trade it:items) {
				if(Objects.equals(it.getTradeID(), trade.getTradeID())) {
					continue;
				}
				if(Objects.equals(it.getTradeType(), TradeType.BUY.toString())) {
					sumQuantity+=it.getQuantity();
				} else {
					sumQuantity-=it.getQuantity();
				}
			}
		}
		if(Objects.equals(trade.getTradeType(), TradeType.BUY.toString())) {
			sumQuantity+=trade.getQuantity();
		} else {
			sumQuantity-=trade.getQuantity();
		}
		return sumQuantity < 0;
	}
}
