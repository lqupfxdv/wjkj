package com.example.shipment.managment.po;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL) 
public class Shipment {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) // GenerationType.AUTO) // 这个用于支持自增的数据库
	private Integer id;
	
	private Integer quantity;
	private String name;
	
	public Shipment(Integer quantity, String name) {
		this.quantity = quantity;
		this.name = name;
	}
	
	public Shipment() {
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Transient
	private List<Shipment> parents;
	public List<Shipment> getParents() {
		return parents;
	}
	public void setParents(List<Shipment> parents) {
		this.parents = parents;
	}
	
	@Override
	public String toString() {
		return "Shipment [id=" + id + ", quantity=" + quantity + ", name=" + name + ", parents=" + parents + "]";
	}
}
