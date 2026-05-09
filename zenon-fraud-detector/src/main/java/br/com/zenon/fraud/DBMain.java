package br.com.zenon.fraud;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public class DBMain {
    static void main() {
        String arquivo = "PS_20174392719_1491204439457_log.csv";
        TransactionIngestor transactionIngestor = new TransactionIngestor();
        TransactionSQLRepository transactionSQLRepository = new TransactionSQLRepository();

        String originName;
        Optional<Transaction> consultaTransaction;
        List<Transaction> transactionsList = transactionIngestor.read(arquivo);
        System.out.println("=========================");
        System.out.println("Inserção no DB");

        Transaction transaction;
        long timeBefore;
        long timeAfter;
        timeBefore = System.nanoTime();
        transactionsList.forEach(transactionSQLRepository::save);
        timeAfter = System.nanoTime();

        System.out.println("Tempo de Execução: " + (timeAfter - timeBefore) / 1000000 + "ms");

        System.out.println("=========================");
        System.out.println("Consulta no DB");

        originName = "C1231006815";
        consultaTransaction = transactionSQLRepository.findTransactionByOriginName(originName);
        consultaTransaction.ifPresent(System.out::println);
        originName = "C12345";
        consultaTransaction = transactionSQLRepository.findTransactionByOriginName(originName);
        consultaTransaction.ifPresent(System.out::println);

    }
}
