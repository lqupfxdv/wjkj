package com.example.shipment.managment.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.shipment.managment.dao.ShipmentRepository;
import com.example.shipment.managment.dto.ChangeResultDto;
import com.example.shipment.managment.dto.ChangeRootDto;
import com.example.shipment.managment.dto.MergeDto;
import com.example.shipment.managment.dto.ResultDto;
import com.example.shipment.managment.dto.SplitDto;
import com.example.shipment.managment.po.Shipment;
import com.example.shipment.managment.service.ResultStatusEnum;
import com.fasterxml.jackson.core.JsonProcessingException;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ShipmentManagmentControllerTest {

	@Autowired
	private ShipmentManagmentController controller;
	
	@Autowired
	private ShipmentRepository shipmentRepository;
	
	/**
	 * 初始化数据源
	 */
	@BeforeEach
	void init() {
		List<Shipment> items = shipmentRepository.findAll();
		for (int i = 0; i < items.size(); i++) {
			Shipment it = items.get(i);
			if (it.getId() == 1) {
				items.remove(it);
				break;
			}
		}
		shipmentRepository.deleteAll(items);
	}
	
	/**
	 * 测试货物拆分
	 * @throws JsonProcessingException
	 */
	@Test
	void testSplit() throws JsonProcessingException {
		SplitDto splitDto = new SplitDto();
		splitDto.setRootId(1);
		ArrayList<Shipment> splits = new ArrayList<Shipment>();
		splits.add(new Shipment(20, "child1"));
		splits.add(new Shipment(30, "child2"));
		splits.add(new Shipment(50, "child3"));
		splitDto.setSplits(splits);
		ResultDto<?> result = controller.split(splitDto);
		assertEquals(result.getCode(), ResultStatusEnum.SUCCESS.getCode());
		@SuppressWarnings("unchecked")
		List<Shipment> items = (List<Shipment>)result.getDatas();
		assertEquals(items.size(), 3);
		Integer totalQuantity = 0;
		for(Shipment it:splits) {
			totalQuantity += it.getQuantity();
		}
		assertEquals(totalQuantity , 100);
	}
	
	/**
	 * 测试发货合并
	 */
	@Test
	void testMerge() {
		SplitDto splitDto = new SplitDto();
		splitDto.setRootId(1);
		ArrayList<Shipment> splits = new ArrayList<Shipment>();
		splits.add(new Shipment(20, "child1"));
		splits.add(new Shipment(30, "child2"));
		splits.add(new Shipment(50, "child3"));
		splitDto.setSplits(splits);
		ResultDto<?> splitResult = controller.split(splitDto);  // 先拆分，后合并
		assertEquals(splitResult.getCode(), ResultStatusEnum.SUCCESS.getCode());
		@SuppressWarnings("unchecked")
		List<Shipment> items = (List<Shipment>)splitResult.getDatas();
		
		Shipment m1 = items.get(1);
		Shipment m2 = items.get(2);
		List<Integer> mIds = Arrays.asList(m1.getId(), m2.getId());
		MergeDto mergeDto = new MergeDto();
		mergeDto.setMergeIds(mIds);
		ResultDto<?> mergeResult = controller.merge(mergeDto);  // 后合并
		assertEquals(mergeResult.getCode(), ResultStatusEnum.SUCCESS.getCode());
		Shipment merge = (Shipment)mergeResult.getDatas();
		assertEquals(merge.getQuantity(), m1.getQuantity() + m2.getQuantity());
	}
	
	/**
	 * 测试货物怎加减少
	 */
	@Test
	void testChangeRoot() {
		SplitDto splitDto = new SplitDto();
		splitDto.setRootId(1);
		ArrayList<Shipment> splits = new ArrayList<Shipment>();
		splits.add(new Shipment(20, "child1"));
		splits.add(new Shipment(30, "child2"));
		splits.add(new Shipment(50, "child3"));
		splitDto.setSplits(splits);
		ResultDto<?> splitResult = controller.split(splitDto);  // 先拆分，后合并
		assertEquals(splitResult.getCode(), ResultStatusEnum.SUCCESS.getCode());
		@SuppressWarnings("unchecked")
		List<Shipment> items = (List<Shipment>)splitResult.getDatas();
		
		Shipment m1 = items.get(1);
		Shipment m2 = items.get(2);
		List<Integer> mIds = Arrays.asList(m1.getId(), m2.getId());
		MergeDto mergeDto = new MergeDto();
		mergeDto.setMergeIds(mIds);
		ResultDto<?> mergeResult = controller.merge(mergeDto);  // 后合并
		assertEquals(mergeResult.getCode(), ResultStatusEnum.SUCCESS.getCode());
		Shipment merge = (Shipment)mergeResult.getDatas();
		assertEquals(merge.getQuantity(), m1.getQuantity() + m2.getQuantity());
		
		ChangeRootDto change = new ChangeRootDto();
		change.setChangeQuantity(100); // 加100
		change.setRootId(1);
		ResultDto<?> changeResult = controller.changeRoot(change);
		assertEquals(changeResult.getCode(), ResultStatusEnum.SUCCESS.getCode());
		ChangeResultDto changeDto = (ChangeResultDto)changeResult.getDatas();
		assertEquals(changeDto.getRoot().getQuantity(), 200);
		assertEquals(changeDto.getSplits().stream().mapToInt(it->it.getQuantity()).sum(), 200);
		assertEquals(changeDto.getMerges().get(0).getQuantity(), 160);
		
		change.setChangeQuantity(-100); // 减100
		change.setRootId(1);
		changeResult = controller.changeRoot(change);
		assertEquals(changeResult.getCode(), ResultStatusEnum.SUCCESS.getCode());
		changeDto = (ChangeResultDto)changeResult.getDatas();
		assertEquals(changeDto.getRoot().getQuantity(), 100);
		assertEquals(changeDto.getSplits().stream().mapToInt(it->it.getQuantity()).sum(), 100);
		assertEquals(changeDto.getMerges().get(0).getQuantity(), 80);
	}
}
