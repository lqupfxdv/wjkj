package com.example.shipment.managment.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shipment.managment.po.ShipmentParentChild;

/**
 * JPA 货物关联数据操作，自动生成方法
 * @author lqupf
 *
 */
public interface ShipmentParentChildRepository extends JpaRepository<ShipmentParentChild, Integer>, ShipmentParentChildRepositoryExtends {

}
