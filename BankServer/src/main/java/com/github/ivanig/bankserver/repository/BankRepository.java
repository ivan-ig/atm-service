package com.github.ivanig.bankserver.repository;

import com.github.ivanig.bankserver.model.Account;
import com.github.ivanig.bankserver.model.BankClient;
import com.github.ivanig.common.messages.RequestFromAtm;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import lombok.Data;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Data
public class BankRepository {

    private final DataSource dataSource;

    public BankRepository() {
        final String JDBC_DRIVER = "org.h2.Driver";
        final String DB_URL = "jdbc:h2:~/bank_db";
        final String USER = "sa";
        final String PASS = "1234";

        ComboPooledDataSource cpds = new ComboPooledDataSource();
        try {
            cpds.setDriverClass(JDBC_DRIVER);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
        cpds.setUser(USER);
        cpds.setPassword(PASS);
        cpds.setJdbcUrl(DB_URL);

        dataSource = cpds;
    }

    public Optional<BankClient> getClient(RequestFromAtm request) {

        BankClient client = null;

        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT client.first_name, client.last_name, account.account_number, " +
                    "account.card_number, account.balance, account.currency FROM client " +
                    "INNER JOIN account ON account.owner_id = client.id " +
                    "WHERE card_number = ? AND pin_code = ? AND first_name = ? AND last_name = ?;";
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setLong(1, request.getCardNumber());
                preparedStatement.setInt(2, request.getPIN());
                preparedStatement.setString(3, request.getFirstName());
                preparedStatement.setString(4, request.getLastName());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        client = extractResults(resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(client);
    }

    private BankClient extractResults(ResultSet resultSet) throws SQLException {

        String firstNameFromDB = resultSet.getString("FIRST_NAME");
        String lastNameFromDB = resultSet.getString("LAST_NAME");

        Set<Account> accountsFromDB = new HashSet<>();
        do {
            accountsFromDB.add(extractAccount(resultSet));
        } while (resultSet.next());

        return new BankClient(firstNameFromDB, lastNameFromDB, accountsFromDB);
    }

    private Account extractAccount(ResultSet resultSet) throws SQLException {
        return new Account(resultSet.getString("ACCOUNT_NUMBER"),
                resultSet.getLong("CARD_NUMBER"),
                resultSet.getString("CURRENCY"),
                resultSet.getBigDecimal("BALANCE"));
    }
}