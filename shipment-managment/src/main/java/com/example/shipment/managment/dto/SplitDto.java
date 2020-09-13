package com.example.shipment.managment.dto;

import java.util.List;

import com.example.shipment.managment.po.Shipment;

public class SplitDto {
	private Integer rootId;
	private List<Shipment> splits;
	
	public Integer getRootId() {
		return rootId;
	}
	public void setRootId(Integer rootId) {
		this.rootId = rootId;
	}
	public List<Shipment> getSplits() {
		return splits;
	}
	public void setSplits(List<Shipment> splits) {
		this.splits = splits;
	}
}
