package com.example.equity.position.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.equity.position.dao.TradeRepository;
import com.example.equity.position.dto.Position;
import com.example.equity.position.dto.ResultDto;
import com.example.equity.position.dto.TradeOperation;
import com.example.equity.position.dto.TradeType;
import com.example.equity.position.po.Trade;
import com.example.equity.position.service.ResultStatusEnum;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class EquityPositionControllerTest {
	
	@Autowired
	private EquityPositionController controller;
	
	@Autowired
	private TradeRepository tradeRepository;
	
	@BeforeEach
	void initData() {
		tradeRepository.deleteAll();
		Trade trade = new Trade();
		trade.setTradeType(TradeType.BUY.toString());
		trade.setQuantity(100);
		trade.setSecurityCode("REL");
		controller.insertTrade(trade);
		
		Trade trade1 = new Trade();
		trade1.setTradeType(TradeType.BUY.toString());
		trade1.setQuantity(10);
		trade1.setSecurityCode("ITC");
		controller.insertTrade(trade1);
		
		Trade trade2 = new Trade();
		trade2.setTradeType(TradeType.BUY.toString());
		trade2.setQuantity(50);
		trade2.setSecurityCode("INF");
		controller.insertTrade(trade2);
	}

	@Test
	void testInsertTrade() {
		final Trade trade = new Trade();
		trade.setTradeType(TradeType.SELL.toString());
		trade.setQuantity(1000);
		trade.setSecurityCode("REL");
		ResultDto<?> result = controller.insertTrade(trade);
		assertEquals(ResultStatusEnum.SELL_OVER_ERROR.getMsg(), result.getMsg());
		
		trade.setQuantity(50);
		result = controller.insertTrade(trade);
		assertEquals(50, ((Trade)result.getDatas()).getQuantity());
	}

	@Test
	void testUpdateTrade() {
		final Trade trade = new Trade();
		trade.setTradeType(TradeType.SELL.toString());
		trade.setQuantity(1000);
		trade.setSecurityCode("REL");
		trade.setTradeID(1);
		ResultDto<?> result = controller.updateTrade(trade);
		assertEquals(ResultStatusEnum.SELL_OVER_ERROR.getMsg(), result.getMsg());
		
		trade.setTradeType(TradeType.BUY.toString());
		trade.setQuantity(500);
		result = controller.updateTrade(trade);
		assertEquals(500, ((Trade)result.getDatas()).getQuantity());
		assertEquals(2, ((Trade)result.getDatas()).getVersion());
	}

	@Test
	void testCancelTrade() {
		final Trade trade = new Trade();
		trade.setTradeType(TradeType.SELL.toString());
		trade.setQuantity(1000);
		trade.setSecurityCode("REL");
		trade.setTradeID(1);
		ResultDto<?> result = controller.cancelTrade(trade);
		assertEquals(TradeOperation.CANCEL.toString(), ((Trade)result.getDatas()).getOperation());
		
		trade.setTradeType(TradeType.BUY.toString());
		trade.setQuantity(500);
		result = controller.updateTrade(trade);
		assertEquals(ResultStatusEnum.CANCEL_CANOT_MODIFY_ERROR.getMsg(), result.getMsg());
	}

	@Test
	void testPositionTrade() {
		ResultDto<?> result = controller.positionTrade();
		Map<String, Integer> position = ((Position)result.getDatas()).getPosition();
		assertEquals(100, position.get("REL"));
		assertEquals(10, position.get("ITC"));
		assertEquals(50, position.get("INF"));
	}

}
