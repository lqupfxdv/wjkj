package com.example.equity.position.controller;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.equity.position.dao.CancelCanotModifyException;
import com.example.equity.position.dto.Position;
import com.example.equity.position.dto.ResultDto;
import com.example.equity.position.po.Trade;
import com.example.equity.position.service.ResultStatusEnum;
import com.example.equity.position.service.SellOverException;
import com.example.equity.position.service.TradeService;

@RestController
@RequestMapping("/eqity")
public class EquityPositionController {
	private static final Log LOGGER = LogFactory.getLog(EquityPositionController.class);
	
	@Autowired
	private TradeService tradeService;
	
	@RequestMapping("/insert")
	public ResultDto<?> insertTrade(@RequestBody Trade trade){
		try {
			trade = tradeService.insertTrade(trade);
			return ResultDto.success(trade);
		} catch (SellOverException soe) {
			LOGGER.error(soe);
			return ResultDto.fail(ResultStatusEnum.SELL_OVER_ERROR);
		} catch (Exception e) {
			LOGGER.error(e);
			return ResultDto.fail(ResultStatusEnum.UNKNOWN_ERROR);
		}
	}
	
	@RequestMapping("/update")
	public ResultDto<?> updateTrade(@RequestBody Trade trade){
		try {
			trade = tradeService.updateTrade(trade);
			return ResultDto.success(trade);
		} catch (CancelCanotModifyException ccme) {
			LOGGER.error(ccme);
			return ResultDto.fail(ResultStatusEnum.CANCEL_CANOT_MODIFY_ERROR);
		} catch (SellOverException soe) {
			LOGGER.error(soe);
			return ResultDto.fail(ResultStatusEnum.SELL_OVER_ERROR);
		} catch (Exception e) {
			LOGGER.error(e);
			return ResultDto.fail(ResultStatusEnum.UNKNOWN_ERROR);
		}
	}
	
	@RequestMapping("/cancel")
	public ResultDto<?> cancelTrade(@RequestBody Trade trade){
		try {
			trade = tradeService.cancelTrade(trade);
			return ResultDto.success(trade);
		} catch (SellOverException soe) {
			LOGGER.error(soe);
			return ResultDto.fail(ResultStatusEnum.SELL_OVER_ERROR);
		} catch (Exception e) {
			LOGGER.error(e);
			return ResultDto.fail(ResultStatusEnum.UNKNOWN_ERROR);
		}
	}
	
	@RequestMapping("/position")
	public ResultDto<?> positionTrade(){
		try {
			Position position = new Position();
			Map<String, Integer> positionMap = tradeService.getPosition();
			position.setPosition(positionMap);
			return ResultDto.success(position);
		} catch (Exception e) {
			LOGGER.error(e);
			return ResultDto.fail(ResultStatusEnum.UNKNOWN_ERROR);
		}
	}
}
