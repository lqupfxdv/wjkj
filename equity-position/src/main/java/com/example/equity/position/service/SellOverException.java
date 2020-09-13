package com.example.equity.position.service;

public class SellOverException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public SellOverException() {
		super("Sell over quantity");
	}
}
