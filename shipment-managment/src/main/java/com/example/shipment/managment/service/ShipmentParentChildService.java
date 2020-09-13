package com.example.shipment.managment.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.example.shipment.managment.dao.ShipmentParentChildRepository;
import com.example.shipment.managment.po.ShipmentParentChild;

@Service
public class ShipmentParentChildService {
	@Autowired
	private ShipmentParentChildRepository shipmentParentChildRepository;
	
	public List<ShipmentParentChild> saveItems(List<ShipmentParentChild> items) {
		return shipmentParentChildRepository.saveAll(items);
	}
	
	public List<ShipmentParentChild> saveParentChilds(Integer parentId, int[] childIds) {
		List<ShipmentParentChild> items = new ArrayList<>();
		for(Integer c : childIds) {
			items.add(new ShipmentParentChild(parentId, c));
		}
		return saveItems(items);
	}
	
	public List<ShipmentParentChild> saveChildParents(Integer childId, int[] parentIds) {
		List<ShipmentParentChild> items = new ArrayList<>();
		for(Integer p : parentIds) {
			items.add(new ShipmentParentChild(p , childId));
		}
		return saveItems(items);
	}
	
	public List<ShipmentParentChild> getParentsByChildId(Integer childId) {
		ShipmentParentChild probe = new ShipmentParentChild();
		probe.setChildId(childId);
		return shipmentParentChildRepository.findAll(Example.of(probe));
	}
	
	public List<ShipmentParentChild> getChildsByParentId(Integer parentId) {
		ShipmentParentChild probe = new ShipmentParentChild();
		probe.setParentId(parentId);
		return shipmentParentChildRepository.findAll(Example.of(probe));
	}
	
	public List<ShipmentParentChild> getChildsByParentIds(List<Integer> parentIds) {
		List<ShipmentParentChild> result = new ArrayList<>();
		for(Integer id : parentIds) {
			List<ShipmentParentChild> items = getChildsByParentId(id);
			result.addAll(items);
		}
		return result;
	}
	
}
