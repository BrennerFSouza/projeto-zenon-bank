package br.com.zenon.fraud;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class TransactionSQLRepository implements TransactionRepository{
    @Override
    public boolean save(Transaction transaction) {
        String sql = """
                INSERT INTO zenon_frauds.transactions
                (step, `type`, amount, name_origin, old_balance_origin, new_balance_origin, name_recipient, old_balance_recipient, new_balance_recipient, is_fraud, is_flagged_fraud)
                VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
                """;

        try(
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
                ){
            ps.setString(1, String.valueOf(transaction.step()));
            ps.setString(2, transaction.transationType().name());
            ps.setString(3, transaction.amount().toString());
            ps.setString(4, transaction.origin().name());
            ps.setString(5, transaction.origin().oldBalance().toString());
            ps.setString(6, transaction.origin().newBalance().toString());
            ps.setString(7, transaction.destin().name());
            ps.setString(8, transaction.destin().oldBalance().toString());
            ps.setString(9, transaction.destin().newBalance().toString());
            ps.setString(10, transaction.isFraud() ? "1" : "0");
            ps.setString(11, transaction.isFlaggedFraud()?"1":"0");
            
            ps.executeUpdate();

            System.out.println("Transação inserida com sucesso!:" + transaction);
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Optional<Transaction> findTransactionByOriginName(String originName) {
        String sql = """
                SELECT id, step, `type`, amount, name_origin, old_balance_origin, new_balance_origin, name_recipient, old_balance_recipient, new_balance_recipient, is_fraud, is_flagged_fraud
                FROM zenon_frauds.transactions
                where name_origin = ?
                LIMIT 1;
                """;
        try (
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1, originName);

            try (var rs = ps.executeQuery()) {
                if (!rs.next()) {
                    System.out.println("Nenhum resultado encontrado: " + originName);
                    return Optional.empty();
                }

                int step = rs.getInt("step");
                TransactionType type = TransactionType.valueOf(rs.getString("type"));
                BigDecimal amount = new BigDecimal(rs.getString("amount"));

                TransactionCustomer origin = new TransactionCustomer(
                        rs.getString("name_origin"),
                        new BigDecimal(rs.getString("old_balance_origin")),
                        new BigDecimal(rs.getString("new_balance_origin"))
                );

                TransactionCustomer destin = new TransactionCustomer(
                        rs.getString("name_recipient"),
                        new BigDecimal(rs.getString("old_balance_recipient")),
                        new BigDecimal(rs.getString("new_balance_recipient"))
                );

                boolean isFraud = "1".equals(rs.getString("is_fraud"));
                boolean isFlaggedFraud = "1".equals(rs.getString("is_flagged_fraud"));

                return Optional.of(new Transaction(step, type, amount, origin, destin, isFraud, isFlaggedFraud));
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
