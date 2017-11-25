package atm;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Scanner;

public class MainApp {
    private static Bank bank = new Bank("Super-Pupper-Atm-Bank-System");

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
        bank.addUser("Alex", User.DEFAULT_PIN);
        bank.addUser("Nik", User.DEFAULT_PIN);
    }
}
