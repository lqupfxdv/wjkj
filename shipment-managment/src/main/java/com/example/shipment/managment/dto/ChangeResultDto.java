package com.example.shipment.managment.dto;

import java.util.List;

import com.example.shipment.managment.po.Shipment;

public class ChangeResultDto {
	private Shipment root;
	private List<Shipment> splits;
	private List<Shipment> merges;
	
	public Shipment getRoot() {
		return root;
	}
	public void setRoot(Shipment root) {
		this.root = root;
	}
	public List<Shipment> getSplits() {
		return splits;
	}
	public void setSplits(List<Shipment> splits) {
		this.splits = splits;
	}
	public List<Shipment> getMerges() {
		return merges;
	}
	public void setMerges(List<Shipment> merges) {
		this.merges = merges;
	}
}
