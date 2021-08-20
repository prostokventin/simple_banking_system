package banking;

import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        String url = "jdbc:sqlite:./card.s3db";

        DB.open(url);

        DB.createCardTable();

        Scanner s = new Scanner(System.in);
        showMainMenu();
        int mainMenuChoise = s.nextInt();

        while (true) {
            switch (mainMenuChoise) {
                case 1:
                    Account acc = new Account();
                    String accCardNumber = acc.getCardNumber();
                    String accPin = acc.getPin();
                    int accBalance = acc.getBalance();
                    DB.insertIntoCardTable(accCardNumber, accPin, accBalance);
                    System.out.println("Your card has been created");
                    System.out.println("Your card number:");
                    System.out.println(accCardNumber);
                    System.out.println("Yout card PIN:");
                    System.out.println(accPin);
                    break;
                case 2:
                    System.out.println("Enter yout card number:");
                    String inputCardNumber = s.next();
                    System.out.println("Enter your PIN:");
                    String inputPin = s.next();

                    Account foundAccount = null;
                    foundAccount = DB.findAccountByCardName(inputCardNumber);
                    if (foundAccount == null || !foundAccount.getPin().equals(inputPin)) {
                        System.out.println("Wrong card number or PIN!");
                        break;
                    } else {
                        System.out.println("You have successfully logged in!");
                        System.out.println();

                        showLoggedMenu();
                        int loggedMenuChoise = s.nextInt();

                        while (true) {
                            try {
                                if (loggedMenuChoise == 1) {
                                    System.out.println("Balance: " + foundAccount.getBalance());
                                }
                                if (loggedMenuChoise == 2) {
                                    System.out.println("");
                                    System.out.println("Enter income:");
                                    int income = s.nextInt();
                                    DB.addIncome(foundAccount, income);
                                    foundAccount.setBalance(foundAccount.getBalance() + income);
                                    System.out.println("Income was added!");
                                }
                                // 4000003453586113
                                // 4000003305160035
                                // 4000003291012850
                                if (loggedMenuChoise == 3) {
                                    System.out.println("");
                                    System.out.println("Enter card number:");
                                    String transferCardNumber = s.next();
                                    Account foundTransferAccount = null;

                                    if (!Account.checkSum(transferCardNumber)) {
                                        throw new IOException("Probably you made a mistake in the card number. Please try again!");
                                    }
                                    foundTransferAccount = DB.findAccountByCardName(transferCardNumber);
                                    if (foundTransferAccount == null) {
                                        throw new IOException("Such a card does not exist.");
                                    }

                                    System.out.println("Enter how much money you want to transfer:");
                                    int transferMoney = s.nextInt();

                                    if (transferMoney > foundAccount.getBalance()) {
                                        throw new IOException("Not enough money!");
                                    }
                                    DB.transferMoney(foundAccount, foundTransferAccount, transferMoney);
                                    foundAccount.setBalance(foundAccount.getBalance() - transferMoney);
                                    System.out.println("Success!");
                                }
                                if (loggedMenuChoise == 4) {
                                    DB.deleteAccount(foundAccount);
                                    System.out.println("The account has been closed!");
                                    break;
                                }
                                if (loggedMenuChoise == 5) {
                                    System.out.println("You have successfully logged out!");
                                    break;
                                }
                                if (loggedMenuChoise == 0) {
                                    System.out.println("Bye!");
                                    DB.close();
                                    return;
                                }
                            } catch (IOException e) {
                                System.out.println(e.getMessage());
                            }

                            System.out.println();
                            showLoggedMenu();
                            loggedMenuChoise = s.nextInt();


                        }
                    }
                    break;
                case 0:
                    System.out.println("Bye!");
                    DB.close();
                    return;
            }

            System.out.println();
            showMainMenu();
            mainMenuChoise = s.nextInt();


        }
    }

    public static void showMainMenu () {
        System.out.println("1. Create an account");
        System.out.println("2. Log into account");
        System.out.println("0. Exit");
    }

    public static void showLoggedMenu () {
        System.out.println("1. Balance");
        System.out.println("2. Add income");
        System.out.println("3. Do transfer");
        System.out.println("4. Close account");
        System.out.println("5. Log out");
        System.out.println("0. Exit");
    }

}