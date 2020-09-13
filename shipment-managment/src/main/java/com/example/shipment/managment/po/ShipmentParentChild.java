package com.example.shipment.managment.po;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ShipmentParentChild {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) // GenerationType.AUTO) // 这个用于支持自增的数据库
	private Integer id;
	
	private Integer parentId;
	private Integer childId;
	
	public ShipmentParentChild(Integer parentId, Integer childId) {
		this.parentId = parentId;
		this.childId = childId;
	}
	public ShipmentParentChild() {
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public Integer getChildId() {
		return childId;
	}
	public void setChildId(Integer childId) {
		this.childId = childId;
	}
	
}
