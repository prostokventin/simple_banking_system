package banking;

import java.io.IOException;
import java.util.Random;

public class Account {

    private String cardNumber;
    private String pin;
    private int balance;

    public void setBalance(int balance) { this.balance = balance; }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getPin() {
        return pin;
    }

    public int getBalance() {
        return balance;
    }

    public Account() {
        String bankIdentificationNumber = "400000";
        String accountIdentificationNumber = generateRandomNumber(9);
        int checkSum = findSum(bankIdentificationNumber + accountIdentificationNumber);
        this.cardNumber = bankIdentificationNumber + accountIdentificationNumber + checkSum;
        this.pin = generateRandomNumber(4);
        this.balance = 0;
    }

    public Account(String cardNumber, String pin, int balance) {
//        if (checkSum(cardNumber)) {
            this.cardNumber = cardNumber;
            this.pin = pin;
            this.balance = balance;
//        } else {
//            throw new IllegalArgumentException("Probably you made a mistake in the card number. Please try again!");
//        }
    }

    private String generateRandomNumber (int numberLength) {
        Random random = new Random();
        String number = "";
        for (int i = 0; i < numberLength; i++) {
            number += String.valueOf(random.nextInt(10));
        }

        return number;
    }

    public boolean login (String cardNumber, String pin) {
        return this.cardNumber.equals(cardNumber) && this.pin.equals(pin);
    }

    private static int findSum (String stringNumber) {

        char[] charArrayNumber = stringNumber.toCharArray();
        int[] numberArrayNumber = new int[charArrayNumber.length];
        for (int i = 0; i < numberArrayNumber.length; i++) {
            numberArrayNumber[i] = Character.getNumericValue(charArrayNumber[i]);
        }

        int sumOfNumbers = 0;
        int checksum = 0;

        for (int i = 0; i < numberArrayNumber.length; i++) {
            if ((i + 1) % 2  != 0) {
                numberArrayNumber[i] *= 2;
            }
            if (numberArrayNumber[i] > 9) {
                numberArrayNumber[i] -= 9;
            }
            sumOfNumbers += numberArrayNumber[i];
        }

        for (int i = 0; i < 10; i++) {
            if ((sumOfNumbers + i) % 10 == 0) {
                checksum = i;
            };
        }

        return checksum;
    }

    public static boolean checkSum (String cardNumber)  {

        String cardNumberWithoutSum = cardNumber.substring(0, cardNumber.length() - 1);
        String cardNumberSum = cardNumber.substring(cardNumber.length() - 1);

        return String.valueOf(findSum(cardNumberWithoutSum)).equals(cardNumberSum);

    }

}
