package com.example.shipment.managment.dto;

public class ChangeRootDto {
	private Integer rootId;
	private Integer changeQuantity;
	public Integer getRootId() {
		return rootId;
	}
	public void setRootId(Integer rootId) {
		this.rootId = rootId;
	}
	public Integer getChangeQuantity() {
		return changeQuantity;
	}
	public void setChangeQuantity(Integer changeQuantity) {
		this.changeQuantity = changeQuantity;
	}
}
