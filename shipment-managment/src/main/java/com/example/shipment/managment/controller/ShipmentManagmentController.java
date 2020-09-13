package com.example.shipment.managment.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.shipment.managment.dto.ChangeResultDto;
import com.example.shipment.managment.dto.ChangeRootDto;
import com.example.shipment.managment.dto.MergeDto;
import com.example.shipment.managment.dto.ResultDto;
import com.example.shipment.managment.dto.SplitDto;
import com.example.shipment.managment.po.Shipment;
import com.example.shipment.managment.po.ShipmentParentChild;
import com.example.shipment.managment.service.ResultStatusEnum;
import com.example.shipment.managment.service.ShipmentParentChildService;
import com.example.shipment.managment.service.ShipmentService;

/**
 * 货物管理控制器，三个功能，货物拆分、合并、变更
 * @author lqupf
 *
 */
@RestController
@RequestMapping("/shipment")
public class ShipmentManagmentController {
	private static final Log LOGGER = LogFactory.getLog(ShipmentManagmentController.class);
	
	@Autowired
	private ShipmentService shipmentService;
	
	@Autowired
	private ShipmentParentChildService shipmentParentChildService;
	
	@RequestMapping(value = "/split", method = RequestMethod.POST)
	public ResultDto<?> split(@RequestBody SplitDto splitDto) {
		Optional<Shipment> shipment = shipmentService.getShipmentById(splitDto.getRootId());
		if(!shipment.isPresent()) {
			return ResultDto.fail(ResultStatusEnum.NOT_FOUND);
		}
		
		Integer rootQuantity = shipment.get().getQuantity();
		Integer totalQuantity = 0;
		if(splitDto.getSplits() == null || splitDto.getSplits().size() <= 0) {
			return ResultDto.fail(ResultStatusEnum.PARAM_ERROR);
		}
		
		for(Shipment item : splitDto.getSplits()) {
			totalQuantity += item.getQuantity();
		}
		if(totalQuantity != rootQuantity) {
			return ResultDto.fail(ResultStatusEnum.PARAM_ERROR);
		}
		
		try {
			List<Shipment> items = shipmentService.saveShipments(splitDto.getSplits());
			shipmentParentChildService.saveParentChilds(splitDto.getRootId(), 
					items.stream().mapToInt(it->it.getId()).toArray());
			return ResultDto.success(items);
		} catch (Exception e) {
			LOGGER.error(e);
			return ResultDto.fail(ResultStatusEnum.UNKNOWN_ERROR, e.getMessage());
		}
	}
	
	@RequestMapping(value = "/merge", method = RequestMethod.POST)
	public ResultDto<?> merge(@RequestBody MergeDto mergeDto) {
		if(mergeDto == null || mergeDto.getMergeIds() == null || mergeDto.getMergeIds().size() <= 0) {
			return ResultDto.fail(ResultStatusEnum.PARAM_ERROR);
		}
		
		List<Shipment> items = shipmentService.getShipments(mergeDto.getMergeIds());
		if(items == null || items.size() <= 0) {
			return ResultDto.fail(ResultStatusEnum.NOT_FOUND);
		}
		
		StringBuilder sb = new StringBuilder("merge");
		Integer mergeQuantity = 0;
		for(Shipment item : items) {
			sb.append("_" + item.getId());
			mergeQuantity += item.getQuantity();
		}
		Shipment merge = new Shipment();
		merge.setName(sb.toString());
		merge.setQuantity(mergeQuantity);
		merge = shipmentService.saveShipment(merge);
		shipmentParentChildService.saveChildParents(merge.getId(), items.stream().mapToInt(it->it.getId()).toArray());
		return ResultDto.success(merge);
	}
	
	@RequestMapping(value = "/changeroot", method = RequestMethod.POST)
	public ResultDto<?> changeRoot(@RequestBody ChangeRootDto change) {
		if(change == null || change.getRootId() == null 
				|| change.getChangeQuantity() == null || change.getChangeQuantity() == 0) {
			return ResultDto.fail(ResultStatusEnum.PARAM_ERROR);
		}
		
		Optional<Shipment> root = shipmentService.getShipmentById(change.getRootId());
		if(!root.isPresent()) {
			return ResultDto.fail(ResultStatusEnum.NOT_FOUND);
		}
		
		try {
			ChangeResultDto result = inOrDecreament(root.get(), change.getChangeQuantity());
			return ResultDto.success(result);
		} catch (Exception ex) {
			LOGGER.error(ex);
			return ResultDto.fail(ResultStatusEnum.UNKNOWN_ERROR, ex.getMessage());
		}
	}
	
	private ChangeResultDto inOrDecreament(Shipment root, Integer change) {
		if(root == null) {
			return null;
		}

		// root 节点变更
		int afterChange = root.getQuantity() + change;
		if(afterChange <= 0) {
			LOGGER.error("change too big to use");
			return null;
		}
		root.setQuantity(afterChange);
		shipmentService.saveShipment(root);
		ChangeResultDto result = new ChangeResultDto();
		result.setRoot(root);
		
		// root 的子节点变更
		List<ShipmentParentChild> childs = shipmentParentChildService.getChildsByParentId(root.getId());
		if(childs != null && childs.size() > 0) {
			List<Integer> childIds = childs.stream().map(c -> c.getChildId()).collect(Collectors.toList());
			List<Shipment> childShipments = shipmentService.getShipments(childIds);

			Integer totalQuantity = childShipments.stream().mapToInt(it -> it.getQuantity()).sum();
			Integer target = totalQuantity + change;
			Double changeRate = 1 + (change * 1.0) / totalQuantity;
			Integer realTotal = 0;
			for(Shipment it : childShipments) {
				Double newQuantity = it.getQuantity() * changeRate;
				realTotal += newQuantity.intValue();
				it.setQuantity(newQuantity.intValue());
				it.setParents(Arrays.asList(root));
			}
			Integer sub = target - realTotal;
			if(sub != 0) {
				for(Shipment it : childShipments) {
					int chSub = it.getQuantity() + sub;
					if(chSub > 0) {
						it.setQuantity(chSub);
						break;
					}
				}
			}
			shipmentService.saveShipments(childShipments);
			result.setSplits(childShipments);
			
			// merge 子节点变更
			List<ShipmentParentChild> mergeItems = shipmentParentChildService.getChildsByParentIds(childIds);
			Map<Integer, List<Integer>> childParentMergeMap = new HashMap<>();
			for(ShipmentParentChild it : mergeItems) {
				List<Integer> parentIds = childParentMergeMap.get(it.getChildId());
				if(parentIds == null) {
					parentIds = new ArrayList<Integer>();
					childParentMergeMap.put(it.getChildId(), parentIds);
				}
				parentIds.add(it.getParentId());
			}
			List<Shipment> merges = new ArrayList<>();
			for(Entry<Integer, List<Integer>> entry : childParentMergeMap.entrySet()) {
				Optional<Shipment> child = shipmentService.getShipmentById(entry.getKey());
				if(!child.isPresent()) {
					continue;
				}
				List<Shipment> parents = shipmentService.getShipments(entry.getValue());
				child.get().setQuantity(parents.stream().mapToInt(it->it.getQuantity()).sum());
				shipmentService.saveShipment(child.get());
				child.get().setParents(parents);
				merges.add(child.get());
			}
			result.setMerges(merges);
		}
		return result;
	}
}
