package br.com.zenon.fraud;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class TransactionSQLRepository implements TransactionRepository{
    public boolean insertNewTransaction(Transaction transaction) {
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
                IO.println(rs.getString("name_origin"));
            }



        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }
}
