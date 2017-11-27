package atm;

import atm.logic.Atm;
import atm.logic.Bank;
import atm.logic.User;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class MainApp {
    private static Bank bank = new Bank("Super-Pupper-System-Bank");

    public static void main(String[] args) {
        initCustomers();
        User user;
        Scanner sc = new Scanner(System.in,
                String.valueOf(StandardCharsets.UTF_8));

        while (true) {
            user = Atm.getLoginUser(bank, sc);
            Atm.showMenu(user, sc);
        }
    }

    private static void initCustomers() {
        System.out.println();
        bank.addUser("Alex", User.DEFAULT_PIN);
        bank.addUser("Nik", User.DEFAULT_PIN);
    }
}
