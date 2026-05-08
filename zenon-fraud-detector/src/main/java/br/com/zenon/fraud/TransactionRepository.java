package br.com.zenon.fraud;

import java.util.Optional;

public interface TransactionRepository {
    Optional<Transaction> findTransactionByOriginName(String originName);
    boolean save(Transaction transaction);
}
