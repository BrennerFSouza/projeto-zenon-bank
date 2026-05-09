package br.com.zenon.fraud;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TransactionMapRepository implements TransactionRepository{
    private final Map<String, Transaction> transactions;

    public TransactionMapRepository(List<Transaction> transactions) {
        Objects.requireNonNull(transactions);

        this.transactions = transactions
                .stream()
                .collect(Collectors.toMap(
                        transaction -> transaction.origin().name(),
                        Function.identity()
                ));
    }

    @Override
    public Optional<Transaction> findTransactionByOriginName(String OriginName) {
        return Optional.ofNullable(transactions.get(OriginName));
    }

    @Override
    public boolean save(Transaction transaction) {
        transactions.put(transaction.origin().name(), transaction);
        return true;
    }
}
