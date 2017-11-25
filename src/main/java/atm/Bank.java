package atm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Bank {
    private String name;
    private List<User> listUser;
    private List<Account> listAccount;

    public String getName() {
        return name;
    }

    public Bank(String name) {
        this.name = name;
        listUser = new ArrayList<User>();
        listAccount = new ArrayList<Account>();
    }

    public String issueNewCardNumber() {
        String cardNumber;
        do {
            cardNumber = generateCardNumber();
        } while (isNonUniqueCard(cardNumber));
        return cardNumber;
    }

    public User addUser(String name, String pin) {
        User newUser = new User(name, pin, this);
        listUser.add(newUser);
        Account newAccount = new Account(newUser);
        newUser.addAccount(newAccount);
        listAccount.add(newAccount);
        return newUser;
    }

    public User userLogin(String cardNumber, String pin) {
        for (User u : listUser) {
            if (u.getCardId().compareTo(cardNumber) == 0 && u.checkPin(pin)) {
                return u;
            }
        }
        return null;
    }

    private boolean isNonUniqueCard(String cardNumber) {
        if (listUser.isEmpty()) {
            return false;
        }

        for (User u : listUser) {
            if (cardNumber.compareTo(u.getCardId()) == 0) {
                return true;
            }
        }
        return false;
    }

    private String generateCardNumber() {
        final int lenDigits = 8;
        StringBuilder result  = new StringBuilder();
        for (int i = 0; i < lenDigits; i++) {
            result.append(new Random().nextInt(10));
        }
        return result.toString();
    }

}
