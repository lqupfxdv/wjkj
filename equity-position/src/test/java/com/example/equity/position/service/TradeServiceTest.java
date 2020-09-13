package com.example.equity.position.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.equity.position.dao.TradeRepository;
import com.example.equity.position.dto.TradeType;
import com.example.equity.position.po.Trade;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class TradeServiceTest {
	
	@Autowired
	private TradeService tradeService;
	
	@Autowired
	private TradeRepository tradeRepository;
	
	@BeforeEach
	void initData() {
		tradeRepository.deleteAll();
		Trade trade = new Trade();
		trade.setTradeType(TradeType.BUY.toString());
		trade.setQuantity(100);
		trade.setSecurityCode("REL");
		tradeService.insertTrade(trade);
		
		Trade trade1 = new Trade();
		trade1.setTradeType(TradeType.BUY.toString());
		trade1.setQuantity(10);
		trade1.setSecurityCode("ITC");
		tradeService.insertTrade(trade1);
		
		Trade trade2 = new Trade();
		trade2.setTradeType(TradeType.BUY.toString());
		trade2.setQuantity(50);
		trade2.setSecurityCode("INF");
		tradeService.insertTrade(trade2);
	}

	@Test
	void testInsertTrade() {
		final Trade trade = new Trade();
		trade.setTradeType(TradeType.SELL.toString());
		trade.setQuantity(1000);
		trade.setSecurityCode("REL");
		assertThrows(SellOverException.class, () -> { tradeService.insertTrade(trade); });
	}

	@Test
	void testUpdateTrade() {
		final Trade trade = new Trade();
		trade.setTradeType(TradeType.SELL.toString());
		trade.setQuantity(1000);
		trade.setSecurityCode("REL");
		trade.setTradeID(1);
		assertThrows(SellOverException.class, () -> { tradeService.updateTrade(trade); });
	}

	@Test
	void testGetPosition() {
		Map<String, Integer> position = tradeService.getPosition();
		assertEquals(100, position.get("REL"));
		assertEquals(10, position.get("ITC"));
		assertEquals(50, position.get("INF"));
	}
}
