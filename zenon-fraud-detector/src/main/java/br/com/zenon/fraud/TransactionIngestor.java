package br.com.zenon.fraud;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class TransactionIngestor {
    public static final int FRAUD_LIMIT = 10_000;
    public List<Transaction> read(String documentName){
        Path path = Path.of("./data/" + documentName);
        try{
            List<String> lines = Files.readAllLines(path);
            return lines.stream()
                    .skip(1)
                    .limit(FRAUD_LIMIT)
                    .map(this::parseTransaction)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();



        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*public List<Transaction> readOld(String documentName) {
        ArrayList<Transaction> transactions = new ArrayList<>();

            Path path = Path.of("./data/" + documentName);
        try(FileInputStream fis = new FileInputStream(path.toFile());
            Scanner scanner = new Scanner(fis)) {
            int countLines = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (countLines == 0) {
                    countLines++;
                    continue;
                }
                if (countLines == 1001) {
                    break;
                }

                var transaction = parseTransaction(line);

                transactions.add(transaction);

                countLines++;
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return transactions;
    }*/

    private Optional<Transaction> parseTransaction(String line) {
        String[] lineArray = line.split(",");
        try{
            int step = Integer.parseInt(lineArray[0]);
            TransactionType type = TransactionType.valueOf(lineArray[1]);
            BigDecimal amount = new BigDecimal(lineArray[2]);

            TransactionCustomer origin = new TransactionCustomer(lineArray[3], new BigDecimal(lineArray[4]), new BigDecimal(lineArray[5]));
            TransactionCustomer destin = new TransactionCustomer(lineArray[6], new BigDecimal(lineArray[7]), new BigDecimal(lineArray[8]));
            boolean isFraud = "1".equals(lineArray[9]);
            boolean isFlaggedFraud = "1".equals(lineArray[10]);
            return Optional.of(new Transaction(step, type, amount, origin, destin, isFraud, isFlaggedFraud));
        }catch (Exception e) {
            System.err.println("ERRO: " + line + " | " + e);
            return Optional.empty();

        }


    }
}

