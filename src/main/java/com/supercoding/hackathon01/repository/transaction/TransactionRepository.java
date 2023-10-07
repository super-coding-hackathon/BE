package com.supercoding.hackathon01.repository.transaction;

import com.supercoding.hackathon01.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends TransactionCustomRepository, JpaRepository<Transaction, Long> {
}
