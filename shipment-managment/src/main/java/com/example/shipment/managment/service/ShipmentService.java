package com.example.shipment.managment.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.shipment.managment.dao.ShipmentRepository;
import com.example.shipment.managment.po.Shipment;

@Service
public class ShipmentService {
	@Autowired
	private ShipmentRepository shipmentRepository;
	
	public Optional<Shipment> getShipmentById(Integer id) {
		return shipmentRepository.findById(id);
	}
	
	public List<Shipment> saveShipments(List<Shipment> shipments) {
		return shipmentRepository.saveAll(shipments);
	}
	
	public List<Shipment> getShipments(List<Integer> shipmentIds) {
		return shipmentRepository.findAllById(shipmentIds);
	}
	
	public Shipment saveShipment(Shipment shipment) {
		return shipmentRepository.save(shipment);
	}
}
