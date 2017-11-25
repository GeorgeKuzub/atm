package atm;


public class User {
    public final static String DEFAULT_PIN = "1111";

    private String name;
    private String cardId;
    private byte hash[];
    private Account userAccount;

    public User (String name, String pin, Bank bank) {
        this.name = name;
        this.cardId = bank.issueNewCardNumber();
        this.hash = Util.generateMD5Hash(pin);

        System.out.printf("User %s with card numer %s created with default pin code %s.%n",
                name, cardId, DEFAULT_PIN);
    }

    public String getName() {
        return name;
    }

    public String getCardId() {
        return cardId;
    }


    public void addAccount(Account account) {
        userAccount = account;
    }

    public long getAccountBalance() {
        return userAccount.getBalance();
    }

    public void addAccountTransaction(long amount) {
        userAccount.addNewTransaction(amount);
    }

    public boolean checkPin(String pin) {
        return Util.checkPinCode(pin, hash);
    }


    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", cardId='" + cardId + '\'' +
                '}';
    }
}