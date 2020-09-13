package com.example.shipment.managment.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.shipment.managment.po.Shipment;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ShipmentRepositoryTest {
	
	@Autowired
	private ShipmentRepository shipmentRepository;

	@Test
	void testFindAll() {
		List<Shipment> items = shipmentRepository.findAll();
		assertEquals(1, items.size()); 
		Shipment item = items.get(0);
		assertEquals(item.getId(), 1);
		assertEquals(item.getName(), "root");
		assertEquals(item.getQuantity(), 100);
	}
}
