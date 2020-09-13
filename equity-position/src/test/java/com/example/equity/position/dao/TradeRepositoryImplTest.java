package com.example.equity.position.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.equity.position.dto.TradeOperation;
import com.example.equity.position.dto.TradeType;
import com.example.equity.position.po.Trade;
import com.example.equity.position.po.Transaction;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class TradeRepositoryImplTest {
	
	@Autowired
	private TradeRepository tradeRepository;
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	@BeforeEach
	void beforeInsert() {
//		tradeRepository.deleteAll();
		Trade trade = new Trade();
		trade.setTradeType(TradeType.BUY.toString());
		trade.setQuantity(100);
		trade.setSecurityCode("REL");
		trade = tradeRepository.insertTrade(trade);
	}

	@Test
	void testInsertTrade() {
		Transaction transaction = new Transaction();
		transaction.setTradeID(1);
		List<Transaction> result = transactionRepository.findAll(Example.of(transaction));
		transaction = result.get(0);
		assertEquals(TradeType.BUY.toString(), transaction.getTradeType());
		assertEquals(100, transaction.getQuantity());
		assertEquals("REL", transaction.getSecurityCode());
		assertEquals(TradeOperation.INSERT.toString(), transaction.getOperation());
		assertEquals(1, transaction.getVersion());
	}

	@Test
	void testUpdateTrade() {
		Optional<Trade> trade = tradeRepository.findById(1);
		assertEquals(true, trade.isPresent());
		Trade tradeNow = trade.get();
		tradeNow.setTradeType(TradeType.SELL.toString());
		Trade tradeAfterUpdate = tradeRepository.updateTrade(tradeNow);

		assertEquals(TradeType.SELL.toString(), tradeAfterUpdate.getTradeType());
		assertEquals(2, tradeAfterUpdate.getVersion());
		assertEquals(TradeOperation.UPDATE.toString(), tradeAfterUpdate.getOperation());
	}

	@Test
	void testCancelTrade() {
		Optional<Trade> trade = tradeRepository.findById(1);
		assertEquals(true, trade.isPresent());
		Trade tradeNow = trade.get();
		tradeNow.setQuantity(200);
		tradeNow.setSecurityCode("ITC");
		Trade tradeAfterUpdate = tradeRepository.cancelTrade(tradeNow);
		
		assertEquals(100, tradeAfterUpdate.getQuantity());
		assertEquals("REL", tradeAfterUpdate.getSecurityCode());
		assertEquals(TradeOperation.CANCEL.toString(), tradeAfterUpdate.getOperation());
		
		tradeNow.setQuantity(300);
		assertThrows(CancelCanotModifyException.class, ()-> { tradeRepository.updateTrade(tradeNow); });
	}

}
