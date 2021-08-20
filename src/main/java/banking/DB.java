package banking;

import java.io.IOException;
import java.sql.*;

public class DB {

    public static Connection connection;

    public static void open(String url) {
        try {
            connection = DriverManager.getConnection(url);
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createCardTable () {
        try (Statement statement = DB.connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS card (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "number TEXT," +
                    "pin TEXT, " +
                    "balance INTEGER DEFAULT 0" +
                    ")"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertIntoCardTable (String cardNumber, String pin, int balance) {
        String query = "INSERT INTO card(number, pin, balance) values (?, ?, ?)";

        try (PreparedStatement statement = DB.connection.prepareStatement(query)) {
            statement.setString(1, cardNumber);
            statement.setString(2, pin);
            statement.setInt(3, balance);

            statement.executeUpdate();
            DB.connection.commit();

        } catch (SQLException e) {
            if (connection != null) {
                try {
                    System.err.println("Transaction is being rolled back.");
                    connection.rollback();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            e.printStackTrace();
        }
    }

    public static Account findAccountByCardName (String cardNumber) {
        String query = "SELECT * FROM card WHERE number = ?";

        Account acc = null;

        try (PreparedStatement statement = DB.connection.prepareStatement(query)) {
            statement.setString(1, cardNumber);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                acc = new Account(rs.getString("number"), rs.getString("pin"), rs.getInt("balance"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return acc;

    }

    public static void addIncome (Account account, int income) {

        String query = "UPDATE card SET balance = balance + ? WHERE number = ?";

        try (PreparedStatement statement = DB.connection.prepareStatement(query)) {
            statement.setInt(1, income);
            statement.setString(2, account.getCardNumber());
            statement.executeUpdate();
            DB.connection.commit();

        } catch (SQLException e) {
            if (connection != null) {
                try {
                    System.err.println("Transaction is being rolled back.");
                    connection.rollback();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            e.printStackTrace();
        }
    }

    public static void transferMoney (Account fromAccount, Account toAccount, int money) {
        String getMoney = "UPDATE card SET balance = balance - ? WHERE number = ?";
        String setMoney = "UPDATE card SET balance = balance + ? WHERE number = ?";

        try (
                PreparedStatement getMoneyStatement = DB.connection.prepareStatement(getMoney);
                PreparedStatement setMoneyStatement = DB.connection.prepareStatement(setMoney);
        ) {
            getMoneyStatement.setInt(1, money);
            getMoneyStatement.setString(2, fromAccount.getCardNumber());
            getMoneyStatement.executeUpdate();

            setMoneyStatement.setInt(1, money);
            setMoneyStatement.setString(2, toAccount.getCardNumber());
            setMoneyStatement.executeUpdate();

            DB.connection.commit();

        } catch (SQLException e) {
            if (connection != null) {
                try {
                    System.err.println("Transaction is being rolled back.");
                    connection.rollback();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            e.printStackTrace();
        }
    }

    public static void deleteAccount (Account account) {

        String query = "delete from card WHERE number = ?";

        try (PreparedStatement statement = DB.connection.prepareStatement(query)) {
            statement.setString(1, account.getCardNumber());
            statement.executeUpdate();
            DB.connection.commit();

        } catch (SQLException e) {
            if (connection != null) {
                try {
                    System.err.println("Transaction is being rolled back.");
                    connection.rollback();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            e.printStackTrace();
        }
    }


}
