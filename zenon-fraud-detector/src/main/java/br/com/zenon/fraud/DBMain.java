package br.com.zenon.fraud;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public class DBMain {
    static void main() {
        String arquivo = "PS_20174392719_1491204439457_log.csv";
        TransactionIngestor transactionIngestor = new TransactionIngestor();
        List<Transaction> transactionsList = transactionIngestor.read(arquivo);
        TransactionSQLRepository transactionSQLRepository = new TransactionSQLRepository();

        /*
        System.out.println("=========================");
        System.out.println("Inserção no DB");

        Transaction transaction1 = transactionsList.get(0);

        transactionSQLRepository.insertNewTransaction(transaction1);
        */

        System.out.println("=========================");
        System.out.println("Consulta no DB");

        Optional<Transaction> consultaTransaction = transactionSQLRepository.findTransactionByOriginName("C1231006815");
        System.out.println(consultaTransaction.get());



    }
}
