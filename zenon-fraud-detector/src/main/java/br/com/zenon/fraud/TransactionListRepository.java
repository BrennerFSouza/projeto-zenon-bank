package br.com.zenon.fraud;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class TransactionListRepository implements TransactionRepository {
    private final List<Transaction> transactions;
    public TransactionListRepository(List<Transaction> transactions){
        this.transactions = transactions;
    }

    @Override
    public Optional<Transaction> findTransactionByOriginName(String OriginName){
            Objects.requireNonNull(OriginName);
            return transactions.stream()
                    .filter(transaction -> transaction.origin().name().equals(OriginName)).findFirst();

    }

    @Override
    public boolean save(Transaction transaction) {
        transactions.add(transaction);
        return true;
    }


}
