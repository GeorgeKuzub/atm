package atm.logic;

import atm.Util;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Collections;
import java.util.Scanner;
import java.util.Set;

public class Atm {
    private static AtmStore atmStore = new AtmStore();

    public static User getLoginUser(Bank bank, Scanner sc) {
        User authUser;
        String cardNumber;
        String pin;
        int attemptsCounterDown = 3;

        do {
            System.out.printf("%nWelcome to %s%n", bank.getName());
            System.out.println("Enter your card number below: ");
            cardNumber = sc.nextLine();
            System.out.println("Enter pin code below: ");
            pin = sc.nextLine();

            authUser = bank.userLogin(cardNumber, pin);
            if (authUser == null) {
                System.out.println("\nIncorrect cardNumber and/or pinCode. ");
                if (attemptsCounterDown > 1) {
                        System.out.printf("You still have %d attempts. Please, try again.%n ",
                                --attemptsCounterDown);
                } else {
                    System.out.println("You used 3 times wrong password. " +
                            "Current session will be terminated immediately.");
                    System.exit(1);
                }
            }
        } while(authUser == null);

        return authUser;

    }

    public static void showMenu(User user, Scanner sc) {
        int choice;
        do {
            System.out.println("Please, make your choice: ");
            System.out.println("(1) Show balance");
            System.out.println("(2) Deposit");
            System.out.println("(3) Withdraw");
            System.out.println("(4) BankNote Status");
            System.out.println("(5) Logout");
            System.out.println("(6) Exit\n");
            System.out.print("Make your choice: ");
            choice = sc.nextInt();

            if (choice < 1 || choice > 6) {
                System.out.println("Wrong choice. Please choose 1-6.");
            }

        } while (choice < 1 || choice > 6);

        switch (choice) {
            case 1:
                Atm.showBalance(user, sc);
                break;
            case 2:
                Atm.deposit(user, sc);
                break;
            case 3:
                Atm.withdraw(user, sc);
                break;
            case 4:
                Atm.getBankNoteStatus();
                break;
            case 5:
                sc.nextLine();
                return;
            case 6:
                System.out.printf("Thanks for you're with us, dear  %s. %n", user.getName());
                System.exit(0);
        }

        Atm.showMenu(user, sc);
    }

    private static void getBankNoteStatus() {
        Set<Nominal> availableSetNominal = atmStore.getStoredBanknotes().keySet();

        if (availableSetNominal.isEmpty()) {
            System.out.println("\n[The ATM is empty now]\n");
            return;
        }

        System.out.println("\nThe following banknotes are being kept inside ATM:");
        availableSetNominal
                .stream()
                .sorted(Collections.reverseOrder())
                .map(banknote -> ("- " + banknote + ":"
                        + atmStore.getStoredBanknotes().get(banknote))).
                forEach(System.out::println);
        System.out.println();
    }

    private static void showBalance(User user, Scanner sc) {
        System.out.printf("Dear customer %s your balance: [%d] %n%n",
                user.getName(), user.getAccountBalance());
    }

    private static void deposit(User user, Scanner sc) {
        long amount;
        do {
            amount = requestAmountBanknotesToDeposit(sc);

            if (amount < 0) {
                System.out.println("Input amount cannot be less then zero.");
            }
        } while (amount < 0);

        if (amount != 0) {
            user.addAccountTransaction(amount);
            System.out.println("Transaction has been done successfully.");
            return;
        }

        System.out.println("Transaction was failed.\n " +
                "Please, check again your balance and requested amount.");

    }

    private static void withdraw(User user, Scanner sc) {
        long amount;
        long actualBalance;

        actualBalance = user.getAccountBalance();
        if (actualBalance == 0) {
            System.out.println("You can't make any withdraw since your current balance is '0'. \n");
            return;
        }

        do {
            System.out.println("Enter the amount to withdraw: ");
            amount = sc.nextLong();

            if (amount < 0) {
                System.out.println("Amount must be greater than zero.");
            } else if (amount > actualBalance) {
                System.out.printf("Amount must be less or equal than current " +
                        "balance of %d.%n", actualBalance);
            }
        } while (amount < 0 || amount > actualBalance);

        if (atmStore.unload(amount)) {
            user.addAccountTransaction(-1 * amount);
            System.out.println("Transaction has been done successfully.");
            return;
        }

        System.out.println("Transaction was failed.\n " +
                "Please, check again your balance and requested amount.");
    }


    private static void transfer() {
        throw new NotImplementedException();
    }

    private static void changePinCode() {
        throw new NotImplementedException();
    }

    private static void showCompletedTransactions() {
        throw new NotImplementedException();
    }

    private static long requestAmountBanknotesToDeposit(Scanner sc) {
        BankNoteAmount bnAmount = promptBankNoteAmount(sc);
        if (bnAmount == null) {
            return 0;
        }

        return atmStore.load(bnAmount.nominal, bnAmount.quantity);

    }

    private static BankNoteAmount promptBankNoteAmount(Scanner sc) {
        System.out.printf("Input type of banknote: index or full name below.%n");
        System.out.printf("[0 : %s], [1 : %s], [2 : %s], [3 : %s], [4: %s], [5 : %s], [6 : %s] %n " +
                        "or type 'q' to quit from current menu.%n",
                Nominal.values()[0], Nominal.values()[1], Nominal.values()[2], Nominal.values()[3],
                Nominal.values()[4], Nominal.values()[5], Nominal.values()[6]);

        Nominal banknote;
        String strInput;
        while (true) {
            strInput = sc.next();

            if (strInput.equalsIgnoreCase("q")) {
                return null;
            }

            try {
                if (Util.isItDigit(strInput)) {
                    banknote = Nominal.values()[Integer.valueOf(strInput)];
                    break;
                } else {
                    banknote = Nominal.valueOf(strInput.toUpperCase());
                    break;
                }
            } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
                System.out.println("Please, check again input type of banknote or press 'q' to quit.");
            }
        }

        System.out.println("Input the quantity of banknotes below:");
        int quantity = sc.nextInt();
        return new BankNoteAmount(banknote, quantity);
    }

    private static class BankNoteAmount {
        Nominal nominal;
        int quantity;

        BankNoteAmount(Nominal nominal, int quantity) {
            this.nominal = nominal;
            this.quantity = quantity;
        }
    }
}