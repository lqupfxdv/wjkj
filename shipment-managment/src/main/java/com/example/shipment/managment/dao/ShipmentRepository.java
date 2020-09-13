package com.example.shipment.managment.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shipment.managment.po.Shipment;

/**
 * JPA 发货管理数据操作
 * @author lqupf
 *
 */
public interface ShipmentRepository extends JpaRepository<Shipment, Integer>, ShipmentRepositoryExtends {
	
}
