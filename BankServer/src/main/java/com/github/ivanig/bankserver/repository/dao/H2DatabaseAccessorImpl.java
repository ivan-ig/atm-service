package com.github.ivanig.bankserver.repository.dao;

import com.github.ivanig.bankserver.domain.Account;
import com.github.ivanig.bankserver.domain.BankClient;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class H2DatabaseAccessorImpl implements H2DatabaseAccessor {

    private final DataSource dataSource;

    public H2DatabaseAccessorImpl() {
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

    @Override
    public Optional<BankClient> getClientFromH2DB(String firstName, String lastName, long cardNumber) {

        BankClient client = null;

        try (Connection conn = dataSource.getConnection()) { // всё еще может вернуть null?
            String sql = "SELECT client.id, client.first_name, client.last_name, account.account_number, " +
                    "account.card_number, account.balance, account.currency FROM client " +
                    "INNER JOIN account ON account.owner_id = client.id " +
                    "WHERE card_number = ? AND first_name = ? AND last_name = ?;";
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setLong(1, cardNumber);
                preparedStatement.setString(2, firstName);
                preparedStatement.setString(3, lastName);
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

        long clientIdFromDB = resultSet.getLong("ID");
        String firstNameFromDB = resultSet.getString("FIRST_NAME");
        String lastNameFromDB = resultSet.getString("LAST_NAME");
        String accountNumberFromDB = resultSet.getString("ACCOUNT_NUMBER");
        long cardNumberFromDB = resultSet.getLong("CARD_NUMBER");
        String currencyFromDB = resultSet.getString("CURRENCY");
        BigDecimal balanceFromDB = resultSet.getBigDecimal("BALANCE");
        Set<Account> accountsFromDB = new HashSet<>();
        accountsFromDB.add(new Account(accountNumberFromDB, cardNumberFromDB, currencyFromDB, balanceFromDB));
        while (resultSet.next()) {
            clientIdFromDB = resultSet.getLong("ID");
            accountNumberFromDB = resultSet.getString("ACCOUNT_NUMBER");
            cardNumberFromDB = resultSet.getLong("CARD_NUMBER");
            currencyFromDB = resultSet.getString("CURRENCY");
            balanceFromDB = resultSet.getBigDecimal("BALANCE");
            accountsFromDB.add(new Account(accountNumberFromDB, cardNumberFromDB, currencyFromDB, balanceFromDB));
        }

        return new BankClient(clientIdFromDB, firstNameFromDB, lastNameFromDB, accountsFromDB);
    }
}
