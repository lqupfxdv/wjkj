package com.example.equity.position.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


public class TransactionRepositoryImpl implements TransactionRepositoryExtends {
	@PersistenceContext
	private EntityManager entityManager;
}
