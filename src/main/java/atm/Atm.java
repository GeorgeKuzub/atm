package atm;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Scanner;

public class Atm {

    public static User getLoginUser(Bank bank, Scanner sc) {
        User authUser;
        String cardNumber;
        String pin;
        int attemptsCounterDown = 3;

        do {
            System.out.printf("%nWelcome to %s%n", bank.getName());
            System.out.println("Enter your card number: ");
            cardNumber = sc.nextLine();
            System.out.println("Enter pin code: ");
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
            System.out.println("(4) Logout");
            System.out.println("(5) Exit\n");
            System.out.print("Make your choice: ");
            choice = sc.nextInt();

            if (choice < 1 || choice > 5) {
                System.out.println("Wrong choice. Please choose 1-5.");
            }

        } while (choice < 1 || choice > 5);

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
                sc.nextLine();
                return;
            case 5:
                System.out.printf("Thanks for you're with us, dear  %s. %n", user.getName());
                System.exit(0);
        }

        if (choice != 5) {
            Atm.showMenu(user, sc);
        }
    }

    private static void showBalance(User user, Scanner sc) {
        System.out.printf("Dear customer %s your balance:%n%d%n%n",
                user.getName(), user.getAccountBalance());
    }

    private static void deposit(User user, Scanner sc) {
        long amount;
        do {
            System.out.printf("Enter the amount to deposit: ");
            amount = sc.nextLong();

            if (amount < 0) {
                System.out.println("Input amount cannot be less then zero.");
            }
        } while (amount < 0);

        user.addAccountTransaction(amount);
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
            } else if  (amount > 0) {
                System.out.printf("Amount must be less or equal than current " +
                        "balance of %d.%n", actualBalance);
            }
        } while (amount < 0 || amount > actualBalance);

        user.addAccountTransaction(-1*amount); // the negative amount to lead to withdraw from balance
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

}