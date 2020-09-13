package com.example.equity.position.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.equity.position.po.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Integer>, TransactionRepositoryExtends {

}
