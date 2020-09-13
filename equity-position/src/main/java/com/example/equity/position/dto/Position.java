package com.example.equity.position.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL) 
public class Position {
	private Map<String, Integer> position;

	public Map<String, Integer> getPosition() {
		return position;
	}

	public void setPosition(Map<String, Integer> position) {
		this.position = position;
	}
}
